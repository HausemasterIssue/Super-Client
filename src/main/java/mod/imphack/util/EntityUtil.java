package mod.imphack.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityUtil {

	static final Minecraft mc = Minecraft.getMinecraft();

	public static BlockPos getPlayerPosWithEntity() {
		return new BlockPos(
				(mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posX
						: EntityUtil.mc.player.posX,
				(EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posY
						: EntityUtil.mc.player.posY,
				(EntityUtil.mc.player.getRidingEntity() != null) ? EntityUtil.mc.player.getRidingEntity().posZ
						: EntityUtil.mc.player.posZ);
	}

	public static boolean isPassive(Entity e) {
		if (e instanceof EntityWolf && ((EntityWolf) e).isAngry())
			return false;
		if (e instanceof EntityAgeable || e instanceof EntityAmbientCreature || e instanceof EntitySquid)
			return true;
		return e instanceof EntityIronGolem && ((EntityIronGolem) e).getRevengeTarget() == null;
	}

	public static boolean isLiving(Entity e) {
		return e instanceof EntityLivingBase;
	}

	public static boolean isFakeLocalPlayer(Entity entity) {
		return entity != null && entity.getEntityId() == -100 && Minecraft.getMinecraft().player != entity;
	}

	public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
		return new Vec3d((entity.posX - entity.lastTickPosX) * x, 0 * y, (entity.posZ - entity.lastTickPosZ) * z);
	}

	public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
		return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
	}

	public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
		return getInterpolatedAmount(entity, ticks, ticks, ticks);
	}

	public static boolean isMobAggressive(Entity entity) {
		if (entity instanceof EntityPigZombie) {
			if (((EntityPigZombie) entity).isArmsRaised() || ((EntityPigZombie) entity).isAngry()) {
				return true;
			}
		} else if (entity instanceof EntityWolf) {
			return ((EntityWolf) entity).isAngry()
					&& !Minecraft.getMinecraft().player.equals(((EntityWolf) entity).getOwner());
		} else if (entity instanceof EntityEnderman) {
			return ((EntityEnderman) entity).isScreaming();
		}
		return isHostileMob(entity);
	}

	public static boolean isNeutralMob(Entity entity) {
		return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
	}

	public static boolean isFriendlyMob(Entity entity) {
		return (entity.isCreatureType(EnumCreatureType.CREATURE, false) && !EntityUtil.isNeutralMob(entity))
				|| (entity.isCreatureType(EnumCreatureType.AMBIENT, false)) || entity instanceof EntityVillager
				|| entity instanceof EntityIronGolem || (isNeutralMob(entity) && !EntityUtil.isMobAggressive(entity));
	}

	public static boolean isHostileMob(Entity entity) {
		return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !EntityUtil.isNeutralMob(entity));
	}
}
