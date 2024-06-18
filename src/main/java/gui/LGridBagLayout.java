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

import javax.swing.*;
import java.awt.*;

public class LGridBagLayout {
	public static JPanel componentString(int[] spaces, Component... components){
		GridBagLayout bagLayout = new GridBagLayout();
		JPanel panel = new JPanel(bagLayout);
		panel.setBackground(new Color(0, 0, 0, 0));
		GridBagConstraints constraints = new GridBagConstraints();
		for (
			int spaceIndex = 0, componentIndex = 0;
			componentIndex < components.length;
			componentIndex++, spaceIndex = Math.min(spaceIndex + 1, spaces.length - 1)
		){
			if (componentIndex == components.length - 1)
				constraints.insets = new Insets(0, 0, 0, 0);
			else
				constraints.insets = new Insets(0, 0, 0, spaces[spaceIndex]);
			bagLayout.setConstraints(components[componentIndex], constraints);
			panel.add(components[componentIndex]);
		}
		return panel;
	}

	public static JPanel componentString(int space, Component... components){
		return componentString(new int[]{space}, components);
	}
}
