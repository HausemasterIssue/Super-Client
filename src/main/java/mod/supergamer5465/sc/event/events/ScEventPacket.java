package mod.supergamer5465.sc.event.events;

import mod.supergamer5465.sc.event.ScEventCancellable;
import net.minecraft.network.Packet;

public class ScEventPacket extends ScEventCancellable {
	private final Packet packet;

	public ScEventPacket(Packet packet) {
		super();

		this.packet = packet;
	}

	public Packet get_packet() {
		return this.packet;
	}

	public static class ReceivePacket extends ScEventPacket {
		public ReceivePacket(Packet packet) {
			super(packet);
		}
	}

	public static class SendPacket extends ScEventPacket {
		public SendPacket(Packet packet) {
			super(packet);
		}
	}
}