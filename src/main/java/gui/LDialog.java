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

import javax.swing.*;
import java.awt.*;

public class LDialog extends JDialog {
	public LDialog(JFrame frame, String title, boolean modal, int dialogWidth, int dialogHeight) {
		super(frame, title, modal);

		getContentPane().setBackground(Colors.LAYER0);
		setSize(dialogWidth, dialogHeight);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		Rectangle bounds = getGraphicsConfiguration().getBounds();
		int xLocation = bounds.width / 2 - dialogWidth / 2;
		int yLocation = bounds.height / 2 - dialogHeight / 2;
		setLocation(xLocation, yLocation);
	}
	public LDialog(JFrame frame, String title, int dialogWidth, int dialogHeight) {
		this(frame, title, false, dialogWidth, dialogHeight);
	}
}
