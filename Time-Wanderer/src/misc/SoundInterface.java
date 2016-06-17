package misc;

import java.util.logging.Level;
import java.util.logging.Logger;
import main.MainClass;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import utils.NumberUtils;

/**
 *  This class implements a little interface with the necessary buttons to
 * stop and start the music and the sound effects.
 *  It will consist in a couple of icons on the top right part of the window 
 * that the user can click over and start and stop the music's or effects'
 * reproduction.
 */
public class SoundInterface {
    
    /**
     * Image for the icon that will control the music's reproduction.
     */
    private Image musicIcon;
    
    /**
     * Position of the effects icon
     */
    Vector2f musicIconPosition = new Vector2f (490, 10);
    
    /**
     * Image for the icon that will control the sound effects' reproduction.
     */
    private Image effectsIcon;
    
    /**
     * Position of the effects icon
     */
    Vector2f effectsIconPosition = new Vector2f (470, 10);
    
    /**
     * Array with all the different images for the music icon.
     *  Each position has the following image:
     *  <br> 0 -> ON
     *  <br> 1 -> OFF
     *  <br> 2 -> ON-glow
     *  <br> 3 -> OFF-glow
     */
    Image [] musicImages = new Image [4];
    
    /**
     * Array with all the different images for the effects icon.
     *  Each position has the following image:
     *  <br> 0 -> ON
     *  <br> 1 -> OFF
     *  <br> 2 -> ON-glow
     *  <br> 3 -> OFF-glow
     */
    Image [] effectsImages = new Image [4];
    
    /**
     * Flag to avoid multiple signals from the left mouse button being 
     * pressed and make just one action so the state won't changed so quickly.
     */
    private boolean musicStateChanged = false;
    
    /**
     * Flag to avoid multiple signals from the left mouse button being 
     * pressed and make just one action so the state won't changed so quickly.
     */
    private boolean effectsStateChanged = false;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */

    /**
     * Constructor.
     */
    public SoundInterface () {
        
        /* Initialization of the images array */
        initImages();
    }
    
    /**
     * Initialices the arrays with the images ({@code musicImages} and 
     * {@code effectsImages}) 
     */
    private void initImages () {
        
        /* Calls the specific method for each array */
        initMusicImages();
        initEffectsImages();
    }
    
    /**
     * Inititalices the images array for the music icon.
     */
    private void initMusicImages () {
        
        /* ON */
        try {
            musicImages[0] = 
                    new Image("resources/ui/pifaceimages/music_ON.png");
            /* Resizes the icon to half its initial value */
            musicImages[0] = musicImages[0].getScaledCopy(0.5f);
            
        } catch (SlickException ex) {   
            
            System.out.println("Exception at SoundInterface().initMusicImages"
                    + " while loading music_ON.png: "
                    + ex.getMessage());
            /* If an exception has been thrown, initialices the icon to null */
            musicImages [0] = null;
        }
        
        /* OFF */
        try {
            musicImages[1] = 
                    new Image("resources/ui/pifaceimages/music_OFF.png");
            /* Resizes the icon to half its initial value */
            musicImages[1] = musicImages[1].getScaledCopy(0.5f);
            
        } catch (SlickException ex) {            
            
            System.out.println("Exception at SoundInterface().initMusicImages"
                    + " while loading music_OFF.png: "
                    + ex.getMessage());
            /* If an exception has been thrown, initialices the icon to null */
            musicImages [1] = null;
        }
        
        /* ON-glow */
        try {
            musicImages[2] = 
                    new Image("resources/ui/pifaceimages/music_ON-glow.png");
            /* Resizes the icon to half its initial value */
            musicImages[2] = musicImages[2].getScaledCopy(0.5f);
            
        } catch (SlickException ex) {            
            
            System.out.println("Exception at SoundInterface().initMusicImages"
                    + " while loading music_ON-glow.png: "
                    + ex.getMessage());
            /* If an exception has been thrown, initialices the icon to null */
            musicImages [2] = null;
        }
        
        /* OFF-glow */
        try {
            musicImages[3] = 
                    new Image("resources/ui/pifaceimages/music_OFF-glow.png");
            /* Resizes the icon to half its initial value */
            musicImages[3] = musicImages[3].getScaledCopy(0.5f);
            
        } catch (SlickException ex) {            
            
            System.out.println("Exception at SoundInterface().initMusicImages"
                    + " while loading music_OFF-glow.png: "
                    + ex.getMessage());
            /* If an exception has been thrown, initialices the icon to null */
            musicImages [3] = null;
        }
    }
    
    /**
     * Inititalices the images array for the music icon.
     */
    private void initEffectsImages () {
        
        /* ON */
        try {
            effectsImages[0] = 
                    new Image("resources/ui/pifaceimages/effects_ON.png");
            /* Resizes the icon to half its initial value */
            effectsImages[0] = effectsImages[0].getScaledCopy(0.5f);
            
        } catch (SlickException ex) { 
            
            System.out.println("Exception at SoundInterface().initEffectsImages"
                    + " while loading effects_ON.png: "
                    + ex.getMessage());
            /* If an exception has been thrown, initialices the icon to null */
            effectsImages [0] = null;
        }
        
        /* OFF */
        try {
            effectsImages[1] = 
                    new Image("resources/ui/pifaceimages/effects_OFF.png");
            /* Resizes the icon to half its initial value */
            effectsImages[1] = effectsImages[1].getScaledCopy(0.5f);
            
        } catch (SlickException ex) {  
            
            System.out.println("Exception at SoundInterface().initEffectsImages"
                    + " while loading effects_OFF.png: "
                    + ex.getMessage());
            /* If an exception has been thrown, initialices the icon to null */
            effectsImages [1] = null;
        }
        
        /* ON-glow */
        try {
            effectsImages[2] = 
                    new Image("resources/ui/pifaceimages/effects_ON-glow.png");
            /* Resizes the icon to half its initial value */
            effectsImages[2] = effectsImages[2].getScaledCopy(0.5f);
            
        } catch (SlickException ex) {  
            
            System.out.println("Exception at SoundInterface().initEffectsImages"
                    + " while loading effects_ON-glow.png: "
                    + ex.getMessage());
            /* If an exception has been thrown, initialices the icon to null */
            effectsImages [2] = null;
        }
        
        /* OFF-glow */
        try {
            effectsImages[3] = 
                    new Image("resources/ui/pifaceimages/effects_OFF-glow.png");
            /* Resizes the icon to half its initial value */
            effectsImages[3] = effectsImages[3].getScaledCopy(0.5f);
            
        } catch (SlickException ex) {      
            
            System.out.println("Exception at SoundInterface().initEffectsImages"
                    + " while loading effects_OFF-glow.png: "
                    + ex.getMessage());
            /* If an exception has been thrown, initialices the icon to null */
            effectsImages [3] = null;
        }
    }
    
    /**
     * This method checks if the mouse is over the given image.
     * 
     * @param image
     *              The image whose position will be compared with the mouse's
     *          one.
     * @param imagePosition 
     *              Vector with the position of the image.
     */
    private boolean checkMousePosition (Image image, Vector2f imagePosition) {
        
        Vector2f mouse = new Vector2f (Mouse.getX(), Mouse.getY());
        /* Makes the proper adjustements */
        mouse = NumberUtils.adjustCoordinates(mouse);
        
        if (image == null) {
            
            return false;
        }
        
        /* Compares the current position of the mouse with the four sides of 
        the image */
        return ((mouse.x > imagePosition.x) &&
                (mouse.x < (imagePosition.x + image.getWidth()))
                &&
                (mouse.y > imagePosition.y) &&
                (mouse.y < (imagePosition.y + image.getHeight()))
                );
    }
    
    /**
     * Updates the state of the icons. If the mouse is over any of them, they'll
     * glow. If they mouse left button is pressed when on the icon's position, 
     * its state will change.
     * 
     * @param container 
     *              Container from which the input will be captured.
     * @param delta 
     *              Milliseconds that took the computer to update and render
     *          the previous frame.
     */
    public void update (GameContainer container, int delta) {
        
        /* If the mouse is over the icon, changes its image to the glow one */
        if (checkMousePosition(musicIcon, musicIconPosition)) {
            
            /* If the left button of the mouse is pressed, changes the state
            of the music's reproduction */
            if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                
                /* Only changes if the state hasn't been change before */
                if (!musicStateChanged) {
                    
                    MainClass.JUKEBOX.changeMusicState();
                    /* The flag is set to true so no more changes can be made */
                    musicStateChanged = true;
                }
            }

            musicIcon = (MainClass.JUKEBOX.isMusicON())?
                            musicImages[2]: /* ON-glow */
                            musicImages[3] /* OFF-glow */;
            
        } else {
            
            /* The mouse is not over the icon anymore, so the flag is resetted
            to false again */
            musicStateChanged = false;
            
            /* If the image was the glow one, restores it with
            the non-glow icon */
            if ((musicIcon != musicImages[0]) ||
                    (musicIcon != musicImages[1])) {

                musicIcon = (MainClass.JUKEBOX.isMusicON())?
                                musicImages[0]: /* ON */
                                musicImages[1] /* OFF */;
            }
        }
        
        /* Checks if the mouse is over the effects icon */
        if (checkMousePosition(effectsIcon, effectsIconPosition)) {
            
            /* If the left button of the mouse is pressed, changes the state
            of the effects' reproduction */
            if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                
                /* Only changes if the state hasn't been change before */
                if (!effectsStateChanged) {
                    
                    MainClass.JUKEBOX.changeEffectsState();
                    /* The flag is set to true so no more changes can be made */
                    effectsStateChanged = true;
                }
            }

            effectsIcon = (MainClass.JUKEBOX.areEffectsOn())?
                            effectsImages[2]: /* ON-glow */
                            effectsImages[3] /* OFF-glow */;
        } else {

            /* The mouse is not over the icon anymore, so the flag is resetted
            to false again */
            effectsStateChanged = false;
            
            /* If the image was the glow one, restores it with
            the non-glow icon */
            if ((effectsIcon != effectsImages[0]) ||
                    (effectsIcon != effectsImages[1])) {

                effectsIcon = (MainClass.JUKEBOX.areEffectsOn())?
                                effectsImages[0]: /* ON */
                                effectsImages[1] /* OFF */;
            }
        }
    }
    
    /**
     * Draws the image on the screen.
     * 
     * @param g 
     *              Frame where the icons will be drawn.
     */
    public void render (Graphics g) {
        
        if (musicIcon != null) {
           
            g.drawImage(musicIcon, 
                        musicIconPosition.x,
                        musicIconPosition.y);
            
            if (effectsIcon != null) {
                
                g.drawImage(effectsIcon, 
                            effectsIconPosition.x,
                            effectsIconPosition.y);
            }
        }
    }
}
