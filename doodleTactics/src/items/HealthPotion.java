package items;

import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public class HealthPotion extends Item{
	
	int _HPupdate;
	
	
	public HealthPotion(DoodleTactics dt, String imagePath, String name, int HPupdate) {
		super(dt,imagePath, name);
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
