package bimaru.solver.tasks;

import java.util.List;

import bimaru.Cell;
import bimaru.CellType;
import bimaru.Grid;
import bimaru.solver.CellActions;
import bimaru.solver.SolverTask;

public class FillZeroSum implements SolverTask {

	private final Grid grid;

	public FillZeroSum(Grid grid) {
		this.grid = grid;
	}
	
	@Override
	public void process(List<Cell> results) {
		grid.forEachLine(line -> {
			if (line.getSum() == 0) {
				line.forEach(CellActions.convert(CellType.UNDEF, CellType.WATER, results, grid));
			}
		});
	}
}
