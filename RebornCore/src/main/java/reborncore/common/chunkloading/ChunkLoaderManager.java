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
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.clientbound.ChunkSyncPayload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

// This does not do the actual chunk loading, just keeps track of what chunks the chunk loader has loaded
public class ChunkLoaderManager extends PersistentState {
	public static final PersistentState.Type<ChunkLoaderManager> TYPE = new Type<>(ChunkLoaderManager::new, ChunkLoaderManager::fromTag, null);

	public static Codec<List<LoadedChunk>> CODEC = Codec.list(LoadedChunk.CODEC);

	private static final ChunkTicketType<ChunkPos> CHUNK_LOADER = ChunkTicketType.create("reborncore:chunk_loader", Comparator.comparingLong(ChunkPos::toLong));
	private static final String KEY = "reborncore_chunk_loader";
	private static final int RADIUS = 1;

	public ChunkLoaderManager() {
	}

	public static ChunkLoaderManager get(World world) {
		ServerWorld serverWorld = (ServerWorld) world;
		return serverWorld.getPersistentStateManager().getOrCreate(TYPE, KEY);
	}

	private final List<LoadedChunk> loadedChunks = new ArrayList<>();

	public static ChunkLoaderManager fromTag(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		ChunkLoaderManager chunkLoaderManager = new ChunkLoaderManager();

		chunkLoaderManager.loadedChunks.clear();

		List<LoadedChunk> chunks = CODEC.parse(NbtOps.INSTANCE, tag.getList("loadedchunks", NbtElement.COMPOUND_TYPE))
				.result()
				.orElse(Collections.emptyList());

		chunkLoaderManager.loadedChunks.addAll(chunks);

		return chunkLoaderManager;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound compoundTag, RegistryWrapper.WrapperLookup registryLookup) {
		CODEC.encodeStart(NbtOps.INSTANCE, loadedChunks)
				.result()
				.ifPresent(tag -> compoundTag.put("loadedchunks", tag));
		return compoundTag;
	}

	public Optional<LoadedChunk> getLoadedChunk(World world, ChunkPos chunkPos, BlockPos chunkLoader){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.world().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.chunk().equals(chunkPos))
			.filter(loadedChunk -> loadedChunk.chunkLoader().equals(chunkLoader))
			.findFirst();
	}

	public Optional<LoadedChunk> getLoadedChunk(World world, ChunkPos chunkPos){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.world().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.chunk().equals(chunkPos))
			.findFirst();
	}

	public List<LoadedChunk> getLoadedChunks(World world, BlockPos chunkLoader){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.world().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.chunkLoader().equals(chunkLoader))
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

		loadChunk((ServerWorld) world, loadedChunk);

		markDirty();
	}

	public void unloadChunkLoader(World world, BlockPos chunkLoader){
		getLoadedChunks(world, chunkLoader).forEach(loadedChunk -> unloadChunk(world, loadedChunk.chunk(), chunkLoader));
	}

	public void unloadChunk(World world, ChunkPos chunkPos, BlockPos chunkLoader){
		Optional<LoadedChunk> optionalLoadedChunk = getLoadedChunk(world, chunkPos, chunkLoader);
		Validate.isTrue(optionalLoadedChunk.isPresent(), "chunk is not loaded");

		LoadedChunk loadedChunk = optionalLoadedChunk.get();

		loadedChunks.remove(loadedChunk);

		if(!isChunkLoaded(world, loadedChunk.chunk())){
			final ServerChunkManager serverChunkManager = ((ServerWorld) world).getChunkManager();
			serverChunkManager.removeTicket(ChunkLoaderManager.CHUNK_LOADER, loadedChunk.chunk(), RADIUS, loadedChunk.chunk());
		}
		markDirty();
	}

	public void onServerWorldLoad(ServerWorld world) {
		loadedChunks.forEach(loadedChunk -> loadChunk(world, loadedChunk));
	}

	public void onServerWorldTick(ServerWorld world) {
		if (!loadedChunks.isEmpty()) {
			world.resetIdleTimeout();
		}
	}

	public static Identifier getWorldName(World world){
		return world.getRegistryKey().getValue();
	}

	public static RegistryKey<World> getDimensionRegistryKey(World world){
		return world.getRegistryKey();
	}

	public void syncChunkLoaderToClient(ServerPlayerEntity serverPlayerEntity, BlockPos chunkLoader){
		syncToClient(serverPlayerEntity, loadedChunks.stream().filter(loadedChunk -> loadedChunk.chunkLoader().equals(chunkLoader)).collect(Collectors.toList()));
	}

	public void syncAllToClient(ServerPlayerEntity serverPlayerEntity) {
		syncToClient(serverPlayerEntity, loadedChunks);
	}

	public void clearClient(ServerPlayerEntity serverPlayerEntity) {
		syncToClient(serverPlayerEntity, Collections.emptyList());
	}

	public void syncToClient(ServerPlayerEntity serverPlayerEntity, List<LoadedChunk> chunks) {
		NetworkManager.sendToPlayer(new ChunkSyncPayload(chunks), serverPlayerEntity);
	}

	private void loadChunk(ServerWorld world, LoadedChunk loadedChunk) {
		ChunkPos chunkPos = loadedChunk.chunk();
		world.getChunkManager().addTicket(ChunkLoaderManager.CHUNK_LOADER, chunkPos, RADIUS, chunkPos);
	}

	public record LoadedChunk(ChunkPos chunk, Identifier world, String player, BlockPos chunkLoader) {
		public static Codec<ChunkPos> CHUNK_POS_CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.INT.fieldOf("x").forGetter(p -> p.x),
					Codec.INT.fieldOf("z").forGetter(p -> p.z)
				)
				.apply(instance, ChunkPos::new));

		public static Codec<LoadedChunk> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					CHUNK_POS_CODEC.fieldOf("chunk").forGetter(LoadedChunk::chunk),
					Identifier.CODEC.fieldOf("world").forGetter(LoadedChunk::world),
					Codec.STRING.fieldOf("player").forGetter(LoadedChunk::player),
					BlockPos.CODEC.fieldOf("chunkLoader").forGetter(LoadedChunk::chunkLoader)
				)
				.apply(instance, LoadedChunk::new));

		public static PacketCodec<ByteBuf, ChunkPos> CHUNK_POS_PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, chunkPos -> chunkPos.x,
			PacketCodecs.INTEGER, chunkPos -> chunkPos.z,
			ChunkPos::new
		);

		public static PacketCodec<ByteBuf, LoadedChunk> PACKET_CODEC = PacketCodec.tuple(
			CHUNK_POS_PACKET_CODEC, LoadedChunk::chunk,
			Identifier.PACKET_CODEC, LoadedChunk::world,
			PacketCodecs.STRING, LoadedChunk::player,
			BlockPos.PACKET_CODEC, LoadedChunk::chunkLoader,
			LoadedChunk::new
		);

		public LoadedChunk {
			Validate.isTrue(!StringUtils.isBlank(player), "Player cannot be blank");
		}
	}
}
