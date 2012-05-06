package controller.combatController.AIController;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import controller.combatController.CombatController;


import main.DoodleTactics;
import map.Tile;
import character.Character;

public class RandomBattleAI extends CombatController implements Runnable {
	
	public static final int RANDOM_BATTLE_NUM_UNITS = 3;
	
	private MenuItem _aiPhase;
	private Action _act;
	private Character _current;
	
	public RandomBattleAI(DoodleTactics dt, HashMap<Character, Tile> random) {
		super(dt, random);
		BufferedImage img = dt.importImage("src/graphics/menu/enemy_phase.png");
		_aiPhase = new MenuItem(_dt.getGameScreen(),img,img,_dt,-10);
		_aiPhase.setLocation(1050,20);
		_aiPhase.setVisible(true);
		_dt.getGameScreen().addMenuItem(_aiPhase);
		
		_act = null;
	}
	
	public void run() {
		
		PriorityQueue<Action> actions;
		
		_current = nextUnit();
		System.out.println("start state" + getState() + " " + _current);
		
		while (_current != null) {
			synchronized(_current) {
				if (_current != null && getState() == State.START) {
					setState(State.CHARACTER_SELECTED);
					actions = new PriorityQueue<Action>();

					for (Tile t : _gameScreen.getMap().getMovementRange(_locations.get(_current),
							_current.getMovementRange())) {
						Action[] possible = {/*new AttackAction(this, toMove, t), new ItemAction(this, toMove, t),*/
								new WaitAction(this, _current, t)};

						actions.add(Collections.max(Arrays.asList(possible)));
					}

					_hasMoved.put(_current, true);
					_act = actions.poll();
					if (_act != null) {
						_gameScreen.panToCoordinate(_current.getX(), _current.getY());
						System.out.println(_act.getValue());
						setState(State.CHARACTER_MOVING);
						move(_current, _locations.get(_current),
								_gameScreen.getMap().getPath(_locations.get(_current), _act.getTile()));
					}
					else {
						setState(State.START);
						_current = nextUnit();
					}
				}
				System.out.println(getState());
			}
		}
		
		System.out.println("End state " + getState());
		
		_gameScreen.popControl();
	}
	
	@Override
	/**
	 * signifies that the character has finished moving along its path
	 */
	public void moveComplete() {
		super.moveComplete();
		
		System.out.println("OWEIURWEOIUR" + getState());
		
		if (_act != null)
			_act.act();
	}
	
	@Override
	
	public void characterWait() {
		if (_current != null)
			synchronized(_current) {
				_current = nextUnit();
			}
		super.characterWait();
		
		System.out.println("Waiting: " + getState() + " " + _current);
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
		
		_act = null;
		new Thread(this).start();
	}
	
	@Override
	public void removeUnit(Character c) {
		super.removeUnit(c);
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
