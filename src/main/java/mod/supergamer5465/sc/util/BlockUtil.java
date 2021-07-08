package mod.supergamer5465.sc.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class BlockUtil {
	static Minecraft mc = Minecraft.getMinecraft();

	public static boolean isScaffoldPos(final BlockPos pos) {
		return BlockUtil.mc.world.isAirBlock(pos)
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.TALLGRASS
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid;
	}

	public static boolean isValidBlock(final BlockPos pos) {
		final Block block = BlockUtil.mc.world.getBlockState(pos).getBlock();
		return !(block instanceof BlockLiquid) && block.getMaterial((IBlockState) null) != Material.AIR;
	}
}
