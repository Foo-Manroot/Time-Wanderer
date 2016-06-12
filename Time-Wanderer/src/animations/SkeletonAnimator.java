/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animations;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author propietario
 */
public class SkeletonAnimator {
    private Image spriteSheetImg;
    private SpriteSheet spriteSheet;   

    private float scale;
    
    //Right
    private Animation idle1R;
    private Animation walk1R;
    private Animation attack1R;
    //Left
    private Animation idle1L;
    private Animation walk1L;
    private Animation attack1L;

    public SkeletonAnimator(){
        init();
    }
    
    /**Set scale of the animations*/
    public void setScale (float sc){
        this.scale=sc;
    }
    
    /**Reset animatios that are not in a loop.*/
    public void resetAnim(){

    }
    
    /**Build all the animations from the spritesheet.*/
    private void init() {
        //r1=new Rectangle(600,200,50,50);
        scale = 1f;

        try {
            spriteSheetImg = new Image("resources/character/SkelSpriteSheet.png");
        } catch (Exception ex) {}
        spriteSheet = new SpriteSheet(spriteSheetImg, 96, 96);

        
        
        //Right
        idle1R = new Animation();
        createIdle1R(idle1R);
        walk1R = new Animation();
        createWalk1R(walk1R);
        attack1R = new Animation();
        createAttack1R(attack1R);
        
        //Left
        idle1L = new Animation();
        createIdle1L(idle1L);
        walk1L = new Animation();
        createWalk1L(walk1L);
        attack1L = new Animation();
        createAttack1L(attack1L);

    }

//----------------------------ANIM_CREATION-------------------------------------
    /*All these methods are the same, we choose a different row of the 
     spriteSheet in each method, because each row corresponds to a different
     animation. We iterate through each row, adding the sprites to the animation.
     Animations may have a different number of frames than the rest, that is why
     the for inside each method might have a different number of iterations.
     R stands for right and L for left*/
    
    /**
     * Create animation of idle right
     *
     * @param anim Animation where we will store the result
     */
    public void createIdle1R(Animation anim) {
        //no idle animation, so just add a frame
        //for (int i = 0; i < spriteSheet.getHorizontalCount()-1; i++) {
            anim.addFrame(spriteSheet.getSprite(0, 0).getScaledCopy(this.scale), 250);
        //}
            anim.setLooping(false);
    }
    /**
     * Create animation of walk right
     *
     * @param anim Animation where we will store the result
     */
    public void createWalk1R(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount()-2; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 0).getScaledCopy(this.scale), 250);
        }
    }
    /**
     * Create animation of attack right
     *
     * @param anim Animation where we will store the result
     */
    public void createAttack1R(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount()-3; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 1).getScaledCopy(this.scale), 250);
        }
    }
    

//----------------------LEFT_ANIMATIONS-----------------------------------------
    
    /**
     * Create animation of idle left
     *
     * @param anim Animation where we will store the result
     */
    public void createIdle1L(Animation anim) {
        //no idle animation, so just add a frame
        //for (int i = 0; i < spriteSheet.getHorizontalCount()-1; i++) {
            anim.addFrame(spriteSheet.getSprite(0, 0).getFlippedCopy(true, false).getScaledCopy(this.scale), 250);
        //}
            anim.setLooping(false);
    }
    /**
     * Create animation of walk left
     *
     * @param anim Animation where we will store the result
     */
    public void createWalk1L(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount()-2; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 0).getFlippedCopy(true, false).getScaledCopy(this.scale), 250);
        }
    }
    /**
     * Create animation of attack left
     *
     * @param anim Animation where we will store the result
     */
    public void createAttack1L(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount()-3; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 1).getFlippedCopy(true, false).getScaledCopy(this.scale), 250);
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
//Right-------------------------------------------------------------------------
    /**
     * Obtain animation of idle right.
     *
     * @return Animation where we will store the result.
     */
    public Animation getIdle1R() {
        return this.idle1R;
    }
    /**
     * Obtain animation of walk right.
     *
     * @return Animation where we will store the result.
     */
    public Animation getWalk1R() {
        return this.walk1R;
    }
    /**
     * Obtain animation of attack right.
     *
     * @return Animation where we will store the result.
     */
    public Animation getAttack1R() {
        return this.attack1R;
    }

//LEFT--------------------------------------------------------------------------
    /**
     * Obtain animation of idling left.
     *
     * @return anim Animation where we will store the result.
     */
    public Animation getIdle1L() {
        return this.idle1L;
    }
    /**
     * Obtain animation of walk left.
     *
     * @return Animation where we will store the result.
     */
    public Animation getWalk1L() {
        return this.walk1L;
    }
    /**
     * Obtain animation of attack left.
     *
     * @return Animation where we will store the result.
     */
    public Animation getAttack1L() {
        return this.attack1L;
    }
    
}
