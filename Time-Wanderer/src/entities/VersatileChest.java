/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import gamestates.LevelState;
import items.Catalog;
import levels.Room;

/**
 *
 * @author propietario
 */
public class VersatileChest extends InteractiveObject {

    /*Room where the chest is located*/
    private Room room;
    private boolean isOpened = false;
    /*Type of the Content of the chest. Will be one of the Catalog*/
    Catalog chestContent;
    /*No space between names so that I can use trim() in constructor.*/
    
    /**Standard constructor*/
    public VersatileChest(float x, float y, int width, int height, Room room,
            Catalog content) {
        super(x, y, width, height, "./src/resources/items/Chest.png");
        this.room = room;
        //Set the sprites of the chest
        this.setAnimation("./src/resources/items/Chest.png");
        
        

        //set content
        this.chestContent = content;
        
    }
    
    /**Constructor that allows us to set the sprites of the chest.*/
    public VersatileChest(float x, float y, int width, int height, Room room,
            Catalog content, String spriteSheetPath) {
        super(x, y, width, height, spriteSheetPath);
        this.room = room;

        //set content
        this.chestContent = content;
    }

    @Override
    public void performAction() {
        
        if (!this.isOpened) {
            //System.err.println("CHEST!");
            String spriteType = "null"; 
            
            switch (chestContent) {
                case HEALTH_POTION:
                    spriteType = "./src/resources/items/RedPotion.png";
                    break;
                case MANA_POTION:
                    spriteType = "./src/resources/items/BluePotion.png";
                    break;
                case MELEE_WEAPON:
                    spriteType = "./src/resources/items/Sword.png";
                    break;
                case MAGIC_WEAPON:
                    spriteType = "./src/resources/items/Staff.png";
                    break;
                case RANGED_WEAPON:
                    /* World index == 0 -> old timeline -> bow
                       World index == 1 -> new timeline -> gun */
                    if (LevelState.worldIdx == 0) {
                        
                        spriteType = "./src/resources/items/Bow.png";
                    } else {
                        
                        spriteType = "./src/resources/items/Gun.png";
                    }
                    break;
                case MAGICAL_RELIC:
                    spriteType = "./src/resources/items/Relic.png";
                    break;
                default:
                    System.err.println("Type of Object to generate"
                            + " not supported by the chest.");
                    break;
            }
            
            //give player content.
            if(!spriteType.equals("null")){
                ChestObject co = new ChestObject(this, spriteType);
            }
            else{
                ChestObject co = new ChestObject(this);
            }

            this.isOpened = true;
            this.advanceAnimation();
        }
    }
    
    /**Returns the room of this chest, used to construct a chestObject and
     give it this room as a parameter.
     * @return Room of this chest.*/
    public Room getRoom(){
        return this.room;
    }

}
