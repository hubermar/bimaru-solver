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
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import bimaru.solver.Solver;

public class Bimaru implements PropertyChangeListener {

	private static final Logger LOG = LoggerFactory.getLogger(Bimaru.class);

	private static final String CONFIG_FOLDER = "config";
	private static final String CONFIG_POSTFIX = ".json";

	private boolean debug;

	private Grid grid;

	private Solver solver;

	private Fleet fleet;

	private Bimaru(boolean debug) throws Exception {
		this.debug = debug;
		init();
	}

	private void init() throws IOException {		
		Ui.banner("", "Welcome to Bimaru", "Version 0.0.1", "");

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

	public static void main(String[] args) throws IOException {		
		Option debug = new Option("d", "debug", false, "enable debug output");
		Options options = new Options();
		options.addOption(debug);

		try {
			CommandLineParser parser = new DefaultParser();
        	CommandLine cmd = parser.parse(options, args);
			boolean debugEnabled = cmd.hasOption(debug.getOpt());
			
			Bimaru bimaru = new Bimaru(debugEnabled);
			boolean successful = bimaru.solve();
			if (successful) {
				Ui.banner("Solver successful !!!");
			} else {
				Ui.banner("Solver failed !!!");
			}
		} catch (Exception e) {
			LOG.error("Failed", e);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("utility-name", options);
			System.exit(1);
		}
	}
}
