package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import graphics.MenuItem;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import controller.LoadGameController;

public class LoadGameScreen extends Screen<LoadGameController>{
	
	private BufferedImage _buttonSelectedImage;
	private BufferedImage _buttonUnselectedImage;
	private BufferedImage _bg;
	private BufferedImage _menuPanel;
	private LoadMenuItem[] _savedGames;
	private LoadMenuItem _currSelected;
	private MenuItem _loadButton;
	
	public LoadGameScreen(DoodleTactics dt) {
		super(dt);
		this.setVisible(true);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_dt = dt;
		//Background
		_bg = dt.importImage("src/graphics/menu/load_menu.png");
		
		//Load MenuItems
		
		
		//RadioButtons
		_buttonUnselectedImage = dt.importImage("src/graphics/menu/load_radio_button.png");
		_buttonSelectedImage = dt.importImage("src/graphics/menu/load_radio_button.png");
		
		//Load Buttons
		BufferedImage loadButtonHoveredImage = _dt.importImage("src/graphics/menu/load_game_button_hovered.png");
		BufferedImage loadButtonImage = _dt.importImage("src/graphics/menu/load_game_button.png");
		_loadButton = new MenuItem(this,loadButtonImage,loadButtonHoveredImage,_dt);
		_loadButton.setLocation(350,700);
		_loadButton.setVisible(true);
		
		//MenuPanel
		_menuPanel = dt.importImage("src/graphics/menu/load_menu_item.png");
		
		//List of LoadMenuItems
		_savedGames = new LoadMenuItem[DoodleTactics.NUM_SAVE_OPTIONS];
		int count = 0;
		HashMap<String,String> titleToFilepath = _dt.getSavedFilePaths();
		for(String title: titleToFilepath.keySet()){
			String filepath = titleToFilepath.get(title);
			int y = (_bg.getHeight()/(DoodleTactics.NUM_SAVE_OPTIONS + 2))*(count + 1);
			_savedGames[count] = new LoadMenuItem(this,loadButtonImage,loadButtonHoveredImage,
									title,filepath,y,_dt);
			_savedGames[count].setVisible(true);
		//	_savedGames[count].setLocation(0,(_bg.getHeight()/DoodleTactics.NUM_SAVE_OPTIONS)*count);
			count++;
		}
		
		//Fill empty files
		while(count < DoodleTactics.NUM_SAVE_OPTIONS){
			int y = (_bg.getHeight()/(DoodleTactics.NUM_SAVE_OPTIONS + 2))*(count + 1);
			_savedGames[count] = new LoadMenuItem(this,loadButtonImage,loadButtonHoveredImage,
					"<Empty>",null,y,_dt);
			_savedGames[count].setVisible(true);
		//	_savedGames[count].setLocation(0,(_bg.getHeight()/DoodleTactics.NUM_SAVE_OPTIONS)*count);

			count++;
		}
		
		repaint();
	}
	
	private class LoadMenuItem extends MenuItem{
		private boolean _selectable;
		private int _y;
		private String _title;
		public LoadMenuItem(JPanel container, BufferedImage defltPath,
				BufferedImage hoveredPath, String title, String filename,
				int y,DoodleTactics dt) {
			super(container, defltPath, hoveredPath, dt);
			_selectable = (filename != null);
			_y = y;
			_title = title;
		}	
		
		@Override
		public void paint(Graphics2D g){
			g.drawImage(_buttonUnselectedImage,null,0,_y);
			g.drawImage(_menuPanel, null, 150,_y + 40);
			g.setFont(new Font("Arial",Font.BOLD,50));
			g.setColor(new Color(0,0,1));
			g.drawString(_title, 375,_y + 90);
		}
	}

	@Override
	protected LoadGameController defaultController() {
		return new LoadGameController(_dt, this);
	}
	
	@Override
	public void paintComponent( Graphics brush ){
		Graphics2D g = (Graphics2D) brush;
		g.drawImage(_bg, null, 0, 0);
		
		for(int i=0; i<DoodleTactics.NUM_SAVE_OPTIONS; i++){
			_savedGames[i].paint(g);
		}
		_loadButton.paint(g,_loadButton.getImage());
	}
}

