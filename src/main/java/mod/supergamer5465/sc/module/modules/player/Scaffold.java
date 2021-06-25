package mod.supergamer5465.sc.module.modules.player;

import java.util.HashMap;
import java.util.Map.Entry;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import mod.supergamer5465.sc.util.world.WorldUtil;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {

	private HashMap<BlockPos, Integer> lastPlaced = new HashMap<>();

	BooleanSetting stopper = new BooleanSetting("Fall Stopper", this, true);
	IntSetting delay = new IntSetting("Delay (ms)", this, 100);
	FloatSetting blockRange = new FloatSetting("Block Range", this, 0.3f);

	public Scaffold() {
		super("Scaffold", "Places Blocks Below You", Category.PLAYER);

		addSetting(stopper);
		addSetting(delay);
		addSetting(blockRange);
	}

	private double delayTimer;
	private double towerTimer;

	@Override
	public void onUpdate() {

		if (delayTimer == -1) {
			delayTimer = System.currentTimeMillis();
			return;
		}

		if (delayTimer >= System.currentTimeMillis() - delay.value)
			return;

		if (!WorldUtil.NONSOLID_BLOCKS
				.contains(mc.world.getBlockState(new BlockPos(mc.player.getPosition().add(0, -0.85, 0))).getBlock())
				&& mc.player.movementInput.jump) {
			if (towerTimer == 0)
				towerTimer = System.currentTimeMillis();
			if (towerTimer + 1500 >= System.currentTimeMillis()) {
				mc.player.motionY = -0.28f;
				towerTimer = 0;
			} else {
				mc.player.setVelocity(0, 0.41999998688f, 0);
			}
		} // tower

		HashMap<BlockPos, Integer> tempMap = new HashMap<>();
		for (Entry<BlockPos, Integer> e : lastPlaced.entrySet()) {
			if (e.getValue() > 0)
				tempMap.put(e.getKey(), e.getValue() - 1);
		}
		lastPlaced.clear();
		lastPlaced.putAll(tempMap);

		if (!(mc.player.inventory.getCurrentItem().getItem() instanceof ItemBlock)) {
			for (int i = 0; i < 9; i++) {
				ItemStack stack = mc.player.inventory.getStackInSlot(i);
				if (stack.getItem() instanceof ItemBlock) {
					mc.player.inventory.currentItem = i;
					break;
				}
			}
		}

		double range = blockRange.value;
		for (int r = 0; r < 7; r++) {
			Vec3d r1 = new Vec3d(0, -0.85, 0);
			if (r == 1)
				r1 = r1.add(range, 0, 0);
			if (r == 2)
				r1 = r1.add(-range, 0, 0);
			if (r == 3)
				r1 = r1.add(0, 0, range);
			if (r == 4)
				r1 = r1.add(0, 0, -range);
			if (r == 5)
				r1 = r1.add(0, 0.35, 0);

			if (WorldUtil.NONSOLID_BLOCKS
					.contains(mc.world.getBlockState(new BlockPos(mc.player.getPositionVector().add(r1))).getBlock())) {
				placeBlockAuto(new BlockPos(mc.player.getPositionVector().add(r1)));
				delayTimer = System.currentTimeMillis();
				return;
			}
		}
		if (mc.player.fallDistance >= 1.3)
			mc.player.motionY = -0.28f;
	}

	public void placeBlockAuto(BlockPos block) {
		if (lastPlaced.containsKey(block))
			return;
		for (EnumFacing d : EnumFacing.values()) {
			if (!WorldUtil.NONSOLID_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
				if (WorldUtil.RIGHTCLICKABLE_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
					mc.player.connection
							.sendPacket((Packet<?>) new CPacketEntityAction(mc.player, Action.START_SNEAKING));
				}
				mc.playerController.processRightClickBlock(mc.player, mc.world, block, d, new Vec3d(block),
						EnumHand.MAIN_HAND);
				mc.world.playSound(block, SoundEvents.BLOCK_NOTE_HAT, SoundCategory.BLOCKS, 1f, 1f, false);
				if (WorldUtil.RIGHTCLICKABLE_BLOCKS.contains(mc.world.getBlockState(block.offset(d)).getBlock())) {
					mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
				}
				lastPlaced.put(block, 5);
				return;
			}
		}
	}
}
