package graphics;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.DoodleTactics;

public class Terrain extends Rectangle{

	private BufferedImage _img;
	protected DoodleTactics _dt;
	
	public Terrain(JPanel container, BufferedImage img) {
		super(container);
		_img = img;
		this.setSize(_img.getWidth(), _img.getHeight());
		this.setVisible(true);
	}
	
	/**
	 * accessor for the image 
	 * @return the image associated with this Terrain
	 */
	public BufferedImage getImage() {
		return _img;
	}
	
	@Override
	public double getY(){
		return super.getY() - _img.getHeight();
	}
	
	@Override
	public void setLocation(double x, double y){
		super.setLocation(x, y - _img.getHeight());
		this.setPaintPriority((int) y + _img.getHeight());
	}
}
