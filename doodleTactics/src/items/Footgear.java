package items;


import character.Character;

public class Footgear extends Equipment {
	int _speedEffect;

	@Override
	public void exert(Character c) {
		c.changeFootgear(this);
	}
}
