package bimaru;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bimaru.solver.CellActions.CellAction;
import bimaru.solver.LineActions.LineAction;

public class Grid {

	private static Logger LOG = LoggerFactory.getLogger(Grid.class);
	
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

	public static final String PROP_CELL_CHANGED = "cellChanged";

	/**
	 * Cell[row][column]
	 */
	private Cell[][] cells;
	private int[] rowSums;
	private int[] colSums;

	public Grid(int[] rowSums, int[] colSums) {
		this.rowSums = rowSums;
		this.colSums = colSums;
		cells = new Cell[rowSums.length][colSums.length];
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col] = new Cell(CellType.UNDEF, new Position(row, col));
			}
		}
	}

	public int getColumnCount() {
		return colSums.length;
	}

	public int getRowCount() {
		return rowSums.length;
	}

	public int getColumnSum(int col) {
		return colSums[col];
	}

	public int getRowSum(int row) {
		return rowSums[row];
	}

	public void forEach(CellAction action) {
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				LOG.debug("Executing {} on ({},{})", action.getClass().getSimpleName(), row, col);
				action.exec(cells[row][col]);
			}
		}
	}

	public void forEachLine(LineAction action) {
		forEachColumn(action);
		forEachRow(action);
	}

	public void forEachRow(LineAction action) {
		for (int row = 0; row < cells.length; row++) {
			List<Cell> lineCells = new ArrayList<>();
			for (int col = 0; col < cells[row].length; col++) {
				lineCells.add(cells[row][col]);
			}
			Line line = new Line(Orientation.HORIZONTAL, row, lineCells, rowSums[row]);
			// optimize: do nothing when all cells are known
			if (line.getUnknownCount() > 0) {
				LOG.debug("Executing {} on row {}", action.getClass().getSimpleName(), row);
				action.exec(line);
			} else {
				LOG.debug("Skipping {} because all cells of row {} are resolved", action.getClass().getSimpleName(), row);
			}
		}
	}

	public void forEachColumn(LineAction action) {
		for (int col = 0; col < cells.length; col++) {
			List<Cell> lineCells = new ArrayList<>();
			for (int row = 0; row < cells.length; row++) {
				lineCells.add(cells[row][col]);
			}
			Line line = new Line(Orientation.VERTICAL, col, lineCells, colSums[col]);
			// optimize: do nothing when all cells are known
			if (line.getUnknownCount() > 0) {
				LOG.debug("Executing {} on column {}", action.getClass().getSimpleName(), col);
				action.exec(line);
			} else {
				LOG.debug("Skipping {} because all cells of column {} are resolved", action.getClass().getSimpleName(), col);
			}
		}
	}
	
	public CellType get(Position pos) {
		return get(pos.getRow(), pos.getColumn());
	}
	
	public CellType get(int row, int column) {
		if (checkBounds(row, column)) {
			return cells[row][column].getType();
		}
		return null;
	}
	
	public void apply(Cell cell) {
		if (cell != null && checkBounds(cell.getRow(), cell.getColumn())) {
			Cell oldCell = cells[cell.getRow()][cell.getColumn()];
			cells[cell.getRow()][cell.getColumn()] = cell;
			support.firePropertyChange(PROP_CELL_CHANGED, oldCell, cell);
		}
	}

	private boolean checkBounds(int row, int col) {
		if (row < 0 || row > rowSums.length - 1) {
			return false;
		}
		if (col < 0 || col > colSums.length - 1) {
			return false;
		}
		return true;
	}

	public Position createPositionWithBoundsCheck(int row, int col) {
		return checkBounds(row, col) ? new Position(row, col) : null;
	}

	public void createCellWithChecks(List<Cell> results, CellType type, int row, int col) {
		Position position = createPositionWithBoundsCheck(row, col);
		if (position != null) {
			if (cells[row][col].getType() != type) {
				results.add(new Cell(type, position));
			}
		}
	}

	public boolean allCellsHaveType() {
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				if (cells[row][col].getType() == CellType.UNDEF) {
					return false;
				}
			}
		}
		return true;
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		support.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		support.removePropertyChangeListener(l);
	}

}
