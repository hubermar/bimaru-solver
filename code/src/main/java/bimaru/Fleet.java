package bimaru;

import java.util.List;

import bimaru.solver.ShipFinder;

public class Fleet {

	private List<ShipType> ships;

	public Fleet(List<ShipType> ships) {
		this.ships = ships;
	}

	public ShipType getLargestUnresolvedShipType() {
		ShipType result = null;
		for (ShipType s : ships) {
			if (s.isDone()) {
				continue;
			}
			if (result == null || s.getSize() > result.getSize()) {
				result = s;
			}
		}
		return result;
	}
	
	public void update(Grid grid) {
		ships.forEach(ShipType::reset);
		ShipFinder finder = new ShipFinder(grid);
		for (Block block : finder.find()) {
			int size = block.getLength();
			ships.stream().filter(s -> s.getSize() == size).forEach(ShipType::decrease);
		}
	}
}
