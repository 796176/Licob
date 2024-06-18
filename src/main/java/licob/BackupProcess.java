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

import constants.ChainTypes;
import constants.Text;
import gui.BackupDialog;
import gui.ChainRule;
import gui.LogArea;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupProcess {
	private String backupItem;
	private final LogArea logArea;
	private final BackupDialog dialog;
	private Exception exception = null;
	private Thread thread;
	public BackupProcess(LogArea logArea, BackupDialog dialog) {
		assert logArea != null && dialog != null;

		this.logArea = logArea;
		this.dialog = dialog;
	}

	public void start(String backupName) {
		backupItem = backupName;
		thread = new Thread(new RunnableBackupProcess());
		thread.start();
		dialog.setVisible(true);
	}

	public void join() {
		try {
			if (thread != null) thread.join();
		} catch (InterruptedException ignored) {}
	}

	public Exception getException() {
		return exception;
	}

	private class RunnableBackupProcess implements Runnable {
		public void run() {
			ChainRule[] chainRules;
			try {
				chainRules = ChainSet.getChainSet(backupItem);
				if (chainRules == null) {
					exception = new Exception("The chain set " + backupItem + " doesn't exist");
					return;
				}
			} catch (IOException e) {
				dialog.setVisible(false);
				exception = e;
				return;
			}
			int chainIndex = 0;
			for (ChainRule rule : chainRules) {
				logArea.log(Text.LogMessages.CHAIN_PROCESSING(chainIndex++));

				if (ruleFails(rule))
					continue;
				ChainTypes type = ChainTypes.valueOf(rule.type);
				File source = new File(rule.source);
				File destination = new File(rule.destination);

				String backupFolder = "backup-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date());
				File backupDestination = new File(destination, backupFolder);
				if (!(backupDestination.mkdir() || backupDestination.exists())) {
					dialog.setVisible(false);
					exception = new IOException(
						"IOException: Failed to create " + backupDestination.getAbsolutePath() + " directory"
					);
					return;
				}

				File[] exceptionArray = getExceptionArray(rule.exceptions);
				LicobEngineInterface engineInterface = new LicobEngine();
				try {
					switch (type) {
						case ChainTypes.File:
							engineInterface.createBackup(source, backupDestination);
							break;
						case ChainTypes.Directory:
							engineInterface.createBackup(source, backupDestination, exceptionArray);
							break;
						case ChainTypes.Content:
							engineInterface.backupContent(source, backupDestination, exceptionArray);
							break;
					}
				} catch (IOException e) {
					logArea.log(Text.LogMessages.CHAIN_SKIP("IOException " + e.getMessage()));
					continue;
				}

				while (!(engineInterface.getStatus().isFinished() || dialog.interrupted)) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ignored) {}
					dialog.update(engineInterface, engineInterface.getStatus(), 0);
				}

				if (dialog.interrupted) {
					engineInterface.interrupt();
					dialog.setVisible(false);
					logArea.log(Text.LogMessages.INTERRUPTED);
					return;
				}

				logArea.log(Text.LogMessages.CHAIN_SUCCESS);
			}

			if (!runScript(backupItem))
				return;

			try {
				ChainSet.updateDate(backupItem);
			} catch (IOException e) {
				exception = e;
				dialog.setVisible(false);
				return;
			}

			logArea.log(Text.LogMessages.BACKUP_SUCCESS);
			dialog.setVisible(false);
		}
	}

	private boolean ruleFails(ChainRule rule) {
		return typeFails(rule.type) || sourceFails(rule.source) || destinationFails(rule.destination);
	}

	private boolean typeFails(String type) {
		try {
			ChainTypes.valueOf(type);
			return false;
		} catch (IllegalArgumentException e) {
			logArea.log(Text.LogMessages.CHAIN_SKIP("unexpected type"));
			return true;
		}
	}

	private boolean sourceFails(String source) {
		if (source == null) {
			logArea.log(Text.LogMessages.CHAIN_SKIP("the source is unspecified"));
			return true;
		} else if (!new File(source).exists()) {
			logArea.log(Text.LogMessages.CHAIN_SKIP("the source doesn't exist"));
			return true;
		}

		return false;
	}

	private boolean destinationFails(String destination) {
		if (destination == null) {
			logArea.log(Text.LogMessages.CHAIN_SKIP("the destination is unspecified"));
			return true;
		} else if (!new File(destination).exists()) {
			logArea.log(Text.LogMessages.CHAIN_SKIP("the destination doesn't exist"));
			return true;
		}

		return false;
	}

	private File[] getExceptionArray(String exc){
		String[] exceptionStringArray = exc.split(System.lineSeparator());
		File[] exceptionFileArray = new File[exceptionStringArray.length];
		for (int arrayIndex = 0; arrayIndex < exceptionStringArray.length; arrayIndex++) {
			exceptionFileArray[arrayIndex] = new File(exceptionStringArray[arrayIndex]);
		}
		return exceptionFileArray;
	}

	private boolean runScript(String backupName) {
		try {
			Process script = Executor.executeScript(ChainSet.getScriptFile(backupName));
			script.waitFor();
		} catch (IOException | InterruptedException e) {
			exception = e;
			dialog.setVisible(false);
			return false;
		} catch (NullPointerException e) {
			exception = new Exception("Failed to run the script");
			dialog.setVisible(false);
			return false;
		}
		return true;
	}
}
