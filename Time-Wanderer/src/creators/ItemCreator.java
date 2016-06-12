package creators;

import entities.GameCharacter;
import entities.Player;
import entities.VersatileChest;
import items.Catalog;
import items.ConsumableItem;
import items.MagicalEffect;
import items.MagicalRelic;
import items.MagicalWeapon;
import items.MeleeWeapon;
import items.Projectile;
import items.RangedWeapon;
import java.util.Random;
import levels.Room;
import main.MainClass;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import utils.NumberUtils;

/**
 *
 * @author Miguel García Martín
 */
public class ItemCreator {
    
    private static Image bulletImage;
    private static Image arrowImage;
    private static Image magicImage;
    
    /**
     * Creates three chests into the room:
     *          - One with a melee weapon.
     *          - One with a health potion or a mana potion.
     *          - One with a ranged or a magical weapon.
     * 
     * @param room
     *          Room that's going to be filled.
     */
    public static void fillRoom (Room room) {
        
        /* First chest: melee weapon */
        VersatileChest chest1 = new VersatileChest(300, 300, 32, 32, room,
                                                   Catalog.MELEE_WEAPON);
        
        /* Second chest: health potion OR 
                         mana potion */
        VersatileChest chest2 = new VersatileChest(360, 300, 32, 32, room,
                (new Random().nextBoolean())? Catalog.HEALTH_POTION : 
                                              Catalog.MANA_POTION);
        
        /* Third chest: ranged weapon OR 
                        magical weapon */
        VersatileChest chest3 = new VersatileChest(420, 300, 32, 32, room,
                (new Random().nextBoolean())? Catalog.RANGED_WEAPON : 
                                              Catalog.MAGIC_WEAPON);
        
        room.addGameObject(chest1);
        room.addGameObject(chest2);
        room.addGameObject(chest3);
    }
    
    /**
     * Returns a new instance of a melee weapon.
     * 
     * @param player
     *          Player that will have this weapon.
     * 
     * @return 
     *          A new instance of a sword.
     */
    public static MeleeWeapon newSword (Player player) {
        
        int id = 0;
        int attack = 15;
        
        /* To avoid duplicates, generates a new id each time */        
        while (player.getInventory().toString().contains("Sword" + id)) {
            
            id++;
        }
        
        /* Gets the player's currently selected magical weapon, so the attack 
        can be updated */
        if (player.getInventory().getMeleeWeapon() != null) {
            
            attack = player.getInventory().getMeleeWeapon().getAttack() + 3;
        }
        
        /* Creates a new longsword */
        return new MeleeWeapon("LongSword", "A big, badass-looking, sword", 
                               "Sword" + id, player.getWidth() * 2, 
                                player.getHeight(), player, attack);
    }
    
    /**
     * Returns a new instance of a melee weapon.
     * 
     * @return 
     *          A new instance of a sword.
     */
    public static MeleeWeapon newSword () {
        
        int id = 0;
        int attack = 15;
        
        /* Gets the current player */
        Player aux = MainClass.players[MainClass.currentPlayer];
        
        /* To avoid duplicates, generates a new id each time */        
        while (aux.getInventory().toString().contains("Sword" + id)) {
            
            id++;
        }
        
        /* Gets the player's currently selected magical weapon, so the attack 
        can be updated */
        if (aux.getInventory().getMeleeWeapon() != null) {
            
            attack = aux.getInventory().getMeleeWeapon().getAttack() + 3;
        }
        
        /* Creates a new longsword */
        return new MeleeWeapon("LongSword", "A big, badass-looking, sword", 
                               "Sword" + id, aux.getWidth() * 2, 
                                aux.getHeight(), aux, attack);
    }
    
    /**
     * Returns a new instance of a Magical Relic.
     * 
     * @return 
     *          A new instance of a MagicalRelic.
     */
    public static MagicalRelic newMagicalRelic () {
        
        int id = 0;
        
        /* Gets the current player */
        Player aux = MainClass.players[MainClass.currentPlayer];
        
        /* To avoid duplicates, generates a new id each time */        
        while (aux.getInventory().toString().contains("Relic" + id)) {
            
            id++;
        }
        
        /* Creates a new MagicalRelic */
        return new MagicalRelic("Standard Relic", "A simple Magical Relic", 
                               "Relic" + id, 5 , aux);
    }
    
    
    /**
     * Returns a new instance of a Health Potion.
     * 
     * @return 
     *          A new instance of a helath potion.
     */
    public static ConsumableItem newHealthPotion () {

        /* Gets the current player */
        Player aux = MainClass.players[MainClass.currentPlayer];
        
        /* Creates a new health potion */
        return new ConsumableItem(
                            "Just a simple Health Potion",
                            "Health Potion", "HP_Potion", aux, 100,
                            Catalog.HEALTH_POTION);
    }
    
    /**
     * Returns a new instance of a Health Potion.
     * 
     * @return 
     *          A new instance of a helath potion.
     */
    public static ConsumableItem newManaPotion () {
        
        /* Gets the current player */
        Player aux = MainClass.players[MainClass.currentPlayer];

        /* Creates a new mana potion */
        return new ConsumableItem(
                            "Just a simple Mana Potion",
                            "Mana Potion", "Mana_Potion", aux, 100,
                            Catalog.MANA_POTION);
    }
    
    
     /**
     * Returns a new instance of a magical weapon.
     * 
     * @return 
     *          A new instance of a magical weapon.
     */
    public static MagicalWeapon newMagicalWeapon () {
        
        int id = 0;
        int attack = 10;
        
        /* Gets the current player */
        Player aux = MainClass.players[MainClass.currentPlayer];
        
        /* To avoid duplicates, generates a new id each time */        
        while (aux.getInventory().toString().contains("Magical" + id)) {
            
            id++;
        }
        
        /* Gets the player's currently selected magical weapon, so the attack 
        can be updated */
        if (aux.getInventory().getMagicalWeapon()!= null) {
            
            attack = aux.getInventory().getMagicalWeapon().getAttack() + 5;
        }
        
        /* Creates a new magical weapon */
        return new MagicalWeapon("Magical Weapon", "Magical" + id, attack);
    }
    
    
    
    /**
     * Returns a new instance of a ranged weapon.
     * 
     * @return 
     *          A new instance of a bow.
     */
    public static RangedWeapon newBow () {
        
        int id = 0;
        int attack = 5;
        
        /* Gets the current player */
        Player aux = MainClass.players[MainClass.currentPlayer];
        
        /* To avoid duplicates, generates a new id each time */        
        while (aux.getInventory().toString().contains("Bow" + id)) {
            
            id++;
        }
        
        /* Gets the player's currently selected magical weapon, so the attack 
        can be updated */
        if (aux.getInventory().getRangedWeapon() != null) {
            
            attack = aux.getInventory().getRangedWeapon().getAttack() + 1;
        }
        
        return new RangedWeapon("Bow", "Bow" + id, false, attack);
    }
    
    /**
     * Returns a new instance of a ranged weapon.
     * 
     * @return 
     *          A new instance of a gun.
     */
    public static RangedWeapon newGun () {
        
        int id = 0;
        int attack = 5;
        
        /* Gets the current player */
        Player aux = MainClass.players[MainClass.currentPlayer];
        
        /* To avoid duplicates, generates a new id each time */        
        while (aux.getInventory().toString().contains("Gun" + id)) {
            
            id++;
        }
        
        /* Gets the player's currently selected magical weapon, so the attack 
        can be updated */
        if (aux.getInventory().getRangedWeapon()!= null) {
            
            attack = aux.getInventory().getRangedWeapon().getAttack() + 1;
        }
        
        return new RangedWeapon("Gun", "Gun" + id, true, attack);
    }
    
    /**
     * Creates and returns a new arrow.
     * 
     * @return 
     *          A new instance of a projectile.
     */
    public static Projectile newArrow () {
        
        /* Gets the current player and the attack from its ranged weapon */
        Player player = MainClass.players[MainClass.currentPlayer];
        int attack = (player.getInventory().getRangedWeapon()== null)?
                      0 :
                      player.getInventory().getRangedWeapon().getAttack();
        
        Vector2f speed = getSpeed(player);
        
        /* Corrects the speed so it is more like a slow arrow than 
        a fast bullet */
        speed.x /= 2;
        speed.y /= 2;
        
        if(arrowImage == null) {
            try {
                arrowImage = new Image("resources/items/Arrow.png");
            } catch (SlickException ex) {
                ex.printStackTrace();
            }
        }
        if(speed.x < 0) {
            return new Projectile(player, 32, 9, speed, attack, arrowImage.getFlippedCopy(true, false));
        } else {
            return new Projectile(player, 32, 9, speed, attack, arrowImage);
        }
        
    }
    
    /**
     * Creates and returns a new arrow.
     * 
     * @return 
     *          A new instance of a projectile.
     */
    public static Projectile newBullet () {
        
        /* Gets the current player */
        Player player = MainClass.players[MainClass.currentPlayer];
        int attack = (player.getInventory().getRangedWeapon()== null)?
                      0 :
                      player.getInventory().getRangedWeapon().getAttack();
        
        Vector2f speed = getSpeed(player);
        if(bulletImage == null) {
            try {
                bulletImage = new Image("resources/items/Bullet.png");
            } catch (SlickException ex) {
                ex.printStackTrace();
            }
        }
        return new Projectile(player, 9, 9, speed, attack, bulletImage);
    }
    
    
    /**
     * Creates and returns a new magical effect.
     * 
     * @param coordinates 
     *              Coordinates where the effect should be shown.
     * 
     * @return 
     *              A new instance of a magical effect.
     */
    public static MagicalEffect newMagicalEffect (Vector2f coordinates) {
        
        /* Gets the current player and its magical weapon attack */
        Player player = MainClass.players[MainClass.currentPlayer];
        int attack = (player.getInventory().getMagicalWeapon() == null)?
                      0 :
                      player.getInventory().getMagicalWeapon().getAttack();
        
        if(magicImage == null) {
            try {
                magicImage = new Image("resources/items/Spell.png");
            } catch (SlickException ex) {
                ex.printStackTrace();
            }
        }
        
        MagicalEffect effect = new MagicalEffect(coordinates, 10, attack, magicImage);
        
        return effect;
    }
    
    
    /**
     * Returns the calculated speed that the projectile has to have, depening 
     * on tha facing of the player and the position of the mouse.
     */
    private static Vector2f getSpeed (Player player) {
        
        int facing = (player.getFacing() == GameCharacter.Facing.RIGHT)? 1 : -1;
        Vector2f mouseVector = new Vector2f();
        float mod = 0;
        
        /* Calculates the vector that links the mouse position with 
        the player's center. */
        mouseVector.x = Math.abs(Mouse.getX() - 
                                (player.getX() + (player.getWidth() / 2)));
        
        mouseVector.y = (NumberUtils.invertOrdinate(Mouse.getY()) - 
                                (player.getY() - (player.getHeight() / 2)));
        
        /* Applies the facing to the speed */
        mouseVector.x *= facing;
        
        /* Only the unit vector is wanted, so the previous vector must be 
        divided by its module */
        mod = (float) Math.sqrt(Math.pow(mouseVector.x, 2) +
                                Math.pow(mouseVector.y, 2));

        mouseVector.x /= mod;
        mouseVector.y /= mod;
        
        
        return mouseVector;
    }
}
