package bimaru.solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import bimaru.Block;
import bimaru.Cell;
import bimaru.CellType;
import bimaru.Grid;
import bimaru.Orientation;
import bimaru.Position;

public class ShipFinderTest {

	@Test
	public void testSingleCenter() {
		Grid grid = new Grid(new int[] {0, 1, 0}, new int[] {0, 1, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(1, 1)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 2)));
		doTest(grid, Arrays.asList(new Block(Orientation.UNDEF, 1, 1, 1)));
	}
	
	@Test
	public void testSingleTopLeftCorner() {
		Grid grid = new Grid(new int[] {0, 0, 0}, new int[] {0, 0, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(0, 0)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 2)));
		doTest(grid, Arrays.asList(new Block(Orientation.UNDEF, 0, 0, 0)));
	}

	@Test
	public void testSingleTopRightCorner() {
		Grid grid = new Grid(new int[] {0, 0, 0}, new int[] {0, 0, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(0, 2)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 2)));
		doTest(grid, Arrays.asList(new Block(Orientation.UNDEF, 0, 2, 2)));
	}

	@Test
	public void testSingleBottomLeftCorner() {
		Grid grid = new Grid(new int[] {0, 0, 0}, new int[] {0, 0, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(0, 2)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 2)));
		doTest(grid, Arrays.asList(new Block(Orientation.UNDEF, 0, 2, 2)));
	}

	@Test
	public void testSingleBottomRightCorner() {
		Grid grid = new Grid(new int[] {0, 0, 0}, new int[] {0, 0, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(2, 2)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 1)));
		doTest(grid, Arrays.asList(new Block(Orientation.UNDEF, 2, 2, 2)));
	}

	@Test
	public void testHorizontal() {
		/**
		 *   0123
		 * 0 ~~~~
		 * 1 ~XX~
		 * 2 ~~~~
		 * 3 ~~~~
		 */
		Grid grid = new Grid(new int[] {0, 0, 0, 0}, new int[] {0, 0, 0, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(1, 1)));
		grid.apply(new Cell(CellType.SHIP, new Position(1, 2)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 3)));

		doTest(grid, Arrays.asList(new Block(Orientation.HORIZONTAL, 1, 1, 2)));
	}

	@Test
	public void testHorizontal2() {
		/**
		 *   0123
		 * 0 ~~~~
		 * 1 ~XX~
		 * 2 ~~~~
		 * 3 ~XX~
		 */
		Grid grid = new Grid(new int[] {0, 0, 0, 0}, new int[] {0, 0, 0, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(1, 1)));
		grid.apply(new Cell(CellType.SHIP, new Position(1, 2)));
		grid.apply(new Cell(CellType.SHIP, new Position(3, 1)));
		grid.apply(new Cell(CellType.SHIP, new Position(3, 2)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 3)));

		doTest(grid, Arrays.asList(new Block(Orientation.HORIZONTAL, 1, 1, 2), new Block(Orientation.HORIZONTAL, 3, 1, 2)));
	}

	@Test
	public void testVertical() {
		/**
		 *   0123
		 * 0 ~~~~
		 * 1 ~X~~
		 * 2 ~X~~
		 * 3 ~~~~
		 */
		Grid grid = new Grid(new int[] {0, 0, 0, 0}, new int[] {0, 0, 0, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(1, 1)));
		grid.apply(new Cell(CellType.SHIP, new Position(2, 1)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 3)));

		doTest(grid, Arrays.asList(new Block(Orientation.VERTICAL, 1, 1, 2)));
	}

	@Test
	public void testVertical2() {
		/**
		 *   0123
		 * 0 ~~~~
		 * 1 ~X~X
		 * 2 ~X~X
		 * 3 ~~~~
		 */
		Grid grid = new Grid(new int[] {0, 0, 0, 0}, new int[] {0, 0, 0, 0});
		grid.apply(new Cell(CellType.SHIP, new Position(1, 1)));
		grid.apply(new Cell(CellType.SHIP, new Position(2, 1)));
		grid.apply(new Cell(CellType.SHIP, new Position(1, 3)));
		grid.apply(new Cell(CellType.SHIP, new Position(2, 3)));
		doTest(grid, Arrays.asList());
		
		// add water
		grid.apply(new Cell(CellType.WATER, new Position(0, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(0, 3)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(1, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(2, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 0)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 1)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 2)));
		grid.apply(new Cell(CellType.WATER, new Position(3, 3)));
		
		doTest(grid, Arrays.asList(new Block(Orientation.VERTICAL, 1, 1, 2), new Block(Orientation.VERTICAL, 3, 1, 2)));
	}

	private void doTest(Grid grid, List<Block> expected) {
		grid.print();
		List<Block> actual = new ShipFinder(grid).find();
		assertEquals("expected " + expected + " but was " + actual, expected.size(), actual.size());
		assertTrue("expected " + expected + " but was " + actual, expected.containsAll(actual));
	}
}
