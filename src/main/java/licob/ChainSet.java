package licob;

import constants.Configuration;
import gui.ChainRule;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class ChainSet {
	public static void addChainSet(String backupName, ChainRule[] items, String scriptContent, boolean isScriptEnabled) throws IOException {
		assert backupName != null && items != null && scriptContent != null;

		Path chainSetDirectory = Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		Files.createDirectories(chainSetDirectory);
		
		for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
			Path rulePath = Path.of(chainSetDirectory.toString(), Integer.toString(itemIndex));
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(rulePath.toString()), 8 * 1024)) {
			    String type = items[itemIndex].type;
				bw.write(type + System.lineSeparator());
				String source = items[itemIndex].source == null ? "" : items[itemIndex].source;
				bw.write(source + System.lineSeparator());
				String dst = items[itemIndex].destination == null ? "" : items[itemIndex].destination;
				bw.write(dst + System.lineSeparator());
				String exceptions = items[itemIndex].exceptions;
				bw.write(exceptions + System.lineSeparator());
			}
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(chainSetDirectory.toString(), "_script")))) {
		    bw.write(isScriptEnabled + System.lineSeparator());
			bw.write(scriptContent);
		}
	}

	public static ChainRule[] getChainSet(String backupName) throws IOException {
		assert backupName != null;

		File backupFile = new File(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		if (!(backupFile.exists() && backupFile.isDirectory())) return null;

		String[] rules = backupFile.list((file, string) -> string.matches("^-?\\d+$"));
		Arrays.sort(rules);
		ChainRule[] chainSet = new ChainRule[rules.length];
		for (int ruleIndex = 0; ruleIndex < rules.length; ruleIndex++) {
			try (
				BufferedReader reader =
					new BufferedReader(new FileReader(new File(backupFile, rules[ruleIndex])), 1024 * 8)
			) {
				chainSet[ruleIndex] = new ChainRule();
			    chainSet[ruleIndex].type = reader.readLine();
				chainSet[ruleIndex].source = reader.readLine();
				if (chainSet[ruleIndex].source.isEmpty()) chainSet[ruleIndex].type = null;
				chainSet[ruleIndex].destination = reader.readLine();
				if (chainSet[ruleIndex].destination.isEmpty()) chainSet[ruleIndex].type = null;

				StringBuilder exceptions = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) exceptions.append(line);
				chainSet[ruleIndex].exceptions = exceptions.toString();
			}
		}

		return chainSet;
	}

	public static void deleteChainSet(String backupName) throws IOException {
		if (backupName == null) return;

		Path backupPath = Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		Files.walkFileTree(backupPath, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
				Files.delete(path);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
				Files.delete(path);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public static boolean retrieveScript(File backupFile, CharBuffer scriptContent) {
		assert backupFile != null && backupFile.isDirectory();

		File scriptFile = new File(backupFile, "_script");
		if (!(scriptFile.exists() && scriptFile.isFile())) return false;

		boolean scriptActive = false;
		try (BufferedReader bw = new BufferedReader(new FileReader(scriptFile))) {
		    scriptActive = Boolean.parseBoolean(bw.readLine());
			bw.read(scriptContent);
		} catch (IOException | NullPointerException exception) {
			return scriptActive;
		}
		scriptContent.flip();
		return scriptActive;
	}

	public static int retrieveChainNumber(File backupFile) {
		assert backupFile != null && backupFile.isDirectory();

		return backupFile.list((file, s) -> s.matches("^\\d+$")).length;
	}

	public static long retrieveDate(File backupFile) {
		assert backupFile != null && backupFile.isDirectory();

		File dateFile = new File(backupFile, "_date");
		if (!(dateFile.exists() && dateFile.isFile())) return 0;

		long date;
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dateFile))) {
		    date = Long.parseLong(new String(bis.readAllBytes()));
		} catch (IOException | NumberFormatException exception) {
			return 0;
		}
		return date;
	}
}
