package mod.imphack.module.modules.combat;

import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.setting.settings.IntSetting;
import mod.imphack.util.BlockInteractionUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Surround extends Module {
	public Surround() {
		super("Surround", "Places Obsidian Around You", Category.COMBAT);

		addSetting(rotate);
		addSetting(hybrid);
		addSetting(triggerable);
		addSetting(center);
		addSetting(block_head);
		addSetting(tick_for_place);
		addSetting(tick_timeout);
	}

	final BooleanSetting rotate = new BooleanSetting("Rotate", this, true);
	final BooleanSetting hybrid = new BooleanSetting("Hybrid", this, true);
	final BooleanSetting triggerable = new BooleanSetting("Toggle", this, true);
	final BooleanSetting center = new BooleanSetting("Center", this, false);
	final BooleanSetting block_head = new BooleanSetting("Block Face", this, false);
	final IntSetting tick_for_place = new IntSetting("Blocks per tick", this, 2);
	final IntSetting tick_timeout = new IntSetting("Ticks til timeout", this, 20);

	private int y_level = 0;
	private int tick_runs = 0;
	private int offset_step = 0;

	private Vec3d center_block = Vec3d.ZERO;

	final Vec3d[] surround_targets = { new Vec3d(1, 0, 0), new Vec3d(0, 0, 1), new Vec3d(-1, 0, 0), new Vec3d(0, 0, -1),
			new Vec3d(1, -1, 0), new Vec3d(0, -1, 1), new Vec3d(-1, -1, 0), new Vec3d(0, -1, -1), new Vec3d(0, -1, 0) };

	final Vec3d[] surround_targets_face = { new Vec3d(1, 1, 0), new Vec3d(0, 1, 1), new Vec3d(-1, 1, 0), new Vec3d(0, 1, -1),
			new Vec3d(1, 0, 0), new Vec3d(0, 0, 1), new Vec3d(-1, 0, 0), new Vec3d(0, 0, -1), new Vec3d(1, -1, 0),
			new Vec3d(0, -1, 1), new Vec3d(-1, -1, 0), new Vec3d(0, -1, -1), new Vec3d(0, -1, 0) };

	@Override
	public void onEnable() {
		if (find_in_hotbar() == -1) {
			this.toggle();
			return;
		}

		if (mc.player != null) {

			y_level = (int) Math.round(mc.player.posY);

			center_block = get_center(mc.player.posX, mc.player.posY, mc.player.posZ);

			if (center.enabled) {
				mc.player.motionX = 0;
				mc.player.motionZ = 0;
			}
		}
	}

	@Override
	public void onUpdate() {

		if (mc.player != null) {

			if (center_block != Vec3d.ZERO && center.enabled) {

				double x_diff = Math.abs(center_block.x - mc.player.posX);
				double z_diff = Math.abs(center_block.z - mc.player.posZ);

				if (x_diff <= 0.1 && z_diff <= 0.1) {
					center_block = Vec3d.ZERO;
				} else {
					double motion_x = center_block.x - mc.player.posX;
					double motion_z = center_block.z - mc.player.posZ;

					mc.player.motionX = motion_x / 2;
					mc.player.motionZ = motion_z / 2;
				}

			}

			if ((int) Math.round(mc.player.posY) != y_level && this.hybrid.enabled) {
				this.toggle();
				return;
			}

			if (!this.triggerable.enabled && this.tick_runs >= this.tick_timeout.value) { // timeout time
				this.tick_runs = 0;
				this.toggle();
				return;
			}

			int blocks_placed = 0;

			while (blocks_placed < this.tick_for_place.value) {

				if (this.offset_step >= (block_head.enabled ? this.surround_targets_face.length
						: this.surround_targets.length)) {
					this.offset_step = 0;
					break;
				}

				BlockPos offsetPos = new BlockPos(block_head.enabled ? this.surround_targets_face[offset_step]
						: this.surround_targets[offset_step]);
				BlockPos targetPos = new BlockPos(mc.player.getPositionVector()).add(offsetPos.getX(), offsetPos.getY(),
						offsetPos.getZ());

				boolean try_to_place = mc.world.getBlockState(targetPos).getMaterial().isReplaceable();

				for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null,
						new AxisAlignedBB(targetPos))) {
					if (entity instanceof EntityItem || entity instanceof EntityXPOrb)
						continue;
					try_to_place = false;
					break;
				}

				if (try_to_place && BlockInteractionUtil.placeBlock(targetPos, find_in_hotbar(), rotate.enabled,
						rotate.enabled)) {
					blocks_placed++;
				}

				offset_step++;

			}

			this.tick_runs++;

		}
	}

	private int find_in_hotbar() {

		for (int i = 0; i < 9; ++i) {

			final ItemStack stack = mc.player.inventory.getStackInSlot(i);

			if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {

				final Block block = ((ItemBlock) stack.getItem()).getBlock();

				if (block instanceof BlockEnderChest)
					return i;

				else if (block instanceof BlockObsidian)
					return i;

			}
		}
		return -1;
	}

	public Vec3d get_center(double posX, double posY, double posZ) {
		double x = Math.floor(posX) + 0.5D;
		double y = Math.floor(posY);
		double z = Math.floor(posZ) + 0.5D;

		return new Vec3d(x, y, z);
	}

}
