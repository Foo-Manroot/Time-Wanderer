package items;

/**
 *
 * @author Miguel García Martín
 */
public class MagicalWeapon extends Item {
    
    /**
     * Damage that the effects produced by this weapon will make.
     */
    private final int attack;

    /**
     * Constructor.
     * 
     * @param attack
     *              Attack for the effects created with this weapon
     */
    public MagicalWeapon (int attack) {
        
        super(Catalog.MAGIC_WEAPON, null, null, false);
        this.attack = attack;
    }
    
    /**
     * Constructor.
     * 
     * @param name 
     *              The name to identify this item (not unique).
     * @param key 
     *              The key to index this weapon on the inventory.
     * @param attack
     *              Attack for the effects created with this weapon
     */
    public MagicalWeapon (String name, String key, int attack) {
        
        super(Catalog.MAGIC_WEAPON, name, key, false);
        this.attack = attack;
    }

    /**
     * NOT USED.
     */
    @Override
    public void use() {
    }
    
    /**
     * Returns the damage that the effects created will make to the enemies.
     * 
     * @return 
     *          The value of {@code attack}
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
