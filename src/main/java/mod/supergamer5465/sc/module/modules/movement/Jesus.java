package mod.supergamer5465.sc.module.modules.movement;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import mod.supergamer5465.sc.setting.settings.ModeSetting;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class Jesus extends Module {

	ModeSetting mode = new ModeSetting("Mode", this, "bounce", new String[] { "bounce", "packet", "spacebar" });
	FloatSetting speed = new FloatSetting("Float Speed", this, 1.0f);
	BooleanSetting dmg = new BooleanSetting("Packet Anti-FallDamage", this, false);

	public Jesus() {
		super("Jesus", "Keeps you afloat in water", Category.MOVEMENT);

		this.addSetting(mode);
		this.addSetting(speed);
		this.addSetting(dmg);
	}

	@Override
	protected void onEnable() {

		MinecraftForge.EVENT_BUS.register(this);

		ScEventBus.EVENT_BUS.subscribe(this);

		Main.config.Save();
	}

	@Override
	public void onUpdate() {
		if (mc.player.isRiding())
			return;
		if (mode.getMode().equalsIgnoreCase("bounce")) {
			if (mc.player.isInWater()) {
				mc.player.motionY = 0.03999999910593033D * speed.value;
			}
		}
		if (mode.getMode().equalsIgnoreCase("packet")) {
			if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.2f, mc.player.posZ))
					.getBlock() == Block.getBlockFromName("water") && mc.player.motionY < 0.0D) {
				mc.player.posY += -mc.player.motionY;
				mc.player.motionY = 0;
				if (dmg.enabled)
					mc.player.fallDistance = 0;
			}
			if (mc.player.isInWater()) {
				mc.player.motionY = 0.03999999910593033D * speed.value;
			}
		}
		if (mode.getMode().equalsIgnoreCase("spacebar")) {
			if (mc.player.isInWater()) {
				mc.player.motionY += 0.03999999910593033D;
			}
		}
	}
}
