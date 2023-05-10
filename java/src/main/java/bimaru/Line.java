package bimaru;

import java.util.List;

import bimaru.solver.CellActions.CellAction;

public class Line {

	private Orientation direction;
	private List<Cell> cells;
	private long sum;
	private int lineNo;
	
	public Line(Orientation direction, int lineNo, List<Cell> cells, int sum) {
		this.direction = direction;
		this.lineNo = lineNo;
		this.cells = cells;
		this.sum = sum;
	}

	public Orientation getDirection() {
		return direction;
	}
	
	public int getLineNo() {
		return lineNo;
	}
	
	public int getSize() {
		return cells.size();
	}

	public Cell getCell(int index) {
		return cells.get(index);
	}
	
	public long getSum() {
		return sum;
	}
	
	public void forEach(CellAction action) {
		for (Cell c : cells) {
			action.exec(c);
		}
	}

	public long getRemainingCount() {
		return sum - cells.stream().filter(c -> c.hasType(CellType.SHIP)).count();
	}
	
	public long getUnknownCount() {
		return cells.stream().filter(c -> c.hasType(CellType.UNDEF)).count();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cells == null) ? 0 : cells.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + lineNo;
		result = prime * result + (int) (sum ^ (sum >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (cells == null) {
			if (other.cells != null)
				return false;
		} else if (!cells.equals(other.cells))
			return false;
		if (direction != other.direction)
			return false;
		if (lineNo != other.lineNo)
			return false;
		if (sum != other.sum)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Line.class.getSimpleName() + "[direction=" + direction + " cells=" + cells + " sum=" + sum + " lineNo=" + lineNo + "]";
	}

}

