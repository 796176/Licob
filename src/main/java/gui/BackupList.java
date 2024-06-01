package gui;

import constants.Colors;
import constants.Dimensions;

import javax.swing.*;
import java.awt.*;

public class BackupList extends JScrollPane {
	private final BackupItem[] backupItems;
	public BackupList() {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		innerPanel.setBackground(Colors.LAYER1);
		setViewportView(innerPanel);
		setPreferredSize(getMinimumSize());
		verticalScrollBar.setUnitIncrement(Dimensions.DEFAULT_PANEL_SCROLL);
	}

	public BackupItem[] getBackupItems() {
		return backupItems;
	}
}
