package items;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import character.Character;
import character.Character.CharacterType;

import main.DoodleTactics;

public class Staff extends Weapon implements Serializable{

	public Staff(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}

	public boolean canBeEquipped(Character character) {
		if (character.getCharacterType() == CharacterType.MAGE || character.getCharacterType() == CharacterType.GENERAL) {
			return true;
		}
		else return false;
	}

	public WeaponType getWeaponType() {
		return WeaponType.STAFF;
	}
}
