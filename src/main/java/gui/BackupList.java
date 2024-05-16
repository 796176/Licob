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

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		innerPanel.setBackground(Colors.LAYER1);
		setViewportView(innerPanel);
		setPreferredSize(getMinimumSize());
		verticalScrollBar.setUnitIncrement(Dimensions.DEFAULT_PANEL_SCROLL);
		for (BackupItem backupItem: items) {
			innerPanel.add(backupItem);
			if (backupItem != backupItems[backupItems.length - 1]){
				innerPanel.add(Box.createRigidArea(new Dimension(0, Dimensions.DEFAULT_COMPONENT_OFFSET)));
			}
		}
	}

	public BackupItem[] getBackupItems() {
		return backupItems;
	}
}
