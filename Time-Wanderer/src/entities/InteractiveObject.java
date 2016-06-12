package entities;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


/**
 * GameObject that will perform something when interacted with.
 * @author Pablo Peña
 * @author Sergio Sanchez
 */
public abstract class InteractiveObject extends GameObject {

    /*This animation may contain different sprites that will be
    used in the different states of the InteractiveObject.
    E.g. in the case of a chest, a sprite of it closed, and 
    another one of the chest opened.*/
    private Animation anim;
    private boolean isAnimationBeingAdvanced;
    private float scale;
    
    /**Standard contructor*/
    public InteractiveObject(float x, float y, int width, int height) {
        super(x, y, width, height);
        scale=1.0f;
        isAnimationBeingAdvanced=false;
        
        //ANIMATION
        this.anim=new Animation();
        //So that the sprites only change if we want to.
        //It is really not an animation
        anim.setAutoUpdate(false);
        anim.stop();
    }
    
    /**Constructor to also set the animation of the InteractiveObject.*/
    public InteractiveObject(float x, float y, int width, int height,
            String spriteSheetPath) {
        super(x, y, width, height);
        scale=1.0f;
        isAnimationBeingAdvanced=false;
        
        //ANIMATION
        this.anim=new Animation();
        //So that the sprites only change if we want to.
        //It is really not an animation
        anim.setAutoUpdate(false);
        anim.stop();
        
        this.setAnimation(spriteSheetPath);
    }
    
    /**Contructor for creating an InteractiveObject with the default size(32x32)*/
    public InteractiveObject(float x, float y) {
        super(x, y);
        scale=1.0f;
        isAnimationBeingAdvanced=false;
        
        //ANIMATION
        this.anim=new Animation();
        //So that the sprites only change if we want to.
        //It is really not an animation
        anim.setAutoUpdate(false);
        anim.stop();
    }
    
    /**Contructor for creating an InteractiveObject with the default size(32x32),
     and also allows to set the sprites of the InteractiveObject*/
    public InteractiveObject(float x, float y, String spriteSheetPath) {
        super(x, y);
        scale=1.0f;
        isAnimationBeingAdvanced=false;
        
        //ANIMATION
        this.anim=new Animation();
        //So that the sprites only change if we want to.
        //It is really not an animation
        anim.setAutoUpdate(false);
        anim.stop();
        
        this.setAnimation(spriteSheetPath);
    }
    
    /**Set scale of the animation of the interactive object.*/
    public void setScale(float s){
        this.scale=s;
    }
    
    /**Get scale of the animation of the interactive object.*/
    public float getScale(){
        return this.scale;
    }
    
    /**Alternative method to set animations of the InteractiveObject.
     In this case, this method will receive the path of the spriteSheet.
     The spriteSheet must have the different sprites with a size of 32x32
     and only one row.*/
    public void setAnimation(String spriteSheetPath){
        try {
            SpriteSheet sheet = new SpriteSheet(spriteSheetPath,32,32);
            
            for(int i=0; i<sheet.getHorizontalCount();i++){
                this.anim.addFrame(sheet.getSprite(i, 0) , 1000);        
            }
            
        } catch (SlickException ex) {System.out.println(ex);}
          catch(Exception e){System.err.println(e);}
        
        
        
    }
    
    /**Set the animation of the interactive object.*/
    public void setAnimation(Animation anim)
    {
        this.anim=anim;
        this.anim.stop();
        this.anim.setCurrentFrame(0);
    }
    
    public void advanceAnimation(){
        if(this.anim.getFrame()!=this.anim.getFrameCount()){
            this.anim.setCurrentFrame(this.anim.getFrame()+1);
        }
        else{//Last frame -> start again
            this.anim.setCurrentFrame(0);
        }
    }
    
    public void advanceAnimation(int frameToChangeTo){
        if(frameToChangeTo<this.anim.getFrameCount()){
            this.anim.setCurrentFrame(frameToChangeTo);
        }
        else{//Last frame -> start again
            System.out.println("InterativeObject.advanceAnimation() error :"
                    + "Invalid Index");
        }
    }
    
    public void scaleAnimation(){
        int l = this.anim.getFrameCount();
        Animation aux=new Animation();
        
        for(int i=0;i<l;i++){
            aux.addFrame(this.anim.getImage(i).getScaledCopy(scale), 
                    this.anim.getDuration(i));
        }
    }

    @Override
    public void render(Graphics g) {
//        Color previousColor = g.getColor();
//        g.setColor(Color.cyan);
//        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
//        g.setColor(previousColor);
        try{
            g.drawAnimation(anim, this.getX(), this.getY());
        }catch(IndexOutOfBoundsException ex){System.out.println(
                "(Interactive Object:161)Error when printing the Animation of an Interactive Object: "+
                ex);}
    }
    
    public abstract void performAction();

    /**
     * Advance the animation of the InteractiveObject. We have the attribute
     * isAnimationBeingAdvanced, that indicates us when the
     * animation is being advanced or not. This method checks when this happens,
     * if the animation is not being advanced, this method advances it; if not,
     * it does nothing
     *
     */
    /*
    public synchronized void advanceAnimation() {//VICTORY

        if (!isAnimationBeingAdvanced) {
            isAnimationBeingAdvanced = true;
            Thread t1 = new Thread(new Runnable() {

                @Override
                public void run() {

                    int n = anim.getFrameCount();
                    
                    if (anim.getFrame() != anim.getFrameCount() - 1) {
                        try {
                            sleep(anim.getDuration(anim.getFrame()));
                        } catch (InterruptedException ex) {
                        }
                        anim.setCurrentFrame(anim.getFrame() + 1);
                    } else//Last Frame -> start loop again
                    {
                        try {
                            sleep(anim.getDuration(anim.getFrame()));
                        } catch (InterruptedException ex) {
                        }
                        anim.setCurrentFrame(0);
                    }
                    isAnimationBeingAdvanced = false;
                }
            });
            t1.start();
        }
    }
    */
    
}
