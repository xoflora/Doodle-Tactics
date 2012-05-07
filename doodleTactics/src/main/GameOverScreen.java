package main;

import graphics.MenuItem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import controller.GameOverScreenController;

public class GameOverScreen extends Screen<GameOverScreenController> {

	private MenuItem _title;
	private MenuItem _quit;
	private MenuItem _gameOver;
	private String _text;

	public GameOverScreen(DoodleTactics dt) {
		super(dt);
		_dt = dt;
		setBackground(Color.GRAY);

		System.out.println("dt: " + dt);
		BufferedImage titleImage = dt.importImage("src/graphics/menu/title.png");
		BufferedImage gameover = dt.importImage("src/graphics/menu/game_over.png");

		_title = new MenuItem(this, titleImage, titleImage, dt);
		_gameOver = new MenuItem(this, gameover, gameover, dt);

		_quit = new QuitMenuButton(this, dt.importImage("src/graphics/menu/quit.png"),
				dt.importImage("src/graphics/menu/quit_hovered.png"), dt);

//		_title.setSize(_title.getImage().getWidth(), _title.getImage().getHeight());
//		_quit.setSize(_quit.getImage().getWidth(), _quit.getImage().getHeight());

		_title.setLocation(((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _title.getImage().getWidth())/2, 50);
		_gameOver.setLocation(((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _gameOver.getWidth()) /2, 275);
		_quit.setLocation(80, 600);

		_title.setVisible(true);
		_gameOver.setVisible(true);
		_quit.setVisible(true);

		setVisible(true);
	}

	@Override
	protected GameOverScreenController defaultController() {
		return new GameOverScreenController(_dt, this);
	}

	public MenuItem checkContains(java.awt.Point point) {
		
		_quit.setDefault();
		
		if (_quit.contains(point)) {
			_quit.setHovered();
			repaint();
			return _quit;
		}

		repaint();
		return null;
	}

	@Override
	public void paint(Graphics brush) {
		super.paint(brush);
		Graphics2D g = (Graphics2D) brush;

		_title.setSize(_title.getImage().getWidth(), _title.getImage().getHeight());
		_quit.setSize(_quit.getImage().getWidth(), _quit.getImage().getHeight());

		_gameOver.paint(g, _gameOver.getImage());
		_title.paint(g, _title.getImage());
		_quit.paint(g, _quit.getImage());
	}
}
