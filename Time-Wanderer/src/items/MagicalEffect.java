package items;

import entities.GameCharacter;
import misc.AttacksObserver;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import utils.Transform;

/**
 * Effect for the magical weapon. Is a little polygon that expands and hits
 * all the enemies within its range.
 */
public class MagicalEffect {

    /**
     * Collision box for the effect.
     */
    private Polygon body;
    
    /**
     * Unit vector that will lead the movement of the body
     */
    private Vector2f unitVector;
    
    /**
     * Damage that will recieve every enemy affected by this object.
     */
    private final int attack;
    
    /**
     * Current length of the side.
     */
    private float side;
    
    /**
     * Length at which the effect will disappear.
     */
    private final float finalSide;
    
    /**
     * Center of the body's rotation.
     */
    private final Vector2f center;
    
    /**
     * This forces the effect to "wait" between hits, so it can only hit one
     * enemy in the given amount of time.
     */
    private int refreshAttack;

    /**
     * Number of updates between two different hits.
     */
    private final int refreshAttackTime = (int) (20 * Math.random() + 30);
    
    /**
     * Observer that is going to control the state of this effct.
     */
    private AttacksObserver observer;
    /**
     * Image that represents the magical effect.
     */
    private Image image;
    /**
     * Rotation of the effect in degrees.
     */
    private float rotation;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */
    
    /**
     * Constructor.
     * 
     * @param coordinates 
     *              Coordinates where the effect will be created.
     * @param side 
     *              Initial side of the collision box.
     * @param attack 
     *              Damage that will recieve every enemy affected by
     *          this object.
     * @param image
     *              Image that will be used to represent the current effect.
     */
    public MagicalEffect (Vector2f coordinates, float side, int attack, Image image) {
        
        /* Initializations: */
        this.refreshAttack = 0;
        /* Unit vector pointing to the left */
        this.unitVector = new Vector2f (-1, 0);
        this.center = coordinates;
        
        /* Points for the collision body.
        Corners ('X' are the given coordinates):
                    A -------- B
                    |     X    |
                    D -------- C
        */
        float points [] = {
            coordinates.x - (side/2), coordinates.y + (side/2), /* A */
            coordinates.x + (side/2), coordinates.y + (side/2), /* B */
            coordinates.x + (side/2), coordinates.y - (side/2), /* C */
            coordinates.x - (side/2), coordinates.y - (side/2)  /* D */
        }; /* End of points initialization */
        
        this.body = new Polygon (points);
        this.attack = attack;
        this.side = side;
        this.image = image;
        /* The final side length is a random number between 
        "side * 2" and "side * 3" */
        this.finalSide = (float) (side * 2 * Math.random() + side * 4);
    }
    
    /**
     * Checks if this object collides with any enemy, but only if the counter
     * {@code refreshAttack} reached 0. If any action can be performed, hits
     * the gameCharacter and restarts the counter.
     * 
     * @param character 
     *              GameCharacter which position is going to be compared.
     * 
     * @return 
     *              <i>true</i> if an atta
     */
    public boolean checkAndHit (GameCharacter character) {
        
        /* Points for the collision body of the character.
        Corners:
                    A -------- B
                    |          |
                    D -------- C
        */
        float [] characterPoints = {
                        character.getX(), /* A */
                        character.getY(),
                        character.getX() +  character.getWidth(), /* B */
                        character.getY(),
                        character.getX() +  character.getWidth(), /* C */
                        character.getY() -  character.getHeight(),
                        character.getX(), /* D */
                        character.getY() -  character.getHeight()
        };
        
        Polygon aux;
        
        /* If the attack can't be performed, returns false */
        if (refreshAttack > 0) {
            
            refreshAttack--;
            return false;
        }
        
        aux = new Polygon(characterPoints);
        
        /* If the GameCharacter doesn't collide with this effect, returns 
        false */
        if (!body.intersects(aux)) {
            
            return false;
        }
        
        /* If the GameCharacter collides, hits it and returns true */
        hitCharacter(character);
        
        return true;
    }
    
    /**
     * Performs the hit action on the given GameCharacter.
     * 
     * @param character
     *                  The GameCharacter that will be affected.
     */
    public void hitCharacter (GameCharacter character) {
        
        character.getHit(attack);
        
        refreshAttack = refreshAttackTime;
    }
    
    /**
     * Generates the points for the body. This method will calculate and update 
     * the rotation of the unitVector, returning the rotated points.
     * 
     * @param delta
     *              Milliseconds that took the computer to update and render the
     *          previous frame.
     */
    private float [] generatePoints (int delta) {
        
        float [] points = new float [8];
        
        /* Rotates slightly the square */
        rotation += (float) (delta * 0.6);
        unitVector = Transform.rotate(unitVector, (float) (delta * 0.6), null);
        
        /* Generates the new points with the rotated unitVector.
        Corners ('X' are the center's coordinates, and '<-' the unitVector):
                    A ------ B
                    |  <--X  |
                    D ------ C
        */
        /* A = Center + (unitVector + unitVector.90º) * (side / 2) */
        points [0] = center.x + (unitVector.x * (side / 2)); /* A.x */
        points [1] = center.y + (
                     unitVector.getPerpendicular().y * (side / 2)
                    ); /* A.y */
        
        /* B = A + (side * -unitVector) */
        points [2] = points [0] + (side * -unitVector.x); /* B.x */
        points [3] = points [1] + (side * -unitVector.y); /* B.y */
        
        /* C = B + (side * unitVector.-90º) */
        points [4] = points [2] + (side * -unitVector.getPerpendicular().x); /* C.x */
        points [5] = points [3] + (side * -unitVector.getPerpendicular().y); /* C.y */
        
        /* D = C + (side * unitVector) */
        points [6] = points [4] + (side * unitVector.x); /* D.x */
        points [7] = points [5] + (side * unitVector.y); /* D.y */        
        
        return points;
    }
    
    /**
     * Updates the state of this object.
     * 
     * @param delta
     *              Milliseconds that took the computer to update and render the
     *          previous frame.
     */
    public void update (int delta) {
        
        
        /* If the current side length reached the final length, notifies the
        observer so this effect can be removed */
        if (side >= finalSide) {
            
            observer.removeEffect(this);
            return;
        }
        
        side += delta * 0.1;        
        body = new Polygon (generatePoints(delta));
    }
    
    /**
     * Draws the effect on the screen.
     * 
     * @param g 
     *              Frame on which the effect will be drawn.
     */
    public void render (Graphics g) {
        
        image.setCenterOfRotation(image.getWidth() / 2, image.getHeight() / 2);
        image.setRotation(rotation);
        image.draw(center.x, center.y, side, side);
        //g.draw (body);
    }
    
    
/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */
    
    /**
     * Changes the observer that's going to control the state of this projectile.
     * 
     * @param observer
     *          The new observer that's going to substitute the previous one.
     */
    public void setObserver (AttacksObserver observer) {
        
        this.observer = observer;
    }
}
