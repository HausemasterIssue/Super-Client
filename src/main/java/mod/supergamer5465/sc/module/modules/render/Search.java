package mod.supergamer5465.sc.module.modules.render;

import java.awt.Color;
import java.util.*;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.BlockSelectorSetting;
import net.minecraft.block.Block;

public class Search extends Module {

	BlockSelectorSetting blocks = new BlockSelectorSetting("Select Blocks", this, true, new ArrayList<Block>(),
			new HashMap<Block, Color>());

	public Search() {
		super("Search", "Highlights Blocks", Category.RENDER);

		addSetting(blocks);
	}

}
