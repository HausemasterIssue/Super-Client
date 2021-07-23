package mod.supergamer5465.sc.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.modules.render.ExtraTab;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

@Mixin(value = { GuiPlayerTabOverlay.class })
public class ScMixinGuiPlayerTabOverlay extends Gui {
	@Redirect(method = {
			"renderPlayerlist" }, at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;", remap = false))
	public List<NetworkPlayerInfo> subListHook(List<NetworkPlayerInfo> list, int fromIndex, int toIndex) {
		return list
				.subList(fromIndex,
						Main.moduleManager.getModule("ExtraTab").isToggled() ? Math.min(
								(((IntSetting) Main.settingManager
										.getSettingByName(Main.moduleManager.getModule("ExtraTab"), "Size")).value),
								list.size()) : toIndex);
	}

	@Inject(method = { "getPlayerName" }, at = { @At(value = "HEAD") }, cancellable = true)
	public void getPlayerNameHook(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> info) {
		if (Main.moduleManager.getModule("ExtraTab").isToggled()) {
			info.setReturnValue(ExtraTab.getPlayerName(networkPlayerInfoIn));
		}
	}
}