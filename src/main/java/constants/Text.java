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


package constants;

public class Text {
	public static String APP_NAME = "Lacob";
	public static String ADD_BUTTON = "Add";
	public static String RUN_BUTTON = "Run";
	public static String DELETE_BUTTON = "Delete";
	public static String[] SUBTITLE_LABEL = new String[] {" chains", " with a post-backup script"};
	public static String ENABLE_SCRIPT_CHECKBOX = "Enable script";
	public static String CANCEL_BUTTON = "Cancel";
	public static String FILE_DIRECTORY = "File/Directory";
	public static String CHOOSE = "choose";
	public static String EXCEPTION_LABEL = "Exceptions:";
	public static String DESTINATION = "Destination";
	public static String LAST_EXECUTION_LABEL = "Last execution on";
	public static String CONFIRMATION_DIALOG_TITLE = "Confirmation";
	public static String BACKUP_NAME_LABEL = "Backup name";
	public static String SAVE_BUTTON = "Save";
	public static String FILE_DOES_NOT_EXIST = "file doesn't exist";
	public static String DIRECTORY = "Directory";
	public static String CONTENT = "Content";
	public static String TYPE_LABEL = "Type:";
	public static String SOURCE_LABEL = "Source:";
	public static String DESTINATION_LABEL = "Destination";
	public static String L_FILE_BUTTON_APPROVAL_BUTTON = "Choose";
	public static String FILE = "File";
	public static String DEFAULT_BACKUP_NAME = "backup";
	public static String DELETE_BACKUP_ITEM_QUESTION(String name) {
		return "Are you sure you want to delete the " + name + " item?";
	}
	public static String DELETE_BACKUP_ITEM_TITLE(String name) {
		return "Delete the " + name + " item?";
	}
	public static String EXIT_WITHOUT_SAVING = "Are you sure you want to exist without saving?";
	public static String CONFIRM = "Confirm";
	public static class SaveDialog {
		public static String TITLE = "Save";
		public static String SAVED = "Successfully saved";
		public static String FAILED_NO_DATA = "No data to save";
	}
	public static class ErrorDialog {
		public static String TITLE = "Error";
		public static String STATUS(Exception e) {
			return "An unexpected exception occurred:" + System.lineSeparator() + e.getMessage();
		}
	}
	public static class BackupDialog {
		public static String TITLE = "Backup in process";
		public static String CHAIN_LABEL(int chain) {
			if (chain % 10 == 1)
				return "Executing the " + chain + "st chain";
			else if (chain % 10 == 2)
				return "Executing the " + chain + "nd chain";
			else if (chain % 10 == 3)
				return "Executing the " + chain + "rd chain";
			else
				return "Executing the " + chain + "th chain";
		}
		public static String COPIED_CURRENT_FILE(String file, long copied, long total){
			return "Copied " + copied + " bytes out of " + total + " from " + file;
		}
		public static String COPIED_FILES(long copied, long total){
			return "Copied " + copied + " files out of " + total + " in the current chain";
		}
		public static String COPIED_BITES(long copied, long total) {
			return "Copied " + copied + " bytes out of " + total + " in the current chain";

		}
	}
	public static class LogMessages {
		public static String BACKUP_SUCCESS = "Backup has been successfully created";
		public static String CHAIN_PROCESSING(int chainIndex){
			return "Executing chain number " + ++chainIndex + "...";
		}
		public static String CHAIN_SKIP(String reason){
			if (reason == null)
				return "\tStatus: skipped";
			else
				return "\tStatus: skipped, " + reason;
		}
		public static String CHAIN_SUCCESS = "\tStatus: success";
		public static String INTERRUPTED = "The process was interrupted";
	}
}
