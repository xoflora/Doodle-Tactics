package items;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Staff extends Weapon implements Serializable{

	public Staff(BufferedImage image, String name) {
		super(image, name);
	}

}
