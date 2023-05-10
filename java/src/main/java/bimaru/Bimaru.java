package bimaru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import bimaru.solver.Solver;

public class Bimaru {

	private static final String CONFIG_FOLDER = "config";
	
	private Bimaru() { }

	public static void main(String[] args) throws IOException {
		Options options = new Options();

        Option debug = new Option("d", "debug", false, "enable debug output");
        options.addOption(debug);

        // Option file = new Option("f", "file", true, "file with initial settings");
        // file.setRequired(true);
        // options.addOption(file);

        HelpFormatter formatter = new HelpFormatter();
        try {
        	CommandLineParser parser = new DefaultParser();
        	CommandLine cmd = parser.parse(options, args);

			List<String> configFiles = listConfigFiles();

			configFiles.stream()/*.filter(f -> f.endsWith(".json"))*/.forEach(
				f -> System.out.println(configFiles.indexOf(f) + " - " + f)				
			);

			int index = -1;
			do {	
				System.out.print("Choose: ");
				try (Scanner in = new Scanner(System.in)) {
					index = in.nextInt();
				}		
			}
			while (index < 0 || index > configFiles.size() - 1);

			Path configPath = Paths.get(CONFIG_FOLDER, configFiles.get(index));
			InputStream configStream = Bimaru.class.getClassLoader().getResourceAsStream(configPath.toString());
        	BimaruConfig config = readAndValidateConfig(configStream);
        	
        	Grid grid = createGrid(config);
        	Fleet fleet = createFleet(config);

    		Solver solver = new Solver(grid, fleet);
    		solver.setDebug(cmd.hasOption(debug.getOpt()));
    		solver.prepare();
    		solver.solve();

			if (!grid.allCellsHaveType()) {
				System.err.println("Solver failed !!!");
			}

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
            return;
        }
	}

	private static List<String> listConfigFiles() throws IOException {
		List<String> files = new ArrayList<>();
		try (InputStream in = Bimaru.class.getClassLoader().getResourceAsStream(CONFIG_FOLDER);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
				String resource;
			while ((resource = br.readLine()) != null) {
				files.add(resource);
			}
		}
		return files;
	}

	private static BimaruConfig readAndValidateConfig(InputStream is) throws IOException {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(is));
		BimaruConfig config = gson.fromJson(reader, BimaruConfig.class);
		config.validate();
		return config;
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
}
