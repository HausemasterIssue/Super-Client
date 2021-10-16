package mod.imphack.module.modules.combat;

import java.util.ArrayList;

import mod.imphack.module.Category;
import mod.imphack.module.Module;
import mod.imphack.setting.settings.BooleanSetting;
import mod.imphack.setting.settings.FloatSetting;
import mod.imphack.setting.settings.IntSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public class CrystalAura extends Module {
	public CrystalAura() {
		super("CrystalAura", "AutoPlaces Crystals", Category.COMBAT);

		addSetting(crystalSpeed);
		addSetting(crystalRange);
		addSetting(place);
		addSetting(playersOnly);
	}

	IntSetting crystalSpeed = new IntSetting("Crystal Speed", this, 1000);
	FloatSetting crystalRange = new FloatSetting("Crystal Range", this, 4.0f);
	BooleanSetting place = new BooleanSetting("Place", this, true);
	BooleanSetting playersOnly = new BooleanSetting("Attack Players Only", this, true);

	private long currentMS = 0L;
	private long lastMS = -1L;

	@Override
	public void onUpdate() {
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
			}
		}
	}

	public boolean hasDelayRun(long time) {
		return (currentMS - lastMS) >= time;
	}
}