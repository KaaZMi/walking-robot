import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Coeur de l'algorithme de Dijkstra utilis�.
 */
public class Dijkstra
{
	private List<Sommet> sommets = new ArrayList<Sommet>();
	private int[][] cases;
	private int D1,D2,F1,F2;
	private String orientationD;
	private int distanceMinimale = Integer.MAX_VALUE;
	
    public Dijkstra(int M, int N, int[][] cases, int D1, int D2, int F1, int F2, String orientationD) {
    	this.cases = cases;
    	this.F1 = F1;
    	this.F2 = F2;
    	this.D1 = D1;
    	this.D2 = D2;
    	this.orientationD = orientationD;
    	
    	init(M,N);
	}
    
    /**
     * Obtient le nombre minimum de secondes pour aller du point de d�part
     * au point de destination ainsi que la liste des commandes � effectuer par le robot.
     * 
     * @return ligne r�sultat 
     */
	public String resolution()
    {			
		// R�cup�ration du sommet de d�part
		Sommet depart = null;
		for (Sommet D : sommets)
		{
			if (D.getLigne() == D1 && D.getColonne() == D2 && D.getOrientation().equals(orientationD))
				depart = D;
		}
		
		// Application de Dijkstra
		calculerChemins(depart);
		Sommet arrivee = null;
		for (Sommet F : sommets)
		{
			if (F.getLigne() == F1 && F.getColonne() == F2)
			{
				if (F.getMinDistance() < distanceMinimale)
				{
					distanceMinimale = F.getMinDistance();
					arrivee = F; // meilleure arriv�e selon l'orientation finale
				}
			}
		}
        String chemin = plusCourtChemin(arrivee);
        
        // RESULTAT
        System.out.println(distanceMinimale + " " + chemin + "\n-------\n");
        if (distanceMinimale == Integer.MAX_VALUE)
        	return "-1";
        else
        	return distanceMinimale + " " + chemin;
        
    }
    
    /**
     * Cr�e une liste des sommets empruntables par le robot en ne gardant pas 
     * les sommets des bords de la grille (= murs de l'entep�t).
     * Chaque sommet peut avoir 4 orientations.
     */
    public void init(int M, int N)
    {
    	int i,j;
    	
    	/*
    	 * Cr�ation des sommets
    	 */
		for (i = 1 ; i < M ; i++) // on ne prend pas les bords nord et sud de la grille
		{
			for (j = 1 ; j < N ; j++) // on ne prend pas les bords ouest et est de la grille
			{
				// Filtrage des cases impraticables � cause des obstacles
				if (cases[i-1][j-1] == 0 & cases[i-1][j] == 0 & cases[i][j] == 0 & cases[i][j-1] == 0)
				{
					sommets.add(new Sommet("nord",i,j));
					sommets.add(new Sommet("sud",i,j));
					sommets.add(new Sommet("est",i,j));
					sommets.add(new Sommet("ouest",i,j));
				}
			}
		}
		
		/* 
		 * Ajout des arcs possibles entre les sommets en fonction de l'orientation
		 * et de la praticabilit� de ces derniers.
		 */
		for (Sommet source : sommets)
		{
			String orientation = source.getOrientation();
			
			switch (orientation)
			{
				case "nord":
					for (Sommet cible : sommets)
					{
						// si on continue vers le nord, on ne change ni d'orientation ni de colonne
				        if (cible.getOrientation().equals("nord") && cible.getColonne() == source.getColonne())
				        {
				        	if (cible.getLigne() == source.getLigne()-1)
				        		source.getVoisins().add(new Edge(cible, 1, "a1"));
				        	else if (cible.getLigne() == source.getLigne()-2)
				        		source.getVoisins().add(new Edge(cible, 1, "a2"));
				        	else if (cible.getLigne() == source.getLigne()-3)
				        	{
				        		// Un sommet 3 cases plus loin est peut-�tre praticable MAIS il faut
				        		// v�rifier que pour l'atteindre, on passe bien par des sommets praticables
				        		for (Sommet s : sommets)
				        		{
				        			if (s.getLigne() == cible.getLigne()+1 && s.getColonne() == cible.getColonne())
				        				source.getVoisins().add(new Edge(cible, 1, "a3"));
				        		}
				        	}
				        }
				        // si on tourne sur soi-m�me, on change uniquement d'orientation
				        if (cible.getColonne() == source.getColonne() && cible.getLigne() == source.getLigne())
				        {
				        	if (cible.getOrientation().equals("est"))
				        		source.getVoisins().add(new Edge(cible, 1, "D"));
				        	else if (cible.getOrientation().equals("ouest"))
				        		source.getVoisins().add(new Edge(cible, 1, "G"));
				        }
				    }
					break;
				case "sud":
					for (Sommet cible : sommets)
					{
						// si on continue vers le sud, on ne change ni d'orientation ni de colonne
				        if (cible.getOrientation().equals("sud") && cible.getColonne() == source.getColonne())
				        {
				        	if (cible.getLigne() == source.getLigne()+1)
				        		source.getVoisins().add(new Edge(cible, 1, "a1"));
				        	else if (cible.getLigne() == source.getLigne()+2)
				        		source.getVoisins().add(new Edge(cible, 1, "a2"));
				        	else if (cible.getLigne() == source.getLigne()+3)
				        	{
				        		for (Sommet s : sommets)
				        		{
				        			if (s.getLigne() == cible.getLigne()-1 && s.getColonne() == cible.getColonne())
				        				source.getVoisins().add(new Edge(cible, 1, "a3"));
				        		}
				        	}
				        }
				        if (cible.getColonne() == source.getColonne() && cible.getLigne() == source.getLigne())
				        {
				        	if (cible.getOrientation().equals("est"))
				        		source.getVoisins().add(new Edge(cible, 1, "G"));
				        	else if (cible.getOrientation().equals("ouest"))
				        		source.getVoisins().add(new Edge(cible, 1, "D"));
				        }
				    }
					break;
				case "est":
					for (Sommet cible : sommets)
					{
						// si on continue vers l'est, on ne change ni d'orientation ni de ligne
				        if (cible.getOrientation().equals("est") && cible.getLigne() == source.getLigne())
				        {
				        	if (cible.getColonne() == source.getColonne()+1)
				        		source.getVoisins().add(new Edge(cible, 1, "a1"));
				        	else if (cible.getColonne() == source.getColonne()+2)
				        		source.getVoisins().add(new Edge(cible, 1, "a2"));
				        	else if (cible.getColonne() == source.getColonne()+3)
				        	{
				        		for (Sommet s : sommets)
				        		{
				        			if (s.getLigne() == cible.getLigne() && s.getColonne() == cible.getColonne()-1)
				        				source.getVoisins().add(new Edge(cible, 1, "a3"));
				        		}
				        	}
				        }
				        if (cible.getColonne() == source.getColonne() && cible.getLigne() == source.getLigne())
				        {
				        	if (cible.getOrientation().equals("nord"))
				        		source.getVoisins().add(new Edge(cible, 1, "G"));
				        	else if (cible.getOrientation().equals("sud"))
				        		source.getVoisins().add(new Edge(cible, 1, "D"));
				        }
				    }
					break;
				case "ouest":
					for (Sommet cible : sommets)
					{
						// si on continue vers l'ouest, on ne change ni d'orientation ni de ligne
				        if (cible.getOrientation().equals("ouest") && cible.getLigne() == source.getLigne())
				        {
				        	if (cible.getColonne() == source.getColonne()-1)
				        		source.getVoisins().add(new Edge(cible, 1, "a1"));
				        	else if (cible.getColonne() == source.getColonne()-2)
				        		source.getVoisins().add(new Edge(cible, 1, "a2"));
				        	else if (cible.getColonne() == source.getColonne()-3)
				        	{
				        		for (Sommet s : sommets)
				        		{
				        			if (s.getLigne() == cible.getLigne() && s.getColonne() == cible.getColonne()+1)
				        				source.getVoisins().add(new Edge(cible, 1, "a3"));
				        		}
				        	}
				        }
				        if (cible.getColonne() == source.getColonne() && cible.getLigne() == source.getLigne())
				        {
				        	if (cible.getOrientation().equals("nord"))
				        		source.getVoisins().add(new Edge(cible, 1, "D"));
				        	else if (cible.getOrientation().equals("sud"))
				        		source.getVoisins().add(new Edge(cible, 1, "G"));
				        }
				    }
					break;
				default:
					System.out.println("Aucune raison d'atterir ici !");
			}
		}
	}
    
    /**
     * Calcul de la distance minimale pour aller d'un point de d�part � tous les points du graphe.
     * 
     * @param sommet de d�part
     */
	public void calculerChemins(Sommet source)
    {
        source.setMinDistance(0);
        PriorityQueue<Sommet> sommetQueue = new PriorityQueue<Sommet>(); // liste des sommets fix�s
        sommetQueue.add(source); // on commence l'algo par la s�lection du sommet de d�part

        while (!sommetQueue.isEmpty())
        {
        	Sommet u = sommetQueue.poll(); // r�cup�ration du sommet en t�te de liste

            // On visite chaque ar�te qui part de u
            for (Edge e : u.getVoisins())
            {
                Sommet v = e.getCible(); // sommet au bout de l'ar�te
                int poids = e.getPoids(); // poids de l'ar�te
                int distanceDepuisU = u.getMinDistance() + poids;
                if (distanceDepuisU < v.getMinDistance()) // si passer par cette ar�te est plus optimal
                {
		            sommetQueue.remove(u);
		            
		            // mise � jour de la distance minimale pour aller de u � v
		            v.setMinDistance(distanceDepuisU); 
		            
		            // m�morisation du sommet/ar�te par lequel on passe afin d'ult�rieurement r�cup�rer le chemin optimal
		            v.setSommetPredecesseur(u); 
		            v.setEdgePredecesseur(e); // l'ar�te dispose d'une information importante : le mouvement effectu� par le robot
		            
		            // ajout de v � notre liste des sommets fix�s
		            sommetQueue.add(v);
		        }
            }
        }
    }
	
	/**
	 * R�cup�ration du plus court chemin � l'aide des pr�d�cesseurs.
	 * 
	 * @param sommet cible
	 * @return chemin optimal (ordonnancement des mouvements du robot)
	 */
    public String plusCourtChemin(Sommet cible)
    {
        List<Sommet> cheminSommets = new ArrayList<Sommet>();
        List<String> mouvements = new ArrayList<String>();
        for (Sommet sommet = cible; sommet != null; sommet = sommet.getSommetPredecesseur())
        {
        	cheminSommets.add(sommet);
        	if (sommet.getEdgePredecesseur() != null)
        	{
        		mouvements.add(sommet.getEdgePredecesseur().toString());
        	}
        }
        
        // on retourne le r�sultat car on �tait parti du point d'arriv�e pour revenir au point de d�part
        Collections.reverse(cheminSommets); // serait utile si on souhaitait voir les sommets parcourus
        Collections.reverse(mouvements); // ce sont les mouvements effectu�s qui nous int�ressent
        
        // mise en forme du r�sultat
        String chemin = "";
        String m = "";
        for(int index = 0 ; index < mouvements.size() ; index++)
        {
        	m = mouvements.get(index);
        	if (index == 0)
        		chemin += m;
        	else
        		chemin += " " + m;
        }
        
        return chemin;
    }
}
