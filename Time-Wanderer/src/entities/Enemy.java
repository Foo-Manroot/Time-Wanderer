package entities;

import org.newdawn.slick.geom.Vector2f;

/**
 * This class represents a generic enemy. The specific classes that represents
 * the enemies should be this class' child.
 */
public abstract class Enemy extends GameCharacter {

    /* ATTRIBUTES: */

    /**
     * Sets the range of the enemy's weapon. If it's <= (width / 2), this
     * character will have a melee weapon. If not, it'll be a ranged weapon.
     */
    protected float range;

    /**
     * This attribute sets the distance on which the enemy will "see" the player
     */
    protected float detectDistance;


/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */

    /* METHODS: */

    /**
     * Constructor.
     * See below to set the default value of some attributes.
     *
     * @param coordinates
     *              Initial coordinates of this enemy.
     * @param width
     *              Width of the object.
     * @param height
     *              Height of the object.
     * @param range
     *              Range of the enemy's weapon. To set the default value, this
     *              argument should be negative.
     * @param detectDistance
     *              Distance on which the enemy will "see" the player.
     *              To set the default value, this argument should be negative.
     */
    public Enemy(Vector2f coordinates, int width, int height,
                 float range, float detectDistance) {

        /* Calls GameCharacter's constructor */
        super(coordinates.x, coordinates.y, width, height);

        /* If range or detectDistance are below 0, the defualt values are set */
        this.range = (range < 0)? (width / 2): range; /* Default: melee */
        this.detectDistance = (detectDistance < 0)? range * 2 : detectDistance;
    }

    /**
     * Updates the state of the enemy. Here, the enemy's behaviour is controlled.
     *
     * @param target The player that this enemy will try to hit.
     * @param delta Milisecinds that took the computer to render the image.
     */
    public void update (Player target, int delta) {

        /* Calls the method that sets the next position of the body */
        move (target, delta);

        /* Finally, updates the position of the character's body */
        super.updateBoundingShape();
    }

    /**
     * Once a collision with any of the player's weapon is detected, this method
     * decrease this enemy's health or play die animation, if health <= 0
     */
    @Override
    protected void die () {
        
        /* Until implemented, just stops its action */
        this.stop();
    }
    
    /**
     * This method will stopm the action of this enemy.
     */
    public abstract void restart ();
    
    /**
     * This method will restart the action of this enemy
     */
    public abstract void stop ();
    
    /**
     * Returns <i>true</i> if the action of this enemy is stopped, <i>false</i>
     * otherwise.
     * 
     * @return 
     */
    public abstract boolean isStopped();
    
    /**
     * Returns <i>true</i> if the enemy has died.
     *
     * @return
     *          The value of the attribute <b>dead</b>.
     */
    public abstract boolean isDead ();


    /**
     * This method, when implemented, will perform the attack of the enemy.
     *
     * @param target The player that will be the main target of this enemy.
     * @param delta Miliseconds that took the computer to render the image.
     */
    protected abstract void attack (Player target, int delta);


    /**
     * This method, when implemented, will control the path that this
     * enemy will follow.
     *
     * @param target The player that will be the main target of this enemy.
     * @param delta Miliseconds that took the computer to render the image.
     */
    protected abstract void move (Player target, int delta);
    
    
}
