package jidm.gui;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.JButton;

public class DownloadInternalFrame extends JInternalFrame {
	private static final long serialVersionUID = 1692069055535282506L;
	private JTextField textField;
	private JProgressBar progressBar;
	private JLabel label;
	private JTextPane textPane;
	private JButton controlButton;
	private JButton stopButton;

	public DownloadInternalFrame(String name) {
		super(name, true, false, true, true);
		setBounds(100, 100, 450, 300);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		progressBar = new JProgressBar();
		springLayout.putConstraint(SpringLayout.NORTH, progressBar, 103,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, progressBar, 10,
				SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, progressBar, 410,
				SpringLayout.WEST, getContentPane());
		getContentPane().add(progressBar);

		label = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, label, 10,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, label, 0,
				SpringLayout.WEST, progressBar);
		getContentPane().add(label);

		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, textField, 10,
				SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, textField, -6,
				SpringLayout.NORTH, progressBar);
		springLayout.putConstraint(SpringLayout.EAST, textField, 0,
				SpringLayout.EAST, progressBar);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setEditable(false);

		textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textPane);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 6,
				SpringLayout.SOUTH, progressBar);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0,
				SpringLayout.WEST, progressBar);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 141,
				SpringLayout.SOUTH, progressBar);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0,
				SpringLayout.EAST, progressBar);
		getContentPane().add(scrollPane);

		controlButton = new JButton("Pause");
		springLayout.putConstraint(SpringLayout.WEST, controlButton, 0,
				SpringLayout.WEST, progressBar);
		springLayout.putConstraint(SpringLayout.SOUTH, controlButton, -3,
				SpringLayout.NORTH, textField);
		getContentPane().add(controlButton);

		stopButton = new JButton("Stop");
		springLayout.putConstraint(SpringLayout.NORTH, stopButton, 0,
				SpringLayout.NORTH, controlButton);
		springLayout.putConstraint(SpringLayout.WEST, stopButton, 6,
				SpringLayout.EAST, controlButton);
		getContentPane().add(stopButton);

	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JTextField getTextField() {
		return textField;
	}

	public JLabel getLabel() {
		return label;
	}

	public JTextPane getTextPane() {
		return textPane;
	}

	public JButton getControlButton() {
		return controlButton;
	}

	public JButton getStopButton() {
		return stopButton;
	}
}
