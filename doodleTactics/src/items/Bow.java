package items;

import java.awt.image.BufferedImage;

import character.Character;
import character.Character.CharacterType;

import main.DoodleTactics;

public class Bow extends Weapon{

	public Bow(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}
//
//	public WeaponType getWeaponType() {
//		return WeaponType.BOW;
//	}

	@Override
	public boolean canBeEquipped(Character character) {
		if (character.getCharacterType() == CharacterType.ARCHER || character.getCharacterType() == CharacterType.GENERAL) {
			return true;
		}
		else return false;
	}

	public WeaponType getWeaponType() {
		return WeaponType.BOW;
	}


}
