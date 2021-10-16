package mod.imphack.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.zero.alpine.listener.Listenable;
import mod.imphack.Main;
import mod.imphack.container.ImpHackInventory;
import mod.imphack.event.ImpHackEventBus;
import mod.imphack.event.events.ImpHackEventRender;
import mod.imphack.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.common.MinecraftForge;

public class Module implements Listenable {

	protected Minecraft mc = Minecraft.getMinecraft();
	protected ImpHackInventory inv = new ImpHackInventory();

	public String name, description;
	public int key;
	private Category category;
	public boolean toggled;
	public List<Setting> settings = new ArrayList<Setting>();
	public List<GuiButton> buttons = new ArrayList<GuiButton>();

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
}