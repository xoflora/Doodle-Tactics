package controller.combatController;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.GameScreen;
import graphics.MenuItem;
import graphics.Rectangle;

/**
 * selection window for the series of options that a unit has after moving
 * @author rroelke
 *
 */
public class CombatOptionWindow extends MenuItem {
	
	private static final String MENU_IMAGE_PATH = "src/graphics/menu/combat_menu_background.png";
//	private static final String ATTACK_IMAGE = "src/graphics/menu/attack.png";
//	private static final String ATTACK_HOVER = "src/graphics/menu/attack_hovered.png";
	private static final String SPECIAL_IMAGE = "src/graphics/menu/special.png";
	private static final String SPECIAL_HOVER = "src/graphics/menu/special_hovered.png";
	private static final String ITEM_IMAGE = "src/graphics/menu/item.png";
	private static final String ITEM_HOVER = "src/graphics/menu/item_hovered.png";
	private static final String TALK_IMAGE = "";
	private static final String TALK_HOVER = "";
	private static final String WAIT_IMAGE = "src/graphics/menu/wait.png";
	private static final String WAIT_HOVER = "src/graphics/menu/wait_hovered.png";
	
	private static final int MENU_PRIORITY = 3;
	private static final int OPTION_PRIORITY = 50;
	
	private class CombatOption extends MenuItem {
		
		private PlayerCombatController _source;
		private ActionType _action;

		public CombatOption(JPanel container, BufferedImage defltPath, BufferedImage hoveredPath,
				DoodleTactics dt, PlayerCombatController source, ActionType action) {
			super(container, defltPath, hoveredPath, dt, OPTION_PRIORITY);
			_source = source;
			_action = action;
		}
		
		@Override
		public void activate(int type) {
			if (type != MouseEvent.BUTTON1)
				return;
			_source.pushAction(_action);
		}
	}
	
	private GameScreen _gameScreen;
	private List<CombatOption> _options;
	
	public CombatOptionWindow(DoodleTactics dt, GameScreen container, boolean special, boolean item, boolean talk,
			PlayerCombatController source) throws IOException {
		
		super(container, dt.importImage(MENU_IMAGE_PATH),
				dt.importImage(MENU_IMAGE_PATH), dt, MENU_PRIORITY);
		_options = new ArrayList<CombatOption>();
		
		if (special) {
			_options.add(new CombatOption(container, dt.importImage(SPECIAL_IMAGE),
					dt.importImage(SPECIAL_HOVER), dt, source, ActionType.SPECIAL));
		}
		if (item) {
			_options.add(new CombatOption(container, dt.importImage(ITEM_IMAGE),
					dt.importImage(ITEM_HOVER), dt, source, ActionType.ITEM));
		}
		if (talk) {
			_options.add(new CombatOption(container, dt.importImage(TALK_IMAGE),
					dt.importImage(TALK_HOVER), dt, source, ActionType.TALK));
		}
		_options.add(new CombatOption(container, dt.importImage(WAIT_IMAGE),
				dt.importImage(WAIT_HOVER), dt, source, ActionType.WAIT));
		
		_gameScreen = container;
	}
	
	public void addToDrawingQueue() {
		_gameScreen.addMenuItem(this);
		
		double y = getY();
		for (MenuItem m : _options) {
			_gameScreen.addMenuItem(m);
			m.setLocation(getX(), y);
			y += m.getImage().getHeight();
			m.setVisible(true);
		}
		
		setVisible(true);
		setSize(getImage().getWidth(), y - getY());
	}
	
	public void removeFromDrawingQueue() {
		for (MenuItem m : _options) {
			_gameScreen.removeMenuItem(m);
			m.setVisible(false);
		}
		_gameScreen.removeMenuItem(this);
	}
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(getX() + x, getY() + y);
		for (MenuItem m : _options)
			m.setLocation(m.getX() + x, m.getY() + y);
	}
}
