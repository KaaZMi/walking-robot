import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IHM extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel container = new JPanel();
	private JFormattedTextField grilleTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
	private JFormattedTextField obstaclesTF = new JFormattedTextField(NumberFormat.getIntegerInstance());
	private JLabel labelGrille = new JLabel("Taille de la grille : ");
	private JLabel labelObstacles = new JLabel("Nombre d'obstacles : ");
	
	/**
	 * Mise en forme de l'interface graphique.
	 */
	public IHM()
	{
		this.setTitle("La balade du robot");
		this.setSize(500, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		container.setBackground(Color.white);
		container.setLayout(new BorderLayout());
		
		JPanel panGrille = new JPanel();
	    panGrille.setBorder(BorderFactory.createTitledBorder("Grille"));
		grilleTF.setPreferredSize(new Dimension(150, 30));
		grilleTF.setForeground(Color.BLUE);
		panGrille.add(labelGrille);
		panGrille.add(grilleTF);
	    
	    JPanel panObstacles = new JPanel();
	    panObstacles.setBorder(BorderFactory.createTitledBorder("Obstacles"));
	    obstaclesTF.setPreferredSize(new Dimension(150, 30));
	    obstaclesTF.setForeground(Color.BLUE);
	    panObstacles.add(labelObstacles);
	    panObstacles.add(obstaclesTF);
	    
	    JPanel content = new JPanel();
	    content.add(panGrille);
	    content.add(panObstacles);

	    JPanel control = new JPanel();
	    JButton okBouton = new JButton("Valider");
	    okBouton.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent arg0)
	    	{        
	        	int taille = Integer.parseInt(grilleTF.getText());
				int nbObstacles = Integer.parseInt(obstaclesTF.getText());
				String solution = genererSolution(taille,nbObstacles);
				System.out.println(solution);
				new SolutionDialog(null, "Solution", true, solution);
	    	}     
	    });
	    
	    JButton cancelBouton = new JButton("Annuler");
	    cancelBouton.addActionListener(new ActionListener()
	    {
	    	public void actionPerformed(ActionEvent arg0)
	      	{
	    		//dispose();
	    		System.exit(0);
	      	}      
	    });

	    control.add(okBouton);
	    control.add(cancelBouton);
	    
	    this.getContentPane().add(content, BorderLayout.CENTER);
	    this.getContentPane().add(control, BorderLayout.SOUTH);
	    
	    this.setVisible(true);            
	}
	
	public static void main(String[] args){       
		new IHM();
	}
	
	/**
	 * Crée une grille aléatoire et lui applique l'algorithme de Dijkstra.
	 * 
	 * @param taille donnée par l'utilisateur
	 * @param nombre d'obstacles donné par l'utilisateur
	 * 
	 * @return solution de la grille
	 */
	public static String genererSolution(int taille, int nbObstacles)
	{
		List<Point> Obstacles = new ArrayList<Point>();
		for (int index = 0 ; index < nbObstacles ; index++)
        {
			int x = new Random().nextInt(taille);
			int y = new Random().nextInt(taille);
			if (Obstacles.contains(new Point(x,y)))
				index--; // on veut des Obstacles distincts
			else
				Obstacles.add(new Point(x,y));	
        }
		
		String matrice = "";
		int[][] cases = new int[taille][taille];
		for (int i = 0 ; i < taille ; i++)
        {
        	for (int j = 0 ; j < taille ; j++)
        	{
        		boolean isObstacle = false;
    			for (Point o : Obstacles)
    			{
    				if (o.getLigne() == i && o.getColonne() == j)
    					isObstacle = true;
    			}
    			if (isObstacle && j == 0)
    			{
    				matrice += "1";
    				cases[i][j] = 1;
    			}
        		else if (isObstacle && j != 0)
        		{
        			matrice += " 1";
        			cases[i][j] = 1;
        		}
        		else if (!isObstacle && j == 0)
        		{
        			matrice += "0";
        			cases[i][j] = 0;
        		}
        		else if (!isObstacle && j != 0)
        		{
        			matrice += " 0";
        			cases[i][j] = 0;
        		}
        	}
        	
        	if (i != taille-1)
        		matrice += "\n";
        	
        }
		
		int D1,D2,F1,F2;
		String orientationD = "";
		List<Point> points = new ArrayList<Point>();
		for (int i = 1 ; i < taille ; i++)
		{
			for (int j = 1 ; j < taille ; j++)
			{
				// Filtrage des cases impraticables
				if (cases[i-1][j-1] == 0 & cases[i-1][j] == 0 & cases[i][j] == 0 & cases[i][j-1] == 0)
				{
					points.add(new Point(i,j));
					points.add(new Point(i,j));
					points.add(new Point(i,j));
					points.add(new Point(i,j));
				}
			}
		}
		
		Point D = points.get(new Random().nextInt(taille*taille-taille));
		Point F = points.get(new Random().nextInt(taille*taille-taille));
		D1 = D.getLigne();
		D2 = D.getColonne();
		F1 = F.getLigne();
		F2 = F.getColonne();
		
		int randomOrientation = new Random().nextInt(4 - 1) + 1;
		switch (randomOrientation)
		{
			case 1:
				orientationD = "nord";
			case 2:
				orientationD = "sud";
			case 3:
				orientationD = "est";
			case 4:
				orientationD = "ouest";
		}
		
		Dijkstra dijkstra = new Dijkstra(taille, taille, cases, D1, D2, F1, F2, orientationD);
		
		return matrice + "\n" + dijkstra.resolution();
	}
}
