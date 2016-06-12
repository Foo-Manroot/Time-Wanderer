/* This class represents a backpack which contains all the items the character
 * has collected and he/she can use. */
package items;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Alberto
 * @version 24/03/2016
 */
public class Inventory {

    /**
     * The collected items. It may be selected four of them max: a melee weapon,
     * a ranged weapon, a magical relic and a consumable object.
     */
    private final HashMap<String, Item> inventory;
    /**
     * The selected consumable item: potions, arrows, crystals... whatever.
     */
    private ConsumableItem consumableItem;
    /**
     * The selected magical relic. Its use will give buffs.
     */
    private MagicalRelic magicalRelic;
    /**
     * The selected magical weapon. Its use will waste mana points.
     */
    private MagicalWeapon magicalWeapon;
    /**
     * The selected melee weapon. Its use won't consume mana points, but a
     * little cold down must be supported.
     */
    private MeleeWeapon meleeWeapon;
    /**
     * The selected ranged weapon. Its use will consume munition.
     */
    private RangedWeapon rangedWeapon;

    /**
     * @param capacity - the number of items that can be collected in the
     * inventory.
     */
    public Inventory(int capacity) {
        inventory = new HashMap<>(capacity);
    }

    /**
     * @return The selected consumable item.
     */
    public ConsumableItem getConsumableItem() {
        return consumableItem;
    }

    /**
     * @param key - the key of the item to fetch from the inventory.
     * @return The value whose key has been passed on success and null on
     * failure.
     */
    public Item get(String key) {
        return inventory.get(key);
    }

    /**
     * @param key - the key of the item to remove from the inventory.
     * @return The value whose key has been passed on success and null on
     * failure.
     */
    public Item remove(String key) {
        return inventory.remove(key);
    }

    /**
     * @return The selected magical relic.
     */
    public MagicalRelic getMagicalRelic() {
        return magicalRelic;
    }

    /**
     * @return The selected magical relic.
     */
    public MagicalWeapon getMagicalWeapon() {
        return magicalWeapon;
    }

    /**
     * @return The selected melee weapon.
     */
    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    /**
     * @return The selected ranged weapon.
     */
    public RangedWeapon getRangedWeapon() {
        return rangedWeapon;
    }

    public Iterator<Item> getIterator() {
        return inventory.values().iterator();
    }

    public int getSize() {
        return inventory.size();
    }

    /**
     * @param item - the new item (which can't be null or classless) to add to
     * the inventory.
     */
    public void add(Item item) {

        if (item == null) {
            return;
        }

        if (item.isConsumable()) {

            if (inventory.containsKey(item.getKey())) {
                inventory.get(item.getKey()).increaseStock(item.getStock());
            } else {

                if ((consumableItem != null)
                    && consumableItem.getKey().equals(item.getKey())) {
                    consumableItem.increaseStock(item.getStock());
                } else {
                    inventory.put(item.getKey(), item);
                }

            }

        } else {
            inventory.put(item.getKey(), item);
        }

    }

    /**
     * @param key - the key of the item which the player wants to select.
     */
    public void select(String key) {
        Item selectedItem = inventory.remove(key);

        if (selectedItem != null) {

            if (selectedItem.isConsumable()) {
                if (consumableItem != null) {
                    inventory.put(consumableItem.getKey(), consumableItem);
                }
                consumableItem = (ConsumableItem) selectedItem;
            } else {

                switch (selectedItem.getKind()) {
                    case MAGICAL_RELIC:

                        if (magicalRelic != null) {
                            /*Equipped Magical Relic is taken from the
                             equipped items to the inventory.*/
                            /*In this case, we remove the buffs from the relic
                             taken into the inventory.*/
                            //Remove buffs from MagicalRelic
                            magicalRelic.removeBuff();
                            //Take current equipped Relic to inventory
                            inventory.put(magicalRelic.getKey(), magicalRelic);

                        }
                        /*Now we take a MAgical Relic from the inventory
                         to the selected items. Then we apply the buffs
                         and then we move it tothe selected items*/
                        //Move new MagicalRelic to the selected items.
                        magicalRelic = (MagicalRelic) selectedItem;
                        //apply new Buffs.
                        magicalRelic.applyBuff();
                        break;
                    case MAGIC_WEAPON:

                        if (magicalWeapon != null) {
                            inventory.put(magicalWeapon.getKey(),
                                magicalWeapon);
                        }

                        magicalWeapon = (MagicalWeapon) selectedItem;
                        break;
                    case MELEE_WEAPON:

                        if (meleeWeapon != null) {
                            inventory.put(meleeWeapon.getKey(), meleeWeapon);
                        }

                        meleeWeapon = (MeleeWeapon) selectedItem;
                        break;
                    case RANGED_WEAPON:

                        if (rangedWeapon != null) {
                            inventory.put(rangedWeapon.getKey(), rangedWeapon);
                        }

                        rangedWeapon = (RangedWeapon) selectedItem;
                }

            }

        }

    }
    
    /**
     * Removes the selected item. When removed, the item will come back to the
     * "bag". If the item should be removed completely from the inventory, the 
     * parameter {@code delete} must be <i>true</i>
     * 
     * @param item 
     *              Item to be removed.
     * @param delete 
     *              If this parameter is <i>true</i>, the item will be removed.
     *          If not, the item will be added to the bag.
     */
    public void removeSelected (Item item, boolean delete) {
        
        /* Sets to "null" the correct item */
        switch (item.getKind()) {
            
            case HEALTH_POTION:
            case MANA_POTION:
                /* Consumable item */
                consumableItem = null;
                break;
                
            case MAGICAL_RELIC:
                /* Magical relic */
                magicalRelic = null;
                break;
                
            case MAGIC_WEAPON:
                /* Magical weapon (TO BE ADDED) */
                break;
                
            case MELEE_WEAPON:
                /* Melee weapon */
                meleeWeapon = null;
                break;
                
            case RANGED_WEAPON:
                /* ranged weapon */
                rangedWeapon = null;
                break;
                
            default:
                System.out.println("Error at Inventory.removeSelected()."
                        + " Unknown item kind: \"" + item.getKind() + "\"");
        }
        
        /* If the item doesn't have to be removed, but restored to the "bag",
        adds it to the list */
        if (!delete) {
            
            inventory.put(item.getKey(), item);
        }
    }

    /**
     * Customized toString()() method.
     */
    @Override
    public String toString() {
        String keySet[] = new String[this.inventory.size()];
        this.inventory.keySet().toArray(keySet);

        String aux = "";
        for (int i = 0; i < keySet.length; i++) {
            aux += this.inventory.get(keySet[i]).toString() + "\n---------------\n";
        }
        return aux;
    }

}
