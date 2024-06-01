package gui;

import constants.Colors;
import constants.Dimensions;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class BackupList extends JScrollPane {
	private BackupItem[] backupItems = new BackupItem[]{};
	public BackupList() {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

		setPreferredSize(getMinimumSize());
		verticalScrollBar.setUnitIncrement(Dimensions.DEFAULT_PANEL_SCROLL);
		initiateList();
	}

	private JPanel getPanel(){
		JPanel panel = new JPanel();
		panel.setBackground(Colors.LAYER1);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		return panel;
	}

	private void initiateList() {
		JPanel panel = getPanel();
		for (BackupItem backupItem: backupItems) {
			panel.add(backupItem);
			if (backupItem != backupItems[backupItems.length - 1]){
				panel.add(Box.createRigidArea(new Dimension(0, Dimensions.DEFAULT_COMPONENT_OFFSET)));
			}
		}
		setViewportView(panel);
	}

	public void addBackupItem(String name, int chainNumber, boolean bashScript, String date) {
		assert name != null && chainNumber > -1 && date != null;

		backupItems = Arrays.copyOf(backupItems, backupItems.length + 1);
		backupItems[backupItems.length - 1] = new BackupItem(this, name, chainNumber, bashScript, date);
		initiateList();
	}

	public void removeBackupItem(BackupItem backupItem) {
		assert backupItem != null;

		int index = 0;
		while (backupItems[index] != backupItem && ++index < backupItems.length);
		if (index == backupItems.length) return;
		System.arraycopy(backupItems, index + 1, backupItems, index, backupItems.length - 1 - index);
		backupItems = Arrays.copyOf(backupItems, backupItems.length - 1);
		initiateList();
	}

	public void changeBackupItem(BackupItem oldBI, String name, int chainNumber, boolean bashScript, String date) {
		assert name != null && chainNumber > -1 && date != null;

		int index = 0;
		while (backupItems[index] != oldBI && ++index < backupItems.length);
		if (index == backupItems.length) return;
		backupItems[index] = new BackupItem(this, name, chainNumber, bashScript, date);
		initiateList();
	}

	public BackupItem[] getBackupItems() {
		return backupItems;
	}
}
