package gamestates;

import main.MainClass;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import sound.Playlist;

/**
 *
 * @author Miguel García Martín
 */
public class ControlsMenu extends BasicGameState {
    
    /**
     * ID of this state.
     */
    private final int identifier;
    
    /**
     * Image with the controls.
     */
    private Image controlsImage;
    
    /**
     * Background image (same as the main menu's one).
     */
    private Image background;
    
    /**
     * Coordinates where the background will be drawn.
     */
    private final Vector2f backgroundLocation;
    
    /**
     * Coordinates where the controls image will be drawn.
     */
    private final Vector2f controlsImageLocation;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */
    
    public ControlsMenu (int identifier, StateBasedGame game) {
        
         
        try {
            /* Opens the images and stores them on their respective attributes */
            this.controlsImage = new Image ("resources/ui/menuimages/controls-image.png");
            this.background = new Image ("resources/ui/menuimages/background.png");
            
            /* Scales the image (at 80% the original size) so it can be into 
            the window */
            controlsImage = controlsImage.getScaledCopy (0.65f);
            
        } catch (SlickException ex) {
            
            this.controlsImage = null;
            this.background = null;
            
            System.out.println("Exception at ControlsMenu (): "
                               + ex.getMessage());
        }
        
        this.identifier = identifier;
        this.backgroundLocation = new Vector2f (0, 0);
        /* The image should be drawn on the middle of the screen */
        this.controlsImageLocation = new Vector2f (
                (MainClass.WINDOW_WIDTH / 2) - (controlsImage.getWidth() / 2),
                (MainClass.WINDOW_HEIGHT / 2) - (controlsImage.getHeight()/ 2)
        );
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        
        /* Updates the current state's id */
        MainClass.currState = MainClass.CONTROLS_MENU_STATE;
    }

    @Override
    public int getID() {
        
        return identifier;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) {
        
        /* Sets the background image */
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        
        /* Renders the images */
        g.drawImage(background, backgroundLocation.x, backgroundLocation.y);
        g.drawImage(controlsImage, controlsImageLocation.x, controlsImageLocation.y);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        
        /* If the ENTER key pressed, returns to the level */
        if (container.getInput().isKeyPressed (Input.KEY_ENTER) ||
            container.getInput().isKeyPressed (Input.KEY_ESCAPE)) {
            
            game.enterState(MainClass.MENU_STATE_ID);
        }
    }
}
