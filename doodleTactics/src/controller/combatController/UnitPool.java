package controller.combatController;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;

import main.DoodleTactics;
import map.Tile;

import graphics.MenuItem;
import graphics.Rectangle;
import character.Character;

/**
 * a unit pool is a graphical interface that allows players to select units
 * @author rroelke
 *
 */
public class UnitPool extends Rectangle {
	
	private static final int DEFAULT_X = 0;
	private static final int DEFAULT_Y = 0;
	private static final int SIDEBAR_WIDTH = Tile.TILE_SIZE;
	private static final int SIDEBAR_HEIGHT = 160;
	private static final int CHARACTER_OFFSET_X = Tile.TILE_SIZE/8;
	private static final int CHARACTER_OFFSET_Y = Tile.TILE_SIZE/8;
	
	private static final Color DEFAULT_COLOR =
	//	new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), 0.7f);
		new Color(0, 0, 0, 0.5f);
	
	private class CharacterSelect extends MenuItem {
		
		private Character _c;
		
		public CharacterSelect(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt, Character c) {
			super(container, defltPath, hoveredPath, dt);
		}
		
		@Override
		public void activate() {
			_source.getCharacterFromPool(_c);
		}
	}
	
	private DoodleTactics _dt;
	private PoolDependent _source;
	private HashMap<Character, CharacterSelect> _unitPool;
	private JPanel _container;
	
	public UnitPool(DoodleTactics dt, JPanel container, PoolDependent source, List<Character> units) {
		super(container);
		
		_dt = dt;
		_container = container;
		_source = source;
		_unitPool = new HashMap<Character, CharacterSelect>();
		
		for (Character unit : units)
			addCharacter(unit);
		
		setLocation(DEFAULT_X, DEFAULT_Y);
		setSize(SIDEBAR_WIDTH, SIDEBAR_HEIGHT);
		setColor(DEFAULT_COLOR);
	}

	public UnitPool(JPanel container, int priority, List<Character> units) {
		super(container, priority);
	}
	
	public void removeCharacter(Character c) {
		_unitPool.remove(c);
	}
	
	public void addCharacter(Character c) {
		_unitPool.put(c, new CharacterSelect(_container, c.getDownImage(), c.getRightImage(),
				_dt, c));
	}
	
	@Override
	public void paint(Graphics2D brush) {
		super.paint(brush);
		
		int x = DEFAULT_X + CHARACTER_OFFSET_X;
		int y = DEFAULT_Y + CHARACTER_OFFSET_Y;
		for (Character c : _unitPool.keySet()) {
			CharacterSelect draw = _unitPool.get(c);
			draw.setLocation(x, y);
			draw.paint(brush);
			
			y += Tile.TILE_SIZE;
		}
	}

	@Override
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}
}
