package mod.supergamer5465.sc.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.supergamer5465.sc.module.modules.render.ExtraTab;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;

@Mixin(value = { GuiPlayerTabOverlay.class })
public class ScMixinGuiPlayerTabOverlay extends Gui {
	@Redirect(method = {
			"renderPlayerlist" }, at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;", remap = false))
	public List<NetworkPlayerInfo> subListHook(List<NetworkPlayerInfo> list, int fromIndex, int toIndex) {
		return list.subList(fromIndex,
				ExtraTab.getINSTANCE().isToggled() ? Math.min(ExtraTab.getINSTANCE().size.getValue(), list.size())
						: toIndex);
	}

	@Inject(method = { "getPlayerName" }, at = { @At(value = "HEAD") }, cancellable = true)
	public void getPlayerNameHook(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> info) {
		if (ExtraTab.getINSTANCE().isToggled()) {
			info.setReturnValue(ExtraTab.getPlayerName(networkPlayerInfoIn));
		}
	}
}