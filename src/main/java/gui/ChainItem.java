package gui;

import javax.swing.*;
import java.awt.*;
import constants.*;

public class ChainItem extends JPanel {
	LLabel type;
	LLabel from;
	LLabel to;
	JButton remove;
	public ChainItem(String type, String source, String destination) {
		setMinimumSize(Dimensions.CHAIN_ITEM);
		setPreferredSize(Dimensions.CHAIN_ITEM);
		setBackground(Colors.LAYER2);
		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.anchor = GridBagConstraints.WEST;
		labelConstraints.gridwidth = GridBagConstraints.REMAINDER;
		labelConstraints.insets = new Insets(
			Dimensions.SMALL_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			0,
			0
		);
		this.type = new LLabel(type);
		this.type.setFont(Fonts.SMALL_DEFAULT);
		bagLayout.setConstraints(this.type, labelConstraints);
		add(this.type);

		from = new LLabel(source);
		from.setFont(Fonts.SMALL_DEFAULT);
		bagLayout.setConstraints(from, labelConstraints);
		add(from);

		to = new LLabel(destination);
		to.setFont(Fonts.SMALL_DEFAULT);
		bagLayout.setConstraints(to, labelConstraints);
		add(to);

		GridBagConstraints removeButtonConstraints = new GridBagConstraints();
		removeButtonConstraints.anchor = GridBagConstraints.SOUTHEAST;
		removeButtonConstraints.weighty = 1;
		removeButtonConstraints.weightx = 1;
		removeButtonConstraints.insets = new Insets(
			0,
			0,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET

		);
		remove = new JButton("-");
		remove.setBackground(Colors.DELETE_BUTTON_COLOR);
		remove.setFont(Fonts.MEDIUM_MONO);
		bagLayout.setConstraints(remove, removeButtonConstraints);
		add(remove);
	}
}
