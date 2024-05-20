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
