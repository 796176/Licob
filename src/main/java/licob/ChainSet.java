/* Licob - Licob Is a Chain-Oriented Backup
 * Copyright (C) 2024 Yegore Vlussove
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package licob;

import constants.Configuration;
import gui.ChainRule;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class ChainSet {
	public static void createChainSet(String backupName) throws IOException {
		assert backupName != null;

		String[] chainSets = new File(Configuration.CHAIN_SETS_DIRECTORY).list();
		Arrays.sort(chainSets);
		if (Arrays.binarySearch(chainSets, backupName) >= 0)
			throw new IOException("The chain set with such name already exist");

		File backupDirectory = new File(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		if (!backupDirectory.mkdir())
			throw new IOException("Failed to create the " + backupDirectory + " directory");

		try (FileWriter scriptStatus = new FileWriter(new File(backupDirectory, "_scriptstatus"))) {
			scriptStatus.write(Boolean.toString(false));
		}

		if (!new File(backupDirectory, "_script").createNewFile()) {
			throw new IOException("Failed to create an empty script file");
		}
	}

	public static void flushChainSet(
		String backupName, ChainRule[] items, String scriptContent, boolean isScriptEnabled
	) throws IOException {
		assert backupName != null && items != null && scriptContent != null;

		File backupFile = new File(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		if (!(backupFile.exists() && backupFile.isDirectory())) return;

		File chainSetDirectory = new File(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		if (ChainSet.retrieveChainNumber(backupName) > 0) {
			Files.walkFileTree(chainSetDirectory.toPath(), new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
					if (file.toFile().getName().matches("^-?\\d+$"))
						Files.delete(file);
					return FileVisitResult.CONTINUE;
				}
			});
		}

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
		File scriptFile = new File(chainSetDirectory.toString(), "_script");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(scriptFile))) {
			bw.write(scriptContent);
		}
		scriptFile.setExecutable(true);

		File scriptStatusFile = new File(chainSetDirectory.toString(), "_scriptstatus");
		try (FileWriter fw = new FileWriter(scriptStatusFile)) {
			fw.write(isScriptEnabled + System.lineSeparator());
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
				if (chainSet[ruleIndex].source.isEmpty()) chainSet[ruleIndex].source = null;
				chainSet[ruleIndex].destination = reader.readLine();
				if (chainSet[ruleIndex].destination.isEmpty()) chainSet[ruleIndex].destination = null;

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

		File backupFile = new File(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		if (!(backupFile.exists() && backupFile.isDirectory())) return;

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

	public static boolean retrieveScript(String backupName, CharBuffer scriptContent) {
		assert backupName != null;

		File scriptStatusFile = Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName, "_scriptstatus").toFile();
		if (!(scriptStatusFile.exists() && scriptStatusFile.isFile())) return false;

		boolean scriptActive = false;
		try (BufferedReader br = new BufferedReader(new FileReader(scriptStatusFile))) {
			scriptActive = Boolean.parseBoolean(br.readLine());
		} catch (IOException exception) {
			return scriptActive;
		}

		File scriptFile = Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName, "_script").toFile();
		if (!(scriptFile.exists() && scriptFile.isFile())) return false;

		try (BufferedReader bw = new BufferedReader(new FileReader(scriptFile))) {
			bw.read(scriptContent);
		} catch (IOException | NullPointerException exception) {
			return scriptActive;
		}
		scriptContent.flip();
		return scriptActive;
	}

	public static int retrieveChainNumber(String backupName) {
		assert backupName != null;

		File backupFile = new File(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		if (!(backupFile.exists() && backupFile.isDirectory())) return 0;
		return backupFile.list((file, s) -> s.matches("^\\d+$")).length;
	}

	public static long retrieveDate(String backupName) {
		assert backupName != null;

		File dateFile = Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName, "_date").toFile();
		if (!(dateFile.exists() && dateFile.isFile())) return 0;

		long date;
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dateFile))) {
		    date = Long.parseLong(new String(bis.readAllBytes()));
		} catch (IOException | NumberFormatException exception) {
			return 0;
		}
		return date;
	}

	public static void updateDate(String backupName) throws IOException {
		assert backupName != null;

		File backupFile = new File(Configuration.CHAIN_SETS_DIRECTORY, backupName);
		if (!(backupFile.exists() && backupFile.isDirectory())) return;

		File dateFile = Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName, "_date").toFile();

		try (FileWriter dateWriter = new FileWriter(dateFile)){
			dateWriter.write(Long.toString(System.currentTimeMillis()));
		}
	}

	public static File getScriptFile(String backupName) {
		assert backupName != null;

		File dateFile = Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName, "_script").toFile();
		if (!(dateFile.exists() && dateFile.isFile())) return null;

		return Path.of(Configuration.CHAIN_SETS_DIRECTORY, backupName, "_script").toFile();
	}

	public static String[] getBackupItemNames() {
		return new File(Configuration.CHAIN_SETS_DIRECTORY).list();
	}

	public static void renameBackupItem(String oldName, String newName) throws IOException {
		assert oldName != null && newName != null;

		File backupFile = new File(Configuration.CHAIN_SETS_DIRECTORY, oldName);
		if (!(backupFile.exists() && backupFile.isDirectory())) return;

		Path oldBackupPath = Path.of(Configuration.CHAIN_SETS_DIRECTORY, oldName);
		Files.walkFileTree(oldBackupPath, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				String newDir = dir.toString().replace(oldName, newName);
				if (!new File(newDir).mkdir()) {
					throw new IOException("Failed to create the " + newDir + " directory");
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.move(file, Path.of(file.toString().replace(oldName, newName)));
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
