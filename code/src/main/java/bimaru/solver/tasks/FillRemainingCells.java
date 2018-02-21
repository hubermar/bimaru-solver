package bimaru.solver.tasks;

import java.util.List;

import bimaru.Cell;
import bimaru.CellType;
import bimaru.Grid;
import bimaru.solver.CellActions;
import bimaru.solver.SolverTask;

public class FillRemainingCells implements SolverTask {

	private final Grid grid;

	public FillRemainingCells(Grid grid) {
		this.grid = grid;
	}
	
	@Override
	public void process(List<Cell> results) {
		grid.forEachLine(line -> {
			long remaining = line.getRemainingCount();
			long unknownCount = line.getUnknownCount();
			if (remaining == 0) {
				// all possible ships were found -> turn the unknown cells to WATER
				line.forEach(CellActions.convert(CellType.UNDEF, CellType.WATER, results, grid));
			} else if (remaining == unknownCount) {
				// all remaining cells must be ships
				line.forEach(CellActions.convert(CellType.UNDEF, CellType.SHIP, results, grid));
			}
		});
	}
}
