package mod.supergamer5465.sc.util.notebot;

import net.minecraft.util.math.BlockPos;

public class NbNote {
	private int knownpitch;
	private int pitch;
	private boolean validated;
	private BlockPos pos;

	public NbNote(int pitch, BlockPos pos) {
		this.knownpitch = -1;
		this.pitch = pitch;
		this.pos = pos;
	}

	public void SetKnownPitch(int pitch) {
		this.knownpitch = pitch;
	}

	public int GetKnownPitch() {
		return knownpitch;
	}

	public int GetPitch() {
		return pitch;
	}

	public BlockPos GetPosition() {
		return pos;
	}

	public boolean IsValidated() {
		return validated;
	}

	public void SetValidated(boolean validated) {
		this.validated = validated;
	}
}