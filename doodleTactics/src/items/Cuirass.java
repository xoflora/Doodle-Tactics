package items;

import java.awt.image.BufferedImage;

import character.Character;

public class Cuirass extends Equipment{
	
	public Cuirass(BufferedImage image) {
		super(image);
	}


	int _defense;
	BufferedImage _menuImage;
	
	
	@Override
	public void exert(Character c) {
		c.changeCuirass(this);
	}


	public int getDefense() {
		return _defense;
	}
}
