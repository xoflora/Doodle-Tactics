package controller.combatController.player;

import items.Item;
import character.Character;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;
import graphics.MenuItem;

public class ItemWindow extends MenuItem implements CombatMenu {
	
	private static final int MENU_PRIORITY = 3;
	private static final int ARROW_PRIORITY = 5;
	private static final int OPTION_PRIORITY = 6;
	private static final int DESCRIPTION_PRIORITY = 4;
	
	private static final int HORZ_BUFFER = 3;
	private static final int VERT_BUFFER = 0;
	private static final int CHAR_HBUFF = 1;
	private static final int CHAR_VBUFF = 4;
	private static final int MAX_TEXT_ROWS = 3;
	
	private static final int VERT_SIZE = 48;
	private static final int FONT_SIZE = 11;
	
	private static final String MENU_IMAGE = "src/graphics/menu/combatMenu/item_box.png";
	private static final String ARROW_IMAGE = "src/graphics/menu/combatMenu/item_arrow.png";
	private static final String DESCRIPTION_BOX = "src/graphics/menu/combatMenu/item_description_box.png";
	
	private class ItemOption extends MenuItem {
		
		private boolean _showDescription;
		private Item _item;
		private char[] _word;
		private int[] _rowLengths;
		private int[] _rowPositions;

		public ItemOption(JPanel container, DoodleTactics dt, Item item) {
			super(container, item.getImage(), item.getImage(), dt, OPTION_PRIORITY);
			_showDescription = false;
			_item = item;
			_word = item.getDescription().toCharArray();
			_rowLengths = null;
			_rowPositions = null;
		}
		
		@Override
		public void setDefault() {
			super.setDefault();
			_showDescription = false;
		}
		
		@Override
		public void setHovered() {
			super.setHovered();
			_showDescription = true;
			
			_descriptionBox.setLocation(getX() - HORZ_BUFFER, _descriptionBox.getY());
			_descriptionBox.setVisible(true);
			
			_arrow.setLocation(getX() - HORZ_BUFFER, ItemWindow.this.getY());
			_arrow.setVisible(true);
		}
		
		@Override
		public void activate(int type) {
			if (type != MouseEvent.BUTTON1)
				return;
			_source.openItemMenu(_item);
		}
		
		@Override
		public void paint(Graphics2D brush, BufferedImage img) {
			super.paint(brush, img);
			
			if (_showDescription && getVisible()) {
				brush.setRenderingHint(
						RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
				brush.setFont(new Font("M",Font.BOLD, FONT_SIZE));
				brush.setColor(new Color(0,0,1));
				
			//	brush.drawString(_item.getDescription(), (int)getX() + CHAR_HBUFF,
			//			(int) (getY() + getHeight() + CHAR_VBUFF + FONT_SIZE));
			//	brush.draw
				
				if (_rowLengths == null || _rowPositions == null) {
					_rowLengths = new int[MAX_TEXT_ROWS];
					_rowPositions = new int[MAX_TEXT_ROWS];
					
					int pos = 0;
					for (int i = 0; i < 2*(_descriptionBox.getWidth() - Tile.TILE_SIZE - 2*CHAR_HBUFF)/FONT_SIZE; i++) {
						if (i >= _word.length) {
							pos = i;
							break;
						}
						if (_word[i] == ' ')
							pos = i;
					}
					_rowLengths[0] = pos;
					_rowPositions[0] = 0;
					
		/*			for (int i = 1; i < MAX_TEXT_ROWS; i++) {
						for (int j = 0; j < 2*(_descriptionBox.getWidth() - 2*CHAR_HBUFF)/FONT_SIZE; j++) {
							if (j + _rowLengths[i - 1] >=  _word.length) {
								_rowLengths[i] = j + pos;
								_rowPositions[i] = _rowLengths[i - 1];
								break;
							}
							if (_word[index + j] == ' ')
								pos += j;
						}
						_rowLengths[i] = pos;
						index = pos;
					}		*/
					
					int j = 0, row = 1;
					while (pos < _word.length && row < MAX_TEXT_ROWS) {
						_rowPositions[row] = _rowLengths[row - 1] + _rowPositions[row - 1];
						if (row == MAX_TEXT_ROWS - 1) {
							_rowLengths[row] = _word.length - _rowPositions[row];
							break;
						}
						
						while (j < 2*(_descriptionBox.getWidth() - 2*CHAR_HBUFF)/FONT_SIZE && pos + j < _word.length) {
							if (_word[pos + j] == ' ')
								_rowLengths[row] = pos + j - _rowPositions[row];
							
							j++;
						}
						
				/*		if (pos + j >= _word.length) {
							_rowLengths[row] = _word.length - _rowPositions[row];
							break;
						}		*/
						
						pos = _rowLengths[row];
						row++;
					}
										
					for (int i = 0; i < _rowLengths.length; i++) {
						System.out.print(_rowPositions[i] + " ");
						System.out.println(_rowLengths[i]);	
					}
					
				/*	String[] words = _item.getDescription().split(" ");
					int pos = 0, i = 0;
					while (pos < _descriptionBox.getWidth() - Tile.TILE_SIZE - 2*CHAR_HBUFF) {
						if (words[i].length() * FONT_SIZE < 0)
							words[i].
					}	*/
				}
				
				brush.drawChars(_word, _rowPositions[0], _rowLengths[0],
						(int) (getX() + Tile.TILE_SIZE + 2*CHAR_HBUFF),
						(int) (getY() + getHeight() + CHAR_VBUFF + FONT_SIZE));
				int x = (int) (getX() - CHAR_HBUFF);
				int y = (int) (getY() + getHeight() + CHAR_VBUFF + 2*FONT_SIZE);
				for (int i = 1; i < _rowLengths.length; i++) {
					if (_rowPositions[i] >= _word.length)
						break;
					brush.drawChars(_word, _rowPositions[i], _rowLengths[i], x, y);
					y += FONT_SIZE + 1;
				}
			}
		}
	}
	
	private List<ItemOption> _items;
	private PlayerCombatController _source;
	private GameScreen _gameScreen;
	private MenuItem _arrow;
	private MenuItem _descriptionBox;

	public ItemWindow(GameScreen container, DoodleTactics dt, Character c, PlayerCombatController source) {
		super(container, dt.importImage(MENU_IMAGE), dt.importImage(MENU_IMAGE), dt, MENU_PRIORITY);
		_items = new ArrayList<ItemOption>();
		_source = source;
		_gameScreen = container;
		
		HashMap<Integer, Item> inventory = c.getInventory();
		for (Integer i : inventory.keySet()) {
			Item item = inventory.get(i);
			_items.add(new ItemOption(container, _dt, item));
		}
		
		_arrow = new MenuItem(_gameScreen, dt.importImage(ARROW_IMAGE),
				dt.importImage(ARROW_IMAGE), _dt, ARROW_PRIORITY);
		_descriptionBox = new MenuItem(_gameScreen, dt.importImage(DESCRIPTION_BOX),
				dt.importImage(DESCRIPTION_BOX), _dt, DESCRIPTION_PRIORITY);
		
	}
	
	/**
	 * adds the item window and all of its components to the game screen
	 */
	public void addToDrawingQueue() {
		_gameScreen.addMenuItem(this);
		_gameScreen.addMenuItem(_arrow);
		_gameScreen.addMenuItem(_descriptionBox);
		
		double x = getX() + HORZ_BUFFER;
		for (ItemOption i : _items) {
			_gameScreen.addMenuItem(i);
			
			i.setLocation(x, getY() + VERT_BUFFER);
			i.setSize(Tile.TILE_SIZE, Tile.TILE_SIZE);
			i.setVisible(true);
			
			x += i.getWidth() + HORZ_BUFFER;
		}
		setSize(x - getX() - HORZ_BUFFER, VERT_SIZE);

		_descriptionBox.setLocation(getX(), getY() + getHeight());
		_descriptionBox.setVisible(false);
		_descriptionBox.setSize(152, Tile.TILE_SIZE);
		_arrow.setLocation(getX(), getY());
		_arrow.setVisible(false);
		setVisible(true);
	}
	
	/**
	 * removes the item window and all of its components from the game screen
	 */
	public void removeFromDrawingQueue() {
		setVisible(false);
		_arrow.setVisible(false);
		for (ItemOption i : _items) {
			i.setVisible(false);
			i._showDescription = false;
			_gameScreen.removeMenuItem(i);
		}
		_gameScreen.removeMenuItem(_descriptionBox);
		_gameScreen.removeMenuItem(_arrow);
		_gameScreen.removeMenuItem(this);
	}
	
	@Override
	public void setLocation(double x, double y) {
		_arrow.setLocation(_arrow.getX() - getX() + x, y);
		_descriptionBox.setLocation(_descriptionBox.getX() - getX() + x, y + getHeight());
		super.setLocation(x, y);
		
		double menuX = x + HORZ_BUFFER;
		for (MenuItem m : _items) {
			m.setLocation(menuX, y + VERT_BUFFER);
			menuX += m.getWidth() + HORZ_BUFFER;
		}
		setSize(menuX - x - HORZ_BUFFER, VERT_SIZE);
	}
	
	@Override
	public void setDefault() {
		super.setDefault();
		if (_arrow != null)
			_arrow.setVisible(false);
		if (_descriptionBox != null)
			_descriptionBox.setVisible(false);
	}
}