package items;


import character.Character;
import character.Character.CharacterType;
import main.DoodleTactics;


public class Axe extends Weapon {

	public Axe(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}

	@Override
	public boolean canBeEquipped(Character character) {
		// TODO Auto-generated method stub
		if (character.getCharacterType() == CharacterType.WARRIOR || character.getCharacterType() == CharacterType.GENERAL) {
			return true;
		}
		else return false;
	}


	public WeaponType getWeaponType() {
		return WeaponType.AXE;
	}


}
