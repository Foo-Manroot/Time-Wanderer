package entities;

/**
 *
 * @author Pablo Peña
 */
public class Stats {

    /**
     * max life points
     */
    private int maxLifePoints;
    /**
     * life points
     */
    private int lifePoints;
    /**
     * mana points
     */
    private int maxManaPoints;
    /**
     * mana points
     */
    private int manaPoints;
    /**
     * Physical damage
     */
    private int attackDamage;
    /**
     * Magical damage
     */
    private int magicalDamage;
    /**
     * Physical defense
     */
    private int armor;
    /**
     * magical resistance
     */
    private int magicalResistance;
    /**
     * Level of the character
     */
    private int level;
    /**
     * Current experience
     */
    private int currentXp;
    /**
     * xp needed to rise level
     */
    private int xpToRiseLv;
    /**
     * xp given when this character is killed
     */
    private int xpGiven;
    /**
     * Amount of time that invulnerability is applied after getting hit
     */
    private float invulnerabilityTime;

//-----------------------------CONSTRUCTOR--------------------------------------
    /**
     * Create the stats of a game character. If no parameters, set values to
     * default
     */
    public Stats() {
        maxLifePoints = 300;
        lifePoints = maxLifePoints;
        maxManaPoints = 10;
        manaPoints = maxManaPoints;
        attackDamage = 10;
        magicalDamage = 0;
        armor = 15;
        magicalResistance = 10;
        level = 1;
        invulnerabilityTime = 0.5f;//in seconds
        currentXp = 0;
        /*xpToRiseLv = 50;
         It is incorrect to do this, we will call the method
         calculateNeededXp() instead to calculate the XP we
         need to rise a level.*/
        this.calculateNeededXp();
        xpGiven = 10;
    }

    /**
     * Create the stats of a game character
     *
     * @param hp max life points of this character.
     * @param mana max mana of this character.
     * @param ad physical damage of this character.
     * @param ap magical damage of this character.
     * @param armor armor of this character.
     * @param mr magical resistance of this character.
     * @param lv level of this character.
     * @param invTime invulnerability time of this character.
     * @param xp experience of this character.
     * @param xp2Rise experience to rise the level of this character.
     * @param xpGiven experience given by this character when it is killed
     */
    public Stats(int hp, int mana, int ad, int ap, int armor, int mr, int lv,
            float invTime, int xp, int xp2Rise, int xpGiven) {
        this.maxLifePoints = hp;
        this.lifePoints = this.maxLifePoints;
        this.maxManaPoints = mana;
        this.manaPoints = this.maxManaPoints;
        this.attackDamage = ad;
        this.magicalDamage = ap;
        this.armor = armor;
        this.magicalResistance = mr;
        this.level = lv;
        this.invulnerabilityTime = invTime;
        this.currentXp = xp;
        /*xpToRiseLv = xp2Rise;
         It is incorrect to do this, we will call the method
         calculateNeededXp() instead to calculate the XP we
         need to rise a level.*/
        this.calculateNeededXp();
        this.xpGiven = xpGiven;
    }

//*****************************CONSTRUCTOR**************************************   
//-----------------------------STANDARD-GETTERS&SETTERS-------------------------
    public int getLifePoints() {
        return lifePoints;
    }

    public int getMaxLifePoints() {
        return maxLifePoints;
    }

    public void setMaxLifePoints(int maxLifePoints) {
        this.maxLifePoints = maxLifePoints;
    }

    public int getMaxManaPoints() {
        return maxManaPoints;
    }

    public void setMaxManaPoints(int maxManaPoints) {
        this.maxManaPoints = maxManaPoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public int getManaPoints() {
        return manaPoints;
    }

    public void setManaPoints(int manaPoints) {
        this.manaPoints = manaPoints;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getMagicalDamage() {
        return magicalDamage;
    }

    public void setMagicalDamage(int magicalDamage) {
        this.magicalDamage = magicalDamage;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getMagicalResistance() {
        return magicalResistance;
    }

    public void setMagicalResistance(int magicalResistance) {
        this.magicalResistance = magicalResistance;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public void setCurrentXp(int currentXp) {
        this.currentXp = currentXp;
    }

    public int getXpToRiseLv() {
        return xpToRiseLv;
    }

    public void setXpToRiseLv(int xpToRiseLv) {
        this.xpToRiseLv = xpToRiseLv;
    }

    public int getXpGiven() {
        return xpGiven;
    }

    /**
     * Adds the given value to the current experience points. If the xp needed
     * to level up are reached, increases the necessary amount of levels.
     *
     * @param addValue The quantity that has to be added to the XP.
     */
    public void addXp(int addValue) {

        currentXp += addValue;

        /* Checks if the level must be rised */
        checkLevelUp();
    }

    public void setXpGiven(int xpGiven) {
        this.xpGiven = xpGiven;
    }

    public float getInvulnerabilityTime() {
        return invulnerabilityTime;
    }

    public void setInvulnerabilityTime(float invulnerabilityTime) {
        this.invulnerabilityTime = invulnerabilityTime;
    }
//*****************************STANDARD-GETTERS&SETTERS*************************
//-----------------------------SPECIAL-GETTERS&SETTERS--------------------------

    /**
     * Decreases the life points of the entity the specified amount.
     *
     * @param dmg amount of points to be decreased.
     */
    public void decreaseLife(int dmg) {

        this.lifePoints -= dmg;
        if (this.lifePoints < 0) {
            this.lifePoints = 0;
        }
    }

    /**
     * Decreases the mana points of the entity the specified amount. This method
     * is synchronized because it can be called concurrently by the applyRegen()
     * method inside MagicalRelicBuff.java.
     *
     * @param manaCost amount of points to be decreased.
     */
    public synchronized void useMana(int manaCost) {
        if (this.manaPoints >= manaCost) {
            this.manaPoints -= manaCost;
        } else {
            System.out.println("Not enough mana (Stats.java:221)");
        }
    }

    /**
     * Set life of the entity at a custom amount. This method is synchronized
     * because it can be called concurrently by the applyRegen() method inside
     * MagicalRelicBuff.java.
     *
     * @param hp2Rise quantity of hp to refill.
     */
    public synchronized void fillLife(int hp2Rise) {
        if (hp2Rise <= 0) {
            //System.out.println("Not a valid amount of hp");
        } else if (this.lifePoints + hp2Rise >= this.maxLifePoints) {
            this.lifePoints = this.maxLifePoints;
        } else if (hp2Rise > 0 && this.lifePoints + hp2Rise < this.maxLifePoints) {
            this.lifePoints += hp2Rise;
        }
    }

    /**
     * Set life of the entity at a custom amount. This method is synchronized
     * because it can be called concurrently by the applyRegen() method inside
     * MagicalRelicBuff.java.
     *
     * @param mana2Rise quantity of mana to refill.
     */
    public synchronized void fillMana(int mana2Rise) {
        if (mana2Rise <= 0) {
            //System.out.println("Not a valid amount of mana");
        } else if (this.manaPoints + mana2Rise >= this.maxManaPoints) {
            this.manaPoints = this.maxManaPoints;
        } else if (mana2Rise > 0 && this.manaPoints + mana2Rise < this.maxManaPoints) {
            this.manaPoints += mana2Rise;
        }
    }

    /**
     * Set life of the entity at its maximum This method is synchronized because
     * it can be called concurrently by the applyRegen() method inside
     * MagicalRelicBuff.java.
     */
    public synchronized void fillAllLife() {
        this.lifePoints = this.maxLifePoints;
    }

    /**
     * Set mana of the entity at its maximum This method is synchronized because
     * it can be called concurrently by the applyRegen() method inside
     * MagicalRelicBuff.java.
     */
    public synchronized void fillAllMana() {
        this.manaPoints = this.maxManaPoints;
    }
//*****************************SPECIAL-GETTERS&SETTERS**************************
//-----------------------------OTHER-METHODS------------------------------------

    /**
     * This method must be used AFTER you have Risen a level, to know how many
     * xp points you will need to get to the next one
     */
    private void calculateNeededXp() {
        if (this.level < 99) {
            this.xpToRiseLv = (int) ((Math.pow(this.level, 3) * (100 - this.level)) / 50);
        }
    }

    /**
     * Check if the entity should rise a level
     */
    public void checkLevelUp() {

        /*If we Have enough Experience to rise a level, we
         increment the level of the entity and then recalculate the needed
         experience for the next level*/
        while (this.currentXp >= this.xpToRiseLv && this.level < 99) {
            //Rise level
            this.riseLevel();
        }

    }

    /**
     * Method to make an entity rise a level. Its stats will increase. The
     * amount of expereince needed to rise to the next level is automatically calculated.
     */
    private void riseLevel() {
        //level
        this.level++;
        //health & mana
        this.maxLifePoints += 10;
        this.maxManaPoints += 5;
        this.fillAllLife();
        this.fillAllMana();
        //atacks
        this.attackDamage += 5;
        this.magicalDamage += 2;
        //defenses
        this.armor += 5;
        this.magicalResistance += 3;
        //update xp needed to rise another level.
        this.calculateNeededXp();

    }

    @Override
    public String toString() {
        return  "\n_________________\n"
                + "Hp:" + this.lifePoints
                + "\nMax HP :"+this.maxLifePoints
                + "\nMana:" + this.manaPoints
                + "\nMax Mana:"+this.maxManaPoints
                + "\nLevel:" + this.level
                + "\nExp:" + this.currentXp
                + "\nNext Lv:" + this.xpToRiseLv
                + "\nAttack:" +this.attackDamage
                + "\nMagical Attack:" + this.magicalDamage
                + "\nArmor:" + this.armor
                + "\nMagical Resistance:" +this.magicalResistance;
                

    }
//*****************************OTHER-METHODS************************************    
}
