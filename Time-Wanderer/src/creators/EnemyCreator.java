package creators;

import entities.BossEnemy;
import entities.FlyingEnemy;
import entities.GroundEnemy;
import levels.Room;
import org.newdawn.slick.geom.Vector2f;

/**
 * This class will control the creation of all the enemies on the level.
 */
public class EnemyCreator {


/* ------------------------ */
/* ---- FACTORY METHODS --- */
/* ------------------------ */

    /**
     * Returns a new flying enemy on the selected room in a "random" position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     *
     * @return
     *          A new instance of a FlyingEnemy.
     */
    public static FlyingEnemy newFlyingEnemy (int x, int y) {

        /* Generates new coordinates into the room */
        Vector2f coordinates = new Vector2f( x, y );        
        FlyingEnemy retVal = new FlyingEnemy (coordinates, 32, 32, 32, 100, 2);
        
        /* Sets the stats */
        retVal.getStats().setAttackDamage(10); /* Attack: 10 */
        retVal.getStats().setMaxLifePoints(40); /* Life: 40 */
        retVal.getStats().setLifePoints(40);
        retVal.getStats().setXpGiven(15); /* Gives 15 XP when is killed */

        return retVal;
    }

    /**
     * Returns a new ground enemy on the selected room in a "random" position.
     *
     * @param x X coordinate
     * @param y Y coordinate
     *
     * @return
     *          A new instance of a GroundEnemy.
     */
    public static GroundEnemy newGroundEnemy (int x, int y) {

        /* Generates new coordinates into the room */
        Vector2f coordinates = new Vector2f( x,y );
        GroundEnemy retVal = new GroundEnemy(coordinates, 32, 64, 50, 100, 2);
        
        /* Sets the stats */
        retVal.getStats().setAttackDamage(10); /* Attack: 10 */
        retVal.getStats().setMaxLifePoints(50); /* Life: 50 */
        retVal.getStats().setLifePoints(50);
        retVal.getStats().setXpGiven(10); /* Gives 10 XP when is killed */

        return retVal;
    }
    
    /**
     * Returns a new flying boss on the selected room.
     *
     * @param room
     *          Room on which the enemy will be created.
     *
     * @return
     *          A new instance of a BossEnemy.
     */
    public static BossEnemy newBoss (int x, int y, Room room) {

        /* Generates new coordinates into the room */
        Vector2f coordinates = new Vector2f( x, y );
        BossEnemy retVal = new BossEnemy(coordinates, 64, 128, 100, 250, (float) 0.5, 20, room);
        
        /* Sets the stats */
        retVal.getStats().setAttackDamage(50); /* Attack: 50 */
        retVal.getStats().setMaxLifePoints(500); /* Life: 500 */
        retVal.getStats().setXpGiven(100); /* Gives 100 XP when is killed */

        return retVal;
    }
}
