package gui;

import constants.Colors;
import constants.Dimensions;
import constants.Fonts;
import constants.Text;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends LFrame{
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
		BackupList backupList = new BackupList(
			new BackupItem[]{
				new BackupItem("name", 5, false, "20222"),
				new BackupItem("name1", 5, false, "20222"),
				new BackupItem("name2", 5, false, "20222"),
				new BackupItem("name3", 5, false, "20222"),
				new BackupItem("name3", 5, false, "20222"),
				new BackupItem("name3", 5, false, "20222"),
				new BackupItem("name3", 5, false, "20222"),
				new BackupItem("name3", 5, false, "20222"),
				new BackupItem("name3", 5, false, "20222"),
				new BackupItem("name3", 5, false, "20222"),
				new BackupItem("name4", 5, false, "20222")
			}
		);
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
}
