import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class KSoundWave {

	private Clip clip;
	public KSoundWave(Object obj, String fileName, boolean flgLoop){

		try{
			if(obj == null){
				obj = this;
			}
			InputStream is = obj.getClass().getResourceAsStream(fileName);
			AudioInputStream sound = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			AudioFormat format = sound.getFormat();
			DataLine.Info di = new DataLine.Info(Clip.class, format);
			this.clip = (Clip) AudioSystem.getLine(di);
			clip.open(sound);

		}catch(UnsupportedAudioFileException ex){
			ex.printStackTrace();
			return;
		}catch(IOException ex){
			ex.printStackTrace();
			return;
		}catch(LineUnavailableException ex){
			ex.printStackTrace();
			return;
		}

	} // end KSoundWave

	public void start(){
		clip.setFramePosition(0);
		clip.start();
	}

	public void stop(){
		clip.stop();
	}

}
