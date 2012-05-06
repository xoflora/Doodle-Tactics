package character;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import character.Character.CharacterType;

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
	
	private CharacterType _currType = CharacterType.GENERAL;
	
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
	
	public void setCharacterType(CharacterType type) {
		_currType = type;
	}

	@Override
	public CharacterType getChararacterType() {
		// TODO Auto-generated method stub
		return _currType;
	}

}
