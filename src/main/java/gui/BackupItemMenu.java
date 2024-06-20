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
import constants.Text;
import licob.ChainSet;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class BackupItemMenu extends JPopupMenu {
	public BackupItemMenu(BackupList backupList, BackupItem item) {
		JMenuItem renameItem = new JMenuItem(Text.BackupItemMenu.RENAME);
		renameItem.setFont(Fonts.SMALL_MONO);
		renameItem.setBackground(Colors.LAST_LAYER);
		renameItem.setForeground(Colors.FONT_COLOR);
		renameItem.addActionListener(actionEvent -> {
			String newBackupItemName =
				JOptionPane.showInputDialog(
					backupList,
					Text.RenameDialog.MES,
					Text.RenameDialog.TITLE,
					JOptionPane.QUESTION_MESSAGE
				);
			if (newBackupItemName == null) return;
			String[] names = ChainSet.getBackupItemNames();
			Arrays.sort(names);
			if (Arrays.binarySearch(names, newBackupItemName) >= 0) {
				JOptionPane.showMessageDialog(backupList, Text.FAILED_TO_RENAME_ALREADY_EXISTS);
				return;
			}
			try {
				ChainSet.renameBackupItem(item.getBackupName(), newBackupItemName);
				backupList.changeBackupItem(
					item,
					newBackupItemName,
					item.getChainNumber(),
					item.getScriptState(),
					item.getScript(),
					item.getDate()
				);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
					backupList,
					Text.ErrorDialog.STATUS(e),
					Text.ErrorDialog.TITLE,
					JOptionPane.ERROR_MESSAGE
				);
			}
		});
		add(renameItem);
	}
}
