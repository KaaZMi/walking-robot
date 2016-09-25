import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Essais num�riques pour �valuer le temps de calcul d'algorithme en fonction du nombre d'obstacles.
 */
public class TestParObstacle {
	
	private static List<String> instancesParObstacle = new ArrayList<String>();
	private static List<String> resultatsParObstacle = new ArrayList<String>();
	private static long dureesParObstacle[] = new long[10];
	
	public static void main(String[] args)
	{
		long dureeMoyenneParObstacle10 = Long.MAX_VALUE,
			dureeMoyenneParObstacle20 = Long.MAX_VALUE,
			dureeMoyenneParObstacle30 = Long.MAX_VALUE,
			dureeMoyenneParObstacle40 = Long.MAX_VALUE,
			dureeMoyenneParObstacle50 = Long.MAX_VALUE;
		
		dureeMoyenneParObstacle10 = resoudreInstanceParObstacle(10);
		dureeMoyenneParObstacle20 = resoudreInstanceParObstacle(20);
		dureeMoyenneParObstacle30 = resoudreInstanceParObstacle(30);
		dureeMoyenneParObstacle40 = resoudreInstanceParObstacle(40);
		dureeMoyenneParObstacle50 = resoudreInstanceParObstacle(50);
		
		int abscisses[] = {10,20,30,40,50};
		long ordonnees[] = {
			dureeMoyenneParObstacle10,
			dureeMoyenneParObstacle20,
			dureeMoyenneParObstacle30,
			dureeMoyenneParObstacle40,
			dureeMoyenneParObstacle50
		};
		
		System.out.println("Temps moyens d'ex�cution :");
		System.out.println(" _________");
		for (int i = 0 ; i < 5 ; i++)
		{
			System.out.println("| " + abscisses[i] + " | " + ordonnees[i]);
		}
		
		writeInFile(instancesParObstacle, "./instancesTestParObstacle.txt", true);
		writeInFile(resultatsParObstacle, "./resultatsTestParObstacle.txt", false);
	}
	
	public static long resoudreInstanceParObstacle(int nbObstacles)
	{
		// G�n�ration de 10 instances al�atoires
		for (int n = 0 ; n < 10 ; n++)
		{
			int taille = 20; // taille fixe
			String dimensions = taille + " " + taille;
			
			// Cr�ation des obstacles
			List<Point> obstacles = new ArrayList<Point>();
			for (int index = 0 ; index < nbObstacles ; index++)
	        {
				int x = new Random().nextInt(taille);
				int y = new Random().nextInt(taille);
				if (obstacles.contains(new Point(x,y)))
					index--; // on veut des obstacles distincts
				else
					obstacles.add(new Point(x,y));	
	        }
			
			String matrice = "";
			int[][] cases = new int[taille][taille];
			for (int i = 0 ; i < taille ; i++)
	        {
	        	for (int j = 0 ; j < taille ; j++)
	        	{
	        		boolean isObstacle = false;
	    			for (Point o : obstacles)
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
			String coordonnees = "" + D1 + " " + D2 + " " + F1 + " " + F2 + " " + orientationD;
			
			String instance = "";
			instance = dimensions + "\n" + matrice + "\n" + coordonnees + "\n";
			
			instancesParObstacle.add(instance);
			System.out.println(instance);
			
			// Calcul du temps d'ex�cution de l'algorithme
			long startTime = System.nanoTime();
			
			Dijkstra dijkstra = new Dijkstra(taille, taille, cases, D1, D2, F1, F2, orientationD);
			resultatsParObstacle.add(dijkstra.resolution());
			
			long endTime = System.nanoTime();
			dureesParObstacle[n] = (endTime - startTime) / 1000000;
		}
		
		long sumDuration = 0;
		for (long d : dureesParObstacle)
			sumDuration += d;
		
		long averageDuration = sumDuration / dureesParObstacle.length;
		return averageDuration;
	}

	private static void writeInFile(List<String> elements, String path, boolean instance)
	{
        try
        {
        	String contenu = "";	
			for(int index = 0 ; index < elements.size() ; index++)
			{
	        	String element = elements.get(index);
	        	if (index == elements.size()-1)
	        		contenu += element;
	        	else
	        	{
	        		if(instance)
	        			contenu += element + "\n";
	        		else
	        			contenu += element + "\n\n";
	        	}
	        }
			
			if (instance)
				contenu += "0 0";

			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			bw.write(contenu);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
