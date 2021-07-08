package mod.supergamer5465.sc.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.zero.alpine.listener.Listenable;
import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.container.ScInventory;
import mod.supergamer5465.sc.event.ScEventBus;
import mod.supergamer5465.sc.event.events.ScEventRender;
import mod.supergamer5465.sc.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module implements Listenable {

	protected Minecraft mc = Minecraft.getMinecraft();
	protected ScInventory inv = new ScInventory();

	public String name, description;
	public int key;
	private Category category;
	public boolean toggled;
	public List<Setting> settings = new ArrayList<Setting>();

	public Module(String name, String description, Category category) {
		super();
		this.name = name;
		this.description = description;
		this.key = 0;
		this.category = category;
		this.toggled = false;
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

		ScEventBus.EVENT_BUS.subscribe(this);

		Main.config.Save();
	}

	protected void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);

		ScEventBus.EVENT_BUS.unsubscribe(this);

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

	public void render(ScEventRender event) {
		// 3d
	}

	public void render() {
		// 2d
	}

	public void onKeyPress() {
	}
}
