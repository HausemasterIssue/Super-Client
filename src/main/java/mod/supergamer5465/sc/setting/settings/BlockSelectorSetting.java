package mod.supergamer5465.sc.setting.settings;

import java.awt.Color;
import java.util.*;
import java.util.Map.Entry;

import mod.supergamer5465.sc.Main;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.Setting;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockSelectorSetting extends Setting {

	public boolean colorSettings;
	public ArrayList<Block> blocks;
	public HashMap<Block, Color> colors;

	public BlockSelectorSetting(String name, Module parent, boolean colorSettings, ArrayList<Block> blocks,
			HashMap<Block, Color> colors) {
		this.name = name;
		this.parent = parent;
		this.colorSettings = colorSettings;
		if (!Main.configLoaded) {
			this.blocks = blocks;
			this.colors = colors;
		}
		for (Block b : GameRegistry.findRegistry(Block.class).getValuesCollection()) {
			colors.put(b, new Color(255, 255, 255));
		}
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}

	public HashMap<Block, Color> getColors() {
		return colors;
	}

	public void setColors(HashMap<Block, Color> colors) {
		this.colors = colors;
	}

	public Color getColor(Block b) {
		for (Entry<Block, Color> e : colors.entrySet()) {
			if (e.getKey() == b) {
				return e.getValue();
			}
		}
		return null;
	}

	public void setColor(Block b, Color c) {
		for (Entry<Block, Color> e : colors.entrySet()) {
			if (e.getKey() == b) {
				e.setValue(c);
			}
		}
	}

	/*
	 * blocks are found at
	 * GameRegistry.findRegistry(Block.class).getValuesCollection()
	 */
}
