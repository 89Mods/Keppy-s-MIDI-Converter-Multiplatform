package keppy.midiConverter.resources;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import keppy.midiConverter.main.KeppysMidiConverter;

public class Sounds {
	
	public static Clip convfail;
	public static Clip convfin;
	public static Clip convstart;
	public static Clip error = null;
	
	public static void load() throws Exception {
		if(KeppysMidiConverter.muted) return;
		convfail = loadSound("/sounds/convfail.wav");
		convfin = loadSound("/sounds/convfin.wav");
		convstart = loadSound("/sounds/convstart.wav");
		error = loadSound("/sounds/error.wav");
	}
	
	private static Clip loadSound(String s) throws Exception {
		if(KeppysMidiConverter.muted) return null;
        InputStream audioSrc = Sounds.class.getResourceAsStream(s);
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
    	   AudioInputStream stream = AudioSystem.getAudioInputStream(bufferedIn);
    	    AudioFormat format = stream.getFormat();
    	    DataLine.Info info = new DataLine.Info(Clip.class, format);
    	    Clip clip = (Clip) AudioSystem.getLine(info);
    	    clip.open(stream);
    	    return clip;
	}
	
	public static void playClip(Clip clip){
		if(KeppysMidiConverter.muted) return;
		clip.stop();
		clip.setMicrosecondPosition(0);
		clip.start();
	}
	
}
