package controller.combatController.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JPanel;

import controller.UnitStatWindow;


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
	private static final int TEXT_X = 112;

	public static final int SIDEBAR_PRIORITY = 0;
	public static final int CHARACTER_SELECT_PRIORITY = 1;
	public static final int DONE_BUTTON_PRIORITY = 1;

	public static final int SELECT_BUTTON = MouseEvent.BUTTON1;
	public static final int ALT_BUTTON = MouseEvent.BUTTON3;

	private static final String IMAGE_PATH = "src/graphics/menu/combatMenu/sidebar.png";
	private static final String DONE_DEFAULT_IMG = "src/graphics/menu/combatMenu/done.png";
	private static final String DONE_HOVER_IMG = "src/graphics/menu/combatMenu/done_hovered.png";

	/**
	 * corresponds to a character button in the unit pool
	 * @author rroelke
	 *
	 */
	private class CharacterSelect extends MenuItem {
		
		private static final int LEFT_MARGIN = 7;
		private static final int TOP_MARGIN = 2;
		private static final int BUFFER = 12;

		private Character _c;
		private boolean _drawStats;

		public CharacterSelect(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt, Character c) {
			super(container, defltPath, hoveredPath, dt, CHARACTER_SELECT_PRIORITY);
			setVisible(true);
			_c = c;
			_drawStats = false;
		}
		
		@Override
		public void setDefault() {
			super.setDefault();
			_drawStats = false;
		}
		
		@Override
		public void setHovered() {
			super.setHovered();
			_drawStats = true;
		}

		@Override
		public void activate(int type) {
			if (type == SELECT_BUTTON)
				_source.getCharacterFromPool(_c);
			else if (type == ALT_BUTTON)
				_source.alternateAction(_c);
		}
		
		@Override
		public void paint(Graphics2D brush, BufferedImage img) {
			super.paint(brush, img);

			if (_drawStats) {
				int y = (int)getY() + TOP_MARGIN;
				
				brush.setRenderingHint(
						RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
				brush.setFont(new Font("Arial", Font.BOLD, 14));
				brush.setColor(new Color(0,0,1));
				brush.drawString(_c.getName(), TEXT_X, y + BUFFER);
				brush.drawString("HP: " + _c.getHP() + "/" + _c.getBaseStats()[7] +
						", Lvl " + _c.getLevel(), TEXT_X, y + BUFFER*2);
				brush.drawString("Atk: " + _c.getBaseStats()[0] + ", Def: "
						+ _c.getBaseStats()[1], TEXT_X, y + BUFFER*3);
				brush.drawString("Range: " + _c.getMovementRange(), TEXT_X, y + BUFFER*4);
			}
		}
	}

	/**
	 * menu item indicating that the unit pool is no longer required
	 * @author rroelke
	 */
	private class DoneButton extends MenuItem {

		private boolean _complete;

		public DoneButton(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt) {
			super(container, defltPath, hoveredPath, dt, DONE_BUTTON_PRIORITY);
			_complete = false;
			setVisible(true);
		}

		@Override
		public void activate(int type) {
			synchronized(this) {
				if (!_complete) {
					_complete = true;
					_source.finalize();
				}
			}
		}
	}

	private PoolDependent _source;
	private HashMap<Character, CharacterSelect> _unitPool;
	private DoneButton _done;
	private GameScreen _gameScreen;

	private boolean _inUse;
	private int _numUnits;

	public UnitPool(DoodleTactics dt, GameScreen screen, PoolDependent source, List<Character> units) 
			throws IOException {
		super(screen, dt.importImage(IMAGE_PATH), dt.importImage(IMAGE_PATH), dt, SIDEBAR_PRIORITY);

		_dt = dt;
		_source = source;
		_unitPool = new HashMap<Character, CharacterSelect>();
		_gameScreen = screen;
		_numUnits = 0;
		_inUse = false;

		for (Character unit : units)
			addCharacter(unit);
		_done = new DoneButton(_gameScreen, dt.importImage(DONE_DEFAULT_IMG),
				dt.importImage(DONE_HOVER_IMG), _dt);
		_done.setLocation(DONE_X, DONE_Y);
		_done.setVisible(true);

		setLocation(DEFAULT_X, DEFAULT_Y);
	}

	/**
	 * removes a unit from the unit pool
	 * @param c the unit to remove
	 * @return whether or not the unit was successfully removed
	 */
	public boolean removeCharacter(Character c) {
		CharacterSelect s = _unitPool.remove(c);
		if (s != null) {
			_numUnits--;
			if (_inUse)
				return _gameScreen.removeMenuItem(s);
			return true;
		}
		return false;
	}

	/**
	 * adds a unit to the unit pool
	 * @param c the unit to add
	 */
	public void addCharacter(Character c) {
		CharacterSelect s = new CharacterSelect(_gameScreen, c.getDownImage(), c.getRightImage(), _dt, c);
		_unitPool.put(c, s);
		_numUnits++;

		if (_inUse)
			_gameScreen.addMenuItem(s);
	}

	@Override
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);

		int x = DEFAULT_X + CHARACTER_OFFSET_X;
		int y = DEFAULT_Y + CHARACTER_OFFSET_Y;

		for (Character c : _unitPool.keySet()) {
			CharacterSelect draw = _unitPool.get(c);
			draw.setLocation(x, y);

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
	
	/**
	 * @return the number of units in the character pool
	 */
	public int getNumUnits() {
		return _numUnits;
	}
	
	/**
	 * @return whether there are no units remaining in the pool
	 */
	public boolean isEmpty() {
		return _numUnits == 0;
	}
	
	@Override
	/**
	 * informs the source that the pool has been clicked on
	 */
	public void activate(int type) {
		_source.unitPoolClicked(type);
	}
}
