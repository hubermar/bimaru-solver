package bimaru;

public class Cell {

	private CellType type;
	private Position position;
	
	public Cell(CellType type, Position position) {
		this.type = type;
		this.position = position;
	}

	public CellType getType() {
		return type;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public int getRow() {
		return position.getRow();
	}
	
	public int getColumn() {
		return position.getColumn();
	}
	
	public boolean hasType(CellType type) {
		return this.type == type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Cell other = (Cell) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Cell.class.getSimpleName() + "[type=" + type + " position=" + position + "]";
	}

}
