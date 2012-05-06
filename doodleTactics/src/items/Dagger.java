package items;

import java.awt.image.BufferedImage;

import character.Character;
import character.Character.CharacterType;

import main.DoodleTactics;


public class Dagger extends Weapon {

	public Dagger(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}

	@Override
	public boolean canBeEquipped(Character character) {
		if (character.getChararacterType() == CharacterType.THIEF) {
			return true;
		}
		else return false;
	}

//	public WeaponType getWeaponType() {
//		return WeaponType.DAGGER;
//	}
	
}
