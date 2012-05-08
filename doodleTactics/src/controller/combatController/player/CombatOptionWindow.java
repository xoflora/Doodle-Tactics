package controller.combatController.player;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JPanel;

import controller.combatController.ActionType;

import main.DoodleTactics;
import main.GameScreen;
import graphics.MenuItem;
import graphics.Rectangle;

/**
 * selection window for the series of options that a unit has after moving
 * @author rroelke
 *
 */
public class CombatOptionWindow extends MenuItem implements CombatMenu {
	
	private static final String MENU_IMAGE_PATH = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/combat_menu_background.png";
	private static final String SPECIAL_IMAGE = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/special.png";
	private static final String SPECIAL_HOVER = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/special_hovered.png";
	private static final String EQUIP_IMAGE = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/equip.png";
	private static final String EQUIP_HOVER = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/equip_hovered.png";
	private static final String ITEM_IMAGE = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/item.png";
	private static final String ITEM_HOVER = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/item_hovered.png";
	private static final String WAIT_IMAGE = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/wait.png";
	private static final String WAIT_HOVER = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/wait_hovered.png";
	
	private static final int MENU_PRIORITY = 3;
	private static final int OPTION_PRIORITY = 50;
	
	private static final int HORZ_BUFFER = 4;
	private static final int VERT_BUFFER = 3;
	
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
	
	public CombatOptionWindow(DoodleTactics dt, GameScreen container, boolean special, boolean equip,
			boolean item, PlayerCombatController source) {
		
		super(container, dt.importImage(MENU_IMAGE_PATH),
				dt.importImage(MENU_IMAGE_PATH), dt, MENU_PRIORITY);
		_options = new ArrayList<CombatOption>();
		
		if (special) {
			_options.add(new CombatOption(container, dt.importImage(SPECIAL_IMAGE),
					dt.importImage(SPECIAL_HOVER), dt, source, ActionType.SPECIAL));
		}
		if (equip) {
			_options.add(new CombatOption(container, dt.importImage(EQUIP_IMAGE),
					dt.importImage(EQUIP_HOVER), dt, source, ActionType.EQUIP));
		}
		if (item) {
			_options.add(new CombatOption(container, dt.importImage(ITEM_IMAGE),
					dt.importImage(ITEM_HOVER), dt, source, ActionType.ITEM));
		}
		
		_options.add(new CombatOption(container, dt.importImage(WAIT_IMAGE),
				dt.importImage(WAIT_HOVER), dt, source, ActionType.WAIT));
		
		System.out.println("COMBAT OPTION MENU " + _options.size());
		
		_gameScreen = container;
		
	//	setSize(getWidth(), _options.get(0).getHeight() + 2*VERT_BUFFER);
	}
	
	/**
	 * adds the option window and its components to the game screen drawing queue
	 */
	public void addToDrawingQueue() {
		_gameScreen.addMenuItem(this);
		
		double y = getY() + VERT_BUFFER;
		for (MenuItem m : _options) {
			_gameScreen.addMenuItem(m);
			y += m.getImage().getHeight() + VERT_BUFFER;
			m.setVisible(true);
		}
		
		setVisible(true);
	}
	
	/**
	 * removes the option window and its components from the game screen drawing queue
	 */
	public void removeFromDrawingQueue() {
		setVisible(false);
		for (MenuItem m : _options) {
			_gameScreen.removeMenuItem(m);
			m.setVisible(false);
		}
		_gameScreen.removeMenuItem(this);
	}
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		
		double menuY = y + VERT_BUFFER;
		for (MenuItem m : _options) {
			m.setLocation(x + HORZ_BUFFER, menuY);
			menuY += m.getImage().getHeight() + VERT_BUFFER;
		}
		setSize(getWidth(), Math.max(menuY - y, _options.get(0).getHeight() + 2*VERT_BUFFER));
	}
}
