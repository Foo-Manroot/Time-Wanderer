package items;

import entities.GameCharacter;
import physics.BoundingRectangle;

/**
 * This class represents a melee weapon, like a sword or staff.
 * 
 *      To implement this object, a square is drawn in front of the player, and
 * it becomes active and detects collisions with the enemies.
 */
public class MeleeWeapon extends Item {
    
    /* ATTRIBUTES: */

    /**
     * Body of the weapon. This is a box that collides with the enemies when
     * the player attacks.
     */
    private BoundingRectangle collider;
    
    /**
     * Player on which this weapon attached.
     */
    private GameCharacter player;
    
    /**
     * Width of the collider.
     */
    private int width;
    
    /**
     * Height of the collider.
     */
    private int height;
    
    /**
     * Attack of the weapon.
     */
    private int attack;
    
/* --------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION ---- */
/* --------------------------------------- */
    
    /* METHODS: */

    /**
     * Constructor.
     *      If the default size is desired, the parameters <i>width</i> or 
     * <i>length</i> (or both) should be negative.
     * 
     * @param name Name of the item.
     * @param key Key of the item.
     * @param width Width of the weapon.
     * @param height Length of the weapon.
     * @param player Character that has this weapon.
     * @param attack Attack of the weapon.
     */    
    public MeleeWeapon(String name, String key, int width, int height, 
                       GameCharacter player, int attack) {

        super (Catalog.MELEE_WEAPON, name, key, false);
        
        initAttributes(width, height, player, attack);
    }
    

    /**
     * Constructor.
     *      If the default size is desired, the parameters <i>width</i> or 
     * <i>length</i> (or both) should be negative.
     * 
     * @param name Name of the item.
     * @param description Description.
     * @param key Key of the item.
     * @param width Width of the body.
     * @param height Length of the body.
     * @param player Character that has this weapon.
     * @param attack Attack of the weapon.
     */
    public MeleeWeapon(String name, String description, String key, int width, 
                       int height, GameCharacter player, int attack) {

        super (Catalog.MELEE_WEAPON, name, key, false);
        
        this.description=description;
        
        initAttributes(width, height, player, attack);
    }
    
    
    /**
     * Tasks that both constructors must perform
     */
    private void initAttributes (int width, int height, 
                                GameCharacter player, int attack) {
     
        this.player = player;
        this.attack = attack;
        
        /* If width or length are negative, the default values are set */
        if ( (width < 0) || (height < 0)) {
            
            this.width = player.getWidth() / 2;
            this.height = player.getHeight();
            
            /* Initially facing right */
            collider = new BoundingRectangle(player.getX() + player.getWidth(),
                                    player.getY(), this.width, this.height);
        } else {
            
            this.width = width;
            this.height = height;
            
            /* Initially facing right */
            collider = new BoundingRectangle(player.getX() + player.getWidth(),
                                     player.getY(), this.height, this.width);
        }
    }
    
    /**
     * Updates the location of the weapon.
     */
    public void update () {
     
        /* The location of the box depends on where the character is facing to */
        if (player.getFacing() == GameCharacter.Facing.RIGHT) {
            
            /* Facing right -> the box is on the right of the character */
            collider.setX(player.getX() + player.getWidth());
        } else {
            
            /* Facing left -> the box is on the left of the character */
            collider.setX(player.getX() - collider.width);
        }
        
        collider.setY(player.getY());
    }
    
    
//    /**
//     * Draws the image.
//     * 
//     * @param g Graphic where the image will be drawn.
//     */
//    @Override
//    public void render (Graphics g) {
//
//        Rectangle aux = new Rectangle(collider.x, collider.y, collider.width, 
//                                      collider.height);
//        
//        g.setColor(Color.cyan);
//        g.draw(aux);
//        g.setColor(Color.green);
//    }
  
    /**
     * Returns <i>true</i> if the character collides with the weapon's collider.
     */
    private boolean checkCollision (GameCharacter character) {
        
        return collider.checkCollision(character.getBoundingShape());
    }
    
    /**
     * Checks if the object colllides with any enemy.
     * 
     * @param character Character that's going to be attacked.
     */
    public void attack (GameCharacter character) {
        
        /* If the two objects collide, applies the attack action */
        if (checkCollision(character)) {
            
            character.getHit(attack); /* The enemy's life is decreased */
        }
    }
    
    
    /**
     * This method won't be implemented, so it won't do anything.
     * To use this weapon, call the method {@code attack()}.
     */
    @Override
    public void use() {
        
        System.out.println("MeleeWeapon's use() method NOT IMPLEMENTED");
    }
    
    
/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */ 
    
    /**
     * Returns the value of attack.
     *
     * @return The value of attack
     */
    public int getAttack() {
        
        return attack;
    }

    /**
     * Sets the value of attack.
     *
     * @param attack New value of attack
     */
    public void setAttack(int attack) {
        
        this.attack = attack;
    }

    
    /**
     * In case of a weapon being changed between characters.
     * 
     * @param character New character that holds this weapon.
     */
    public void setCharacter (GameCharacter character) {
        
        player = character;
    }
    
    /**
     * @return A description of the item properties. */
    @Override
    public String toString() {
        String info = "";

        info += "NAME: " + name + "\n";
        info += "KIND: " + kind + "\n";
        info += "KEY: " + key + "\n";
        info += "DAMAGE: " + attack + "\n";
        info += "DESCRIPTION: " + description;

        return info;
    }
}