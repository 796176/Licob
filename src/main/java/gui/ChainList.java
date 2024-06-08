package gui;

import constants.Colors;
import constants.Dimensions;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ChainList extends JScrollPane {
	private ChainItem[] chainItems = new ChainItem[]{};
	private JFrame parent;
	private boolean touched = false;
	public ChainList(JFrame parent) {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
		assert parent != null;

		this.parent = parent;

		initiateList();
		verticalScrollBar.setUnitIncrement(Dimensions.DEFAULT_PANEL_SCROLL);
		setPreferredSize(getMinimumSize());
	}

	public ChainRule[] getItems() {
		ChainRule[] items = new ChainRule[chainItems.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new ChainRule(chainItems[i]);
		}
		return items;
	}

	private JPanel getPanel(){
		JPanel panel = new JPanel();
		panel.setBackground(Colors.LAYER1);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		return panel;
	}

	private void initiateList(){
		JPanel innerPanel = getPanel();
		for (ChainItem item: chainItems){
			innerPanel.add(item);
			if (item != chainItems[chainItems.length - 1])
				innerPanel.add(Box.createRigidArea(new Dimension(0, Dimensions.DEFAULT_COMPONENT_OFFSET)));
		}
		setViewportView(innerPanel);
	}

	public void addItem(ChainRule chainRule){
		assert chainRule != null;

		chainItems = Arrays.copyOf(chainItems, chainItems.length + 1);
		chainItems[chainItems.length - 1] = new ChainItem(this, parent, chainRule);
		initiateList();
		touched = true;
	}

	public void removeItem(ChainItem item) {
		int index = 0;
		while (chainItems[index] != item && ++index < chainItems.length);
		if (index == chainItems.length) return;

		System.arraycopy(chainItems, index + 1, chainItems, index, chainItems.length - 1 - index);
		chainItems = Arrays.copyOf(chainItems, chainItems.length - 1);
		initiateList();
		touched = true;
	}

	public void editItem(ChainItem oldItem, ChainRule chainRule) {
		int index = 0;
		while (chainItems[index] != oldItem && ++index < chainItems.length);
		if (index == chainItems.length) return;
		chainItems[index] = new ChainItem(this, parent, chainRule);
		initiateList();
		touched = true;
	}

	public boolean isEmpty() {
		return chainItems.length == 0;
	}

	public boolean isTouched() {
		return touched;
	}

	public void untouch() {
		touched = false;
	}
}
