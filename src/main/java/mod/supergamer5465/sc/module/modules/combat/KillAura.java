package mod.supergamer5465.sc.module.modules.combat;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class KillAura extends Module {

	BooleanSetting player = new BooleanSetting("Player", this, true);
	BooleanSetting hostile = new BooleanSetting("Hostile", this, true);
	BooleanSetting passive = new BooleanSetting("Passive", this, true);
	BooleanSetting tps = new BooleanSetting("Sync TPS", this, true);

	public KillAura() {
		super("KillAura", "Makes You Hit Entities", Category.COMBAT);

		this.addSetting(player);
		this.addSetting(hostile);
		this.addSetting(passive);
		this.addSetting(tps);
	}

	boolean start_verify = true;

	EnumHand actual_hand = EnumHand.MAIN_HAND;

	double tick = 0;

	@Override
	public void onEnable() {
		tick = 0;

		MinecraftForge.EVENT_BUS.register(this);

		Main.config.Save();
	}

	@Override
	public void onUpdate() {
		if (mc.player != null && mc.world != null) {

			tick++;

			if (mc.player.isDead | mc.player.getHealth() <= 0) {
				return;
			}
			if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
				start_verify = true;
			} else {
				for (int i = 0; i < 9; i++) {
					ItemStack stack = mc.player.inventory.getStackInSlot(i);
					if (stack.getItem() instanceof ItemSword) {
						mc.player.inventory.currentItem = i;
						break;
					}
				}
				start_verify = true;
			}

			Entity entity = find_entity();

			if (entity != null && start_verify) {
				// Tick.
				float tick_to_hit = 20.0f - Main.get_event_handler().get_tick_rate();

				// If possible hit or no.
				boolean is_possible_attack = mc.player.getCooledAttackStrength(tps.enabled ? -tick_to_hit : 0.0f) >= 1;// TODO
																														// test
																														// tps
																														// sync

				// To hit if able.
				if (is_possible_attack) {
					attack_entity(entity);
				}
			}

		}
	}

	public void attack_entity(Entity entity) {

		// Get actual item off hand.
		ItemStack off_hand_item = mc.player.getHeldItemOffhand();

		// If off hand not null and is some SHIELD like use.
		if (off_hand_item.getItem() == Items.SHIELD) {
			// Ignore ant continue.
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
					BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
		}

		// Start hit on entity.
		mc.player.connection.sendPacket(new CPacketUseEntity(entity));
		mc.player.swingArm(actual_hand);
		mc.player.resetCooldown();
	}

	public Entity find_entity() {
		// Create a request.
		Entity entity_requested = null;

		for (Entity e : mc.world.loadedEntityList) {
			// If entity is not null continue to next event.
			if (e != null) {
				// If is compatible.
				if (is_compatible(e)) {
					// If is possible to get.
					if (mc.player.getDistance(e) <= 5.0) {
						// Atribute the entity into entity_requested.
						entity_requested = e;
					}
				}
			}
		}

		// Return the entity requested.
		return entity_requested;
	}

	public boolean is_compatible(Entity entity) {
		// Instend entity with some type entity to continue or no.
		if (player.enabled && entity instanceof EntityPlayer) {
			if (entity != mc.player && !(entity.getName()
					.equals(mc.player.getName())) /* && WurstplusFriendManager.is_friend(entity) == false */) {
				return true;
			}
		}

		// If is hostile.
		if (hostile.enabled && (EntityUtil.isNeutralMob(entity) || EntityUtil.isHostileMob(entity))) {
			return true;
		}

		if (passive.enabled && EntityUtil.isPassive(entity)) {
			return true;
		}

		// If entity requested die.
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entity_living_base = (EntityLivingBase) entity;

			if (entity_living_base.getHealth() <= 0) {
				return false;
			}
		}

		// Return false.
		return false;
	}

}
