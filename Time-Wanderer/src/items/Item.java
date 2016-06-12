/* This class represents an item which could be taken, saved, wasted or equipped
 * by the character. There are different kinds of item and all of them are
 * specified at Catalog. */
package items;

import org.newdawn.slick.Graphics;

/**
 * @author Alberto
 * @version 24/03/2016 */
public abstract class Item {
    /**
     * The kind of the item. */
    protected Catalog kind;
    /**
     * A description of the item. */
    protected String description;
    /**
     * The name of the item. */
    protected final String name;
    /**
     * The key that identifies the item. */
    protected final String key;
    /**
     * Indicates if the item is consumable when it is used. */
    private final boolean consumable;
    /**
     * Indicates if the item is consumable when it is used. */
    private int stock;

    /**
     * @param kind - the kind of the item (see Catalog).
     * @param name - the name of the item.
     * @param key - the key of the item.
     * @param consumable - if the item is consumable or not. */
    protected Item(Catalog kind, String name, String key, boolean consumable) {
        this.kind = kind;
        this.description = "no description";
        this.name = name == null ? "no name" : name;
        this.key = key == null ? "no key" : key;
        this.consumable = consumable;
        stock = consumable ? 1 : 0;
    }

    /**
     * @param kind - the kind of the item (see Catalog).
     * @param name - the name of the item.
     * @param key - the key of the item.
     * @param stock - the number of copies of the item (if it is consumable). */
    protected Item(Catalog kind, String name, String key, int stock) {
        this.kind = kind;
        this.description = "no description";
        this.name = name == null ? "no name" : name;
        this.key = key == null ? "no key" : key;
        consumable = stock > 0;
        this.stock = consumable ? stock : 0;
    }

    /**
     * @param kind - the kind of the item (see Catalog).
     * @param description - a description of the item.
     * @param name - the name of the item.
     * @param key - the key of the item.
     * @param consumable - if the item is consumable or not. */
    protected Item(Catalog kind, String description, String name, String key,
        boolean consumable) {
        this.kind = kind;
        this.description = description == null ? "no description" : description;
        this.name = name == null ? "no name" : name;
        this.key = key == null ? "no key" : key;
        this.consumable = consumable;
        stock = consumable ? 1 : 0;
    }

    /**
     * @param kind - the kind of the item (see Catalog).
     * @param description - a description of the item.
     * @param name - the name of the item.
     * @param key - the key of the item.
     * @param stock - the number of copies of the item (if it is consumable). */
    protected Item(Catalog kind, String description, String name, String key,
        int stock) {
        this.kind = kind;
        this.description = description == null ? "no description" : description;
        this.name = name == null ? "no name" : name;
        this.key = key == null ? "no key" : key;
        consumable = stock > 0;
        this.stock = consumable ? stock : 0;
    }

    /**
     * @return The kind of the item. */
    public Catalog getKind() {
        return kind;
    }

    /**
     * @return The description of the item. */
    public String getDescription() {
        return description;
    }

    /**
     * @return The name of the item. */
    public String getName() {
        return name;
    }

    /**
     * @return The key of the item. */
    public String getKey() {
        return key;
    }

    /**
     * @return A description of the item properties. */
    @Override
    public String toString() {
        String info = "";

        info += "NAME: " + name + "\n";
        info += "KIND: " + kind + "\n";
        info += "KEY: " + key + "\n";
        info += "CONSUMABLE: " + (consumable? "yes" : "no") + "\n";
        info += "STOCK: " + stock + "\n";
        info += "DESCRIPTION: " + description;

        return info;
    }

    /**
     * @return True if the item is consumable. Otherwise, false is returned. */
    public boolean isConsumable() {
        return consumable;
    }

    /**
     * @return The number of copies of the item. */
    public int getStock() {
        return stock;
    }

    /**
     * @param decrement - an amount to deduct from the stock of the item. */
    public void decreaseStock(int decrement) {

        if (consumable && (decrement <= stock)) {
            stock -= decrement;
        }

    }

    /**
     * @param increment - an amount to add to the stock of the item. */
    public void increaseStock(int increment) {

        if (consumable) {
            stock += increment;
        }

    }

    /**
     * @param description - the new description of the item. */
    public void setDescription(String description) {

        if (description != null) {
            this.description = description;
        }

    }

    /**
     * @param stock - the new stock of the item. */
    public void setStock(int stock) {

        if (consumable && (stock >= 0)) {
            this.stock = stock;
        }

    }

    /**
     * The main method of every item. It does something associated with each
     * item nature when invoked. */
    public abstract void use();
    
    /**
     * Overridable method for the child classes to override (if needed).
     * 
     * @param g
     *          Graphics where the shapes will be drawn.
     */
    public void render (Graphics g) {}
}
