package character;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.GameOverScreen;

@SuppressWarnings("serial")
public class MainCharacter extends Character{
	
	private DoodleTactics _dt;
	
	int _tileX, _tileY;
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
	
	//special, options to choose special ability, set name, special statistics
	//unique options for first level
	
	@Override
	public void setDefeated() {
		_dt.changeScreens(new GameOverScreen(_dt));
	}
}
