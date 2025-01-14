package mod.supergamer5465.sc.event;

import java.util.Arrays;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import mod.supergamer5465.sc.event.events.ScEventPacket;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;

public class ScEventHandler implements Listenable {
	public static ScEventHandler INSTANCE;

	static final float[] ticks = new float[20];

	private long last_update_tick;
	private int next_index = 0;

	@EventHandler
	private Listener<ScEventPacket.ReceivePacket> receive_event_packet = new Listener<>(event -> {
		if (event.get_packet() instanceof SPacketTimeUpdate) {
			INSTANCE.update_time();
		}
	});

	public ScEventHandler() {
		ScEventBus.EVENT_BUS.subscribe(this);

		reset_tick();
	}

	public float get_tick_rate() {
		float num_ticks = 0.0f;
		float sum_ticks = 0.0f;

		for (float tick : ticks) {
			if (tick > 0.0f) {
				sum_ticks += tick;
				num_ticks += 1.0f;
			}
		}

		return MathHelper.clamp(sum_ticks / num_ticks, 0.0f, 20.0f);
	}

	public void reset_tick() {
		this.next_index = 0;
		this.last_update_tick = -1L;

		Arrays.fill(ticks, 0.0f);
	}

	public void update_time() {
		if (this.last_update_tick != -1L) {
			float time = (float) (System.currentTimeMillis() - this.last_update_tick) / 1000.0f;
			ticks[(this.next_index % ticks.length)] = MathHelper.clamp(20.0f / time, 0.0f, 20.0f);

			this.next_index += 1;
		}

		this.last_update_tick = System.currentTimeMillis();
	}
}