package licob;

import constants.Configuration;
import gui.ChainItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChainSet {
	public static void addChainSet(String backupName, ChainItem[] items, String scriptContent, boolean isScriptEnabled) throws IOException {
		assert backupName != null && items != null && scriptContent != null;

		Path chainSetDirectory = Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		Files.createDirectories(chainSetDirectory);
		
		for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
			Path rulePath = Path.of(chainSetDirectory.toString(), Integer.toString(itemIndex));
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(rulePath.toString()), 8 * 1024)) {
			    String type = items[itemIndex].getType();
				bw.write(type + System.lineSeparator());
				String source = items[itemIndex].getSource();
				bw.write(source + System.lineSeparator());
				String dst = items[itemIndex].getDestination();
				bw.write(dst + System.lineSeparator());
				String exceptions = items[itemIndex].getExceptions();
				bw.write(exceptions + System.lineSeparator());
			}
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(chainSetDirectory.toString(), "_script")))) {
		    bw.write(isScriptEnabled + System.lineSeparator());
			bw.write(scriptContent);
		}
	}
}
