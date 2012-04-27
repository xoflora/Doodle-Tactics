package character;

import javax.swing.JPanel;

public class Thief extends Character{
	 

	public Thief(JPanel container, String profile, String left, String right, String up, String down, String name, double x, double y){
		super(container, profile, left, right, up, down, name,x, y);
		
		//set base stats
		_BASE_STATS[STRENGTH] = 6;
		_BASE_STATS[DEFENSE] = 6;
		_BASE_STATS[SPECIAL] = 5;
		_BASE_STATS[RESISTANCE] = 6;
		_BASE_STATS[SPEED] = 10;
		_BASE_STATS[SKILL] = 7;
		_BASE_STATS[LUCK] = 9;
		_BASE_STATS[MAX_HP] = 13;
		initStats();
	}
}
