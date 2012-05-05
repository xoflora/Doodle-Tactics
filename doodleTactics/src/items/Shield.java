package items;

import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public class Shield extends Equipment{
	
	public Shield(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}


	/**
	 * 
	 */
	int _defense;

	@Override
	public void exert(Character c) {
		c.changeShield(this);
	}

	public int getDefense() {
		return _defense;
	}
}
