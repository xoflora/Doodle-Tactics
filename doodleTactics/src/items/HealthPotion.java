package items;

import java.awt.image.BufferedImage;

import character.Character;

public class HealthPotion extends Item{
	
	int _HPupdate;
	
	public HealthPotion(BufferedImage image, String name, int HPupdate) {
		super(image, name);
		_HPupdate = HPupdate;
	}

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
