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

public enum ChainTypes {
	Directory(Text.DIRECTORY), Content(Text.CONTENT), File(Text.FILE);
	ChainTypes(String s) {
		visualRepresentation = s;
	}
	private final String visualRepresentation;

	public String getVisualRepresentation() {
		return visualRepresentation;
	}
}
