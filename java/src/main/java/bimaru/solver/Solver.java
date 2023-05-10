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
	
	public boolean solve() {
		doPreliminaryTasks();
		doRepeatableTasks();
		return grid.allCellsHaveType();
	}
	
	private void doPreliminaryTasks() {
		if (debug) {
			grid.print();
		}
		List<SolverTask> tasks = Arrays.asList(
			new ConvertConfigCellTypes(grid),
			new FillZeroSum(grid)
		);
		processTasks(tasks);
	}
	
	private void doRepeatableTasks() {
		List<SolverTask> tasks = Arrays.asList(
			new FillRemainingCells(grid),
			new FindLargestShip(grid, fleet)
		);
		// loop through all tasks until no more results can be found
		int taskResultCount = 0;
		do {
			taskResultCount = processTasks(tasks);
		} while (taskResultCount > 0);
	}

	private int processTasks(List<SolverTask> tasks) {
		int affectectCellSum = 0;
		for (SolverTask task : tasks) {
			// repeat this task until it can't find more changed cells, then use next task
			int affectedCellCount = 0;
			do {
				List<Cell> affectedCells = processTask(task);
				updateFleet(affectedCells);
				affectedCellCount = affectedCells.size();
				affectectCellSum += affectedCellCount;
			} while (affectedCellCount > 0);
		}
		return affectectCellSum;
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
