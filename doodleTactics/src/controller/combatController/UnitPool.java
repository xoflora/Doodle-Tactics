package controller.combatController;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;

import graphics.MenuItem;
import graphics.Rectangle;
import character.Character;

/**
 * a unit pool is a graphical interface that allows players to select units
 * @author rroelke
 *
 */
public class UnitPool extends MenuItem {
	
	private static final int SIDEBAR_WIDTH = Tile.TILE_SIZE;
	private static final int SIDEBAR_HEIGHT = 160;
	
	private static final int CHARACTER_OFFSET_X = 40;
	private static final int CHARACTER_OFFSET_Y = 120;
	private static final int OFFSET_BETWEEN_CHARACTERS = Tile.TILE_SIZE + 4;
	
	private static final int DEFAULT_X = 0;
	private static final int DEFAULT_Y = 0;
	private static final int DONE_X = 11;
	private static final int DONE_Y = 746;
	
	public static final int SIDEBAR_PRIORITY = 1;
	public static final int CHARACTER_SELECT_PRIORITY = 2;
	public static final int DONE_BUTTON_PRIORITY = 2;
	
	private static final String IMAGE_PATH = "src/graphics/menu/sidebar.png";
	private static final String DONE_DEFAULT_IMG = "src/graphics/menu/done.png";
	private static final String DONE_HOVER_IMG = "src/graphics/menu/done_hovered.png";
	
	/**
	 * corresponds to a character button in the unit pool
	 * @author rroelke
	 *
	 */
	private class CharacterSelect extends MenuItem {
		
		private Character _c;
		
		public CharacterSelect(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt, Character c) {
			super(container, defltPath, hoveredPath, dt, CHARACTER_SELECT_PRIORITY);
			setVisible(true);
		}
		
		@Override
		public void activate(int type) {
			_source.getCharacterFromPool(_c);
		}
	}
	
	/**
	 * menu item indicating that the unit pool is no longer required
	 * @author rroelke
	 */
	private class DoneButton extends MenuItem {
		public DoneButton(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt) {
			super(container, defltPath, hoveredPath, dt, DONE_BUTTON_PRIORITY);
			setVisible(true);
		}
		
		@Override
		public void activate(int type) {
			_source.finalize();
		}
	}
	
	private PoolDependent _source;
	private HashMap<Character, CharacterSelect> _unitPool;
	private DoneButton _done;
	private GameScreen _gameScreen;
	
	private boolean _inUse;
	
	public UnitPool(DoodleTactics dt, GameScreen screen, PoolDependent source, List<Character> units) 
			throws IOException {
		super(screen, ImageIO.read(new File(IMAGE_PATH)), ImageIO.read(new File(IMAGE_PATH)), dt, SIDEBAR_PRIORITY);
		
		_dt = dt;
		_source = source;
		_unitPool = new HashMap<Character, CharacterSelect>();
		_gameScreen = screen;
		
		for (Character unit : units)
			addCharacter(unit);
		try {
			_done = new DoneButton(_gameScreen, ImageIO.read(new File(DONE_DEFAULT_IMG)),
						ImageIO.read(new File(DONE_HOVER_IMG)), _dt);
			_done.setLocation(DONE_X, DONE_Y);
			_done.setVisible(true);
		} catch(IOException e) {
			
		}
		
		setLocation(DEFAULT_X, DEFAULT_Y);
		
		_inUse = false;
	}
	
	/**
	 * removes a unit from the unit pool
	 * @param c the unit to remove
	 * @return whether or not the unit was successfully removed
	 */
	public boolean removeCharacter(Character c) {
		CharacterSelect s = _unitPool.remove(c);
		if (_inUse && s != null)
			return _gameScreen.removeMenuItem(s);
		return s != null;
	}
	
	/**
	 * adds a unit to the unit pool
	 * @param c the unit to add
	 */
	public void addCharacter(Character c) {
		CharacterSelect s = new CharacterSelect(_gameScreen, c.getDownImage(), c.getRightImage(), _dt, c);
		_unitPool.put(c, s);
		
		if (_inUse)
			_gameScreen.addMenuItem(s);
	}
	
	@Override
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);
		
		_done.paint(brush, _done.getImage());
		
		int x = DEFAULT_X + CHARACTER_OFFSET_X;
		int y = DEFAULT_Y + CHARACTER_OFFSET_Y;
		
		for (Character c : _unitPool.keySet()) {
			CharacterSelect draw = _unitPool.get(c);
			draw.setLocation(x, y);
			draw.paint(brush, draw.getImage());
			
			y += OFFSET_BETWEEN_CHARACTERS;
		}
	}
	
	/**
	 * sets whether or not the pool should be used
	 * @param b whether or not the pool should be used
	 */
	public void setInUse(boolean b) {
		if (b && !_inUse) {
			_gameScreen.addMenuItem(this);
			_gameScreen.addMenuItem(_done);
			for (Character c : _unitPool.keySet())
				_gameScreen.addMenuItem(_unitPool.get(c));
			
			_inUse = true;
			setVisible(true);
		}
		else if (!b && _inUse){
			for (Character c : _unitPool.keySet())
				_gameScreen.removeMenuItem(_unitPool.get(c));
			_gameScreen.removeMenuItem(_done);
			_gameScreen.removeMenuItem(this);
			
			_inUse = false;
			setVisible(false);
		}
	}
}
