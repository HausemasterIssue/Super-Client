package mod.supergamer5465.sc.ui.clickgui.components;

import mod.supergamer5465.sc.ui.clickgui.components.actions.ActionButton;

public interface ClickGuiComponent {
	public abstract String getLabel();

	public abstract ActionButton getAction();

	public abstract ClickGuiComponent getChild();

}
