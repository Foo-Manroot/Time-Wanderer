package entities;

import levels.Room;
import main.MainClass;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import sound.Playlist;

public class LevelButton extends InteractiveObject {

    private Room room1;
    private Room room2;
    /**
     * Direction of there the blockade is located, in comparison to room 1.
     */
    private int direction;
    private boolean interactedWith = false;

    public LevelButton(float x, float y, int width, int height, Room room1, Room room2, int direction) {
        super(x, y, width, height,"./src/resources/items/Button.png");
        this.room1 = room1;
        this.room2 = room2;
        this.direction = direction;
    }

    @Override
    public void performAction() {
        //System.out.println("HELLO SOMEONE HAS INTERACTED WITH A BUTTON!");
        if (!interactedWith) {
            interactedWith = true;
            this.advanceAnimation();
            MainClass.JUKEBOX.play(Playlist.FALLING_RUBBLE, true, 12);
            switch (direction) {
                case 0: // UP
                    room1.setUpBlockade(false);
                    room2.setDownBlockade(false);
                    break;
                case 1: // DOWN
                    room1.setDownBlockade(false);
                    room2.setUpBlockade(false);
                    break;
                case 2: // LEFT
                    room1.setLeftBlockade(false);
                    room2.setRightBlockade(false);
                    break;
                case 3: // RIGHT
                    room1.setRightBlockade(false);
                    room2.setLeftBlockade(false);
                    break;
                default:
                    break;
            }
        }
    }
    
    
//    @Override
//    public void render(Graphics g) {
//        Color previousColor = g.getColor();
//        if(interactedWith) {
//            g.setColor(Color.red);
//        } else {
//            g.setColor(Color.cyan);
//        }
//        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
//        g.setColor(previousColor);
//        g.drawAnimation(anim, this.getX(), this.getY());
//    }

}
