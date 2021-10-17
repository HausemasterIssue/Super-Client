package mod.imphack.module.modules.combat;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.setting.settings.FloatSetting;
import mod.imphack.setting.settings.IntSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

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
	}

	IntSetting crystalSpeed = new IntSetting("Crystal Speed", this, 1000);
	FloatSetting crystalRange = new FloatSetting("Crystal Range", this, 4.0f);
	FloatSetting health = new FloatSetting("AntiSuicide Health Cap", this, 4.0f);
	FloatSetting maxDamage = new FloatSetting("Max Self Damage", this, 3.0f);
	BooleanSetting place = new BooleanSetting("Place", this, true);
	BooleanSetting highPing = new BooleanSetting("High Ping Optimize", this, true);
	BooleanSetting playersOnly = new BooleanSetting("Attack Players Only", this, true);

	private long currentMS = 0L;
	private long lastMS = -1L;

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
		if (hasDelayRun((long) (1000 / crystalSpeed.getValue()))) {
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

				ArrayList<Entity> attackEntityList = new ArrayList<Entity>();

				for (Entity e : mc.world.loadedEntityList) {
					if (e instanceof EntityPlayer && e != mc.player && playersOnly.enabled) {
						attackEntityList.add(e);
					}
					if (e != mc.player && !playersOnly.enabled) {
						attackEntityList.add(e);
					}
				}

				Entity minEntity = null;
				Float minDistance = 100f;

				for (Entity e : attackEntityList) {
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

			return getReduction(entity, damage, new Explosion(Minecraft.getMinecraft().world, null, pos.x, pos.y, pos.z, 6F, false, true));
		} catch (NullPointerException e) {
			return 0;
		}
	}

	private static float getReduction(EntityLivingBase entity, float damage, Explosion explosion) {
		damage = CombatRules.getDamageAfterAbsorb(damage, (float) Minecraft.getMinecraft().player.getTotalArmorValue(), (float) Minecraft.getMinecraft().player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
		damage *= (1.0F - (float) EnchantmentHelper.getEnchantmentModifierDamage(Minecraft.getMinecraft().player.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)) / 25.0F);

		if (Minecraft.getMinecraft().player.isPotionActive(Potion.getPotionById(11))) {
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

	public boolean hasDelayRun(long time) {
		return (currentMS - lastMS) >= time;
	}
}