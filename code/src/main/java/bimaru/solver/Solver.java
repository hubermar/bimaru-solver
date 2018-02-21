package bimaru.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bimaru.Cell;
import bimaru.CellType;
import bimaru.Fleet;
import bimaru.Grid;
import bimaru.solver.tasks.ConvertConfigCellTypes;
import bimaru.solver.tasks.FillRemainingCells;
import bimaru.solver.tasks.FillZeroSum;
import bimaru.solver.tasks.FindLargestShip;

public class Solver {

	private static Logger LOG = LoggerFactory.getLogger(Solver.class);
	
	private final Grid grid;
	private final Fleet fleet;

	private boolean debug;


	public Solver(Grid grid, Fleet fleet) {
		this.grid = grid;
		this.fleet = fleet;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public void prepare() {
		if (debug) {
			grid.print();
		}
		List<SolverTask> tasks = Arrays.asList(
			new ConvertConfigCellTypes(grid),
			new FillZeroSum(grid)
		);
		processTasks(tasks);
	}
	
	public void solve() {
		List<SolverTask> tasks = Arrays.asList(
			new FillRemainingCells(grid),
			new FindLargestShip(grid, fleet)
		);
		processTasks(tasks);
		if (debug) {
			grid.print();
		}
	}

	private void processTasks(List<SolverTask> tasks) {
		// loop through all tasks until no more results can be found
		boolean executeTasks = true;
		while (executeTasks) {
			int taskResultCount = 0;
			for (SolverTask task : tasks) {
				// repeat this task until it can't find more results, then use next task
				boolean repeat = true;
				while (repeat) {					
					List<Cell> affectedCells = processTask(task);
					int affectectCellCount = affectedCells.size();
					repeat = affectectCellCount > 0;
					taskResultCount += affectectCellCount;
					updateFleet(affectedCells);
				}
			}
			executeTasks = taskResultCount > 0;
		}
	}
	
	private void updateFleet(List<Cell> affectedCells) {
		long shipCount = affectedCells.stream().filter(c -> c.hasType(CellType.SHIP)).count();
		if (shipCount > 0) {
			fleet.update(grid);
		}
	}

	private List<Cell> processTask(SolverTask task) {
		LOG.debug("processing task {} ...", task.getClass().getSimpleName());
		List<Cell> taskResults = new ArrayList<>();
		task.process(taskResults);
		LOG.debug("task {} found {} results.", task.getClass().getSimpleName(), taskResults.size());
		taskResults.stream().forEach(grid::apply);
		if (debug && !taskResults.isEmpty()) {
			grid.print();
		}
		return taskResults;
	}

}
