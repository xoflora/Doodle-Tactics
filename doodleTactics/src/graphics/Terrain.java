package graphics;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.DoodleTactics;
import map.Tile;

public class Terrain extends Rectangle{

	private transient BufferedImage _img;
	protected transient DoodleTactics _dt;
	String _imgPath;
	
	public Terrain(DoodleTactics dt, JPanel container, String path,double x, double y) {
		super(container);
		_dt = dt;
		_imgPath = path;
		parseImage();
		this.setSize(_img.getWidth(), _img.getHeight());
		this.setVisible(true);
		int overflow = 0;
		if(_img.getWidth() - Tile.TILE_SIZE <= 25.0)
			overflow = (_img.getWidth() - Tile.TILE_SIZE) / 2;
		setLocation(x - overflow, y - _img.getHeight() + Tile.TILE_SIZE);
	}
	
	/**
	 * accessor for the image 
	 * @return the image associated with this Terrain
	 */
	public BufferedImage getImage() {
		return _img;
	}
	
	/**
	 * Load image
	 */
	public void load(DoodleTactics dt){
		_dt = dt;
		parseImage();
	}
	
	/**
	 * parses an image
	 */
	public void parseImage(){
		_img = _dt.importImage(_imgPath);
	}
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		this.setPaintPriority((int) y + _img.getHeight());
	}
}
