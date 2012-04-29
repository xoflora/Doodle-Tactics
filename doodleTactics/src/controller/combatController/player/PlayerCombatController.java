package controller.combatController.player;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.combatController.ActionType;
import controller.combatController.CombatController;


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
		CHARACTER_OPTION_MENU,
		SELECTING_ITEM
	}
	
	private Tile _destTile;
	private Tile _selectedTile;
	private Character _selectedCharacter;
	
	private List<Tile> _selectedMovementRange;
	private List<Tile> _enemyAttackRange;
	private List<Tile> _characterAttackRange;
	
	private List<Tile> _cacheMovementRange;
	private List<Tile> _cacheAttackRange;
	
	private List<Tile> _path;
	private int _pathCost;
	private List<Tile> _cachePath;
	private int _cacheCost;
	private Tile _cacheDestTile;
	
	private int _menuDraggedx;
	private int _menuDraggedy;
	private boolean _draggingMenu;
	
	private State _state;
	private UnitPool _pool;
	private CombatOptionWindow _optionWindow;
	private ItemWindow _itemWindow;
	private boolean _finalized;
	
	public PlayerCombatController(DoodleTactics dt, List<Character> units) {
		super(dt, units);

		_destTile = null;
		_selectedTile = null;
		_selectedCharacter = null;
		
		_selectedMovementRange = new ArrayList<Tile>();
		_enemyAttackRange = new ArrayList<Tile>();
		_characterAttackRange = new ArrayList<Tile>();
		
		_path = new ArrayList<Tile>();
		_pathCost = 0;
		_cachePath = null;
		_cacheCost = 0;
		_cacheDestTile = null;
		
		_cacheMovementRange = null;
		_cacheAttackRange = null;
		
		_pool = null;
		_optionWindow = null;
		_itemWindow = null;
		
		_menuDraggedx = 0;
		_menuDraggedy = 0;
		_draggingMenu = false;
		
		clear();
	}
	
	/**
	 * sets the current movement path
	 * @param source the starting tile
	 * @param dest the ending tile
	 */
	private void setPath(Tile source, Tile dest) {
		clearPath();
		_path = _gameScreen.getMap().getPath(source, dest);
		for (Tile t : _path) {
			t.setInMovementPath(true);
			_pathCost += t.cost();
		}
	}
	
	/**
	 * sets the player attack range
	 * @param source the source tile
	 * @param c the character
	 */
	private void setPlayerAttackRange(Tile source, Character c) {
		clearPlayerAttackRange();
		_characterAttackRange = _gameScreen.getMap().getAttackRange(source, 0,
				c.getMinAttackRange(), c.getMaxAttackRange());
		for (Tile t : _characterAttackRange)
			t.setInPlayerAttackRange(true);
	}
	
	/**
	 * resets the player combat controller such that nothing is selected
	 */
	private void clear() {
		for (Tile t : _enemyAttackRange)
			t.setInEnemyAttackRange(false);
		
		_selectedTile = null;
		_selectedCharacter = null;
		_destTile = null;
		_enemyAttackRange = new ArrayList<Tile>();

		clearMovementRange();
		clearPlayerAttackRange();
		clearPath();
		
		_cacheMovementRange = null;
		_cacheAttackRange = null;
		_cachePath = null;
		_cacheCost = 0;
		_cacheDestTile = null;
		
		if (_optionWindow != null)
			_optionWindow.removeFromDrawingQueue();
		_optionWindow = null;
		_state = State.START;
	}
	
	private void clearMovementRange() {
		for (Tile t : _selectedMovementRange)
			t.setInMovementRange(false);
		_selectedMovementRange = new ArrayList<Tile>();
	}
	
	private void clearPlayerAttackRange() {
		for (Tile t : _characterAttackRange)
			t.setInPlayerAttackRange(false);
		_characterAttackRange = new ArrayList<Tile>();
	}
	
	private void clearPath() {
		for (Tile t : _path)
			t.setInMovementPath(false);
		_path = new ArrayList<Tile>();
		_pathCost = 0;
	}
	
	@Override
	public void move(Character c, Tile source, List<Tile> path) {
		super.move(c, source, path);
		
		try {
			_optionWindow = new CombatOptionWindow(_dt, _gameScreen, false,
					_selectedCharacter.getInventory().size() != 0, false, this);
			_optionWindow.setLocation(_destTile.getX() + Tile.TILE_SIZE, _destTile.getY() - Tile.TILE_SIZE);
			_optionWindow.addToDrawingQueue();
			
		} catch(IOException e) {
			_dt.error("Error finding combat window files.");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);

		if (e.getButton() == MouseEvent.BUTTON1) {
			Tile t = _gameScreen.getTile(e.getX(), e.getY());
			if (t != null) {
				Character c = t.getOccupant();
				if (_state == State.START) {
					if (t.isOccupied()) {
						if (c.getAffiliation() == this && !hasMoved(c)) {
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
							_state = State.CHARACTER_SELECTED;
						}
						else if (_enemyAffiliations.contains(c.getAffiliation())) {
							_enemyAttackRange = Util.union(_enemyAttackRange,
									_gameScreen.getMap().getAttackRange(t, c.getMovementRange(),
											c.getMinAttackRange(), c.getMaxAttackRange()));
						}
					}
				}
				//selected tile and character are not null; selected character is the occupant of the tile
				else if (_state == State.CHARACTER_SELECTED) {
					if (_path.contains(t)) {
						_destTile = t;
						_state = State.CHARACTER_MOVING;
						
						move(_selectedCharacter, _selectedTile, _path);
						
						_cacheMovementRange = _selectedMovementRange;
						clearMovementRange();
						
						_cacheAttackRange = _characterAttackRange;
						setPlayerAttackRange(_destTile, _selectedCharacter);
						
						_cachePath = _path;
						_cacheCost = _pathCost;
						_cacheDestTile = _destTile;
						clearPath();
						
						_state = State.CHARACTER_OPTION_MENU;
					}
				}
				//selected tile is the coordinate to move the character to
				else if (_state == State.CHARACTER_MOVING) {
					
				}
				else if (_state == State.CHARACTER_OPTION_MENU) {
				//	_pool.removeCharacter(_selectedCharacter);
				//	clear();
				//	_state = State.START;
					if (_characterAttackRange.contains(t) && isEnemy(t.getOccupant())) {
						attack(_selectedCharacter, t.getOccupant());
						_pool.removeCharacter(_selectedCharacter);
						
						clear();
						_state = State.START;
					}
				}
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
			if (_state == State.START)
				clear();
			else if (_state == State.CHARACTER_SELECTED)
				clear();
			else if (_state == State.CHARACTER_MOVING) {
				clearPath();
				_state = State.CHARACTER_SELECTED;
			}
			else if (_state == State.CHARACTER_OPTION_MENU) {
				_destTile.setOccupant(null);
				_selectedTile.setOccupant(_selectedCharacter);
				_selectedCharacter.setLocation(_selectedTile.getX(), _selectedTile.getY());
				clearPath();
				clearPlayerAttackRange();
				
				_selectedMovementRange = _cacheMovementRange;
				for (Tile toPaint : _selectedMovementRange)
					toPaint.setInMovementRange(true);
				_cacheMovementRange = null;
				
				_characterAttackRange = _cacheAttackRange;
				for (Tile toPaint : _characterAttackRange)
					toPaint.setInPlayerAttackRange(true);
				_cacheAttackRange = null;
				
				_path = _cachePath;
				_pathCost = _cacheCost;
				_destTile = _cacheDestTile;
				for (Tile toPaint : _path)
					toPaint.setInMovementPath(true);
				_cachePath = null;
				_cacheCost = 0;
				_cacheDestTile = null;
				
				_hasMoved.put(_selectedCharacter, false);
				_optionWindow.removeFromDrawingQueue();
				_optionWindow = null;
				
				_state = State.CHARACTER_SELECTED;
			}
			else if (_state == State.SELECTING_ITEM) {
				_itemWindow.removeFromDrawingQueue();
				_itemWindow = null;
				
				_optionWindow.addToDrawingQueue();
				_state = State.CHARACTER_OPTION_MENU;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null) {
			
			if (_state == State.CHARACTER_SELECTED) {	//selected character and tile are not null
				if (_selectedMovementRange.contains(t)) {
					if (_path.isEmpty()) {
						if (!_path.contains(_selectedTile)) {
							_path.add(_selectedTile);
							_selectedTile.setInMovementPath(true);
						}
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
	 * 
	 */
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		MenuItem m = _gameScreen.checkContains(e.getPoint());
		if (m != null && (m == _optionWindow || m == _itemWindow)) {
			_menuDraggedx = e.getX();
			_menuDraggedy = e.getY();
			_draggingMenu = true;
		}
	}
	
	@Override
	/**
	 * 
	 */
	public void mouseDragged(MouseEvent e) {
		if (!_draggingMenu)
			super.mouseDragged(e);
		else {
			if (_optionWindow != null) {
				_optionWindow.setLocation(e.getX() - _menuDraggedx, e.getY() - _menuDraggedy);
				_menuDraggedx = e.getX();
				_menuDraggedy = e.getY();
			}
			if (_itemWindow != null) {
				_itemWindow.setLocation(e.getX() - _menuDraggedx, e.getY() - _menuDraggedy);
				_menuDraggedx = e.getX();
				_menuDraggedy = e.getY();
			}
		}
	}
	
	@Override
	/**
	 * 
	 */
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		_draggingMenu = false;
	}

	@Override
	/**
	 * ends the player's turn
	 */
	public void release() {
		// TODO Auto-generated method stub
		super.release();
		
		clear();
	}

	@Override
	/**
	 * begins the player's turn
	 */
	public void take() {
		super.take();
		System.out.println("Player phase!");
		
		initialize();
	}

	@Override
	/**
	 * pans the camera to focus on the given character
	 */
	public void getCharacterFromPool(Character c) {
		// TODO Auto-generated method stub
		double x = c.getX();
		double y = c.getY();
		//pan to coordinates
	}

	@Override
	public UnitPool getPool() {
		return _pool;
	}

	@Override
	/**
	 * unused
	 */
	public void addUnitToPool(Character c) { }
	
	@Override
	/**
	 * pans the map to focus on the given character
	 */
	public void alternateAction(Character c) {
		getCharacterFromPool(c);
	}

	@Override
	/**
	 * 
	 */
	public void removeUnitFromPool(Character c) {
		_pool.removeCharacter(c);
	}
	
	@Override
	/**
	 * 
	 */
	public void initialize() {
		try {
			_pool = new UnitPool(_dt, _gameScreen, this, _units);
			_pool.setInUse(true);
		} catch (IOException e) {
			_dt.error("");
		}
	}
	
	@Override
	/**
	 * used at the end of the player's turn
	 */
	public void finalize() {
		if (!_finalized) {
			_finalized = true;
			_pool.setInUse(false);
			_pool = null;
			_gameScreen.popControl();
			_finalized = false;
		}
	}

	/**
	 * sends an action to the controller
	 * @param action the action to send
	 */
	public void pushAction(ActionType action) {
		assert(_state == State.CHARACTER_OPTION_MENU);
		
		if (action == ActionType.WAIT) {
			_pool.removeCharacter(_selectedCharacter);
			clear();
			System.out.println(_pool.getNumUnits());
			if (_pool.isEmpty()) {
				_pool.setInUse(false);
				_pool = null;
				_gameScreen.popControl();
			}
			else
				_state = State.START;
		}
		else if (action == ActionType.ITEM) {
			_itemWindow = new ItemWindow(_gameScreen, _dt, _selectedCharacter, this);
			_itemWindow.setLocation(_optionWindow.getX(), _optionWindow.getY());
			
			_optionWindow.removeFromDrawingQueue();
			_itemWindow.addToDrawingQueue();
			
			_state = State.SELECTING_ITEM;
		}
		else if (action == ActionType.SPECIAL) {
			
		}
	}
}
