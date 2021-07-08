package mod.supergamer5465.sc.util;

import java.lang.reflect.Field;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

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

	private static Field serverSprintState;
	private static Field serverSneakState;
	private static Field lastReportedPosX;
	private static Field lastReportedPosY;
	private static Field lastReportedPosZ;
	private static Field lastReportedPitch;
	private static Field lastReportedYaw;
	private static Field positionUpdateTicks;
	private static Field prevOnGround;
	private static Field autoJumpEnabled;

	public static void PacketFacePitchAndYaw(float p_Pitch, float p_Yaw) {
		Class<EntityPlayerSP> entityPlayerClass = EntityPlayerSP.class;

		try {
			serverSprintState = entityPlayerClass.getDeclaredField("serverSprintState");
			serverSneakState = entityPlayerClass.getDeclaredField("serverSneakState");
			lastReportedPosX = entityPlayerClass.getDeclaredField("lastReportedPosX");
			lastReportedPosY = entityPlayerClass.getDeclaredField("lastReportedPosY");
			lastReportedPosZ = entityPlayerClass.getDeclaredField("lastReportedPosZ");
			lastReportedPitch = entityPlayerClass.getDeclaredField("lastReportedPitch");
			lastReportedYaw = entityPlayerClass.getDeclaredField("lastReportedYaw");
			positionUpdateTicks = entityPlayerClass.getDeclaredField("positionUpdateTicks");
			prevOnGround = entityPlayerClass.getDeclaredField("prevOnGround");
			autoJumpEnabled = entityPlayerClass.getDeclaredField("autoJumpEnabled");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(
					"Super Client error: no such field " + e.getMessage() + " in class EntityPlayerSP");
		}
		serverSprintState.setAccessible(true);
		serverSneakState.setAccessible(true);
		lastReportedPosX.setAccessible(true);
		lastReportedPosY.setAccessible(true);
		lastReportedPosZ.setAccessible(true);
		lastReportedPitch.setAccessible(true);
		lastReportedYaw.setAccessible(true);
		positionUpdateTicks.setAccessible(true);
		prevOnGround.setAccessible(true);
		autoJumpEnabled.setAccessible(true);

		boolean l_IsSprinting = mc.player.isSprinting();

		try {
			if (l_IsSprinting != serverSprintState.getBoolean(mc.player)) {
				if (l_IsSprinting) {
					mc.player.connection
							.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
				} else {
					mc.player.connection
							.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
				}

				serverSprintState.setBoolean(mc.player, l_IsSprinting);
			}

			boolean l_IsSneaking = mc.player.isSneaking();

			if (l_IsSneaking != serverSneakState.getBoolean(mc.player)) {
				if (l_IsSneaking) {
					mc.player.connection
							.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
				} else {
					mc.player.connection
							.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
				}

				serverSneakState.setBoolean(mc.player, l_IsSneaking);
			}

			if (PlayerUtil.isCurrentViewEntity()) {
				float l_Pitch = p_Pitch;
				float l_Yaw = p_Yaw;

				AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
				double l_PosXDifference = mc.player.posX - lastReportedPosX.getDouble(mc.player);
				double l_PosYDifference = axisalignedbb.minY - lastReportedPosY.getDouble(mc.player);
				double l_PosZDifference = mc.player.posZ - lastReportedPosZ.getDouble(mc.player);
				double l_YawDifference = (double) (l_Yaw - lastReportedYaw.getFloat(mc.player));
				double l_RotationDifference = (double) (l_Pitch - lastReportedPitch.getFloat(mc.player));
				positionUpdateTicks.set(mc.player, positionUpdateTicks.getInt(mc.player) + 1);
				boolean l_MovedXYZ = l_PosXDifference * l_PosXDifference + l_PosYDifference * l_PosYDifference
						+ l_PosZDifference * l_PosZDifference > 9.0E-4D || positionUpdateTicks.getInt(mc.player) >= 20;
				boolean l_MovedRotation = l_YawDifference != 0.0D || l_RotationDifference != 0.0D;

				if (mc.player.isRiding()) {
					mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D,
							mc.player.motionZ, l_Yaw, l_Pitch, mc.player.onGround));
					l_MovedXYZ = false;
				} else if (l_MovedXYZ && l_MovedRotation) {
					mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX,
							axisalignedbb.minY, mc.player.posZ, l_Yaw, l_Pitch, mc.player.onGround));
				} else if (l_MovedXYZ) {
					mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, axisalignedbb.minY,
							mc.player.posZ, mc.player.onGround));
				} else if (l_MovedRotation) {
					mc.player.connection.sendPacket(new CPacketPlayer.Rotation(l_Yaw, l_Pitch, mc.player.onGround));
				} else if (prevOnGround.getBoolean(mc.player) != mc.player.onGround) {
					mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
				}

				if (l_MovedXYZ) {
					lastReportedPosX.setDouble(mc.player, mc.player.posX);
					lastReportedPosY.setDouble(mc.player, axisalignedbb.minY);
					lastReportedPosZ.setDouble(mc.player, mc.player.posZ);
					positionUpdateTicks.setInt(mc.player, 0);
				}

				if (l_MovedRotation) {
					lastReportedYaw.setFloat(mc.player, l_Yaw);
					lastReportedPitch.setFloat(mc.player, l_Pitch);
				}

				prevOnGround.setBoolean(mc.player, mc.player.onGround);
				autoJumpEnabled.setBoolean(mc.player, mc.gameSettings.autoJump);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Super Client error: " + e.getMessage());
		}
	}

	public static boolean isCurrentViewEntity() {
		return mc.getRenderViewEntity() == mc.player;
	}
}
