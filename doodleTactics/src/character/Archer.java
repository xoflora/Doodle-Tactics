package character;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;

import event.InvalidEventException;

import main.DoodleTactics;

public class Archer extends Character{



	public Archer(DoodleTactics dt, JPanel container, String profile, String left, String right, String up, String down, String name,double x, double y){
		super(dt,container, profile, left, right, up, down, name,x,y);
		_BASE_STATS[STRENGTH] = 7;
		_BASE_STATS[DEFENSE] = 5;
		_BASE_STATS[SPECIAL] = 5;
		_BASE_STATS[RESISTANCE] = 6;
		_BASE_STATS[SPEED] = 9;
		_BASE_STATS[ACCURACY] = 9;
		_BASE_STATS[LUCK] = 3;
		_BASE_STATS[MAX_HP] = 14;
		initStats();
	}
}
