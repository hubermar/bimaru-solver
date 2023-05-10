package bimaru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipType {

	private static final Logger LOG = LoggerFactory.getLogger(ShipType.class);
	
	private final int size;
	private final int stock;
	
	private int quantity;
	
	public ShipType(int size, int stock) {
		this.size = size;
		this.stock = stock;
	}

	public void reset() {
		quantity = stock;
	}
	
	public void decrease() {
		quantity--;
		LOG.debug("size {}: new quantity={}", size, quantity);
		if (quantity < 0) {
			throw new IllegalStateException("quantity for size=" + size + " can not go below zero");
		}
	}
	
	public boolean isDone() {
		return quantity == 0;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getStock() {
		return stock;
	}
	
	public int getQuantity() {
		return quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + quantity;
		result = prime * result + size;
		result = prime * result + stock;
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
		ShipType other = (ShipType) obj;
		if (quantity != other.quantity)
			return false;
		if (size != other.size)
			return false;
		if (stock != other.stock)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ShipType.class.getSimpleName() + "[size=" + size + " stock=" + stock + " quantity=" + quantity + "]";
	}
}
