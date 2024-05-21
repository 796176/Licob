package gui;

import constants.Colors;
import constants.Dimensions;
import constants.Fonts;
import constants.Text;
import licob.ChainSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChainConfigurationFrame extends LFrame {
	private JCheckBox scriptCheckBox;
	private ScriptArea scriptArea;
	private ChainList chainList;
	private JTextField nameField;
	private boolean statusChanged = false;

	public ChainConfigurationFrame(String name, ChainItem[] items, String scriptContent, boolean isScriptActive) {
		super(Text.APP_NAME, Dimensions.CHAIN_CONFIGURATION_FRAME_WIDTH, Dimensions.CHAIN_CONFIGURATION_FRAME_HEIGHT);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		assert name != null && items != null && scriptContent != null;

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
		chainList = new ChainList(items);
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

	public ChainConfigurationFrame(){
		this("", new ChainItem[]{}, "", false);
	}

	private class SaveButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (chainList.isEmpty()) {
				NotificationDialog dialog = new NotificationDialog(ChainConfigurationFrame.this, "No data to save");
				return;
			}
			String name = nameField.getText();
			ChainItem[] chainItems = chainList.getChainItems();
			boolean scriptStatus = scriptCheckBox.isSelected();
			String scriptContent = scriptArea.getContent();
			try {
				ChainSet.addChainSet(name, chainItems, scriptContent, scriptStatus);
				NotificationDialog dialog = new NotificationDialog(ChainConfigurationFrame.this, "Successfully saved");
				statusChanged = false;
			} catch (IOException e) {
				NotificationDialog dialog = new NotificationDialog(ChainConfigurationFrame.this, e.toString());
			}
		}
	}
}
