package bimaru;

import com.google.gson.annotations.SerializedName;

public enum CellType {
	@SerializedName(" ")
	UNDEF(" "), 
	@SerializedName("~")
	WATER("~"), 
	@SerializedName("X")
	SHIP("X"), 
	@SerializedName("U")
	SHIP_UP("U"), 
	@SerializedName("D")
	SHIP_DOWN("D"), 
	@SerializedName("L")
	SHIP_LEFT("L"), 
	@SerializedName("R")
	SHIP_RIGHT("R"),
	@SerializedName("S")
	SHIP_SINGLE("S");

	private String symbol;
	
	CellType(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public static CellType fromSymbol(String s) {
		for (CellType c : values()) {
			if (c.symbol.equalsIgnoreCase(s)) {
				return c;
			}
		}
		return UNDEF; 
	}
}