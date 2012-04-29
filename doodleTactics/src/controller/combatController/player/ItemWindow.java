package controller.combatController.player;

import items.Item;
import character.Character;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.GameScreen;
import graphics.MenuItem;

public class ItemWindow extends MenuItem {
	
	private static final int MENU_PRIORITY = 3;
	private static final int OPTION_PRIORITY = 4;
	
	private static final int DEFAULT_HEIGHT = 76;
	private static final int HORZ_BUFFER = 6;
	private static final int VERT_BUFFER = 2;
	
	private static final String MENU_IMAGE = "src/graphics/menu/combat_menu_background.png";
	
	
	private class ItemOption extends MenuItem {
		
		private boolean _showDescription;
		private Item _item;

		public ItemOption(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, DoodleTactics dt, Item item) {
			super(container, defltPath, hoveredPath, dt, OPTION_PRIORITY);
			_showDescription = false;
			_item = item;
		}
		
		@Override
		public void setDefault() {
			_showDescription = false;
		}
		
		@Override
		public void setHovered() {
			_showDescription = true;
		}
		
		@Override
		public void paint(Graphics2D brush, BufferedImage img) {
			super.paint(brush, img);
			
			if (_showDescription) {
				brush.setRenderingHint(
						RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
				brush.setFont(new Font("M",Font.BOLD,25));
				brush.setColor(new Color(0,0,1));
				brush.drawString(_item.getDescription(), 450,665);
			}
		}
		
		@Override
		public void activate(int type) {
			
		}
	}
	
	private List<ItemOption> _items;
	private PlayerCombatController _source;
	private GameScreen _gameScreen;

	public ItemWindow(GameScreen container, DoodleTactics dt, Character c, PlayerCombatController source) {
		super(container, dt.importImage(MENU_IMAGE), dt.importImage(MENU_IMAGE), dt, MENU_PRIORITY);
		_items = new ArrayList<ItemOption>();
		_source = source;
		_gameScreen = container;
		
		HashMap<Integer, Item> inventory = c.getInventory();
		for (Integer i : inventory.keySet()) {
			Item item = inventory.get(i);
			_items.add(new ItemOption(container, item.getImage(), item.getImage(), _dt, item));
		}
	}
	
	/**
	 * adds the item window and all of its components to the game screen
	 */
	public void addToDrawingQueue() {
		_gameScreen.addMenuItem(this);
		double x = getX() + HORZ_BUFFER;
		double y = 0;
		for (ItemOption i : _items) {
			_gameScreen.addMenuItem(i);
			
			i.setLocation(x, getY() + VERT_BUFFER);
			i.setSize(i.getImage().getWidth(), i.getImage().getHeight());
			i.setVisible(true);
			
			x += i.getImage().getWidth() + HORZ_BUFFER;
			if (i.getImage().getHeight() > y)
				y = i.getImage().getHeight();
		}
		setSize(x - getX(), y);
	//	setWidth(x - getX());
	//	getImage().
		setVisible(true);
	}
	
	/**
	 * removes the item window and all of its components from the game screen
	 */
	public void removeFromDrawingQueue() {
		setVisible(false);
		for (ItemOption i : _items) {
			i.setVisible(false);
			_gameScreen.removeMenuItem(i);
		}
		_gameScreen.removeMenuItem(this);
	}
	
	public void setLocation(double x, double y) {
		super.setLocation(getX() + x, getY() + y);
		for (MenuItem m : _items)
			m.setLocation(m.getX() + x, m.getY() + y);
	}
}
