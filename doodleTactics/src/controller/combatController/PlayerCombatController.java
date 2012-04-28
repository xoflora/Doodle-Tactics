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
	
	private enum State {
		START,
		CHARACTER_SELECTED,
		CHARACTER_MOVING,
		CHARACTER_OPTION_MENU
	}
	
	private Tile _selectedTile;
	private Tile _hoveredTile;
	private Character _selectedCharacter;
	private Character _hoveredCharacter;
	
	private List<Tile> _selectedMovementRange;
	private List<Tile> _enemyAttackRange;
	
	private List<Tile> _path;
	private int _pathCost;
	
	private State state;
	
	public PlayerCombatController(DoodleTactics dt) {
		super(dt, dt.getParty());
		
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
	
	private void setPath(Tile source, Tile dest) {
		_pathCost = 0;
		_path = _gameScreen.getMap().getPath(source, dest);
		for (Tile t : _path) {
			t.setInMovementPath(true);
			_pathCost += t.cost();
		}
		
	}
	
	/**
	 * resets the player combat controller such that nothing is selected
	 */
	public void clear() {
		for (Tile t : _selectedMovementRange)
			t.setInMovementRange(false);
		for (Tile t : _enemyAttackRange)
			t.setInEnemyAttackRange(false);
		for (Tile t : _path)
			t.setInMovementPath(false);
		
		_selectedTile = null;
		_selectedCharacter = null;
		_hoveredCharacter = null;
		_selectedMovementRange = new ArrayList<Tile>();
		_enemyAttackRange = new ArrayList<Tile>();
		
		_path = new ArrayList<Tile>();
		_pathCost = 0;
		
		state = State.START;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		if (e.getButton() == MouseEvent.BUTTON1) {
			Tile t = _gameScreen.getTile(e.getX(), e.getY());
			if (t != null && t.isOccupied()) {
				Character c = t.getOccupant();
				if (state == State.START) {
					if (c.getAffiliation() == this) {
						_selectedTile = t;
						_selectedMovementRange = _gameScreen.getMap().getMovementRange(t, c.getMovementRange());
						_selectedCharacter = c;
						for (Tile toPaint : _selectedMovementRange)
							toPaint.setInMovementRange(true);
						state = State.CHARACTER_SELECTED;
					}
					else if (_enemyAffiliations.contains(c.getAffiliation())) {
						_enemyAttackRange = Util.union(_enemyAttackRange,
								_gameScreen.getMap().getAttackRange(t, c.getMovementRange(),
										c.getMinAttackRange(), c.getMaxAttackRange()));
					}
				}
				else if (state == State.CHARACTER_SELECTED) {
					
				}
				else if (state == State.CHARACTER_MOVING) {

				}
				else if (state == State.CHARACTER_OPTION_MENU) {

				}
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
			if (state == State.START)
				clear();
			else if (state == State.CHARACTER_SELECTED)
				clear();
			else if (state == State.CHARACTER_MOVING) {

			}
			else if (state == State.CHARACTER_OPTION_MENU) {

			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null) {
		/*	_hoveredCharacter = t.getOccupant();
			
			if (_hoveredCharacter == null) {
				if (_selectedTile != null && _selectedCharacter != null) {
					if (t.isAdjacent(_path.get(_path.size() - 1)) && _pathCost + t.cost() <=
							_selectedCharacter.getMovementRange()) {
						_pathCost += t.cost();
						_path.add(t);
					}
					else if (_selectedMovementRange.contains(t)) {
						_path = _gameScreen.getMap().getPath(_selectedTile, t);
						_pathCost = _selectedCharacter.getMovementRange();
					}
				}
			}*/
			
			if (state == State.CHARACTER_SELECTED) {	//selected character and tile are not null
				if (_selectedMovementRange.contains(t)) {
					if (_path.isEmpty()) {
						if (t.isAdjacent(_selectedTile) && t.cost() <= _selectedCharacter.getMovementRange()) {
							_path.add(t);
							_pathCost += t.cost();
							t.setInMovementPath(true);
						}
					}
					else if (!_path.contains(t)) {
						if (t.isAdjacent(_path.get(_path.size() - 1)) && _pathCost + t.cost() <=
								_selectedCharacter.getMovementRange()) {
							_path.add(t);
							_pathCost += t.cost();
							t.setInMovementPath(true);
							
							System.out.println("Hello");
						}
						else {
							System.out.println(_pathCost);
							for (Tile u : _path)
								u.setInMovementPath(false);
							_path = new ArrayList<Tile>();
							_pathCost = 0;
							
							setPath(_selectedTile, t);
						}
					}
					else {
						
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
		System.out.println("Player phase!");
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
	public void addUnitToPool(Character c) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void alternateAction(Character c) {
		
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
