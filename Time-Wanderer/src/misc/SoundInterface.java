package misc;

import main.MainClass;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

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
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */

    /**
     * Constructor.
     */
    public SoundInterface () {
        
        try {
            /* Gets the initial state of the music and loads the right icon */
            musicIcon = (MainClass.JUKEBOX.isMusicON())? 
                        new Image("resources/ui/pifaceimages/music_ON.png") :
                        new Image("resources/ui/pifaceimages/music_OFF.png");
            
        } catch (SlickException ex) {
            
            System.out.println("Exception at SoundInterface(): "
                                + ex.getMessage());
            
            /* If an exception has been thrown, initialices the icon to null */
            musicIcon = null;
        }
        
        try {
            /* Gets the initial state of the effects and loads the right icon */
            effectsIcon = (MainClass.JUKEBOX.areEffectsOn())? 
                        new Image("resources/ui/pifaceimages/effects_ON.png") :
                        new Image("resources/ui/pifaceimages/effects_OFF.png");
            
        } catch (SlickException ex) {
            
            System.out.println("Exception at SoundInterface(): "
                                + ex.getMessage());
            
            /* If an exception has been thrown, initialices the icon to null */
            effectsIcon = null;
        }
    }
    
    /**
     * Updates the state of the icons. If the mouse is over any of them, they'll
     * glow. If they mouse left button is pressed when on the icon's position, 
     * its state will change.
     */
    public void update () {
        
        
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
