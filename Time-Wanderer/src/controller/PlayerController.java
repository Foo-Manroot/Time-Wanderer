package controller;

import entities.Player;
import org.newdawn.slick.Input;

/**
 * Abstract class that represents the controls handler of the game.
 * @author sergio
 */
public abstract class PlayerController {
    
    protected Player player;
    
    public PlayerController(Player player){
        this.player = player;
    }
    
    public abstract void handleInput(Input i, int delta);

}
