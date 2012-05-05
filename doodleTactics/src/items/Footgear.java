package items;


import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public class Footgear extends Equipment {
	
	public Footgear(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}

	int _speedEffect;

	@Override
	public void exert(Character c) {
		c.changeFootgear(this);
	}
}
