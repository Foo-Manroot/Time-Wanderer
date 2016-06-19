/* This class represents a jukebox which plays sounds related with magical
 * effects, soundtrack, collisions... For more information, all the kinds of
 * sound are specified at Playlist. */
package sound;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Alberto
 * @version 26/03/2016 */
public class Jukebox {
    
    /**
     * This attribute stores the current state of the music (ON or OFF).
     * If it's true, the music is playing.
     */
    private boolean musicON = true;
    
    /**
     * This attribute stores the current state of the sound effects (ON or OFF).
     * If it's true, the effects are playing.
     */
    private boolean effectsON = true;
    
    /**
     * A pool whose threads will play a sound (finite or in a loop). */
    private final ThreadPoolExecutor pool;
    private final ConcurrentLinkedDeque<Playable> playables;
    private final AtomicBoolean mustWait;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */
    
    /**
     * Creates a new jukebox.
     */
    public Jukebox() {

        pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        playables = new ConcurrentLinkedDeque<>();
        mustWait = new AtomicBoolean(false);
    }

    private synchronized void waitUntilClearIfStopped() {

        while (mustWait.get()) {

            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }

    }

    /**
     * @param sound - the sound identifier.
     * @param finite - if the sound will end or not (played in a loop). */
    public void play(Playlist sound, boolean finite) {
        String path;
        Playable p;

        /* Checks if the sound can be played */
        if (!check(sound)) {
            
            return;
        }
        
        waitUntilClearIfStopped();
        
        if (sound != null) {

            switch (sound) {
                case ARROW_HIT:
                    path = "/resources/sound/ArrowHit.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case ARROW_SHOT:
                    path = "/resources/sound/ArrowShot.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case CAUGHT:
                    path = "/resources/sound/Caught.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case FREEZING_SPELL:
                    path = "/resources/sound/FreezingSpell.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case GAME_OVER:
                    path = "/resources/sound/GameOver.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case GUITAR_CONCERT:
                    path = "/resources/sound/GuitarConcertInEMinor.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_1:
                    path = "/resources/sound/Hit1.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_2:
                    path = "/resources/sound/Hit2.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_3:
                    path = "/resources/sound/Hit3.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case JUMP:
                    path = "/resources/sound/Jump.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MAGIC_SMITE:
                    path = "/resources/sound/MagicSmite.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MEMORIES:
                    path = "/resources/sound/MemoriesInDMinor.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MOUSE_ENTERED:
                    path = "/resources/sound/MouseEntered.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case SELECT:
                    path = "/resources/sound/Select.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case SHOT:
                    path = "/resources/sound/Shot.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case STEP:
                    path = "/resources/sound/Step.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case THE_LURKING_BEAST:
                    path = "/resources/sound/TheLurkingBeast.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case FALLING_RUBBLE:
                    path = "/resources/sound/FallingRubble.wav";
                    p = new Playable(path, finite, sound);
                    playables.add(p);
                    pool.execute(p);
            }

        }

    }

    /**
     * @param sound - the sound identifier.
     * @param finite - if the sound will end or not (played in a loop).
     * @param decibels - the number of decibels to deduct from the volume. */
    public void play(Playlist sound, boolean finite, float decibels) {
        String path;
        Playable p;
        
        /* Checks if the sound can be played */
        if (!check(sound)) {
            
            return;
        }

        waitUntilClearIfStopped();

        if (sound != null) {

            switch (sound) {
                case ARROW_HIT:
                    path = "/resources/sound/ArrowHit.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case ARROW_SHOT:
                    path = "/resources/sound/ArrowShot.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case CAUGHT:
                    path = "/resources/sound/Caught.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case FREEZING_SPELL:
                    path = "/resources/sound/FreezingSpell.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case GAME_OVER:
                    path = "/resources/sound/GameOver.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case GUITAR_CONCERT:
                    path = "/resources/sound/GuitarConcertInEMinor.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_1:
                    path = "/resources/sound/Hit1.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_2:
                    path = "/resources/sound/Hit2.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_3:
                    path = "/resources/sound/Hit3.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case JUMP:
                    path = "/resources/sound/Jump.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MAGIC_SMITE:
                    path = "/resources/sound/MagicSmite.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MEMORIES:
                    path = "/resources/sound/MemoriesInDMinor.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MOUSE_ENTERED:
                    path = "/resources/sound/MouseEntered.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case SELECT:
                    path = "/resources/sound/Select.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case SHOT:
                    path = "/resources/sound/Shot.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case STEP:
                    path = "/resources/sound/Step.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case THE_LURKING_BEAST:
                    path = "/resources/sound/TheLurkingBeast.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case FALLING_RUBBLE:
                    path = "/resources/sound/FallingRubble.wav";
                    p = new Playable(path, finite, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
            }

        }

    }

    /**
     * @param sound - the sound identifier.
     * @param start - the starting frame of the clip.
     * @param end - the ending frame of the clip. */
    public void play(Playlist sound, int start, int end) {
        String path;
        Playable p;
        
        /* Checks if the sound can be played */
        if (!check(sound)) {
            
            return;
        }

        waitUntilClearIfStopped();

        if (sound != null) {

            switch (sound) {
                case ARROW_HIT:
                    path = "/resources/sound/ArrowHit.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case ARROW_SHOT:
                    path = "/resources/sound/ArrowShot.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case CAUGHT:
                    path = "/resources/sound/Caught.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case FREEZING_SPELL:
                    path = "/resources/sound/FreezingSpell.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case GAME_OVER:
                    path = "/resources/sound/GameOver.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case GUITAR_CONCERT:
                    path = "/resources/sound/GuitarConcertInEMinor.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_1:
                    path = "/resources/sound/Hit1.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_2:
                    path = "/resources/sound/Hit2.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_3:
                    path = "/resources/sound/Hit3.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case JUMP:
                    path = "/resources/sound/Jump.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MAGIC_SMITE:
                    path = "/resources/sound/MagicSmite.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MEMORIES:
                    path = "/resources/sound/MemoriesInDMinor.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MOUSE_ENTERED:
                    path = "/resources/sound/MouseEntered.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case SELECT:
                    path = "/resources/sound/Select.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case SHOT:
                    path = "/resources/sound/Shot.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case STEP:
                    path = "/resources/sound/Step.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;


                case THE_LURKING_BEAST:
                    path = "/resources/sound/TheLurkingBeast.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case FALLING_RUBBLE:
                    path = "/resources/sound/FallingRubble.wav";
                    p = new Playable(path, start, end, sound);
                    playables.add(p);
                    pool.execute(p);
            }

        }

    }

    /**
     * @param sound - the sound identifier.
     * @param start - the starting frame of the clip.
     * @param end - the ending frame of the clip.
     * @param decibels - the number of decibels to deduct from the volume. */
    public void play(Playlist sound, int start, int end, float decibels) {
        String path;
        Playable p;
        
        /* Checks if the sound can be played */
        if (!check(sound)) {
            
            return;
        }

        waitUntilClearIfStopped();

        if (sound != null) {

            switch (sound) {
                case ARROW_HIT:
                    path = "/resources/sound/ArrowHit.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case ARROW_SHOT:
                    path = "/resources/sound/ArrowShot.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case CAUGHT:
                    path = "/resources/sound/Caught.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case FREEZING_SPELL:
                    path = "/resources/sound/FreezingSpell.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case GAME_OVER:
                    path = "/resources/sound/GameOver.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case GUITAR_CONCERT:
                    path = "/resources/sound/GuitarConcertInEMinor.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_1:
                    path = "/resources/sound/Hit1.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_2:
                    path = "/resources/sound/Hit2.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case HIT_3:
                    path = "/resources/sound/Hit3.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case JUMP:
                    path = "/resources/sound/Jump.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MAGIC_SMITE:
                    path = "/resources/sound/MagicSmite.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MEMORIES:
                    path = "/resources/sound/MemoriesInDMinor.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case MOUSE_ENTERED:
                    path = "/resources/sound/MouseEntered.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case SELECT:
                    path = "/resources/sound/Select.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;
                case SHOT:
                    path = "/resources/sound/Shot.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case STEP:
                    path = "/resources/sound/Step.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case THE_LURKING_BEAST:
                    path = "/resources/sound/TheLurkingBeast.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
                    break;

                case FALLING_RUBBLE:
                    path = "/resources/sound/FallingRubble.wav";
                    p = new Playable(path, start, end, decibels, sound);
                    playables.add(p);
                    pool.execute(p);
            }

        }

    }
    
    /**
     * Checks if that sound can be played (depends on the attributes 
     * {@code musicON} and {@code effectsON}).
     */
    private boolean check (Playlist sound) {
        
        /* Checks if it's a music clip and can be played */
        if (musicON && isMusicClip(sound)) {
            
            return true;
        }
        
        /* Checks if it's an effect clip and can be played */
        return effectsON && !(isMusicClip(sound));
    }

    /**
     * Awaits until the clips being played would terminate or until the time
     * interval expires.
     * @param time
     * @param timeUnit */
    public void awaitTermination(int time, TimeUnit timeUnit) {

        if (!pool.isTerminated() && !pool.isTerminating()) {

            try {
                pool.awaitTermination(time, timeUnit);
            } catch (InterruptedException exception) {
                System.out.println(exception.getMessage());
            }

        }

    }

    /**
     * Stops all the clips are being played. */
    public synchronized void stop() {
        mustWait.set(true);

        for (Playable p : playables) {
         
            p.end();
        }

        playables.clear();
        mustWait.set(false);
        notifyAll();
    }
    
    /**
     * Mute all sounds of the selected group.
     * 
     * @param muteMusic 
     *              If this parameter is <i>true</i>, only the music will be
     *          muted. If it's <i>false</i>, only sound effects will be muted.
     */
    public synchronized void mute (boolean muteMusic) {
        
        /* If it's the music the sound that will be muted, stops its 
        reproduction */
        if (muteMusic) {
            
            for (Playable p : playables) {
                
                if (isMusicClip(p.getClipName())) {
                    
                    p.stopPlaying();
                }
            }
            
        }
        
        /* The sound effects are finite and doesn't long very much, so they
        will end by themselves and don't have to be stopped */
    }
    
    /**
     * Returns <i>true</i> if the clip is a music clip. If it's a sound effect,
     * returns <i>false</i>.
     */
    private boolean isMusicClip (Playlist clip) {
        
        return (clip.equals(Playlist.GAME_OVER) ||
                clip.equals(Playlist.GUITAR_CONCERT) ||
                clip.equals(Playlist.MEMORIES) ||
                clip.equals(Playlist.THE_LURKING_BEAST));
    }
    
    /**
     * This method will be called when a playable ends its task, so it can be
     * removed from the list.
     *
     * @param playable
     *              Playable that finished its execution.
     */
    public void clipEnded (Playable playable) {

        /* Removes the object from the list */
        playables.remove(playable);
    }
    

/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */

    /**
     * Returns <i>true</i> if the given clip is playing.
     *
     * @param clip
     *             The clip which name is going to be searched between the
     *          currently playing clips.
     *
     * @return
     */
    public boolean isPlaying (Playlist clip) {

        for (Playable p : playables) {

            if (p.getClipName().equals(clip)) {

                return true;
            }
        }

        return false;
    }
    
    /**
     * Returns <i>true</i> if the main music is being played; <i>false</i>
     * if it's muted.
     * 
     * @return 
     *          The value of {@code musicON}
     */
    public boolean isMusicON () {
        
        return musicON;
    }

    /**
     * Returns <i>true</i> if the sound effects are being played; <i>false</i>
     * if they're muted.
     * 
     * @return 
     *          The value of {@code effectsON}
     */
    public boolean areEffectsOn() {
        
        return effectsON;
    }    
    
    /**
     * Inverts the value of {@code musicON}.
     * 
     * @return 
     *              The new value of {@code musicON}
     */
    public boolean changeMusicState () {
        
        musicON = (!musicON);
        
        if (musicON) {
            
            mute (true);
        } else {
            
            /* Restarts the music that was being played */
            for (Playable p : playables) {
                
                if (isMusicClip(p.getClipName())) {
                    
                    p.restartClip();
                }
            }
        }
                
        return musicON;
    }
    
    /**
     * Inverts the value of {@code effectsON}.
     * 
     * @return 
     *              The new value of {@code effectsON}
     */
    public boolean changeEffectsState () {
        
        effectsON = (!effectsON);
        
        mute (false);
        
        return effectsON;
    }
}
