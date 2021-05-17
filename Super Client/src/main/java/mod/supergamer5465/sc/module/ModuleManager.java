package mod.supergamer5465.sc.module;

import java.util.ArrayList;
import java.util.List;

import mod.supergamer5465.sc.module.modules.client.ClickGui;
import mod.supergamer5465.sc.module.modules.movement.Flight;
import mod.supergamer5465.sc.module.modules.movement.Speed;

public class ModuleManager {
	public ArrayList<Module> modules;

	public ModuleManager() {
		modules = new ArrayList<Module>();
		modules.clear();

		addModule(new ClickGui());

		// client

		// combat

		// movement
		addModule(new Speed());
		addModule(new Flight());

		// player

		// render

		// utilities
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
}
