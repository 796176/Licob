package gui;

import constants.Dimensions;

import javax.swing.*;
import java.awt.*;

public class NotificationDialog extends LDialog {
	public NotificationDialog(JFrame frame, String mes){
		super(frame, "", Dimensions.CONFIRMATION_DIALOG_WIDTH, Dimensions.CONFIRMATION_DIALOG_HEIGHT);

		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weighty = 1;
		constraints.weightx = 1;
		LLabel label = new LLabel(mes);
		bagLayout.setConstraints(label, constraints);
		add(label);

		setVisible(true);
	}
}
