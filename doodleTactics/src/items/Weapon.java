package items;

import java.awt.image.BufferedImage;
import character.Character;
public abstract class Weapon extends Equipment{
	String _name;
	int _attackRange;
	double _accuracy;
	int _attackPower;
	
	BufferedImage _displayImage;
	BufferedImage _menuImage;
	
	public void exert(Character c){
		c.changeWeapon(this);
	}
}
