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

import constants.*;
import licob.ChainSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFrame extends LFrame{
	private final BackupList backupList;
	public MainFrame() {
		super(Text.APP_NAME, Dimensions.MAIN_FRAME_WIDTH, Dimensions.MAIN_FRAME_HEIGHT);

		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);

		GridBagConstraints backupListConstraints = new GridBagConstraints();
		backupListConstraints.fill = GridBagConstraints.BOTH;
		backupListConstraints.weighty = 15;
		backupListConstraints.gridwidth = GridBagConstraints.REMAINDER;
		backupListConstraints.weightx = 1;
		backupListConstraints.insets = new Insets(
			Dimensions.LIST_PANEL_OFFSET,
			Dimensions.LIST_PANEL_OFFSET,
			0,
			Dimensions.LIST_PANEL_OFFSET
		);

		String[] backupNames = new File(Configuration.CHAIN_SETS_DIRECTORY).list();
		backupList = new BackupList();
		for (String backupName: backupNames) {
			CharBuffer scriptContent = CharBuffer.allocate(1024 * 8);
			boolean scriptEnabled = ChainSet.retrieveScript(backupName, scriptContent);
			int chainNumber = ChainSet.retrieveChainNumber(backupName);
			long date = ChainSet.retrieveDate(backupName);
			String lastExecution = date == 0 ? "-" : new SimpleDateFormat().format(new Date(date));
			backupList.addBackupItem(
				backupName, chainNumber, scriptEnabled, scriptContent.toString(), lastExecution
			);
		}
		bagLayout.setConstraints(backupList, backupListConstraints);
		add(backupList);

		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;
		buttonConstraints.anchor = GridBagConstraints.WEST;
		buttonConstraints.insets = new Insets(
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.LIST_PANEL_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			0
		);
		JButton addButton = new JButton(Text.ADD_BUTTON);
		addButton.setFont(Fonts.MEDIUM_DEFAULT);
		addButton.setBackground(Colors.ADD_BUTTON_COLOR);
		addButton.setMinimumSize(Dimensions.MAIN_FRAME_CONTROL_BUTTON);
		addButton.setPreferredSize(Dimensions.MAIN_FRAME_CONTROL_BUTTON);
		addButton.addActionListener(new AddButtonListener());
		bagLayout.setConstraints(addButton, buttonConstraints);
		add(addButton);

		GridBagConstraints logAreaConstraints = new GridBagConstraints();
		logAreaConstraints.gridwidth = GridBagConstraints.REMAINDER;
		logAreaConstraints.fill = GridBagConstraints.BOTH;
		logAreaConstraints.weighty = 1;
		LogArea logArea = new LogArea();
		bagLayout.setConstraints(logArea, logAreaConstraints);
		add(logArea);

		setVisible(true);
	}

	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String newBackupName = Text.DEFAULT_BACKUP_NAME + System.currentTimeMillis();
			backupList.addBackupItem(newBackupName, 0, false, "", "-");
			try {
				ChainSet.addChainSet(newBackupName, new ChainRule[]{}, "", false);
			} catch (IOException exception) {
				JOptionPane.showMessageDialog(
					MainFrame.this,
					Text.ErrorDialog.STATUS(exception),
					Text.ErrorDialog.TITLE,
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
