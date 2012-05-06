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
		if (character.getChararacterType() == CharacterType.MAGE) {
			return true;
		}
		else return false;
	}

//	@Override
//	public WeaponType getWeaponType() {
//		// TODO Auto-generated method stub
//		return WeaponType.STAFF;
//	}

}
