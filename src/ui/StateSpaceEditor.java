package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import stateSpace.StateSpace;
import util.Copyable;
import util.Nameable;
import util.Position;

public class StateSpaceEditor<Node extends Position&Nameable&Copyable> extends JPanel {

	private StateSpace<Node> space;
	private Node selected = null;
	
	private JLabel nameLabel;
    private JPanel neighborsList;
    private JScrollPane scrollPane;
    
    private SSEListener<Node> listener;
	
	public StateSpaceEditor(StateSpace<Node> space) {
		this.space = space;
		
		setBackground(Color.GRAY);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        nameLabel = new JLabel("-");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameLabel);

        JLabel neighborsTitle = new JLabel("Neighbors");
        neighborsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(neighborsTitle);

        neighborsList = new JPanel();
        neighborsList.setLayout(new BoxLayout(neighborsList, BoxLayout.X_AXIS));
        neighborsList.setBackground(Color.GRAY);

        scrollPane = new JScrollPane(neighborsList);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.GRAY);

        JButton saveButton = new JButton("Next step");
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listener != null) {
					listener.nextIteration();
				}
			}
        });
        JButton cancelButton = new JButton("Search");
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listener != null) {
					listener.continueSearch();
				}
			}
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void select(Node node) {
		this.selected = node;
		setVisible(selected != null);
		
		if(node == null) return;
		
		nameLabel.setText(selected.getName());
		neighborsList.removeAll();
		for(Node neighbor : space.getNeighbors(node)) {
			neighborsList.add(getButton(neighbor));
		}
		repaint();
	}
	
	private Component getButton(Node node) {
		JButton button = new JButton(node.getName());
		button.setSize(button.getPreferredSize());
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(node);
				if(listener != null) {
					listener.select(node);
				}
			}		
		});
		
		return button;
	}
	
	public void setSSEListener(SSEListener<Node> listener) {
		this.listener = listener;
	}
	
}
