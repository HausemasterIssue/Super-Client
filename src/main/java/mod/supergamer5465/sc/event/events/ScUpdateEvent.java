package mod.supergamer5465.sc.event.events;

import mod.supergamer5465.sc.event.ScEventCancellable;
import net.minecraft.block.BlockEventData;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.util.math.BlockPos;

public class ScUpdateEvent extends Event {
	private final EntityPlayerSP player;

	public ScUpdateEvent(EntityPlayerSP player) {
		this.player = player;
	}

	public EntityPlayerSP getPlayer() {
		return player;
	}
}
