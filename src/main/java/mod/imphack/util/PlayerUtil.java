package mod.imphack.util;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil {
	static Minecraft mc = Minecraft.getMinecraft();

	public static double getFallDistance(EntityPlayerSP player, WorldClient world) {
		for (double i = player.serverPosY; i > 0; i--) {
			if (world.getBlockState(new BlockPos(player.serverPosX, i, player.serverPosZ))
					.getBlock() instanceof BlockAir) {
				return i;
			}
		}
		return 0;
	}

	public static void PacketFacePitchAndYaw(float p_Pitch, float p_Yaw) {

		boolean l_IsSprinting = mc.player.isSprinting();

		if (l_IsSprinting != mc.player.serverSprintState) {
			if (l_IsSprinting) {
				mc.player.connection
						.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
			} else {
				mc.player.connection
						.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
			}

			mc.player.serverSprintState = l_IsSprinting;
		}

		boolean l_IsSneaking = mc.player.isSneaking();

		if (l_IsSneaking != mc.player.serverSneakState) {
			if (l_IsSneaking) {
				mc.player.connection
						.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			} else {
				mc.player.connection
						.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
			}

			mc.player.serverSneakState = l_IsSneaking;
		}

		if (PlayerUtil.isCurrentViewEntity()) {
			float l_Pitch = p_Pitch;
			float l_Yaw = p_Yaw;

			AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
			double l_PosXDifference = mc.player.posX - mc.player.lastReportedPosX;
			double l_PosYDifference = axisalignedbb.minY - mc.player.lastReportedPosY;
			double l_PosZDifference = mc.player.posZ - mc.player.lastReportedPosZ;
			double l_YawDifference = (double) (l_Yaw - mc.player.lastReportedYaw);
			double l_RotationDifference = (double) (l_Pitch - mc.player.lastReportedPitch);
			mc.player.positionUpdateTicks = mc.player.positionUpdateTicks + 1;
			boolean l_MovedXYZ = l_PosXDifference * l_PosXDifference + l_PosYDifference * l_PosYDifference
					+ l_PosZDifference * l_PosZDifference > 9.0E-4D || mc.player.positionUpdateTicks >= 20;
			boolean l_MovedRotation = l_YawDifference != 0.0D || l_RotationDifference != 0.0D;

			if (mc.player.isRiding()) {
				mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D,
						mc.player.motionZ, l_Yaw, l_Pitch, mc.player.onGround));
				l_MovedXYZ = false;
			} else if (l_MovedXYZ && l_MovedRotation) {
				mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, axisalignedbb.minY,
						mc.player.posZ, l_Yaw, l_Pitch, mc.player.onGround));
			} else if (l_MovedXYZ) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, axisalignedbb.minY,
						mc.player.posZ, mc.player.onGround));
			} else if (l_MovedRotation) {
				mc.player.connection.sendPacket(new CPacketPlayer.Rotation(l_Yaw, l_Pitch, mc.player.onGround));
			} else if (mc.player.prevOnGround != mc.player.onGround) {
				mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
			}

			if (l_MovedXYZ) {
				mc.player.lastReportedPosX = mc.player.posX;
				mc.player.lastReportedPosY = axisalignedbb.minY;
				mc.player.lastReportedPosZ = mc.player.posZ;
				mc.player.positionUpdateTicks = 0;
			}

			if (l_MovedRotation) {
				mc.player.lastReportedYaw = l_Yaw;
				mc.player.lastReportedPitch = l_Pitch;
			}

			mc.player.prevOnGround = mc.player.onGround;
			mc.player.autoJumpEnabled = mc.gameSettings.autoJump;
		}
	}

	public static boolean isCurrentViewEntity() {
		return mc.getRenderViewEntity() == mc.player;
	}

	public static void lookAtPos(BlockPos pos) {
		float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()),
				new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
		setPlayerRotations(angle[0], angle[1]);
	}

	public static void setPlayerRotations(float yaw, float pitch) {
		mc.player.rotationYaw = yaw;
		mc.player.rotationYawHead = yaw;
		mc.player.rotationPitch = pitch;
	}

	public static boolean IsEating() {
		return mc.player != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemFood
				&& mc.player.isHandActive();
	}
}
