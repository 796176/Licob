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

import gui.ChainRule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ChainSetTests {
	@TempDir
	private static File workingDirectory;
	private static final String backupName = "licob";

	@BeforeAll
	static void beforeAll() {
		constants.Configuration.CHAIN_SETS_DIRECTORY = workingDirectory.getAbsolutePath();
	}

	@Nested
	class NoRules {
		@Test
		void flushRule() {
			try {
				ChainSet.flushChainSet(backupName, new ChainRule[]{}, "", false);
			} catch (IOException exception) {
				fail("No io operations performed to throw an IOException " + exception.getMessage());
			}
		}

		@Test
		void retrieveNonExistentRule() {
			try {
				assertNull(ChainSet.getChainSet(backupName));
			} catch (IOException exception) {
				fail("No io operations performed to throw an IOException " + exception.getMessage());
			}
		}

		@Test
		void retrieveNonExistentScript() {
			assertFalse(ChainSet.retrieveScript(backupName, CharBuffer.allocate(1024)));
		}

		@Test
		void retrieveChainNumber() {
			assertEquals(0, ChainSet.retrieveChainNumber(backupName));
		}

		@Test
		void retrieveDate() {
			assertEquals(0, ChainSet.retrieveDate(backupName));
		}

		@Test
		void updateDate() {
			try {
				ChainSet.updateDate(backupName);
			} catch (IOException exception) {
				fail("No io operations performed to throw an IOException " + exception.getMessage());
			}
		}

		@Test
		void retrieveNonExistentScriptFile() {
			assertNull(ChainSet.getScriptFile(backupName));
		}

		@Test
		void retrieveBackupNames() {
			assertEquals(0, ChainSet.getBackupItemNames().length);
		}

		@Test
		void renameNonExistentRule() {
			try {
				ChainSet.renameBackupItem(backupName, backupName);
			} catch (IOException exception) {
				fail("No io operations performed to throw an IOException " + exception.getMessage());
			}
		}

		@Test
		void deleteNonExistentRule() {
			try {
				ChainSet.deleteChainSet(backupName);
			} catch (IOException exception) {
				fail("No io operations performed to throw an IOException " + exception.getMessage());
			}
		}

		@Nested
		class OneBackupItemCreated {
			@BeforeEach
			void beforeEach() throws IOException{
				ChainSet.createChainSet(backupName);
			}

			@AfterEach
			void afterEach() throws IOException {
				Files.walkFileTree(workingDirectory.toPath(), new SimpleFileVisitor<>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						if (!dir.equals(workingDirectory.toPath()))
							Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			}

			@Test
			void retrieveRules() throws IOException {
				assertEquals(0, ChainSet.getChainSet(backupName).length);
			}

			@Test
			void retrieveScript() {
				CharBuffer scriptContent = CharBuffer.allocate(1024);
				assertFalse(ChainSet.retrieveScript(backupName, scriptContent));
				assertEquals(0, scriptContent.toString().length());
			}

			@Test
			void retrieveChainNumber() {
				assertEquals(0, ChainSet.retrieveChainNumber(backupName));
			}

			@Test
			void retrieveDate() {
				assertEquals(0, ChainSet.retrieveDate(backupName));
			}

			@Test
			void retrieveScriptFile() {
				assertNotNull(ChainSet.getScriptFile(backupName));
			}

			@Test
			void retrieveBackupNames() {
				assertArrayEquals(new String[]{backupName}, ChainSet.getBackupItemNames());
			}

			@Nested
			class DeleteBackupItem {
				@Test
				void deleteBackupItem() throws IOException{
					ChainSet.deleteChainSet(backupName);
					assertEquals(0, ChainSet.getBackupItemNames().length);
				}

				@AfterEach
				void afterEach() throws IOException{
					ChainSet.createChainSet(backupName);
				}
			}

			@Nested
			class RenameBackupItem {
				private final String newName = "new" + backupName;
				@Test
				void renameBackupItem() throws IOException {
					ChainSet.renameBackupItem(backupName, newName);
					assertArrayEquals(new String[]{newName}, ChainSet.getBackupItemNames());
				}

				@AfterEach
				void afterEach() throws IOException {
					ChainSet.renameBackupItem(newName, backupName);
				}
			}

			@Nested
			class FlushOneRule {
				ChainRule[] chainRules = new ChainRule[]{
					new ChainRule("type", "source", "destination", "exceptions")
				};
				boolean scriptStatus = true;
				String scriptContent = "content";

				@BeforeEach
				void beforeEach() throws IOException{
					ChainSet.flushChainSet(backupName, chainRules, scriptContent, scriptStatus);
				}

				@AfterEach
				void afterEach() throws IOException {
					ChainSet.deleteChainSet(backupName);
					ChainSet.createChainSet(backupName);
				}

				@Test
				void retrieveChainNumber() {
					assertEquals(1, ChainSet.retrieveChainNumber(backupName));
				}

				@Test
				void retrieveRules() throws IOException{
					ChainRule[] rules = ChainSet.getChainSet(backupName);
					assertNotNull(rules);
					assertEquals(chainRules[0], rules[0]);
				}

				@Test
				void retrieveScript() {
					CharBuffer scriptCntn = CharBuffer.allocate(1024);
					assertTrue(ChainSet.retrieveScript(backupName, scriptCntn));
					assertEquals(scriptContent, scriptCntn.toString());
				}

				@Test
				void retrieveScriptFile() {
					assertNotNull(ChainSet.getScriptFile(backupName));
				}

				@Test
				void updateDate() throws IOException{
					ChainSet.updateDate(backupName);
					assertNotEquals(0, ChainSet.retrieveDate(backupName));
				}
			}

			@Nested
			class FlushTwoRules {
				ChainRule[] chainRules = new ChainRule[]{
					new ChainRule("type1", "source1", "destination1", "exceptions1"),
					new ChainRule("type2", "source2", "destination2", "exceptions2")
				};

				@BeforeEach
				void beforeEach() throws IOException {
					ChainSet.flushChainSet(backupName, chainRules, "", false);
				}

				@AfterEach
				void afterEach() throws IOException {
					ChainSet.deleteChainSet(backupName);
					ChainSet.createChainSet(backupName);
				}

				@Test
				void retrieveChainNumber() {
					assertEquals(2, ChainSet.retrieveChainNumber(backupName));
				}

				@Test
				void retrieveRules() throws IOException{
					ChainRule[] rules = ChainSet.getChainSet(backupName);
					assertNotNull(rules);
					Arrays.sort(rules);
					assertEquals(2, rules.length);
					Arrays.stream(chainRules)
						.forEach(rule -> assertTrue(Arrays.binarySearch(rules, rule) >= 0));
				}

				@Test
				void deleteFirstRule() throws IOException{
					ChainRule[] newRules = new ChainRule[] {chainRules[1]};
					ChainSet.flushChainSet(backupName, newRules, "", false);
					assertEquals(1, ChainSet.retrieveChainNumber(backupName));
					ChainRule[] retrieved = ChainSet.getChainSet(backupName);
					assertNotNull(retrieved);
					assertEquals(1, retrieved.length);
					assertEquals(newRules[0], retrieved[0]);
				}

				@Test
				void deleteSecondRule() throws IOException{
					ChainRule[] newRules = new ChainRule[] {chainRules[0]};
					ChainSet.flushChainSet(backupName, newRules, "", false);
					assertEquals(1, ChainSet.retrieveChainNumber(backupName));
					ChainRule[] retrieved = ChainSet.getChainSet(backupName);
					assertNotNull(retrieved);
					assertEquals(1, retrieved.length);
					assertEquals(newRules[0], retrieved[0]);
				}
			}

			@Nested
			class FlushIncompleteRules{
				ChainRule[] chainRules = new ChainRule[] {
					new ChainRule("type1", null, "destination1", "exceptions1"),
					new ChainRule("type2", "source2", null, "exceptions2"),
					new ChainRule("type3", null, null, "exceptions3")
				};

				@BeforeEach
				void beforeEach() throws IOException {
					ChainSet.flushChainSet(backupName, chainRules, "", false);
				}

				@AfterEach
				void afterEach() throws IOException {
					ChainSet.deleteChainSet(backupName);
					ChainSet.createChainSet(backupName);
				}

				@Test
				void retrieveRules() throws IOException{
					ChainRule[] retrieved = ChainSet.getChainSet(backupName);
					assertNotNull(retrieved);
					Arrays.sort(retrieved);
					assertEquals(3, retrieved.length);
					Arrays.stream(chainRules)
						.forEach(rule -> assertTrue(Arrays.binarySearch(retrieved, rule) >= 0));
				}
			}

			@Nested
			class SecondBackupItemCreated {
				private final String backupName2 = "licob2";

				@BeforeEach
				void beforeEach() throws IOException{
					ChainSet.createChainSet(backupName2);
				}

				@AfterEach
				void afterEach() throws IOException {
					ChainSet.deleteChainSet(backupName2);
				}

				@Test
				void retrieveBackupNames() {
					String[] names = ChainSet.getBackupItemNames();
					Arrays.sort(names);
					assertEquals(2, names.length);
					Stream.of(backupName, backupName2)
						.forEach(name -> assertTrue(Arrays.binarySearch(names, name) >= 0));
				}
			}
		}
	}
}
