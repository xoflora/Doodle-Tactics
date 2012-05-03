package items;

import java.awt.image.BufferedImage;

import character.Character;

public class Shield extends Equipment{
	
	public Shield(BufferedImage image, String name) {
		super(image, name);
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
