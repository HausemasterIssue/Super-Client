package mod.supergamer5465.sc.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.lwjgl.opengl.GL11;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventPacket;
import mod.supergamer5465.sc.event.events.ScEventSettings;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.SearchBlockSelectorSetting;
import mod.supergamer5465.sc.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Search extends Module {

	SearchBlockSelectorSetting blocks = new SearchBlockSelectorSetting("Select Blocks", this, true,
			new ArrayList<Block>(), new HashMap<Block, Integer>());
	BooleanSetting outline = new BooleanSetting("Outline", this, true);
	BooleanSetting tracer = new BooleanSetting("Tracer", this, true);

	public Search() {
		super("Search", "Highlights Blocks", Category.RENDER);

		addSetting(blocks);
		addSetting(tracer);

		this.to_search = new HashMap<Block, Integer>();
		this.search_lock = new ReentrantReadWriteLock();
		this.targets = new HashMap<BlockPos, Integer>();
		this.targets_lock = new ReentrantReadWriteLock();
		this.new_chunks = new ArrayList<ChunkPos>();
		this.new_chunks_lock = new ReentrantReadWriteLock();
		this.camera = new Frustum();
	}

	/* Map of Blocks to be searched */
	private ReadWriteLock search_lock;
	public Map<Block, Integer> to_search;

	/* List of blocks that have been selected for highlighting */
	private ReadWriteLock targets_lock;
	private Map<BlockPos, Integer> targets;

	/* New chunks to be search for targets */
	private ReadWriteLock new_chunks_lock;
	private List<ChunkPos> new_chunks;

	private ICamera camera;

	@Override
	public void onUpdate() {

		if (to_search == null)
			for (Block b : blocks.blocks) {

				int color;
				color = new Color(blocks.getColor(b)).getRGB();
				this.to_search.put(b, color);
			}
	}

	/*
	 * Basic module actions
	 */

	@Override
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		ScEventBus.EVENT_BUS.subscribe(this);

		Main.config.Save();

		this.resetTargets();

		for (Block b : blocks.getBlocks()) {

			int color;
			color = new Color(blocks.getColor(b)).getRGB();
			to_search.put(b, color);
		}
	}

	@Override
	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		ScEventBus.EVENT_BUS.unsubscribe(this);

		Main.config.Save();

		this.onDisconnect();
	}

	public void onDisconnect() {
		this.targets_lock.writeLock().lock();
		this.targets.clear();
		this.targets_lock.writeLock().unlock();

		this.new_chunks_lock.writeLock().lock();
		this.new_chunks.clear();
		this.new_chunks_lock.writeLock().unlock();
	}

	/*
	 * Configurations save & load
	 */

	// Remove targets located on unload chunks
	@SubscribeEvent
	public void onUnLoad(ChunkEvent.Unload event) {
		int x = event.getChunk().x, z = event.getChunk().z;

		this.targets_lock.writeLock().lock();
		Iterator<BlockPos> iterator = this.targets.keySet().iterator();
		while (iterator.hasNext()) {
			BlockPos position = iterator.next();
			if ((position.getX() >> 4) == x && (position.getZ() >> 4) == z) {
				iterator.remove();
			}
		}
		this.targets_lock.writeLock().unlock();
	}

	// Explore new chunks for new targets
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (mc.world == null)
			return;

		this.new_chunks_lock.readLock().lock();
		int size = this.new_chunks.size();
		this.new_chunks_lock.readLock().unlock();

		if (size == 0)
			return;

		Minecraft mc = Minecraft.getMinecraft();
		if (mc.world == null)
			return;
		IChunkProvider provider = mc.world.getChunkProvider();
		if (provider == null)
			return;

		this.new_chunks_lock.writeLock().lock();
		Iterator<ChunkPos> iterator = this.new_chunks.iterator();
		while (iterator.hasNext()) {
			ChunkPos position = (ChunkPos) iterator.next();
			Chunk c = provider.getLoadedChunk(position.x, position.z);
			if (c != null) {
				this.searchChunk(c);
				iterator.remove();
			}
		}
		this.new_chunks_lock.writeLock().unlock();
	}

	@EventHandler
	private Listener<ScEventSettings> SettingEvent = new Listener<>(event -> {
		this.onDisable();
		this.onEnable();
	});

	/*
	 * Network handler
	 */

	@EventHandler
	private Listener<ScEventPacket.ReceivePacket> PacketRecieveEvent = new Listener<>(event -> {
		if (event.get_packet() instanceof SPacketChunkData) { // New chunk
			SPacketChunkData chunk = (SPacketChunkData) event.get_packet();
			this.new_chunks_lock.writeLock().lock();
			this.new_chunks.add(new ChunkPos(chunk.getChunkX(), chunk.getChunkZ()));
			this.new_chunks_lock.writeLock().unlock();
		} else if (event.get_packet() instanceof SPacketUpdateTileEntity) { // New tile entity
			SPacketUpdateTileEntity updt = (SPacketUpdateTileEntity) event.get_packet();

			int color = this.isTargeted(Minecraft.getMinecraft().world.getBlockState(updt.getPos()), updt.getPos(),
					updt.getNbtCompound());

			this.targets_lock.writeLock().lock();
			if (color != 0) {
				this.targets.put(updt.getPos(), color);
			} else
				this.targets.remove(updt.getPos());
			this.targets_lock.writeLock().unlock();

		} else if (event.get_packet() instanceof SPacketBlockChange) { // New Block
			SPacketBlockChange change = (SPacketBlockChange) event.get_packet();

			int color = this.isTargeted(change.getBlockState(), change.getBlockPosition(), null);

			this.targets_lock.writeLock().lock();
			if (color != 0) {
				this.targets.put(change.getBlockPosition(), color);

			} else
				this.targets.remove(change.getBlockPosition());
			this.targets_lock.writeLock().unlock();
		} else if (event.get_packet() instanceof SPacketMultiBlockChange) { // New Blocks
			SPacketMultiBlockChange change = (SPacketMultiBlockChange) event.get_packet();
			for (SPacketMultiBlockChange.BlockUpdateData up : change.getChangedBlocks()) {

				int color = this.isTargeted(up.getBlockState(), up.getPos(), null);

				this.targets_lock.writeLock().lock();
				if (color != 0) {
					this.targets.put(up.getPos(), color);
				} else
					this.targets.remove(up.getPos());
				this.targets_lock.writeLock().unlock();
			}
		}
	});

	/*
	 * Render function, highlight targets with right color + Draw tracers
	 */

	@SubscribeEvent
	public void onRender(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		RenderManager renderManager = mc.getRenderManager();

		if (renderManager == null || renderManager.options == null)
			return;

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.disableDepth();

		GlStateManager.pushMatrix();
		GlStateManager.depthMask(false);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glLineWidth(1.5f);

		List<Target> tracers = new LinkedList<Target>();

		this.targets_lock.readLock().lock();
		for (BlockPos position : this.targets.keySet()) {

			AxisAlignedBB bb = mc.world.getBlockState(position).getSelectedBoundingBox(mc.world, position)
					.grow(0.0020000000949949026D)
					.offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);

			int color = this.targets.get(position);
			tracers.add(new Target(position, color));

			this.camera.setPosition(0d, 0d, 0d);
			if (camera.isBoundingBoxInFrustum(bb)) {

				float red = ((float) ((color >> 16) & 255)) / 255f;
				float blue = ((float) ((color >> 8) & 255)) / 255f;
				float green = ((float) (color & 255)) / 255f;

				RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, red, blue, green,
						0.65f);
				RenderGlobal.renderFilledBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, red, blue, green,
						0.25f);
			}
		}
		this.targets_lock.readLock().unlock();

		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();

		final boolean bobbing = mc.gameSettings.viewBobbing;
		mc.gameSettings.viewBobbing = false;
		mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

		for (Target t : tracers) {

			Vec3d pos = new Vec3d(t.position).add(0.5, 0.5, 0.5).subtract(renderManager.viewerPosX,
					renderManager.viewerPosY, renderManager.viewerPosZ);
			Vec3d forward = new Vec3d(0, 0, 1)
					.rotatePitch(-(float) Math.toRadians(mc.getRenderViewEntity().rotationPitch))
					.rotateYaw(-(float) Math.toRadians(mc.getRenderViewEntity().rotationYaw));
			if (this.tracer.isEnabled())
				RenderUtil.drawLine3D((float) forward.x, (float) forward.y + mc.getRenderViewEntity().getEyeHeight(),
						(float) forward.z, (float) pos.x, (float) pos.y, (float) pos.z, 0.85f, t.color);
		}

		mc.gameSettings.viewBobbing = bobbing;
		mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

		GlStateManager.glLineWidth(1f);
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableCull();
	}

	/*
	 * Global Search functions, used to reset targets data for all loaded chunks in
	 * our field of view
	 */

	// Clear all targets, renew targets for every chunks in render distance
	public void resetTargets() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.world == null)
			return;

		this.targets_lock.writeLock().lock();
		this.targets.clear();
		this.targets_lock.writeLock().unlock();

		int x = (int) mc.getRenderViewEntity().posX >> 4, z = (int) mc.getRenderViewEntity().posZ >> 4;
		for (int i = x - mc.gameSettings.renderDistanceChunks; i <= x + mc.gameSettings.renderDistanceChunks; i++) {
			for (int j = z - mc.gameSettings.renderDistanceChunks; j <= z + mc.gameSettings.renderDistanceChunks; j++) {
				Chunk c = mc.world.getChunkProvider().getLoadedChunk(i, j);
				if (c != null) {
					this.searchChunk(c);
				}
			}
		}
	}

	// Search a chunk for targets
	private void searchChunk(Chunk chunk) {
		int m = -1;
		for (ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
			m++;
			if (storage == null)
				continue;
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					for (int k = 0; k < 16; k++) {
						BlockPos position = new BlockPos((chunk.x << 4) + i, (m << 4) + j, (chunk.z << 4) + k);
						int color = this.isTargeted(storage.get(i, j, k), position, null);
						if (blocks.blocks.contains(mc.world.getBlockState(position).getBlock())) {
							this.targets_lock.writeLock().lock();

							this.targets.put(position, color);
							this.targets_lock.writeLock().unlock();
						}
					}
				}
			}
		}
	}

	// Remove targets that were registered with given property
	public void ClearFromTargets(int color) {
		this.targets_lock.readLock().lock();
		Iterator<BlockPos> i = this.targets.keySet().iterator();
		while (i.hasNext()) {
			BlockPos position = (BlockPos) i.next();
			if (this.targets.get(position) == color)
				i.remove();
		}
		this.targets_lock.readLock().unlock();
	}

	// Which color to use for highlighting given blockstate, null if none
	public int isTargeted(IBlockState state, BlockPos position, NBTTagCompound updt_tag) {
		this.search_lock.readLock().lock();

		int color = 0;

		if (state.getBlock() != null && this.to_search != null)
			if (this.to_search.get(state.getBlock()) != null) {
				color = new Color(blocks.colors.get(state.getBlock())).getRGB();
			} else {
				this.search_lock.readLock().unlock();
				return 0;
			}

		this.search_lock.readLock().unlock();

		return color;
	}

	// A target to be highlighted, located at position
	private static class Target {
		public int color;
		public BlockPos position;

		public Target(BlockPos position, int color) {
			this.position = position;
			this.color = color;
		}
	}

	public boolean containsTag(NBTTagCompound tag, NBTTagCompound sub) {
		for (String key : sub.getKeySet()) {
			if (tag.getTagId(key) != sub.getTagId(key))
				return false;
			if (sub.getTagId(key) == 10) { // NBTTagCompound
				if (!this.containsTag(tag.getCompoundTag(key), sub.getCompoundTag(key)))
					return false;
			} else {
				if (!tag.getTag(key).equals(sub.getTag(key)))
					return false;
			}
		}
		return true;
	}
}
