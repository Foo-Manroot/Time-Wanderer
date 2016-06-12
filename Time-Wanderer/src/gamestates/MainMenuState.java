package gamestates;

import java.util.concurrent.TimeUnit;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import sound.Playlist;
import main.MainClass;
import static main.MainClass.JUKEBOX;
import utils.NumberUtils;

public class MainMenuState extends BasicGameState {
    /**
     * The integer which identifies this game state. */
    private final int identifier;
    /**
     * The state based game which contains this main menu state. */
    private final StateBasedGame game;
    /* Random elements (they will be changed). */
    private int windowWidth;
    private int windowHeight;
    private Image background;
    private Image title;
    private Image selectFile;
    private Image options;
    private Image exit;
    private Image selectFileGlow;
    private Image optionsGlow;
    private Image exitGlow;
    private int backgroundSize[];
    private int titleSize[];
    private int selectFileSize[];
    private int optionsSize[];
    private int exitSize[];
    private int backgroundLocation[];
    private int titleLocation[];
    private int selectFileLocation[];
    private int optionsLocation[];
    private int exitLocation[];
    private int entered;

    /**
     * @param identifier - the identifier of the MainMenuState.
     * @param game - the state based game which will contain the main menu. */
    public MainMenuState(int identifier, StateBasedGame game) {
        this.identifier = identifier;
        this.game = game;

        if (game == null) {
            throw new NullPointerException("passed StateBasedGame mustn't be"
                + "null");
        }

    }

    /**
     * @return The MainMenuState identifier. */
    @Override
    public int getID() {
        return identifier;
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        
        MainClass.currState = MainClass.MENU_STATE_ID;
        
        /* Stops the previous sounds and plays the menu's music */
        if (!JUKEBOX.isPlaying(Playlist.MEMORIES)) {
            JUKEBOX.stop();
            JUKEBOX.play(Playlist.MEMORIES, false, 10);
        }

    }

    @Override
    public void init(GameContainer c, StateBasedGame s) throws SlickException {
        int remainingSpace;
        int separation;
        
        /* Elements initialization (images loading). */
        windowWidth = (int) (MainClass.WINDOW_WIDTH);
        windowHeight = (int) (MainClass.WINDOW_HEIGHT);
        background = new Image("resources/ui/menuimages/background.png");
        title = new Image("resources/ui/menuimages/Title.png");
        selectFile = new Image("resources/ui/menuimages/Play.png");
        options = new Image("resources/ui/menuimages/Controls.png");
        exit = new Image("resources/ui/menuimages/Exit.png");
        selectFileGlow = new Image("resources/ui/menuimages/PlayGlow.png");
        optionsGlow = new Image("resources/ui/menuimages/ControlsGlow.png");
        exitGlow = new Image("resources/ui/menuimages/ExitGlow.png");
        /* Sizes initialization. */
        backgroundSize = new int[] {windowWidth, windowHeight};
        titleSize = new int[] {title.getWidth(), title.getHeight()};
        selectFileSize = new int[] {
            selectFile.getWidth(),
            selectFile.getHeight()
        };
        optionsSize = new int[] {options.getWidth(), options.getHeight()};
        exitSize = new int[] {exit.getWidth(), exit.getHeight()};
        /* Remaining space treatment. */
        remainingSpace = windowHeight - titleSize[1] - selectFileSize[1]
            - optionsSize[1] - exitSize[1];
        /* Separation between elements. */
        separation = remainingSpace / 5 + 50;
        /* Locations initialization. */
        backgroundLocation = new int[] {0, 0};
        titleLocation = new int[] {
            (backgroundSize[0] / 2) - (titleSize[0] / 2),
            separation
        };
        selectFileLocation = new int[] {
            (backgroundSize[0] / 2) - (selectFileSize[0] / 2),
            separation + titleSize[1]
        };
        optionsLocation = new int[] {
            (backgroundSize[0] / 2) - (optionsSize[0] / 2),
            separation + titleSize[1] + selectFileSize[1]
        };
        exitLocation = new int[] {
            (backgroundSize[0] / 2) - (exitSize[0] / 2),
            separation + titleSize[1] + selectFileSize[1] + optionsSize[1]
        };
    }

    @Override
    public void render(GameContainer c, StateBasedGame s, Graphics g)
        throws SlickException {
        // g.scale(MapTraversingTest.SCALE, MapTraversingTest.SCALE);

        switch (entered) {
            case 0:
                g.drawImage(background, backgroundLocation[0],
                    backgroundLocation[1]);
                g.drawImage(title, titleLocation[0], titleLocation[1]);
                g.drawImage(selectFileGlow, selectFileLocation[0],
                    selectFileLocation[1]);
                g.drawImage(options, optionsLocation[0], optionsLocation[1]);
                g.drawImage(exit, exitLocation[0], exitLocation[1]);
                break;
            case 1:
                g.drawImage(background, backgroundLocation[0],
                    backgroundLocation[1]);
                g.drawImage(title, titleLocation[0], titleLocation[1]);
                g.drawImage(selectFile, selectFileLocation[0],
                    selectFileLocation[1]);
                g.drawImage(optionsGlow, optionsLocation[0],
                    optionsLocation[1]);
                g.drawImage(exit, exitLocation[0], exitLocation[1]);
                break;
            case 2:
                g.drawImage(background, backgroundLocation[0],
                    backgroundLocation[1]);
                g.drawImage(title, titleLocation[0], titleLocation[1]);
                g.drawImage(selectFile, selectFileLocation[0],
                    selectFileLocation[1]);
                g.drawImage(options, optionsLocation[0], optionsLocation[1]);
                g.drawImage(exitGlow, exitLocation[0], exitLocation[1]);
                break;
            default:
                g.drawImage(background, backgroundLocation[0],
                    backgroundLocation[1]);
                g.drawImage(title, titleLocation[0], titleLocation[1]);
                g.drawImage(selectFile, selectFileLocation[0],
                    selectFileLocation[1]);
                g.drawImage(options, optionsLocation[0], optionsLocation[1]);
                g.drawImage(exit, exitLocation[0], exitLocation[1]);
        }

    }

    @Override
    public void update(GameContainer c, StateBasedGame s, int delta)
        throws SlickException {
        int x = Mouse.getX();
        int y = NumberUtils.invertOrdinate(Mouse.getY());
        boolean pressed = false;

        if (Mouse.isButtonDown(0)) {
            pressed = true;
        }

        if ((x >= selectFileLocation[0])
            && (x <= (selectFileLocation[0] + selectFileSize[0]))
            && (y >= selectFileLocation[1])
            && (y <= (selectFileLocation[1] + selectFileSize[1]))) {

            if (entered != 0) {
                MainClass.JUKEBOX.play(Playlist.MOUSE_ENTERED, true, 10);
            }

            entered = 0;

            if (pressed) {
                /* "PLAY" option slected */
                JUKEBOX.stop();
                JUKEBOX.play(Playlist.SELECT, true, 10);
                /* Waits for the previous sounds to end */
                MainClass.JUKEBOX.awaitTermination(500, TimeUnit.MILLISECONDS);

                /* Stops the previous sounds and plays the level's music */
                MainClass.JUKEBOX.stop();
                MainClass.JUKEBOX.play(Playlist.GUITAR_CONCERT, false, 10);
                
                /* Initiates and enters the main level state */
                game.getState(MainClass.LEVEL_STATE_ID).init(c, game);
                game.enterState(MainClass.LEVEL_STATE_ID);
            }

        } else if ((x >= optionsLocation[0])
            && (x <= (optionsLocation[0] + optionsSize[0]))
            && (y >= optionsLocation[1])
            && (y <= (optionsLocation[1] + optionsSize[1]))) {

            if (entered != 1) {
                MainClass.JUKEBOX.play(Playlist.MOUSE_ENTERED, true);
            }

            entered = 1;

            if (pressed) {
                /* "CONTROLS" option slected */
                MainClass.JUKEBOX.play(Playlist.MAGIC_SMITE, true);
                
                /* Enters the ControlsMenu state */
                game.enterState (MainClass.CONTROLS_MENU_STATE);
            }

        } else if ((x >= exitLocation[0])
            && (x <= (exitLocation[0] + exitSize[0]))
            && (y >= exitLocation[1])
            && (y <= (exitLocation[1] + exitSize[1]))) {

            if (entered != 2) {
                MainClass.JUKEBOX.play(Playlist.MOUSE_ENTERED, true);
            }

            entered = 2;

            if (pressed) {
                MainClass.JUKEBOX.play(Playlist.SELECT, true);
                MainClass.JUKEBOX.awaitTermination(1, TimeUnit.SECONDS);
                System.exit(0);
            }

        } else {
            entered = -1;
        }

    }

}
