package items;

import entities.Player;
import misc.AttacksObserver;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import physics.BoundingShape;
import main.MainClass;
import org.newdawn.slick.Image;

/**
 *
 * @author Miguel García Martín
 */
public class Projectile {

    /**
     * This is the attack of this enemy.
     */
    private final int attack;
    
    /**
     * Observer that is going to control the state of this projectile.
     */
    private AttacksObserver observer;
    
    /**
     * Collider of the projectile.
     */
    private Polygon collider;
    
    /**
     * Vector that will store the x and y speed
     */
    private final Vector2f speed;
    /**
     * Image that will be used to represent this proyectile.
     */
    private Image image;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */
    
    /**
     * Constructor
     * 
     * @param player
     *          Player that fires this projectiles.
     * @param width 
     *          Width of this projectile's collider.
     * @param height
     *          Height of this projectile's collider.
     * @param speed
     *          Vector with the initial speed (vector.x -> horizontal speed and 
     *      vector.y -> vertical speed).
     * @param attack 
     *          Damage that the enemy will recieve when it gets hit.
     */
    public Projectile(Player player, int width, int height, Vector2f speed, 
                      int attack, Image image) {
        
        Vector2f playerCenter = new Vector2f (
                                player.getX() + (player.getHeight() / 2),
                                player.getY() + (player.getHeight() / 2));
        
        /* Initial points for the collider. It's a box with the following
        position being 'X' the center of the player:
                    A --------- D
                    |     X     |
                    B --------- C
        */
        float points [] = {
            playerCenter.x - (width/2), playerCenter.y + (height/2), /* A */
            playerCenter.x - (width/2), playerCenter.y - (height/2), /* B */
            playerCenter.x + (width/2), playerCenter.y - (height/2), /* C */
            playerCenter.x + (width/2), playerCenter.y + (height/2)  /* D */
        }; /* End of points initialization */
        
        this.collider = new Polygon(points);
        
        this.attack = attack;
        
        this.observer = null;
        this.speed = speed;
        this.image = image;
    }
    
    
    /**
     * Generates the points that are needed to draw the collider.
     * 
     * @param delta 
     *          Milliseconds that took the computer to update and render the 
     *      last frame.
     * 
     * @return
     *          An array with the 4 pair of coordinates needed (8 float elements).
     */
    private float[] calculatePoints (int delta) {
        
        float [] points = new float [8];
        float mod;
        Vector2f unitVector = new Vector2f();
        
        /* Converts the vector to a unit vector */
        mod = (float) Math.sqrt(Math.pow(speed.x, 2) + Math.pow(speed.y, 2));

        unitVector.x = speed.x / mod;
        unitVector.y = speed.y / mod;
        
         /* Calculates the new points, being aware of the current position and 
        the vector "speed". 
        The collider is a box with the following position with respective to
        "unitVector", paralell to the vector "speed":
                       (unitVector)
                        \/ 
                    A -->------ D
                    |           |
                    B --------- C
            The previous position of A (the reference here) is updated with the 
        speed set by the vector "speed".
        */
        points[0] = collider.getX() + (speed.x * delta); /* A.x */
        points[1] = collider.getY() + (speed.y * delta); /* A.y */
        
        points[2] = points[0]; /* B.x */
        points[3] = points[1] - collider.getHeight(); /* B.x */
        
        points[4] = points[0] + collider.getWidth(); /* C.x */
        points[5] = points[1] - collider.getHeight(); /* C.y */
        
        points[6] = points[0] + collider.getWidth(); /* D.x */
        points[7] = points[1]; /* D.y */
        
        return points;
    }


    /**
     * Renders the image on the game.
     * 
     * @param g 
     *          Graphics on which the image will be drawn.
     */
    public void render(Graphics g) {
        g.drawImage(image,collider.getX(), collider.getY());
    }
    
    
    /**
     * Checks if this projectile's body is colliding with the given rectangle.
     * 
     * @param rectangle
     *          BoundingRectangle to compare.
     * 
     * @return 
     *          <i>true</i> if the two objects collide, <i>false</i> otherwise.
     */
    public boolean checkCollision (BoundingShape rectangle) {
        
        Rectangle aux = new Rectangle(rectangle.getX(),
                                      rectangle.getY(), 
                                      rectangle.getWidth(),
                                      rectangle.getHeight());
        
        return (collider.intersects(aux));
    }
    
    /**
     * Updates the state of this object.
     * 
     * @param delta
     *          Milliseconds that took the computer to render and update the 
     *      last frame.
     */
    public void update (int delta) {
        
        Rectangle playZone = new Rectangle(0, 0, 
                                MainClass.WINDOW_WIDTH, 
                                MainClass.WINDOW_HEIGHT);
        
        /* Simulates gravity (for some reason, the Y axis is upside down...) */
        speed.y += (0.002 * delta);
        
        /* Updates the position */
        collider = new Polygon(calculatePoints(delta));
                
        
        /* If this object is not within the game's window, it's destroyed */
        if (!playZone.contains(collider.getX(), collider.getY())) {

            notifyObserver();
        }
    }
    
    
    /**
     * Tells the observer that this projectile already finished its life, so it 
     * should be removed from the observer's list.
     */
    private void notifyObserver () {
        
        observer.removeProjectile(this);
    }


/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */
    
    /**
     * Returns the damage that this enemy will make to the player.
     * 
     * @return 
     *          The value of <i>attack</i>.
     */
    public int getAttack () {
        
        return attack;
    }
    
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
