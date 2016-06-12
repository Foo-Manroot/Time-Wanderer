package physics;

import entities.FlyingEnemy;
import entities.GameCharacter;
import entities.Player;
import entities.GameObject;
import java.util.ArrayList;
import levels.Room;
import levels.tiles.DamagingTile;
import levels.tiles.LadderTile;
import levels.tiles.PlatformTile;
import levels.tiles.SolidTile;
import levels.tiles.Tile;

/**
 * Class that contains the methods to handle the whole physics 'engine' of
 * a single map.
 * @author sergio
 */
public class Physics {

    private final float gravity = 0.003f;
    
    /**
     * Public method to apply all physics behaviour to all entities.
     * @param level Level where the entities are located.
     * @param delta Delta value to move properly.
     */
    public void handlePhysics(Room level, int delta) {
        handleCharacters(level, delta);
        handleAllGameObjects(level, delta);
    }
    /**
     * Method to handle the character's special physics.
     * Also applies the standard GameObject physics (since characters are also
     * GameObjects).
     * @param level Level where the entities are located.
     * @param delta Delta value to move properly.
     */
    private void handleCharacters(Room level, int delta) {
        for (GameCharacter c : level.getCharacters()) {

            // Decelerate the character if he is not moving anymore
            if (!c.isMoving()) {
                c.decelerate(delta);
            }

            handleGameObject(c, level, delta);
            
            if(!c.getIgnoresCollisions()) {
                if(isOnDamaging(c, level.getTiles())){
                    c.getHit(DamagingTile.DAMAGE);
                }
            }
            
            // Special cases for the player
            if (c instanceof Player) {
                // If the player is touching any ladder, it can climb.
                if(checkLadder(c, level.getTiles())) {
                    ((Player) c).setCanClimb(true);
                } else {
                    // If not, then stop climbing if the player is climbing.
                    // Set canClimb to false, too.
                    ((Player) c).setCanClimb(false);
                    c.setIgnoresGravity(false);
                    ((Player) c).setIsClimbing(false);
                }
            }

        }
    }
    /**
     * Method that calls handleGameObject for all of the level's gameObjects.
     * @param level Level to take out the gameObjects from.
     * @param delta Delta value to move properly.
     */
    private void handleAllGameObjects(Room level, int delta) {
        for (GameObject obj : level.getGameObjects()) {
            handleGameObject(obj, level, delta);
        }
    }
    /**
     * Compute and move the gameobject according to gravity.
     * @param obj GameObject to apply gravity and other movement forces.
     * @param level Current level where the GameObject is.
     * @param delta Delta value to move properly.
     */
    private void handleGameObject(GameObject obj, Room level, int delta) {
        
        /* If the game object is not on a platform, reset the value
            that tells if the game object should ignore the platform
            collision.
        */
        if(!isOnPlatform(obj, level.getTiles())) {
            if(!(obj instanceof FlyingEnemy)) // A flying enemy should never stop ignoring platforms.
                obj.setIgnoresPlatforms(false);
        }
        //first update the onGround of the object
        /* If the gameobject is ignoring platforms, i.e. it wants to step down
           from a platform, trick it telling it is not on the floor so normal
           gravity will be applied to it: ignore the platform's collision.
           If not, check normally if the gameobject is on any kind of ground
           (be it a SolidTile or a PlatformTile).
        */
        if(obj.getIgnoresPlatforms()) {
            obj.setOnGround(false);
        } else {
            obj.setOnGround(isOnGround(obj, level.getTiles()));
        }
        /* If the object is not on ground, or it is about to jump (negative Y velocity),
           apply the gravity force on it. If not, it means that it is on the ground,
           there's no need to apply gravity in the Y velocity axis.
        */
        // If the object 'is climbing', we don't need to touch its Y velocity.
        // Otherwise, apply gravity if not on ground.
        if(!obj.getIgnoresGravity()) {
            if (!obj.isOnGround() || obj.getYVelocity() < 0) {
                obj.applyGravity(gravity * delta);
            } else {
                obj.setYVelocity(0);
            }
        }
        
        
        // Calculate how much we actually have to move
        float x_movement = obj.getXVelocity() * delta;
        float y_movement = obj.getYVelocity() * delta;

        /* We have to calculate the step we have to take: the step helps to
           move 'step by step' to the x_movement or y_movement. This is useful
           to avoid wrong movement calculations in cases of fps drops where the
           gameobject suddenly goes through a tile where it should collide, getting
           it stuck. The code below takes care of that.
        */
        
        float step_y = 0;
        float step_x = 0;

        if (x_movement != 0) {
            
            step_y = Math.abs(y_movement) / Math.abs(x_movement);
            if (y_movement < 0) {
                step_y = -step_y;
            }

            if (x_movement > 0) {
                step_x = 1;
            } else {
                step_x = -1;
            }

            if ((step_y > 1 || step_y < -1) && step_y != 0) {
                step_x = Math.abs(step_x) / Math.abs(step_y);
                if (x_movement < 0) {
                    step_x = -step_x;
                }
                if (y_movement < 0) {
                    step_y = -1;
                } else {
                    step_y = 1;
                }
            }
        } else if (y_movement != 0) {
            // If we only have vertical movement, we can just use a step of 1
            if (y_movement > 0) {
                step_y = 1;
            } else {
                step_y = -1;
            }
        }

        // And then do little steps until we are done moving
        while (x_movement != 0 || y_movement != 0) {

            // We first move in the x direction
            if (x_movement != 0) {
                // When we do a step, update the amount we have to move after this
                if ((x_movement > 0 && x_movement < step_x) || 
                    (x_movement > step_x && x_movement < 0)) {
                    
                    step_x = x_movement;
                    x_movement = 0;
                    
                } else {
                    x_movement -= step_x;
                }

                // Then we move the object one step
                obj.setX(obj.getX() + step_x);

                // If we collide with any of the bounding shapes of the tiles,
                // we have to revert to our original position.
                if (checkCollision(obj, level.getTiles())) {

                    // Undo our step, and set the velocity and amount we still 
                    // have to move to 0 because we can't move in that direction
                    obj.setX(obj.getX() - step_x);
                    obj.setXVelocity(0);
                    x_movement = 0;
                }

            }
            // Same thing for the vertical, or y movement
            if (y_movement != 0) {
                if ((y_movement > 0 && y_movement < step_y) || 
                    (y_movement > step_y && y_movement < 0)) {
                    
                    step_y = y_movement;
                    y_movement = 0;
                    
                } else {
                    y_movement -= step_y;
                }
                // Then we move the object one step
                obj.setY(obj.getY() + step_y);

                // If we collide with any of the bounding shapes of the tiles,
                // we have to revert to our original position.
                if (checkCollision(obj, level.getTiles())) {
                      
                    // Undo our step, and set the velocity and amount we still 
                    // have to move to 0 because we can't move in that direction
                    obj.setY(obj.getY() - step_y);
                    obj.setYVelocity(0);
                    y_movement = 0;
                    break;
                }
                
                // If the gameobject is not ignoring platforms (or collisions),
                // check if the object is on a platform and in that case, set
                // its Y position properly: in cases of FPS drops and since
                // platforms have a different collision system than SolidTiles,
                // the gameobject might go inside the bounding shape of the platform,
                // instead of being on top of it.
                // Also fixes the bug where the gameobject might be below the
                // top of the platform but still detects it as the ground tile,
                // making it to float. If that is the case, the method tells
                // the gameobject to ignore platforms. (Until there's no platforms
                // below).
                if(!obj.getIgnoresPlatforms())
                    fixPlatform(obj, level.getTiles());
            }
        }
        
        // If the object is climbing, since we have already moved, set the velocity
        // to 0.
        if(obj instanceof Player) {
            if(((Player)obj).getIsClimbing()) {
                obj.setYVelocity(0);
            }
        }

        
    }
    /**
     * Check if a GameObject is colliding with a tile with BoundingShape in
     * any of its occupied tiles.
     * @param obj GameObject to check the tiles it occupies.
     * @param mapTiles Tiles of the map.
     * @return True if collision exists, false if not.
     */
    private boolean checkCollision(GameObject obj, Tile[][] mapTiles) {
        if(obj.getIgnoresCollisions()) // If the obj is ignoring collisions,
            return false;              // always return false.
        
        // First check if it collides with a solid tile.
        if(checkCollisionWithSolidTile(obj, mapTiles)) {
            return true;
        } else if(!obj.getIgnoresPlatforms()) {
            //get only the tiles that matter
            ArrayList<Tile> tiles = obj.getBoundingShape().getTilesOccupying(mapTiles);


            for (Tile t : tiles) {
                //if this tile has a bounding shape
                if (t.getBoundingShape() != null) {
                    if(t instanceof PlatformTile) {
                        BoundingShape aux = obj.getBoundingShape();
                        if(aux.getY()+aux.getHeight() <= t.getBoundingShape().getY()) {
                            return true;
                        }
                    } else if (t.getBoundingShape().checkCollision(obj.getBoundingShape())) {
                        return true;
                    }
                }

            }
        }
        return false;
    }
    /**
     * Check if a GameObject is colliding with a SolidTile
     * @param obj GameObject to check the tiles it occupies.
     * @param mapTiles Tiles of the map.
     * @return True if collision exists, false if not.
     */
    private boolean checkCollisionWithSolidTile(GameObject obj, Tile[][] mapTiles) {
        //get only the tiles that matter
        ArrayList<Tile> tiles = obj.getBoundingShape().getTilesOccupying(mapTiles);
        
        
        for (Tile t : tiles) {
            //if this tile has a bounding shape
            if (t.getBoundingShape() != null) {
                if(t instanceof SolidTile) {
                    if (t.getBoundingShape().checkCollision(obj.getBoundingShape())) 
                        return true;
                }
            }
        }
        return false;
    }
    /**
     * Check if a GameObject is colliding with LadderTile in
     * any of its occupied tiles.
     * @param obj GameObject to check the tiles it occupies.
     * @param mapTiles Tiles of the map.
     * @return True if collision exists, false if not.
     */
    private boolean checkLadder(GameObject obj, Tile[][] mapTiles) {
        //get only the tiles that matter
        ArrayList<Tile> tiles = obj.getBoundingShape().getTilesOccupying(mapTiles);

        for (Tile t : tiles) {
            if (t instanceof LadderTile) {
                return true;
            }
        }
        return false;
    }
    /**
     * Method to check if there are tiles underneath with a BoundingShape.
     * On a normal state, any tile with a boundingshape should be considered
     * as a tile that the gameobject should collide with.
     * @param obj GameObject to check the tiles underneath it.
     * @param mapTiles Tiles of the map
     * @return True if there are, false if not.
     */
    private boolean isOnGround(GameObject obj, Tile[][] mapTiles) {
        // We get the tiles that are directly "underneath" the characters,
        // also known as the ground tiles
        ArrayList<Tile> tiles = obj.getBoundingShape().getGroundTiles(mapTiles);
        
        // We lower the the bounding object a bit so we can check if we are 
        // actually a bit above the ground
        obj.getBoundingShape().movePosition(0, 1);

        for (Tile t : tiles) {
            // Not every tile has a bounding shape (empty tiles for example)
            if (t.getBoundingShape() != null) {
                // If the ground and the lowered object collide, 
                // then we are on the ground
                if (t.getBoundingShape().checkCollision(obj.getBoundingShape())) {
                    // Don't forget to move the object back up 
                    // even if we are on the ground!
                    obj.getBoundingShape().movePosition(0, -1);
                    return true;
                }
            }
        }

        // And obviously we have to move the object back up if we don't hit the ground
        obj.getBoundingShape().movePosition(0, -1);

        return false;
    }
    
    /**
     * Method to check if the tiles below the GameObject contains a platform tile
     * AND if these tiles dont contain any solid tile.
     * Used to know when is the GameObject able to step down a platform.
     * @param obj GameObject to check the tiles underneath it.
     * @param mapTiles Tiles of the map
     * @return True if there are, false if not.
     */
    private boolean isOnPlatform(GameObject obj, Tile[][] mapTiles) {
        //we get the tiles that are directly "underneath": "ground tiles"
        ArrayList<Tile> tiles = obj.getBoundingShape().getGroundTiles(mapTiles);
        int counter = 0;
        
        boolean containsPlatforms = false;
        // If there are no PlatformTiles below, directly return false.
        for (Tile t : tiles) {
            if(t instanceof PlatformTile) {
                containsPlatforms = true;
                break;
            }
        }
        if(!containsPlatforms)
            return false;
        
        // We lower the the bounding object a bit so we can check if we are 
        // actually a bit above the ground.
        obj.getBoundingShape().movePosition(0, 1);

        for (Tile t : tiles) {
            //not every tile has a bounding shape (empty tiles for example)
            if (t.getBoundingShape() != null) {
                counter++;// Increment counter of tiles that have boundingShapes
                if (t.getBoundingShape().checkCollision(obj.getBoundingShape())) {
                    if(t instanceof PlatformTile) {
                        counter--;
                    }
                }
            }
        }
        //don't forget to move the object back up even if we are on the ground!
        obj.getBoundingShape().movePosition(0, -1);
        // If the counter is different than 0, it means that atleast one of the
        // tiles below is not a PlatformTile and contains a BoundingShape, so
        // we have to collide with it. We can't step down.
        return counter == 0;
            

    }
     /**
     * Method to check if a character is above a DamagingTile.
     * @param obj GameCharacter to check the tiles underneath it.
     * @param mapTiles Tiles of the map
     * @return True if there are, false if not.
     */
    private boolean isOnDamaging(GameCharacter character, Tile[][] mapTiles) {
        //we get the tiles that are directly "underneath": "ground tiles"
        ArrayList<Tile> tiles = character.getBoundingShape().getGroundTiles(mapTiles);
        
        // We lower the the bounding object a bit so we can check if we are 
        // actually a bit above the ground.
        character.getBoundingShape().movePosition(0, 1);

        for (Tile t : tiles) {
            //not every tile has a bounding shape (empty tiles for example)
            if (t.getBoundingShape() != null) {
                if (t.getBoundingShape().checkCollision(character.getBoundingShape())) {
                    if(t instanceof DamagingTile) {
                        // Move the character back up.
                        character.getBoundingShape().movePosition(0, -1);
                        return true;
                    }
                }
            }
        }
        // If reached here, then there was no DamagingTile below.
        // Don't forget to move the object back up even if we are on the ground!
        character.getBoundingShape().movePosition(0, -1);
        return false;
        
    }
    /**
     * Method to fix the Y position when walking on platforms.
     * Forces the gameobject's lower vertical point to be on top of the platform
     * in case it is walking on one.
     * If the gameObject is actually below the 'top' of the platform, and
     * is 'floating' in mid-air because his ground tiles are still the platform,
     * forces it to step down.
     * @param obj GameObject whose Y coordinate we want to fix.
     * @param mapTiles Tiles of the map, used to take out the platform tiles.
     */
    private void fixPlatform(GameObject obj, Tile[][] mapTiles) {
        ArrayList<Tile> tiles = obj.getBoundingShape().getGroundTiles(mapTiles);
        //obj.getBoundingShape().movePosition(0, 1);
        for (Tile t : tiles) {
            if (t instanceof PlatformTile) {
                BoundingShape tbs = t.getBoundingShape();
                BoundingShape obs = obj.getBoundingShape();
                if((obs.getY()+obs.getHeight()) >= tbs.getY() && 
                        (obs.getY()+obs.getHeight()) <= tbs.getY() + 4 ) {
                    obj.getBoundingShape().movePosition(0, -1);
                    obj.setY(obs.getY()); 
                }
                
            }
        }
        
        if(isOnPlatform(obj, mapTiles)) {
            tiles = obj.getBoundingShape().getGroundTiles(mapTiles);
            for(Tile t : tiles) {
                if (t instanceof PlatformTile) {
                    BoundingShape tbs = t.getBoundingShape();
                    if((obj.getY()+obj.getHeight()) > (tbs.getY()+4)) {
                        obj.setIgnoresPlatforms(true);
                    }
                        
                }
            }
        }
        
        
    }
}
