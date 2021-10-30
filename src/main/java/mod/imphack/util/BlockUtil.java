package mod.imphack.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BlockUtil {
	static Minecraft mc = Minecraft.getMinecraft();
	public static final List blackList;
	public static final List shulkerList;

	


	public static boolean isScaffoldPos(final BlockPos pos) {
		return BlockUtil.mc.world.isAirBlock(pos)
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.TALLGRASS
				|| BlockUtil.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid;
	}

	@SuppressWarnings("deprecation")
	public static boolean isValidBlock(final BlockPos pos) {
		final Block block = BlockUtil.mc.world.getBlockState(pos).getBlock();
		return !(block instanceof BlockLiquid) && block.getMaterial(null) != Material.AIR;
	}

	public static EnumFacing getFacing(BlockPos pos) {
		for (EnumFacing facing : EnumFacing.values()) {
			RayTraceResult rayTraceResult = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX,
					BlockUtil.mc.player.posY + (double) BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ),
					new Vec3d((double) pos.getX() + 0.5 + (double) facing.getDirectionVec().getX() / 2.0,
							(double) pos.getY() + 0.5 + (double) facing.getDirectionVec().getY() / 2.0,
							(double) pos.getZ() + 0.5 + (double) facing.getDirectionVec().getZ() / 2.0),
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
	
	//BlockOverlay
	
	public static IBlockState getState(BlockPos pos)
	{
		return mc.world.getBlockState(pos);
	}
	
	public static Block getBlock(BlockPos pos)
	{
		return getState(pos).getBlock();
	}
	
	static{
		blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
		shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
				Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
		mc = Minecraft.getMinecraft();
	}
	
	public static EnumFacing getPlaceableSide(BlockPos pos){

		for (EnumFacing side : EnumFacing.values()){

			BlockPos neighbour = pos.offset(side);

			if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)){
				continue;
			}

			IBlockState blockState = mc.world.getBlockState(neighbour);
			if (!blockState.getMaterial().isReplaceable()){
				return side;
			}
		}

		return null;
	}
	
	public static boolean canBeClicked(BlockPos pos)
	{
		return getBlock(pos).canCollideCheck(getState(pos), false);
	}
	public static void faceVectorPacketInstant(Vec3d vec){
		float[] rotations = getNeededRotations(vec);

		mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0],
				rotations[1], mc.player.onGround));
	}
	private static float[] getNeededRotations(Vec3d vec){
		Vec3d eyesPos = getEyesPos();

		double diffX = vec.x - eyesPos.x;
		double diffY = vec.y - eyesPos.y;
		double diffZ = vec.z - eyesPos.z;

		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));

		return new float[]{
				mc.player.rotationYaw
						+ MathHelper.wrapDegrees(yaw - mc.player.rotationYaw),
				mc.player.rotationPitch + MathHelper
						.wrapDegrees(pitch - mc.player.rotationPitch)};
	}

	public static Vec3d getEyesPos(){
		return new Vec3d(mc.player.posX,
				mc.player.posY + mc.player.getEyeHeight(),
				mc.player.posZ);
	}
	
}
