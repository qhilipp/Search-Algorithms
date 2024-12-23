package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import searchAlgorithms.GeneralSearch;
import stateSpace.StateSpace;
import util.Copyable;
import util.Nameable;
import util.Position;

public class StateSpaceWindow<Node extends Position&Nameable&Copyable> extends JFrame {

	private StateSpaceView<Node> ssv;
	private StateSpaceEditor<Node> sse;
	
	public StateSpaceWindow(GeneralSearch<Node> searchAlgorithm) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		sse = new StateSpaceEditor<>(searchAlgorithm);
		sse.setPreferredSize(new Dimension(200, 0));
		add(sse, BorderLayout.WEST);
		
		ssv = new StateSpaceView<>(searchAlgorithm);
		add(ssv, BorderLayout.CENTER);
		
		ssv.setSSVListener(new SSVListener<Node>() {
			
			@Override
			public void nodeSelected(Node node) {
				sse.select(node);
			}

			@Override
			public void hasPathChanged(boolean hasPath) {
				sse.enableSearch(!hasPath);
			}
			
		});
		
		sse.setSSEListener(new SSEListener<Node>() {
			@Override
			public void nextIteration() {
				ssv.nextIteration();
			}
			
			@Override
			public void continueSearch() {
				ssv.continueSearch();
			}
			
			@Override
			public void select(Node node) {
				ssv.select(node);
			}
			
			@Override
			public void reset() {
				ssv.initialize();
			}
			
			@Override
			public void setSearchSpeed(int speed) {
				double p = 0.1;
				double a = 5000 / (1 - Math.pow(100, -p));
		        double b = 5000 - a;
		        int delay = (int) (a * Math.pow(speed, -p) + b);
		        System.out.println(delay);
				ssv.setSearchDelay(delay);
			}
		});
		
		setVisible(true);
	}
	
}
