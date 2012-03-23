package character;

public class Mage extends Character{
	public Mage(String name){
		super(name);
		
		//set base stats
		_BASE_STATS[STRENGTH] = 4;
		_BASE_STATS[DEFENSE] = 5;
		_BASE_STATS[SPECIAL] = 9;
		_BASE_STATS[RESISTANCE] = 8;
		_BASE_STATS[SPEED] = 7;
		_BASE_STATS[SKILL] = 5;
		_BASE_STATS[LUCK] = 5;
		_BASE_STATS[MAX_HP] = 12;
		initStats();

	}
}
