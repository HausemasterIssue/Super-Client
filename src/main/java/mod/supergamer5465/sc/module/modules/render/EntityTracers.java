package mod.supergamer5465.sc.module.modules.render;

import java.awt.Color;

import mod.supergamer5465.sc.event.events.ScEventRender;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.*;
import mod.supergamer5465.sc.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityTracers extends Module {

	IntSetting max = new IntSetting("Maximum Tracers", this, 50);
	BooleanSetting monster = new BooleanSetting("Monsters", this, true);
	BooleanSetting passive = new BooleanSetting("Passive Mobs", this, true);
	BooleanSetting players = new BooleanSetting("Players", this, true);
	BooleanSetting items = new BooleanSetting("Items", this, true);
	BooleanSetting other = new BooleanSetting("Other Entities", this, true);
	ColorSetting monsterColor = new ColorSetting("Monster Color", this, 255, 255, 255);
	ColorSetting passiveColor = new ColorSetting("Passive Color", this, 255, 255, 255);
	ColorSetting playerColor = new ColorSetting("Player Color", this, 255, 255, 255);
	ColorSetting itemColor = new ColorSetting("Item Color", this, 255, 255, 255);
	ColorSetting otherColor = new ColorSetting("Other Color", this, 255, 255, 255);
	FloatSetting width = new FloatSetting("Tracer Width", this, 1f);

	public EntityTracers() {
		super("Tracers", "Traces a line to entities", Category.RENDER);

		addSetting(monster);
		addSetting(passive);
		addSetting(players);
		addSetting(items);
		addSetting(other);
		addSetting(monsterColor);
		addSetting(passiveColor);
		addSetting(playerColor);
		addSetting(itemColor);
		addSetting(otherColor);
		addSetting(width);
	}

	@Override
	public void render(ScEventRender event) {
		if (mc.world == null)
			return;

		for (Entity entity : mc.world.loadedEntityList) {
			if (checkShouldRenderTracers(entity)) {
				final Vec3d pos = MathUtil.interpolateEntity(entity, event.get_partial_ticks()).subtract(
						mc.getRenderManager().renderPosX, mc.getRenderManager().renderPosY,
						mc.getRenderManager().renderPosZ);

				if (pos != null) {
					final boolean bobbing = mc.gameSettings.viewBobbing;
					mc.gameSettings.viewBobbing = false;
					mc.entityRenderer.setupCameraTransform(event.get_partial_ticks(), 0);
					final Vec3d forward = new Vec3d(0, 0, 1)
							.rotatePitch(-(float) Math.toRadians(Minecraft.getMinecraft().player.rotationPitch))
							.rotateYaw(-(float) Math.toRadians(Minecraft.getMinecraft().player.rotationYaw));
					RenderUtil.drawLine3D((float) forward.x, (float) forward.y + mc.player.getEyeHeight(),
							(float) forward.z, (float) pos.x, (float) pos.y, (float) pos.z, width.value,
							getColor(entity));
					mc.gameSettings.viewBobbing = bobbing;
					mc.entityRenderer.setupCameraTransform(event.get_partial_ticks(), 0);
				}
			}
		}
	}

	private boolean checkShouldRenderTracers(Entity e) {
		if (e == Minecraft.getMinecraft().player)
			return false;
		if (e instanceof EntityPlayer) {
			return players.enabled;
		}
		if ((EntityUtil.isHostileMob(e) || EntityUtil.isNeutralMob(e))) {
			return monster.enabled;
		}
		if (EntityUtil.isPassive(e)) {
			return passive.enabled;
		}
		if (e instanceof EntityItem) {
			return items.enabled;
		}
		return other.enabled;
	}

	private int getColor(Entity e) {
		if (e instanceof EntityPlayer) {
			return new Color(playerColor.red, playerColor.green, playerColor.blue, 0.5f).getRGB();
		}
		if ((EntityUtil.isHostileMob(e) || EntityUtil.isNeutralMob(e))) {
			return new Color(monsterColor.red, monsterColor.green, monsterColor.blue, 0.5F).getRGB();
		}
		if (EntityUtil.isPassive(e)) {
			return new Color(passiveColor.red, passiveColor.green, passiveColor.blue, 0.5F).getRGB();
		}
		if (e instanceof EntityItem) {
			return new Color(itemColor.red, itemColor.green, itemColor.blue, 0.5F).getRGB();
		}
		return new Color(otherColor.red, otherColor.green, otherColor.blue, 0.5F).getRGB();
	}
}
