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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.function.Consumer;

public class ChainConfigurationFrame extends LFrame {
	private JCheckBox scriptCheckBox;
	private ScriptArea scriptArea;
	private ChainList chainList;
	private JTextField nameField;
	private final BackupList backupList;
	private final BackupListNotificator backupListNotificator;

	public ChainConfigurationFrame(
		BackupList list,
		BackupListNotificator notificator,
		String name,
		String scriptContent,
		boolean isScriptActive
	) {
		super(Text.APP_NAME, Dimensions.CHAIN_CONFIGURATION_FRAME_WIDTH, Dimensions.CHAIN_CONFIGURATION_FRAME_HEIGHT);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (chainList.isTouched() || scriptArea.isTouched()) {
					int result = JOptionPane.showConfirmDialog(
						ChainConfigurationFrame.this,
						Text.EXIT_WITHOUT_SAVING,
						Text.CONFIRM,
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
					);

					if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) return;
				}
				ChainConfigurationFrame.this.setVisible(false);
			}
		});

		assert list != null && notificator != null && name != null && scriptContent != null;

		backupList = list;
		backupListNotificator = notificator;

		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);

		GridBagConstraints nameConstraints = new GridBagConstraints();
		nameConstraints.fill = GridBagConstraints.HORIZONTAL;
		nameConstraints.insets =
			new Insets(
				Dimensions.DEFAULT_COMPONENT_OFFSET,
				Dimensions.DEFAULT_COMPONENT_OFFSET,
				0,
				Dimensions.DEFAULT_COMPONENT_OFFSET
			);
		LLabel nameLabel = new LLabel(Text.BACKUP_NAME_LABEL);
		bagLayout.setConstraints(nameLabel, nameConstraints);
		add(nameLabel);

		nameConstraints.insets =
			new Insets(Dimensions.DEFAULT_COMPONENT_OFFSET, 0, 0, Dimensions.DEFAULT_COMPONENT_OFFSET);
		nameConstraints.gridwidth = GridBagConstraints.REMAINDER;
		nameField = new JTextField(name);
		nameField.setBackground(Colors.LAST_LAYER);
		nameField.setForeground(Colors.FONT_COLOR);
		nameField.setCaretColor(Colors.FONT_COLOR);
		nameField.setFont(Fonts.MEDIUM_DEFAULT);
		bagLayout.setConstraints(nameField, nameConstraints);
		add(nameField);

		GridBagConstraints chainListConstraints = new GridBagConstraints();
		chainListConstraints.weighty = 5;
		chainListConstraints.weightx = 1;
		chainListConstraints.gridwidth = GridBagConstraints.REMAINDER;
		chainListConstraints.fill = GridBagConstraints.BOTH;
		chainListConstraints.insets =
			new Insets(
				Dimensions.LIST_PANEL_OFFSET, Dimensions.LIST_PANEL_OFFSET, 0, Dimensions.LIST_PANEL_OFFSET
			);
		chainList = new ChainList(this);
		try {
			ChainRule[] chainRules = ChainSet.getChainSet(name);
			for (ChainRule chainRule : chainRules) {
				chainList.addItem(chainRule);
			}
		} catch (IOException | NullPointerException exception) {
			JOptionPane.showMessageDialog(
				this,
				Text.ErrorDialog.STATUS(exception),
				Text.ErrorDialog.TITLE,
				JOptionPane.ERROR_MESSAGE
			);
		}
		chainList.untouch();
		bagLayout.setConstraints(chainList, chainListConstraints);
		add(chainList);

		GridBagConstraints buttonConstrains = new GridBagConstraints();
		buttonConstrains.anchor = GridBagConstraints.WEST;
		buttonConstrains.gridwidth = GridBagConstraints.REMAINDER;
		buttonConstrains.insets = new Insets(
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			0
		);
		JButton addButton = new JButton(Text.ADD_BUTTON);
		addButton.setFont(Fonts.MEDIUM_DEFAULT);
		addButton.setBackground(Colors.ADD_BUTTON_COLOR);
		addButton.addActionListener(new AddButtonListener());
		JButton saveButton = new JButton(Text.SAVE_BUTTON);
		saveButton.setFont(Fonts.MEDIUM_DEFAULT);
		saveButton.setBackground(Colors.ADD_BUTTON_COLOR);
		saveButton.addActionListener(new SaveButtonListener());
		JPanel buttonPanel = LGridBagLayout.componentString(Dimensions.DEFAULT_COMPONENT_OFFSET, addButton, saveButton);
		bagLayout.setConstraints(buttonPanel, buttonConstrains);
		add(buttonPanel);

		GridBagConstraints scriptCheckBoxConstraints = new GridBagConstraints();
		scriptCheckBoxConstraints.anchor = GridBagConstraints.WEST;
		scriptCheckBoxConstraints.gridwidth = GridBagConstraints.REMAINDER;
		scriptCheckBoxConstraints.insets = new Insets(
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			0
		);
		scriptCheckBox = new JCheckBox(Text.ENABLE_SCRIPT_CHECKBOX, isScriptActive);
		scriptCheckBox.setFont(Fonts.MEDIUM_DEFAULT);
		scriptCheckBox.setForeground(Colors.FONT_COLOR);
		scriptCheckBox.setBackground(Colors.LAYER0);
		scriptCheckBox.addActionListener( actionEvent -> {
			JCheckBox checkBox = (JCheckBox) actionEvent.getSource();
			if (checkBox.isSelected()){
				scriptArea.setActive();
			} else {
				scriptArea.setPassive();
			}
		});
		bagLayout.setConstraints(scriptCheckBox, scriptCheckBoxConstraints);
		add(scriptCheckBox);

		GridBagConstraints scriptAreaConstraints = new GridBagConstraints();
		scriptAreaConstraints.weighty = 5;
		scriptAreaConstraints.gridwidth = GridBagConstraints.REMAINDER;
		scriptAreaConstraints.fill = GridBagConstraints.BOTH;
		scriptAreaConstraints.insets = new Insets(
			0,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET
		);
		scriptArea = new ScriptArea(scriptContent, isScriptActive);
		bagLayout.setConstraints(scriptArea, scriptAreaConstraints);
		add(scriptArea);

		setVisible(true);
	}

	public String getBackupName() {
		return nameField.getText();
	}

	public int getChainNumber() {
		return chainList.getItems().length;
	}

	public boolean getScriptState() {
		return scriptCheckBox.isSelected();
	}

	public String getScriptContent() {
		return scriptArea.getContent();
	}

	private class SaveButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (chainList.isEmpty()) {
				JOptionPane.showMessageDialog(
					ChainConfigurationFrame.this,
					Text.SaveDialog.FAILED_NO_DATA,
					Text.SaveDialog.TITLE,
					JOptionPane.INFORMATION_MESSAGE
				);
				return;
			}
			String name = nameField.getText();
			ChainRule[] chainItems = chainList.getItems();
			boolean scriptStatus = scriptCheckBox.isSelected();
			String scriptContent = scriptArea.getContent();
			try {
				ChainSet.flushChainSet(name, chainItems, scriptContent, scriptStatus);
				JOptionPane.showMessageDialog(
					ChainConfigurationFrame.this,
					Text.SaveDialog.SAVED,
					Text.SaveDialog.TITLE,
					JOptionPane.INFORMATION_MESSAGE
				);
				chainList.untouch();
				scriptArea.untouch();
				backupListNotificator.notify(backupList, ChainConfigurationFrame.this);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
					ChainConfigurationFrame.this,
					Text.ErrorDialog.STATUS(e),
					Text.ErrorDialog.TITLE,
					JOptionPane.ERROR_MESSAGE
				);
			}
		}
	}

	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			Consumer<ChainConfigurationDialog> consumer = (ccd) -> {
				chainList.addItem(
					new ChainRule(ccd.getChainType(), ccd.getSource(), ccd.getDestination(), ccd.getExceptions())
				);
			};

			ChainConfigurationDialog dialog =
				new ChainConfigurationDialog(ChainConfigurationFrame.this, consumer);
		}
	}
}
