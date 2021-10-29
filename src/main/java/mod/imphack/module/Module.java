package mod.imphack.module;

import me.zero.alpine.listener.Listenable;
import mod.imphack.Client;
import mod.imphack.Main;
import mod.imphack.container.ImpHackInventory;
import mod.imphack.event.ImpHackEventBus;
import mod.imphack.event.events.ImpHackEventRender;
import mod.imphack.setting.Setting;
import mod.imphack.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module implements Listenable {

	protected final Minecraft mc = Minecraft.getMinecraft();
	protected final ImpHackInventory inv = new ImpHackInventory();
	public final String name;
	public String description;
	public int key;
	private final Category category;
	public boolean toggled;
	public final List<Setting> settings = new ArrayList <>();
	public final List<GuiButton> buttons = new ArrayList <>();

	public Module(String name, String description, Category category) {
		super();
		this.name = name;
		this.description = description;
		this.key = 0;
		this.category = category;
		this.toggled = false;
	}

	public void addButton(GuiButton... buttons) {
		this.buttons.addAll(Arrays.asList(buttons));
	}

	public void addSetting(Setting... settings) {
		this.settings.addAll(Arrays.asList(settings));
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
		Main.config.Save();
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;

		if (this.toggled) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}

	public void toggle() {
		this.toggled = !this.toggled;

		if (this.toggled) {
			onEnable();
		} else {
			onDisable();
		}
	}
	protected void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);

		ImpHackEventBus.EVENT_BUS.subscribe(this);

		Main.config.Save();
	}

	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		ImpHackEventBus.EVENT_BUS.unsubscribe(this);

		Main.config.Save();
	}

	public String getName() {
		return this.name;
	}

	public Category getCategory() {
		return this.category;
	}

	public void onUpdate() {
	}

	public void render(ImpHackEventRender event) {
		// 3d
	}

	public void render() {
		// 2d
	}

	public void onKeyPress() {
	}

	public void actionPerformed(GuiButton b) {
	}

	public void onRenderWorldLast(ImpHackEventRender event) {		
	}

}