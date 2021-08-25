package mod.supergamer5465.sc.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BlockUtil {
	static Minecraft mc = Minecraft.getMinecraft();

	public static boolean isScaffoldPos(final BlockPos pos) {
		return BlockUtil.mc.world.isAirBlock(pos)
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.TALLGRASS
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid;
	}

	@SuppressWarnings("deprecation")
	public static boolean isValidBlock(final BlockPos pos) {
		final Block block = BlockUtil.mc.world.getBlockState(pos).getBlock();
		return !(block instanceof BlockLiquid) && block.getMaterial((IBlockState) null) != Material.AIR;
	}

	public static EnumFacing getFacing(BlockPos pos) {
		for (EnumFacing facing : EnumFacing.values()) {
			RayTraceResult rayTraceResult = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX,
					BlockUtil.mc.player.posY + (double) BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ),
					new Vec3d((double) pos.getX() + 0.5 + (double) facing.getDirectionVec().getX() * 1.0 / 2.0,
							(double) pos.getY() + 0.5 + (double) facing.getDirectionVec().getY() * 1.0 / 2.0,
							(double) pos.getZ() + 0.5 + (double) facing.getDirectionVec().getZ() * 1.0 / 2.0),
					false, true, false);
			if (rayTraceResult != null && (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK
					|| !rayTraceResult.getBlockPos().equals(pos)))
				continue;
			return facing;
		}
		if ((double) pos.getY() > BlockUtil.mc.player.posY + (double) BlockUtil.mc.player.getEyeHeight()) {
			return EnumFacing.DOWN;
		}
		return EnumFacing.UP;
	}
}
