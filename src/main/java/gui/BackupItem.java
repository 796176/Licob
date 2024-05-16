package gui;

import constants.Colors;
import constants.Dimensions;
import constants.Fonts;
import constants.Text;

import javax.swing.*;
import java.awt.*;

public class BackupItem extends JPanel {
	LLabel name;
	LLabel subtitle;
	LLabel lastExecuted;
	JButton deleteButton;
	JButton runButton;

	public BackupItem(String name, int chainNumber, boolean bashScript, String date) {
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
		bagLayout.setConstraints(runButton, buttonConstraints);
		add(runButton);

		buttonConstraints.anchor = GridBagConstraints.SOUTHEAST;
		buttonConstraints.gridwidth = GridBagConstraints.REMAINDER;
		deleteButton = new JButton(Text.DELETE_BUTTON);
		deleteButton.setMinimumSize(Dimensions.BACKUP_ITEM_CONTROL_BUTTON);
		deleteButton.setPreferredSize(Dimensions.BACKUP_ITEM_CONTROL_BUTTON);
		deleteButton.setFont(Fonts.MEDIUM_DEFAULT);
		deleteButton.setBackground(Colors.DELETE_BUTTON_COLOR);
		bagLayout.setConstraints(deleteButton, buttonConstraints);
		add(deleteButton);
	}
}
