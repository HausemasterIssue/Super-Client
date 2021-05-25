package mod.supergamer5465.sc.module.modules.client;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.ui.clickgui.ClickGuiController;

public class ClickGui extends Module {

	public ClickGui() {
		super("ClickGUI", "GUI interface to interact with modules", Category.CLIENT);
	}

	@Override
	protected void onEnable() {
		mc.displayGuiScreen(ClickGuiController.INSTANCE);
		this.toggled = false;
	}

}
