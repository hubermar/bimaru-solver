package bimaru;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import bimaru.solver.Solver;

public class Bimaru {

	private static final Logger LOG = LoggerFactory.getLogger(Bimaru.class);
	
	private Bimaru() { }

	public static void main(String[] args) throws IOException {
		Options options = new Options();

        Option debug = new Option("d", "debug", false, "enable debug output");
        options.addOption(debug);

        Option file = new Option("f", "file", true, "file with initial settings");
        file.setRequired(true);
        options.addOption(file);

        HelpFormatter formatter = new HelpFormatter();
        try {
        	CommandLineParser parser = new DefaultParser();
        	CommandLine cmd = parser.parse(options, args);

        	String fileName = cmd.getOptionValue(file.getOpt());
        	LOG.info("reading config from {}", fileName);
//        	BimaruConfig config = new BimaruConfig();
//        	config.read(Bimaru.class.getClassLoader().getResourceAsStream(fileName));

        	Gson gson = new Gson();
        	InputStream is = Bimaru.class.getClassLoader().getResourceAsStream(fileName);
        	JsonReader reader = new JsonReader(new InputStreamReader(is));
        	BimaruConfig config = gson.fromJson(reader, BimaruConfig.class);
        	config.validate();
        	
        	Grid grid = createGrid(config);
        	Fleet fleet = createFleet(config);

    		Solver solver = new Solver(grid, fleet);
    		solver.setDebug(cmd.hasOption(debug.getOpt()));
    		solver.prepare();
    		solver.solve();

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
            return;
        }
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
