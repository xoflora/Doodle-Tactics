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
	
	private Tile _destTile;
	private Tile _selectedTile;
	private Character _selectedCharacter;
	
	private List<Tile> _selectedMovementRange;
	private List<Tile> _enemyAttackRange;
	private List<Tile> _characterAttackRange;
	
	private List<Tile> _path;
	private int _pathCost;
	
	private State state;
	
	public PlayerCombatController(DoodleTactics dt) {
		super(dt, dt.getParty());

		_destTile = null;
		_selectedTile = null;
		_selectedCharacter = null;
		
		_selectedMovementRange = new ArrayList<Tile>();
		_enemyAttackRange = new ArrayList<Tile>();
		_characterAttackRange = new ArrayList<Tile>();
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
	private void clear() {
		for (Tile t : _selectedMovementRange)
			t.setInMovementRange(false);
		for (Tile t : _enemyAttackRange)
			t.setInEnemyAttackRange(false);
		for (Tile t : _characterAttackRange)
			t.setInPlayerAttackRange(false);
		
		_selectedTile = null;
		_selectedCharacter = null;
		_selectedMovementRange = new ArrayList<Tile>();
		_enemyAttackRange = new ArrayList<Tile>();
		_characterAttackRange = new ArrayList<Tile>();
		
		clearPath();
		
		state = State.START;
	}
	
	private void clearPath() {
		for (Tile t : _path)
			t.setInMovementPath(false);
		_path = new ArrayList<Tile>();
		_pathCost = 0;
		_destTile = null;
		
		if (_selectedTile != null) {
			_path.add(_selectedTile);
			_selectedTile.setInMovementPath(true);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		if (e.getButton() == MouseEvent.BUTTON1) {
			Tile t = _gameScreen.getTile(e.getX(), e.getY());
			if (t != null) {
				Character c = t.getOccupant();
				if (state == State.START) {
					if (t.isOccupied()) {
						if (c.getAffiliation() == this) {
							_selectedTile = t;
							_selectedCharacter = c;
							
							List<Tile> mv = _gameScreen.getMap().getMovementRange(t, c.getMovementRange());
							List<Tile> atk = _gameScreen.getMap().getAttackRange(t, c.getMovementRange(),
												c.getMinAttackRange(), c.getMaxAttackRange());
							
							_selectedMovementRange = mv;
							_characterAttackRange = Util.difference(atk, mv);
							
							for (Tile toPaint : _selectedMovementRange)
								toPaint.setInMovementRange(true);
							for (Tile toPaint : _characterAttackRange) {
								toPaint.setInPlayerAttackRange(true);
							}
							state = State.CHARACTER_SELECTED;
						}
						else if (_enemyAffiliations.contains(c.getAffiliation())) {
							_enemyAttackRange = Util.union(_enemyAttackRange,
									_gameScreen.getMap().getAttackRange(t, c.getMovementRange(),
											c.getMinAttackRange(), c.getMaxAttackRange()));
						}
					}
				}
				//selected tile and character are not null; selected character is the occupant of the tile
				else if (state == State.CHARACTER_SELECTED) {
					if (_path.contains(t)) {
						_destTile = t;
						state = State.CHARACTER_MOVING;
						move(_selectedCharacter, _path);
						state = State.CHARACTER_OPTION_MENU;
					}
				}
				//selected tile is the coordinate to move the character to
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
				clearPath();
				state = State.CHARACTER_SELECTED;
			}
			else if (state == State.CHARACTER_OPTION_MENU) {
				_destTile.setOccupant(null);
				_selectedTile.setOccupant(_selectedCharacter);
				_selectedCharacter.setLocation(_selectedTile.getX(), _selectedTile.getY());
				clearPath();
				state = State.CHARACTER_SELECTED;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null) {
			
			if (state == State.CHARACTER_SELECTED) {	//selected character and tile are not null
				if (_selectedMovementRange.contains(t)) {
					if (_path.isEmpty()) {
						if (t.isAdjacent(_selectedTile) && t.cost() <= _selectedCharacter.getMovementRange()) {
							_path.add(t);
							_pathCost += t.cost();
							t.setInMovementPath(true);
						}
						else
							setPath(_selectedTile, t);
					}
					else if (!_path.contains(t)) {
						if (t.isAdjacent(_path.get(_path.size() - 1)) && _pathCost + t.cost() <=
								_selectedCharacter.getMovementRange()) {
							_path.add(t);
							_pathCost += t.cost();
							t.setInMovementPath(true);
						}
						else {	//unreachable from current path - change entirely
							clearPath();
							setPath(_selectedTile, t);
						}
					}
					else {	//backtracing in path
						int i = _path.size() - 1;
						while (_path.get(i) != t && i >= 0) {
							_path.get(i).setInMovementPath(false);
							_path.remove(i);
							i--;
						}
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
