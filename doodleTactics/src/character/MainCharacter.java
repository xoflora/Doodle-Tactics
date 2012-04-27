package character;

import javax.swing.JPanel;

import main.DoodleTactics;

@SuppressWarnings("serial")
public class MainCharacter extends Character{
	
	int _tileX, _tileY;
	public MainCharacter(JPanel container, String profile, String left, String right, String up, String down, String name, int x, int y){
		super(container, profile, left, right, up, down, name, x , y);
		_tileX = x;
		_tileY = y;
	}
	
	public int getTileX(){
		return _tileX;
	}
	
	public int getTileY(){
		return _tileY;
	}
	
	//special, options to choose special ability, set name, special statistics
	//unique options for first level
}
