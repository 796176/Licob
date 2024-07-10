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

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LicobEngineTests {
	@TempDir
	private static File sourceDir;
	@TempDir
	private static File dstDir;

	private void clear(File dir) throws IOException {
		Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<>() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
				Files.delete(path);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
				if (!path.equals(dir.toPath()))
					Files.delete(path);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	@Nested
	class CreateOneFileBackup {
		private final String fileName = "licob";
		@BeforeEach
		void beforeEach() throws IOException{
			try (FileWriter fw = new FileWriter(new File(sourceDir, fileName))) {
				fw.write(fileName);
			}
		}

		@AfterEach
		void afterEach() throws IOException{
			clear(sourceDir);
			clear(dstDir);
		}

		@Test
		void backupIntegrityCheck() throws IOException, InterruptedException {
			LicobEngineInterface engine = new LicobEngine();
			engine.createBackup(new File(sourceDir, fileName), dstDir);
			while (!engine.getStatus().isFinished()) {
				Thread.sleep(50);
			}

			File backupFile = new File(dstDir, fileName);
			assertTrue(backupFile.exists(), "Backup wasn't created");

			CharBuffer buffer = CharBuffer.allocate(1024);
			try (FileReader fr = new FileReader(backupFile)){
				fr.read(buffer);
				buffer.flip();
			}
			assertEquals(buffer.toString(), fileName, "The content is corrupt");

			assertEquals(1, dstDir.list().length, "The amount of the created files doesn't match");
		}

		@Test
		void backupStatusExamination() throws IOException, InterruptedException {
			LicobEngineInterface engine = new LicobEngine();
			engine.createBackup(new File(sourceDir, fileName), dstDir);
			LicobStatus status = engine.getStatus();
			while (!engine.getStatus().isFinished()) {
				Thread.sleep(50);
			}
			assertEquals(1, status.getAllFiles().size(), "The amount of the files doesn't match");
			File backupFile = new File(sourceDir, fileName);
			assertEquals(backupFile, status.getAllFiles().getFirst(), "The file " + backupFile + " is missing");
		}
	}

	@Nested
	class CreateMultipleFilesBackup {
		private final static int fileNumber = 1000;

		@BeforeEach
		void beforeEach() throws IOException {
			for (int fileIndex = 0; fileIndex < fileNumber; fileIndex++) {
				try (FileWriter fw = new FileWriter(new File(sourceDir, Integer.toString(fileIndex)))) {
				    fw.write(Integer.toString(fileIndex));
				}
			}
		}

		@AfterEach
		void afterEach() throws IOException {
			clear(sourceDir);
			clear(dstDir);
		}

		static Stream<Arguments> exceptionsProvider() {
			return Stream.of(
				Arguments.of((Object) new File[]{}),
				Arguments.of((Object) IntStream
					.iterate(0, (n) -> new Random(System.currentTimeMillis() + n).nextInt(fileNumber))
					.limit(fileNumber / 10)
					.distinct()
					.mapToObj((i) -> new File(sourceDir, Integer.toString(i)))
					.toArray(File[]::new))
			);
		}

		@ParameterizedTest
		@MethodSource("exceptionsProvider")
		void backupIntegrityCheck(File[] exceptions) throws IOException, InterruptedException {
			LicobEngineInterface engine = new LicobEngine();
			engine.backupContent(sourceDir, dstDir, exceptions.clone());
			Arrays.sort(exceptions);
			while (!engine.getStatus().isFinished()) {
				Thread.sleep(50);
			}

			for (int fileIndex = 0; fileIndex < fileNumber; fileIndex++) {
				if (Arrays.binarySearch(exceptions, new File(sourceDir, Integer.toString(fileIndex))) >= 0)
					continue;

				File backupFile = new File(dstDir, Integer.toString(fileIndex));
				assertTrue(backupFile.exists(), "The file " + backupFile + " is missing");
				CharBuffer buffer = CharBuffer.allocate(1024);
				try (FileReader fr = new FileReader(backupFile)) {
					fr.read(buffer);
					buffer.flip();
				}
				assertEquals(
					buffer.toString(),
					Integer.toString(fileIndex),
					"The content of the file " + backupFile + " is corrupt"
				);
			}
			assertEquals(
				fileNumber - exceptions.length,
				dstDir.list().length,
				"The amount of the created files doesn't match"
			);
		}

		@ParameterizedTest
		@MethodSource("exceptionsProvider")
		void backupStatusExamination(File[] exceptions) throws IOException, InterruptedException {
			LicobEngineInterface engine = new LicobEngine();
			engine.backupContent(sourceDir, dstDir, exceptions.clone());
			Arrays.sort(exceptions);
			LicobStatus status = engine.getStatus();
			while (!status.isFinished()) {
				Thread.sleep(50);
			}

			assertEquals(
				fileNumber - exceptions.length,
				status.getAllFiles().size(),
				"The amount of the file doesn't match"
			);
			File[] allFiles = status.getAllFiles().toArray(new File[0]);
			Arrays.sort(allFiles);
			for (int fileIndex = 0; fileIndex < fileNumber; fileIndex++) {
				File backupFile = new File(sourceDir, Integer.toString(fileIndex));
				if (Arrays.binarySearch(exceptions, backupFile) >= 0) continue;
				assertTrue(
					Arrays.binarySearch(allFiles, backupFile) >= 0,
					"The file " + backupFile + " is missing"
				);
			}
		}

		@RepeatedTest(value = 3)
		void backupInterruptionCheck() throws IOException{
			LicobEngineInterface engine = new LicobEngine();
			engine.backupContent(sourceDir, dstDir);
			assertTimeout(Duration.of(200, ChronoUnit.MILLIS), engine::interrupt);
		}
	}

	@Nested
	class CreateDirectoryBackup{
		private static final String parentDir = "licob";
		private static final int fileNumber = 1000;

		@BeforeEach
		void beforeEach() throws IOException{
			File backupDir = new File(sourceDir, parentDir);
			assertTrue(backupDir.mkdir(), "Failed to create a directory");

			for (int fileIndex = 0; fileIndex < fileNumber; fileIndex++) {
				try (FileWriter fw = new FileWriter(new File(backupDir, Integer.toString(fileIndex)))) {
				    fw.write(Integer.toString(fileIndex));
				}
			}
		}

		@AfterEach
		void afterEach() throws IOException{
			clear(sourceDir);
			clear(dstDir);
		}

		static Stream<Arguments> exceptionsProvider() {
			return Stream.of(
				Arguments.of((Object) new File[]{}),
				Arguments.of((Object) IntStream
					.iterate(0, (n) -> new Random(System.currentTimeMillis() + n).nextInt(fileNumber))
					.limit(fileNumber / 10)
					.distinct()
					.mapToObj((i) -> Path.of(sourceDir.getAbsolutePath(), parentDir, Integer.toString(i)).toFile())
					.toArray(File[]::new))
			);
		}

		@ParameterizedTest
		@MethodSource("exceptionsProvider")
		void backupIntegrityCheck(File[] exceptions) throws IOException, InterruptedException {
			LicobEngineInterface engine = new LicobEngine();
			engine.createBackup(new File(sourceDir, parentDir), dstDir, exceptions.clone());
			Arrays.sort(exceptions);
			while (!engine.getStatus().isFinished()) {
				Thread.sleep(50);
			}

			File backupDir = new File(dstDir, parentDir);
			assertTrue(backupDir.exists(), "Backup wasn't created");
			for (int fileIndex = 0; fileIndex < fileNumber; fileIndex++) {
				if (
					Arrays.binarySearch(
						exceptions,
						Path.of(sourceDir.getAbsolutePath(), parentDir, Integer.toString(fileIndex)).toFile()
					) >= 0
				) continue;

				File backupFile = new File(backupDir, Integer.toString(fileIndex));
				assertTrue(backupFile.exists(), "The file " + backupFile + " is missing");

				CharBuffer buffer = CharBuffer.allocate(1024);
				try (FileReader fr = new FileReader(backupFile)) {
				    fr.read(buffer);
					buffer.flip();
				}
				assertEquals(
					buffer.toString(),
					Integer.toString(fileIndex),
					"The content of the file " + backupFile + " is corrupt"
				);
				assertEquals(1, dstDir.list().length, "Too many files were created in the destination directory");
				assertEquals(
					fileNumber - exceptions.length,
					backupDir.list().length,
					"The amount of the created files doesn't match"
				);
			}
		}

		@ParameterizedTest
		@MethodSource("exceptionsProvider")
		void backupStatusExamination(File[] exceptions) throws IOException, InterruptedException{
			LicobEngineInterface engine = new LicobEngine();
			engine.createBackup(new File(sourceDir, parentDir), dstDir, exceptions.clone());
			Arrays.sort(exceptions);
			LicobStatus status = engine.getStatus();
			while (!status.isFinished()) {
				Thread.sleep(50);
			}

			File[] allFiles = status.getAllFiles().toArray(new File[0]);
			Arrays.sort(allFiles);
			assertEquals(
				fileNumber + 1 - exceptions.length,
				allFiles.length,
				"The amount of the files doesn't match"
			);
			File backupDir = new File(sourceDir, parentDir);
			assertTrue(
				Arrays.binarySearch(allFiles, backupDir) >= 0,
				"The file " + backupDir + " is missing"
			);
			for (int fileIndex = 0; fileIndex < fileNumber; fileIndex++) {
				File backupFile = new File(backupDir, Integer.toString(fileIndex));
				if (Arrays.binarySearch(exceptions, backupFile) >= 0) continue;
				assertTrue(
					Arrays.binarySearch(allFiles, backupFile) >= 0,
					"The file " + backupFile + " is missing"
				);
			}
		}

		@RepeatedTest(3)
		void backupInterruptionCheck() throws IOException{
			LicobEngineInterface engine = new LicobEngine();
			engine.createBackup(new File(sourceDir, parentDir), dstDir);
			assertTimeout(Duration.of(200, ChronoUnit.MILLIS), engine::interrupt);
		}
	}
}
