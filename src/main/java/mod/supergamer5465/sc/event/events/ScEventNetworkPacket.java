package mod.supergamer5465.sc.event.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.network.Packet;

public class ScEventNetworkPacket extends Cancellable {
	public Packet<?> m_Packet;

	public ScEventNetworkPacket(Packet<?> p_Packet) {
		super();
		this.m_Packet = p_Packet;
	}

	public Packet<?> GetPacket() {
		return m_Packet;
	}

	public Packet<?> getPacket() {
		return m_Packet;
	}
}
