package entities;

import org.newdawn.slick.Graphics;
import physics.BoundingRectangle;
import physics.BoundingShape;
/**
 * Class that represents any non-tile entity in a level.
 * Contains the coordinates, width, height, a bounding shape to specify
 * the collision rectangle, current velocity in both axes, the maximumFallSpeed,
 * and booleans to know if this GameObject should ignore collisions and another
 * to know if the gameobject is currently on ground. These last attributes
 * are used in physics computation.
 * @author sergio
 */
abstract public class GameObject {
    private float x;
    private float y;
    
    private BoundingShape boundingShape;
    
    private int width;
    private int height;
    
    private float xVelocity = 0;
    private float yVelocity = 0;
    private float maximumFallSpeed = 0.75f;
    
    private boolean ignoresCollisions = false;
    private boolean onGround = true;
    private boolean ignoresPlatforms = false;
    private boolean ignoresGravity = false;
    
    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
        this.boundingShape = new BoundingRectangle(x,y,width,height);
    }
    
    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.boundingShape = new BoundingRectangle(x,y,width,height);
    }
    
    /**
     * Changes the vertical velocity due to the effect of gravity.
     * @param gravity 
     */
    public void applyGravity(float gravity){
        // If we aren't already moving at maximum speed, accelerate.
        if(yVelocity < maximumFallSpeed){
            yVelocity += gravity;
            if(yVelocity > maximumFallSpeed){  // If we exceed maximum speed,
                yVelocity = maximumFallSpeed;  // cap it to maximum.
            }
        }
    }
    
    /**
     * Moves the bounding shape to the object's coordinates.
     */
    public void updateBoundingShape(){
        boundingShape.updatePosition(x,y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        updateBoundingShape();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        updateBoundingShape();
    }

    public BoundingShape getBoundingShape() {
        return boundingShape;
    }

    public void setBoundingShape(BoundingShape boundingShape) {
        this.boundingShape = boundingShape;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getXVelocity() {
        return xVelocity;
    }

    public void setXVelocity(float xSpeed) {
        this.xVelocity = xSpeed;
    }

    public float getYVelocity() {
        return yVelocity;
    }

    public void setYVelocity(float ySpeed) {
        this.yVelocity = ySpeed;
    }

    public float getMaximumFallSpeed() {
        return maximumFallSpeed;
    }

    public void setMaximumFallSpeed(float maximumFallSpeed) {
        this.maximumFallSpeed = maximumFallSpeed;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean getIgnoresCollisions() {
        return ignoresCollisions;
    }

    public void setIgnoresCollisions(boolean ignoresCollisions) {
        this.ignoresCollisions = ignoresCollisions;
    }
    
    public boolean getIgnoresPlatforms() {
        return ignoresPlatforms;
    }
    
    public void setIgnoresPlatforms(boolean v) {
        this.ignoresPlatforms = v;
    }
    
    public boolean getIgnoresGravity() {
        return ignoresGravity;
    }

    public void setIgnoresGravity(boolean ignores) {
        this.ignoresGravity = ignores;
    }
    
    abstract public void render(Graphics g);

}
