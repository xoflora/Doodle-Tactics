package controller.combatController;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


import util.Util;

import main.DoodleTactics;
import map.*;
import character.Character;

/**
 * 
 * @author rroelke
 * 
 */
public class PlayerCombatController extends CombatController implements PoolDependent {
	
	private Tile _selectedTile;
	private Tile _hoveredTile;
	private Character _selectedCharacter;
	private Character _hoveredCharacter;
	
	private List<Tile> _selectedMovementRange;
	private List<Tile> _enemyAttackRange;
	
	private List<Tile> _path;
	private int _pathCost;
	
	public PlayerCombatController(DoodleTactics dt) {
		super(dt, dt.getParty());
		
		System.out.println(_gameScreen == _dt.getGameScreen());
		System.out.println(_gameScreen == null);
		
		_selectedTile = null;
		_hoveredTile = null;
		_selectedCharacter = null;
		_hoveredCharacter = null;
		
		_selectedMovementRange = new ArrayList<Tile>();
		_enemyAttackRange = new ArrayList<Tile>();
		_path = new ArrayList<Tile>();
		_pathCost = 0;
		
		clear();
	}
	
	/**
	 * resets the player combat controller such that nothing is selected
	 */
	public void clear() {
		for (Tile t : _selectedMovementRange)
			t.setInMovementRange(false);
		for (Tile t : _enemyAttackRange)
			t.setInEnemyAttackRange(false);
		
		_selectedTile = null;
		_selectedCharacter = null;
		_hoveredCharacter = null;
		_selectedMovementRange = new ArrayList<Tile>();
		_enemyAttackRange = new ArrayList<Tile>();
		
		_path = new ArrayList<Tile>();
		_pathCost = 0;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null) {
			Character c = t.getOccupant();
			
			if (c != null) {
				if (c.getAffiliation() == this && !hasMoved(c)) {
					_selectedTile = t;
					_selectedMovementRange = _map.getMovementRange(t, c.getMovementRange());

					//set paint properties the movement range
					for (Tile paint : _selectedMovementRange)
						paint.setInMovementRange(true);
				}
				else if (_enemyAffiliations.contains(c.getAffiliation())) {
					_enemyAttackRange = Util.union(_enemyAttackRange,
							_map.getAttackRange(t, c.getMovementRange(),
									c.getMinAttackRange(), c.getMaxAttackRange()));

					//set paint properties all tiles in that range.
					for (Tile paint : _enemyAttackRange)
						paint.setInEnemyAttackRange(true);
				}
			}
			else if (_selectedMovementRange.contains(t)) {
				
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null) {
			_hoveredCharacter = t.getOccupant();
			
			if (_hoveredCharacter == null) {
				if (_selectedTile != null && _selectedCharacter != null) {
					if (t.isAdjacent(_path.get(_path.size() - 1)) && _pathCost + t.cost() <=
							_selectedCharacter.getMovementRange()) {
						_pathCost += t.cost();
						_path.add(t);
					}
					else if (_selectedMovementRange.contains(t)) {
						_path = _map.getPath(_selectedTile, t);
						_pathCost = _selectedCharacter.getMovementRange();
					}
				}
			}
		}
	}

	@Override
	/**
	 * ends the player's turn
	 */
	public void release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * begins the player's turn
	 */
	public void take() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCharacterFromPool(Character c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UnitPool getPool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCharacterToPool(Character c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUnitFromPool(Character c) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initialize() {
		
	}
	
	@Override
	public void finalize() {
		
	}
}
