package items;

import java.awt.image.BufferedImage;

import character.Character;

public class HealthPotion extends Item{
	
	public HealthPotion(BufferedImage image) {
		super(image);
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
