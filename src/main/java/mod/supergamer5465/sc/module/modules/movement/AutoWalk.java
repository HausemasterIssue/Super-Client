package mod.supergamer5465.sc.module.modules.movement;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;

public class AutoWalk extends Module {
	public AutoWalk() {
		super("AutoWalk", "Walk Automatically", Category.MOVEMENT);
	}

	@Override
	public void onUpdate() {
		mc.player.moveForward = 4.3f;
	}
}
