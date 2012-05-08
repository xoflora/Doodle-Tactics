package character;

import controller.SpecialAttackController;
import controller.SplatterTimer;
import main.DoodleTactics;
import main.GameOverScreen;
import main.GameScreen;

public class MainCharacter extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient DoodleTactics _dt;
	
	private int _tileX, _tileY;
	
	private CharacterType _currType = CharacterType.GENERAL;
	
	public MainCharacter(DoodleTactics dt, GameScreen container, String profile, String left, String right, String up, String down, String name, int x, int y){
		super(dt,container, profile, left, right, up, down, name, x , y);
		_tileX = x;
		_tileY = y;
		_dt = dt;
		_BASE_STATS[STRENGTH] = .4;
		_BASE_STATS[DEFENSE] = .4;
		_BASE_STATS[SPECIAL] = .4;
		_BASE_STATS[RESISTANCE] = .4;
		_BASE_STATS[SPEED] = .4;
		_BASE_STATS[SKILL] = .4;
		_BASE_STATS[LUCK] = .4;
		_BASE_STATS[MAX_HP] = 1;
		initStats(1);
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
	public CharacterType getCharacterType() {
		// TODO Auto-generated method stub
		return _currType;
	}

	@Override
	public void load(DoodleTactics dt) {
		super.load(dt);
		_dt = dt;
	}
	
	@Override
	public boolean hasSpecial() {
		return true;
	}

	@Override
	public SpecialAttackController getSpecialAttack(int x, int y) {
		SpecialAttackController toReturn = new SpecialAttackController(_dt);
		toReturn.setSpecialTimer(new SplatterTimer(toReturn, _dt, x, y));
		return toReturn;
	}

	@Override
	public int getMaxSpecialRange() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public int getMinSpecialRange() {
		// TODO Auto-generated method stub
		return 5;
	}
}
