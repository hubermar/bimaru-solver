package bimaru.solver;

import java.util.List;

import bimaru.Cell;
import bimaru.CellType;
import bimaru.Grid;

public class CellActions {

	@FunctionalInterface
	public interface CellAction {
		void exec(Cell cell);
	}

	public static CellAction convert(CellType from, CellType to, List<Cell> results, Grid grid) {
		return cell -> {
			if (cell.hasType(from)) {
				results.add(new Cell(to, cell.getPosition()));
				if (CellType.SHIP == to) {
					grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() - 1);
					grid.createCellWithChecks(results, CellType.WATER, cell.getRow() - 1, cell.getColumn() + 1);
					grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() - 1);
					grid.createCellWithChecks(results, CellType.WATER, cell.getRow() + 1, cell.getColumn() + 1);
				}
			}
		};
	}
}
