package controller.combatController;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;

import main.DoodleTactics;

import graphics.MenuItem;
import graphics.Rectangle;
import character.Character;

/**
 * a unit pool is a graphical interface that allows players to select units
 * @author rroelke
 *
 */
public class UnitPool extends Rectangle {
	
	
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
		
		for (Character c : _unitPool.keySet()) {
			//set x and y coordinates
			_unitPool.get(c).paint(brush);
		}
	}

}
