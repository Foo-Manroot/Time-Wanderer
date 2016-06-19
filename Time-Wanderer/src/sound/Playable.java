package sound;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.MainClass;

/**
 * @author Alberto
 * @version 26/03/2016 */
public class Playable implements Runnable {
    
    /**
     * Stores the name of the clip.
     */
    private final Playlist clipName;
    
    /**
     * The clip to be played. */
    private Clip sound;
    /**
     * The path of the sound file to be played. */
    private final String path;
    
    /**
     * The boolean flag that indicates if the sound must finish or not. */
    private final boolean finite;
    /**
     * The start frame if the sound must not finish. */
    private final int start;
    /**
     * The end frame if the sound must not finish. */
    private final int end;
    /**
     * The object that controls the volume of the clip to play. */
    private FloatControl gainControl;
    /**
     * The number of decibels to deduce from the sonorous volume */
    private final float decibels;

    /**
     * @param path - the relative path of the music file to play.
     * @param finite - indicates if the sound must finish or not.
     * @param clipName
     *              Element from the {@link Playlist} enum that says which
     *          clip is played on this object.
     */
    public Playable(String path, boolean finite, Playlist clipName) {
        Clip clip;

        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException exception) {
            System.out.println("The music clip is not available. See:");
            System.out.println(exception.getMessage());
            clip = null;
        }

        sound = clip;
        this.path = path;
        this.finite = finite;
        start = 0;
        end = -1;
        decibels = 0;
        this.clipName = clipName;
    }

    /**
     * @param path - the relative path of the music file to play.
     * @param finite - indicates if the sound must finish or not.
     * @param decibels - the number of decibels to deduct from the volume. 
     * @param clipName
     *              Element from the {@link Playlist} enum that says which
     *          clip is played on this object.
     */
    public Playable(String path, boolean finite, float decibels, Playlist clipName) {
        Clip clip;

        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException exception) {
            System.out.println("The music clip is not available. See:");
            System.out.println(exception.getMessage());
            clip = null;
        }

        sound = clip;
        this.path = path;
        this.finite = finite;
        start = 0;
        end = -1;
        this.decibels = decibels;
        this.clipName = clipName;
    }

    /**
     * @param path - the relative path of the music file to play.
     * @param start - the initial frame of the sound loop.
     * @param end - the ending frame of the sound loop.
     * @param clipName
     *              Element from the {@link Playlist} enum that says which
     *          clip is played on this object.
     */
    public Playable(String path, int start, int end, Playlist clipName) {
        Clip clip;

        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException exception) {
            System.out.println("The music clip is not available. See:");
            System.out.println(exception.getMessage());
            clip = null;
        }

        sound = clip;
        this.path = path;
        this.finite = false;
        this.start = start;
        this.end = end;
        decibels = 0;
        this.clipName = clipName;
    }

    /**
     * @param path - the relative path of the music file to play.
     * @param start - the initial frame of the sound loop.
     * @param end - the ending frame of the sound loop.
     * @param decibels - the number of decibels to deduct from the volume.
     * @param clipName
     *              Element from the {@link Playlist} enum that says which
     *          clip is played on this object.
     */
    public Playable(String path, int start, int end, float decibels, Playlist clipName) {
        Clip clip;

        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException exception) {
            System.out.println("The music clip is not available. See:");
            System.out.println(exception.getMessage());
            clip = null;
        }

        sound = clip;
        this.path = path;
        this.finite = false;
        this.start = start;
        this.end = end;
        this.decibels = decibels;
        this.clipName = clipName;
    }

    /**
     * @param decibels - the number of decibels to deduct from the volume. */
    public void reduceVolume(float decibels) {

        if (gainControl != null) {
            gainControl.setValue(-decibels);
        }

    }
    
    /**
     * @param milliseconds - the time (in milliseconds) to sleep. */
    private void sleep(int milliseconds) {

        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
            System.out.println(exception.getMessage());
        }

    }
    
    /**
     * The method which plays the selected sound. */
    @Override
    public void run() {        
        AudioInputStream audioStream;
        /* Stores the length of the audio clip and waits until it ends, so
        it can be closed properly */
        long microsecondsLength;
        //InputStream inputStream;

        if (sound == null) {
            return;
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(getClass().getResource(path));
            //inputStream = getClass().getResourceAsStream(path);
            DataLine.Info info = new DataLine.Info(Clip.class,audioStream.getFormat());
            sound = (Clip) AudioSystem.getLine(info);
            //audioStream = AudioSystem.getAudioInputStream(inputStream);
            sound.open(audioStream);
            sound.setLoopPoints(start, end);
            
            /* Gets the length of the clip */
            microsecondsLength = sound.getMicrosecondLength();

            if (decibels > 0) {
                if(sound.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    gainControl = (FloatControl)
                        sound.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-decibels);
                }
            }

            if (finite) {
                sound.start();
                
                /* Converts the microseconds into milliseconds and sleeps that 
                amount of time */
                sleep((int) (microsecondsLength / 100));
                /* Closes the clip and notifies the Jukebox */
                end();
                
            } else {
                sound.loop(Clip.LOOP_CONTINUOUSLY);
            }
            
        } catch (IOException exception) {
            System.out.println("The music clip couldn't be imported. See:");
            System.out.println(exception.getMessage());
        } catch (LineUnavailableException exception) {
            System.out.println("The music clip is not available. See:");
            System.out.println(exception.getMessage());
        } catch (UnsupportedAudioFileException exception) {
            System.out.println("The music clip format is not supported. See:");
            System.out.println(exception.getMessage());
        }
    }
    
    /**
     * Ends the playback of the sound and notifies the JUKEBOX to remove this
     * playable from its list.
     */
    public void end() {
        
        sound.stop();
        sound.close();
        /* Notifies that this clip finished its reproduction */
        MainClass.JUKEBOX.clipEnded(this);
    }
    
    /**
     * Stops the sound being played.
     */
    public synchronized void stopPlaying () {
        
        /* Stops the clip's reproduction */
        sound.stop();        
    }
    
    /**
     * Continues the reproduction of the clip.
     */
    public synchronized void restartClip () {
        
        /* Restarts the clip's reproduction */
        sound.start();
    }

    /**
     * Returns a {@link Playlist} element that determines which clip this
     * Playable is playing.
     * 
     * @return 
     */
    public Playlist getClipName () {
        
        return clipName;
    }
}
