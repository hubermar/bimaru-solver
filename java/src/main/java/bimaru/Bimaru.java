package bimaru;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import bimaru.solver.Solver;

public class Bimaru implements PropertyChangeListener {

	private static final Logger LOG = LoggerFactory.getLogger(Bimaru.class);

	private static final String CONFIG_FOLDER = "config";
	private static final String CONFIG_POSTFIX = ".json";

	private static final String MENU_PLAY = "Play Bimaru";
	private static final String MENU_DEBUG = "Toggle debug output";
	private static final String MENU_QUIT = "Quit";
	private static final List<String> MENUS = Arrays.asList(MENU_PLAY, MENU_DEBUG, MENU_QUIT);

	private boolean debug;

	private Grid grid;

	private Solver solver;

	private Fleet fleet;

	private Bimaru(boolean debug) throws Exception {
		this.debug = debug;
		init();
	}

	private void init() throws IOException {		
		List<String> configs = enumerateConfigs();
		String selectedConfig = Ui.selectItem("Choose configuration: ", configs, Object::toString);
		BimaruConfig config = readConfig(selectedConfig);
		config.validate();

		grid = createGrid(config);
		fleet = createFleet(config);
		
		solver = new Solver(grid, fleet);
		solver.addPropertyChangeListener(this);
	}

	public boolean solve() {
		return solver.solve();
	}

	private List<String> enumerateConfigs() throws IOException {
		List<String> files = new ArrayList<>();
		try (InputStream in = Bimaru.class.getClassLoader().getResourceAsStream(CONFIG_FOLDER);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
				String resource;
			while ((resource = br.readLine()) != null) {
				files.add(resource.replace(CONFIG_POSTFIX, ""));
			}
		}
		return files;
	}

	private BimaruConfig readConfig(String selectedConfig) throws IOException {
		Path configPath = Paths.get(CONFIG_FOLDER, selectedConfig + CONFIG_POSTFIX);
		InputStream configStream = Bimaru.class.getClassLoader().getResourceAsStream(configPath.toString());
		JsonReader reader = new JsonReader(new InputStreamReader(configStream));
		Gson gson = new Gson();
		return gson.fromJson(reader, BimaruConfig.class);
	}
	
	private static Fleet createFleet(BimaruConfig config) {
		return new Fleet(config.getShips());
	}

	private static Grid createGrid(BimaruConfig config) {
		Grid grid = new Grid(config.getRowSums(), config.getColumnSums());
		for (int row = 0; row < config.getGrid().length; row++) {
			for (int col = 0; col < config.getGrid()[row].length; col++) {
				grid.apply(new Cell(config.getGrid()[row][col], new Position(row, col)));
			}
		}
		return grid;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case Solver.PROP_TASK_COMPLETED:
				if (debug) {
					Ui.writeGrid(grid);
				}
				break;
			default:
				LOG.warn("Unhandled property " + evt.getPropertyName());
		}
	}	

	public static void main(String[] args) throws Exception {
		Ui.banner("", "Welcome to Bimaru", "Version 0.0.1", "");
		boolean running = true;
		boolean debug = false;
		while (running) {
			Ui.write("Debug is " + (debug ? "on" : "off"));
			String menu = Ui.selectItem("Choose: ", MENUS, Object::toString);
			switch (menu) {
			case MENU_PLAY:
				play(debug);
				break;
			case MENU_DEBUG:
				debug = !debug;
				break;
			case MENU_QUIT:
				running = false;
				break;
			default:
			}
		}
		Ui.banner("Bye");
	}

	private static void play(boolean debug) throws Exception {
		Bimaru bimaru = new Bimaru(debug);
		boolean successful = bimaru.solve();
		if (successful) {
			Ui.banner("Solver successful !!!");
		} else {
			Ui.banner("Solver failed !!!");
		}
	}
}
