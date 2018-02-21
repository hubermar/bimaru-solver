package bimaru.solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static bimaru.CellType.SHIP;
import static bimaru.CellType.UNDEF;
import static bimaru.CellType.WATER;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import bimaru.Block;
import bimaru.Cell;
import bimaru.CellType;
import bimaru.Orientation;
import bimaru.Line;

public class BlockFinderTest {

	private static final int TEST_SUM = 5;
	private static final int TEST_LINE_NO = 0;
	private static final Orientation TEST_DIR = Orientation.HORIZONTAL;

	@Test
	public void testNoBorderSingleType() {
		doTest(EnumSet.of(SHIP), Collections.emptySet(),
				Arrays.asList(), 
				Collections.emptyList());
		doTest(EnumSet.of(SHIP), Collections.emptySet(), 
				Arrays.asList(WATER), 
				Collections.emptyList());
		doTest(EnumSet.of(SHIP), Collections.emptySet(),
				Arrays.asList(WATER, WATER), 
				Collections.emptyList());
		doTest(EnumSet.of(SHIP), Collections.emptySet(),
				Arrays.asList(WATER, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 1)));
		doTest(EnumSet.of(SHIP), Collections.emptySet(),
				Arrays.asList(WATER, SHIP, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 2)));
		doTest(EnumSet.of(SHIP), Collections.emptySet(), 
				Arrays.asList(WATER, SHIP, WATER, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 1), new Block(TEST_DIR, TEST_LINE_NO, 3, 3)));
		doTest(EnumSet.of(SHIP), Collections.emptySet(),
				Arrays.asList(SHIP, SHIP, SHIP, SHIP, SHIP), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 0, 4)));
	}
	
	@Test
	public void testNoBorderMultipleTypes() {
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(), 
				Collections.emptyList());
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(WATER), 
				Collections.emptyList());
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(WATER, WATER), 
				Collections.emptyList());
		doTestNoBorder(EnumSet.of(UNDEF, UNDEF), 
				Arrays.asList(WATER, WATER), 
				Collections.emptyList());
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(WATER, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 1)));
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(WATER, SHIP, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 2)));
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(WATER, SHIP, UNDEF, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 2)));
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(WATER, SHIP, WATER, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 1), new Block(TEST_DIR, TEST_LINE_NO, 3, 3)));
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(WATER, SHIP, WATER, UNDEF, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 1), new Block(TEST_DIR, TEST_LINE_NO, 3, 3)));
		doTestNoBorder(EnumSet.of(SHIP, UNDEF), 
				Arrays.asList(UNDEF, SHIP, SHIP, SHIP, UNDEF), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 0, 4)));
	}

	private void doTestNoBorder(Set<CellType> allowed, List<CellType> cellTypes, List<Block> expected) {
		BlockFinder bf = new BlockFinder(allowed);
		List<Cell> cells = cellTypes.stream().map(type -> new Cell(type, null)).collect(Collectors.toList());
		Line line = new Line(TEST_DIR, TEST_LINE_NO, cells, TEST_SUM);
		bf.find(line);
		List<Block> actual = bf.getAllBlocks();
		assertEquals(expected.size(), actual.size());
		assertTrue(expected.containsAll(actual));
	}

	@Test
	public void testBorderSingleType() {
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER), 
				Arrays.asList(), 
				Collections.emptyList());
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER),
				Arrays.asList(WATER), 
				Collections.emptyList());
		doTest(EnumSet.of(SHIP),  EnumSet.of(WATER),
				Arrays.asList(WATER, WATER), 
				Collections.emptyList());
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER),
				Arrays.asList(UNDEF, SHIP, UNDEF), 
				Collections.emptyList());
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER),
				Arrays.asList(WATER, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 1)));
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER),
				Arrays.asList(UNDEF, WATER, SHIP, WATER, UNDEF), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 2, 2)));
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER),
				Arrays.asList(WATER, SHIP, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 2)));
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER),
				Arrays.asList(WATER, SHIP, WATER, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 1), new Block(TEST_DIR, TEST_LINE_NO, 3, 3)));
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER),
				Arrays.asList(SHIP, SHIP, SHIP, SHIP, SHIP), 
				Arrays.asList());
		doTest(EnumSet.of(SHIP), EnumSet.of(WATER),
				Arrays.asList(WATER, SHIP, SHIP, SHIP, WATER), 
				Arrays.asList(new Block(TEST_DIR, TEST_LINE_NO, 1, 3)));
	}
	
	private void doTest(Set<CellType> target, Set<CellType> border, List<CellType> cellTypes, List<Block> expected) {
		BlockFinder bf = new BlockFinder(target, border);
		List<Cell> cells = cellTypes.stream().map(type -> new Cell(type, null)).collect(Collectors.toList());
		Line line = new Line(TEST_DIR, TEST_LINE_NO, cells, TEST_SUM);
		bf.find(line);
		List<Block> actual = bf.getAllBlocks();
		assertEquals(expected.size(), actual.size());
		assertTrue(expected.containsAll(actual));
	}

}
