package items;

import entities.GameCharacter;
import entities.Stats;

/**
 *
 * @author Pablo Peña
 */
public class ConsumableItem extends Item {

    /**Character using Potion*/
    private GameCharacter charac;
    /**Health OR Mana Restored*/
    private int recuperation = 0;
    
    /**Indicates what kind of consumable item this is*/
    //private Catalog type;
    
    
    /**Default contructor. Although you do not specify anything, it creates
     a health potion.*/
    public ConsumableItem(String description, String name, String key,
            GameCharacter characterUsingItem, int recovery) {

        super(Catalog.HEALTH_POTION, description, name, true);
        this.charac = characterUsingItem;
        this.recuperation = recovery;
    }
    
    /**Custom contructor. You have to specify which type of potion this is
     * with a Catalog object. If this Catalog object is not a Helath Potion,
     * nor a Mana one, the constructor will create a Helath Potion */
    public ConsumableItem(String description, String name, String key,
            GameCharacter characterUsingItem, int recovery, Catalog type) {
        super(type, description, name, true);
        if(this.kind!=Catalog.HEALTH_POTION && this.kind!=Catalog.MANA_POTION){
            this.kind=Catalog.HEALTH_POTION;
        }
        this.charac = characterUsingItem;
        this.recuperation=recovery;
    }
 

    @Override
    public void use() {
        if (this.getStock() > 0) {
            
            Stats st = this.charac.getStats();
            
            //Recover mana or helath depending on the kind of the potion.
            if(this.kind==Catalog.HEALTH_POTION){
                st.fillLife(this.recuperation);
            }
            else if(this.kind==Catalog.MANA_POTION){
                st.fillMana(this.recuperation);
            }
            
            this.charac.setStats(st);
            this.decreaseStock(1);
        }
    }

}
