package entities;

import animations.PastPlayerAnimator;
import animations.PlayerAnimator;
import animations.PlayerAnimatorInterface;
import items.Inventory;
import creators.ItemCreator;
import gamestates.LevelState;
import items.MagicalEffect;
import items.MeleeWeapon;
import items.Projectile;
import items.RangedWeapon;
import levels.Room;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.MainClass;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.geom.Vector2f;
import sound.Playlist;
import utils.NumberUtils;

/**
 * Class that represents the player. Inherits from GameCharacter since it is a
 * character contained by a level.
 *
 * @author sergio
 */
public class Player extends GameCharacter {
    /**Tells if the player is able to jump or not.*/
    private boolean canJump;
    /**Tells if the player is able to climb or not.*/
    private boolean canClimb;
    /**Tells if the player is climbing or not.*/
    private boolean isClimbing = false;
    /**Tells if the player is attacking or not.*/
    private boolean isAttacking = false;
    /**Room where the player is currently at.*/
    private Room currentRoom;
    /**Current animation of the player.*/
    private Animation currentAnim;
    /**Animator that provides the animation for the player.*/
    private PlayerAnimatorInterface animGen;
    /**We will create two animator generators, one for the
     past and another one for the present. In the update method we 
     will check in which time line we are:
     If we are in the present -> animGen = present time line animGen.
     If we are in the past -> animGen = past time line animGen.
     In this way we consume more memory, but we avoid to create then entire 
     class of the PlayerAnimator when we change time lines.*/
    //Provides animations for the present time line.
    private PlayerAnimator presentAnimGen;
    //Provides animations for the past time line.
    private PastPlayerAnimator pastAnimGen;
    
    public enum Timeline {PAST,PRESENT};

    /**
     * When the player dies, this  attribute will be changed to <i>true</i>.
     */
    private boolean dead = false;
      
    /**
     * Inventory containing all the items of the player (weapons,
     * consumables...)
     */
    Inventory inventory;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */

//    public Player(float x, float y, int width, int height, Room room) {
//        super(x, y, width, height);
//
//        setAccelerationSpeed(0.002f);
//        setMaximumSpeed(0.30f);
//        setMaximumFallSpeed(0.6f);
//        setDecelerationSpeed(0.002f);
//
//        currentRoom = room;
//
//        /* Initial capacity of the inventory: 10 */
//        inventory = new Inventory(10);
//
//        try {
//            //Create Animators for both time lines.
//            this.presentAnimGen = new PlayerAnimator();
//            this.pastAnimGen = new PastPlayerAnimator();
//        } catch (SlickException ex) {
//            System.out.println(ex);
//        }
//        //FOr the moment, we will select the animations from the present time line.
//        this.animGen = this.presentAnimGen;
//        
//        this.currentAnim = animGen.getDie1R();
//    }
    
    /**Constructor in which we can specify the timeline to which
     the player belongs*/
    public Player(float x, float y, int width, int height, Room room,
            Timeline timeline) {
        super(x, y, width, height);

        setAccelerationSpeed(0.002f);
        setMaximumSpeed(0.30f);
        setMaximumFallSpeed(0.6f);
        setDecelerationSpeed(0.002f);

        currentRoom = room;

        /* Initial capacity of the inventory: 10 */
        inventory = new Inventory(10);
        
        //Create animator depending on timeline.
        if(timeline.compareTo(Timeline.PAST)==0){
            try {
                this.animGen = new PastPlayerAnimator();
            } catch (SlickException ex) {}
            //System.out.println("Im in the past");
        }
        else{
            try {
                this.animGen = new PlayerAnimator();
            } catch (SlickException ex) {}
            //System.out.println("Im in the present");
        }

        this.currentAnim = animGen.getDie1R();
    }

    /**
     * Handles the attack with the melee weapon, if available.
     */
    public void meleeAttack() {

        /* If there isn't any weapon, no attack can be performed */
        if (inventory.getMeleeWeapon() != null) {
            //In order to avoid spamming the attack button.
            //You cannot attack while climbing.
            if (!this.isAttacking && !this.isClimbing) {
                
                this.isAttacking = true;
                currentRoom.checkMeleeAttack();
                this.resetAttackState();
                //Just for debugging.
                //System.out.println(this.getStats().getAttackDamage());
                
            }

        }
    }

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
                animGen.resetAttackAnim();
            }
        });

        t1.start();
    }
    
    /**
     * When the player dies, there will be a new state created, like a "Game 
     * over" screen or something.
     */
    @Override
    public void die () {
        
        dead = true;
    }

    /**
     * Creates a new projectile, if any ranged weapon is available.
     */
    public void rangedAttack() {

        Projectile aux;

        /* If there isn't any weapon, no attack will be performed */
        if (inventory.getRangedWeapon() != null) {
            
            /* Creates a different projectile, depending on the current weapon */
            if (inventory.getRangedWeapon().isGun()) {
                
                /* Creates a new arrow and passes it to the observer */
                aux = ItemCreator.newBullet();

                /* Plays the sound of a gun being fired */
                MainClass.JUKEBOX.play(Playlist.SHOT, true, 20);
                
                currentRoom.addObservedProjectile(aux);
            } else {
                
                /* Creates a new arrow and passes it to the observer */
                aux = ItemCreator.newArrow();
                
                /* Plays the sound of an arrow being shot */
                MainClass.JUKEBOX.play(Playlist.ARROW_SHOT, true, 15);

                currentRoom.addObservedProjectile(aux);
            }
        }
    }
    
    /**
     * Performs a magical attack, if any magical weapon is selected
     */
    public void magicalAttack () {
        
        MagicalEffect aux;

        /* If there isn't any weapon, no attack will be performed */
        if (inventory.getMagicalWeapon() != null) {
            
            /* Checks if the player has enough mana */
            if (getStats().getManaPoints() >= 10) {
                
                /* Plays the sound of the magical weapon */
                MainClass.JUKEBOX.play(Playlist.FREEZING_SPELL, true, 20);
                
                /* Creates a new effect and passes it to the observer */
                aux = ItemCreator.newMagicalEffect (calculateCoordinates());

                currentRoom.addObservedEffect(aux);
                
                /* Decreases the mana points */
                getStats().useMana (10);
            }
        }
    }
    
    /**
     * Returns the coordinates where the effect should be created.
     */
    private Vector2f calculateCoordinates () {
        
        Vector2f coordinates = new Vector2f (
                                Mouse.getX(),
                                NumberUtils.invertOrdinate(Mouse.getY())
        );
        
        coordinates.x /= MainClass.SCALE;
        coordinates.y /= MainClass.SCALE;
        
        return coordinates;
    }

    public void interact() {
        for (GameObject obj : currentRoom.getGameObjects()) {
            if (obj instanceof InteractiveObject) {
                if (obj.getBoundingShape().checkCollision(this.getBoundingShape())) {
                    ((InteractiveObject) obj).performAction();
                }
            }
        }
    }

    /**
     * Returns the current player's inventory.
     *
     * @return The value of the inventory.
     */
    public Inventory getInventory() {

        return inventory;
    }

    /**
     * Changes the current inventory for a new one.
     *
     * @param inventory The new inventory.
     */
    public void setInventory(Inventory inventory) {

        this.inventory = inventory;
    }

    /**
     * Substitutes the current melee weapon for a new one.
     *
     * @param weapon The new weapon
     */
    public void setMeleeWeapon(MeleeWeapon weapon) {

        inventory.add(weapon);
        inventory.select(weapon.getKey());
        currentRoom.addItem(weapon);
    }

    /**
     * Substitutes the current ranged weapon for a new one.
     *
     * @param weapon The new weapon
     */
    public void setRangedWeapon(RangedWeapon weapon) {

        inventory.add(weapon);
        inventory.select(weapon.getKey());
    }

/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */
    
    /**
     * If the player is dead, this method will return <i>true</i>.
     * 
     * @return 
     *          Te value of {@code dead}.
     */
    public boolean isDead () {
        
        return dead;
    }
    
    public boolean getCanJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public boolean getCanClimb() {
        return canClimb;
    }

    public void setCanClimb(boolean canClimb) {
        this.canClimb = canClimb;
    }

    public void climbUp() {
        this.setYVelocity(-0.12f);
        //ANIMATION
        this.animGen.advanceClimbAnimation();
    }

    public void climbDown() {
        this.setYVelocity(0.12f);
        //ANIMATION
        this.animGen.advanceClimbAnimation();
    }

    public boolean getIsClimbing() {
        return isClimbing;
    }

    public void setIsClimbing(boolean isClimbing) {
        this.isClimbing = isClimbing;
    }

    public void climb() {
        this.setIsClimbing(true);
        this.setIgnoresGravity(true);
    }

    public void render(Graphics g) {
//        /*The Following lines should be in the update method, but
//        we would have to override it, so may be it is a little more safe.*/
//            /*  World index == 0 -> old timeline -> past animator
//                World index == 1 -> new timeline -> present animator */
//                    if (LevelState.worldIdx == 0 
//                            && !this.animGen.equals(this.pastAnimGen)) {
//                        this.animGen=this.pastAnimGen;
//                    }
//                    else if(LevelState.worldIdx == 1 
//                            && !this.animGen.equals(this.presentAnimGen)){
//                        this.animGen=this.presentAnimGen;
//                    }
//        /*--------------------------------------------------------------------*/
        //DEAD
        if (this.getStats().getLifePoints() <= 0) {
            if (this.getFacing().equals(this.getFacing().RIGHT)) {
                this.currentAnim = animGen.getDie1R();
            } else {
                this.currentAnim = animGen.getDie1L();
            }
        } //CLIMBING
        else if (this.getIsClimbing() && this.getStats().getLifePoints() > 0) {
            this.currentAnim = animGen.getClimb1();
        } //ON THE GROUND
        else if (this.isOnGround() && this.getStats().getLifePoints() > 0) {
            //reset jump animation
            this.animGen.resetJumpAnim();

            //WALK
            if (this.isMoving()) {
                //If the animation is not already the walk animation
                if (this.currentAnim != animGen.getWalk1R()
                        || this.currentAnim != animGen.getWalk1L()) {
                    if (this.getFacing().equals(this.getFacing().RIGHT)) {
                        this.currentAnim = animGen.getWalk1R();
                    } else {
                        this.currentAnim = animGen.getWalk1L();
                    }
                }
            } //IDLE
            else {
                //If the animation is not already the walk animation
                if (this.currentAnim != animGen.getIdle1R()
                        && this.currentAnim != animGen.getIdle1L()) {
                    if (this.getFacing().equals(this.getFacing().RIGHT)) {
                        this.currentAnim = animGen.getIdle1R();
                    } else {
                        this.currentAnim = animGen.getIdle1L();
                    }
                }

            }
            //NOT ON GROUND ~ JUMPING   
        } else if (this.currentAnim != animGen.getJump1R()
                && this.currentAnim != animGen.getJump1L()) {
            if (this.getFacing().equals(this.getFacing().RIGHT)) {
                this.currentAnim = animGen.getJump1R();
            } else {
                this.currentAnim = animGen.getJump1L();
            }
        }

        //ATTACK
        if (this.isAttacking) {
            if (this.currentAnim != animGen.getAttack1R()
                    || this.currentAnim != animGen.getAttack1L()) {
                if (this.getFacing().equals(this.getFacing().RIGHT)) {
                    this.currentAnim = animGen.getAttack1R();
                } else {
                    this.currentAnim = animGen.getAttack1L();
                }
            }
        }

        /*Calculations for properly placing
         the animations over the BoundingShape of the player*/
        int animWidth = this.currentAnim.getWidth();
        int animHeight = this.currentAnim.getHeight();

        int playerWidth = this.getWidth();
        int playerHeight = this.getHeight();
        /*Draw currrent animation.*/
        g.drawAnimation(currentAnim,
                this.getX() + playerWidth / 2 - animWidth / 2,
                this.getY() + playerHeight - animHeight);

    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {

        MeleeWeapon aux;

        this.currentRoom = currentRoom;

        /* If the player has a melee weapon, adds it to the items list */
        if ((aux = getInventory().getMeleeWeapon()) != null) {

            currentRoom.addItem(aux);
        }
    }
}
