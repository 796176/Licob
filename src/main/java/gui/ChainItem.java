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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Consumer;

import constants.*;

public class ChainItem extends JPanel {
	private final ChainList chainList;
	private JFrame parent;
	private final ChainRule chainRule;
	public ChainItem(ChainList cl, JFrame parent, ChainRule chainRule) {
		assert cl != null && parent != null && chainRule != null;

		chainList = cl;
		this.chainRule = chainRule;
		this.parent = parent;

		setMinimumSize(Dimensions.CHAIN_ITEM);
		setMaximumSize(new Dimension(Integer.MAX_VALUE, Dimensions.CHAIN_ITEM.height));
		setPreferredSize(Dimensions.CHAIN_ITEM);
		setBackground(Colors.LAYER2);
		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);
		addMouseListener(new ChainItemPanelListener());

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.anchor = GridBagConstraints.WEST;
		labelConstraints.gridwidth = GridBagConstraints.REMAINDER;
		labelConstraints.insets = new Insets(
			Dimensions.SMALL_COMPONENT_OFFSET,
			Dimensions.DEFAULT_COMPONENT_OFFSET,
			0,
			0
		);
		LLabel typeLabel;
		try {
			typeLabel = new LLabel(ChainTypes.valueOf(chainRule.type).getVisualRepresentation());
		} catch (NullPointerException | IllegalArgumentException exception) {
			typeLabel = new LLabel(ChainTypes.Content.getVisualRepresentation());
		}
		typeLabel.setFont(Fonts.SMALL_DEFAULT);
		bagLayout.setConstraints(typeLabel, labelConstraints);
		add(typeLabel);

		LLabel sourceLabel = chainRule.source == null ? new LLabel("Not set") : new LLabel(chainRule.source);
		sourceLabel.setFont(Fonts.SMALL_DEFAULT);
		bagLayout.setConstraints(sourceLabel, labelConstraints);
		add(sourceLabel);

		LLabel destinationLabel =
			chainRule.destination == null ? new LLabel("Not set") : new LLabel(chainRule.destination);
		destinationLabel.setFont(Fonts.SMALL_DEFAULT);
		bagLayout.setConstraints(destinationLabel, labelConstraints);
		add(destinationLabel);

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
		JButton removeButton = new JButton("-");
		removeButton.setBackground(Colors.DELETE_BUTTON_COLOR);
		removeButton.setFont(Fonts.MEDIUM_MONO);
		removeButton.addActionListener(new RemoveButtonListener());
		bagLayout.setConstraints(removeButton, removeButtonConstraints);
		add(removeButton);
	}

	public String getType(){
		return chainRule.type;
	}

	public String getSource(){
		return chainRule.source;
	}

	public String getDestination() {
		return chainRule.destination;
	}

	public String getExceptions() {
		return chainRule.exceptions;
	}

	private class ChainItemPanelListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
				ChainTypes chainType = null;
				try {
					chainType = ChainTypes.valueOf(getType());
				} catch (IllegalArgumentException ignored) {}
				
				File source = null;
				if (getSource() != null && new File(getSource()).exists())
					source = new File(getSource());

				File destination = null;
				if (getDestination() != null && new File(getDestination()).exists())
					destination = new File(getDestination());

				Consumer<ChainConfigurationDialog> consumer = (ccd) -> {
					chainList.editItem(
						ChainItem.this,
						new ChainRule(
							ccd.getChainType(),
							ccd.getSource(),
							ccd.getDestination(),
							ccd.getExceptions()
						)
					);
				};
				var dialog = new ChainConfigurationDialog(
					parent, chainType, source, destination, getExceptions(), consumer
				);
			}
		}
	}

	private class RemoveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			ChainItem chainItem = ChainItem.this;
			chainItem.chainList.removeItem(chainItem);
		}
	}
}
