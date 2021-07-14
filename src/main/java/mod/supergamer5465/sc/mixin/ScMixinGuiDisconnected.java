package mod.supergamer5465.sc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.supergamer5465.sc.Client;
import mod.supergamer5465.sc.ui.AutoReconnectButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;

@Mixin(value = GuiDisconnected.class, priority = Integer.MAX_VALUE)
public class ScMixinGuiDisconnected extends ScMixinGuiScreen {
	@Shadow
	public int textHeight;

	private AutoReconnectButton ReconnectingButton;

	@Inject(method = "initGui", at = @At("RETURN"))
	public void initGui(CallbackInfo info) {
		/// Clear old ones :)
		buttonList.clear();

		this.buttonList.add(new GuiButton(0, this.width / 2 - 100,
				Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT, this.height - 30),
				I18n.format("gui.toMenu")));
		this.buttonList.add(new GuiButton(421, this.width / 2 - 100,
				Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT + 20, this.height - 10),
				"Reconnect"));
		this.buttonList.add(ReconnectingButton = new AutoReconnectButton(420, this.width / 2 - 100,
				Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT + 40, this.height + 10),
				"AutoReconnect"));
	}

	@Inject(method = "actionPerformed", at = @At("RETURN"))
	protected void actionPerformed(GuiButton button, CallbackInfo info) {
		if (button.id == 420) {
			ReconnectingButton.Clicked();
		} else if (button.id == 421)
			Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(null, Minecraft.getMinecraft(),
					Client.lastConnectedIP, Client.lastConnectedPort));
	}

	@Inject(method = "drawScreen", at = @At("RETURN"))
	public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
		if (buttonList.size() > 3) {
			this.buttonList.clear();
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100,
					Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT, this.height - 30),
					I18n.format("gui.toMenu")));
			this.buttonList.add(new GuiButton(421, this.width / 2 - 100, Math
					.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT + 20, this.height - 10),
					"Reconnect"));
			this.buttonList.add(ReconnectingButton = new AutoReconnectButton(420, this.width / 2 - 100, Math
					.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT + 40, this.height + 10),
					"AutoReconnect"));
		}
	}
}
