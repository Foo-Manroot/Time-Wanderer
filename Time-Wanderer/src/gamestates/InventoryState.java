package gamestates;

import controller.KeyboardMouseController;
import entities.Player;
import items.Item;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import main.MainClass;
import org.newdawn.slick.Color;
import utils.NumberUtils;

/**
 * @author Alberto */
public class InventoryState extends BasicGameState {
    public static final int MAXIMUM_ITEMS = 16;
    private final int id;
    private int ix;
    private int iy;
    private int six;
    private int siy;
    private int index;
    private final StateBasedGame game;
    private Player player;
    private Image inventoryImage;
    private Image selectedItem;
    private Image enteredItem;
    private Image meleeWeapon;
    private Image rangedWeapon;
    private Image magicalRelic;
    private Image magicalWeapon;
    private Image consumableItem;
    private static KeyboardMouseController controller;
    private boolean mousePressed;
    private int doNothing = 0;
    private ArrayList<Item> items;

    public InventoryState(int id, StateBasedGame game) {
        this.id = id;
        this.game = game;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {

        /* Updates the current state's id */
        MainClass.currState = (LevelState.worldIdx == 0)?
                               MainClass.INVENTORY_STATE_P1_ID :
                               MainClass.INVENTORY_STATE_P2_ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) {
        try {
            inventoryImage = new Image("resources/ui/inventory/Inventory.png");
            enteredItem = new Image("resources/ui/inventory/EnteredItem.png");
            selectedItem = new Image("resources/ui/inventory/SelectedItem.png");
            meleeWeapon = new Image("resources/ui/inventory/Sword.png");

            /* World index == 0 -> old timeline -> bow
               World index == 1 -> new timeline -> gun */
            if (LevelState.worldIdx == 0) {

                rangedWeapon = new Image("resources/ui/inventory/Bow.png");
            } else {

                rangedWeapon = new Image("resources/ui/inventory/Gun.png");
            }

            magicalRelic = new Image("resources/ui/inventory/Relic.png");
            magicalWeapon = new Image("resources/ui/inventory/Staff.png");
            consumableItem = new Image("resources/ui/inventory/Consumable.png");
        } catch (Exception ex) {
            System.out.println("The game reminds you to have a wonderful day and to love yourself <3");
        }

            ix = (MainClass.WINDOW_WIDTH / 2)
                    - (inventoryImage.getWidth() / 2);
            iy = (MainClass.WINDOW_HEIGHT / 2)
                    - (inventoryImage.getHeight() / 2);
            six = ix + 21;
            siy = iy + 12;
            index = 0;
            items = new ArrayList<>();
            player = MainClass.players [LevelState.worldIdx];

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
        throws SlickException {
        Iterator<Item> iterator = player.getInventory().getIterator();
        Item item;
        int ind = 0;

        g.setColor(Color.white);
        g.drawImage(inventoryImage, ix, iy);

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                if (iterator.hasNext()) {
                    item = iterator.next();
                    ind++;

                    switch (item.getClass().getSimpleName()) {
                        case "MeleeWeapon":
                            g.drawImage(
                                meleeWeapon,
                                ix + 21 + (i % 4) * 78,
                                iy + 12 * (j % 4 + 1) + (j % 4) * 66
                            );
                            break;
                        case "MagicalRelic":
                            g.drawImage(
                                magicalRelic,
                                ix + 21 + (i % 4) * 78,
                                iy + 12 * (j % 4 + 1) + (j % 4) * 66
                            );
                            break;
                        case "MagicalWeapon":
                            g.drawImage(
                                magicalWeapon,
                                ix + 21 + (i % 4) * 78,
                                iy + 12 * (j % 4 + 1) + (j % 4) * 66
                            );
                            break;
                        case "RangedWeapon":
                            g.drawImage(
                                rangedWeapon,
                                ix + 21 + (i % 4) * 78,
                                iy + 12 * (j % 4 + 1) + (j % 4) * 66
                            );
                            break;
                        case "ConsumableItem":
                            g.drawImage(
                                consumableItem,
                                ix + 21 + (i % 4) * 78,
                                iy + 12 * (j % 4 + 1) + (j % 4) * 66
                            );
                            break;
                    }

                } else {
                    i = 4;
                    break;
                }

            }

        }

        if (index < items.size()) {
            g.drawString(items.get(index).toString(), 10, 100);

            if (mousePressed) {
                player.getInventory().select(items.get(index).getKey());
            }

        }

        if (mousePressed) {
            g.drawImage(selectedItem, six, siy);
            doNothing = 10;
            mousePressed = false;
        } else {
            g.drawImage(enteredItem, six, siy);
        }

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
        throws SlickException {
        int x = Mouse.getX();
        int y = NumberUtils.invertOrdinate(Mouse.getY());
        player = MainClass.players [LevelState.worldIdx];
        Iterator<Item> iterator = player.getInventory().getIterator();

        if (doNothing > 0) {
            doNothing--;
            return;
        }

        items.clear();

        while (iterator.hasNext())
            items.add(iterator.next());

        mousePressed = Mouse.isButtonDown(0);

        if (controller == null) {
            MainClass.changeState(MainClass.LEVEL_STATE_ID);
        } else {
            controller.handleInput(container.getInput(), delta);
        }

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                if ((x >= (ix + 21 + (i % 4) * 78))
                    && (x <= (ix + 21 + (i % 4) * 78 + 66))
                    && (y >= (iy + 12 * (j % 4 + 1) + (j % 4) * 66))
                    && (y <= (iy + 12 * (j % 4 + 1) + (j % 4) * 66 + 66))) {
                    six = ix + 21 + (i % 4) * 78;
                    siy = iy + 12 * (j % 4 + 1) + (j % 4) * 66;
                    index = i * 4 + j;
                }

            }

        }

    }

    public static void addController(KeyboardMouseController controller) {

        if ((controller != null) && (InventoryState.controller == null)) {
            InventoryState.controller = controller;
        }

    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
