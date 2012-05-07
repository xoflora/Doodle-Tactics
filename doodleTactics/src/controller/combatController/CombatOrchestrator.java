package controller.combatController;

import graphics.MenuItem;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import util.Util;
import controller.GameScreenController;
import controller.combatController.player.PlayerCombatController;
import controller.combatController.player.PlayerSetup;
import main.DoodleTactics;
import map.Tile;
import character.Character;

public abstract class CombatOrchestrator extends GameScreenController {
	
	private enum State {
		SETUP,
		SETUP_COMPLETE,
		BATTLING
	}
	
	private List<CombatController> _factions;
	private ListIterator<CombatController> _factionCycle;
	private State _state;
	
	protected List<CombatController> _enemies;
	protected List<CombatController> _partners;
	protected List<CombatController> _others;
	protected List<CombatController> _defeated;
	
	private int _numUnits;
	private static final int NUM_EXTRA_SETUP_SPACES = 0;
	
	private PlayerCombatController _p;
	
	public CombatOrchestrator(DoodleTactics dt, List<CombatController> enemies, List<CombatController> partners,
				List<CombatController> others, int numUnits) {
		super(dt);
				
		_factions = null;
		_factionCycle = null;
		_state = State.SETUP;
		_numUnits = numUnits;
		
		_enemies = enemies;
		_partners = partners;
		_others = others;
		_defeated = new ArrayList<CombatController>();
		
		_p = null;
	}
	
	protected class VictoryTimer implements Runnable {
		private MenuItem _menu;
		private int _stop;
		private int _delay;
		
		public VictoryTimer(MenuItem menu, int stop, int delay) {
			_menu = menu;
			_stop = stop;
			_delay = delay;
		}
		
		@Override
		public void run() {
			
			double start = _menu.getY();
			
			while(_menu.getY() < _stop){
				try {
					Thread.sleep(_delay);
				} catch (InterruptedException e) {
					//Do Nothing
				}
				
				_menu.setLocation(_menu.getX(),_menu.getY() + (_delay/2));
				_dt.getGameScreen().repaint();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				//Do Nothing
			}
			
			while(_menu.getY() > start) {
				
				try {
					Thread.sleep(_delay);
				} catch (InterruptedException ex) {
					//Do Nothing
				}
				
				_menu.setLocation(_menu.getX(),_menu.getY() - (_delay/2));
				_dt.getGameScreen().repaint();
			}
			
			while (!_p.getUnits().isEmpty())
				//	if (_p.getUnits().get(0) != _gameScreen.getMainChar())
				_p.removeUnit(_p.getUnits().get(0));
			
			_dt.getGameScreen().removeMenuItem(_menu);
			_gameScreen.popControl();
		}
	}
	
	public List<CombatController> getEnemyAffiliations() {
		return _enemies;
	}

	@Override
	/**
	 * releases control of the game screen
	 * signifies the end of combat, or the beginning of a turn
	 */
	public void release() {
		// TODO determine whether the combat is a win or a loss and act accordingly
		//	if combat is a loss, swap screens for the "game over" screen
		super.release();
	}
	
	/**
	 * performs any tile events that are associated with a combat controller
	 */
	public abstract void performTileEvents();
	
	/**
	 * performs an update that happens every x turns
	 */
	public abstract void performTurnUpdate();
	
	/**
	 * @return whether or not the current combat state represents a win for the player
	 */
	public abstract boolean isWin();
	
	/**
	 * @return whether or not the current combat state represents a loss for the player
	 */
	public abstract boolean isLoss();
	
	/**
	 * @return whether or not the current combat state reprsents an escape condition
	 */
	public abstract boolean isRun();
	
	
	
	
	/**
	 * removes a defeated faction from this combat controller
	 * @param f the faction to remove
	 */
	public void removeFaction(CombatController f) {
		if (_enemies != null && _enemies.remove(f))
			return;
		else if (_partners != null && _partners.remove(f))
			return;
		else if (_others != null && _others.remove(f))
			return;
		_dt.error("Combat faction is neither partner, enemy, nor neutral.");
	}

	@Override
	/**
	 * gains control of the game screen
	 * signifies the beginning of combat, or end of a turn
	 */
	public void take() {
		super.take();
		
		if (_state == State.BATTLING) {
			
			if (isWin()) {
				victory();
			}
			else if (isLoss()) {
				
			}
			else if (isRun()) {
				
			}
			else {
				if (_factionCycle.hasNext()) {
					CombatController f = _factionCycle.next();
				//	System.out.println("MEHHH");
					_gameScreen.pushControl(f);
				}
				else {	// factions should not be empty; if the player controller is removed the loss condition would
						// have already occurred
					System.out.println("SHOULDN'T AHPPEN YET");
					_factionCycle = _factions.listIterator();
					take();
				}
			}
			
		/*	if (_factionCycle.hasNext()) {
				CombatController f = _factionCycle.next();
				if (f.getUnits().isEmpty()) {
					_factionCycle.remove();
					removeFaction(f);
					if (isWin())
						victory();
					else if (isLoss())
						defeat();
					else
						take();
				}
				else
					_gameScreen.pushControl(f);
			}
			else if (!_factions.isEmpty()) {	//INCORRECT CONDITION - swap for not win/loss condition
				_factionCycle = _factions.listIterator();
				take();
			}	*/
		}
		else if (_state == State.SETUP_COMPLETE) {
			System.out.println("SETUP COMPLETE");
			if (_partners == null)
				_partners = new ArrayList<CombatController>();
			System.out.println(_p == null);
			_partners.add(_p);
			
			for (CombatController p : _partners)
				p.setEnemyAffiliations(_enemies);
			
			for (CombatController e : _enemies)
				e.setEnemyAffiliations(_partners);
			
			List<List<CombatController>> zip = new ArrayList<List<CombatController>>();
			
			zip.add(_partners);
			if (_enemies != null)
				zip.add(_enemies);
			if (_others != null)
				zip.add(_others);
			
			_factions = Util.zip(zip);
			for (CombatController c : _factions)
				c.setOrchestrator(this);
			
			
			_factionCycle = _factions.listIterator();
			_state = State.BATTLING;
			take();
		}
		else if (_state == State.SETUP){
			System.out.println("SETTING UP");
			_gameScreen.pushControl(new PlayerSetup(_dt,
					_gameScreen.getValidSetupTiles(_numUnits + NUM_EXTRA_SETUP_SPACES), this));
			_state = State.SETUP_COMPLETE;
		}
	}
	
	/**
	 * ends the combat with the player victorious
	 */
	private void victory() {
		System.out.println("Player is victorious!");
		
		while (!_p.getUnits().isEmpty())
				_p.removeUnit(_p.getUnits().get(0));
		_gameScreen.popControl();
		
		BufferedImage img = _dt.importImage("src/graphics/menu/combatMenu/victory.png");
		MenuItem victory = new MenuItem(_gameScreen,img,img,_dt,0);
		victory.setLocation(((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - victory.getImage().getWidth())/2, - victory.getHeight());
		_gameScreen.addMenuItem(victory);
		victory.setVisible(true);
		new Thread(new VictoryTimer(victory,0,20)).start();
	}
	
	/**
	 * ends the combat with the player defeated
	 */
	private void defeat() {

	}

	/**
	 * @return a list containing all the characters involved in the battle
	 */
	public List<Character> getCharactersToDisplay() {
		List<List<Character>> c = new ArrayList<List<Character>>();
		c.add(super.getCharactersToDisplay());
		if (_factions != null)
			for (CombatController faction : _factions)
				c.add(faction.getUnits());
		return Util.union(c);
	}

	/**
	 * set the player combat controller to have the given units
	 * @param units the unit list of the player combat controller
	 */
	public void setPlayerUnits(HashMap<Character, Tile> units) {
		System.out.println("EWOIUREWORIU");
		units.put(_gameScreen.getMainChar(), _gameScreen.
				getTile((int)_gameScreen.getMainChar().getX(), (int)_gameScreen.getMainChar().getY()));
		_p = new PlayerCombatController(_dt, units);
	}
}
