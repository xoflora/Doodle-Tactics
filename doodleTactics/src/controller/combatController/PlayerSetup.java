package controller.combatController;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.Util;

import main.DoodleTactics;
import map.Tile;
import controller.GameScreenController;
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
	
	private Character _selectedCharacter;
	private boolean _selectedFromPool;

	public PlayerSetup(DoodleTactics dt, List<Tile> validTiles) {
		super(dt);
		_validTiles = validTiles;
		_units = _dt.getParty();
		_toPlace = Util.clone(_units);
		_inPlace = new ArrayList<Character>();
		
		_selectedCharacter = null;
		_selectedFromPool = false;

		try {
			_pool = new UnitPool(_dt, _gameScreen, this, _toPlace);
		} catch(IOException e) {
			
		}
	}

	@Override
	public void release() {
		for (Tile t : _validTiles) {
			t.setInMovementRange(false);
		}
	}

	@Override
	public void take() {
		
		initialize();
		
		for (Tile t : _validTiles) {
			t.setInMovementRange(true);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		MenuItem m = _gameScreen.checkContains(e.getPoint());
		boolean valid = _validTiles.contains(t);
		
	/*	if (_validTiles.contains(t)) {
			_selectedTile = t;
			
			System.out.println(e.getButton());
			Character occupant = t.getOccupant();
			if (e.getButton() == MouseEvent.BUTTON1) {	//select the character
				
			}
			else if (e.getButton() == MouseEvent.BUTTON3) {	//swap characters
				if (_selectedFromPool) {
					removeUnitFromPool(_selectedCharacter);
					if (occupant != null)
						addUnitToPool(occupant);
				}
				else {
					_selectedTile.setOccupant(occupant);
					t.setOccupant(_selectedCharacter);
				}
				
				_selectedCharacter = null;
				_selectedFromPool = false;
			}
		}	*/
		
		if (e.getButton() == UnitPool.SELECT_BUTTON) {
			if (valid && m == null) {
				_selectedTile = t;
				Character occupant = t.getOccupant();
				
				if (occupant != null) {
					_selectedCharacter = occupant;
					_selectedFromPool = false;
				}
			}
		/*	else if (m != null) {
				
			}	*/
		}
		else if (e.getButton() == UnitPool.SWAP_BUTTON) {	//swap characters
			if (valid && m == null) {
				if (_selectedFromPool) {
					
				}
				else if (_selectedTile != null) {
					_selectedTile.setOccupant(t.getOccupant());
					t.setOccupant(_selectedCharacter);
				}
			}
			
			_selectedCharacter = null;
			_selectedFromPool = false;
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
		if (!_selectedFromPool && _selectedTile != null) {
			_selectedTile.setOccupant(c);
			removeUnitFromPool(c);
			
			if (_selectedCharacter != null) {
				addUnitToPool(_selectedCharacter);
				_gameScreen.getCharacterQueue().remove(_selectedCharacter);
			}
			
			_gameScreen.getCharacterQueue().add(c);
		}
	}

	@Override
	public void removeUnitFromPool(Character c) {
		_pool.removeCharacter(c);
	}
	
	@Override
	public void initialize() {
		_pool.setInUse(true);
	}

	@Override
	public void finalize() {
		_pool.setInUse(false);
		release();
	}
}
