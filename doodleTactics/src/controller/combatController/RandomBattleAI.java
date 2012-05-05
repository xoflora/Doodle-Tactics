package controller.combatController;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import main.DoodleTactics;
import map.Tile;
import character.Character;

public class RandomBattleAI extends CombatController implements Runnable {
	
	public static final int RANDOM_BATTLE_NUM_UNITS = 3;

	public RandomBattleAI(DoodleTactics dt, HashMap<Character, Tile> random) {
		super(dt, random);
	}
	
	public void run() {
		_gameScreen.popControl();
	}
	
	@Override
	public void release() {
		super.release();
	}

	@Override
	public void take() {
		super.take();
		System.out.println("Enemy phase");
		new Thread(this).start();
	}
	
	@Override
	public void removeUnit(Character c) {
		super.removeUnit(c);
		System.out.println("CRUCNH");
		_gameScreen.getMap().removeRandomBattle(c);
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) { }

	@Override
	public void mouseMoved(MouseEvent e) { }

}
