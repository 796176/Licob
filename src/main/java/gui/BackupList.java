package gui;

import constants.Colors;
import constants.Dimensions;

import javax.swing.*;
import java.awt.*;

public class BackupList extends JScrollPane {
	private final BackupItem[] backupItems;
	public BackupList(BackupItem[] items) {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

		assert items != null;
		backupItems = items;

		JPanel innerPanel = new JPanel(new GridLayout(0, 1, 0, Dimensions.DEFAULT_COMPONENT_OFFSET));
		innerPanel.setBackground(Colors.LAYER1);
		setViewportView(innerPanel);
		verticalScrollBar.setUnitIncrement(Dimensions.DEFAULT_PANEL_SCROLL);
		for (BackupItem backupItem: items) {
			innerPanel.add(backupItem);
		}
	}

	public BackupItem[] getBackupItems() {
		return backupItems;
	}
}
