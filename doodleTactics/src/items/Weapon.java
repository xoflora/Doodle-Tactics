package items;

import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public abstract class Weapon extends Equipment{
	
	private String _name;
	private int _minAttackRange;
	private int _maxAttackRange;
	private double _accuracy;
	private int _attackPower;
	
	public Weapon(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}
	
	BufferedImage _displayImage;
	BufferedImage _menuImage;
	
	public void exert(Character c){
		c.changeWeapon(this);
	}
	
	public int getMinAttackRange() {
		return _minAttackRange;
	}
	
	public int getMaxAttackRange() {
		return _maxAttackRange;
	}

	public int getPower() {
		return _attackPower;
	}

	public double getAccuracy() {
		return _accuracy;
	}
}
