package controller.combatController;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
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
public class UnitPool extends MenuItem {
	
	private static final int SIDEBAR_WIDTH = Tile.TILE_SIZE;
	private static final int SIDEBAR_HEIGHT = 160;
	private static final int CHARACTER_OFFSET_X = Tile.TILE_SIZE/8;
	private static final int CHARACTER_OFFSET_Y = Tile.TILE_SIZE/8;
	
	private static final int DEFAULT_X = 0;
	private static final int DEFAULT_Y = 0;
	private static final int DONE_X = 0;
	private static final int DONE_Y = 160;
	
	private static final String IMAGE_PATH = "src/graphics/characters/pokeball.png";
	private static final String DONE_DEFAULT_IMG = "src/graphics/characters/pokeball.png";
	private static final String DONE_HOVER_IMG = "src/graphics/characters/pokeball.png";
	
	/**
	 * corresponds to a character button in the unit pool
	 * @author rroelke
	 *
	 */
	private class CharacterSelect extends MenuItem {
		
		private Character _c;
		
		public CharacterSelect(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt, Character c) {
			super(container, defltPath, hoveredPath, dt);
			setVisible(true);
		}
		
		@Override
		public void activate() {
			_source.getCharacterFromPool(_c);
		}
	}
	
	/**
	 * menu item indicating that the unit pool is no longer required
	 * @author rroelke
	 *
	 */
	private class DoneButton extends MenuItem {
		public DoneButton(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt) {
			super(container, defltPath, hoveredPath, dt);
			setVisible(true);
		}
		
		@Override
		public void activate() {
			_source.finalize();
			System.out.println("hi");
		}
	}
	
	private PoolDependent _source;
	private HashMap<Character, CharacterSelect> _unitPool;
	private JPanel _container;
	private DoneButton _done;
	
	public UnitPool(DoodleTactics dt, JPanel container, PoolDependent source, List<Character> units) 
			throws IOException {
		super(container, ImageIO.read(new File(IMAGE_PATH)), ImageIO.read(new File(IMAGE_PATH)), dt);
		
		_dt = dt;
		_source = source;
		_unitPool = new HashMap<Character, CharacterSelect>();
		_container = container;
		
		for (Character unit : units)
			addCharacter(unit);
		
		_done.setLocation(DONE_X, DONE_Y);
		_done.setVisible(true);
		
		setLocation(DEFAULT_X, DEFAULT_Y);
		
		setVisible(true);
	}
	
	public void removeCharacter(Character c) {
		_unitPool.remove(c);
	}
	
	public void addCharacter(Character c) {
	/*	System.out.println();
		System.out.println(c == null);
		System.out.println(c.getDownImage() == null);
		System.out.println(c.getRightImage() == null);
		System.out.println(_dt == null);		*/
		_unitPool.put(c, new CharacterSelect(_container, c.getDownImage(), c.getRightImage(), _dt, c));
	}
	
	@Override
	public void paint(Graphics2D brush, BufferedImage img) {
		System.out.println("PAINTING UNIT POOL");
		super.paint(brush, img);
		
		_done.paint(brush, _done.getImage());
		
		int x = DEFAULT_X + CHARACTER_OFFSET_X;
		int y = DEFAULT_Y + CHARACTER_OFFSET_Y;
		
		for (Character c : _unitPool.keySet()) {
			CharacterSelect draw = _unitPool.get(c);
			draw.setLocation(x, y);
			System.out.println(draw.getImage());
			draw.paint(brush, draw.getImage());
			
			y += Tile.TILE_SIZE;
			
			System.out.println("BLEH");
		}
	}
}
