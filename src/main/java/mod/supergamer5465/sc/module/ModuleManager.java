package mod.supergamer5465.sc.module;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import mod.supergamer5465.sc.event.events.ScEventRender;
import mod.supergamer5465.sc.module.modules.client.ClickGui;
import mod.supergamer5465.sc.module.modules.client.Hud;
import mod.supergamer5465.sc.module.modules.combat.KillAura;
import mod.supergamer5465.sc.module.modules.movement.AutoWalk;
import mod.supergamer5465.sc.module.modules.movement.Flight;
import mod.supergamer5465.sc.module.modules.movement.Jesus;
import mod.supergamer5465.sc.module.modules.movement.Speed;
import mod.supergamer5465.sc.module.modules.player.Scaffold;
import mod.supergamer5465.sc.module.modules.render.EntityTracers;
import mod.supergamer5465.sc.module.modules.render.Freecam;
import mod.supergamer5465.sc.module.modules.render.FullBright;
import mod.supergamer5465.sc.module.modules.utilities.AutoFish;
import mod.supergamer5465.sc.module.modules.utilities.NoHunger;
import mod.supergamer5465.sc.util.RenderHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {
	public static Minecraft mc = Minecraft.getMinecraft();

	public ArrayList<Module> modules;

	public ModuleManager() {
		modules = new ArrayList<Module>();
		modules.clear();

		// client
		addModule(new ClickGui());
		addModule(new Hud());

		// combat
		addModule(new KillAura());

		// movement
		addModule(new Speed());
		addModule(new Flight());
		addModule(new Jesus());
		addModule(new AutoWalk());

		// player
		addModule(new Scaffold());

		// render
		addModule(new EntityTracers());
		addModule(new FullBright());
		addModule(new Freecam());

		// utilities
		addModule(new AutoFish());
		addModule(new NoHunger());
	}

	public void addModule(Module m) {
		this.modules.add(m);
	}

	public Module getModule(String name) {
		for (Module m : this.modules) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	public List<Module> getModuleList() {
		new ModuleManager();
		return this.modules;
	}

	public List<Module> getModulesByCategory(Category c) {
		List<Module> modules = new ArrayList<Module>();

		for (Module m : this.modules) {
			if (m.getCategory() == c)
				modules.add(m);
		}
		return modules;
	}

	public void update() {
		for (Module module : modules) {
			if (module.toggled) {
				module.onUpdate();
			}
		}
	}

	public void render(RenderWorldLastEvent event) {
		mc.profiler.startSection("super client");
		mc.profiler.startSection("setup");

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.disableDepth();

		GlStateManager.glLineWidth(1f);

		Vec3d pos = get_interpolated_pos(mc.player, event.getPartialTicks());

		ScEventRender event_render = new ScEventRender(RenderHelp.INSTANCE, pos);

		event_render.reset_translation();

		mc.profiler.endSection();

		for (Module m : getModuleList()) {
			if (m.toggled) {
				mc.profiler.startSection(m.name);

				m.render(event_render);

				mc.profiler.endSection();
			}
		}

		mc.profiler.startSection("release");

		GlStateManager.glLineWidth(1f);

		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableCull();

		RenderHelp.release_gl();

		mc.profiler.endSection();
		mc.profiler.endSection();
	}

	public void render() {
		for (Module m : getModuleList()) {
			if (m.toggled) {
				m.render();
			}
		}
	}

	public Vec3d get_interpolated_pos(Entity entity, double ticks) {
		return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)
				.add(process(entity, ticks, ticks, ticks)); // x, y, z.
	}

	public Vec3d process(Entity entity, double x, double y, double z) {
		return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y,
				(entity.posZ - entity.lastTickPosZ) * z);
	}
}
