package controller.combatController.player;

import graphics.MenuItem;

import items.Item;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

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
	
	private UnitPool _pool;
	private Stack<CombatMenu> _menus;
	private MenuItem _playerPhase;
	private boolean _finalized;
	
	public PlayerCombatController(DoodleTactics dt, HashMap<Character, Tile> units) {
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
		
		_menuDraggedx = 0;
		_menuDraggedy = 0;
		_draggingMenu = false;
		_menus = new Stack<CombatMenu>();
		
		BufferedImage img = _dt.importImage("src/graphics/menu/player_phase.png");
		_playerPhase = new MenuItem(_dt.getGameScreen(),img,img,_dt,-10);
		_playerPhase.setLocation(1050,20);
		_playerPhase.setVisible(true);
		_dt.getGameScreen().addMenuItem(_playerPhase);

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
	 * @param useMove whether or not the attack range factor in movement range
	 */
	private void setPlayerAttackRange(Tile source, Character c, boolean useMove) {
		clearPlayerAttackRange();
		
		if (useMove) {
			List<Tile> mv = _gameScreen.getMap().getMovementRange(source, c.getMovementRange());
			List<Tile> atk = _gameScreen.getMap().getAttackRange(source, c.getMovementRange(),
								c.getMinAttackRange(), c.getMaxAttackRange());
			
			_selectedMovementRange = mv;
			_characterAttackRange = Util.difference(atk, mv);
			
			for (Tile toPaint : _selectedMovementRange)
				toPaint.setInMovementRange(true);
		}
		else
			_characterAttackRange = _gameScreen.getMap().getAttackRange(source, 0,
					c.getMinAttackRange(), c.getMaxAttackRange());
		
		for (Tile toPaint : _characterAttackRange)
			toPaint.setInPlayerAttackRange(true);
		
	/*	List<Tile> mv = _gameScreen.getMap().getMovementRange(t, c.getMovementRange());
		List<Tile> atk = _gameScreen.getMap().getAttackRange(t, c.getMovementRange(),
							c.getMinAttackRange(), c.getMaxAttackRange());
		
		_selectedMovementRange = mv;
		_characterAttackRange = Util.difference(atk, mv);
		
		for (Tile toPaint : _selectedMovementRange)
			toPaint.setInMovementRange(true);
		for (Tile toPaint : _characterAttackRange) {
			toPaint.setInPlayerAttackRange(true);
		}	*/
	}
	
	/**
	 * draws another layer of combat menu, hiding the previous layer (if it exists)
	 * @param menu the new menu to draw
	 * @param x the x-position of the menu
	 * @param y the y-position of the menu
	 */
	private void addMenu(CombatMenu menu, double x, double y) {
		if (!_menus.isEmpty() && _menus.peek() != null)
			_menus.peek().removeFromDrawingQueue();
		_menus.push(menu);
		menu.setLocation(x, y);
		menu.addToDrawingQueue();
	}
	
	/**
	 * removes the highest layer of combat menu, drawing the previous layer (if it exists)
	 * @return
	 */
	CombatMenu removeMenu() {
		CombatMenu toReturn = _menus.pop();
		toReturn.removeFromDrawingQueue();

		if (!_menus.isEmpty()) {
			CombatMenu next = _menus.peek();
			if (next != null)
				next.addToDrawingQueue();
		}
		
		return toReturn;
	}
	
	/**
	 * removes and un-draws all menu items
	 */
	private void removeAllMenus() {
		while (!_menus.isEmpty()) {
			_menus.pop().removeFromDrawingQueue();
		}
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
		
		removeAllMenus();
		
		setState(State.START);
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
	}
	
	@Override
	public void moveComplete() {
		super.moveComplete();
		addMenu(new CombatOptionWindow(_dt, _gameScreen, false,
					_selectedCharacter.getInventory().size() != 0, false, this),
				_destTile.getX() + Tile.TILE_SIZE,
				_destTile.getY() - Tile.TILE_SIZE);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			Tile t = _gameScreen.getTile(e.getX(), e.getY());
			if (t != null) {
				Character c = t.getOccupant();
				if (getState() == State.START) {
					if (t.isOccupied()) {
						if (c.getAffiliation() == this && !hasMoved(c)) {
							_selectedTile = t;
							_selectedCharacter = c;
							
							setPlayerAttackRange(_selectedTile, _selectedCharacter, true);
							
							setState(State.CHARACTER_SELECTED);
						}
						else if (isEnemy(c)) {
							_enemyAttackRange = Util.union(_enemyAttackRange,
									_gameScreen.getMap().getAttackRange(t, c.getMovementRange(),
											c.getMinAttackRange(), c.getMaxAttackRange()));
							for (Tile toPaint : _enemyAttackRange)
								toPaint.setInEnemyAttackRange(true);
						}						
					}
				}
				//selected tile and character are not null; selected character is the occupant of the tile
				else if (getState() == State.CHARACTER_SELECTED) {
					if (_path.contains(t)) {
						_destTile = t;
						
						_gameScreen.panToCoordinate(_selectedTile.getX(), _selectedTile.getY());
						move(_selectedCharacter, _selectedTile, _path);
						
						_cacheMovementRange = _selectedMovementRange;
						clearMovementRange();
						
						_cacheAttackRange = _characterAttackRange;
						setPlayerAttackRange(_destTile, _selectedCharacter, false);
						
						_cachePath = _path;
						_cacheCost = _pathCost;
						_cacheDestTile = _destTile;
						clearPath();
					}
				}
				else if (getState() == State.CHARACTER_OPTION_MENU) {
					if (_characterAttackRange.contains(t) && isEnemy(t.getOccupant())) {
						clearPath();
						clearMovementRange();
						clearPlayerAttackRange();
						_pool.removeCharacter(_selectedCharacter);
						_hasMoved.put(_selectedCharacter, true);
						attack(_destTile, t, _selectedTile.gridDistanceToTile(t));
						System.out.println(getState());
					}
				}
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
			if (getState() == State.START)
				clear();
			else if (getState() == State.CHARACTER_SELECTED)
				clear();
			else if (getState() == State.CHARACTER_MOVING || getState() == State.CHARACTER_OPTION_MENU) {
				_selectedCharacter.stopMotion();
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
				_locations.put(_selectedCharacter, _selectedTile);
				
				removeAllMenus();
				
				setState(State.CHARACTER_SELECTED);
				_gameScreen.panToCoordinate(_selectedTile.getX(), _selectedTile.getY());
			}
			else if (getState() == State.SELECTING_ITEM) {
				removeMenu();
				setState(State.CHARACTER_OPTION_MENU);
			}
			else if (getState() == State.ITEM_SELECTED) {
				removeMenu();
				setState(State.SELECTING_ITEM);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
		if (t != null) {
			
			if (getState() == State.CHARACTER_SELECTED) {	//selected character and tile are not null
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

		if (!_menus.isEmpty() && _menus.peek().contains(e.getPoint())) {
			System.out.println("HLO");
			_menuDraggedx = e.getX();
			_menuDraggedy = e.getY();
			_draggingMenu = true;
		}
	//	else
	//		_draggingMenu = false;
	}
	
	@Override
	/**
	 * 
	 */
	public void mouseDragged(MouseEvent e) {
		System.out.println(_draggingMenu);
		if (!_draggingMenu)
			super.mouseDragged(e);
		else {
			if (_menus.peek() != null)
				_menus.peek().updateLocation(e.getX() - _menuDraggedx, e.getY() - _menuDraggedy);
		/*	if (_optionWindow != null)
				_optionWindow.setLocation(e.getX() - _menuDraggedx, e.getY() - _menuDraggedy);
			if (_itemWindow != null)
				_itemWindow.setLocation(e.getX() - _menuDraggedx, e.getY() - _menuDraggedy);	*/
			_menuDraggedx = e.getX();
			_menuDraggedy = e.getY();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (getState() == State.START) {
			if (e.getKeyCode() == _dt.getLeftKey()) {
				Character c = nextUnit();
				System.out.println(c);
				_gameScreen.panToCoordinate(c.getX(), c.getY());
			}
			else if (e.getKeyCode() == _dt.getRightKey()) {
				Character c = nextUnit();
				System.out.println(c);
				_gameScreen.panToCoordinate(c.getX(), c.getY());
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
		super.release();
		
		System.out.println(getState());
		if (getState() != State.ATTACKING) {
			new Thread(new SlideTimer(_playerPhase,-1050)).start();
			clear();
		}
	}

	@Override
	/**
	 * begins the player's turn
	 */
	public void take() {
		super.take();
		
		if (getState() != State.ATTACKING) {
			_gameScreen.panToCoordinate(_gameScreen.getMainChar().getX(),
					_gameScreen.getMainChar().getY());
			_playerPhase.setLocation(1050,_playerPhase.getY());
			new Thread(new SlideTimer(_playerPhase,250)).start();
			
			initialize();
		}
		else {
			clear();
			
			if (_pool.isEmpty()) {
				_pool.setInUse(false);
				_pool = null;
				_gameScreen.popControl();
			}
		}
	}

	@Override
	/**
	 * pans the camera to focus on the given character
	 */
	public void getCharacterFromPool(Character c) {
		_gameScreen.panToCoordinate(c.getX() - _pool.getWidth()/2, c.getY());
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
	public void unitPoolClicked(int type) {
		
	}
	
	@Override
	/**
	 * 
	 */
	public void initialize() {
		_pool = new UnitPool(_dt, _gameScreen, this, _units);
		_pool.setInUse(true);
	}
	
	@Override
	/**
	 * used at the end of the player's turn; clears the unit pool and releases control
	 */
	public boolean finish() {
		if (!_finalized && getState() == State.START) {
			_finalized = true;
			_pool.setInUse(false);
			_pool = null;
			_gameScreen.popControl();
			_finalized = false;
			return true;
		}
		else
			return false;
	}
	
	@Override
	public void characterWait() {
		super.characterWait();
		_pool.removeCharacter(_selectedCharacter);
		clear();
		
		if (_pool.isEmpty()) {
			_pool.setInUse(false);
			_pool = null;
			_gameScreen.popControl();
		}
	}

	/**
	 * sends an action to the controller
	 * @param action the action to send
	 */
	public void pushAction(ActionType action) {
		assert(getState() == State.CHARACTER_OPTION_MENU);
		
		if (action == ActionType.WAIT)
			characterWait();
		else if (action == ActionType.ITEM) {
		/*	_itemWindow = new ItemWindow(_gameScreen, _dt, _selectedCharacter, this);
			_itemWindow.setLocation(_optionWindow.getX(), _optionWindow.getY());
			
			_optionWindow.removeFromDrawingQueue();
			_itemWindow.addToDrawingQueue();	*/
			addMenu(new ItemWindow(_gameScreen, _dt, _selectedCharacter, this),
					_menus.peek().getX(), _menus.peek().getY());
			
			setState(State.SELECTING_ITEM);
		}
		else if (action == ActionType.SPECIAL) {
			
		}
	}
	
	public void returnToOptionMenu() {
		removeAllMenus();
		addMenu(new CombatOptionWindow(_dt, _gameScreen, false,
				_selectedCharacter.getInventory().size() != 0, false, this),
				_destTile.getX() + Tile.TILE_SIZE,
				_destTile.getY() - Tile.TILE_SIZE);
		setState(State.CHARACTER_OPTION_MENU);
	}

	/**
	 * opens an item menu corresponding to a given item
	 * @param i the item whose menu should be opened
	 */
	public void openItemMenu(Item i) {
		System.out.println(i);
		addMenu(new ItemActionWindow(i, _selectedCharacter, _gameScreen, _dt, this),
				_menus.peek().getX(), _menus.peek().getY());
		setState(State.ITEM_SELECTED);
	}
}