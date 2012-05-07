package controller.combatController.player;

import character.Character;

import items.Equipment;
import items.Item;
import items.ItemException;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import controller.combatController.CombatController;

import main.DoodleTactics;
import main.GameScreen;
import graphics.MenuItem;
import graphics.Rectangle;

public class ItemActionWindow extends MenuItem implements CombatMenu {
	
	private static final String WINDOW_IMAGE = "src/graphics/menu/combatMenu/combat_menu_background.png";
	private static final String EQUIP_IMAGE = "src/graphics/menu/combatMenu/equip.png";
	private static final String EQUIP_HOVERED = "src/graphics/menu/combatMenu/equip_hovered.png";
	private static final String USE_IMAGE = "src/graphics/menu/combatMenu/use.png";
	private static final String USE_HOVERED = "src/graphics/menu/combatMenu/use_hovered.png";
	private static final String DISCARD_IMAGE = "src/graphics/menu/combatMenu/discard.png";
	private static final String DISCARD_HOVERED = "src/graphics/menu/combatMenu/discard_hovered.png";
	
	private static final double VERT_BUFFER = 3;
	private static final double HORZ_BUFFER = 4;
	
	private static final int OPTION_PRIORITY = 5;
	private static final int MENU_PRIORITY = 3;
	
	private boolean _isEquip;
	private Item _item;
	private Equipment _equip;
	private List<MenuItem> _options;
	private MenuItem _itemImage;
	
	private GameScreen _gameScreen;

	@SuppressWarnings("serial")
	public ItemActionWindow(Item item, final Character c, GameScreen container,
			final DoodleTactics dt, final PlayerCombatController source) {
		super(container, dt.importImage(WINDOW_IMAGE), dt.importImage(WINDOW_IMAGE), dt, MENU_PRIORITY);
		_gameScreen = container;
		_item = item;
		
		_itemImage = new MenuItem(_gameScreen, _item.getImage(), _item.getImage(), dt);
		
		_options = new ArrayList<MenuItem>();
		if (_item.isEquip()) {
			_isEquip = true;
			_equip = (Equipment) item;
			_item = null;
			_options.add(new MenuItem(container, dt.importImage(EQUIP_IMAGE),
					dt.importImage(EQUIP_HOVERED), dt, OPTION_PRIORITY) {
				@Override
				public void activate(int type) {
					if (type != MouseEvent.BUTTON1)
						return;
					if (!c.equip(_equip))
						dt.error("That item cannot be equipped.");
					
					source.returnToOptionMenu();
				}
			});
		}
		else {
			_isEquip = false;
			_item = item;
			_equip = null;
			_options.add(new MenuItem(container, dt.importImage(USE_IMAGE),
					dt.importImage(USE_HOVERED), dt, OPTION_PRIORITY) {
				@Override
				public void activate(int type) {
					if (type != MouseEvent.BUTTON1)
						return;
					_item.exert(c);
					source.characterWait();
				}
			});
		}
		
		_options.add(new MenuItem(container, dt.importImage(DISCARD_IMAGE),
				dt.importImage(DISCARD_HOVERED), dt, OPTION_PRIORITY) {
			@Override
			public void activate(int type) {
				try {
					if (type != MouseEvent.BUTTON1)
						return;
					c.removeFromInventory(_item);
					
					source.returnToOptionMenu();
				} catch (ItemException e) {
					_dt.error("Removing an item that isn't in the inventory.");
				}
			}
		});
	}
	
	@Override
	public boolean contains(Point p) {
		return super.contains(p) || _itemImage.contains(p);
	}

	
	public void addToDrawingQueue() {
		_gameScreen.addMenuItem(this);
		setVisible(true);
		
		_gameScreen.addMenuItem(_itemImage);
		_itemImage.setVisible(true);
		
		for (MenuItem m : _options) {
			_gameScreen.addMenuItem(m);
			m.setVisible(true);
		}
	}
	
	public void removeFromDrawingQueue() {
		for (MenuItem m : _options) {
			m.setVisible(false);
			_gameScreen.removeMenuItem(m);
		}
		
		_itemImage.setVisible(false);
		_gameScreen.removeMenuItem(_itemImage);
		
		setVisible(false);
		_gameScreen.removeMenuItem(this);
	}
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x - _itemImage.getWidth(), y);
		_itemImage.setLocation(x, y);
		
		double optionY = y + VERT_BUFFER;
		for (MenuItem m : _options) {
			m.setLocation(x + HORZ_BUFFER - _itemImage.getWidth(), optionY);
			optionY += m.getImage().getHeight() + VERT_BUFFER;
		}
	}
}
