package items;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import main.DoodleTactics;

public class Staff extends Weapon implements Serializable{

	public Staff(DoodleTactics dt, String imagePath, String name) {
		super(dt,imagePath, name);
	}

}
