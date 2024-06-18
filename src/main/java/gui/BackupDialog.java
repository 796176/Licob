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

import constants.Dimensions;
import constants.Text;
import licob.LicobEngineInterface;
import licob.LicobStatus;

import javax.swing.*;
import java.awt.*;

public class BackupDialog extends LDialog{
	private final LLabel chainLabel = new LLabel();
	private final LLabel currentFileLabel = new LLabel();
	private final LLabel allFilesStatusLabel = new LLabel();
	private final LLabel allBytesStatusLabel = new LLabel();
	public boolean interrupted = false;

	public BackupDialog(JFrame frame) {
		super(
			frame,
			Text.BackupDialog.TITLE,
			true,
			Dimensions.BACKUP_DIALOG_WIDTH,
			Dimensions.BACKUP_DIALOG_HEIGHT
		);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.weighty = 1;
		bagLayout.setConstraints(chainLabel, constraints);
		add(chainLabel);

		bagLayout.setConstraints(currentFileLabel, constraints);
		add(currentFileLabel);

		bagLayout.setConstraints(allFilesStatusLabel, constraints);
		add(allFilesStatusLabel);

		bagLayout.setConstraints(allBytesStatusLabel, constraints);
		add(allBytesStatusLabel);

		constraints.weighty = 30;
		constraints.anchor = GridBagConstraints.SOUTH;
		JButton cancelButton = new JButton(Text.CANCEL_BUTTON);
		cancelButton.addActionListener(actionEvent -> interrupted = true);
		bagLayout.setConstraints(cancelButton, constraints);
		add(cancelButton);
	}

	public void update(LicobEngineInterface licobEngineInterface, LicobStatus licobStatus, int chainIndex) {
		assert licobEngineInterface != null && licobStatus != null && chainIndex > -1;

		setChainLabel(chainIndex);
		setCurrentFileLabel(
			licobStatus.getCurrentFile().getAbsolutePath(),
			licobStatus.getCurrentProgress(),
			licobStatus.getCurrentTotal()
		);
		setAllFilesStatusLabel(licobStatus.getFinishedFiles().size(), licobStatus.getAllFiles().size());
		setAllBytesStatusLabel(licobStatus.getProgress(), licobStatus.getTotal());
	}

	private void setChainLabel(int labelIndex) {
		chainLabel.setText(Text.BackupDialog.CHAIN_LABEL(labelIndex + 1));
	}

	private void setCurrentFileLabel(String currentFile, long copied, long total) {
		currentFileLabel.setText(Text.BackupDialog.COPIED_CURRENT_FILE(currentFile, copied, total));
	}

	private void setAllFilesStatusLabel(long copied, long total) {
		allFilesStatusLabel.setText(Text.BackupDialog.COPIED_FILES(copied, total));
	}

	private void setAllBytesStatusLabel(long copied, long total) {
		allBytesStatusLabel.setText(Text.BackupDialog.COPIED_BITES(copied, total));
	}
}
