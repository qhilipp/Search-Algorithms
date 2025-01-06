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

public class StateSpaceSidebar<Node extends Position & Nameable & Copyable> extends JPanel {

    private GeneralSearch<Node> searchAlgorithm;
    private Node selected = null;

    private LabeledComponent<JLabel> nameLabel;
    private LabeledComponent<JLabel> positionLabel;
    private LabeledComponent<JScrollPane> neighborsPanel;
    private LabeledComponent<JSlider> speedSlider;
    private LabeledComponent<JLabel> heuristicValueLabel;
    private LabeledComponent<JLabel> pathValueLabel;
    private LabeledComponent<JLabel> totalValueLabel;
    private JButton nextStepButton;
    private JButton searchButton;
    private JButton resetButton;

    private SSEListener<Node> listener;

    public StateSpaceSidebar(GeneralSearch<Node> searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;

        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());
                
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(getBackground());

        nameLabel = createLabeledValue("Name");
        centerPanel.add(nameLabel);
        
        positionLabel = createLabeledValue("Position");
        centerPanel.add(positionLabel);
        
        heuristicValueLabel = createLabeledValue("Heuristic value");
        centerPanel.add(heuristicValueLabel);
        
        pathValueLabel = createLabeledValue("Path value");
        centerPanel.add(pathValueLabel);
        
        totalValueLabel = createLabeledValue("Total value");
        centerPanel.add(totalValueLabel);

        centerPanel.add(Box.createVerticalStrut(10));
        
        JPanel neighborsList = new JPanel();
        neighborsList.setLayout(new BoxLayout(neighborsList, BoxLayout.X_AXIS));
        neighborsList.setBackground(getBackground());

        JScrollPane scrollPane = new JScrollPane(neighborsList, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        neighborsPanel = new LabeledComponent<>("Neighbors", scrollPane);
        centerPanel.add(neighborsPanel);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(getBackground());

        JSlider slider = new JSlider(1, 100);
        slider.setBackground(getBackground());
        slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, slider.getPreferredSize().height));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(listener != null) listener.setSearchSpeed(slider.getValue());
            }
        });
        
        speedSlider = new LabeledComponent<>("Speed", slider);
        bottomPanel.add(speedSlider);

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

        resetButton = new JButton("Reset");
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

    private LabeledComponent<JLabel> createLabeledValue(String title) {
    	JLabel label = new JLabel("-");
    	label.setFont(new Font("Arial", Font.PLAIN, 14));
    	label.setForeground(Color.DARK_GRAY);
    	label.setAlignmentX(Component.LEFT_ALIGNMENT);
    	
        return new LabeledComponent<>(title, label);
    }

    public void select(Node node) {
        this.selected = node;
        
        JPanel neighborsList = ((JPanel) neighborsPanel.component.getViewport().getView());
        neighborsList.removeAll();

        if(selected != null) {
            nameLabel.component.setText(selected.getName());

            positionLabel.component.setText(selected.getPosition().toString());

            heuristicValueLabel.component.setText(searchAlgorithm.getHeuristic().futureCost(searchAlgorithm.getStateSpace(), node) + "");

            ArrayList<Node> bestPath = null;
            for(SearchStrategy<Node>.PathRating pathRating : searchAlgorithm.getStrategy().getHistory()) {
                if(pathRating.path.getLast().equals(node) && bestPath == null) {
                    bestPath = pathRating.path;
                    break;
                }
            }
            if(bestPath != null) {
                double bestPathValue = searchAlgorithm.getPathEvaluator().pastCost(searchAlgorithm.getStateSpace(), bestPath);
                pathValueLabel.component.setText(bestPathValue + "");
                totalValueLabel.component.setText(searchAlgorithm.rate(bestPath) + "");
            } else {
                pathValueLabel.component.setText("-");
                totalValueLabel.component.setText("-");
            }

            for(Node neighbor : searchAlgorithm.getStateSpace().getNeighbors(node)) {
            	neighborsList.add(getButton(neighbor));
            }
        }

        nameLabel.setVisible(selected != null);
        positionLabel.setVisible(selected != null);
        heuristicValueLabel.setVisible(selected != null);
        pathValueLabel.setVisible(selected != null);
        totalValueLabel.setVisible(selected != null);
        neighborsPanel.setVisible(neighborsList.getComponentCount() != 0);
        neighborsList.repaint();
    }

    private Component getButton(Node node) {
        JButton button = new JButton(node.getName());
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
        speedSlider.component.setValue(25);
    }

    public void enableSearch(boolean enabled) {
        nextStepButton.setEnabled(enabled);
        searchButton.setEnabled(enabled);
    }
    
    class LabeledComponent<C extends Component> extends JPanel {
    	JLabel label;
    	C component;
    	
    	public LabeledComponent(String text, C component) {
    		this.component = component;
    		
    		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.LIGHT_GRAY);
    		
    		label = new JLabel(text);
    		label.setFont(new Font("Arial", Font.BOLD, 10));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(label);
            
            component.setBackground(Color.GRAY);
            
            add(component);
            
            add(Box.createVerticalStrut(5));
    	}
    }
}
