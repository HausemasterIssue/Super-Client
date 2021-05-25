package mod.supergamer5465.sc.ui.clickgui.components.actions;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public abstract class ActionButton extends GuiButton {
	public ActionButton(int id, int x, int y, int width, int height, String text) {
		super(id, x, y, width, height, text);
	}

	public abstract void onClick(GuiScreen parent);

}
