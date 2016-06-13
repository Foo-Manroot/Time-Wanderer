package gamestates;

import controller.KeyboardMouseController;
import controller.PlayerController;
import entities.InteractiveObject;
import entities.Player;
import creators.ItemCreator;
import items.MeleeWeapon;
import items.RangedWeapon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import levels.MapGenerator;
import levels.Room;
import misc.PlayerInterface;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import physics.Physics;
import main.MainClass;
import static main.MainClass.INVENTORY_STATE_P1_ID;
import static main.MainClass.INVENTORY_STATE_P2_ID;
import misc.MiniMap;
import misc.SoundInterface;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import sound.Playlist;
/**
 * BasicGameState that represents an actual level.
 * Contains the room to display, and creates a map the first time this is created.
 * Contains the player, the controller & the physics engine.
 * @author sergio
 */
public class LevelState extends BasicGameState {
  
    private static Room[][][] map;
    
    private Room[][] currentMap;
    private Room[] currentRoom;
    
    private Room finalRoom;
    
    private int row[] = {0,0}, column[] = {-1,-1};
    
    private MapGenerator mg;

    private Player[] player = new Player[2];
    
    /**
     * Index of the world (timeline)
     * <br> 0 = old timeline
     * <br> 1 = new timeline
     */
    public static int worldIdx = 0;
    
    private static int[] stageIndex;
    
    private PlayerController[] playerController;
    
    public static boolean levelBossKilled = false;
    
    private Physics physics;

    private Rectangle gradientTransitionRect;
    
    private boolean wentInsideDungeon[] = {true,true};
    
    private boolean isInFinalRoom[] = {false,false};
    
    /**
     * Interface for the minimap.
     */
    private MiniMap miniMap;
    
    /**
     * Game containing this state.
     */
    private StateBasedGame game;
    
    /**
     * Identifier of this state.
     */
    private final int id;
    
    int opacityTransition = 0;
    boolean transition = false;
    boolean endTransition = false;
    
    enum TransitionDirection {
        NONE,UP,DOWN,LEFT,RIGHT,TIMELINE,MAP,ENDROOM;
    }
    
    private static boolean changeMapNow = false;
    
    TransitionDirection transitionInfo = TransitionDirection.NONE;

    private PlayerInterface[] playerInterface;
    
    /**
     * Interface to show the current state of the sound (music ON/OFF, and 
     * sound effects ON/OFF).
     */
    private SoundInterface soundInterface;
    
    /**
     * Path where the file with the map is stored.
     */
    public static String MAP_FILE_PATH = "MapFile.tmp";
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */

    /**
     * On this constructor, the map will be generated automatically.
     * 
     * @param id
     *              Identifier of this state.
     * @param game 
     *              Game that contains this state.
     */
    public LevelState(int id, StateBasedGame game) {
        
        this.id = id;
        this.game = game;
        currentRoom = new Room[2];
        currentRoom[0] = new Room("none","outside","outside",
                                  false,false,false,true);
        currentRoom[1] = new Room("none","outside","outside2",
                                  false,false,false,true);
        finalRoom = new Room("none","final","stone",false,false,false,true);
        player[0] = new Player(350,350,32,62,currentRoom[0],Player.Timeline.PAST);
        player[1] = new Player(350,350,32,62,currentRoom[1],Player.Timeline.PRESENT);
    }    
    
    /**
     * Initialices the common things between the constructors.
     */
    private void initialization () throws SlickException {
        
        /* The level is set initially with the modern timeline selected */
        worldIdx = 1;
        
        levelBossKilled = false;
        stageIndex = new int[2];
        
        finalRoom = new Room("none","final","stone",false,false,false,true);
        
        stageIndex[0] = 0;
        stageIndex[1] = 0;
        
        currentRoom = new Room[2];
        currentRoom[0] = new Room("none","outside","outside",
                                  false,false,false,true);
        currentRoom[1] = new Room("none","outside","outside2",
                                  false,false,false,true);
        
        player[0] = new Player(350,350,32,62,currentRoom[0],Player.Timeline.PAST);
        player[1] = new Player(350,350,32,62,currentRoom[1],Player.Timeline.PRESENT);
        ((InventoryState) game.getState(INVENTORY_STATE_P1_ID)).setPlayer(player[0]);
        ((InventoryState) game.getState(INVENTORY_STATE_P2_ID)).setPlayer(player[1]);
        
        /* Generates a new map */
        mg = new MapGenerator();

        mg.generateMap();

        map = new Room[4][][];

        map[0] = mg.convertMap("castle");
        map[1] = mg.convertMap("castle2");
        
        mg.generateButtons(map[0],mg.generateBlockades(map[1]));
        
        mg.generateMap();
        
        map[2] = mg.convertMap("temple");
        map[3] = mg.convertMap("temple2");
        
        mg.generateButtons(map[2],mg.generateBlockades(map[3]));
        
        wentInsideDungeon[0] = true;
        wentInsideDungeon[1] = true;
        
        isInFinalRoom[0] = false;
        isInFinalRoom[1] = false;
        
        row[0] = 0;
        row[1] = 0;
        
        column[0] = -1;
        column[1] = -1;
        
        currentMap = map[0];
                
        gradientTransitionRect = new Rectangle(0, 0, MainClass.WINDOW_WIDTH,
                                                     MainClass.WINDOW_HEIGHT);
        
        playerInterface = new PlayerInterface[2];
        playerInterface[0] = new PlayerInterface(player[0], new int[]{130, 6});
        playerInterface[1] = new PlayerInterface(player[1], new int[]{130, 6});
        
        
        /* Creates the interface for the control of the sound on the level */
        soundInterface = new SoundInterface();
        
        playerController = new KeyboardMouseController[2];
        playerController[0] = new KeyboardMouseController(player[0]);
        playerController[1] = new KeyboardMouseController(player[1]);
        
        currentRoom[0].addCharacter(player[0]);
        currentRoom[1].addCharacter(player[1]);
        
        currentRoom[0].setPlayer(player[0]);
        currentRoom[1].setPlayer(player[1]);
        
        physics = new Physics();
        
        miniMap = new MiniMap(currentMap);

        InventoryState.addController((KeyboardMouseController) playerController[0]);
        InventoryState.addController((KeyboardMouseController) playerController[1]);
        currentRoom[0].placePlayer(player[0]);
        currentRoom[1].placePlayer(player[1]);
        
        //CHEST
        ItemCreator.fillRoom(currentRoom[0]);
        ItemCreator.fillRoom(currentRoom[1]);
    }
       
    private void createNewDungeon() {
        levelBossKilled = false;
        /* Generates a new map */
        mg = new MapGenerator();

        mg.generateMap();

        map = new Room[4][][];

        map[0] = mg.convertMap("castle");
        map[1] = mg.convertMap("castle2");
        
        mg.generateButtons(map[0],mg.generateBlockades(map[1]));
        
        mg.generateMap();
        
        map[2] = mg.convertMap("temple");
        map[3] = mg.convertMap("temple2");
        
        mg.generateButtons(map[2],mg.generateBlockades(map[3]));
        
        wentInsideDungeon[0] = true;
        wentInsideDungeon[1] = true;
        
        isInFinalRoom[0] = false;
        isInFinalRoom[1] = false;
        
        row[0] = 0;
        row[1] = 0;
        
        column[0] = 0;
        column[1] = 0;
        
        //worldIdx = 0;
        
        currentMap = map[0];
        
        currentRoom[0] = map[0][row[0]][column[0]];
        currentRoom[1] = map[1][row[1]][column[1]];
        
        currentRoom[0].setPlayer(player[0]);
        currentRoom[1].setPlayer(player[1]);
        
        currentRoom[0].addCharacter(player[0]);
        currentRoom[1].addCharacter(player[1]);
        
        currentRoom[0].placePlayer(player[0]);
        currentRoom[1].placePlayer(player[1]);
        
        stageIndex[0] = 0;
        stageIndex[1] = 0;
        
        
        miniMap.setCurrentMap(map[worldIdx]);
        miniMap.setCurrentRoom(row[worldIdx],column[worldIdx]);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        
        MainClass.currState = MainClass.LEVEL_STATE_ID;
        
        /* If the main level's music wasn't playing, stops the previous sounds
        and plays it */
        if (!MainClass.JUKEBOX.isPlaying(Playlist.GUITAR_CONCERT) && 
            !MainClass.JUKEBOX.isPlaying(Playlist.THE_LURKING_BEAST)) {
            
            MainClass.JUKEBOX.stop();
            MainClass.JUKEBOX.play(Playlist.GUITAR_CONCERT, false, 10);
        }
    }
    
    
    @Override
    public int getID() {
        
        return id;
    }
    
    @Override
    public void init(GameContainer container, StateBasedGame game) 
            throws SlickException {
        
        /* Initialices every component on the level */
        initialization();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.scale(MainClass.SCALE, MainClass.SCALE);
        currentRoom[worldIdx].render(g);
        
        if (transition) {
            g.setColor(new Color(0, 0, 0, opacityTransition));
            g.fill(gradientTransitionRect);
        }
        
        playerInterface[worldIdx].render(g);
        g.setColor(Color.green);      
        
        miniMap.render(g);
        
        soundInterface.render(g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        // If player is not already in the 'transition' mode
        //<editor-fold desc="Transition info">
        if(!transition) {
            playerController[worldIdx].handleInput(container.getInput(), delta);
            physics.handlePhysics(currentRoom[worldIdx], delta);
            if(changeMapNow) {
                changeMapNow = false;
                transitionInfo = TransitionDirection.TIMELINE;
                transition = true;   
            } else if(player[worldIdx].getX()+(player[worldIdx].getWidth()/2) > currentRoom[worldIdx].getMapWidth()) {
                // If player is crossing the right 'door'
                column[worldIdx]++;
                if(column[worldIdx] >= MapGenerator.columns || (isInFinalRoom[worldIdx])) {
                    column[worldIdx] = 0;
                    row[worldIdx] = 0;
                    transitionInfo = TransitionDirection.MAP;
                    levelBossKilled = false;
                    
                    if(stageIndex[worldIdx] == 0) {
                        stageIndex[worldIdx] += 2;
                    } else {
                        isInFinalRoom[worldIdx] = true;
                    }
                    wentInsideDungeon[worldIdx] = true;
                } else {
                    transitionInfo = TransitionDirection.RIGHT;
                }
                transition = true;
            } else if (player[worldIdx].getX()+(player[worldIdx].getWidth()/2) < 0) {
                // If player is crossing the left 'door'
                column[worldIdx]--;
                transitionInfo = TransitionDirection.LEFT;
                transition = true;
            } else if(player[worldIdx].getY()+(player[worldIdx].getHeight()/2) > currentRoom[worldIdx].getMapHeight()) {
                // If player is crossing the down 'door'
                row[worldIdx]++;
                transitionInfo = TransitionDirection.DOWN;
                transition = true;
            } else if(player[worldIdx].getY()+(player[worldIdx].getHeight() + 2) < 0) {
                // If player is crossing the top 'door'
                row[worldIdx]--;
                transitionInfo = TransitionDirection.UP;
                transition = true;
            }
        }

        if (transition) {
            if (!endTransition) {
                opacityTransition += 15;
                if (opacityTransition >= 255) {
                    endTransition = true;
                    
                    currentRoom[worldIdx].stopEnemies();
                    if(transitionInfo != TransitionDirection.TIMELINE) {
                        currentRoom[worldIdx].setPlayer(null); // Remove the player from the room before changing.
                        currentRoom[worldIdx].removeCharacter(player[worldIdx]);
                    }
                    if(transitionInfo == TransitionDirection.TIMELINE) {
                        worldIdx = (worldIdx + 1) % 2;
                    }
                    if(transitionInfo == TransitionDirection.TIMELINE ||
                            transitionInfo == TransitionDirection.MAP) {
                        currentMap = map[worldIdx+stageIndex[worldIdx]];
                        miniMap.setCurrentMap(currentMap);
                    }
                    
                    player[worldIdx].setCurrentRoom(currentRoom[worldIdx]);
                    miniMap.setCurrentRoom(row[worldIdx],column[worldIdx]);
                    
                    if(transitionInfo != TransitionDirection.TIMELINE) {
                        if(isInFinalRoom[worldIdx]) {
                            if(currentRoom[worldIdx] == finalRoom) {
                                createNewDungeon();
                            } else {
                                currentRoom[worldIdx] = finalRoom;
                                currentRoom[worldIdx].setPlayer(player[worldIdx]); // Add the player to the new room.
                                currentRoom[worldIdx].addCharacter(player[worldIdx]);
                                if(currentRoom[worldIdx].getCharacters().size()!=2) {
                                   currentRoom[worldIdx].setRightBlockade(true);
                                } else {
                                    currentRoom[worldIdx].setRightBlockade(false);
                                }
                            }
                            
                        } else {
                            currentRoom[worldIdx] = map[worldIdx+stageIndex[worldIdx]][row[worldIdx]][column[worldIdx]]; // Change the current room
                            currentRoom[worldIdx].setPlayer(player[worldIdx]); // Add the player to the new room.
                            currentRoom[worldIdx].addCharacter(player[worldIdx]);
                        }
                        
                    }
                    currentRoom[worldIdx].setVisited(true);
                    player[worldIdx].setCurrentRoom(currentRoom[worldIdx]);
                    currentRoom[worldIdx].resumeEnemies();
                    
                    /* Checks if the final boss' music should be played */
                    if (checkBossDistance()) {
                        
                        /* Stops the previous sounds and starts the final boss'
                        music (only if it wasn't already playing...) */
                        if (!MainClass.JUKEBOX.isPlaying(Playlist.THE_LURKING_BEAST)) {
                            
                            MainClass.JUKEBOX.stop();
                            MainClass.JUKEBOX.play(Playlist.THE_LURKING_BEAST, false, 5);
                        }
                    } else {
                        
                        /* If the player went to the boss room and came back 
                        (being, therefore, the boss' music is still playing), 
                        changes back the main level's music */
                        if (MainClass.JUKEBOX.isPlaying(Playlist.THE_LURKING_BEAST)) {
                            
                            MainClass.JUKEBOX.stop();
                            MainClass.JUKEBOX.play(Playlist.GUITAR_CONCERT, false, 10);
                        }
                    }
                    
                    // If the transition is between the outside and the inside...
                    if(wentInsideDungeon[worldIdx]) {
                        // Place the player in the spawnpoint of the room.
                        currentRoom[worldIdx].placePlayer(player[worldIdx]);
                        // If now the player is inside the dungeon (i.e. inside the array coordinates),
                        // change the flag.
                        if(column[worldIdx] >= 0 && row[worldIdx] >= 0)
                            wentInsideDungeon[worldIdx] = false;
                        
                    } else {
                        switch(transitionInfo) {
                            case UP:
                                player[worldIdx].setY(currentRoom[worldIdx].getMapHeight()-(player[worldIdx].getHeight()/2)-2);
                                break;
                            case DOWN:
                                player[worldIdx].setY(0-player[worldIdx].getHeight()/2-5);
                                break;
                            case LEFT:
                                player[worldIdx].setX(currentRoom[worldIdx].getMapWidth()-(player[worldIdx].getWidth()/2)-2);
                                break;
                            case RIGHT:
                                player[worldIdx].setX(0-player[worldIdx].getWidth()/2+2);
                                break;
                        }    
                    }
                }
            } else {
                opacityTransition -= 15;
                if (opacityTransition == 0) {
                    endTransition = false;
                    transition = false;

                }
            }
        }
        //</editor-fold>

        currentRoom[worldIdx].update(delta);

        playerInterface[worldIdx].update();
        
        soundInterface.update();
        
        /* If the player has died, the game state must be changed to one
        with a "you died" screen, or something */
        if (player [worldIdx].isDead()) {
            
            deathState();
        }
    }
    
    /**
     * Checks if the current room is near the boss' room, so the music can be 
     * played.
     * 
     * @return 
     *              <i>true</i> if the current room is near the final boss' 
     *          room, so the musci can be played.
     */
    private boolean checkBossDistance () {
       
        /* Minimum distance required to say that the current room is "near" 
        the boss' one */
        int minDistance = 1;
        
        
        /* The music will be played
        Current room = row [worldIndex], column[worldIndex} */
        return (Math.abs(row[worldIdx] - MapGenerator.bossRoomRow) <= minDistance
                &&
                Math.abs(column[worldIdx] - MapGenerator.bossRoomColumn) <= minDistance);
    }
    
    /**
     * Changes the current state for the death screen ({@link PlayerDeathState}).
     */
    private void deathState () {
        
        /* Before changing the state, stores the map into a temporary file */
//        storeMapState();
        
        Color fadeInColor = new Color (102, 0, 0); /* Dark red */
        Color fadeOutColor = new Color (51, 0, 0); /* Even more dark red */
        
        /* Enters the death screen's state with a fancy transition */
        game.enterState(MainClass.DEATH_STATE_ID,
                        new FadeOutTransition(fadeInColor), 
                        new FadeInTransition(fadeOutColor, 400));
    }
    
    /**
     * Opens the file that contains a definition of the map.
     *  <br>·If the file is empty or inexistent, returns a null object. 
     *  <br>·If the file contains a valid {@code Room [][][]} instance,
     * retrieves the information and deletes the file
     * 
     * @return 
     *          A tridimensional matrix containing the rooms that form this map.
     */
    private Room [][][] retrieveMap () {
        
        Room [][][] newLevelMap = new Room[2]
                                          [MapGenerator.rows]
                                          [MapGenerator.columns];
        String fileContent = new String();
        File file;
        
        try {
            
            FileInputStream fileInStr = new FileInputStream(MAP_FILE_PATH);
            
            try (ObjectInputStream ois = new ObjectInputStream(fileInStr)) {
                fileContent = (String) ois.readObject();
            }
            
            newLevelMap[0] = MapGenerator.getRepresentedMap(fileContent, "castle");
            newLevelMap[1] = MapGenerator.getRepresentedMap(fileContent, "temple");
            
            /* Now, deletes the file */
            file = new File(MAP_FILE_PATH);
            
            if ((file.exists()) && !file.isDirectory()) {
                
                if (!file.delete()) {
                    
                    System.out.println("Error deleting file.");
                }
            }
            
            return newLevelMap;
            
        } catch (IOException | ClassNotFoundException ex) {
            
            System.out.println("Exception at LevelState.retrieveMap(): " +
                                ex.getMessage());
            return null;
        }
    }
    
    /**
     * Stores the map into a file, so it can be retrieved later.
     */
    private void storeMapState () {
        
        /* Instead of storing all the rooms, with the characters, 
        the objects... stores a (way much smaller) string that represents
        the disposition of rooms on this level */
        String storeValue = MapGenerator.getMapRepresentation(currentMap);
        
        try {
            
            FileOutputStream file = new FileOutputStream(MAP_FILE_PATH);
            
            try (ObjectOutputStream oos = new ObjectOutputStream(file)) {
                oos.writeObject(storeValue);
            }
            
            System.out.println("Successfully stored at " + MAP_FILE_PATH);
            
        } catch (IOException ex) {
            
            System.out.println("Exception at LevelState.storeMapState(): "
                                + ex.getMessage());
        }
    }
    
    public static void changeTimeline() {
        
        changeMapNow = true;
    }
    
    /**
     * Shall be called when killing a boss, checks if both bosses are killed.
     * If so, it breaks the blockade that blocked the entrance to the next level.
     */
    public static void enableChangeOfMap() {
        if(levelBossKilled) {
            System.out.println("level boss killed is FALSSSSEEEEE now");
            levelBossKilled = false;
            map[stageIndex[0]][MapGenerator.bossRoomRow][MapGenerator.bossRoomColumn].setRightBlockade(false);
            map[stageIndex[1]+1][MapGenerator.bossRoomRow][MapGenerator.bossRoomColumn].setRightBlockade(false);
        } else {
            System.out.println("level boss killed is true now.");
            levelBossKilled = true;
        }
    }
    
/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */
    
    /**
     * Returns the current map that's been used.
     * 
     * @return 
     *              The value of {@code map}.
     */
    public Room [][][] getMap () {
        
        return map;
    }    
    
    /**
     * Changes the map to be used.
     * 
     * @param map 
     *              The new map for this level.
     */
    public void setMap (Room [][][] map) {
        
        this.map = map;
    } 

    public Player[] getPlayers() {
        return player;
    }

}
