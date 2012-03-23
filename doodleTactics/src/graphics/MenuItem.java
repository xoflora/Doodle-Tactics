package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * models an item in the menu
 * @author jeshapir
 *
 */

public class MenuItem extends Rectangle {

	private BufferedImage _default;
	private BufferedImage _hovered;
	private BufferedImage _current;
	
	public MenuItem(JPanel container, String deflt, String hovered) {
		super(container);
		try {
			_hovered = ImageIO.read(new File(hovered));
			_default = ImageIO.read(new File(deflt));
		} catch(IOException e) {
			System.out.println("Bad file path!");
		}
		
		this.setDefault();
	}
	
	/**
	 * accessor for the current image 
	 * @return the current image of the menu
	 */
	public BufferedImage getCurrentImage() {
		return _current;
	}
	
	public void setDefault() {
		_current = _default;
	}
	
	public void setHovered() {
		_current = _hovered;
	}

}
