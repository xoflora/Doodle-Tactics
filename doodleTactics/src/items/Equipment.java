package items;

import character.Character;

public abstract class Equipment extends Item{
	/**
	 *  Removes this Item from a Character's inventory 
	 * @param c - the Character
	 * @throws ItemException if Item was not on the Character
	 */
	public void revert(Character c) throws ItemException{
			c.removeFromInventory(this);
	}

}
