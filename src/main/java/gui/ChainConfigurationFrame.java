package gui;

import constants.Colors;
import constants.Dimensions;
import constants.Fonts;
import constants.Text;

import javax.swing.*;
import java.awt.*;

public class ChainConfigurationFrame extends LFrame {
	public ChainConfigurationFrame() {
		super(Text.APP_NAME, Dimensions.CHAIN_CONFIGURATION_FRAME_WIDTH, Dimensions.CHAIN_CONFIGURATION_FRAME_HEIGHT);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

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
		JTextField nameField = new JTextField();
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
		ChainList chainList = new ChainList( new ChainItem[]{});
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
		JCheckBox scriptCheckBox = new JCheckBox(Text.ENABLE_SCRIPT_CHECKBOX);
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
		ScriptArea scriptArea = new ScriptArea();
		bagLayout.setConstraints(scriptArea, scriptAreaConstraints);
		add(scriptArea);

		setVisible(true);
	}
}
