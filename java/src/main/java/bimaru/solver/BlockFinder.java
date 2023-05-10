package bimaru.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import bimaru.Block;
import bimaru.Cell;
import bimaru.CellType;
import bimaru.Line;

public class BlockFinder {

	private static final int UNDEF = -1;
	
	private final Set<CellType> targets;
	private final Set<CellType> borders;
	
	private List<Block> blocks = new ArrayList<>();

	public BlockFinder(Set<CellType> targets) {
		this(targets, Collections.emptySet());
	}

	public BlockFinder(Set<CellType> targets, Set<CellType> borders) {
		this.targets = targets;
		this.borders = borders;
	}
	
	public int find(Line line) {
		if (borders.isEmpty()) {
			return findWithoutBorder(line);
		} else {
			return findWithBorder(line);
		}
	}
	
	private int findWithoutBorder(Line line) {
		blocks.clear();
		int begin = UNDEF;
		int end = UNDEF;
		for (int index = 0; index < line.getSize(); index++) {
			Cell c = line.getCell(index);
			if (targets.contains(c.getType())) {
				if (begin == UNDEF) {
					// new block
					begin = index;
				}
				// continue block
				end = index;
			} else {
				if (begin != UNDEF) {
					// end of block
					blocks.add(new Block(line.getDirection(), line.getLineNo(), begin, end));
					begin = UNDEF;
					end = UNDEF;
				}
			}
		}
		// save last block
		if (begin != UNDEF) {
			blocks.add(new Block(line.getDirection(), line.getLineNo(), begin, end));
		}
		return blocks.size();
	}

	private int findWithBorder(Line line) {
		blocks.clear();
		boolean armed = false;
		boolean disarmed = false;
		int begin = UNDEF;
		int end = UNDEF;
		for (int index = 0; index < line.getSize(); index++) {
			Cell c = line.getCell(index);
			if (borders.contains(c.getType())) {
				// found border
				if (begin == UNDEF) {
					// found starting border
					armed = true;
				} else if (armed && begin != UNDEF) {
					// found closing border
					disarmed = true;
				}
			} else if (targets.contains(c.getType())) {
				if (armed) {
					if (disarmed) {
						// end of block
						blocks.add(new Block(line.getDirection(), line.getLineNo(), begin, end));
						// start a new
						begin = index;
						disarmed = false;
					}
					if (begin == UNDEF) {
						// new block
						begin = index;
					}
					// continue block
					end = index;
				}
			} else {
				if (armed && disarmed && begin != UNDEF) {
					// end of block
					blocks.add(new Block(line.getDirection(), line.getLineNo(), begin, end));
				}
				begin = UNDEF;
				end = UNDEF;
				armed = false;
				disarmed = false;
			}
		}
		// save last block
		if (begin != UNDEF) {
			blocks.add(new Block(line.getDirection(), line.getLineNo(), begin, end));
		}
		return blocks.size();
	}

	public List<Block> getAllBlocks() {
		return Collections.unmodifiableList(blocks);
	}

	public List<Block> getBlocksByMinSize(int minSize) {
		return blocks.stream().filter(b -> b.getLength() >= minSize).collect(Collectors.toList());
	}
}
