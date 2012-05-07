package items;

import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public abstract class Equipment extends Item{
	
	public final static int STRENGTH = 0;
	public final static int DEFENSE = 1;
	public final static int SPECIAL = 2;
	public final static int RESISTANCE = 3;
	public final static int SPEED = 4;
	public final static int SKILL = 5;
	public final static int LUCK =  6;
	public final static int MAX_HP =  7;
	public final static int NUM_STATS = 8;
	protected final int[] _BASE_STATS;
	
	public Equipment(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
		_BASE_STATS = new int[NUM_STATS];
		_isEquip = true;
	}
	
//	public abstract void setStats();

	
//	public abstract void exert(Character c);
	
//	public abstract void revert(Character c);
	
	/**
	 *  Removes this Item from a Character's inventory 
	 * @param c - the Character
	 * @throws ItemException if Item was not on the Character
	 */
//	public void revert(Character c) throws ItemException{
//			c.removeFromInventory(this);
//	}
}
