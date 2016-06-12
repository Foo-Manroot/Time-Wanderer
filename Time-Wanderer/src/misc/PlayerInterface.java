/* This class shows on screen parameters like: health points, mana points,
 * chosen melee weapon, chosen magic relic, etc. */
package misc;

import entities.Player;
import items.ConsumableItem;
import items.MagicalRelic;
import items.MagicalWeapon;
import items.MeleeWeapon;
import items.RangedWeapon;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import main.MainClass;

/**
 * @author Alberto Serrano Ibaibarriaga */
public class PlayerInterface {
    private final Player player;
    private final Image imageHPBar;
    private final Image imageMPBar;
    private final Image imageXpBar;
    private final Image meleeWeapon;
    private final Image rangedWeapon;
    private final Image magicalRelic;
    private final Image magicalWeapon;
    private final Image consumableItem;
    private final int barContainer[];
    private int maxHealthPoints;
    private int maxManaPoints;
    private float healthPoints;
    private float manaPoints;
    private float experiencePoints;
    private float level;
    private final int margin1;
    private final int margin2;

    /**
     * @param player - the player which stats and items will be shown.
     * @param barContainer - the dimension of the bar (HP or MP) container.
     * These is not the dimension of the image. It is the ensemble of width and
     * height of the rectangle inside the bar frame. barContainer[0] is the
     * width and barContainer[1] is the height. The method allows just a two
     * cell array.
     * @throws SlickException */
    public PlayerInterface(Player player, int[] barContainer)
        throws SlickException {
        boolean twoCells = barContainer.length == 2;

        try {

            if (player == null) {
                throw new NullPointerException("player mustn't be null!");
            }

        } catch (NullPointerException exception) {
            System.out.println(exception.getMessage());
            System.exit(-1);
        }

        this.player = player;
        imageHPBar = new Image("resources/ui/pifaceimages/HPBar.png");
        imageHPBar.setFilter(Image.FILTER_NEAREST);
        imageMPBar = new Image("resources/ui/pifaceimages/MPBar.png");
        imageMPBar.setFilter(Image.FILTER_NEAREST);
        imageXpBar = new Image("resources/ui/pifaceimages/XpBar.png");
        imageXpBar.setFilter(Image.FILTER_NEAREST);
        meleeWeapon = new Image("resources/ui/pifaceimages/Sword.png");
        meleeWeapon.setFilter(Image.FILTER_NEAREST);
        rangedWeapon = new Image("resources/ui/pifaceimages/Bow.png");
        rangedWeapon.setFilter(Image.FILTER_NEAREST);
        magicalRelic = new Image("resources/ui/pifaceimages/Relic.png");
        magicalRelic.setFilter(Image.FILTER_NEAREST);
        magicalWeapon = new Image("resources/ui/pifaceimages/Staff.png");
        magicalWeapon.setFilter(Image.FILTER_NEAREST);
        consumableItem = new Image("resources/ui/pifaceimages/Consumable.png");
        consumableItem.setFilter(Image.FILTER_NEAREST);
        this.barContainer = twoCells ? barContainer : new int[]{0, 0};
        maxHealthPoints = player.getStats().getMaxLifePoints();
        maxManaPoints = player.getStats().getMaxManaPoints();
        margin1 = 5;
        margin2 = 3;
        healthPoints = maxHealthPoints;
        manaPoints = maxManaPoints;
        experiencePoints = player.getStats().getCurrentXp();
    }

    public void update() {
        int hp = player.getStats().getLifePoints();
        int mp = player.getStats().getManaPoints();
        int xp = player.getStats().getCurrentXp();

        if (hp < 0) {
            hp = 0;
        }

        healthPoints = hp;

        if (mp < 0) {
            mp = 0;
        }

        manaPoints = mp;

        if (xp < 0) {
            xp = 0;
        }

        if ((xp % player.getStats().getXpToRiseLv()) == 0)
            xp = 0;

        
        /* Updates the max experience, health and mana points (when the player
        rises a level, those values change) */
        experiencePoints = (xp % player.getStats().getXpToRiseLv());
        level = player.getStats().getLevel();
        maxHealthPoints = player.getStats().getMaxLifePoints();
        maxManaPoints = player.getStats().getMaxManaPoints();
    }

    public void render(Graphics g) {
        int xpToRLv = player.getStats().getXpToRiseLv();
        float marginToQuit = xpToRLv - experiencePoints;
        float healthWidth = (healthPoints / maxHealthPoints) * barContainer[0];
        float manaWidth = (manaPoints / maxManaPoints) * barContainer[0];
        float experienceWidth = (marginToQuit / xpToRLv) * barContainer[0];
        float experienceOffset = (experiencePoints / xpToRLv) * barContainer[0];
        float imageHPBarX = margin1;
        float imageHPBarY = 4 * margin1;
        float imageMPBarX = margin1;
        float imageMPBarY = 5 * margin1 + imageHPBar.getHeight();
        float imageXpBarX = margin1;
        float imageXpBarY = 6 * margin1 + 2 * imageMPBar.getHeight();
        MeleeWeapon mw = player.getInventory().getMeleeWeapon();
        RangedWeapon rw = player.getInventory().getRangedWeapon();
        MagicalRelic mr = player.getInventory().getMagicalRelic();
        MagicalWeapon maw = player.getInventory().getMagicalWeapon();
        ConsumableItem ci = player.getInventory().getConsumableItem();
        boolean drawSword = mw != null;
        boolean drawBow = rw != null;
        boolean drawStaff = maw != null;
        boolean drawRelic = mr != null;
        boolean drawConsumable = ci != null;
        
        g.scale(1.7f, 1.7f);
        g.drawImage(imageHPBar, imageHPBarX, imageHPBarY);
        g.drawImage(imageMPBar, imageMPBarX, imageMPBarY);
        g.drawImage(imageXpBar, imageXpBarX, imageXpBarY);
        g.setColor(Color.black);
        g.fillRect(imageHPBarX + margin2 + healthWidth, imageHPBarY + margin2,
            barContainer[0] - healthWidth, barContainer[1]);
        g.fillRect(imageMPBarX + margin2 + manaWidth, imageMPBarY + margin2,
            barContainer[0] - manaWidth, barContainer[1]);
        g.fillRect(imageXpBarX + margin2 + experienceOffset,
            imageXpBarY + margin2, experienceWidth, barContainer[1]);
        g.setColor(Color.red);
        g.drawString((int) healthPoints + "/" + maxHealthPoints,
            imageHPBarX + barContainer[0] + margin1, imageHPBarY);
        g.setColor(Color.blue);
        g.drawString((int) manaPoints + "/" + maxManaPoints,
            imageMPBarX + barContainer[0] + margin1, imageMPBarY);
        g.setColor(Color.yellow);
        g.drawString((int) experiencePoints + "/" + xpToRLv + 
                           "\nLvl. " + player.getStats().getLevel(),
            imageXpBarX + barContainer[0] + margin1, imageXpBarY);
        g.setColor(Color.lightGray);

        if (drawSword) {
            g.drawImage(meleeWeapon, (MainClass.WINDOW_WIDTH / 4)
                - meleeWeapon.getWidth(), 1);
        }

        if (drawBow) {
            g.drawImage(rangedWeapon, (MainClass.WINDOW_WIDTH / 4)
                - meleeWeapon.getWidth() - rangedWeapon.getWidth() - 1,
                1);
        }

        if (drawStaff) {
            g.drawImage(magicalWeapon, (MainClass.WINDOW_WIDTH / 4) + 2, 1);
        }

        if (drawRelic) {
            g.drawImage(magicalRelic, (MainClass.WINDOW_WIDTH / 4)
                + meleeWeapon.getWidth() + 4, 1);
        }

        if (drawConsumable) {
            g.drawImage(consumableItem, (MainClass.WINDOW_WIDTH / 4)
                + 2 * meleeWeapon.getWidth() + 7, 1);
            g.drawString("" + player.getInventory().getConsumableItem().getStock(),
                MainClass.WINDOW_WIDTH / 4
                + 2 * meleeWeapon.getWidth() + 6,
                1
            );
        }

        g.drawRect((MainClass.WINDOW_WIDTH / 4)
            - meleeWeapon.getWidth() - 1, 0, 18, 18);
        g.drawRect((MainClass.WINDOW_WIDTH / 4)
            - meleeWeapon.getWidth() - rangedWeapon.getWidth() - 3, 0, 18,
            18);
        g.drawRect((MainClass.WINDOW_WIDTH / 4) + 1, 0, 18, 18);
        g.drawRect((MainClass.WINDOW_WIDTH / 4)
            + meleeWeapon.getWidth() + 3, 0, 18, 18);
        g.drawRect((MainClass.WINDOW_WIDTH / 4)
            + 2 * meleeWeapon.getWidth() + 5, 0, 18, 18);
        g.scale(MainClass.SCALE, MainClass.SCALE);
    }

}
