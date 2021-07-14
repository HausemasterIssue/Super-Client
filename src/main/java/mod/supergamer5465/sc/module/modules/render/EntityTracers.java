package mod.supergamer5465.sc.module.modules.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mod.supergamer5465.sc.event.events.ScEventRender;
import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.ColorSetting;
import mod.supergamer5465.sc.setting.settings.FloatSetting;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import mod.supergamer5465.sc.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EntityTracers extends Module {

	private final ArrayList<Entity> entities = new ArrayList<>();

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

		GlStateManager.pushMatrix();

		GL11.glLineWidth(width.value);

		entities.clear();
		entities.addAll(mc.world.loadedEntityList);

		GL11.glColor3f(1, 1, 1);
		GL11.glColor4f(1, 1, 1, 1);

		drawLines(event);

		GL11.glColor3f(1, 1, 1);
		GL11.glColor4f(1, 1, 1, 1);

		GlStateManager.popMatrix();
	}

	private void drawLines(ScEventRender event) {

		final Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch))
				.rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));

		for (Entity e : entities) {
			if (e != mc.player) {
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

				final double posX = this.interpolate(e.posX, e.lastTickPosX) - mc.getRenderManager().viewerPosX;
				final double posY = this.interpolate(e.posY, e.lastTickPosY) - mc.getRenderManager().viewerPosY;
				final double posZ = this.interpolate(e.posZ, e.lastTickPosZ) - mc.getRenderManager().viewerPosZ;

				if (checkShouldRenderTracers(e)) {
					GL11.glBegin(1);

					GL11.glVertex3d(eyes.x, eyes.y + mc.player.getEyeHeight(), eyes.z);
					GL11.glVertex3d(posX, posY, posZ);
					GL11.glVertex3d(posX, posY, posZ);

					GL11.glEnd();
				}
			}
		}
	}

	private double interpolate(final double now, final double then) {
		return then + (now - then) * mc.getRenderPartialTicks();
	}

	private boolean checkShouldRenderTracers(Entity e) {
		if (e == Minecraft.getMinecraft().player)
			return false;
		if (e instanceof EntityPlayer) {
			GL11.glColor4f(playerColor.red / 255, playerColor.green / 255, playerColor.blue / 255, 0.5F);
			return players.enabled;
		}
		if ((EntityUtil.isHostileMob(e) || EntityUtil.isNeutralMob(e))) {
			GL11.glColor4f(monsterColor.red / 255, monsterColor.green / 255, monsterColor.blue / 255, 0.5F);
			return monster.enabled;
		}
		if (EntityUtil.isPassive(e)) {
			GL11.glColor4f(passiveColor.red / 255, passiveColor.green / 255, passiveColor.blue / 255, 0.5F);
			return passive.enabled;
		}
		if (e instanceof EntityItem) {
			GL11.glColor4f(itemColor.red / 255, itemColor.green / 255, itemColor.blue / 255, 0.5F);
			return items.enabled;
		}
		GL11.glColor4f(otherColor.red / 255, otherColor.green / 255, otherColor.blue / 255, 0.5F);
		return other.enabled;
	}
}
