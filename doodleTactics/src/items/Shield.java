package items;

import character.Character;

public class Shield extends Equipment{
	/**
	 * 
	 */
	int _defense;

	@Override
	public void exert(Character c) {
		c.changeShield(this);
	}
}
