package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.DoodleTactics;

/**
 * models an item in the menu
 * @author jeshapir
 *
 */

public class MenuItem extends Rectangle {

	private BufferedImage _default;
	private BufferedImage _hovered;
	private BufferedImage _current;
	protected DoodleTactics _dt;
	
	public MenuItem(JPanel container, String defltPath, String hoveredPath, DoodleTactics dt) {
		super(container);
		try {
			_dt = dt;
			_default = ImageIO.read(new File(defltPath));
			_hovered = ImageIO.read(new File(hoveredPath));
		} catch(IOException e) {
			System.out.println("Bad file path!");
		}
		
		this.setDefault();
		this.setSize(_current.getWidth(), _current.getHeight());
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
	
	public void activate() {
		
	}

}
