package tests;

import java.util.List;

import javax.swing.JPanel;

import org.junit.*;

import map.*;

/**
 * 
 * @author rroelke
 * tests the classes and methods of the map package
 */
public class MapTester {
	
	private static Map _test;
	
	public static boolean tileEquals(Tile t, int x, int y) {
		return t.x() == x && t.y() == y;
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		try {
			JPanel panel = new JPanel();
			Tile[][] tiles = new Tile[16][12];
			for (int i = 0; i < tiles.length; i++)
				for (int j = 0; j < tiles[i].length; j++)
					tiles[i][j] = Tile.tile(panel, "", 'F', i, j, 1);
			_test = new Map(tiles, "TestMap");
			
		} catch(InvalidTileException e) {
			assert(false);
		}
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

	@Test
	/**
	 * tests the Map's pathfinding capabilities
	 */
	public void testPathFinding() {
		List<Tile> path = _test.getPath(_test.getTile(0, 0), _test.getTile(0, 1));
		assert(path.size() == 2);
		assert(tileEquals(path.get(0), 0, 0));
		assert(tileEquals(path.get(1), 0, 1));
		
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(1, 0));
		assert(path.size() == 2);
		assert(tileEquals(path.get(0), 0, 0));
		assert(tileEquals(path.get(1), 1, 0));
		
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(0, 10));
		assert(path.size() == 11);
		for (int i = 0; i < path.size(); i++)
			assert(tileEquals(path.get(i), 0, i));
	}
}
