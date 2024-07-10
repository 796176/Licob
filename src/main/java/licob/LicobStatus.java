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

import java.io.File;
import java.util.LinkedList;

public class LicobStatus {
	private boolean isFinished = false;
	private LinkedList<File> finishedFiles = new LinkedList<>();
	private File currentFile;
	private long progress = 0;
	private long total = 0;
	private long currentProgress = 0;
	private long currentTotal = 0;
	private LinkedList<File> allFiles = new LinkedList<>();
	private Thread thread;
	private Exception e;

	void setIsFinished(boolean v){
		isFinished = v;
	}

	public boolean isFinished() {
		return isFinished;
	}

	void setFinishedFiles(LinkedList<File> v){
		assert v != null;
		finishedFiles = v;
	}

	public LinkedList<File> getFinishedFiles() {
		return finishedFiles;
	}

	void setCurrentFile(File v){
		assert v != null;
		currentFile = v;
	}

	public File getCurrentFile() {
		return currentFile;
	}

	void setProgress(long v){
		assert v >= 0;
		progress = v;
	}

	public long getProgress() {
		return progress + currentProgress;
	}

	void setTotal(long v){
		assert v >= 0;
		total = v;
	}

	public long getTotal() {
		return total;
	}

	void setCurrentProgress(long v){
		assert v >= 0;
		currentProgress = v;
	}

	public long getCurrentProgress() {
		return currentProgress;
	}

	void setCurrentTotal(long v) {
		assert v >= 0;
		currentTotal = v;
	}

	public long getCurrentTotal() {
		return currentTotal;
	}

	void setAllFiles(LinkedList<File> v) {
		assert v != null;
		allFiles = v;
	}

	public LinkedList<File> getAllFiles() {
		return allFiles;
	}

	void setThread(Thread v) {
		assert v != null;
		thread = v;
	}

	Thread getThread() {
		return thread;
	}

	void setException(Exception v) {
		assert v != null;
		e = v;
	}

	public Exception getException() {
		return e;
	}
}