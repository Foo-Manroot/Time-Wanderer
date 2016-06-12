package entities;

import animations.BatAnimator;
import utils.Transform;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

/**
 * This class represents an enemy that flies randomly within a patrolling area;
 * and, when the player is detected, flies towards it and attacks it.
 */
public class FlyingEnemy extends Enemy implements Runnable {

    /* ATTRIBUTES: */

    /**
     * Parameter that multiplies the base speed, so different enemies can have
     * different movement speeds.
     */
    private final float speed;

    /**
     * Unit vector that this enemy is currently following.
     */
    private final Vector2f path;


    /**
     * This will set the area on which the enemy will "patrol" (fly around
     * waiting for a player to come)
     */
    private Polygon patrolArea;

    /**
     * If the player hasn't been detected, this attribute should be <i>true</i>.
     * When the player steps into the detections area, this attribute will
     * change to <i>false</i>
     */
    private boolean patrolling;

    /**
     * After this enemy attacks the player, it will go back a little far so it
     * can avoid the player's attack.
     */
    private boolean retreat;
    
    /**
     * If this attribute is <i>true</i>, no action will be performed
     */
    private boolean stopped;
    
    /**
     * This attribute is <i>true</i> when the enemy is dead.
     */
    private boolean dead;
    
    /**
     * When this counter reaches 0, the enemy will be removed from the room.
     */
    private int deathCounter;
    
    /**
     * Number of frames with (life < 0) until the state is changed to "dead".
     */
    private final int deathCounterTime = (int) (20 * Math.random() + 30);
    
    /**
     * Variables for animation
     */
    /*Current animation of the bat*/
    private Animation currentAnimation;
    /*Animator Generator object. will provide us the different animations
    for the flying enemy.*/
    private final BatAnimator animGen;

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
     * @param speed
     *              Parameter that multiplies the base speed, so different
     *              enemies can have different movement speeds.
     */
    public FlyingEnemy (Vector2f coordinates, int width, int height,
                        float range, float detectDistance, float speed) {

        /* Calls Enemy's constructor */
        super(coordinates, width, height, range, detectDistance);

        /* Points for the patrol area. Initially it's a box with dimensions
        (width * 7) X (height * 3).
        Corners ('X' is the initial position of the enemy):
                    A -------- B
                    |     X    |
                    D -------- C
        */
        float points [] = {
            coordinates.x - (width * 7), coordinates.y + (width * 3), /* A */
            coordinates.x + (width * 7), coordinates.y + (width * 3), /* B */
            coordinates.x + (width * 7), coordinates.y - (width * 3), /* C */
            coordinates.x - (width * 7), coordinates.y - (width * 3)  /* D */
        }; /* End of points initialization */

        /* Initialization of the attributes */
        this.dead = false;
        this.speed = speed;
        this.path = new Vector2f(-1, 0); /* Initially going to the left */
        this.range = range;
        this.patrolArea = new Polygon(points);
        this.patrolling = true;
        this.deathCounter = deathCounterTime;

        this.setIgnoresGravity(true);
        this.setIgnoresPlatforms(false);
        this.setIgnoresCollisions(false);

        this.stopped = false;
        
        /*ANIMATION*/
        this.animGen = new BatAnimator();
        this.currentAnimation = new Animation();
        this.currentAnimation = animGen.getIdle1R();

        /* Starts the thread */
        new Thread(this).start();
    }

    @Override
    public void render(Graphics g) {
        
        int remainingLife = this.getStats().getLifePoints();
        
        /* Color for the text: slightly dark green if HP >= MaxHP / 2, 
                               slightly dark red otherwise */
        Color textColor = (remainingLife >= (getStats().getMaxLifePoints() / 2)) ? 
                                new Color (0, 160, 0) : /* green */
                                new Color(200, 0, 0); /* red */
        /* Sets the font to draw the text with */
//        Font font = new Font("Verdana", Font.BOLD, 12);
//        TrueTypeFont ttf = new TrueTypeFont(font, true);
        
        //if facing right
        if (this.getFacing().equals(this.getFacing().RIGHT)) {
            if(!this.currentAnimation.equals(animGen.getIdle1R())){
                this.currentAnimation=animGen.getIdle1R();
            }    
        }
        else{//facing left
            if(!this.currentAnimation.equals(animGen.getIdle1L())){
                this.currentAnimation=animGen.getIdle1L();
            } 
        }
        
        /*Calculations for properly placing
         the animations over the BoundingShape of the player*/
        int animWidth = this.currentAnimation.getWidth();
        int animHeight = this.currentAnimation.getHeight();

        int enemyWidth = this.getWidth();
        int enemyHeight = this.getHeight();
        /*Draw currrent animation.*/
        g.drawAnimation(currentAnimation,
                this.getX() + enemyWidth / 2 - animWidth / 2,
                this.getY() + enemyHeight - animHeight);
        
        /* Changes the color */
        g.setColor(textColor);
//        g.setFont(new UnicodeFont(font));
        
        /* Draws the remaining life right above its head */
        g.drawString ("HP: " + remainingLife,
                      this.getX(),
                      this.getY() - this.getHeight());
    }


    /**
     * This method will control the attack of this enemy.
     * Attack and retreat behavoiur: the enemy advances at twice the normal
     * speed towards the player and then goes back to the initial position.
     *
     * @param target The player that will be the main target of this enemy.
     * @param delta Milliseconds that took the computer to render the image.
     */
    @Override
    public void attack (Player target, int delta) {

        /* Position of the player */
        Vector2f player = new Vector2f(target.getX(), target.getY()),
                 aux = new Vector2f(),
                 thisEnemy = new Vector2f(this.getX(), this.getY());
        float mod;
        int factor = (retreat)? 1: 2; /* Slows down on the retreat */

        /* If no action should be performed, returns */
        if (stopped || dead) {
            return;
        }

        /* First of all, detects if the player can be attacked with this enemy's
        weapon */
        if (player.distance(thisEnemy) <= range) {

            /* Only attacks the player if this enemy wasn't retreating */
            if (!retreat) {
                
                /* Damages the player */
                target.getHit(this.getStats().getAttackDamage());
                retreat = true;        /* Starts the retreating action */
            }
        }
        
        /* Calculates the vector that leads to the player */
        aux.x = player.getX() - this.getX();
        aux.y = player.getY() - this.getY();

        /* Converts the vector to a unit vector */
        mod = (float) Math.sqrt(Math.pow(aux.x, 2) + Math.pow(aux.y, 2));

        path.x = aux.x / mod;
        path.y = aux.y / mod;

        /* If it's retreating, goes at the opposite position of the target.
        If not, advances towards the target at twice the normal speed */
        if (retreat) {

            /* Changes the unit vector to the (almost) opposite one */
            aux = Transform.rotate(path, 165, null);
            path.x = aux.x;
            path.y = aux.y;
        }

        /* Multiplies the unit vector "speed" times and adds it to the current
        path vector */
        this.setX((float) ((this.getX() + (path.x * factor * speed * delta * 0.1) )));
        this.setY((float) ((this.getY() + (path.y * factor * speed * delta * 0.1) )));
    }


    /**
     * Returns <i>true</i> if the enemy is still into the patrol area.
     */
    private boolean inPatrolArea () {

        /* Compares the limits of the patrol area with the limits of the
        enemy's body. The four corners that will be compared are:
                        A -- B
                        |    |
                        C -- D
        */
        return (patrolArea.contains(this.getX(), this.getY()) /* A */
                &&
                patrolArea.contains(this.getX() + this.getWidth(), /* B */
                        this.getY())
                &&
                patrolArea.contains(this.getX(),        /* C */
                        this.getY() + this.getHeight())
                &&
                patrolArea.contains(this.getX() + this.getWidth(),  /* D */
                        this.getY() + this.getHeight()) );
    }

    
    /**
     * This method will control the path that this enemy will follow.
     * On flying enemies, the behavior is the following:
     *      ·The enemy will be flying around with a random path (changed when
     *          the thread wakes up), always into the patrol area.
     *      ·If any object is in the middle of the path, a new path will be
     *          generated.
     *      ·If the player is in the detectDistance, the path will be changed
     *          so this enemy can attack the player.
     *
     * @param target The player that this enemy will try to hit.
     * @param delta Milliseconds that took the computer to render the image.
     */
    @Override
    public void move(Player target, int delta) {

        Vector2f aux = new Vector2f(),
                 player = new Vector2f (target.getX(), target.getY()),
                 thisEnemy = new Vector2f(this.getX(), this.getY());
        float mod;

        /* If no action should be performed, returns */
        if (stopped) {

            return;
        }
        
        if (deathCounter < deathCounterTime) {
            
            die();
        }
        
        /* If the player is within the detection range, the followed path will
        be the one that leads to the player. */
        if (thisEnemy.distance(player) <= detectDistance) {

            patrolling = false;

            /* If the player is also within the attack range, activates the
            attack behaviour (attack_range = range * 2) */
            if (thisEnemy.distance(player) <= range * 2) {

                attack(target, delta);
                return;
            } else {

                retreat = false;
            }

            /* Generates a new vector leading to the target */
            aux.x = target.getX() - this.getX();
            aux.y = target.getY() - this.getY();

            /* Converts the vector to a unit vector */
            mod = (float) Math.sqrt(Math.pow(aux.x, 2) + Math.pow(aux.y, 2));

            path.x = aux.x / mod;
            path.y = aux.y / mod;
        } else {

            patrolling = true;

            /* If it's still into the patrolling area, the enemy will follow the
            current path normally. If not, generates a vector that will get it
            into the area again */
            if (!inPatrolArea()) {

                /* Goes back to the patrol area */
                aux.x = patrolArea.getCenterX() - this.getX();
                aux.y = patrolArea.getCenterY() - this.getY();

                /* Converts the vector to a unit vector */
                mod = (float) Math.sqrt(Math.pow(aux.x, 2) + Math.pow(aux.y, 2));

                path.x = aux.x / mod;
                path.y = aux.y / mod;
            }
        }

        /* Sets the correct facing, according to the generated vector */
        if (path.x > 0) {
            
            this.setFacing(Facing.RIGHT);
        } else {
            
            this.setFacing(Facing.LEFT);
        }
            
        
        /* Multiplies the unit vector "speed" times and adds it to the current
        path vector */
        this.setX((float) ((this.getX() + (path.x * speed * delta * 0.1) )));
        this.setY((float) ((this.getY() + (path.y * speed * delta * 0.1) )));
    }


    @Override
    public void die() {

        /* Falls to the floor and dissapears when reaches the bottom */
        this.setIgnoresGravity(false);
        this.setIgnoresPlatforms(false);
        
        /* Stays here until the counter is 0 */
        if (deathCounter > 0) {
            
            deathCounter--;
        } else {
        
            dead = true;
        }
    }


    /**
     * Generates the vector that corresponds to the next movement, following
     * the path.
     */
    private void generateVector () {

        Vector2f aux;
        float alpha;

        /* Changes the vector, so the new one will form an angle with "alpha"
        degrees, being "alpha" a random number between ±45 */
        alpha = (float) (90 * Math.random() - 45);

        /* Now, changes the unit vector with the new angle */
        aux = Transform.rotate(path, alpha, null);

        path.x = aux.x;
        path.y = aux.y;
    }


    /**
     * This method will sleep a random time between 0 and 0.5 seconds and will
     * change the unit vector that leads the path.
     */
    @Override
    public void run() {

        /* Infinite loop */
        while (true) {

            try {
                /* Waits a random time between 0 and 0.5 seconds. */
                Thread.sleep((long) (500 * Math.random()));
            } catch (InterruptedException ex) {
                Logger.getLogger(FlyingEnemy.class.getName()).log(Level.SEVERE, null, ex);
            }

            checkStopped();
            /* Changes the path (only if the player hasn't been detected) */
            if (patrolling)
                generateVector();
        }
    }

/* ------------------------------------- */
/* ---- THREAD STATE CONTROL METHODS --- */
/* ------------------------------------- */

    /**
     * Checks if the attribute "stopped" is true and, therefore, this thread
     * must wait to be notified.
     */
    private synchronized void checkStopped () {

        while (stopped) {

            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(FlyingEnemy.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }

    /**
     * Stops the action of this enemy.
     */
    @Override
    public synchronized void stop () {

        stopped = true;
    }

    /**
     * Restarts the action of this thread.
     */
    @Override
    public synchronized void restart () {

        stopped = false;
        notifyAll();
    }

/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */

    /**
     * Returns <i>true</i> if the actions of this enemy are stopped.
     *
     * @return
     *          The value of the attribute <b>stopped</b>.
     */
    @Override
    public boolean isStopped () {

        return stopped;
    }
    
    /**
     * Returns <i>true</i> if the enemy has died.
     *
     * @return
     *          The value of the attribute <b>dead</b>.
     */
    @Override
    public boolean isDead () {

        return dead;
    }

    /**
     * Returns the patrol area of this enemy.
     *
     * @return A polygon that surrounds the patrolling area.
     */
    public Polygon getPatrolArea() {

        return patrolArea;
    }

    /**
     * Sets the patrol area of this enemy.
     *
     * @param patrolArea A polygon that surrounds the patrolling area.
     */
    public void setPatrolArea(Polygon patrolArea) {

        this.patrolArea = patrolArea;
    }

    /**
     * Returns the range area of this enemy.
     *
     * @return The attack range of this enemy.
     */
    public float getRange() {

        return range;
    }

    /**
     * Sets the range area of this enemy.
     *
     * @param range The new attack range of this enemy.
     */
    public void setRange(float range) {

        this.range = range;
    }
}
