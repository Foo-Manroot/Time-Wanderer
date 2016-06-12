package entities;

import animations.BossAnimator;
import gamestates.LevelState;
import static java.lang.Thread.sleep;
import java.util.Random;
import levels.Room;
import misc.RandomObjBoss;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

/**
 * This class represents a ground enemy that will be patrolling until the enemy
 * is within the detection range. Then, follows the player and tries to attack
 * him.
 */
public class BossEnemy extends Enemy implements Runnable {

    /* ATTRIBUTES: */

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
     * This attribute will be <i>true</i> if this enemy is moving to the left.
     */
    private boolean movingLeft;

    /**
     * This is the attack of this enemy.
     */
    private final int attack;

    /**
     * This variable stores the previous position, so it can be detected that
     * this enemy is stuck and can't move.
     */
    private Vector2f previousPos;

    /**
     * Minimum distance that the object must move between two different updates.
     */
    private final float stuckDistance;

    /**
     * If this attribute is <i>true</i>, no action will be performed
     */
    private boolean stopped;
    
    /**
     * Projectiles that come out of this boss randomly, damaging the player when
     * it hits him.
     */
    private final RandomObjBoss projectiles;
    
    /**
     * This attribute is <i>true</i> when the enemy is dead.
     */
    private boolean dead;
    
    /**
     * This forces the enemy to wait between attacks
     */
    private int refreshAttack;

    /**
     * Number of updates between two different attacks.
     */
    private final int refreshAttackTime = (int) (20 * Math.random() + 30);
    
    /**
     * When this counter reaches 0, the enemy will be removed from the room.
     */
    private int deathCounter;
    
    /**
     * Number of frames with (life < 0) until the state is changed to "dead".
     */
    private final int deathCounterTime = (int) (10 * Math.random() + 20);
    
    /**
     * Animation variables
     */
    /*Current animation of the Boss*/
    private Animation currentAnimation;
    /*Animator Generator object. will provide us the different animations
    for the Boss.*/
    private BossAnimator animGen;
    /** Room where the boss is currently at. */
    private Room currentRoom;
    /**
     * Indicates whether the boss is attacking or not.
     * Used for rendering animations.
     */
    private boolean isAttacking = false;

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
     * @param attack
     *              Damage that this enemy will make to the player.
     *              -1 to set the default value.
     * @param room
     *              Room where the boss is currently at, used to unlock the
     *              next level when dying.
     */
    public BossEnemy (Vector2f coordinates, int width, int height,
                        float range, float detectDistance, float speed,
                        int attack, Room room) {

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
        this.stuckDistance = speed / 2;
        this.setMaximumSpeed((float) (speed * 0.1));
        this.range = range;
        this.patrolArea = new Polygon(points);
        this.patrolling = true;
        this.attack = (attack < 0)? 10 : attack; /* Default: 10 */
        this.movingLeft = true; /* Starts moving to the left */

        this.previousPos = new Vector2f(coordinates.x - stuckDistance,
                                        coordinates.y - stuckDistance);

        this.stopped = false;
        
        this.projectiles = new RandomObjBoss(1, 1000, this);
        
        this.deathCounter = deathCounterTime;
        this.refreshAttack = 0;
        
        /*ANIMATION*/
        this.animGen = new BossAnimator();
        this.currentAnimation = new Animation();
        this.currentAnimation = animGen.getIdle1R();
        
        this.currentRoom = room;
        
        /* Starts the thread */
        new Thread(this).start();
    }
    
    @Override
    public void die() {

        /* Stays here until the counter is 0 */
        if (deathCounter > 0) {
            
            deathCounter--;
        } else {
        
            dead = true;
            /* Unblocks the pass to the next level */
            LevelState.enableChangeOfMap();
        }
        
        
    
    }
    
    /**
     * Updates the position of this character and its projectiles.
     * 
     * @param target 
     *          The player that this enemy will try to hit.
     * @param delta 
     *          Milliseconds that took the computer to render the image.
     */
    @Override
    public void update (Player target, int delta) {
        
        move(target, delta);
        this.projectiles.update(delta, target);
        
        this.updateBoundingShape();
    }

    /**
     * Draws the boss' body and all the objects that came out of it.
     */
    @Override
    public void render(Graphics g) {
        
        //g.setColor(Color.blue);

        this.projectiles.render(g);
        //super.render(g);

        //g.setColor(Color.green);
        //ANIMATION
        //WALK
            if (this.isMoving()) {
                //If the animation is not already the walk animation
                if (this.currentAnimation != animGen.getWalk1R()
                        || this.currentAnimation != animGen.getWalk1L()) {
                    if (this.getFacing().equals(this.getFacing().RIGHT)) {
                        this.currentAnimation = animGen.getWalk1R();
                    } else {
                        this.currentAnimation = animGen.getWalk1L();
                    }
                }
            } //IDLE
            else {
                //If the animation is not already the walk animation
                if (this.currentAnimation != animGen.getIdle1R()
                        && this.currentAnimation != animGen.getIdle1L()) {
                    if (this.getFacing().equals(this.getFacing().RIGHT)) {
                        this.currentAnimation = animGen.getIdle1R();
                    } else {
                        this.currentAnimation = animGen.getIdle1L();
                    }
                }

            }
            
            //ATTACK
        if (this.isAttacking) {
            if (this.currentAnimation != animGen.getAttack1R()
                    || this.currentAnimation != animGen.getAttack1L()) {
                if (this.getFacing().equals(this.getFacing().RIGHT)) {
                    this.currentAnimation = animGen.getAttack1R();
                } else {
                    this.currentAnimation = animGen.getAttack1L();
                }
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
        //ANIMATION-END

    }


    /**
     * Simulates the attack of this enemy to its target (the player).
     */
    @Override
    public void attack(Player target, int delta) {

        Vector2f currentPos = new Vector2f(this.getX(), this.getY());
        Vector2f player = new Vector2f(target.getX(), target.getY());
        Vector2f playerMaxX = new Vector2f(target.getX() + target.getWidth(),
                                           target.getY());

        /* If no action should be performed, returns */
        if (stopped || dead) {
            return;
        }
        
        /* If the refresh counter hasn't been resetted yet, drops it
        by one unit */
        if (refreshAttack > 0) {

            refreshAttack--;
            return;
        }

        /* If the player still within range, decreases its life */
        if ( (currentPos.distance(player) <= range) ||
             (currentPos.distance(playerMaxX) <= range) ) {
            
            //ANIMATION
            //set attacking to true to render attack animation.
            this.isAttacking=true;
            /*This method will wait for the animation to finish and then set
            isAttacking to false;*/
            resetAttackState();
            //END-ANIMATION
            
            /* Stops and "hits" the player */
            this.setXVelocity(0);
            target.getHit(attack);
            refreshAttack = refreshAttackTime;
        }
    }
    
    /**Used for rendering purposes. Waits for the attack animation 
     * to finish and then sets the isAttacking variable to false, so that
     the rest of the animations can be played.*/
    public synchronized void resetAttackState() {

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {

                //calculate the duration of the attack animation.
                int attackDuration = 0;
                for (int duration : animGen.getAttack1R().getDurations()) {
                    attackDuration += duration;
                }
                /*make player wait duration of the attack animation
                 plus extra time before being able to attack again.*/
                try {
                    sleep(attackDuration + 0);
                } catch (InterruptedException ex) {
                    System.err.println(ex);
                }
                //reset attack availability.
                isAttacking = false;
            }
        });

        t1.start();
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
//        return (patrolArea.contains(this.getX(), this.getY()) /* A */
//                &&
//                patrolArea.contains(this.getX() + this.getWidth(), /* B */
//                        this.getY())
//                &&
//                patrolArea.contains(this.getX(),        /* C */
//                        this.getY() + this.getHeight())
//                &&
//                patrolArea.contains(this.getX() + this.getWidth(),  /* D */
//                        this.getY() + this.getHeight()) );

        /* Only compares the X axis, so this enemy can fall from a platform
        without getting stuck or anything similar */
        return (this.getX() > patrolArea.getX()
                && this.getX() < patrolArea.getMaxX());
    }

    /**
     * Returns <i>true</i> if the patrol area is on the left of this enemy (and,
     * therefore, it will have to go to the left to go back into it), and
     * returns <i>false</i> otherwise.
     */
    private boolean patrolAreaOnLeft () {

        /* When the X coordinate of the enemy is greater than the X coordinate
        of the right limit (maxX) of the patrol area, that means that the enemy
        is on the right of the area. */
        return (this.getX() > patrolArea.getMaxX());
    }

    /**
     * Returns <i>true</i> if the player is on the left of this enemy (and,
     * therefore, it will have to go to the left to attack it), and
     * returns <i>false</i> otherwise.
     *
     * @param player
     *              The player which position has to be compared.
     */
    private boolean playerOnLeft (Player player) {

        /* When the X coordinate of the enemy is greater than the X coordinate
        of the right limit (X + Width) of the patrol area, that means that
        the enemy is on the right of the player. */
        return (this.getX() > (player.getX() + player.getWidth()));
    }

    /**
     * If the last recorded position is the same as the current one and the
     * enemy is not attacking the player, this method returns <i>true</i>. That
     * means that this enemy is possibly stucked in front of a wall or something
     * similar.
     */
    private boolean stuck () {

        Vector2f currentPos = new Vector2f(this.getX(), this.getY());
        boolean retVal = false;

        if ((currentPos.distance(previousPos) < stuckDistance) &&
            patrolling) {

            retVal = true;
        }

        /* Updates the value of "previousPos" */
        previousPos = currentPos;

        return retVal;
    }

    /**
     * This method controlls the movement of this enemy.
     * If it's not patrolling, will go towards the player.
     *
     * @param target The player that this enemy will try to hit.
     * @param delta Milliseconds that took the computer to render the image.
     */
    @Override
    public void move(Player target, int delta) {

        Vector2f currentPos = new Vector2f(this.getX(), this.getY());
        Vector2f player = new Vector2f(target.getX(), target.getY());
        Vector2f playerMaxX = new Vector2f(target.getX() + target.getWidth(),
                                           target.getY());

        /* If no action should be performed, returns */
        if (stopped) {
            return;
        }

        /* If it's stuck, tries to avoid the obstacle, either jumping or 
        changing the movement direction */
        if (stuck()) {

            if (new Random().nextBoolean()) {
                
                this.jump();
            } else {
                
                if (movingLeft) {
                    
                    this.moveRight(delta);
                    movingLeft = false;
                } else {
                    
                    this.moveLeft(delta);
                    movingLeft = true;
                }
            }
        }
        
        if (deathCounter < deathCounterTime) {
            
            die();
        }

        /* First, sees if the player is within the detection range */
        if ( (currentPos.distance(player) <= detectDistance) ||
             (currentPos.distance(playerMaxX) <= detectDistance) ) {
            
            /* Stops patrolling */
            patrolling = false;

            /* Checks if the player is within the attack range */
            if ( (currentPos.distance(player) <= range) ||
                 (currentPos.distance(playerMaxX) <= range) ) {
                
                attack(target, delta);

            } else {

                /* If the player isn't on the attack range, moves and chase it */
                if (playerOnLeft(target)) {

                    this.moveLeft(delta);
                    movingLeft = true;
                } else {

                    this.moveRight(delta);
                    movingLeft = false;
                }
            }
        } else {

            patrolling = true;
            /* If the player hasn't been detected, keeps patrolling */

            /* First, checks if it's still in the patrol area. If not, goes
            back into it */
            if (inPatrolArea()) {

                /* If it's moving left, keeps doing it until the left part of
                the patrol area is reached */
                if (movingLeft) {

                    this.moveLeft(delta);
                } else {

                    this.moveRight(delta);
                }

            } else {

                /* Tries to go back to the patrol area */
                if (patrolAreaOnLeft()) {

                    moveLeft(delta);
                    movingLeft = true;
                } else {

                    moveRight(delta);
                    movingLeft = false;
                }
            }
        }
    }

    


    /**
     * Changes the movement direction (left or right) from time to time.
     */
    @Override
    public void run() {

        /* Infinite loop */
        while (true) {

            try {
                /* Waits a random time between 3 and 5 seconds. */
                Thread.sleep((long) (2000 * Math.random() + 3000));
            } catch (InterruptedException ex) {
                System.out.println("Exception at BossEnemy.run(): "
                                    + ex.getMessage());
            }

            checkStopped();
            /* Changes the direction */
            if (patrolling)
                movingLeft = !movingLeft;
        }
    }


/* ------------------------------------- */
/* ---- THREAD STATE CONTROL METHODS --- */
/* ------------------------------------- */

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
     * Checks if the attribute "stopped" is true and, therefore, this thread
     * must wait to be notified.
     */
    private synchronized void checkStopped () {

        while (stopped) {

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("Exception at GroundEnemy.checkStopped(): "
                                    + ex.getMessage());
            }
        }
    }

    /**
     * Stops the action of this enemy.
     */
    @Override
    public synchronized void stop () {

        stopped = true;
        this.setXVelocity(0);
        
        projectiles.stopAction();
    }

    /**
     * Restarts the action of this thread.
     */
    @Override
    public synchronized void restart () {

        /* Changes the previous position so it doesn't think it got stucked and
        jumps trying to avoid the obstacle */
        this.previousPos = new Vector2f(previousPos.x - stuckDistance,
                                        previousPos.y - stuckDistance);
        stopped = false;
        notifyAll();
        
        projectiles.restart();
    }

/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */

    /**
     * Returns the patrol area of this enemy.
     *
     * @return 
     *          A polygon that surrounds the patrolling area.
     */
    public Polygon getPatrolArea() {

        return patrolArea;
    }

    /**
     * Sets the patrol area of this enemy.
     *
     * @param patrolArea
     *              A polygon that surrounds the patrolling area.
     */
    public void setPatrolArea(Polygon patrolArea) {

        this.patrolArea = patrolArea;
    }

    /**
     * Returns the range area of this enemy.
     *
     * @return 
     *          The attack range of this enemy.
     */
    public float getRange() {

        return range;
    }

    /**
     * Sets the range area of this enemy.
     *
     * @param range 
     *              The new attack range of this enemy.
     */
    public void setRange(float range) {

        this.range = range;
    }
    
    /**
     * Returns the damage that this enemy will make to the player.
     * 
     * @return 
     *          The value of <i>attack</i>.
     */
    public int getAttack () {
        
        return attack;
    }
}
