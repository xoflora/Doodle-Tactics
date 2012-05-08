package graphics;

import java.awt.RenderingHints;
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

	private boolean _highQuality;
	protected BufferedImage _default;
	protected BufferedImage _hovered;
	protected BufferedImage _current;
	protected DoodleTactics _dt;

	
	public MenuItem(JPanel container, BufferedImage defltPath, BufferedImage hoveredPath, DoodleTactics dt) {
		super(container, DEFAULT_PRIORITY);
		_dt = dt;
		_default = defltPath;
		_hovered = hoveredPath;
		_current = _default;
		_highQuality = true;
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
	
	public void setHighQuality(boolean quality) {
		_highQuality = quality;
	}
	
	@Override
	public void paint(java.awt.Graphics2D brush, BufferedImage img) {
		if(_highQuality) {
			brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			super.paint(brush, img);
			brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		} else {
			super.paint(brush,img);
		}
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
	
	public void activate(int type) {
		
	}

}
