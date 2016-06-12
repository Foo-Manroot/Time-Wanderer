package misc;

import entities.GameCharacter;
import entities.Player;
import items.MagicalEffect;
import items.MeleeWeapon;
import items.Projectile;
import java.util.concurrent.ConcurrentLinkedQueue;
import levels.Room;
import main.MainClass;
import sound.Playlist;

/**
 * Checks every character on the room and performs certain tasks if any
 * collision between two (or more) observed objects (characters) is detected.
 * This object helps on the detection of the enemies that are beeing hit by the
 * player or one of its projectiles.
 */
public class AttacksObserver {

    /**
     * List of observed game characters
     */
    private final ConcurrentLinkedQueue <GameCharacter> characters;
    
    /**
     * Player that will perform the attack
     */
    private Player player;
    
    /**
     * List of projectiles fired by the player with its ranged weapon 
     * (it can be empty if no projectiles where shot).
     */
    private final ConcurrentLinkedQueue <Projectile> projectiles;
    
    /**
     * List of magical effects made by the player with its magical weapon 
     * (it can be empty if no effects where created).
     */
    private final ConcurrentLinkedQueue <MagicalEffect> magicalEffects;
    
    /**
     * Room that's going to be observed.
     */
    private final Room room;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */

    /**
     * Constructor.
     *
     * @param room
     *          Room that this observer will control.
     */
    public AttacksObserver (Room room) {

        /* Initializations */
        this.characters = new ConcurrentLinkedQueue<>();
        this.projectiles = new ConcurrentLinkedQueue<>();
        this.magicalEffects = new ConcurrentLinkedQueue<>();
        this.player = room.getPlayer();
        this.room = room;
    }
    

    /**
     * Checks the position of every gamecharacter and resolves all conflicts.
     */
    public void update () {
        
        /* Iterates through the projectile list and checks any collision with 
        the enemies */
        if (!projectiles.isEmpty()) {
            
            for (Projectile p : projectiles) {
                
                for (GameCharacter g : characters) {
                    /* If the two objects collide, damages the game character */
                    if (p.checkCollision(g.getBoundingShape())) {
                        
                        /* Plays the sound of a projectile hit */
                        MainClass.JUKEBOX.play(Playlist.ARROW_HIT, true, 15);
                        
                        g.getHit(p.getAttack());
                        
                        /* Removes the current projectile from the list, so it
                        can't hit the same enemy again */
                        removeProjectile(p);
                    }
                }
            }
        }
        
        /* Makes the same with the magical effects */
        if (!magicalEffects.isEmpty()) {
         
            for (MagicalEffect m : magicalEffects) {
                
                for (GameCharacter g : characters) {
                    
                    /* This method hits the GameCharacter if it's required */
                    m.checkAndHit(g);
                }
            }
        }            
    }
    
    /**
     * This method will be called when the player attacks. Checks if its weapon 
     * (must be melee) is colliding with any enemy.
     */
    public void checkAttack () {

        MeleeWeapon weapon;
        
        /* If the player doen't have a melee weapon, returns */
        if ((weapon = player.getInventory().getMeleeWeapon()) == null) {
            return;
        }
        
        /* Now checks any collision between the weapon and the enemies */
        for (GameCharacter g : characters) {
            /* The method attack() already performs all actions needed (even 
            decreases the enemy's life) */
            weapon.attack(g);
        }
    }
    
/* ------------------------------------------------- */
/* ---- METHODS TO ADD AND REMOVE FROM THE LISTS --- */
/* ------------------------------------------------- */
    
    /**
     * Adds a new game character to the list of the controlled by this observer.
     * 
     * @param character 
     *              Game character that will be observed by this object.
     */
    public void addObserved (GameCharacter character) {
        
        /* If it's not in the list (to avoid errors on the collision detection),
        adds it */
        if (!characters.contains(character)) {
            characters.add(character);
        }
    }
    
    /**
     * Removes the game character, if it was on the list.
     * 
     * @param character 
     *              Game character that will be removed from the list.
     */
    public void removeObserved (GameCharacter character) {
        
        characters.remove(character);
    }
    
    /**
     * Adds a new projectile to the list.
     * 
     * @param projectile
     *              Projectile that's going to be observed.
     */
    public void addProjectile (Projectile projectile) {
        
        /* Checks if it already was on the list to avoid duplicates */
        if (!projectiles.contains(projectile)) {
            
            projectile.setObserver(this);
            projectiles.add(projectile);
        }
    }
    
    /**
     * Removes a projectile from the list, so it won't be observed anymore.
     * It also removes it from the room's list.
     * 
     * @param projectile
     *              GameObject that's going to be removed.
     */
    public void removeProjectile (Projectile projectile) {
        
        projectiles.remove(projectile);
        room.removeObservedProjectile(projectile);
    }
    
    /**
     * Adds a new magicalEffect to the list.
     * 
     * @param effect
     *              Magical effect that's going to be observed.
     */
    public void addEffect (MagicalEffect effect) {
        
        /* Checks if it already was on the list to avoid duplicates */
        if (!magicalEffects.contains(effect)) {
            
            effect.setObserver(this);
            magicalEffects.add(effect);
        }
    }
    
    /**
     * Removes a magical effect from the list, so it won't be observed anymore.
     * It also removes it from the room's list.
     * 
     * @param effect
     *              Magical effect that's going to be observed.
     */
    public void removeEffect (MagicalEffect effect) {
        
        magicalEffects.remove(effect);
        room.removeObservedEffect(effect);
    }
    
    
/* ---------------------------- */
/* ---- GETTERS AND SETTERS --- */
/* ---------------------------- */

    /**
     * Changes the player that will be controled.
     * 
     * @param player
     *          The new main player
     */
    public void setPlayer (Player player) {
        
        this.player = player;
    }
}
