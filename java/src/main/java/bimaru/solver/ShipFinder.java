package bimaru.solver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bimaru.Block;
import bimaru.Cell;
import bimaru.CellType;
import bimaru.Direction;
import bimaru.Grid;
import bimaru.Orientation;
import bimaru.Position;

public class ShipFinder {

	private static final Logger LOG = LoggerFactory.getLogger(ShipFinder.class);
	
	private static final int UNDEF = -1;
	
	private final Grid grid;
	
	public ShipFinder(Grid grid) {
		this.grid = grid;
	}
	
	public List<Block> find() {
		List<Block> result = new ArrayList<>();
		
		grid.forEach(cell -> {
			if (CellType.SHIP == cell.getType()) {
				Block b = null;
				// case single ship
				if (b == null) {
					b = analyzeSingleCell(cell);
					LOG.debug("single cell result={}", b);
				}
				// case direction vertical (column)
				if (b == null) {
					b = analyzeVertical(cell);
					LOG.debug("vertical result={}", b);
				}
				// case direction horizontal (row)
				if (b == null) {
					b = analyzeHorizontal(cell);
					LOG.debug("horizontal result={}", b);
				}
				
				if (b != null) {
					LOG.debug("adding result {}", b);
					result.add(b);
				}
			}
		});
		
		return result;
	}

	private Block analyzeHorizontal(Cell cell) {
		int begin = analyze(cell, Orientation.HORIZONTAL, Direction.LEFT, true);
		int end = analyze(cell, Orientation.HORIZONTAL, Direction.RIGHT, false);
		if (begin != UNDEF && end != UNDEF) {
			return new Block(Orientation.HORIZONTAL, cell.getRow(), begin, end);
		}
		return null;
	}

	private Block analyzeVertical(Cell cell) {
		int begin = analyze(cell, Orientation.VERTICAL, Direction.UP, true);
		int end = analyze(cell, Orientation.VERTICAL, Direction.DOWN, false);
		if (begin != UNDEF && end != UNDEF) {
			return new Block(Orientation.VERTICAL, cell.getRow(), begin, end);
		}
		return null;
	}

	private int analyze(Cell cell, Orientation or, Direction dir, boolean including) {
		int columnOffset = 0;
		int rowOffset = 0; 
		if (Orientation.HORIZONTAL == or) {
			if (Direction.LEFT == dir) {
				columnOffset = -1;
			} else if (Direction.RIGHT == dir) {
				columnOffset = 1;
			}
		} else if (Orientation.VERTICAL == or) {
			if (Direction.UP == dir) {
				rowOffset = -1;
			} else if (Direction.DOWN == dir) {
				rowOffset = 1;
			}
		}
		Position pos = cell.getPosition();
		if (!including) {
			pos = pos.create(rowOffset, columnOffset);
		}
		while (analyzeLineCell(pos, rowOffset, columnOffset)) {
			LOG.debug("pos {} ok, moving on ...", pos);
			pos = pos.create(rowOffset, columnOffset);
		}
		if (analyzeLineEnd(pos, or, rowOffset, columnOffset)) {
			LOG.debug("final check on {}", pos);
			if (Orientation.HORIZONTAL == or) {
				LOG.debug("final check horizontal ok, returning {}", pos.getColumn() - columnOffset);
				return pos.getColumn() - columnOffset;
			} else if (Orientation.VERTICAL == or) {
				LOG.debug("final check vertical ok, returning {}", pos.getRow() - rowOffset);
				return pos.getRow() - rowOffset;
			}
		}
		return UNDEF;
	}
	
	private boolean analyzeLineEnd(Position pos, Orientation or, int rowOffset, int columnOffset) {
		if (checkCellAt(pos, CellType.WATER)) {
			switch (or) {
			case HORIZONTAL:
				return checkCellAt(pos.create(-columnOffset, -1), CellType.WATER) && checkCellAt(pos.create(-columnOffset, 1), CellType.WATER); 
			case VERTICAL:
				return checkCellAt(pos.create(-1, -rowOffset), CellType.WATER) && checkCellAt(pos.create(1, -rowOffset), CellType.WATER); 
			default:
				// nop
			}
		}
		return false;
	}
	
	private boolean analyzeLineCell(Position pos, int rowOffset, int columnOffset) {
		return grid.get(pos) == CellType.SHIP && 
		checkCellAt(pos.create( 1,  1), CellType.WATER) && 
		checkCellAt(pos.create( 1, -1), CellType.WATER) && 
		checkCellAt(pos.create(-1,  1), CellType.WATER) && 
		checkCellAt(pos.create(-1, -1), CellType.WATER) &&
		checkCellAt(pos.create(rowOffset, columnOffset), CellType.WATER);
	}
	
	private Block analyzeSingleCell(Cell cell) {
		if (checkCellAt(cell.getPosition().create(-1, -1), CellType.WATER) &&
			checkCellAt(cell.getPosition().create(-1,  0), CellType.WATER) &&
			checkCellAt(cell.getPosition().create(-1,  1), CellType.WATER) &&
			checkCellAt(cell.getPosition().create( 0, -1), CellType.WATER) &&
			checkCellAt(cell.getPosition().create( 0,  1), CellType.WATER) &&
			checkCellAt(cell.getPosition().create( 1, -1), CellType.WATER) &&
			checkCellAt(cell.getPosition().create( 1,  0), CellType.WATER) &&
			checkCellAt(cell.getPosition().create( 1,  1), CellType.WATER)) {
			return new Block(Orientation.UNDEF, cell.getRow(), cell.getColumn(), cell.getColumn());
		}
		return null;
	}
	
	private boolean checkCellAt(Position pos, CellType targetType) {
		CellType type = grid.get(pos);
		return type == null /* outside grid */ || type == targetType;
	}
}
