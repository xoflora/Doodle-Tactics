package character;

import javax.swing.JPanel;

public class Warrior extends Character{
	
	public Warrior(JPanel container, String avatar, String profile, String left, String right, String up, String down, String name){
		super(container, avatar, profile, left, right, up, down, name);
		_name = "Warrior";
		
		//set base stats
		_BASE_STATS[STRENGTH] = 8;
		_BASE_STATS[DEFENSE] = 6;
		_BASE_STATS[SPECIAL] = 5;
		_BASE_STATS[RESISTANCE] = 5;
		_BASE_STATS[SPEED] = 6;
		_BASE_STATS[SKILL] = 7;
		_BASE_STATS[LUCK] = 3;
		_BASE_STATS[MAX_HP] = 15;
		initStats();

	}
}
