package gui;

import constants.*;
import licob.ChainSet;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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

		String[] backupItemNames = new File(Configuration.CHAIN_SETS_DIRECTORY).list();
		BackupList backupList = new BackupList();
		for (int backupItemIndex = 0; backupItemIndex < backupItemNames.length; backupItemIndex++) {
			File backupFile = new File(Configuration.CHAIN_SETS_DIRECTORY, backupItemNames[backupItemIndex]);
			boolean scriptEnabled = ChainSet.retrieveScript(backupFile, null);
			int chainNumber = ChainSet.retrieveChainNumber(backupFile);
			long date = ChainSet.retrieveDate(backupFile);
			String lastExecution = date == 0 ? "-" : new SimpleDateFormat().format(new Date(date));
			backupList.addBackupItem(backupItemNames[backupItemIndex], chainNumber, scriptEnabled, lastExecution);
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
