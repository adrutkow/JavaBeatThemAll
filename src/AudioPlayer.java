import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AudioPlayer {

    ArrayList<AudioInputStream> sounds = new ArrayList<>();

    public AudioPlayer() throws UnsupportedAudioFileException, IOException {
        String[] filepaths = {
                ".\\src\\assets\\sounds\\coin.wav",
                ".\\src\\assets\\sounds\\redsun.wav",
        };

        for (int i = 0; i < filepaths.length; i++) {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filepaths[i]));
            sounds.add(audioInput);
        }

    }

    public void playSound(int id){



        try{
            Clip clip = AudioSystem.getClip();
            clip.open(sounds.get(id));


            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float volume = 0.3f;

            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);


            clip.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void stopSound(int id){
        try{
            Clip c = AudioSystem.getClip();
            c.stop();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

}
