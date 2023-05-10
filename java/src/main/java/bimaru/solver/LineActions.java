package bimaru.solver;

import bimaru.Line;

public class LineActions {

	private LineActions() { }
	
	@FunctionalInterface
	public interface LineAction {
		void exec(Line line);
	}
}
