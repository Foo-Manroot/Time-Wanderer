package animations;


import static java.lang.Thread.sleep;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Pablo Peña
 */
public class PlayerAnimator implements PlayerAnimatorInterface{

    //private Rectangle r1;
    private Image spriteSheetImg;
    private SpriteSheet spriteSheet;
    //private Image hoja3;
    private float dimx;
    private float dimy;

    private float scale;

    private Animation climb1;
    //Right
    private Animation die1R; 
    private Animation idle1R;
    private Animation walk1R;
    private Animation jump1R;
    private Animation attack1R;
    //Left
    private Animation die1L;
    private Animation idle1L;
    private Animation walk1L;
    private Animation jump1L;
    private Animation attack1L;

    private boolean isClimbingAnimationBeingAdvanced;

    public PlayerAnimator() throws SlickException {
        init();
    }
    
    /**Set scale of the animations*/
    public void setScale (float sc){
        this.scale=sc;
    }
    
    /**Reset Jumps animations because they are not in a loop.*/
    public void resetJumpAnim(){
        this.jump1L.restart();
        this.jump1R.restart();
    }
    
    /**Reset Attack animations because they are not in a loop.*/
    public void resetAttackAnim(){
        this.attack1L.restart();
        this.attack1R.restart();
    } 
    
    /**Build all the animations from the spritesheet.*/
    private void init() throws SlickException {
        //r1=new Rectangle(600,200,50,50);
        scale = 1.0f;

        spriteSheetImg = new Image("./src/resources/character/Final Sprite.png");
        spriteSheet = new SpriteSheet(spriteSheetImg, 96, 96);

        isClimbingAnimationBeingAdvanced = false;
        
        //Right
        die1R = new Animation();
        createDie1R(die1R);
        idle1R = new Animation();
        createIdle1R(idle1R);
        jump1R = new Animation();
        createJump1R(jump1R);
        walk1R = new Animation();
        createWalk1R(walk1R);
        attack1R = new Animation();
        createAttack1R(attack1R);
        //Left
        die1L = new Animation();
        createDie1L(die1L);
        idle1L = new Animation();
        createIdle1L(idle1L);
        jump1L = new Animation();
        createJump1L(jump1L);
        walk1L = new Animation();
        createWalk1L(walk1L);
        attack1L = new Animation();
        createAttack1L(attack1L);
        //Climb
        climb1 = new Animation();
        createClimb1(climb1);

    }

//----------------------------ANIM_CREATION-------------------------------------
    /*All these methods are the same, we choose a different row of the 
     spriteSheet in each method, because each row corresponds to a different
     animation. We iterate through each row, adding the sprites to the animation.
     Animations may have a different number of frames than the rest, that is why
     the for inside each method might have a different number of iterations.
     R stands for right and L for left*/
    
    /**
     * Create animation of dying towards right
     *
     * @param anim Animation where we will store the result
     */
    public void createDie1R(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount() - 1; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 0).getScaledCopy(this.scale), 150);
        }
        anim.setLooping(false);
    }

    /**
     * Create animation of idle right
     *
     * @param anim Animation where we will store the result
     */
    public void createIdle1R(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount() - 1; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 1).getScaledCopy(this.scale), 250);
        }
    }

    /**
     * Create animation of walking right
     *
     * @param anim Animation where we will store the result
     */
    public void createWalk1R(Animation anim) {

        for (int i = 1; i < spriteSheet.getHorizontalCount() - 2; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 2).getScaledCopy(this.scale), 150);
        }
    }

    /**
     * Create animation of jump to right
     *
     * @param anim Animation where we will store the result
     */
    public void createJump1R(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount() - 1; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 3).getScaledCopy(this.scale), 150);
        }
        anim.setLooping(false);

    }
    
    
    /**
     * Create animation of sword-attack 1 right
     *
     * @param anim Animation where we will store the result
     */
    public void createAttack1R(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount() - 3; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 4).getScaledCopy(this.scale), 150);
        }
    }

    /**
     * Create animation of climbing
     *
     * @param anim Animation where we will store the result
     */
    public void createClimb1(Animation anim) {
        for (int i = 0; i < spriteSheet.getHorizontalCount() - 2; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 5).getScaledCopy(this.scale), 150);
        }
        anim.setAutoUpdate(false);
    }

    /**
     * Advance the animation of climbing. We have the attribute
     * isClimbingAnimationBeingAdvanced, that indicates us when the climbing
     * animation is being advanced or not. This method checks when this happens,
     * if the animation is not being advanced, this method advances it; if not,
     * it does nothing
     *
     * 
     */
    public synchronized void advanceClimbAnimation() {//VICTORY
        
        if (!isClimbingAnimationBeingAdvanced) {
            isClimbingAnimationBeingAdvanced = true;
            Thread t1 = new Thread(new Runnable() {

                @Override
                public void run() {

                    int n = climb1.getFrameCount();
                    /*Not on the last frame*/
                    if (climb1.getFrame() != climb1.getFrameCount() - 1) {
                        try {
                            sleep(climb1.getDuration(climb1.getFrame()));
                        } catch (InterruptedException ex) {
                        }
                        climb1.setCurrentFrame(climb1.getFrame() + 1);
                    } else//Last Frame -> start loop again
                    {
                        try {
                            sleep(climb1.getDuration(climb1.getFrame()));
                        } catch (InterruptedException ex) {
                        }
                        climb1.setCurrentFrame(0);
                    }
                    isClimbingAnimationBeingAdvanced = false;
                }

            });

            t1.start();
        }

    }

//----------------------LEFT_ANIMATIONS-----------------------------------------
    
    /**
     * Create animation of dying towards left
     *
     * @param anim Animation where we will store the result
     */
    public void createDie1L(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount()-1; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 0).getFlippedCopy(true, false).getScaledCopy(this.scale), 150);
        }
        anim.setLooping(false);
    }

    
    /**
     * Create animation of idling left
     *
     * @param anim Animation where we will store the result
     */
    public void createIdle1L(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount()-1; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 1).getFlippedCopy(true, false).getScaledCopy(this.scale), 250);
        }
    }

    /**
     * Create animation of walking left
     *
     * @param anim Animation where we will store the result
     */
    public void createWalk1L(Animation anim) {

        for (int i = 1; i < spriteSheet.getHorizontalCount()-2; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 2).getFlippedCopy(true, false).getScaledCopy(this.scale), 150);
        }
    }
    
    /**
     * Create animation of jumping left
     *
     * @param anim Animation where we will store the result
     */
    public void createJump1L(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount()-1; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 3).getFlippedCopy(true, false).getScaledCopy(this.scale), 150);
        }
        anim.setLooping(false);

    }


    /**
     * Create animation of sword-attack 1 left
     *
     * @param anim Animation where we will store the result
     */
    public void createAttack1L(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount() - 3; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 4).getFlippedCopy(true, false).getScaledCopy(this.scale), 150);
        }
    }

//****************************ANIM_CREATION*************************************
//----------------------------ANIM_GETTER---------------------------------------
    /*All these methods are the same, we choose a different row of the 
     spriteSheet in each method, because each row corresponds to a different
     animation. We iterate through each row, adding the sprites to the animation.
     Animations may have a different number of frames than the rest, that is why
     the for inside each method might have a different number of iterations.
     R stands for right and L for left*/
//CLIMB-------------------------------------------------------------------------
    /**
     * Obtain animation of climbing
     *
     * @return anim Animation where we will store the result
     */
    public Animation getClimb1() {
        return this.climb1;
    }
//LEFT--------------------------------------------------------------------------    
    /**
     * Obtain animation of idle right
     *
     * @return Animation where we will store the result
     */
    public Animation getIdle1R() {
        return this.idle1R;
    }

    /**
     * Obtain animation of jump to right
     *
     * @return anim Animation where we will store the result
     */
    public Animation getJump1R() {

        return this.jump1R;
    }

    /**
     * Obtain animation of walking right
     *
     * @return anim Animation where we will store the result
     */
    public Animation getWalk1R() {

        return this.walk1R;
    }

    /**
     * Obtain animation of dying towards right
     *
     * @return anim Animation where we will store the result
     */
    public Animation getDie1R() {

        return this.die1R;
    }

    /**
     * Obtain animation of sword-attack 1 right
     *
     * @return anim Animation where we will store the result
     */
    public Animation getAttack1R() {
        return this.attack1R;
    }

//LEFT--------------------------------------------------------------------------
    /**
     * Obtain animation of idling left
     *
     * @return anim Animation where we will store the result
     */
    public Animation getIdle1L() {
        return this.idle1L;
    }

    /**
     * Obtain animation of jumping left
     *
     * @return anim Animation where we will store the result
     */
    public Animation getJump1L() {
        return this.jump1L;

    }

    /**
     * Obtain animation of walking left
     *
     * @return anim Animation where we will store the result
     */
    public Animation getWalk1L() {
        return this.walk1L;
    }

    /**
     * Obtain animation of dying towards left
     *
     * @return anim Animation where we will store the result
     */
    public Animation getDie1L() {
        return this.die1L;
    }

    /**
     * Obtain animation of sword-attack 1 left
     *
     * @return anim Animation where we will store the result
     */
    public Animation getAttack1L() {
        return this.attack1L;
    }

//****************************ANIM_GETTER***************************************
}
