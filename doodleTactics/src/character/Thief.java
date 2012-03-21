package character;

public class Thief extends Character{
	public Thief(){
		_name = "Thief";
		
		//set base stats
		_BASE_STATS[STRENGTH] = 6;
		_BASE_STATS[DEFENSE] = 6;
		_BASE_STATS[SPECIAL] = 5;
		_BASE_STATS[RESISTANCE] = 6;
		_BASE_STATS[SPEED] = 10;
		_BASE_STATS[SKILL] = 7;
		_BASE_STATS[LUCK] = 9;
		_BASE_STATS[MAX_HP] = 13;
	}
}
