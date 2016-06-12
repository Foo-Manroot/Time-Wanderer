package items;

/**
 *
 * @author Miguel García Martín
 */
public class RangedWeapon extends Item {
    
    /**
     * If this attribute is true, the weapon is a gun. If not, is a bow.
     */
    private final boolean isGun;
    
    /**
     * Attack for the projectiles fired with this weapon.
     */
    private final int attack;
    
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */
    
    /**
     * Constructor.
     * 
     * @param isGun 
     *              If this parameter is true, the weapon is a gun. If not,
     *          is a bow.
     * @param attack
     *              Attack for the projectiles fired with this weapon.
     */
    public RangedWeapon(boolean isGun, int attack) {
        
        super(Catalog.RANGED_WEAPON, null, null, false);
        
        this.isGun = isGun;
        this.attack = attack;
    }
    
    /**
     * Constructor.
     * 
     * @param name 
     *              The name to identify this item (not unique).
     * @param key 
     *              The key to index this weapon on the inventory.
     * @param isGun 
     *              If this parameter is true, the weapon is a gun. If not,
     *          is a bow.
     * @param attack
     *              Attack for the projectiles fired with this weapon.
     */
    public RangedWeapon(String name, String key, boolean isGun, int attack) {
        
        super(Catalog.RANGED_WEAPON, name, key, false);
        
        this.isGun = isGun;
        this.attack = attack;
    }

    @Override
    public void use() {
    }
    
    /**
     * Returns <i>true</i> is the weapon is a fire gun, <i>false</i> if it's a 
     * bow.
     * @return 
     */
    public boolean isGun () {
        
        return this.isGun;
    }
    
    
    /**
     * Returns the damage that the projectiles created with this weapon will 
     * make to the enemies.
     * 
     * @return 
     *              The value of {@code attack}
     */
    public int getAttack () {
        
        return this.attack;
    }
    
    /**
     * @return A description of the item properties. */
    @Override
    public String toString() {
        String info = "";

        info += "NAME: " + name + "\n";
        info += "KIND: " + kind + "\n";
        info += "KEY: " + key + "\n";
        info += "DAMAGE: " + attack + "\n";
        info += "DESCRIPTION: " + description;

        return info;
    }
}
