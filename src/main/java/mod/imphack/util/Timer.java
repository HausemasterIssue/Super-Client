package mod.imphack.util;

public class Timer {
	private long time;

	public Timer() {
		this.time = -1L;
	}

	public boolean getPassedSeconds(final double s) {
		return this.getPassedMillis((long) s * 1000L);
	}

	public boolean getPassedMillis(final long ms) {
		return this.getPassedNanos(this.convertToNano(ms));
	}

	public void setMillis(final long ms) {
		this.time = System.nanoTime() - this.convertToNano(ms);
	}

	public boolean getPassedNanos(final long ns) {
		return System.nanoTime() - this.time >= ns;
	}

	public long getPassedTimeMs() {
		return this.convertToMillis(System.nanoTime() - this.time);
	}

	public Timer reset() {
		this.time = System.nanoTime();
		return this;
	}

	public long convertToMillis(final long time) {
		return time / 1000000L;
	}

	public long convertToNano(final long time) {
		return time * 1000000L;
	}
}
