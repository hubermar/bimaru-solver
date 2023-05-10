package bimaru;

/**
 * @author mhuber
 *
 */
public class Block {

	private Orientation orientation;
	private int line;
	private int begin;
	private int end;
	
	public Block(Orientation orientation, int line, int begin, int end) {
		this.orientation = orientation;
		this.line = line;
		this.begin = begin;
		this.end = end;
	}

	public Orientation getOrientation() {
		return orientation;
	}
	
	public int getLineNo() {
		return line;
	}
	
	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}

	public int getLength() {
		return 1 + Math.abs(end - begin);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + begin;
		result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
		result = prime * result + end;
		result = prime * result + line;
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
		Block other = (Block) obj;
		if (begin != other.begin)
			return false;
		if (orientation != other.orientation)
			return false;
		if (end != other.end)
			return false;
		if (line != other.line)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Block.class.getSimpleName() + "[direction=" + orientation + " line=" + line + " begin=" + begin + " end=" + end + "]";
	}
}
