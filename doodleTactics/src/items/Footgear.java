package items;


import java.awt.image.BufferedImage;

import character.Character;

public class Footgear extends Equipment {
	
	public Footgear(BufferedImage image, String name) {
		super(image, name);
	}

	int _speedEffect;

	@Override
	public void exert(Character c) {
		c.changeFootgear(this);
	}
}
