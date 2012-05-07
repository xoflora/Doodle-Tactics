package controller.combatController.player;

import character.Character;

import items.Equipment;
import items.Item;
import items.ItemException;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.GameScreen;
import graphics.MenuItem;

public class ItemActionWindow extends MenuItem implements CombatMenu {
	
	private static final String WINDOW_IMAGE = "";
	private static final String EQUIP_IMAGE = "src/graphics/menu/combatMenu/equip.png";
	private static final String EQUIP_HOVERED = "src/graphics/menu/combatMenu/equip_hovered.png";
	private static final String USE_IMAGE = "src/graphics/menu/combatMenu/use.png";
	private static final String USE_HOVERED = "src/graphics/menu/combatMenu/use_hovered.png";
	private static final String DISCARD_IMAGE = "src/graphics/menu/combatMenu/discard.png";
	private static final String DISCARD_HOVERED = "src/graphics.menu/combatMenu/discard_hovered.png";
	
	private boolean _isEquip;
	private Item _item;
	private Equipment _equip;
	private List<MenuItem> _options;

	@SuppressWarnings("serial")
	public ItemActionWindow(Item item, final Character c, GameScreen container, BufferedImage defltPath,
			BufferedImage hoveredPath, final DoodleTactics dt) {
		super(container, defltPath, hoveredPath, dt);
	//	_gameScreen = container;
		
		_options = new ArrayList<MenuItem>();
		if (_item.isEquip()) {
			_isEquip = true;
			_equip = (Equipment) item;
			_item = null;
			_options.add(new MenuItem(container, dt.importImage(USE_IMAGE),
					dt.importImage(USE_HOVERED), dt) {
				@Override
				public void activate(int type) {
					if (!c.equip(_equip))
						dt.error("That item cannot be equipped.");
				}
			});
		}
		else {
			_isEquip = false;
			_item = item;
			_equip = null;
			_options.add(new MenuItem(container, dt.importImage(EQUIP_IMAGE),
					dt.importImage(EQUIP_HOVERED), dt) {
				@Override
				public void activate(int type) {
					
				}
			});
		}
		
		_options.add(new MenuItem(container, dt.importImage(DISCARD_IMAGE),
				dt.importImage(DISCARD_HOVERED), dt) {
			@Override
			public void activate(int type) {
				try {
					c.removeFromInventory(_item);
				} catch (ItemException e) {
					_dt.error("Removing an item that isn't in the inventory.");
				}
			}
		});
	}

	
	public void addToDrawingQueue() {
		
	}
	
	public void removeFromDrawingQueue() {
		
	}
}
