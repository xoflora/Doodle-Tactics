package controller.combatController.player;

import graphics.MenuItem;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Util;

import main.DoodleTactics;
import map.Tile;
import controller.GameScreenController;
import controller.combatController.CombatController;
import controller.combatController.CombatOrchestrator;
import character.Character;

/**
 * controller class corresponding to the combat set-up step
 * @author rroelke
 *
 */
public class PlayerSetup extends GameScreenController implements PoolDependent {
	
	/**
	 * characterizes the state of the player setup controller
	 * @author rroelke
	 */
	private enum State {
		SELECTING,
		SELECTED_EMPTY_TILE,
		SELECTED_FROM_POOL,
		SELECTED_FROM_TILE
	}
	
	private List<Tile> _validTiles;
	private List<Character> _units;
	private List<Character> _toPlace;
	private HashMap<Character, Tile> _inPlace;
	private Tile _selectedTile;
	private UnitPool _pool;
	private boolean _finalized;
	
	private Character _selectedCharacter;
	
	private State _state;
	
	private CombatOrchestrator _orch;
	private List<Tile> _enemyAttackRange;
	private MenuItem _curtain;
	private MenuItem _doodleCombat;

	public PlayerSetup(DoodleTactics dt, List<Tile> validTiles, CombatOrchestrator orch) {
		super(dt);

		try {
			_validTiles = validTiles;
			_units = _dt.getParty();
			_units.remove(_gameScreen.getMainChar());
			
			_toPlace = Util.clone(_units);
			_inPlace = new HashMap<Character, Tile>();
			
			_selectedCharacter = null;
			
			_pool = new UnitPool(_dt, _gameScreen, this, _toPlace);
			
			_state = State.SELECTING;
			
			_finalized = false;
			
			_orch = orch;
			_enemyAttackRange = new ArrayList<Tile>();
			
			BufferedImage combat = _dt.importImage("src/graphics/menu/combatMenu/doodle_combat.png");
			BufferedImage curtain = _dt.importImage("src/graphics/menu/combatMenu/curtain.png");
			_doodleCombat = new MenuItem(_dt.getGameScreen(),combat,combat,_dt,3);
			_curtain = new MenuItem(_dt.getGameScreen(),curtain,curtain,_dt,2);
			_curtain.setLocation(-25,-900);
			_doodleCombat.setLocation(115, -306);
			
		} catch(IOException e) {
			_dt.error("Error initializing unit setup.");
		}
	}
	
	protected class CurtainTimer implements Runnable{
		private MenuItem _menu1;
		private MenuItem _menu2;
		private int _stop;
		private int _delay;
		
		public CurtainTimer(MenuItem menu1, MenuItem menu2, int stop, int delay){
			_menu1 = menu1;
			_menu2 = menu2;
			_stop = stop;
			_delay = delay;
		}
		@Override
		public void run() {
			
			double start = _menu1.getY();
			
			while(_menu1.getY() < _stop){
				try {
					Thread.sleep(_delay);
				} catch (InterruptedException e) {
					//Do Nothing
				}
				
				_menu1.setLocation(_menu1.getX(),_menu1.getY() + _delay);
				_dt.getGameScreen().repaint();
			}
						
			if(_menu2 != null) {
				Thread t = new Thread(new CurtainTimer(_menu2,null,0,_delay));
				t.start();
				
				try {
					Thread.sleep((long) ((Math.abs(_curtain.getY() - 10))*2));
				} catch (InterruptedException e) {
					
				}
			}
			
			if(_menu2 == null) {
				_pool.setInUse(true);
			}
				
			while(_menu1.getY() > start) {
				
				try {
					Thread.sleep(_delay);
				} catch (InterruptedException ex) {
					//Do Nothing
				}
				
				_menu1.setLocation(_menu1.getX(),_menu1.getY() - _delay);
				_dt.getGameScreen().repaint();
			}
			
			_dt.getGameScreen().removeMenuItem(_menu1);
		}
	}
	
	/**
	 * clears all selections of the map
	 */
	private void clearSelection() {
		if (_selectedTile != null)
			_selectedTile.setInMovementPath(false);
		_selectedTile = null;
		_selectedCharacter = null;
		_state = State.SELECTING;
	}
	
	private void clearEnemyAttackRange() {
		for (Tile t : _enemyAttackRange)
			t.setInEnemyAttackRange(false);
		_enemyAttackRange = new ArrayList<Tile>();
	}

	@Override
	public void release() {
		for (Tile t : _validTiles) {
			t.setInMovementRange(false);
		}
		clearSelection();
		clearEnemyAttackRange();
		super.release();
	}

	@Override
	public void take() {
		
		initialize();
		
		for (Tile t : _validTiles) {
			t.setInMovementRange(true);
		}
		
		super.take();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		
		if (t != null) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (_validTiles.contains(t)) {
					if (_state == State.SELECTING) {
						if (t.isOccupied()) {
							_selectedTile = t;
							_selectedTile.setInMovementPath(true);
							_selectedCharacter = t.getOccupant();
							_state = State.SELECTED_FROM_TILE;
						}
						else {
							_selectedTile = t;
							_selectedTile.setInMovementPath(true);
							_state = State.SELECTED_EMPTY_TILE;
						}
					}
					else if (_state == State.SELECTED_EMPTY_TILE) {
						if (t.isOccupied()) {	//move occupant into empty tile

							_selectedCharacter = t.getOccupant();
							_selectedTile.setOccupant(_selectedCharacter);
							t.setOccupant(null);

							_selectedCharacter.setLocation(_selectedTile.getX(), _selectedTile.getY());
							_selectedCharacter.setVisible(true);

							clearSelection();
							_state = State.SELECTING;
						}
					}
					else if (_state == State.SELECTED_FROM_POOL) {
						swapUnitIntoPool(t.getOccupant(), _selectedCharacter, t);
						clearSelection();
						_state = State.SELECTING;
					}
					else {
						Character sw = t.getOccupant();
						_selectedTile.setOccupant(sw);
						if (sw != null)
							sw.setLocation(_selectedTile.getX(), _selectedTile.getY());

						t.setOccupant(_selectedCharacter);
						_selectedCharacter.setLocation(t.getX(), t.getY());
						_selectedCharacter.setVisible(true);

						clearSelection();
						_state = State.SELECTING;
					}
				}
				else {
					Character occ = t.getOccupant();
					if (occ != null) {
						boolean isEnemy = false;
						for (CombatController c : _orch.getEnemyAffiliations())
							if (c.containsCharacter(occ)) {
								isEnemy = true;
								break;
							}
						if (isEnemy) {
							List<Tile> add = _gameScreen.getMap().getAttackRange(t, occ.getMovementRange(),
									occ.getMinAttackRange(), occ.getMaxAttackRange());
							for (Tile toPaint : add)
								toPaint.setInEnemyAttackRange(true);
							_enemyAttackRange = Util.union(_enemyAttackRange, add);
						}
					}
				}
			}
			else if (e.getButton() == MouseEvent.BUTTON3) {
				clearSelection();
				clearEnemyAttackRange();
			}
		}
	}
	
/*	@Override
	public void mouseReleased(MouseEvent e) {
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (_pool.contains(e.getPoint())) {
			
		}
		else if (t != null && !t.isOccupied() && _selectedCharacter != null) {
			t.setOccupant(_selectedCharacter);
			_selectedTile.setOccupant(null);
			_selectedCharacter = null;
			_selectedFromPool = false;
		}
	}	*/
	
	/**
	 * swaps a unit in the unit pool with a unit in the map
	 */
	private void swapUnitIntoPool(Character inMap, Character inPool, Tile t) {
		if (inMap != null) {
			addUnitToPool(inMap);
			inMap.setVisible(false);
			_inPlace.remove(inMap);
			_gameScreen.removeCharacter(inMap);
		}
		
		removeUnitFromPool(inPool);
		t.setOccupant(inPool);
		inPool.setVisible(true);
		inPool.setLocation(t.getX(), t.getY());
		inPool.setDown();
		_inPlace.put(inPool, t);
		_gameScreen.addCharacter(inPool);
		
		_gameScreen.repaint();
	}


	@Override
	public void getCharacterFromPool(Character c) {
		if (_state == State.SELECTING) {
			_selectedCharacter = c;
			_state = State.SELECTED_FROM_POOL;
		}
		else if (_state == State.SELECTED_EMPTY_TILE) {
			swapUnitIntoPool(null, c, _selectedTile);
			clearSelection();
			_state = State.SELECTING;
		}
		else if (_state == State.SELECTED_FROM_POOL)
			_selectedCharacter = c;
		else {
			swapUnitIntoPool(_selectedCharacter, c, _selectedTile);
			clearSelection();
			_state = State.SELECTING;
		}
	}
	
	@Override
	public List<Character> getUnits() {
		return _units;
	}

	@Override
	public UnitPool getPool() {
		return _pool;
	}

	@Override
	public void addUnitToPool(Character c) {
		_pool.addCharacter(c);
	}
	
	@Override
	/**
	 * swaps a character into or out of the unit pool
	 * @param c the character to swap
	 */
	public void alternateAction(Character c) {
	/*	if (!_selectedFromPool && _selectedTile != null && _selectedTile != null) {
			_selectedTile.setOccupant(c);
			c.setLocation(_selectedTile.getX(), _selectedTile.getY());
			c.setDown();
			removeUnitFromPool(c);
			System.out.println("character put in place at " + _selectedTile);
			_inPlace.put(c, _selectedTile);
			
			if (_selectedCharacter != null) {
				addUnitToPool(_selectedCharacter);
				_gameScreen.getCharacterQueue().remove(_selectedCharacter);
			}
			
			_gameScreen.addCharacter(c);
		}	*/
	}

	@Override
	public void removeUnitFromPool(Character c) {
		_pool.removeCharacter(c);
	}

	@Override
	public void unitPoolClicked(int type) {
		if (type == MouseEvent.BUTTON1) {
			if (_state == State.SELECTED_EMPTY_TILE) {
				clearSelection();
				_state = State.SELECTING;
			}
			else if (_state == State.SELECTED_FROM_TILE) {
				_selectedTile.setOccupant(null);

				addUnitToPool(_selectedCharacter);
				_selectedCharacter.setVisible(false);
				_inPlace.remove(_selectedCharacter);
				_gameScreen.removeCharacter(_selectedCharacter);

				clearSelection();
				_state = State.SELECTING;
			}
		}
	}
	
	@Override
	public void initialize() {
		if (_pool != null)
			
			// perform doodle combat curtain animation
			new Thread(new CurtainTimer(_doodleCombat,_curtain,-10,5)).start();

			_dt.getGameScreen().addMenuItem(_curtain);
			_dt.getGameScreen().addMenuItem(_doodleCombat);
			_curtain.setVisible(true);
			_doodleCombat.setVisible(true);
	}

	@Override
	public boolean finish() {
		if (!_finalized) {
			_finalized = true;
			_pool.setInUse(false);
			_orch.setPlayerUnits(_inPlace);
			_gameScreen.popControl();
			return true;
		}
		return false;
	}
}
