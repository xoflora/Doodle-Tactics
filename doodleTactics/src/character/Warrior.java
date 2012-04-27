package character;

import javax.swing.JPanel;

import main.DoodleTactics;

public class Warrior extends Character{

	public Warrior(JPanel container,  String profile, String left, String right, String up, String down, String name, double x, double y){
		super(container, profile, left, right, up, down, name,x,y);
		
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
