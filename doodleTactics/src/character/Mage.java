package character;

import main.DoodleTactics;
import main.GameScreen;

public class Mage extends Character{
	
	public Mage(DoodleTactics dt, GameScreen container, String profile, String left, String right, String up, String down, String name,double x, double y){
		super(dt,container, profile, left, right, up, down, name,x,y);
		
		//set base stats
		_BASE_STATS[STRENGTH] = .4;
		_BASE_STATS[DEFENSE] = .5;
		_BASE_STATS[SPECIAL] = .9;
		_BASE_STATS[RESISTANCE] = .8;
		_BASE_STATS[SPEED] = .7;
		_BASE_STATS[SKILL] = .5;
		_BASE_STATS[LUCK] = .5;
		_BASE_STATS[MAX_HP] = 1.2;
		initStats();

	}

	public CharacterType getCharacterType() {
		return CharacterType.MAGE;
	}
}
