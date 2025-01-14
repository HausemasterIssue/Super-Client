package mod.supergamer5465.sc.module.modules.render;

import mod.supergamer5465.sc.module.Category;
import mod.supergamer5465.sc.module.Module;
import mod.supergamer5465.sc.setting.settings.IntSetting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class ExtraTab extends Module {
	public IntSetting size = new IntSetting("Size", this, 1000);

	public ExtraTab() {
		super("ExtraTab", "Extends Tab Menu", Category.RENDER);

		addSetting(size);
	}

	public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
		String name = networkPlayerInfoIn.getDisplayName() != null
				? networkPlayerInfoIn.getDisplayName().getFormattedText()
				: ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(),
						networkPlayerInfoIn.getGameProfile().getName());
		return name;
	}
}