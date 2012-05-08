package items;

import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public class Shield extends Equipment{
	
	int _defense;
	
	public Shield(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
		_isShield = true;
	}


	public void exert(Character c) {
		c.changeShield(this);
	}
	
	public void setDefense(int def) {
		_defense = def;
	}

	public int getDefense() {
		return _defense;
	}
}
