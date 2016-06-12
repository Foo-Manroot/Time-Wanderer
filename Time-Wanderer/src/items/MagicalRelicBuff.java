/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import entities.GameCharacter;
import entities.Stats;

/**
 * The instances of this class will always belong to a MagicalRelic object. This
 * class will be used to make the entity using a magical relic suffer the
 * effects of it.
 *
 * @author Pablo Peña
 */
public class MagicalRelicBuff {
    /*Game Character that uses the MagicalRelic and will suffer the Buffs.*/

    private GameCharacter relicOwner;
    /*max life points buff.*/
    private int maxLifePointsBuff;
    /*life points regen.*/
    private int lifePointsRegen;
    /*max mana points buff*/
    private int maxManaPointsBuff;
    /*mana points regen.*/
    private int manaPointsRegen;
    /*Physical damage buff.*/
    private int attackDamageBuff;
    /*Magical damage buff.*/
    private int magicalDamageBuff;
    /*Physical defense buff.*/
    private int armorBuff;
    /*magical resistance buff.*/
    private int magicalResistanceBuff;
    /*Variable used to stop the regen Thread*/
    private boolean stopRegen;
    
    /**Custom constructor. You have to specify the boost of every stat.*/
    public MagicalRelicBuff(GameCharacter relicOwner,
            int maxLifePointsBuff,
            int lifePointsRegen,
            int maxManaPointsBuff,
            int manaPointsRegen,
            int attackDamageBuff,
            int magicalDamageBuff,
            int armorBuff,
            int magicalResistanceBuff) {
        this.relicOwner = relicOwner;
        this.maxLifePointsBuff = maxLifePointsBuff;
        this.lifePointsRegen = lifePointsRegen;
        this.maxManaPointsBuff = maxManaPointsBuff;
        this.manaPointsRegen = manaPointsRegen;
        this.attackDamageBuff = attackDamageBuff;
        this.magicalDamageBuff = magicalDamageBuff;
        this.armorBuff = armorBuff;
        this.magicalResistanceBuff = magicalResistanceBuff;
        this.stopRegen = false;
    }
    
    /**Simple constructor*/
    public MagicalRelicBuff(GameCharacter relicOwner) {
        this.relicOwner = relicOwner;
        this.maxLifePointsBuff = 10;
        this.lifePointsRegen = 10;
        this.maxManaPointsBuff = 10;
        this.manaPointsRegen = 10;
        this.attackDamageBuff = 10;
        this.magicalDamageBuff = 10;
        this.armorBuff = 10;
        this.magicalResistanceBuff = 10;
        this.stopRegen = false;
    }

    /**
     * Apply the buffs to the Game Character. Used when equipping the magical
     * Relic
     */
    public void applyBuffs() {
        Stats improvedStats = relicOwner.getStats();

        improvedStats.setMaxLifePoints(improvedStats.getMaxLifePoints()
                + maxLifePointsBuff);
        improvedStats.setMaxManaPoints(improvedStats.getMaxManaPoints()
                + maxManaPointsBuff);
        improvedStats.setAttackDamage(improvedStats.getAttackDamage()
                + attackDamageBuff);
        improvedStats.setMagicalDamage(improvedStats.getMagicalDamage()
                + magicalDamageBuff);
        improvedStats.setArmor(improvedStats.getArmor()
                + armorBuff);
        improvedStats.setMagicalResistance(improvedStats.getMagicalResistance()
                + magicalResistanceBuff);
        
        //Refill all lief and mana after applying buffs.
        improvedStats.fillAllLife();
        improvedStats.fillAllMana();
    }

    /**
     * Remove the buffs from the Game Character. Used when equipping the magical
     * Relic
     */
    public void removeBuffs() {
        Stats improvedStats = relicOwner.getStats();

        improvedStats.setMaxLifePoints(improvedStats.getMaxLifePoints()
                - maxLifePointsBuff);
        improvedStats.setMaxManaPoints(improvedStats.getMaxManaPoints()
                - maxManaPointsBuff);
        improvedStats.setAttackDamage(improvedStats.getAttackDamage()
                - attackDamageBuff);
        improvedStats.setMagicalDamage(improvedStats.getMagicalDamage()
                - magicalDamageBuff);
        improvedStats.setArmor(improvedStats.getArmor()
                - armorBuff);
        improvedStats.setMagicalResistance(improvedStats.getMagicalResistance()
                - magicalResistanceBuff);
    }

    /**
     * If this relic applies mana or hp regen, apply it to the GameCharacter
     */
    public void applyRegen() {
        //Create Task
        Runnable regenTask = new Runnable() {

            @Override
            public void run() {
                while (!stopRegen) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println(ex);
                    }
                    relicOwner.getStats().fillLife(lifePointsRegen);
                    relicOwner.getStats().fillMana(manaPointsRegen);
                }
            }
        };
        //Create a Thread with the Task and start it.
        Thread regenThread = new Thread(regenTask);
        regenThread.start();

    }
    
    /**Stop Regeneration. It is very important to use it.
     * If not, several methods could happen inside the applyRegen() method
     when destroying the entity*/
    public void stopRegen(){
        this.stopRegen=true;
    }
    
    /**
     * @return A description of the item properties. */
    @Override
    public String toString() {
        String info = "";

        info += "MAX HP BOOST: " + maxLifePointsBuff + "\n";
        info += "MAX MANA BOOST: " + maxManaPointsBuff + "\n";
        info += "ATTACK BOOST: " + attackDamageBuff + "\n";
        info += "MAGICAL ATTACK BOOST: " + magicalDamageBuff + "\n";
        info += "ARMOR BOOST: " + armorBuff + "\n";
        info += "MAGICAL RESISTANCE BOOST: " + magicalResistanceBuff + "\n";

        return info;
    }
}
