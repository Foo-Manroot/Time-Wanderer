package animations;


import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Pablo Peña
 */
public class BatAnimator {

    private Image spriteSheetImg;
    private SpriteSheet spriteSheet;   

    private float scale;
    
    //Right
    private Animation idle1R;

    //Left
    private Animation idle1L;

    public BatAnimator(){
        try {
            init();
        } catch (SlickException ex) {System.err.println(ex);}
    }
    
    /**Set scale of the animations*/
    public void setScale (float sc){
        this.scale=sc;
    }
    
    /**Reset animatios that are not in a loop.*/
    public void resetAnim(){

    }
    
    /**Build all the animations from the spritesheet.*/
    private void init() throws SlickException {
        //r1=new Rectangle(600,200,50,50);
        scale = 1.0f;

        spriteSheetImg = new Image("./src/resources/character/Murcielo final.png");
        spriteSheet = new SpriteSheet(spriteSheetImg, 96, 96);

        
        
        //Right
        idle1R = new Animation();
        createIdle1R(idle1R);
        
        //Left
        idle1L = new Animation();
        createIdle1L(idle1L);

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

        for (int i = 0; i < spriteSheet.getHorizontalCount()-1; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 0).getScaledCopy(this.scale), 250);
        }
    }


//----------------------LEFT_ANIMATIONS-----------------------------------------
   
    /**
     * Create animation of idling left
     *
     * @param anim Animation where we will store the result
     */
    public void createIdle1L(Animation anim) {

        for (int i = 0; i < spriteSheet.getHorizontalCount()-1; i++) {
            anim.addFrame(spriteSheet.getSprite(i, 0).getFlippedCopy(true, false).getScaledCopy(this.scale), 250);
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
     * Obtain animation of idle right
     *
     * @return Animation where we will store the result
     */
    public Animation getIdle1R() {
        return this.idle1R;
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

//****************************ANIM_GETTER***************************************
}