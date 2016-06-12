package entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * Class that represents any Character (be it enemy, the player, or ally).
 * Inherits from GameObject, since any character will be inside a level and will
 * be affected by physics (gravity, movement, etc). Contains some attributes
 * that will be used in physics calculation: acceleration speed, deceleration speed,
 * maximum speed (names are self-explanatory). Also contains a Facing enum to
 * know if the character is facing left or right, a boolean to know if the character
 * is moving and the stats of the character.
 * For now, the sprite that this character will be represented with is just a
 * placeholder rectangle, which must be changed later.
 * @author sergio
 */
public class GameCharacter extends GameObject {

    public enum Facing {
        LEFT, RIGHT;
    }

    private Facing facing;
    private boolean moving = false;
    private float accelerationSpeed = 1;
    private float decelerationSpeed = 1;
    private float maximumSpeed = 1;
    private Rectangle sprite;


    private Stats characterStats;

    public GameCharacter(float x, float y, int width, int height) {
        super(x, y, width, height);
        facing = facing.RIGHT;
        sprite = new Rectangle(x,y,width,height);

        characterStats=new Stats();
    }

    public GameCharacter(float x, float y, int width, int height,
            Stats charStats) {
        super(x, y, width, height);
        facing = facing.RIGHT;
        sprite = new Rectangle(x,y,width,height);

        characterStats= charStats;
    }


    /**
     * Handles the character's death.
     */
    protected void die () {
    }

    /**
     * Simulates a hit that decrements the health of the character.
     *
     * @param damage Damage that the object made to this character.
     */
    public void getHit (int damage) {

        if ((characterStats.getLifePoints() - damage) <= 0) {
            
            characterStats.setLifePoints(0);
            die ();
        } else {
            
            characterStats.decreaseLife(damage);
        }
    }

    /**Set stats of the entity
     * @param st Stats to be set to the entity.*/
    public void setStats(Stats st){
        this.characterStats=st;
    }

    /**Get stats of the entity.
     * @return the stats of the entity*/
    public Stats getStats(){
        return this.characterStats;
    }

    /**
     * Decelerates the character's X velocity in case it is not moving anymore.
     * Uses the decelerationSpeed parameter to partially decrease the horizontal
     * velocity.
     * @param delta
     */
    public void decelerate(int delta) {
        if(getXVelocity() > 0){
            setXVelocity(getXVelocity() - decelerationSpeed * delta);
            if(getXVelocity() < 0)
                setXVelocity(0);
        }else if(getXVelocity() < 0){
            setXVelocity(getXVelocity() + decelerationSpeed * delta);
            if(getXVelocity() > 0)
                setXVelocity(0);
        }
    }

    /**
     * Method to make this character jump.
     * If the character is on the ground, and it is not moving vertically,
     * then it is able to jump. Changes the Y velocity to negative values.
     */
    public void jump() {
        if(isOnGround() && getYVelocity() == 0) {
            setYVelocity(-0.8f);
        }
    }
    /**
     * Method to make this characater move to the left.
     * Using acceleration, changes the X velocity to negative values.
     * (goes to the left that way).
     * @param delta
     */
    public void moveLeft(int delta) {
        if(getXVelocity() > -maximumSpeed) {
            setXVelocity(getXVelocity() - accelerationSpeed*delta);
            if(getXVelocity() < -maximumSpeed){
                //and if we exceed maximum speed, set it to maximum speed
                setXVelocity(-maximumSpeed);
            }
        }
        // The character is moving.
        setMoving(true);
        // If moving left, then the character is facing the left side.
        setFacing(Facing.LEFT);
    }

    /**
     * Method to make this characater move to the right.
     * Using acceleration, changes the X velocity to positive values.
     * (goes to the right that way).
     * @param delta
     */
    public void moveRight(int delta) {
       if(getXVelocity() < maximumSpeed) {
            setXVelocity(getXVelocity() + accelerationSpeed*delta);
            if(getXVelocity() > maximumSpeed){
                //and if we exceed maximum speed, set it to maximum speed
                setXVelocity(maximumSpeed);
            }
        }
        // The character is moving.
        setMoving(true);
        // If moving right, then the character is facing the right side.
        setFacing(Facing.RIGHT);
    }

    /**
     * Method to make this characater move up.
     * Using acceleration, changes the Y velocity to negative values.
     *
     * @param delta
     */
    public void moveUp(int delta) {
       if(getYVelocity() > -maximumSpeed) {
            setYVelocity(getYVelocity() - accelerationSpeed*delta);
            if(getYVelocity() < -maximumSpeed){
                //and if we exceed maximum speed, set it to maximum speed
                setYVelocity(-maximumSpeed);
            }
        }
        // The character is moving.
        setMoving(true);
    }

    /**
     * Method to make this characater move down.
     * Using acceleration, changes the Y velocity to positive values.
     *
     * @param delta
     */
    public void moveDown(int delta) {
       if(getYVelocity() < maximumSpeed) {
            setYVelocity(getYVelocity() + accelerationSpeed*delta);
            if(getYVelocity() > maximumSpeed){
                //and if we exceed maximum speed, set it to maximum speed
                setYVelocity(maximumSpeed);
            }
        }
        // The character is moving.
        setMoving(true);
    }

    /**
     * Render method, where the animation/sprite should be rendered.
     * For now, it just renders a placeholder rectangle.
     * @param g
     */
    @Override
    public void render(Graphics g) {
        g.fillRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
        //this.getBoundingShape().updatePosition(this.getX(),this.getY());
        // Change animations here.
    }

    /* add more */

    public Facing getFacing() {
        return facing;
    }

    public void setFacing(Facing facing) {
        this.facing = facing;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public float getAccelerationSpeed() {
        return accelerationSpeed;
    }

    public void setAccelerationSpeed(float accelerationSpeed) {
        this.accelerationSpeed = accelerationSpeed;
    }

    public float getDecelerationSpeed() {
        return decelerationSpeed;
    }

    public void setDecelerationSpeed(float decelerationSpeed) {
        this.decelerationSpeed = decelerationSpeed;
    }

    public float getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(float maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }


}
