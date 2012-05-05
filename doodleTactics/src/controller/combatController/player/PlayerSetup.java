package controller.combatController.player;

import graphics.MenuItem;

import java.awt.event.MouseEvent;
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
			
		} catch(IOException e) {
			_dt.error("Error initializing unit setup.");
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
		_inPlace.put(inPool, _selectedTile);
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
			_pool.setInUse(true);
	}

	@Override
	public void finalize() {
		if (!_finalized) {
			_finalized = true;
			_pool.setInUse(false);
			_orch.setPlayerUnits(_inPlace);
			_gameScreen.popControl();
		}
	}
}
