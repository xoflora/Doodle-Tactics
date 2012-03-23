package items;

import character.Character;

public class HealthPotion extends Item{
	
	@Override
	public void exert(Character c) {
		c.updateHP(10);
		try{
			c.removeFromInventory(this);
		} catch(ItemException e){
			//nothing to do here
		}
	}

}
