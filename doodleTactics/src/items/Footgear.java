package items;


import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public class Footgear extends Equipment {
	
	int _speedEffect;
	
	public Footgear(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
		_isFootgear = true;
	}

	public int getSpeed() {
		return _speedEffect;
	}
	
	public void setSpeed(int speed) {
		_speedEffect = speed;
	}
	
	@Override
	public void exert(Character c) {
		c.changeFootgear(this);
	}
}
