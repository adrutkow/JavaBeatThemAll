import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {

    public static float fps;


    public static void main(String[] args) throws IOException, InterruptedException{
        new Game();


        final int TICKS_PER_SECOND = 60;
        final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;

        double nextGameTickTime = System.currentTimeMillis();
        long fpsCheckTime = System.currentTimeMillis() + 1000;
        int frames = 0;
        double sleepTime;

        while (Game.currentGame.isRunning){

            sleepTime = nextGameTickTime - System.currentTimeMillis();
            Thread.sleep((long) sleepTime);

            Game.currentGame.tick();
            nextGameTickTime = System.currentTimeMillis() + SKIP_TICKS;


            frames++;
            if (System.currentTimeMillis() > fpsCheckTime){
                fpsCheckTime = System.currentTimeMillis() + 1000;
                fps = frames;
                frames = 0;
            }
        }
        JFrame f = Game.currentGame.window;
        f.dispatchEvent(new WindowEvent (f, WindowEvent.WINDOW_CLOSING));
    }
}