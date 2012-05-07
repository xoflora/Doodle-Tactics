package items;

import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public class Cuirass extends Equipment{
	
	public Cuirass(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
		_isCuirass = true;
	}


	int _defense;
	BufferedImage _menuImage;
	
	
	@Override
	public void exert(Character c) {
		c.changeCuirass(this);
	}
	
	public void setDefense(int def) {
		_defense = def;
	}


	public int getDefense() {
		return _defense;
	}
}
