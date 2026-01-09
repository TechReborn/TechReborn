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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.clientbound.ChunkSyncPayload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

// This does not do the actual chunk loading, just keeps track of what chunks the chunk loader has loaded
public class ChunkLoaderManager extends SavedData {

	public static Codec<ChunkLoaderManager> CODEC = Codec.list(LoadedChunk.CODEC).xmap(ChunkLoaderManager::fromChunks, ChunkLoaderManager::getLoadedChunks);
	public static final SavedDataType<ChunkLoaderManager> TYPE = new SavedDataType<>("chunk_loader", ChunkLoaderManager::new, CODEC, null);

	private static TicketType CHUNK_LOADER;
	private static final String KEY = "reborncore_chunk_loader";
	private static final int RADIUS = 1;

	public static void register() {
		CHUNK_LOADER = TicketType.register("reborncore:chunk_loader", 0L, 15);
	}

	public ChunkLoaderManager() {
	}

	public static ChunkLoaderManager get(Level world) {
		ServerLevel serverWorld = (ServerLevel) world;
		return serverWorld.getDataStorage().computeIfAbsent(TYPE);
	}

	private final List<LoadedChunk> loadedChunks = new ArrayList<>();

	public static ChunkLoaderManager fromChunks(List<LoadedChunk> chunks) {
		ChunkLoaderManager chunkLoaderManager = new ChunkLoaderManager();

		chunkLoaderManager.loadedChunks.clear();

		chunkLoaderManager.loadedChunks.addAll(chunks);

		return chunkLoaderManager;
	}

	public List<LoadedChunk> getLoadedChunks() {
		return loadedChunks;
	}

	public Optional<LoadedChunk> getLoadedChunk(Level world, ChunkPos chunkPos, BlockPos chunkLoader){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.world().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.chunk().equals(chunkPos))
			.filter(loadedChunk -> loadedChunk.chunkLoader().equals(chunkLoader))
			.findFirst();
	}

	public Optional<LoadedChunk> getLoadedChunk(Level world, ChunkPos chunkPos){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.world().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.chunk().equals(chunkPos))
			.findFirst();
	}

	public List<LoadedChunk> getLoadedChunks(Level world, BlockPos chunkLoader){
		return loadedChunks.stream()
			.filter(loadedChunk -> loadedChunk.world().equals(getWorldName(world)))
			.filter(loadedChunk -> loadedChunk.chunkLoader().equals(chunkLoader))
			.collect(Collectors.toList());
	}

	public boolean isChunkLoaded(Level world, ChunkPos chunkPos, BlockPos chunkLoader){
		return getLoadedChunk(world, chunkPos, chunkLoader).isPresent();
	}

	public boolean isChunkLoaded(Level world, ChunkPos chunkPos){
		return getLoadedChunk(world, chunkPos).isPresent();
	}


	public void loadChunk(Level world, ChunkPos chunkPos, BlockPos chunkLoader, String player){
		Validate.isTrue(!isChunkLoaded(world, chunkPos, chunkLoader), "chunk is already loaded");
		LoadedChunk loadedChunk = new LoadedChunk(chunkPos, getWorldName(world), player, chunkLoader);
		loadedChunks.add(loadedChunk);

		loadChunk((ServerLevel) world, loadedChunk);

		setDirty();
	}

	public void unloadChunkLoader(Level world, BlockPos chunkLoader){
		getLoadedChunks(world, chunkLoader).forEach(loadedChunk -> unloadChunk(world, loadedChunk.chunk(), chunkLoader));
	}

	public void unloadChunk(Level world, ChunkPos chunkPos, BlockPos chunkLoader){
		Optional<LoadedChunk> optionalLoadedChunk = getLoadedChunk(world, chunkPos, chunkLoader);
		Validate.isTrue(optionalLoadedChunk.isPresent(), "chunk is not loaded");

		LoadedChunk loadedChunk = optionalLoadedChunk.get();

		loadedChunks.remove(loadedChunk);

		if(!isChunkLoaded(world, loadedChunk.chunk())){
			final ServerChunkCache serverChunkManager = ((ServerLevel) world).getChunkSource();
			serverChunkManager.removeTicketWithRadius(ChunkLoaderManager.CHUNK_LOADER, loadedChunk.chunk(), RADIUS);
		}
		setDirty();
	}

	public void onServerWorldLoad(ServerLevel world) {
		loadedChunks.forEach(loadedChunk -> loadChunk(world, loadedChunk));
	}

	public void onServerWorldTick(ServerLevel world) {
		if (!loadedChunks.isEmpty()) {
			world.resetEmptyTime();
		}
	}

	public static Identifier getWorldName(Level world){
		return world.dimension().identifier();
	}

	public static ResourceKey<Level> getDimensionRegistryKey(Level world){
		return world.dimension();
	}

	public void syncChunkLoaderToClient(ServerPlayer serverPlayerEntity, BlockPos chunkLoader){
		syncToClient(serverPlayerEntity, loadedChunks.stream().filter(loadedChunk -> loadedChunk.chunkLoader().equals(chunkLoader)).collect(Collectors.toList()));
	}

	public void syncAllToClient(ServerPlayer serverPlayerEntity) {
		syncToClient(serverPlayerEntity, loadedChunks);
	}

	public void clearClient(ServerPlayer serverPlayerEntity) {
		syncToClient(serverPlayerEntity, Collections.emptyList());
	}

	public void syncToClient(ServerPlayer serverPlayerEntity, List<LoadedChunk> chunks) {
		NetworkManager.sendToPlayer(new ChunkSyncPayload(chunks), serverPlayerEntity);
	}

	private void loadChunk(ServerLevel world, LoadedChunk loadedChunk) {
		ChunkPos chunkPos = loadedChunk.chunk();
		world.getChunkSource().addTicketWithRadius(ChunkLoaderManager.CHUNK_LOADER, chunkPos, RADIUS);
	}

	public record LoadedChunk(ChunkPos chunk, Identifier world, String player, BlockPos chunkLoader) {
		public static Codec<ChunkPos> CHUNK_POS_CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.INT.fieldOf("x").forGetter(p -> p.x()),
					Codec.INT.fieldOf("z").forGetter(p -> p.z())
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

		public static StreamCodec<ByteBuf, ChunkPos> CHUNK_POS_PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, chunkPos -> chunkPos.x(),
			ByteBufCodecs.INT, chunkPos -> chunkPos.z(),
			ChunkPos::new
		);

		public static StreamCodec<ByteBuf, LoadedChunk> PACKET_CODEC = StreamCodec.composite(
			CHUNK_POS_PACKET_CODEC, LoadedChunk::chunk,
			Identifier.STREAM_CODEC, LoadedChunk::world,
			ByteBufCodecs.STRING_UTF8, LoadedChunk::player,
			BlockPos.STREAM_CODEC, LoadedChunk::chunkLoader,
			LoadedChunk::new
		);

		public LoadedChunk {
			Validate.isTrue(!StringUtils.isBlank(player), "Player cannot be blank");
		}
	}
}
