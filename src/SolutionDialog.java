import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SolutionDialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	public SolutionDialog(JFrame parent, String title, boolean modal, String solution)
	{
	    super(parent, title, modal); // appel du construteur JDialog
	    this.setSize(1000, 1000);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	    
	    JPanel panSolution = new JPanel();
	    panSolution.setPreferredSize(new Dimension(220, 60));
	    JTextArea solutionText = new JTextArea(solution);
	    solutionText.setEditable(false);
	    panSolution.add(solutionText);
	    
	    JPanel control = new JPanel();
	    JButton okBouton = new JButton("OK");
	    okBouton.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent arg0)
	    	{        
	    		dispose();
	    	}     
	    });
	    control.add(okBouton);
	    
	    this.getContentPane().add(panSolution, BorderLayout.CENTER);
	    this.getContentPane().add(control, BorderLayout.SOUTH);
	    
	    this.setVisible(true);
	}
}