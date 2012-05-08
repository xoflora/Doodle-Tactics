package controller.combatController;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import main.DoodleTactics;
import map.Tile;
import character.Character;

public class RandomBattleAI extends CombatController implements Runnable {
	
	public static final int RANDOM_BATTLE_NUM_UNITS = 3;
	MenuItem _aiPhase;
	public RandomBattleAI(DoodleTactics dt, HashMap<Character, Tile> random) {
		super(dt, random);
		BufferedImage img = dt.importImage("src/graphics/menu/enemy_phase.png");
		_aiPhase = new MenuItem(_dt.getGameScreen(),img,img,_dt,-10);
		_aiPhase.setLocation(1050,20);
		_aiPhase.setVisible(true);
		_dt.getGameScreen().addMenuItem(_aiPhase);

	}
	
	public void run() {
		_gameScreen.popControl();
	}
	
	@Override
	public void release() {
		super.release();
		//Temporary, while AI Phase does nothing
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}

		new Thread(new SlideTimer(_aiPhase,-1050)).start();
	}

	@Override
	public void take() {
		super.take();
		//Temporary, while AI Phase does nothing
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}

		System.out.println("Enemy phase");
		_aiPhase.setLocation(1050,_aiPhase.getY());
		new Thread(new SlideTimer(_aiPhase,250)).start();
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
