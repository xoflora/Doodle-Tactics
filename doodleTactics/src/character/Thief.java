package character;

import controller.SpecialAttackController;
import main.DoodleTactics;
import main.GameScreen;

public class Thief extends Character{
	 

	public Thief(DoodleTactics dt, GameScreen container, String profile, String left, String right, String up, String down, String name, double x, double y){
		super(dt,container, profile, left, right, up, down, name,x, y);
		
		//set base stats
		_BASE_STATS[STRENGTH] = .6;
		_BASE_STATS[DEFENSE] = .6;
		_BASE_STATS[SPECIAL] = .5;
		_BASE_STATS[RESISTANCE] = .6;
		_BASE_STATS[SPEED] = 1.0;
		_BASE_STATS[SKILL] = .7;
		_BASE_STATS[LUCK] = .9;
		_BASE_STATS[MAX_HP] = 1.3;
		initStats();
	}

	@Override
	public CharacterType getCharacterType() {
		// TODO Auto-generated method stub
		return CharacterType.THIEF;
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
