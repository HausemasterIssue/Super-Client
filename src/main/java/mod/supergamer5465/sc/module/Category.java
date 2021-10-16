package mod.supergamer5465.sc.module;

public enum Category {
	COMBAT("Combat"), RENDER("Render"), MOVEMENT("Movement"), PLAYER("Player"), UTILITIES("Utilities"), HUD("Hud"),
	CLIENT("Client");

	public String name;
	public int moduleIndex;

	Category(String name) {
		this.name = name;
	}
}
