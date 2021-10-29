package mod.imphack.util;

public class Timer {
	private long time;
	private long lastMS = 0L;
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

	public
	void reset() {
		this.time = System.nanoTime();
	}

	public long convertToMillis(final long time) {
		return time / 1000000L;
	}

	public long convertToNano(final long time) {
		return time * 1000000L;
	}
	public boolean isDelay(long delay) {
		if(System.currentTimeMillis() - lastMS >= delay) {
			return true;
		}
		return false;
	}
	
    public long getCurrentMS(){
		return System.nanoTime() / 1000000L;
	}
	
    public void setLastMS(long lastMS) {
		this.lastMS = lastMS;
	}
	
    public void setLastMS() {
		this.lastMS = System.currentTimeMillis();
	}
}
