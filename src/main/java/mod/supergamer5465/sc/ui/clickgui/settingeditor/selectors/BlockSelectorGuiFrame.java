package mod.supergamer5465.sc.ui.clickgui.settingeditor.selectors;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.setting.settings.BlockSelectorSetting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockSelectorGuiFrame {
	int x, y, width, height;

	Minecraft mc = Minecraft.getMinecraft();

	BlockSelectorSetting setting;

	GuiScreen controller;

	public BlockSelectorGuiFrame(int x, int y, BlockSelectorSetting setting, GuiScreen controller) {
		this.x = x;
		this.y = y;
		this.width = 400;
		this.setting = setting;
		this.controller = controller;

		controller.itemRender = mc.getRenderItem();

		int offsetY = 2;

		if (((BlockSelectorGuiController) controller).blocks == null)
			((BlockSelectorGuiController) controller).blocks = new ArrayList<BlockButton>();

		if (Main.settingManager.getSettingsByMod(setting.parent) != null
				&& ((BlockSelectorGuiController) controller).blocks.isEmpty()) {
			for (Block b : ForgeRegistries.BLOCKS.getValuesCollection()) {
				((BlockSelectorGuiController) controller).blocks
						.add(new BlockButton(setting.parent, setting, this.x + 2, this.y + offsetY, this, b));
				offsetY += 20;
			}
		} else {
			for (int i = 0; i < ((BlockSelectorGuiController) controller).blocks.size(); i++) {
				offsetY += 20;
			}
		}

		this.height = offsetY;
	}

	public void render(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLineWidth(5);

		GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.4f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y + height);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x + width, y);
		GL11.glEnd();

		GL11.glColor3f(0.0f, 200.0f, 255.0f);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y + height);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x + width, y);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glColor3f(1, 1, 1);

		for (BlockButton b : ((BlockSelectorGuiController) controller).blocks) {
			if (b != null && b.y + b.height >= 0 && b.y <= mc.displayHeight)
				b.draw();
		}
	}

	public void onClick(int x, int y, int button) {
		for (BlockButton b : ((BlockSelectorGuiController) controller).blocks) {
			if (b != null)
				b.onClick(x, y, button);
		}
	}
}