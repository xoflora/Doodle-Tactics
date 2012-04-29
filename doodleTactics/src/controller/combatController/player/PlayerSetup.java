package controller.combatController.player;

import graphics.MenuItem;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.Util;

import main.DoodleTactics;
import map.Tile;
import controller.GameScreenController;
import controller.combatController.CombatOrchestrator;
import character.Character;

/**
 * controller class corresponding to the combat set-up step
 * @author rroelke
 *
 */
public class PlayerSetup extends GameScreenController implements PoolDependent {
	
	private List<Tile> _validTiles;
	private List<Character> _units;
	private List<Character> _toPlace;
	private List<Character> _inPlace;
	private Tile _selectedTile;
	private UnitPool _pool;
	private boolean _finalized;
	
	private Character _selectedCharacter;
	private boolean _selectedFromPool;
	
	private CombatOrchestrator _orch;

	public PlayerSetup(DoodleTactics dt, List<Tile> validTiles, CombatOrchestrator orch) {
		super(dt);

		try {
			_validTiles = validTiles;
			_units = _dt.getParty();
			
			_toPlace = Util.clone(_units);
			_inPlace = new ArrayList<Character>();
			
			_selectedCharacter = null;
			_selectedFromPool = false;
			
			_pool = new UnitPool(_dt, _gameScreen, this, _toPlace);
			
			_finalized = false;
			
			_orch = orch;
		} catch(IOException e) {
			_dt.error("");
		}
	}
	
	/**
	 * clears all selections of the map
	 */
	private void clearSelection() {
		if (_selectedTile != null)
			_selectedTile.setInEnemyAttackRange(false);
		_selectedTile = null;
		_selectedCharacter = null;
		_selectedFromPool = false;
	}

	@Override
	public void release() {
		for (Tile t : _validTiles) {
			t.setInMovementRange(false);
		}
		System.out.println("setup released");
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
		MenuItem m = _gameScreen.checkContains(e.getPoint());
		boolean valid = _validTiles.contains(t);
		
		if (e.getButton() == UnitPool.SELECT_BUTTON) {
			if (valid && m == null) {
				if (_selectedTile != null)
					_selectedTile.setInEnemyAttackRange(false);
				_selectedTile = t;
				_selectedTile.setInEnemyAttackRange(true);
				
				Character occupant = _selectedTile.getOccupant();
				
				_selectedCharacter = occupant;
				_selectedFromPool = false;
				System.out.println("occupant at selected tile: " + occupant);
			}
		/*	else if (m != null) {
				System.out.println("currently selected: " + _selectedCharacter);
			}	*/
			else if (m == null)
				clearSelection();
		}
		else if (e.getButton() == UnitPool.ALT_BUTTON) {	//swap characters
			System.out.println("selected character: " + _selectedCharacter);
		
			if (valid && m == null) {
				if (_selectedFromPool) {	//swap character out of pool into tile
					Character c = t.getOccupant();
					t.setOccupant(_selectedCharacter);
					
					if (_selectedCharacter != null) {
						_gameScreen.addCharacter(_selectedCharacter);
						_pool.removeCharacter(_selectedCharacter);
						_selectedCharacter.setLocation(t.getX(), t.getY());
						_selectedCharacter.setDown();
						_inPlace.add(_selectedCharacter);
					}
					if (c != null) {
						_gameScreen.getCharacterQueue().remove(c);
						_pool.addCharacter(c);
					}
				}
				else if (_selectedCharacter != null) {
					Character c = t.getOccupant();
					_selectedTile.setOccupant(c);
					t.setOccupant(_selectedCharacter);
					_selectedCharacter.setLocation(t.getX(), t.getY());
					if (c != null)
						c.setLocation(_selectedTile.getX(), _selectedTile.getY());
				}
			}
			
			clearSelection();
			
			System.out.println(_selectedCharacter + " " + _selectedFromPool);
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


	@Override
	public void getCharacterFromPool(Character c) {
		_selectedCharacter = c;
		_selectedFromPool = true;
		System.out.println("selected character " + c);
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
		if (!_selectedFromPool && _selectedTile != null && _selectedTile != null) {
			_selectedTile.setOccupant(c);
			c.setLocation(_selectedTile.getX(), _selectedTile.getY());
			c.setDown();
			removeUnitFromPool(c);
			_inPlace.add(c);
			
			if (_selectedCharacter != null) {
				addUnitToPool(_selectedCharacter);
				_gameScreen.getCharacterQueue().remove(_selectedCharacter);
			}
			
			_gameScreen.addCharacter(c);
		}
	}

	@Override
	public void removeUnitFromPool(Character c) {
		_pool.removeCharacter(c);
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
