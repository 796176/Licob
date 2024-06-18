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


abstract public class LFrame extends JFrame {
	public LFrame(String title, int windowWidth, int windowHeight){
		super(title);
		setSize(windowWidth, windowHeight);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(Colors.LAYER0);

		Rectangle bounds = getGraphicsConfiguration().getBounds();
		int xLocation = bounds.width / 2 - windowWidth / 2;
		int yLocation = bounds.height / 2 - windowHeight / 2;
		setLocation(xLocation, yLocation);
	}
}
