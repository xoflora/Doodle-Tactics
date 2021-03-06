package character;

import controller.SpecialAttackController;
import main.DoodleTactics;
import main.GameScreen;

public class Archer extends Character{



	public Archer(DoodleTactics dt, GameScreen container, String profile, String left, String right, String up, String down, String name,double x, double y){
		super(dt,container, profile, left, right, up, down, name,x,y);
		_BASE_STATS[STRENGTH] = .7;
		_BASE_STATS[DEFENSE] = .5;
		_BASE_STATS[SPECIAL] = .5;
		_BASE_STATS[RESISTANCE] = .6;
		_BASE_STATS[SPEED] = .9;
		_BASE_STATS[SKILL] = .9;
		_BASE_STATS[LUCK] = .3;
		_BASE_STATS[MAX_HP] = 1.4;
		initStats(1);
	}
	
	public Archer(DoodleTactics dt, GameScreen container, String profile, String left, String right, String up, String down, String name,double x, double y, int level){
		super(dt,container, profile, left, right, up, down, name,x,y);
		_BASE_STATS[STRENGTH] = .7;
		_BASE_STATS[DEFENSE] = .5;
		_BASE_STATS[SPECIAL] = .5;
		_BASE_STATS[RESISTANCE] = .6;
		_BASE_STATS[SPEED] = .9;
		_BASE_STATS[SKILL] = .9;
		_BASE_STATS[LUCK] = .3;
		_BASE_STATS[MAX_HP] = 1.4;
		initStats(level);

	}


	@Override
	public CharacterType getCharacterType() {
		return CharacterType.ARCHER;
	}
	
	@Override
	public boolean hasSpecial() {
		return false;
	}
	
	@Override
	public SpecialAttackController getSpecialAttack(int x, int y) {
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
