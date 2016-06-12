/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import creators.ItemCreator;
import gamestates.LevelState;
import items.Catalog;
import items.ConsumableItem;
import items.MagicalRelic;
import items.MagicalWeapon;
import items.MeleeWeapon;
import items.RangedWeapon;
import levels.Room;

/**
 *
 * @author propietario
 */
public class ChestObject extends InteractiveObject {

    private Catalog type;
    private final Room room;
    private boolean hasBounced = false;
    /*Height the object will have in comparison with the chest*/
    private int relativeHeight = 20;

    /**
     * Standard contructor.
     */
    public ChestObject(float x, float y, int width, int height,
            Room room, Catalog type) {

        super(x, y, width, height);

        this.room = room;

        this.type = type;

        //create
        this.setYVelocity(-0.2f);
        this.room.addGameObject(this);
    }

    /**
     * Constructor using a VersatileChest as a model to set the attributes.
     */
    ChestObject(VersatileChest vc) {
        /*We set all the attributes using the chest*/
        super(vc.getX(), vc.getY() - 10, vc.getWidth(), vc.getHeight());

        this.room = vc.getRoom();

        //set type
        this.type = vc.chestContent;

        //create
        //System.err.println("---> BEFORE");
        this.setY(this.getY() - 10);
        this.room.addGameObject(this);
        this.setY(this.getY() - 10);
        //System.err.println("---> AFTER");
        this.setYVelocity(-0.3f);

    }

    /**
     * Constructor using a VersatileChest as a model to set the attributes. This
     * also allows us to sheet the sprites for this ChestObject.
     */
    ChestObject(VersatileChest vc, String spriteSheetPath) {
        /*We set all the attributes using the chest*/
        super(vc.getX(), vc.getY() - 10, vc.getWidth(), vc.getHeight(),
                spriteSheetPath);

        this.room = vc.getRoom();

        //set type
        this.type = vc.chestContent;

        //create
        //System.err.println("---> BEFORE");
        this.setY(this.getY() - 10);
        this.room.addGameObject(this);
        this.setY(this.getY() - 10);
        //System.err.println("---> AFTER");
        this.setYVelocity(-0.3f);

    }

    @Override
    public void performAction() {
        if (this.isOnGround() && this.getYVelocity() == 0) {
            //System.out.println("OBJECT!");
            //give player content
            switch (this.type) {
                case HEALTH_POTION:
                    //create a potion
                    ConsumableItem ci = ItemCreator.newHealthPotion();
                    //add potion to inventory
                    //System.err.println("BEFORE: \n" + this.room.getPlayer().inventory.toString());
                    this.room.getPlayer().inventory.add(ci);
                    //System.err.println("Added Potion");
                    //System.err.println("AFTER: \n" + this.room.getPlayer().inventory.toString());

                    //delete object from the room. CAUSES ERROR
                    synchronized (this.room) {
                        this.room.removeObject(this);
                    }
                break;
            
                case MANA_POTION:
                    //create a mana potion
                    ConsumableItem ciMana = ItemCreator.newManaPotion();
                    //add potion to inventory
                    //System.err.println("BEFORE: \n" + this.room.getPlayer().inventory.toString());
                    this.room.getPlayer().inventory.add(ciMana);
                    //System.err.println("Added Potion");
                    //System.err.println("AFTER: \n" + this.room.getPlayer().inventory.toString());

                    //delete object from the room. CAUSES ERROR
                    synchronized (this.room) {
                        this.room.removeObject(this);
                    }
                    break;
            
                case MELEE_WEAPON:
                    MeleeWeapon mw = ItemCreator.newSword();
                    
                    //add weapon to inventory
                    //System.err.println("BEFORE: \n" + this.room.getPlayer().inventory.toString());
                    this.room.getPlayer().inventory.add(mw);
                    //System.err.println("Added MeleeWeapon");
                    //System.err.println("AFTER: \n" + this.room.getPlayer().inventory.toString());

                    //To equip the sword we have just got.
                    this.room.getPlayer().getInventory().select(mw.getKey());

                    //delete object from the room. CAUSES ERROR
                    synchronized (this.room) {
                        this.room.removeObject(this);
                    }
                    break;
                    
                case RANGED_WEAPON:

                    /* create a different weapon, depending on the timeline */
                    if (LevelState.worldIdx == 0) {

                        /* Old timeline -> bow */
                        RangedWeapon rw = ItemCreator.newBow();
                        //add weapon to inventory
                        //System.err.println("BEFORE: \n" + this.room.getPlayer().inventory.toString());
                        this.room.getPlayer().inventory.add(rw);
                        
                        /* Selects the weapon */
                        this.room.getPlayer().getInventory().select(rw.getKey());
                        
                    } else {

                        /* New timeline -> gun */
                        RangedWeapon rw = ItemCreator.newGun();
                        //add weapon to inventory
                        //System.err.println("BEFORE: \n" + this.room.getPlayer().inventory.toString());
                        this.room.getPlayer().inventory.add(rw);
                        
                        /* Selects the weapon */
                        this.room.getPlayer().getInventory().select(rw.getKey());
                    }
                    
                    //delete object from the room. CAUSES ERROR
                    synchronized (this.room) {
                        this.room.removeObject(this);
                    }
                    break;

                case MAGICAL_RELIC:

                    //create generic relic
                    MagicalRelic mr = ItemCreator.newMagicalRelic();

                    //add relic to inventory
                    //System.err.println("BEFORE: \n" + this.room.getPlayer().inventory.toString());
                    this.room.getPlayer().inventory.add(mr);
                    //System.err.println("Added magical relic");
                    //System.err.println("AFTER: \n" + this.room.getPlayer().inventory.toString());
                    //Add buffs from magical Relic to the Player.
                    //mr.applyBuff();

                    synchronized (this.room) {
                        this.room.removeObject(this);
                    }
                    break;

                case MAGIC_WEAPON:
                    
                    //create magical weapon
                    MagicalWeapon magical = ItemCreator.newMagicalWeapon();

                    /* Adds the new weapon to the inventory */
                    this.room.getPlayer().inventory.add(magical);
                    
                    /* Selects the weapon */
                    this.room.getPlayer().getInventory().select(magical.getKey());

                    synchronized (this.room) {
                        this.room.removeObject(this);
                    }
                    
                    break;

                default:
                    System.err.println("CestObject.performAction(): Type of "
                            + "Object not implemented in ChestObject.");
            }
        }
    }
}
