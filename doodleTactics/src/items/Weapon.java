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
	private transient BufferedImage _displayImage;
	private transient BufferedImage _menuImage;

	
	public enum WeaponType {
		AXE, BOW, DAGGER, STAFF;
	}
	
	public Weapon(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
		_isWeapon = true;
	}
	
	
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
	
	public void setStats(int minRange, int maxRange, int power, int accuracy) {
		_minAttackRange = minRange;
		_maxAttackRange = maxRange;
		_attackPower = power;
		_accuracy = accuracy;
	}
	
	public double getAccuracy() {
		return _accuracy;
	}
	
	public void setAsWeapon(boolean bool) {
		_isWeapon = bool;
	}
	
	public abstract boolean canBeEquipped(Character character);
	
	public abstract WeaponType getWeaponType();
	}
