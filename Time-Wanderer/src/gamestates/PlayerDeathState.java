/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates;

import main.MainClass;
import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import sound.Playlist;

/**
 * This state will prompt a little message, like "you died" or anything like 
 * that, 
 */
public class PlayerDeathState extends BasicGameState {
        
    /**
     * ID of this state.
     */
    private final int id;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */
    
    /**
     * Constructor.
     * 
     * @param id 
     *              Identifier of this state.
     * @param game 
     *              Game that contains this state.
     */
    public PlayerDeathState (int id, StateBasedGame game) {

        this.id = id;
    }

    @Override
    public int getID() {
        
        return id;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
            throws SlickException {
        
        MainClass.currState = MainClass.MENU_STATE_ID;
        
        MainClass.JUKEBOX.stop();
        MainClass.JUKEBOX.play(Playlist.GAME_OVER, true);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        
        /* Changes the font to a bigger one */
        Font font = new Font("Verdana", Font.BOLD, 45);
        TrueTypeFont ttf = new TrueTypeFont(font, true);
        Color color = new Color (160, 0, 0); /* Slightly dark red */
        
        /* Draws the text on the middle of the screen */
        ttf.drawString(MainClass.WINDOW_WIDTH / 2, MainClass.WINDOW_HEIGHT / 2,
                       "You died", color);
        
        /* Sets a smaller font again... */
        font = new Font("Verdana", Font.BOLD, 16);
        ttf = new TrueTypeFont(font, true);
        
        /* Draws the text on the middle of the screen */
        ttf.drawString(MainClass.WINDOW_WIDTH / 2, 
                       MainClass.WINDOW_HEIGHT / 2 + 60,
                       "Press Enter to return", color);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
                
        /* If there's any key pressed, returns to the level */
        if (container.getInput().isKeyPressed(Input.KEY_ENTER)) {
            
            /* Stops the previous sounds and plays the main level's music */
            MainClass.JUKEBOX.stop();
            MainClass.JUKEBOX.play(Playlist.GUITAR_CONCERT, false, 10);
            
            /* Begins again from the start, so it must restart everything */
            game.getState(MainClass.LEVEL_STATE_ID).init(container, game);
            game.enterState(MainClass.LEVEL_STATE_ID);
        }
        
        /* If the ESC key is pressed, returns to the main menu */
        if (container.getInput().isKeyPressed (Input.KEY_ESCAPE)) {
            
            game.enterState(MainClass.MENU_STATE_ID);
        }
    }
}
