package mod.supergamer5465.sc.ui.clickgui.settingeditor.selectors;

import net.minecraft.client.gui.GuiScreen;

public class EntitySelectorGuiController extends GuiScreen {
	public boolean colorSettings;
	public GuiScreen lastScreen;

	public EntitySelectorGuiController(GuiScreen lastScreen, boolean colorSettings) {
		this.lastScreen = lastScreen;
		this.colorSettings = colorSettings;
	}
}
