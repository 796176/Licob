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
import constants.Fonts;
import constants.Text;
import licob.BackupProcess;
import licob.ChainSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupItem extends JPanel {
	private LLabel name;
	private LLabel subtitle;
	private LLabel lastExecuted;
	private JButton deleteButton;
	private JButton runButton;
	private final BackupList backupList;
	private final String scriptContent;
	private final boolean isScriptActive;
	private final BackupProcess backupProcess;
	private BackupItemMenu itemMenu;
	private int chainNumber;
	private boolean scriptState;
	private String script;
	private String date;

	public BackupItem(
		BackupList bl,
		BackupProcess backupProcess,
		String name,
		int chainNumber,
		boolean bashScript,
		String script,
		String date
	) {
		assert bl != null && backupProcess != null && script != null;

		backupList = bl;
		this.backupProcess = backupProcess;
		scriptContent = script;
		isScriptActive = bashScript;
		itemMenu = new BackupItemMenu(bl, this);

		this.chainNumber = chainNumber;
		scriptState = bashScript;
		this.script = script;
		this.date = date;

		addMouseListener(new PanelListener());
		setMinimumSize(Dimensions.BACKUP_ITEM);
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Dimensions.BACKUP_ITEM.height));
		setPreferredSize(Dimensions.BACKUP_ITEM);
		setBackground(Colors.LAYER2);
		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.anchor = GridBagConstraints.NORTHWEST;
		labelConstraints.gridwidth = GridBagConstraints.REMAINDER;
		labelConstraints.weightx = 1;
		labelConstraints.weighty = 2;
		labelConstraints.insets =
			new Insets(Dimensions.SMALL_COMPONENT_OFFSET, Dimensions.DEFAULT_COMPONENT_OFFSET, 0, 0);
		this.name = new LLabel(name);
		bagLayout.setConstraints(this.name, labelConstraints);
		add(this.name);

		labelConstraints.weighty = 1;
		StringBuilder subtitle = new StringBuilder(chainNumber + Text.SUBTITLE_LABEL[0]);
		if (bashScript) subtitle.append(Text.SUBTITLE_LABEL[1]);
		this.subtitle = new LLabel(subtitle.toString());
		this.subtitle.setFont(Fonts.SMALL_DEFAULT);
		bagLayout.setConstraints(this.subtitle, labelConstraints);
		add(this.subtitle);

		lastExecuted = new LLabel(Text.LAST_EXECUTION_LABEL + " " + date);
		lastExecuted.setFont(Fonts.SMALL_DEFAULT);
		bagLayout.setConstraints(lastExecuted, labelConstraints);
		add(lastExecuted);

		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.anchor = GridBagConstraints.SOUTHWEST;
		buttonConstraints.weightx = 1;
		buttonConstraints.weighty = 10;
		buttonConstraints.insets = new Insets(
			0,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.SMALL_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET
		);
		runButton = new JButton(Text.RUN_BUTTON);
		runButton.setMinimumSize(Dimensions.BACKUP_ITEM_CONTROL_BUTTON);
		runButton.setPreferredSize(Dimensions.BACKUP_ITEM_CONTROL_BUTTON);
		runButton.setFont(Fonts.MEDIUM_DEFAULT);
		runButton.setBackground(Colors.RUN_BUTTON_COLOR);
		runButton.addActionListener(new RunButtonListener());
		bagLayout.setConstraints(runButton, buttonConstraints);
		add(runButton);

		buttonConstraints.anchor = GridBagConstraints.SOUTHEAST;
		buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;
		deleteButton = new JButton(Text.DELETE_BUTTON);
		deleteButton.setMinimumSize(Dimensions.BACKUP_ITEM_CONTROL_BUTTON);
		deleteButton.setPreferredSize(Dimensions.BACKUP_ITEM_CONTROL_BUTTON);
		deleteButton.setFont(Fonts.MEDIUM_DEFAULT);
		deleteButton.setBackground(Colors.DELETE_BUTTON_COLOR);
		deleteButton.addActionListener(new DeleteButtonListener());
		bagLayout.setConstraints(deleteButton, buttonConstraints);
		add(deleteButton);
	}

	public String getBackupName() {
		return name.getText();
	}

	public String getLastTimeExecuted() {
		return lastExecuted.getText().substring((Text.LAST_EXECUTION_LABEL + " ").length());
	}

	public int getChainNumber() {
		return chainNumber;
	}

	public boolean getScriptState() {
		return scriptState;
	}

	public String getScript() {
		return script;
	}

	public String getDate() {
		return date;
	}

	public class DeleteButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			int result = JOptionPane.showConfirmDialog(
				backupList,
				Text.DELETE_BACKUP_ITEM_QUESTION(name.getText()),
				Text.DELETE_BACKUP_ITEM_TITLE(name.getText()),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);
			if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) return;

			backupList.removeBackupItem(BackupItem.this);
			try {
				ChainSet.deleteChainSet(name.getText());
			}catch (IOException exception) {

			}
		}
	}

	public class PanelListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			switch (mouseEvent.getButton()) {
				case MouseEvent.BUTTON1:
					BackupListNotificator notificator = new BackupListNotificator(BackupItem.this);
					ChainConfigurationFrame chainConfigurationFrame =
						new ChainConfigurationFrame(
							backupList, notificator, name.getText(), scriptContent, isScriptActive
						);
					break;

				case MouseEvent.BUTTON3:
					itemMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
					break;
			}
		}
	}

	public class RunButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			backupProcess.start(getBackupName());
			backupProcess.join();
			long lastExecution = ChainSet.retrieveDate(getBackupName());
			String formatedLastExecution =
				lastExecution == 0 ? "-" : new SimpleDateFormat().format(new Date(lastExecution));
			lastExecuted.setText(Text.LAST_EXECUTION_LABEL + " " + formatedLastExecution);

			if (backupProcess.getException() != null) {
				JOptionPane.showMessageDialog(
					backupList,
					Text.ErrorDialog.STATUS(backupProcess.getException()),
					Text.ErrorDialog.TITLE, JOptionPane.ERROR_MESSAGE
				);
			}
		}
	}
}
