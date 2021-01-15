package jidm.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 132854482051377988L;
	private final JPanel contentPanel = new JPanel();

	public AboutDialog() {
		setBounds(100, 100, 450, 300);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

		JEditorPane aboutEditoPane = new JEditorPane();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, aboutEditoPane, 0,
				SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, aboutEditoPane, 5,
				SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, aboutEditoPane, 0,
				SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, aboutEditoPane, -5,
				SpringLayout.EAST, contentPanel);
		aboutEditoPane.setEditable(false);
		aboutEditoPane.setBackground(Color.BLACK);
		aboutEditoPane.setForeground(Color.ORANGE);
		aboutEditoPane
				.setText("This program provide download manager in very simple way ...\nthe download pakage provide by Dr.Ghafarian and gui provide under homework.\nDedicated to MRMA\nspecial thancks to Mr.Alipour and Mr.Ahmad Panah");
		contentPanel.add(aboutEditoPane);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						AboutDialog.this.dispose();
					}
				});
			}
		}
	}
}
