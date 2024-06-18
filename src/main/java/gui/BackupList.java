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

	public void addBackupItem(String name, int chainNumber, boolean bashScript, String script, String date) {
		assert name != null && chainNumber > -1 && date != null;

		backupItems = Arrays.copyOf(backupItems, backupItems.length + 1);
		backupItems[backupItems.length - 1] = new BackupItem(this, name, chainNumber, bashScript, script, date);
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

	public void changeBackupItem(
		BackupItem oldBI, String name, int chainNumber, boolean bashScript, String script, String date
	) {
		assert name != null && chainNumber > -1 && date != null;

		int index = 0;
		while (backupItems[index] != oldBI && ++index < backupItems.length);
		if (index == backupItems.length) return;
		backupItems[index] = new BackupItem(this, name, chainNumber, bashScript, script, date);
		initiateList();
	}

	public BackupItem[] getBackupItems() {
		return backupItems;
	}
}
