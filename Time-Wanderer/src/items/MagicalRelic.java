package items;

import entities.GameCharacter;

/**
 *
 * @author Miguel García Martín
 */
public class MagicalRelic extends Item {

    /*Mana consumed when using the relic.*/
    private final int manaConsumed;
    /*Time elapsed between the uses of this Magical Relic.*/
    private int cooldown;
    /*Time when the relic was last used.*/
    private long lastTimeUsed;
    /*Character using relic*/
    private GameCharacter charac;
    /**Buff offered by the MagicalRelic*/
    private MagicalRelicBuff buff;
    
    /**Default contructor*/
    public MagicalRelic(String description, String name, String key,
            int manaConsumed, GameCharacter characterUsingRelic) {

        super(Catalog.MAGICAL_RELIC,description, name, key, false);
        this.manaConsumed = manaConsumed;
        this.charac = characterUsingRelic;
        this.buff = new MagicalRelicBuff(this.charac);
        
        //Apply buffs to player
        //this.buff.applyBuffs();
    }
    
    /**Default contructor*/
    public MagicalRelic(String description, String name, String key,
            int manaConsumed, GameCharacter characterUsingRelic,
            MagicalRelicBuff b) {

        super(Catalog.MAGICAL_RELIC,description, name, key, false);
        this.manaConsumed = manaConsumed;
        this.charac = characterUsingRelic;
        this.buff = b;
        
        //Apply buffs to player
        //this.buff.applyBuffs();
    }

    @Override
    public void use() {
        if((System.currentTimeMillis() - this.lastTimeUsed)/1000 >= cooldown ) {
            this.charac.getStats().useMana(manaConsumed);
            /*Effects to be added*/
        }
    }
    
    public void applyBuff(){
        this.buff.applyBuffs();
    }
    
    public void removeBuff(){
        this.buff.removeBuffs();
    }
/*
    
    long tStart = System.currentTimeMillis();
    the time passes...
    long tEnd = System.currentTimeMillis();
    long tDelta = tEnd - tStart;
    double elapsedSeconds = tDelta / 1000.0;
*/
    
    /**
     * @return A description of the item properties. */
    @Override
    public String toString() {
        String info = "";

        info += "NAME: " + name + "\n";
        info += "KIND: " + kind + "\n";
        info += "KEY: " + key + "\n";
        info += "DESCRIPTION: " + description + "\n";
        info += "BUFF: " + buff + "\n";

        return info;
    }
}
