/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore.common.chunkloading;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import reborncore.common.network.ClientBoundPackets;
import reborncore.common.network.NetworkManager;

import java.util.*;
import java.util.stream.Collectors;

//This does not do the actual chunk loading, just keeps track of what chunks the chunk loader has loaded
public class ChunkLoaderManager extends PersistentState {

	public static Codec<List<LoadedChunk>> CODEC = Codec.list(LoadedChunk.CODEC);

	private static final ChunkTicketType<ChunkPos> CHUNK_LOADER = ChunkTicketType.create("reborncore:chunk_loader", Comparator.comparingLong(ChunkPos::toLong));
	private static final String KEY = "reborncore_chunk_loader";

	public ChunkLoaderManager() {
	}

	public static ChunkLoaderManager get(World world) {
		ServerWorld serverWorld = (ServerWorld) world;
		return serverWorld.getPersistentStateManager().getOrCreate(ChunkLoaderManager::fromTag, ChunkLoaderManager::new, KEY);
	}

	private final List<LoadedChunk> loadedChunks = new ArrayList<>();

	public static ChunkLoaderManager fromTag(NbtCompound tag) {
		ChunkLoaderManager chunkLoaderManager = new ChunkLoaderManager();

		chunkLoaderManager.loadedChunks.clear();

		List<LoadedChunk> chunks = CODEC.parse(NbtOps.INSTANCE, tag.getCompound("loadedchunks"))
				.result()
				.orElse(Collections.emptyList());

		chunkLoaderManager.loadedChunks.addAll(chunks);

		return chunkLoaderManager;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound compoundTag) {
		CODEC.encodeStart(NbtOps.INSTANCE, loadedChunks)
				.result()
				.ifPresent(tag -> compoundTag.put("loadedchunks", tag));
		return compoundTag;
	}

	public Optional<LoadedChunk> getLoadedChunk(World world, ChunkPos chunkPos, BlockPos chunkLoader){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.getWorld().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.getChunk().equals(chunkPos))
			.filter(loadedChunk -> loadedChunk.getChunkLoader().equals(chunkLoader))
			.findFirst();
	}

	public Optional<LoadedChunk> getLoadedChunk(World world, ChunkPos chunkPos){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.getWorld().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.getChunk().equals(chunkPos))
			.findFirst();
	}

	public List<LoadedChunk> getLoadedChunks(World world, BlockPos chunkloader){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.getWorld().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.getChunkLoader().equals(chunkloader))
			.collect(Collectors.toList());
	}

	public boolean isChunkLoaded(World world, ChunkPos chunkPos, BlockPos chunkLoader){
		return getLoadedChunk(world, chunkPos, chunkLoader).isPresent();
	}

	public boolean isChunkLoaded(World world, ChunkPos chunkPos){
		return getLoadedChunk(world, chunkPos).isPresent();
	}


	public void loadChunk(World world, ChunkPos chunkPos, BlockPos chunkLoader, String player){
		Validate.isTrue(!isChunkLoaded(world, chunkPos, chunkLoader), "chunk is already loaded");
		LoadedChunk loadedChunk = new LoadedChunk(chunkPos, getWorldName(world), player, chunkLoader);
		loadedChunks.add(loadedChunk);

		final ServerChunkManager serverChunkManager = ((ServerWorld) world).getChunkManager();
		serverChunkManager.addTicket(ChunkLoaderManager.CHUNK_LOADER, loadedChunk.getChunk(), 31, loadedChunk.getChunk());

		markDirty();
	}

	public void unloadChunkLoader(World world, BlockPos chunkLoader){
		getLoadedChunks(world, chunkLoader).forEach(loadedChunk -> unloadChunk(world, loadedChunk.getChunk(), chunkLoader));
	}

	public void unloadChunk(World world, ChunkPos chunkPos, BlockPos chunkLoader){
		Optional<LoadedChunk> optionalLoadedChunk = getLoadedChunk(world, chunkPos, chunkLoader);
		Validate.isTrue(optionalLoadedChunk.isPresent(), "chunk is not loaded");

		LoadedChunk loadedChunk = optionalLoadedChunk.get();

		loadedChunks.remove(loadedChunk);

		if(!isChunkLoaded(world, loadedChunk.getChunk())){
			final ServerChunkManager serverChunkManager = ((ServerWorld) world).getChunkManager();
			serverChunkManager.removeTicket(ChunkLoaderManager.CHUNK_LOADER, loadedChunk.getChunk(), 31, loadedChunk.getChunk());
		}
		markDirty();
	}

	public static Identifier getWorldName(World world){
		return world.getRegistryKey().getValue();
	}

	public static RegistryKey<World> getDimensionRegistryKey(World world){
		return world.getRegistryKey();
	}

	public void syncChunkLoaderToClient(ServerPlayerEntity serverPlayerEntity, BlockPos chunkLoader){
		syncToClient(serverPlayerEntity, loadedChunks.stream().filter(loadedChunk -> loadedChunk.getChunkLoader().equals(chunkLoader)).collect(Collectors.toList()));
	}

	public void syncAllToClient(ServerPlayerEntity serverPlayerEntity) {
		syncToClient(serverPlayerEntity, loadedChunks);
	}

	public void clearClient(ServerPlayerEntity serverPlayerEntity) {
		syncToClient(serverPlayerEntity, Collections.emptyList());
	}

	public void syncToClient(ServerPlayerEntity serverPlayerEntity, List<LoadedChunk> chunks) {
		NetworkManager.sendToPlayer(ClientBoundPackets.createPacketSyncLoadedChunks(chunks), serverPlayerEntity);
	}

	public static class LoadedChunk {

		public static Codec<ChunkPos> CHUNK_POS_CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
					Codec.INT.fieldOf("x").forGetter(p -> p.x),
					Codec.INT.fieldOf("z").forGetter(p -> p.z)
				)
				.apply(instance, ChunkPos::new));

		public static Codec<LoadedChunk> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
					CHUNK_POS_CODEC.fieldOf("chunk").forGetter(LoadedChunk::getChunk),
					Identifier.CODEC.fieldOf("world").forGetter(LoadedChunk::getWorld),
					Codec.STRING.fieldOf("player").forGetter(LoadedChunk::getPlayer),
					BlockPos.CODEC.fieldOf("chunkLoader").forGetter(LoadedChunk::getChunkLoader)
				)
				.apply(instance, LoadedChunk::new));

		private ChunkPos chunk;
		private Identifier world;
		private String player;
		private BlockPos chunkLoader;

		public LoadedChunk(ChunkPos chunk, Identifier world, String player, BlockPos chunkLoader) {
			this.chunk = chunk;
			this.world = world;
			this.player = player;
			this.chunkLoader = chunkLoader;
			Validate.isTrue(!StringUtils.isBlank(player), "Player cannot be blank");
		}

		public ChunkPos getChunk() {
			return chunk;
		}

		public Identifier getWorld() {
			return world;
		}

		public String getPlayer() {
			return player;
		}

		public BlockPos getChunkLoader() {
			return chunkLoader;
		}
	}
}
