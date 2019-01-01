package textsum;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SelectRank extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static int r=0;
	public JComboBox cboxrank;
	public JButton okButton;

	/**
	 * Create the dialog.
	 */
	public SelectRank(int r) {
		this.r=r;
		setTitle("Select Rank");
		setBounds(100, 100, 225, 106);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblSelectRank = new JLabel("select rank");
			lblSelectRank.setBounds(10, 8, 52, 14);
			contentPanel.add(lblSelectRank);
		}
		{
			cboxrank = new JComboBox();
			cboxrank.setBounds(90, 5, 118, 20);
			contentPanel.add(cboxrank);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				 okButton = new JButton("OK");
				
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		for (int i=1; i<=r; i++)
			cboxrank.addItem(i);
	}
		

}
