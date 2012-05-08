package controller.combatController.player;

import graphics.MenuItem;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import controller.combatController.ActionType;

import main.DoodleTactics;
import main.GameScreen;

public class CharacterSelectedOptionMenu extends MenuItem implements CombatMenu {
	
	private static final String MENU_IMAGE = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/combat_menu_background.png";
	private static final String ATTACK_IMAGE = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/attack.png";
	private static final String ATTACK_HOVERED = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/attack_hovered.png";
	private static final String TRADE_IMAGE = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/trade.png";
	private static final String TRADE_HOVERED = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/trade_hovered.png";
	private static final String TALK_IMAGE = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/equip.png";
	private static final String TALK_HOVERED = DoodleTactics.IMAGE_SOURCE_PATH + "menu/combatMenu/equip_hovered.png";
	
	private static final int MENU_PRIORITY = 3;
	private static final int OPTION_PRIORITY = 5;
	
	private static final int HORZ_BUFFER = 4;
	private static final int VERT_BUFFER = 3;
	
	private class Option extends MenuItem {

		private ActionType _type;
		
		public Option(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt, ActionType type) {
			super(container, defltPath, hoveredPath, dt, OPTION_PRIORITY);
			_type = type;
		}
		
		@Override
		public void activate(int type) {
			_source.pushCharacterSelectAction(_type);
		}
	}

	private GameScreen _gameScreen;
	private PlayerCombatController _source;
	private List<Option> _options;
	
	public CharacterSelectedOptionMenu(GameScreen container, DoodleTactics dt, PlayerCombatController source,
			boolean attack, boolean trade, boolean talk) {
		super(container, dt.importImage(MENU_IMAGE), dt.importImage(MENU_IMAGE), dt, MENU_PRIORITY);
		_gameScreen = container;
		_source = source;
		_options = new ArrayList<Option>();
		
		if (attack) {
			_options.add(new Option(container, dt.importImage(ATTACK_IMAGE),
					dt.importImage(ATTACK_HOVERED), dt, ActionType.ATTACK));
		}
		if (trade) {
			_options.add(new Option(container, dt.importImage(TRADE_IMAGE),
					dt.importImage(TRADE_HOVERED), dt, ActionType.TRADE));
		}
		if (talk) {
			_options.add(new Option(container, dt.importImage(TALK_IMAGE),
					dt.importImage(TALK_HOVERED), dt, ActionType.TALK));
		}
	}

	@Override
	public void addToDrawingQueue() {
		setVisible(true);
		_gameScreen.addMenuItem(this);
		
		for (Option m : _options) {
			m.setVisible(true);
			_gameScreen.addMenuItem(m);
		}
	}

	@Override
	public void removeFromDrawingQueue() {
		for (Option m : _options) {
			_gameScreen.removeMenuItem(m);
			m.setVisible(false);
		}
		
		_gameScreen.removeMenuItem(this);
		setVisible(false);
	}
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		
		double menuY = y + VERT_BUFFER;
		for (Option m : _options) {
			m.setLocation(x + HORZ_BUFFER, menuY);
			menuY += m.getImage().getHeight() + VERT_BUFFER;
		}
		
		setSize(getImage().getWidth(), menuY - y);
	}
}
