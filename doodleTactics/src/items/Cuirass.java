package items;

import java.awt.image.BufferedImage;

import character.Character;

public class Cuirass extends Equipment{
	int _defense;
	BufferedImage _menuImage;
	
	
	@Override
	public void exert(Character c) {
		c.changeCuirass(this);
	}
}
