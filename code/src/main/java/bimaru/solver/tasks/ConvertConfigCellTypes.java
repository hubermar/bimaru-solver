package bimaru.solver.tasks;

import java.util.List;

import bimaru.Cell;
import bimaru.CellType;
import bimaru.Grid;
import bimaru.solver.SolverTask;

public class ConvertConfigCellTypes implements SolverTask {

	private final Grid grid;

	public ConvertConfigCellTypes(Grid grid) {
		this.grid = grid;
	}
	
	@Override
	public void process(List<Cell> results) {
		grid.forEach(cell -> {
			switch (cell.getType()) {
			case SHIP_UP:
			case SHIP_DOWN:
				handleShipUpDown(cell, results);
				break;
			case SHIP_LEFT:
			case SHIP_RIGHT:
				handleShipLeftRight(cell, results);
				break;
			case SHIP_SINGLE:
				handleSingleShip(cell, results);
				break;
			case SHIP:
				handleShip(cell, results);
				break;
			default:
			}
		});
	}

	private void handleShip(Cell cell, List<Cell> results) {
		// convert cells in the diagonal to WATER
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() + 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() + 1);
	}

	private void handleSingleShip(Cell cell, List<Cell> results) {
		// convert cell to SHIP
		results.add(new Cell(CellType.SHIP, cell.getPosition()));
		// add sea on all adjoining cells
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn());
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() + 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow(), cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow(), cell.getColumn() + 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn());
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() + 1);
	}

	private void handleShipUpDown(Cell cell, List<Cell> results) {
		int rowOffset = CellType.SHIP_DOWN == cell.getType() ? 1 : -1;
		// convert cell to SHIP
		results.add(new Cell(CellType.SHIP, cell.getPosition()));
		// add sea on all adjoining cells
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - rowOffset, cell.getColumn());
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - rowOffset, cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - rowOffset, cell.getColumn() + 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow(), cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow(), cell.getColumn() + 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + rowOffset, cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + rowOffset, cell.getColumn() + 1);
		// add one SHIP below plus adjoining cells
		grid.createCellWithChecks(results, CellType.SHIP,  cell.getRow() + rowOffset, cell.getColumn());
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 2 * rowOffset, cell.getColumn() - 1);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 2 * rowOffset, cell.getColumn() + 1);
	}

	private void handleShipLeftRight(Cell cell, List<Cell> results) {
		int colOffset = CellType.SHIP_RIGHT == cell.getType() ? 1 : -1;
		// convert cell to SHIP
		results.add(new Cell(CellType.SHIP, cell.getPosition()));
		// add sea on all adjoining cells
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() - colOffset);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 0, cell.getColumn() - colOffset);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() - colOffset);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn());
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn());
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() + colOffset);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() + colOffset);
		// add one SHIP below plus adjoining cells
		grid.createCellWithChecks(results, CellType.SHIP,  cell.getRow(), cell.getColumn() + colOffset);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() + 2 * colOffset);
		grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() + 2 * colOffset);
	}
}
