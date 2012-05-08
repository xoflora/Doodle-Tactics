package character;

import controller.SpecialAttackController;
import main.DoodleTactics;
import main.GameScreen;

public class Warrior extends Character{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Warrior(DoodleTactics dt, GameScreen container,  String profile, String left, String right, String up, String down, String name, double x, double y){
		super(dt,container, profile, left, right, up, down, name,x,y);
		
		//set base stats
		_BASE_STATS[STRENGTH] = .8;
		_BASE_STATS[DEFENSE] = .6;
		_BASE_STATS[SPECIAL] = .5;
		_BASE_STATS[RESISTANCE] = .5;
		_BASE_STATS[SPEED] = .6;
		_BASE_STATS[SKILL] = .7;
		_BASE_STATS[LUCK] = .3;
		_BASE_STATS[MAX_HP] = 1.5;
		initStats(1);
	}
	
	public Warrior(DoodleTactics dt, GameScreen container,  String profile, String left, String right, String up, String down, String name, double x, double y, int level){
		super(dt,container, profile, left, right, up, down, name,x,y);
		
		//set base stats
		_BASE_STATS[STRENGTH] = .8;
		_BASE_STATS[DEFENSE] = .6;
		_BASE_STATS[SPECIAL] = .5;
		_BASE_STATS[RESISTANCE] = .5;
		_BASE_STATS[SPEED] = .6;
		_BASE_STATS[SKILL] = .7;
		_BASE_STATS[LUCK] = .3;
		_BASE_STATS[MAX_HP] = 1.5;
		initStats(level);
	}


	@Override
	public CharacterType getCharacterType() {
		// TODO Auto-generated method stub
		return CharacterType.WARRIOR;
	}
	
	@Override
	public boolean hasSpecial() {
		return false;
	}

	@Override
	public SpecialAttackController getSpecialAttack(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxSpecialRange() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinSpecialRange() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
