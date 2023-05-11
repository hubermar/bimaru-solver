package bimaru;

import java.util.Arrays;
import java.util.List;

public class Ui {
    
    private static final int DISPLAY_WITH = 80;

    @FunctionalInterface
    public static interface DisplayTextSupplier<T> {
        String get(T t);
    }

    public static <T> T selectItem(String question, List<T> items, DisplayTextSupplier<T> displayTextSupplier ) {
        items.stream().forEach(
            f -> write(items.indexOf(f) + " - " + displayTextSupplier.get(f))				
        );
        int index = -1;
        do {
            String input = read(question);
            if (input != null) {
                try {
                    index = Integer.valueOf(input);
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        while (index < 0 || index > items.size() - 1);
        Ui.write("");
        return items.get(index);
    }

    public static void banner(String... texts) {
        Arrays.stream(texts).forEach(t -> write(center((t.isEmpty() ? t : " " + t + " "), DISPLAY_WITH, '*')));
        Ui.write("");
    }

	public static void writeGrid(Grid grid) {
		System.out.print("  ");

		for (int col = 0; col < grid.getColumnCount(); col++) {
			System.out.print(" " + grid.getColumnSum(col));
		}
		System.out.println();
		for (int row = 0; row < grid.getRowCount(); row++) {
			System.out.print(grid.getRowSum(row) + " ");
			for (int col = 0; col < grid.getColumnCount(); col++) {
				System.out.print(" ");
				System.out.print(grid.get(row, col).getSymbol());
			}
			System.out.println("");
		}
		System.out.println();
	}

    private static String center(String s, int size, char pad) {
        if (s == null || size <= s.length()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }

    public static void write(String s) {
        System.out.println(s);
    }

    public static String read(String s) {
        return System.console().readLine(s);
    }
}
