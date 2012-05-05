package items;

import java.awt.image.BufferedImage;

import main.DoodleTactics;

import character.Character;

public abstract class Equipment extends Item{
	
	public Equipment(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}

	/**
	 *  Removes this Item from a Character's inventory 
	 * @param c - the Character
	 * @throws ItemException if Item was not on the Character
	 */
	public void revert(Character c) throws ItemException{
			c.removeFromInventory(this);
	}

}
