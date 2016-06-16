package main;

import entities.Player;
import gamestates.ControlsMenu;
import gamestates.InventoryState;
import gamestates.LevelState;
import gamestates.MainMenuState;
import gamestates.PlayerDeathState;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import static levels.tiles.Tile.TILE_SIZE;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import sound.Jukebox;

public class MainClass extends StateBasedGame {

/* ------------------------ */
/* ---- GAME PARAMETERS --- */
/* ------------------------ */
    public static final int WINDOW_WIDTH = 1152;
    public static final int WINDOW_HEIGHT = (int) ((float) WINDOW_WIDTH / 16 * 9);
    public static final boolean FULLSCREEN = false;
    public static final Jukebox JUKEBOX = new Jukebox();
    public static Player players[];

    /* STATES IDENTIFIERS: */
    public static final int LEVEL_STATE_ID = 0;
    public static final int MENU_STATE_ID = 1;
    public static final int INVENTORY_STATE_P1_ID = 2;
    public static final int INVENTORY_STATE_P2_ID = 4;
    public static final int DEATH_STATE_ID = 3;
    public static final int CONTROLS_MENU_STATE = 5;

    public static int currState = MENU_STATE_ID;
    /* ------------------- */

    private static MainClass vj;

    public static final float SCALE = ((float) WINDOW_HEIGHT / (TILE_SIZE * 18));
    
/* ------------------------------- */
/* ---- END OF GAME PARAMETERS --- */
/* ------------------------------- */

    public static void main (String[] args) {

        /* Sets the properties to execute the game without the option
        "-Djava.library.path=<blablabla>" */
        System.setProperty("java.library.path", "lib");
        System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());

        vj = new MainClass("Time Wanderer");
        AppGameContainer appgc = null;

        try {
            appgc = new AppGameContainer(vj);
            appgc.setDisplayMode(WINDOW_WIDTH,WINDOW_HEIGHT,FULLSCREEN);
            appgc.setTargetFrameRate(60);

            //JUKEBOX.play(Playlist.GUITAR_CONCERT, false);
            //JUKEBOX.play(Playlist.MEMORIES, false, 10);

            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public MainClass(String title) {
        super(title);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {

        addState(new MainMenuState(MENU_STATE_ID, this));
        addState(new LevelState(LEVEL_STATE_ID, this));
        addState(new InventoryState(INVENTORY_STATE_P1_ID, this));
        addState(new InventoryState(INVENTORY_STATE_P2_ID, this));
        addState(new PlayerDeathState(DEATH_STATE_ID, this));
        addState(new ControlsMenu (CONTROLS_MENU_STATE, this));

        Player p[] = ((LevelState) getState(LEVEL_STATE_ID)).getPlayers();
        players = p;
        ((InventoryState) getState(INVENTORY_STATE_P1_ID)).setPlayer(p[0]);
        ((InventoryState) getState(INVENTORY_STATE_P2_ID)).setPlayer(p[1]);
        this.enterState(1);
    }

    public static void changeState(int stateID) {
        vj.enterState(stateID);
    }

}
