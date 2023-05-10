package bimaru.solver.tasks;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bimaru.Block;
import bimaru.Cell;
import bimaru.CellType;
import bimaru.Orientation;
import bimaru.Fleet;
import bimaru.Grid;
import bimaru.Line;
import bimaru.Position;
import bimaru.ShipType;
import bimaru.solver.BlockFinder;
import bimaru.solver.SolverTask;

public class FindLargestShip implements SolverTask {

	private static final Logger LOG = LoggerFactory.getLogger(FindLargestShip.class);
	
	private final Grid grid;
	private final Fleet fleet;

	public FindLargestShip(Grid grid, Fleet fleet) {
		this.grid = grid;
		this.fleet = fleet;
	}
	
	@Override
	public void process(List<Cell> results) {
		ShipType ship = fleet.getLargestUnresolvedShipType();
		LOG.debug("largest unresolved ship={}", ship);
		grid.forEachLine(line -> {
			if (line.getSum() >= ship.getSize()) {
				BlockFinder finder = new BlockFinder(EnumSet.of(CellType.UNDEF, CellType.SHIP));
				finder.find(line);
				List<Block> potentialBlocks = finder.getBlocksByMinSize(ship.getSize());
				if (potentialBlocks.size() <= ship.getQuantity()) {
					potentialBlocks.forEach(block -> {
						LOG.info("use this block {}", block);
						fitShipInside(results, ship.getSize(), block, line);
					});
				}
			}
		});
	}

	private void fitShipInside(List<Cell> results, int shipSize, Block block, Line line) {
		if (block.getLength() == shipSize) {
			// perfect fit -> fill
			for (int index = block.getBegin(); index <= block.getEnd(); index++) {
				int row = block.getOrientation() == Orientation.HORIZONTAL ? block.getLineNo() : index;
				int col = block.getOrientation() == Orientation.HORIZONTAL ? index : block.getLineNo();
				results.add(new Cell(CellType.SHIP, new Position(row, col)));
			}
			// ship complete, add WATER in the following cell
			int row = block.getOrientation() == Orientation.HORIZONTAL ? block.getLineNo() : block.getBegin() + shipSize;
			int col = block.getOrientation() == Orientation.HORIZONTAL ? block.getBegin() + shipSize : block.getLineNo();
			results.add(new Cell(CellType.WATER, new Position(row, col)));
		} else {
			// find ship position inside block
			LOG.debug("find ship of size {} inside block {}", shipSize, block);
			CellType[] large = new CellType[block.getLength()];
			for (int index = 0; index < block.getLength(); index++) {
				large[index] = line.getCell(block.getBegin() + index).getType();
			}
			CellType[] small = new CellType[shipSize];
			Arrays.fill(small, CellType.SHIP);
			int offset = ccf(large, small);
			LOG.debug("ccf offset is {}", offset);
			// found offset -> fill
			for (int index = block.getBegin() + offset; index < block.getBegin() + offset + shipSize; index++) {
				int row = block.getOrientation() == Orientation.HORIZONTAL ? block.getLineNo() : index;
				int col = block.getOrientation() == Orientation.HORIZONTAL ? index : block.getLineNo();
				results.add(new Cell(CellType.SHIP, new Position(row, col)));
			}
			// ship complete, add WATER in the following cell
			int row = block.getOrientation() == Orientation.HORIZONTAL ? block.getLineNo() : block.getBegin() + shipSize;
			int col = block.getOrientation() == Orientation.HORIZONTAL ? block.getBegin() + shipSize : block.getLineNo();
			results.add(new Cell(CellType.WATER, new Position(row, col)));
		}
	}
	
	// cross correlation function
	private int ccf(CellType[] large, CellType[] small) {
		assert large.length > small.length;
		int maxOffset = 0;
		int maxValue = 0;
		for (int offset = 0; offset <= (large.length - small.length); offset++) {
			int value = 0;
			for (int index = 0; index < small.length; index++) {
				CellType lct = large[offset + index];
				CellType sct = small[index];
				if (lct == sct) {
					value += 2;
				} else if (lct == CellType.UNDEF) {
					value += 1;
				} else {
					// don't increase value
				}
			}
			LOG.debug("ccf: offset {} -> value {}", offset, value);
			if (value > maxValue) {
				maxValue = value;
				maxOffset = offset;
			}
		}
		return maxOffset;
	}
}
