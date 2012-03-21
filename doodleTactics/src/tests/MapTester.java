package tests;

import java.util.ArrayList;

import character.Character;
import character.Warrior;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import org.junit.*;

import character.Archer;

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
			Tile[][] tiles = new Tile[32][24];
			for (int i = 0; i < tiles.length; i++)
				for (int j = 0; j < tiles[i].length; j++)
					tiles[i][j] = Tile.tile(panel, "", 'F', i, j, 1);
			_test = new Map(tiles, "TestMap");
			
			_test.getTile(7, 7).setCost(5);
			_test.getTile(6, 8).setCost(2);
			
			_test.getTile(20, 17).setCost(7);
			_test.getTile(21, 16).setCost(7);
			_test.getTile(19, 16).setCost(2);
			
			//26, 12), _test.getTile(23, 11
			_test.getTile(24, 12).setCost(6);
			_test.getTile(24, 11).setCost(6);
			_test.getTile(24, 13).setCost(2);
			
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
     * tests the map's distance estimation
     */
    public void testDistanceCalc() {
    	assert(_test.estimateDistance(_test.getTile(0, 0), _test.getTile(0, 0)) == 0);
    	assert(_test.estimateDistance(_test.getTile(0, 0), _test.getTile(0, 5)) == 5);
    	assert(_test.estimateDistance(_test.getTile(0, 0), _test.getTile(5, 0)) == 5);
    	assert(_test.estimateDistance(_test.getTile(0, 0), _test.getTile(5, 5)) == 10);
    	assert(_test.estimateDistance(_test.getTile(5, 5), _test.getTile(0, 0)) == 10);
    	assert(_test.estimateDistance(_test.getTile(5, 0), _test.getTile(0, 5)) == 10);
    	assert(_test.estimateDistance(_test.getTile(0, 5), _test.getTile(5, 0)) == 10);
    	assert(_test.estimateDistance(_test.getTile(8, 8), _test.getTile(0, 5)) == 11);
    	assert(_test.estimateDistance(_test.getTile(8, 8), _test.getTile(12, 5)) == 7);
    	assert(_test.estimateDistance(_test.getTile(8, 8), _test.getTile(0, 11)) == 11);
    	assert(_test.estimateDistance(_test.getTile(8, 8), _test.getTile(12, 11)) == 7);
    }

	@Test
	/**
	 * tests the Map's pathfinding capabilities
	 */
	public void testPathFinding() {
		
		/*
		 * trivial test cases - test a straight-line path with no obstructions
		 */
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
		
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(10, 0));
		assert(path.size() == 11);
		for (int i = 0; i < path.size(); i++)
			assert(tileEquals(path.get(i), i, 0));
		
		/*
		 * tests a nonlinear path with no obstructions
		 */
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(5, 5));
		assert(path.size() == 11);
		path = _test.getPath(_test.getTile(5, 5), _test.getTile(0, 0));
		assert(path.size() == 11);
		
		/*
		 * tests paths with movement-cost obstructions
		 */	
		path = _test.getPath(_test.getTile(6, 7), _test.getTile(8, 7));
		assert(path.size() == 5);
		assert(tileEquals(path.get(0), 6, 7));
		assert(tileEquals(path.get(1), 6, 6));
		assert(tileEquals(path.get(2), 7, 6));
		assert(tileEquals(path.get(3), 8, 6));
		assert(tileEquals(path.get(4), 8, 7));
		
		path = _test.getPath(_test.getTile(20, 16), _test.getTile(21, 17));
		assert(path.size() == 7);
		assert(tileEquals(path.get(0), 20, 16));
		assert(tileEquals(path.get(1), 20, 15));
		assert(tileEquals(path.get(2), 21, 15));
		assert(tileEquals(path.get(3), 22, 15));
		assert(tileEquals(path.get(4), 22, 16));
		assert(tileEquals(path.get(5), 22, 17));
		assert(tileEquals(path.get(6), 21, 17));
		
		path = _test.getPath(_test.getTile(27, 11), _test.getTile(23, 11));
		assert(path.size() == 7);
		assert(tileEquals(path.get(0), 27, 11));
		assert(tileEquals(path.get(1), 26, 11));
		assert(tileEquals(path.get(2), 26, 10));
		assert(tileEquals(path.get(3), 25, 10));
		assert(tileEquals(path.get(4), 24, 10));
		assert(tileEquals(path.get(5), 23, 10));
		assert(tileEquals(path.get(6), 23, 11));
		
		/*
		 * tests paths moving through movement-cost obstructions
		 */
		path = _test.getPath(_test.getTile(25, 13), _test.getTile(23, 11));
		System.out.println(path.size());
	//	for (int i = 0; i < path.size(); i++)
	//		System.out.println("Path " + i + ": " + path.get(i).x() + ", " + path.get(i).y());
		assert(path.size() == 5);
		assert(tileEquals(path.get(0), 25, 13));
		assert(tileEquals(path.get(1), 24, 13));
		assert(tileEquals(path.get(2), 23, 13));
		assert(tileEquals(path.get(3), 23, 12));
		assert(tileEquals(path.get(4), 23, 11));
		
		
	}
}
