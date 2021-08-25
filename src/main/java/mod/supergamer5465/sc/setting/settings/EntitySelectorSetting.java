package mod.supergamer5465.sc.setting.settings;

import java.util.ArrayList;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class EntitySelectorSetting extends Setting {

	public boolean colorSettings;
	public ArrayList<EntityEntry> entities;

	public EntitySelectorSetting(String name, Module parent, boolean colorSettings, ArrayList<EntityEntry> entities) {
		this.name = name;
		this.parent = parent;
		this.colorSettings = colorSettings;
		if (!Main.configLoaded)
			this.entities = entities;
	}

	public ArrayList<EntityEntry> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<EntityEntry> entities) {
		this.entities = entities;
	}

	// TODO for(EntityEntry e:ForgeRegistries.ENTITIES.getValues())
	// {e.getEntityClass();}
}
