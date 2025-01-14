package mod.supergamer5465.sc.util.notebot;

import java.awt.Color;
import java.util.ArrayList;

import mod.supergamer5465.sc.module.modules.utilities.NoteBot;
import mod.supergamer5465.sc.module.modules.utilities.NoteBot.MusicNote;
import net.minecraft.util.math.BlockPos;

public class NbPlayer implements Runnable {
	public static volatile boolean Running;

	public static volatile boolean Playing;
	public static volatile boolean Started;
	public static volatile NoteBot.Music PlayingMusic;

	public static volatile String LastNote;
	public static volatile Color LastNoteColor;

	private static long starttime;

	@Override
	public void run() {
		while (Running) {
			if (Playing) {
				if (PlayingMusic == null)
					continue;

				if (!Started) {
					starttime = System.currentTimeMillis();
					Started = true;
				}

				PlayingMusic.CalculateProgress(starttime, System.currentTimeMillis());

				boolean finished = true;
				for (NoteBot.MusicChannel channel : PlayingMusic.GetChannels()) {
					if (channel.IsDone())
						continue;

					finished = false;
					ArrayList<NoteBot.MusicNote> notes = channel.GetNextNotes();

					long time = System.currentTimeMillis() - starttime;

					if (time >= notes.get(0).GetTime()) {
						int notes_c = notes.size();
						for (int i = 0; i < notes_c; i++) {
							MusicNote note = notes.get(i);

							int inst = channel.GetInstrument();

							if (inst == 0) {
								channel.RemoveNextNote();
								continue;
							}

							NbInstrument instrument = NbMapper.GetInstrument(inst);

							if (instrument == null) {
								channel.RemoveNextNote();
								continue;
							}

							int pitch = note.GetNote();

							BlockPos pos = instrument.GetNotePosition(pitch);

							if (pos != null)
								NbUtil.LeftClick(pos);

							channel.RemoveNextNote();
							LastNote = NbUtil.GetRealNote(pitch);
							LastNoteColor = NbMapper.GetInstrumentColor(inst);
						}
					}
				}

				if (finished) {
					Started = false;
					Playing = false;
				}

			}

		}
	}

}