package mod.supergamer5465.sc.module.modules.render;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BooleanSetting;
import mod.supergamer5465.sc.setting.settings.ColorSetting;
import mod.supergamer5465.sc.setting.settings.IntSetting;

public class EntityTracers extends Module {

	IntSetting max = new IntSetting("Maximum Tracers", this, 50);
	BooleanSetting hostile = new BooleanSetting("Hostile Mobs", this, true);
	BooleanSetting passive = new BooleanSetting("Passive Mobs", this, true);
	BooleanSetting players = new BooleanSetting("Players", this, true);
	BooleanSetting items = new BooleanSetting("Items", this, true);
	BooleanSetting other = new BooleanSetting("Other Entities", this, true);
	ColorSetting hostileColor = new ColorSetting("Hostile Color", this, 255, 255, 255);
	ColorSetting passiveColor = new ColorSetting("Passive Color", this, 255, 255, 255);
	ColorSetting playerColor = new ColorSetting("Player Color", this, 255, 255, 255);
	ColorSetting itemColor = new ColorSetting("Item Color", this, 255, 255, 255);
	ColorSetting otherColor = new ColorSetting("Other Color", this, 255, 255, 255);

	public EntityTracers() {
		super("Tracers", "Traces a line to entities", Category.RENDER);

		addSetting(hostile);
		addSetting(passive);
		addSetting(players);
		addSetting(items);
		addSetting(other);
		addSetting(hostileColor);
		addSetting(passiveColor);
		addSetting(playerColor);
		addSetting(itemColor);
		addSetting(otherColor);
	}

}
