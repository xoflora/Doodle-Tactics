package items;

import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public class Cuirass extends Equipment{
	
	private int _defense;
	private int _resistance;
	private transient BufferedImage _menuImage;
	private String _imgPath;
	
	public Cuirass(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
		_isCuirass = true;
	}


	
	
	@Override
	public void exert(Character c) {
		c.changeCuirass(this);
	}
	
	public void setDefense(int def) {
		_defense = def;
	}
	
	public void setResistance(int resistance) {
		_resistance = resistance;
	}
	
	public int getDefense() {
		return _defense;
	}
	
	public int getResistance() {
		return _resistance;
	}
	
	public void load(DoodleTactics dt){
		_menuImage = dt.importImage(_imgPath);
	}
}
