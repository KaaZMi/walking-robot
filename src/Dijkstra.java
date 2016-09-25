import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Coeur de l'algorithme de Dijkstra utilisé.
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
     * Obtient le nombre minimum de secondes pour aller du point de départ
     * au point de destination ainsi que la liste des commandes à effectuer par le robot.
     * 
     * @return ligne résultat 
     */
	public String resolution()
    {			
		// Récupération du sommet de départ
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
					arrivee = F; // meilleure arrivée selon l'orientation finale
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
     * Crée une liste des sommets empruntables par le robot en ne gardant pas 
     * les sommets des bords de la grille (= murs de l'entepôt).
     * Chaque sommet peut avoir 4 orientations.
     */
    public void init(int M, int N)
    {
    	int i,j;
    	
    	/*
    	 * Création des sommets
    	 */
		for (i = 1 ; i < M ; i++) // on ne prend pas les bords nord et sud de la grille
		{
			for (j = 1 ; j < N ; j++) // on ne prend pas les bords ouest et est de la grille
			{
				// Filtrage des cases impraticables à cause des obstacles
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
		 * et de la praticabilité de ces derniers.
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
				        		// Un sommet 3 cases plus loin est peut-être praticable MAIS il faut
				        		// vérifier que pour l'atteindre, on passe bien par des sommets praticables
				        		for (Sommet s : sommets)
				        		{
				        			if (s.getLigne() == cible.getLigne()+1 && s.getColonne() == cible.getColonne())
				        				source.getVoisins().add(new Edge(cible, 1, "a3"));
				        		}
				        	}
				        }
				        // si on tourne sur soi-même, on change uniquement d'orientation
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
     * Calcul de la distance minimale pour aller d'un point de départ à tous les points du graphe.
     * 
     * @param sommet de départ
     */
	public void calculerChemins(Sommet source)
    {
        source.setMinDistance(0);
        PriorityQueue<Sommet> sommetQueue = new PriorityQueue<Sommet>(); // liste des sommets fixés
        sommetQueue.add(source); // on commence l'algo par la sélection du sommet de départ

        while (!sommetQueue.isEmpty())
        {
        	Sommet u = sommetQueue.poll(); // récupération du sommet en tête de liste

            // On visite chaque arête qui part de u
            for (Edge e : u.getVoisins())
            {
                Sommet v = e.getCible(); // sommet au bout de l'arête
                int poids = e.getPoids(); // poids de l'arête
                int distanceDepuisU = u.getMinDistance() + poids;
                if (distanceDepuisU < v.getMinDistance()) // si passer par cette arête est plus optimal
                {
		            sommetQueue.remove(u);
		            
		            // mise à jour de la distance minimale pour aller de u à v
		            v.setMinDistance(distanceDepuisU); 
		            
		            // mémorisation du sommet/arête par lequel on passe afin d'ultérieurement récupérer le chemin optimal
		            v.setSommetPredecesseur(u); 
		            v.setEdgePredecesseur(e); // l'arête dispose d'une information importante : le mouvement effectué par le robot
		            
		            // ajout de v à notre liste des sommets fixés
		            sommetQueue.add(v);
		        }
            }
        }
    }
	
	/**
	 * Récupération du plus court chemin à l'aide des prédécesseurs.
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
        
        // on retourne le résultat car on était parti du point d'arrivée pour revenir au point de départ
        Collections.reverse(cheminSommets); // serait utile si on souhaitait voir les sommets parcourus
        Collections.reverse(mouvements); // ce sont les mouvements effectués qui nous intéressent
        
        // mise en forme du résultat
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
