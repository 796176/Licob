package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.BiConsumer;

import constants.*;

public class ChainItem extends JPanel {
	private final ChainConfigurationFrame chainConfigurationFrame;
	private LLabel type;
	private LLabel from;
	private LLabel to;
	private JButton remove;
	private String exceptions;
	public ChainItem(ChainConfigurationFrame ccf, String type, String source, String destination, String exceptions, int id) {
		assert ccf != null && type != null && source != null && destination != null && exceptions != null;

		chainConfigurationFrame = ccf;
		this.exceptions = exceptions;

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

	public String getType(){
		return type.getText();
	}

	public String getSource(){
		return from.getText();
	}

	public String getDestination() {
		return to.getText();
	}

	public String getExceptions() {
		return exceptions;
	}

	private class ChainItemPanelListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
				ChainTypes chainType = null;
				try {
					chainType = ChainTypes.valueOf(getType());
				} catch (IllegalArgumentException ignored) {}
				File source = new File(getSource());
				if (!source.exists()) source = null;
				File destination = new File(getDestination());
				if (!destination.exists()) destination = null;

				BiConsumer<ChainConfigurationFrame, ChainConfigurationDialog> biConsumer = (ccf, ccd) -> {
					String src = ccd.getSource() == null ? "Not set" : ccd.getSource();
					String dst = ccd.getDestination() == null ? "Not set" : ccd.getDestination();
					ccf.replaceChainRule(ChainItem.this, ccd.getChainType(), src, dst, ccd.getExceptions());
				};
				var dialog = new ChainConfigurationDialog(
					ChainItem.this.chainConfigurationFrame, chainType, source, destination, getExceptions(), biConsumer
				);
			}
		}
	}
}
