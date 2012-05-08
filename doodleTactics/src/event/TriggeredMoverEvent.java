package event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.DoodleTactics;
import map.Map;
import map.Tile;
import character.Character;

/**
 * a mover event class that moves the character that steps onto the tile;
 * anonymous in the sense that it applies 
 * @author rroelke
 *
 */
public class TriggeredMoverEvent extends MoverEvent {

public static TriggeredMoverEvent parseTriggeredMoverEvent(DoodleTactics dt, String filePath) throws InvalidEventException {
		
		try {
			List<Integer> xCoords;
			List<Integer> yCoords;
			int xSrc, ySrc;
			int xDest, yDest;
			
			BufferedReader r = new BufferedReader(new FileReader(new File(filePath)));
			
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
				
				return new TriggeredMoverEvent(dt, false, xSrc, ySrc, xDest, yDest);
				
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
				return new TriggeredMoverEvent(dt, false, xCoords, yCoords);
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

	public TriggeredMoverEvent(DoodleTactics dt, boolean hasOccured, List<Tile> path) {
		super(dt, hasOccured, null, path);
	}

	public TriggeredMoverEvent(DoodleTactics dt, boolean b, int xSrc, int ySrc,
			int xDest, int yDest) {
		super(dt, b, null, xSrc, ySrc, xDest, yDest);
	}

	public TriggeredMoverEvent(DoodleTactics dt, boolean b,
			List<Integer> xCoords, List<Integer> yCoords) {
		super(dt, b, null, xCoords, yCoords);
	}

	@Override
	public void take() {
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
		}
		
		_src = _path.get(0);
		_dest = _path.get(_path.size() - 1);
		
		_c = _src.getOccupant();
		_gameScreen.panToCoordinate(_c.getX(), _c.getY());
		_c.followPath(this, _path);
	}
}
