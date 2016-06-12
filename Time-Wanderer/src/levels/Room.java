package levels;

import entities.Enemy;
import levels.tiles.SolidTile;
import levels.tiles.Tile;
import levels.tiles.EmptyTile;
import entities.GameCharacter;
import entities.GameObject;
import entities.LevelButton;
import entities.Player;
import items.Item;
import items.MeleeWeapon;
import items.Projectile;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import levels.tiles.DamagingTile;
import levels.tiles.LadderTile;
import levels.tiles.PlatformTile;
import static levels.tiles.Tile.TILE_SIZE;
import misc.AttacksObserver;
import creators.EnemyCreator;
import entities.VersatileChest;
import items.Catalog;
import items.MagicalEffect;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Class that contains the state of a room. Contains the booleans that tell if
 * this room contains a door up, down, left right, strings to find the path of
 * both the .tmx files and the tileset used by that .tmx file, and two
 * arraylists: one to store the GameCharacters inside this room, and another to
 * store the GameObjects inside this room. GameCharacters are only inside the
 * characters arraylist, even if they inherit from gameobject too. Stores the
 * ported map from Tiled in an object of class TiledMap, and a 2d array of tiles
 * that represent all the different kind of tiles that form the room.
 *
 * @author sergio
 */
public class Room {

    private boolean upDoor;
    private boolean downDoor;
    private boolean leftDoor;
    private boolean rightDoor;

    private boolean upBlockade;
    private boolean downBlockade;
    private boolean leftBlockade;
    private boolean rightBlockade;

    private String representation;
    private String roomPath;
    private String tilesetName;
    private String roomName;

    private ConcurrentLinkedQueue<GameCharacter> characters;
    private ConcurrentLinkedQueue<GameObject> gameObjects;
    
    /**
     * List with all the projectiles fired by the player
     */
    private final ConcurrentLinkedQueue<Projectile> projectiles;
    
    /**
     * List with all the magical effects created by the player
     */
    private final ConcurrentLinkedQueue<MagicalEffect> magicalEffects;
    private ConcurrentLinkedQueue<Item> items;
    private Player player;

    private Tile[][] tiles;
    private TiledMap map;

    private float mapWidth;
    private float mapHeight;

    //private boolean isNight;
    
    private boolean visited = false;
    
    private boolean bossRoom = false;
    /**
     * Observer that will control every attack performed by the player.
     */
    private final AttacksObserver observer;

/* -------------------------------------- */
 /* ---- END OF ATTRIBUTES DECLARATION --- */
 /* -------------------------------------- */
    public Room(String s, String roomName, String tilesetName, boolean up,
            boolean down, boolean left, boolean right) {

        this.roomName = roomName;
        representation = s;
        this.upDoor = up;
        this.downDoor = down;
        this.leftDoor = left;
        this.rightDoor = right;
        this.tilesetName = tilesetName;
        this.roomPath = "resources/rooms/" + roomName + ".tmx";

        this.gameObjects = new ConcurrentLinkedQueue<>();
        this.projectiles = new ConcurrentLinkedQueue<>();
        this.magicalEffects = new ConcurrentLinkedQueue<>();
        this.characters = new ConcurrentLinkedQueue<>();
        this.items = new ConcurrentLinkedQueue<>();

        this.observer = new AttacksObserver(this);

        try {
            map = new TiledMap(roomPath, "resources/tilesets/" + tilesetName);
        } catch (SlickException ex) {
            System.out.println("Unable to load " + roomName + ".tmx file.\n" + ex.getMessage());
        }
        this.loadTileMap();
        this.mapWidth = tiles.length * TILE_SIZE;
        this.mapHeight = tiles[0].length * TILE_SIZE;
        
        float randomNumber = (float) (Math.random() * 100);
        //isNight = randomNumber >= 75;
        bossRoom = roomName.equals("boss");
        
    }

    public Room makeRoomCopy() {
        return new Room(representation, roomName, tilesetName, upDoor, downDoor, leftDoor, rightDoor);
    }

    /**
     * Method to render the TiledMap and everything inside the current room.
     *
     * @param g object from Graphics class used to render the room.
     */
    public void render(Graphics g) {
        if (!roomName.equals("outside")) {
            // Render all except the blockades
            for (int i = 0; i < 8; i++) {
                map.render(0, 0, i);
            }

            if (hasUpBlockade()) {
                map.render(0, 0, 8);
            }

            if (hasDownBlockade()) {
                map.render(0, 0, 9);
            }

            if (hasLeftBlockade()) {
                map.render(0, 0, 10);
            }

            if (hasRightBlockade()) {
                map.render(0, 0, 11);
            }

            /* Renders the player and all the objects on the room */
            player.render(g);

            for (GameObject obj : gameObjects) {
                obj.render(g);
            }

            for (GameCharacter c : characters) {
                c.render(g);
            }

            for (Item i : items) {

                i.render(g);
            }

            for (Projectile p : projectiles) {

                p.render(g);
            }
            
            for (MagicalEffect m : magicalEffects) {
                m.render(g);
            }
            
        } else {
            for (int i = 0; i < 8; i++) {
                map.render(0, 0, i);
            }

            /* Renders the player and all the objects on the room */
            player.render(g);

            for (GameObject obj : gameObjects) {
                obj.render(g);
            }

            for (GameCharacter c : characters) {
                c.render(g);
            }

            for (Item i : items) {
                i.render(g);
            }

            for (Projectile p : projectiles) {
                p.render(g);
            }
            
            for (MagicalEffect m : magicalEffects) {
                m.render(g);
            }
            
            map.render(0,0,8);
            
            //if(isNight)
            //    map.render(0,0,9);
        }
    }

    /**
     * Updates the position of everything on this room
     *
     * @param delta Milliseconds that took the computer to update and render the
     * last frame.
     */
    public void update(int delta) {

        MeleeWeapon melee;

        /**
         * First, updates the player's weapons
         */
        if ((melee = player.getInventory().getMeleeWeapon()) != null) {
            melee.update();
        }

        /* Iterates through the list of game characters updating the enemies.
        If any of them is dead, removes it from the list */
        for (GameCharacter c : characters) {
            Enemy aux;

            if (c instanceof Enemy) {
                aux = (Enemy) c;
                aux.update(player, delta);
                
                /* If the enemy has died, removes it from the list and adds the
                experience to the player */
                if (aux.isDead()) {
                    
                    removeCharacter(c);
                    player.getStats().addXp (c.getStats().getXpGiven());
                }
            }
        }

        /* Updates the position of the projectiles */
        for (Projectile p : projectiles) {

            p.update(delta);
        }
        
        /* Updates the state of the effects */
        for (MagicalEffect m : magicalEffects) {

            m.update(delta);
        }

        observer.update();
    }

    /**
     * Method to load each tile of the map into a matrix. Helpful for static
     * tiles that need to collide with other moving entities.
     */
    public void loadTileMap() {
        tiles = new Tile[map.getWidth()][map.getHeight()];

        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                tiles[i][j] = new EmptyTile(i, j);
            }
        }

        loadTileLayer("CollisionLayer");
        loadTileLayer("PlatformLayer");
        if(!roomName.equals("outside")) {
            loadTileLayer("DamagingLayer");
            loadTileLayer("LadderLayer");
            loadTileBlockadeLayer("UpBlockingLayer", 0);
            loadTileBlockadeLayer("DownBlockingLayer", 1);
            loadTileBlockadeLayer("LeftBlockingLayer", 2);
            loadTileBlockadeLayer("RightBlockingLayer", 3);
        }
    }

    /**
     * Spawn enemies, and some other
     * objects.
     */
    public void fillObjectLayer() {
        Random random = new Random();

        int numberOfEnemies = map.getObjectCount(0);
        
        int x, y;
        for (int i = 0; i < numberOfEnemies; i++) {
            x = map.getObjectX(0, i);
            y = map.getObjectY(0, i);
            if(bossRoom) {
                this.addCharacter(EnemyCreator.newBoss(x,y,this));
            } else {
                if (random.nextInt(2) == 0) {
                    this.addCharacter(EnemyCreator.newFlyingEnemy(x, y));
                } else {
                    this.addCharacter(EnemyCreator.newGroundEnemy(x, y));
                }
            }
            
        }
        
        int numberOfChests = map.getObjectCount(3);
        for(int i = 0; i < numberOfChests; i++) {
            if(random.nextInt(100) < 35) {
                x = map.getObjectX(3,i);
                y = map.getObjectY(3,i);
                
                Catalog[] types = Catalog.values();
                int numberOfTypes = types.length;
                
                Catalog type = types[random.nextInt(numberOfTypes)];
                this.addGameObject(new VersatileChest(x,y,32,32,this,type));
            }
        }
    }
    public void placeButton(LevelButton button) {
        int x = map.getObjectX(2,0);
        int y = map.getObjectY(2,0);
        button.setX(x);
        button.setY(y);
        
    }
    
    public void placePlayer(Player player) {
        int x = map.getObjectX(1, 0);
        int y = map.getObjectY(1, 0);
        player.setX(x);
        player.setY(y);
    }

    private void loadTileLayer(String layerName) {

        int layerIndex;

        layerIndex = map.getLayerIndex(layerName);

        if (layerIndex != -1) {
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    int tileID = map.getTileId(x, y, layerIndex);

                    Tile tile = null;
                    // Obtain the property of the tile. If the property is 
                    // "empty", then it is an emptytile. by default, in that layer
                    // the tiles will be the tiles corresponding to the layer name.
                    switch (map.getTileProperty(tileID, "tileType", "non-empty")) {
                        case "empty":
                            break;
                        default:
                            switch (layerName) {
                                case "CollisionLayer":
                                    tile = new SolidTile(x, y);
                                    break;
                                case "PlatformLayer":
                                    tile = new PlatformTile(x, y);
                                    break;
                                case "DamagingLayer":
                                    tile = new DamagingTile(x, y);
                                    break;
                                case "LadderLayer":
                                    tile = new LadderTile(x, y);
                                    break;
                            }

                            tiles[x][y] = tile;
                            break;
                    }
                }
            }
        }
    }

    private void loadTileBlockadeLayer(String layerName, int direction) {

        int layerIndex;
        // 0: up   1: down  2: left  3: right.

        if ((upBlockade && direction == 0) || (downBlockade && direction == 1)
                || (leftBlockade && direction == 2) || (rightBlockade && direction == 3)) {

            layerIndex = map.getLayerIndex(layerName);
            if (layerIndex != -1) {
                for (int x = 0; x < map.getWidth(); x++) {
                    for (int y = 0; y < map.getHeight(); y++) {
                        int tileID = map.getTileId(x, y, layerIndex);

                        Tile tile = null;

                        // Obtain the property of the tile. If the property is 
                        // "empty", then it is an emptytile. by default, in that layer
                        // the tiles will be damagingtiles.
                        switch (map.getTileProperty(tileID, "tileType", "blockade")) {
                            case "empty":
                                break;
                            default:
                                tile = new SolidTile(x, y);
                                tiles[x][y] = tile;
                                break;
                        }
                    }
                }
            }
        }
    }

    public void stopEnemies() {
        for (GameCharacter gc : characters) {
            if (gc instanceof Enemy) {
                ((Enemy) gc).stop();
            }
        }
    }

    public void resumeEnemies() {
        for (GameCharacter gc : characters) {
            if (gc instanceof Enemy) {
                ((Enemy) gc).restart();
            }
        }
    }

    /**
     * Prints the representation of the different kind of tiles that appear in
     * this room.
     */
    private void printTileMap() {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {

                if (tiles[j][i] instanceof EmptyTile) {
                    System.out.print("-");
                } else if (tiles[j][i] instanceof SolidTile) {
                    System.out.print("x");
                } else if (tiles[j][i] instanceof LadderTile) {
                    System.out.print("L");
                } else if (tiles[j][i] instanceof PlatformTile) {
                    System.out.print("_");
                } else if (tiles[j][i] instanceof DamagingTile) {
                    System.out.print("^");
                }
            }
            System.out.println();
        }

    }

    /**
     * Method that checks if the room's doors match with the ones passed as
     * parameter.
     *
     * @param doors Booleans that correspond to each door's appearance.
     * @return True or false depending if they match or not.
     */
    public boolean validLevel(boolean[] doors) {
        return (doors[0] == leftDoor
                && doors[1] == upDoor
                && doors[2] == rightDoor
                && doors[3] == downDoor);
    }

    // --- Getters and setters ---
    public boolean hasUpDoor() {
        return upDoor;
    }

    public void setUpDoor(boolean upDoor) {
        this.upDoor = upDoor;
    }

    public boolean hasDownDoor() {
        return downDoor;
    }

    public void setDownDoor(boolean downDoor) {
        this.downDoor = downDoor;
    }

    public boolean hasLeftDoor() {
        return leftDoor;
    }

    public void setLeftDoor(boolean leftDoor) {
        this.leftDoor = leftDoor;
    }

    public boolean hasRightDoor() {
        return rightDoor;
    }

    public void setRightDoor(boolean rightDoor) {
        this.rightDoor = rightDoor;
    }

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {

        this.player = player;

        /* Sets it as main player on the observer, too */
        observer.setPlayer(player);
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    public boolean hasUpBlockade() {
        return upBlockade;
    }

    public void setUpBlockade(boolean upBlockade) {
        this.upBlockade = upBlockade;
        loadTileMap();
    }

    public boolean hasDownBlockade() {
        return downBlockade;
    }

    public void setDownBlockade(boolean downBlockade) {
        this.downBlockade = downBlockade;
        loadTileMap();
    }

    public boolean hasLeftBlockade() {
        return leftBlockade;
    }

    public void setLeftBlockade(boolean leftBlockade) {
        this.leftBlockade = leftBlockade;
        loadTileMap();
    }

    public boolean hasRightBlockade() {
        return rightBlockade;
    }

    public void setRightBlockade(boolean rightBlockade) {

        this.rightBlockade = rightBlockade;
        loadTileMap();
    }

    @Override
    public String toString() {
        return getRepresentation();
    }

    /**
     * Add a character to the list of this room's characters.
     */
    public void addCharacter(GameCharacter c) {
        characters.add(c);

        /* If it's a player, sets it as main player on the observer */
        if (c instanceof Player) {
            observer.setPlayer((Player) c);
        } else {
            observer.addObserved(c);
        }
    }

    /**
     * Remove a character from the list of this room's characters.
     */
    public void removeCharacter(GameCharacter c) {
        characters.remove(c);
        /* Removes it also from the observer */
        observer.removeObserved(c);
    }

    /**
     * Retrieves the list of characters in this room.
     */
    public ConcurrentLinkedQueue<GameCharacter> getCharacters() {
        return characters;
    }

    /**
     * Adds a new item on the room.
     *
     * @param item The item to be added
     */
    public void addItem(Item item) {

        /* Checks if it already was on the list to avoid duplicates */
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    /**
     * Removes an item from the list
     *
     * @param item The item to be removed.
     */
    public void removeItem(Item item) {

        items.remove(item);
    }

    /**
     * Adds a game object to the list of this room's game objects.
     */
    public void addGameObject(GameObject obj) {
        gameObjects.add(obj);
    }

    /**
     * Retrieves the list of gameobjects in this room.
     */
    public ConcurrentLinkedQueue<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void removeObject(GameObject obj) {
        gameObjects.remove(obj);
    }

    public void removeObjects(ArrayList<GameObject> objects) {
        gameObjects.removeAll(objects);
    }

    
   
/* --------------------------------- */
/* ---- OBSERVER-RELATED METHODS --- */
/* --------------------------------- */
    /**
     * Checks if the (melee) attack of the player has hit any enemy.
     */
    public void checkMeleeAttack() {

        observer.checkAttack();
    }

    /**
     * Adds a new projectile to the list.
     *
     * @param projectile Projectile that the player just fired and must be
     * added.
     */
    public void addObservedProjectile(Projectile projectile) {

        observer.addProjectile(projectile);
        projectiles.add(projectile);
    }
    
    /**
     * Removes a projectile from the list.
     *
     * @param projectile
     *              Object to be removed.
     */
    public void removeObservedProjectile (Projectile projectile) {

        projectiles.remove(projectile);
    }
    
    /**
     * Adds a new projectile to the list.
     *
     * @param effect
     *              Effect that the player just created and must be added.
     */
    public void addObservedEffect (MagicalEffect effect) {

        observer.addEffect(effect);
        magicalEffects.add(effect);
    }
    
    /**
     * Removes the given magical effect from the list.
     *
     * @param effect 
     *              Object to be removed.
     */
    public void removeObservedEffect (MagicalEffect effect) {

        magicalEffects.remove(effect);
    }


    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    /** Method that returns an integer representing the doors of the room,
     *  as the translation in decimal of the binary codification of the doors,
     * u d l r -> binary -> decimal.
     * 
     */
    public int getIntRepresentation() {
        int number = 0;
        if(rightDoor) {
            number += 1;
        }
        if(leftDoor) {
            number += 2;
        }
        if(downDoor) {
            number += 4;
        }
        if(upDoor) {
            number += 8;
        }
        return number;
    }
}
