package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import searchAlgorithms.GeneralSearch;
import searchStrategy.SearchStrategy;
import util.Copyable;
import util.Nameable;
import util.Position;

public class StateSpaceEditor<Node extends Position&Nameable&Copyable> extends JPanel {

    private GeneralSearch<Node> searchAlgorithm;
    private Node selected = null;

    private JLabel nameLabel;
    private JLabel positionLabel;
    private JLabel neighborsTitle;
    private JPanel neighborsList;
    private JScrollPane scrollPane;
    private JSlider slider;
    private JLabel sliderValue;
    private JLabel heuristicValueLabel;
    private JLabel pathValueLabel;
    private JButton nextStepButton;
    private JButton searchButton;

    private SSEListener<Node> listener;

    public StateSpaceEditor(GeneralSearch<Node> searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;

        setBackground(Color.GRAY);
        setLayout(new BorderLayout());

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setVisible(false);
        add(nameLabel, BorderLayout.NORTH);
        
        add(Box.createVerticalStrut(10));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.GRAY);

        positionLabel = new JLabel();
        positionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        positionLabel.setHorizontalAlignment(JLabel.CENTER);
        positionLabel.setVisible(false);
        centerPanel.add(positionLabel);
        
        heuristicValueLabel = new JLabel("Heuristic Value: -");
        heuristicValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        heuristicValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(heuristicValueLabel);
        
        pathValueLabel = new JLabel("Path Value: -");
        pathValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        pathValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(pathValueLabel);

        centerPanel.add(Box.createVerticalStrut(10));
        
        neighborsTitle = new JLabel("Neighbors");
        neighborsTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        neighborsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(neighborsTitle);

        neighborsList = new JPanel();
        neighborsList.setLayout(new BoxLayout(neighborsList, BoxLayout.X_AXIS));
        neighborsList.setBackground(Color.GRAY);

        scrollPane = new JScrollPane(neighborsList, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.GRAY);

        sliderValue = new JLabel();
        bottomPanel.add(sliderValue);

        slider = new JSlider(1, 100);
        slider.setBackground(Color.GRAY);
        slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, slider.getPreferredSize().height));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sliderValue.setText("Speed: " + slider.getValue());
                if(listener != null) listener.setSearchSpeed(slider.getValue());
            }
        });
        bottomPanel.add(slider);

        nextStepButton = new JButton("Next step");
        nextStepButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextStepButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, nextStepButton.getPreferredSize().height));
        nextStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.nextIteration();
                }
            }
        });
        bottomPanel.add(nextStepButton);

        searchButton = new JButton("Search");
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchButton.getPreferredSize().height));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.continueSearch();
                }
            }
        });
        bottomPanel.add(searchButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, resetButton.getPreferredSize().height));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.reset();
                }
            }
        });
        bottomPanel.add(resetButton);

        add(bottomPanel, BorderLayout.SOUTH);

        select(null);
    }

    public void select(Node node) {
        this.selected = node;

        neighborsList.removeAll();

        if(selected != null) {
            nameLabel.setText(selected.getName());

            positionLabel.setText("Position: " + selected.getPosition());

            heuristicValueLabel.setText("Heuristic Value: " + searchAlgorithm.getHeuristic().futureCost(searchAlgorithm.getStateSpace(), node));

            ArrayList<Node> bestPath = null;
//            for(SearchStrategy<ArrayList<Node>>.PathRating pathRating : searchAlgorithm.getStrategy().getList()) {
//            	if(pathRating.path.getLast().equals(node) && bestPath == null) {
//            		bestPath = pathRating.path;
//            	}
//            }
            if(bestPath != null) {            	
            	pathValueLabel.setText("Path Value: " + searchAlgorithm.getPathEvaluator().pastCost(searchAlgorithm.getStateSpace(), bestPath));
            } else {
            	pathValueLabel.setText("Path Value: -");
            }

            for(Node neighbor : searchAlgorithm.getStateSpace().getNeighbors(node)) {
                neighborsList.add(getButton(neighbor));
            }
        }

        nameLabel.setVisible(selected != null);
        positionLabel.setVisible(selected != null);
        heuristicValueLabel.setVisible(selected != null);
        pathValueLabel.setVisible(selected != null);
        neighborsTitle.setVisible(neighborsList.getComponentCount() != 0);
        neighborsList.repaint();
    }

    private Component getButton(Node node) {
        JButton button = new JButton(node.getName());
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select(node);
                if (listener != null) {
                    listener.select(node);
                }
            }
        });

        return button;
    }

    public void setSSEListener(SSEListener<Node> listener) {
        this.listener = listener;
        slider.setValue(25);
    }

    public void enableSearch(boolean enabled) {
        nextStepButton.setEnabled(enabled);
        searchButton.setEnabled(enabled);
    }
}
