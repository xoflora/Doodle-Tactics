package event;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.PathListener;

import main.DoodleTactics;
import map.Map;
import map.Tile;
import character.Character;

/**
 * moves a character along a path on the game screen
 * @author rroelke
 *
 */
public class MoverEvent extends Event implements PathListener {
	
	protected List<Tile> _path;
	protected Character _c;
	protected String _charGet;
	protected Tile _src;
	protected Tile _dest;
	
	protected List<Integer> _xCoordinates;
	protected List<Integer> _yCoordinates;
	protected int _xSrc;
	protected int _ySrc;
	protected int _xDest;
	protected int _yDest;
	
	/**
	 * parses in a mover event (moves a character along a path)
	 * @param dt
	 * @param filePath
	 * @return a new Mover Event (parsed in from the file)
	 * @throws InvalidEventException
	 */
	public static MoverEvent parseMoverEvent(DoodleTactics dt, String filePath) throws InvalidEventException {
		
		try {
			List<Integer> xCoords;
			List<Integer> yCoords;
			int xSrc, ySrc;
			int xDest, yDest;
			
			BufferedReader r = new BufferedReader(new FileReader(new File(filePath)));
			
			String charGet = r.readLine();
			
			String line = r.readLine();
			if (line.equalsIgnoreCase("OPTIMAL")) {
				line = r.readLine();
				String[] parts = line.split(",");
				xSrc = Integer.parseInt(parts[0]);
				ySrc = Integer.parseInt(parts[1]);
				
				
				line = r.readLine();
				parts = line.split(",");
				xDest = Integer.parseInt(parts[0]);
				yDest = Integer.parseInt(parts[1]);
				
				return new MoverEvent(dt, false, charGet, xSrc, ySrc, xDest, yDest);
				
			//	path = m.getPath(s, t);
			}
			else if (line.equalsIgnoreCase("MANUAL")) {
				xCoords = new ArrayList<Integer>();
				yCoords = new ArrayList<Integer>();
				line = r.readLine();
				
				while (r != null) {
					String[] parts = line.split(",");
					
					xCoords.add(Integer.parseInt(parts[0]));
					yCoords.add(Integer.parseInt(parts[1]));
					
					line = r.readLine();
				}
				return new MoverEvent(dt, false, charGet, xCoords, yCoords);
			}
			else {
				r.close();
				throw new InvalidEventException("Invalid path paradigm in event file.");
			}
			
		} catch(IOException e) {
			throw new InvalidEventException("Invalid filename - error reading from file.");
		} catch(NumberFormatException e) {
			throw new InvalidEventException("Invalid tile location in event file.");
		}
	}

	public MoverEvent(DoodleTactics dt, boolean hasOccured, String c, List<Tile> path) {
		super(dt, hasOccured);
		_charGet = c;
		_path = path;
		_src = _path.get(0);
		_dest = _path.get(_path.size() - 1);
	}
	
	public MoverEvent(DoodleTactics dt, boolean hasOccurred, String c, List<Integer> xCoords,
			List<Integer> yCoords) {
		super(dt, hasOccurred);
		_charGet = c;
		_xCoordinates = xCoords;
		_yCoordinates = yCoords;
	}
	
	public MoverEvent(DoodleTactics dt, boolean hasOccurred, String c, int xSrc, int ySrc, int xDest, int yDest) {
		super(dt, hasOccurred);
		_charGet = c;
		_xSrc = xSrc;
		_ySrc = ySrc;
		_xDest = xDest;
		_yDest = yDest;
	}

	@Override
	public String save() {
		return null;
	}
	
	@Override
	public void take() {
		super.take();
		
		_c = _dt.getCharacter(_charGet);
		if (_path == null) {
			if (_xCoordinates != null && _yCoordinates != null && _xCoordinates.size() == _yCoordinates.size()) {
				_path = new ArrayList<Tile>();
				while (!_xCoordinates.isEmpty())
					_path.add(_gameScreen.getMap().getTile(_xCoordinates.remove(0), _yCoordinates.remove(0)));
			}
			else {
				_path = _gameScreen.getMap().getPath(_gameScreen.getMap().getTile(_xSrc, _ySrc),
						_gameScreen.getMap().getTile(_xDest, _yDest));
			}
			
			_src = _path.get(0);
			_dest = _path.get(_path.size() - 1);
		}
		_gameScreen.panToCoordinate(_c.getX(), _c.getY());
		_c.followPath(this, _path);
	}
	
	@Override
	public void release() {
		super.release();
		_src.removeOccupant();
		_dest.setOccupant(_c);
		_gameScreen.panToCoordinate(_gameScreen.getMainChar().getX(), _gameScreen.getMainChar().getY());
	}

	@Override
	public void moveComplete() {
		_gameScreen.popControl();
	}
}
