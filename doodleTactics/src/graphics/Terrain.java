package graphics;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.DoodleTactics;
import map.Tile;

public class Terrain extends Rectangle{

	private BufferedImage _img;
	protected DoodleTactics _dt;
	
	public Terrain(JPanel container, BufferedImage img,double x, double y) {
		super(container);
		_img = img;
		this.setSize(_img.getWidth(), _img.getHeight());
		this.setVisible(true);
		int overflow = 0;
		if(_img.getWidth() - Tile.TILE_SIZE <= 25.0)
			overflow = (_img.getWidth() - Tile.TILE_SIZE) / 2;
		System.out.println("X LOC: " + x + " Y LOC: " + y); 
		setLocation(x - overflow, y - _img.getHeight() + Tile.TILE_SIZE);
	}
	
	/**
	 * accessor for the image 
	 * @return the image associated with this Terrain
	 */
	public BufferedImage getImage() {
		return _img;
	}
	
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		this.setPaintPriority((int) y + _img.getHeight());
	}
}
