package main;

import graphics.*;
import java.awt.Graphics2D;
import controller.Controller;
import controller.OverworldController;

/** 
 * This screen models the main menu and is initially displayed
 * upon running doodle tactics
 * @author jeshapir
 */

@SuppressWarnings("serial")
public class MainMenuScreen extends Screen {

	private MenuItem _title;
	private ScreenChangeMenuItem _newGame;
	private MenuItem _continue;
	private MenuItem _quit;
	
	public MainMenuScreen(Controller control, DoodleTactics dt) {
		super(control, dt);
		this.setBackground(java.awt.Color.GRAY);
		_title = new MenuItem(this, "src/graphics/title.png","src/graphics/title.png", dt);
		_newGame = new ScreenChangeMenuItem(this, "src/graphics/new_game.png","src/graphics/new_game_hovered.png", dt, dt.getGameScreen(), new OverworldController(dt.getGameScreen()));
		_continue = new MenuItem(this, "src/graphics/continue.png","src/graphics/continue_hovered.png", dt);
		_quit = new MenuItem(this, "src/graphics/quit.png","src/graphics/quit_hovered.png", dt);
		_title.setSize(_title.getCurrentImage().getWidth(), _title.getCurrentImage().getHeight());
		_newGame.setSize(_newGame.getCurrentImage().getWidth(), _newGame.getCurrentImage().getHeight());
		_continue.setSize(_continue.getCurrentImage().getWidth(), _continue.getCurrentImage().getHeight());
		_quit.setSize(_quit.getCurrentImage().getWidth(), _quit.getCurrentImage().getHeight());
		
		_title.setLocation(((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _title.getCurrentImage().getWidth())/2, 50);
		int offset = ((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _newGame.getCurrentImage().getWidth())/2;
		_newGame.setLocation(offset, 250);
		_continue.setLocation(offset, 400);
		_quit.setLocation(offset, 550);
		_title.setVisible(true);
		_newGame.setVisible(true);
		_continue.setVisible(true);
		_quit.setVisible(true);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		_title.paint((Graphics2D) g, _title.getCurrentImage());
		_newGame.paint((Graphics2D) g, _newGame.getCurrentImage());
		_continue.paint((Graphics2D) g, _continue.getCurrentImage());
		_quit.paint((Graphics2D) g, _quit.getCurrentImage());
	}
	
	public MenuItem checkContains(java.awt.Point point) {
		
		/* set all of the buttons to default */
		_newGame.setDefault();
		_continue.setDefault();
		_quit.setDefault();
		
		/* check if the point is in any of the buttons */
		if(_newGame.contains(point)) {
			_newGame.setHovered();
			this.repaint();
			return _newGame;
		}
		
		if(_continue.contains(point)) {
			_continue.setHovered();
			this.repaint();
			return _continue;
		}
		
		if(_quit.contains(point)) {
			_quit.setHovered();
			this.repaint();
			return _quit;
		}
		
		this.repaint();
		return null;
	}

}
