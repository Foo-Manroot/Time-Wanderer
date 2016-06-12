package controller;

import entities.Player;
import gamestates.LevelState;
import org.newdawn.slick.Input;
import main.MainClass;
import sound.Playlist;

/**
 * Class that handles the input from the actual player. Will be able to handle
 * both keyboard keystrokes and mouse movement.
 *
 * @author sergio
 */
public class KeyboardMouseController extends PlayerController {

    public KeyboardMouseController(Player player) {
        super(player);
    }

    public void handleInput(Input i, int delta) {
        //handle any input from the keyboard
        handleKeyboardInput(i, delta);
    }

    private void handleKeyboardInput(Input i, int delta) {
        // We can both use the WASD or arrow keys to move around,
        // obviously we can't move both left and right simultaneously
        if (i.isKeyDown(Input.KEY_Q)) {
            
            LevelState.changeTimeline();
            MainClass.currentPlayer = (MainClass.currentPlayer + 1) % 2;
        }
        if (i.isKeyDown(Input.KEY_A) || i.isKeyDown(Input.KEY_LEFT)) {
            
            player.moveLeft(delta); 
            
            /* Plays the sound of a step if it wasn't being played */
            if (!MainClass.JUKEBOX.isPlaying(Playlist.STEP)) {
                
                MainClass.JUKEBOX.play(Playlist.STEP, true, 20);
            }
        } else if (i.isKeyDown(Input.KEY_D) || i.isKeyDown(Input.KEY_RIGHT)) {
            
            player.moveRight(delta);
            
            /* Plays the sound of a step if it wasn't being played */
            if (!MainClass.JUKEBOX.isPlaying(Playlist.STEP)) {
                
                MainClass.JUKEBOX.play(Playlist.STEP, true, 20);
            }
        } else {
            // We dont move if we don't press left or right,
            // this will have the effect that our player decelerates.
            player.setMoving(false);
        }
        if (i.isKeyDown(Input.KEY_W)) {
            if (!player.getIsClimbing()) {
                if (player.getCanClimb()) {
                    player.climb();
                }
            } else {
                player.climbUp();
            }
        }
        if (i.isKeyDown(Input.KEY_S)) { // Step down a platform.
            if (player.isOnGround()) {
                player.setIgnoresPlatforms(true);
            }

            if (player.getIsClimbing()) {
                player.climbDown();
            } else {
                if (player.getCanClimb()) {
                    player.climb();
                }
            }
        }

        if (i.isKeyDown(Input.KEY_SPACE)) {
            
            /* Plays the jumping sound only if the player wasn't jumping */
            if (player.isOnGround()) {
                
                MainClass.JUKEBOX.play(Playlist.JUMP, true, 17);
            }
            
            player.setIgnoresGravity(false);
            player.setIsClimbing(false);
            player.jump();
        }

        if (i.isKeyDown(Input.KEY_E)) {
            player.interact();

        }

        if (i.isKeyDown(Input.KEY_P) || i.isKeyDown(Input.KEY_ESCAPE)) {

            if (MainClass.currentPlayer == 0) {
                MainClass.changeState(MainClass.INVENTORY_STATE_P1_ID);
            } else if (MainClass.currentPlayer == 1) {
                MainClass.changeState(MainClass.INVENTORY_STATE_P2_ID);
            }

        }

        if (i.isKeyDown(Input.KEY_O)) {
            MainClass.changeState(MainClass.LEVEL_STATE_ID);
        }

        /* Melee attack -> left click */
        if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {

            player.meleeAttack();
        }

        /* Ranged attack -> right click */
        if (i.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {

            player.rangedAttack();
        }
        
        /* Magical attack -> middle button */
        if (i.isMousePressed(Input.MOUSE_MIDDLE_BUTTON)) {

            player.magicalAttack();
        }

        //Testing purposes. Shows entire inventory and stats.
        if (i.isKeyPressed(Input.KEY_N)) {
            System.out.println(this.player.getStats().toString()
                    + "\n_-_-_-_-_-\nINVENTORY\n"
                    + this.player.getInventory().toString()
                    + "\n_-_-_-_-_-\nEQUIPPED\n"
                    + "\nMeleeWeapon: "
                    + this.player.getInventory().getMeleeWeapon()
                    + "\nMagicalRelic: "
                    + this.player.getInventory().getMagicalRelic()
                    + "\nRangedWeapon: "
                    + this.player.getInventory().getRangedWeapon()
                    + "\nConsumable: "
                    + this.player.getInventory().getConsumableItem());
        }
        
        /* Uses ConsumableItem equipped (if any). */
        if (i.isKeyPressed(Input.KEY_F)) {
            
            if (this.player.getInventory().getConsumableItem() != null) {
                
                this.player.getInventory().getConsumableItem().use();
                
                /* If all the consumables where used, removes the "selected 
                consumable item" */
                if (this.player.getInventory().getConsumableItem().getStock() <= 0) {
                    
                    this.player.getInventory().removeSelected(
                            player.getInventory().getConsumableItem(), true
                    );
                }
            }
        }
        
        /* If the selected key is "ESC", returns to the main menu */
        if (i.isKeyPressed(Input.KEY_ESCAPE)) {
            
            MainClass.changeState (MainClass.MENU_STATE_ID);
        }
    }

}
