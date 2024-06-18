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

import java.io.*;

public interface LicobEngineInterface {
	void createBackup(File source, File dst, File[] exceptions) throws IOException;
	void createBackup(File source, File dst) throws IOException;
	void backupContent(File source, File dst, File[] exceptions) throws IOException;
	void backupContent(File source, File dst) throws IOException;
	LicobStatus getStatus();
	void interrupt();
}
