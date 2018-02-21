package bimaru;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the file format.<br>
 * The lines may be in random order.<br>
 * <br>
 * <b>Format definition:</b><br>
 * SHIPS:length,count<br>
 * ROW_SUMS:r1|r2|rn<br>
 * COL_SUMS:c1|c2|cn<br>
 * GRID:matrix of {@link CellType} as comma separated rows<br>
 * <br>
 * <b>Example:</b>
 * <p style="font-family: Lucida Console, Monaco, monospace">
 * SHIPS:2,3|1,4<br>
 * ROW_SUMS:0|2|1|3<br>
 * COL_SUMS:1|1|2|1<br>
 * GRID: ,X, ,~|~, , , | , , , | , ,X, <br>
 * </p>
 * 
 * @author mhuber
 */
public final class BimaruConfig {
	
	private static final String VERSION = "1.0";
	
	private String version;
	private List<ShipType> ships = new ArrayList<>();
	private CellType[/*rows*/][/*columns*/] grid;
	private int[] columnSums;
	private int[] rowSums;
	
	public void validate() throws IOException {
		validateVersion();
		validateHeight();
		validateWidth();
	}

	private void validateWidth() throws IOException {
		for (CellType[] row : grid) {
			if (columnSums.length != row.length) {
				throw new IOException(
						"Dimension missmatch! colSums.length : grid.width --> " + columnSums.length + " : " + row.length);
			}
		}
	}

	private void validateHeight() throws IOException {
		if (rowSums.length != grid.length) {
			throw new IOException("Dimension missmatch! rowSums.length : grid.height --> " + rowSums.length + " : " + grid.length) ;
		}
	}

	private void validateVersion() throws IOException {
		if (!VERSION.equals(version)) {
			throw new IOException("Version missmatch! Expected " + VERSION + " but was " + version) ;
		}
	}

	public List<ShipType> getShips() {
		return ships;
	}

	public void setShips(List<ShipType> ships) {
		this.ships = ships;
	}

	public CellType[][] getGrid() {
		return grid;
	}

	public void setGrid(CellType[][] grid) {
		this.grid = grid;
	}

	public int[] getColumnSums() {
		return columnSums;
	}

	public void setColumnSums(int[] columnSums) {
		this.columnSums = columnSums;
	}

	public int[] getRowSums() {
		return rowSums;
	}

	public void setRowSums(int[] rowSums) {
		this.rowSums = rowSums;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
