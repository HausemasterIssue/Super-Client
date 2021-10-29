package mod.imphack.module.modules.combat;

import mod.imphack.event.events.ImpHackEventPacket;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.setting.settings.FloatSetting;
import mod.imphack.setting.settings.IntSetting;
import mod.imphack.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class CrystalAura extends Module {
	
	public CrystalAura() {
		super("CrystalAura", "AutoPlaces Crystals", Category.COMBAT);

		addSetting(crystalSpeed);
		addSetting(crystalRange);
		addSetting(place);
		addSetting(playersOnly);
		addSetting(health);
		addSetting(highPing);
		addSetting(maxDamage);
		addSetting(Rotations);
	}

	final IntSetting crystalSpeed = new IntSetting("Crystal Speed", this, 1000);
	final FloatSetting crystalRange = new FloatSetting("Crystal Range", this, 4.0f);
	final FloatSetting health = new FloatSetting("AntiSuicide Health Cap", this, 4.0f);
	final FloatSetting maxDamage = new FloatSetting("Max Self Damage", this, 3.0f);
	final BooleanSetting place = new BooleanSetting("Place", this, true);
	final BooleanSetting highPing = new BooleanSetting("High Ping Optimize", this, true);
	final BooleanSetting playersOnly = new BooleanSetting("Attack Players Only", this, true);
	final BooleanSetting Rotations = new BooleanSetting("Rotations", this, true);

	private long currentMS = 0L;
	private long lastMS = -1L;
	
	//Rotations
	final CPacketPlayer cpacketp = new CPacketPlayer();
	private float nextYaw = 0;
	private float nextPitch = 0;
	private Field yaw;
	private Field pitch;
	private Field rotating;
	
	@Override
	public void onEnable() {
		try {
			this.yaw = ReflectionUtil.getField(cpacketp.getClass(), "yaw");
			this.pitch = ReflectionUtil.getField(cpacketp.getClass(), "pitch");
			this.rotating =  ReflectionUtil.getField(cpacketp.getClass(), "rotating");
			ReflectionUtil.makeAccessible(yaw);
			ReflectionUtil.makeAccessible(pitch);
			ReflectionUtil.makeAccessible(rotating);
		} catch (NoSuchFieldException e) {
			System.err.println("[ERROR] Couldn't find field" + e.getCause());
			e.printStackTrace();
		}
	}

	@Override
	public void onUpdate() {

		if (mc.player.getHealth() < health.getValue()) {
			mc.player.stopActiveHand();
		}

		float selfDamage = calculateDamage(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), mc.player);
		if (selfDamage > maxDamage.getValue()) {
			mc.player.stopActiveHand();
		}

		currentMS = System.nanoTime() / 1000000;
		if (hasDelayRun(1000 / crystalSpeed.getValue())) {
			for (Entity e : mc.world.loadedEntityList) {
				if (mc.player.getDistance(e) < crystalRange.getValue()) {
					if (e instanceof EntityEnderCrystal) {
						Minecraft.getMinecraft().playerController.attackEntity(mc.player, e);
						mc.player.swingArm(EnumHand.MAIN_HAND);
						lastMS = System.nanoTime() / 1000000;
						return;
					}
				}
			}
			if (place.enabled && mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {

				ArrayList<Entity> attackEntityList = new ArrayList <>();

				for (Entity e : mc.world.loadedEntityList) {
					if (e instanceof EntityPlayer && e != mc.player && playersOnly.enabled) {
						attackEntityList.add(e);
					}
					if (e != mc.player && !playersOnly.enabled) {
						attackEntityList.add(e);
					}
				}

				Entity minEntity = null;
				float minDistance = 100f;

				for (Entity e : attackEntityList) {
					assert mc.player != null;
					if (mc.player.getDistance(e) < minDistance) {
						minEntity = e;
						minDistance = mc.player.getDistance(e);
					}
				}

				if (minEntity != null && mc.player.getDistance(minEntity) < crystalRange.getValue()) {
					for (int i = -5; i <= 5; i++) {
						for (int j = -5; j <= 5; j++) {
							if (mc.world.getBlockState(minEntity.getPosition().add(i, 0, j)).getBlock()
									.equals(Blocks.AIR)
									&& (mc.world.getBlockState(minEntity.getPosition().add(i, -1, j)).getBlock()
											.equals(Blocks.OBSIDIAN)
											|| mc.world.getBlockState(minEntity.getPosition().add(i, -1, j)).getBlock()
													.equals(Blocks.BEDROCK))) {
								if(Rotations.isEnabled())
									rotateToBlock(minEntity.getPosition().add(i, -1, j));
									
								mc.playerController.processRightClickBlock(mc.player, mc.world,
										minEntity.getPosition().add(i, -1, j), EnumFacing.UP, mc.objectMouseOver.hitVec,
										EnumHand.MAIN_HAND);
							}
						}
					}
				}

				if (highPing.isEnabled()) {
					mc.world.removeAllEntities();
					mc.world.getLoadedEntityList();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPacketSend(ImpHackEventPacket.SendPacket e) {
		if(Rotations.isEnabled()) {
			try {
				yaw.setFloat(cpacketp, this.nextYaw);
				pitch.setFloat(cpacketp, this.nextPitch);
				rotating.setBoolean(cpacketp, true);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				System.err.println("[ERROR] couldn't set value for rotations");
				e1.printStackTrace();
			}
		}
	}

	public static float calculateDamage(Vec3d pos, EntityLivingBase entity) {
		try {
			if (entity.getDistance(pos.x, pos.y, pos.z) > 12) {
				return 0;
			}

			double blockDensity = entity.world.getBlockDensity(pos, entity.getEntityBoundingBox());
			double power = (1.0D - (entity.getDistance(pos.x, pos.y, pos.z) / 12.0D)) * blockDensity;
			float damage = (float) ((int) ((power * power + power) / 2.0D * 7.0D * 12.0D + 1.0D));

			int difficulty = Minecraft.getMinecraft().world.getDifficulty().getId();
			damage *= (difficulty == 0 ? 0 : (difficulty == 2 ? 1 : (difficulty == 1 ? 0.5f : 1.5f)));

			return getReduction(entity, damage,
					new Explosion(Minecraft.getMinecraft().world, null, pos.x, pos.y, pos.z, 6F, false, true));
		} catch (NullPointerException e) {
			return 0;
		}
	}

	private static float getReduction(EntityLivingBase entity, float damage, Explosion explosion) {
		damage = CombatRules.getDamageAfterAbsorb(damage, (float) Minecraft.getMinecraft().player.getTotalArmorValue(),
				(float) Minecraft.getMinecraft().player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS)
						.getAttributeValue());
		damage *= (1.0F - (float) EnchantmentHelper.getEnchantmentModifierDamage(
				Minecraft.getMinecraft().player.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion))
				/ 25.0F);

		if (Minecraft.getMinecraft().player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11)))) {
			damage -= damage / 4;
		}

		return damage;
	}

	public static float calculateDamage(BlockPos pos, EntityLivingBase entity) {
		try {
			return calculateDamage(new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), entity);
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
    private void rotateToBlock(BlockPos pos) {
    	float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f));
    	
    	this.nextPitch = angle[1];
    	this.nextYaw = angle[0];
		boolean nextRotation = true;
    	mc.player.rotationYawHead = angle[0];
    }
    
    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double x = (to.x - from.x);
        double y = (to.y - from.y) * -1.0;
        double z = (to.z - from.z);
        double distance = MathHelper.sqrt(x * x + z * z);
        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(y, distance)))};
    }

	public boolean hasDelayRun(long time) {
		return (currentMS - lastMS) >= time;
	}
}