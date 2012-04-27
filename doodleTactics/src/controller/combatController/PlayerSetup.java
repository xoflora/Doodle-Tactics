package controller.combatController;

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
	private Character _selectedCharacter;
	private List<Character> _toPlace;
	private List<Character> _inPlace;
	private Tile _selectedTile;
	private UnitPool _pool;

	public PlayerSetup(DoodleTactics dt, List<Tile> validTiles) {
		super(dt);
		_validTiles = validTiles;
		_units = _dt.getParty();
		_toPlace = Util.clone(_units);
		_inPlace = new ArrayList<Character>();

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
		if (_validTiles.contains(t)) {
			_selectedTile = t;
		}
	}


	@Override
	public void getCharacterFromPool(Character c) {
		// TODO Auto-generated method stub
		
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
	public void addCharacterToPool(Character c) {
		_pool.addCharacter(c);
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
