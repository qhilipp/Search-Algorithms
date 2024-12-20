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
	
	public StateSpaceWindow(StateSpace<Node> space, GeneralSearch<Node> searchAlgorithm) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		sse = new StateSpaceEditor<>(space);
		sse.setPreferredSize(new Dimension(200, 0));
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
		});
		add(sse, BorderLayout.WEST);
		
		ssv = new StateSpaceView<>(space, searchAlgorithm);
		ssv.setSSVListener(new SSVListener<Node>() {

			@Override
			public void nodeSelected(Node node) {
				sse.select(node);
			}
			
		});
		add(ssv, BorderLayout.CENTER);
		
		setVisible(true);
	}
	
}
