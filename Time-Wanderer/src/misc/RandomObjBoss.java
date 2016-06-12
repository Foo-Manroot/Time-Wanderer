package misc;

import entities.BossEnemy;
import entities.Player;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import static java.lang.Thread.sleep;
import physics.BoundingRectangle;
import main.MainClass;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Random object coming out of the boss, damaging the player if it gets hit.
 * 
 * Each one of this objects will have a different, randomly generated, target.
 */
public class RandomObjBoss extends Thread {

    /**
     * Array that contains the random objects
     */
    private final ConcurrentLinkedQueue<Obj> objects;     

    /**
     * Sleep time in milliseconds
     */
    private int sleepMillis;
    
    /**
     * Factor that multiplies the speed to control it.
     */
    private final double baseSpeed;
    
    /**
     * Boss at which this generator will be attached.
     */
    private final BossEnemy boss;
    
    /**
     * Factor that, multiplied by the speed, will be the damage made to the 
     * player.
     */
    private final int damage;
    
    /**
     * If this attribute is <i>true</i>, no action will be performed
     */
    private boolean stopped;
    
    private Image projectileImage;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */

    /**
     * Constructor. To set the default settings, just make all parameters
     * negative.
     *
     * @param speed 
     *              Factor that multiplies the speed to control it.
     * @param sleepMilis 
     *              Sleep time in milliseconds between two random objects.
     * @param boss 
     *              Boss from which this objects will be generated.
     */
    public RandomObjBoss(double speed, int sleepMilis, BossEnemy boss) {

        objects = new ConcurrentLinkedQueue<>();

        /* If (sleepMillis < 0), sets the default value (5000 miliseconds */
        this.sleepMillis = (sleepMilis < 0) ? 5000 : sleepMilis;

        /* Again, if (speed < 0), sets the default value ( = 1) */
        this.baseSpeed = (speed < 0) ? 1 : speed;
        
        this.boss = boss;
        this.damage = (boss.getAttack() / 3);
        
        try {
            this.projectileImage = new Image("resources/items/BossObj.png");
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
        this.start();
    }
    
    /**
     * Updates the position of the objects
     * 
     * @param delta 
     *              Milliseconds that took the computer to render and update.
     * @param player
     *              Player that will get damage if gets hit by one of the
     *          objects.
     */
    public void update (int delta, Player player) {
        
        /* Travels around the list, updating all the objects */
        for (Obj o : objects) {
            
            /* If the object reached its target, it's removed */
            if (!o.update(delta, player))
                objects.remove(o);
        }
    }

    /**
     * Renders all the available objects
     *
     * @param g Graphics on which the image will be drawn
     */
    public void render(Graphics g) {

        /* Travels around the list, rendering all the objects */
        objects.stream().forEach((object) -> {

            object.render(g);
        });
    }
    
    /**
     * Creates a new object from time to time.
     */
    @Override
    public void run() {

        Obj aux;
        
        /* Infinite loop */
        while (true) {

            try {
                /* Sleeps a random time between 100 and 'sleepMillis' * 5 
                milliseconds before creating any object */
                sleep((long) (sleepMillis * 5 * Math.random() + 100));
                
                checkStopped();
                
                /* Creates a new object and adds it to the list */
                aux = new Obj(projectileImage);
                objects.add(aux);

            } catch (InterruptedException ex) {
                Logger.getLogger(RandomObjBoss.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
/* -------------------------------------- */
/* ---- THREAD STATE CONTROLL METHODS --- */
/* -------------------------------------- */

    /**
     * Returns <i>true</i> if the actions of this enemy are stopped.
     *
     * @return
     *          The value of the attribute <b>stopped</b>.
     */
    public boolean isStopped () {

        return stopped;
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
                Logger.getLogger(RandomObjBoss.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }
    
    /**
     * Stops the action of this enemy.
     */
    public synchronized void stopAction () {

        stopped = true;
    }

    /**
     * Restarts the action of this thread.
     */
    public synchronized void restart () {
        
        stopped = false;
        notifyAll();
    }


/*---------------------*/
/* Getters and setters */
/*---------------------*/
    
    /**
     * Returns the sleep time in milliseconds between the generation of two
     * random objects.
     *
     * @return sleepMillis
     */
    public int getSleepMillis() {

        return sleepMillis;
    }

    /**
     * Sets the sleep time in milliseconds between the generation of two random
     * objects. If it's a negative number, the default sleep time is set.
     *
     * @param sleepMillis
     */
    public void setSleepMillis(int sleepMillis) {

        this.sleepMillis = (sleepMillis < 0) ? 180000 : sleepMillis;
    }

/*-------------------------------------*/
/* End of the getters and setters zone */
/*-------------------------------------*/
    
    /* Inner class that represents one of those random objects */
    private class Obj {

        /**
         * Body of the object
         */
        private final BoundingRectangle body;
        
        /**
         * Target of this object
         */
        private final Vector2f unitVector;
        
        /**
         * Speed of this object.
         */
        private final float speed;
        
        private Image image;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */

        /**
         * Constructor.
         */
        public Obj(Image image) {

            float mod;
            
            /* Gets a random speed between 'baseSpeed'/2 and 'baseSpeed'*10 */
            this.speed = (float) ((baseSpeed/2) + Math.random() * baseSpeed * 5);
            /* Creates a new rectangle with size 20 x 20 coming from within the
            boss' body */
            body = new BoundingRectangle(
                    (float) (boss.getBoundingShape().getWidth() * Math.random())
                            + boss.getX(),
                    (float) (boss.getBoundingShape().getHeight() * Math.random() 
                            + boss.getY()),
                    20, 20);
            
            /* Creates a unit vector that will set the path that this object 
            will follow */
            unitVector = new Vector2f((float) (2 * Math.random() - 1), 
                                      (float) (2 * Math.random() - 1));
            
            /* Converts the vector to a unit vector */
            mod = (float) Math.sqrt(Math.pow(unitVector.x, 2) +
                          Math.pow(unitVector.y, 2));
            
            unitVector.x /= mod;
            unitVector.y /= mod;
            
            this.image = image;
        }
        
        /**
         * Updates the state of this projectile.
         * 
         * @param delta 
         *              Milliseconds that took the computer to update and render.
         * @param player 
         *              Player that will receive the damage if gets hit.
         */
        public boolean update (int delta, Player player) {
            
            Rectangle playZone = new Rectangle(0, 0, 
                                MainClass.WINDOW_WIDTH, 
                                MainClass.WINDOW_HEIGHT);
                        
            /* If this object is not within the game's window, it's destroyed */
            if (!playZone.contains(body.getX(), body.getY())) {
                
                return false;
            }
            
            if (stopped)
                return true;
            
            /* Checks if the player got hit by this object */
            if (this.body.checkCollision(player.getBoundingShape()))
                player.getHit((int) (damage * speed));
            
            /* Updates the position of the body */
            body.setX((float) (body.getX() + (unitVector.x * speed * delta * 0.1)));
            body.setY((float) (body.getY() + (unitVector.y * speed * delta * 0.1)));
            
            return true;
        }

        /**
         * Renders the object.
         *
         * @param g Graphics on which the image will be drawn.
         */
        public void render(Graphics g) {
            
            /* Draws the final object */
            g.drawImage(image,body.x,body.y);
        }
    }
}
