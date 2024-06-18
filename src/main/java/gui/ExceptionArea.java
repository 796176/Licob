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


package gui;

import constants.Colors;
import constants.Fonts;

import javax.swing.*;

public class ExceptionArea extends JScrollPane {
	public final JTextArea textArea;
	public ExceptionArea(String exceptions, boolean isActive) {
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);

		assert exceptions != null;

		textArea = new JTextArea(exceptions);
		textArea.setFont(Fonts.MEDIUM_MONO);
		textArea.setCaretColor(Colors.FONT_COLOR);
		if (isActive) setActive();
		else setPassive();

		setPreferredSize(getMinimumSize());
		setMaximumSize(getMinimumSize());
		setViewportView(textArea);
	}

	public ExceptionArea() {
		this("", true);
	}

	public String getExceptions() {
		return textArea.getText();
	}

	public void setActive() {
		textArea.setEditable(true);
		textArea.setForeground(Colors.FONT_COLOR);
		textArea.setBackground(Colors.LAST_LAYER);
	}

	public void setPassive(){
		textArea.setEditable(false);
		textArea.setForeground(Colors.SCRIPT_AREA_FONT_COLOR_INACTIVE);
		textArea.setBackground(Colors.SCRIPT_AREA_COLOR_INACTIVE);
	}
}
