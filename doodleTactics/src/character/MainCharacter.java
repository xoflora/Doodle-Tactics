package character;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.GameOverScreen;
import map.Tile;

public class MainCharacter extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient DoodleTactics _dt;
	
	private int _tileX, _tileY;
	public MainCharacter(DoodleTactics dt, JPanel container, String profile, String left, String right, String up, String down, String name, int x, int y){
		super(dt,container, profile, left, right, up, down, name, x , y);
		_tileX = x;
		_tileY = y;
		_dt = dt;
	}
	
	public int getTileX(){
		return _tileX;
	}
	
	public int getTileY(){
		return _tileY;
	}
	
	public void setTileX(int x) {
		_tileX = x;
	}
	
	public void setTileY(int y) {
		_tileY = y;
	}
	
	//special, options to choose special ability, set name, special statistics
	//unique options for first level
	
	@Override
	public void setDefeated() {
		_dt.changeScreens(new GameOverScreen(_dt));
	}
	
	@Override
	public void paint(java.awt.Graphics2D brush, BufferedImage img) {
		super.paint(brush,img);
		System.out.println("THE WORST x: " + this.getX() + ", " + "y: " + this.getY());
	}
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		System.out.println("Setting location of main character to x: " + x + ", y:" + y);
		System.out.println("Location is, x: " + this.getX() + ", " + "y: " + this.getY());
	}
}
