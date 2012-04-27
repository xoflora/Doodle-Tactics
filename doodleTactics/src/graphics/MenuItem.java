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
	
	private static final int DEFAULT_PRIORITY = 0;

	private BufferedImage _default;
	private BufferedImage _hovered;
	private BufferedImage _current;
	protected DoodleTactics _dt;
	
	public MenuItem(JPanel container, BufferedImage defltPath, BufferedImage hoveredPath, DoodleTactics dt) {
		super(container, DEFAULT_PRIORITY);
		_dt = dt;
		_default = defltPath;
		_hovered = hoveredPath;
		_current = _default;
		this.setDefault();
		this.setSize(_current.getWidth(), _current.getHeight());
	}
	
	public MenuItem(JPanel container, BufferedImage defltPath, BufferedImage hoveredPath, DoodleTactics dt,
			int paintPriority) {
		super(container, paintPriority);
		_dt = dt;
		_default = defltPath;
		_hovered = hoveredPath;
		_current = _default;
		this.setDefault();
		this.setSize(_current.getWidth(), _current.getHeight());
	}
	
	/**
	 * accessor for the current image 
	 * @return the current image of the menu
	 */
	public BufferedImage getImage() {
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
