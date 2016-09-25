import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Donne un fichier de r�sultats � partir d'un fichier d'entr�e.
 * Permet notamment de v�rifier le fonctionnement de l'application par rapport � l'exemple donn� dans l'�nonc�.
 */
public class Robot {
	
	/**
	 * D�roulement du processus du robot:
	 * - Il lit un fichier d'entr�e.
	 * - Il fait appel � l'algorithme de Dijskstra sur chaque bloc d'informations du fichier d'entr�e.
	 * - Il �crit le r�sultat du ou des algorithme(s) dans le fichier de sortie.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
    {
		String[] blocs = readblocs();
		
        int[][] cases; // matrice des cases de l'entrep�t
    	int M; // nombre de lignes
    	int N; // nombre de colonnes
    	int D1; // ligne des coordonn�es du point de d�part
    	int D2; // colonne des coordonn�es du point de d�part
    	int F1; // ligne des coordonn�es du point d'arriv�e
    	int F2; // colonne des coordonn�es du point d'arriv�e
    	String orientationD; // orientation au point de d�part
    	List<String> resultats = new ArrayList<String>();
    	
        for (String bloc : blocs)
        {
            bloc = bloc.trim();
            String[] lines = bloc.split("\n");
            
            // R�cup�ration des dimensions de la matrice
            String dimensions = lines[0];
            String[] dimParts = dimensions.split(" ");
			M = Integer.parseInt(dimParts[0]); 
			N = Integer.parseInt(dimParts[1]);
			System.out.println("\nM lignes:" + M + ", N colonnes:" + N);
			
			// R�cup�ration de la matrice
			cases = new int[M][N];
            for (int i = 1 ; i < M+1 ; i++)
            {
            	String[] data = lines[i].split(" ");
            	for (int j = 0 ; j < N ; j++)
			        cases[i-1][j] = Integer.parseInt(data[j]);
            }
            
            System.out.println("Matrice: ");
            System.out.println(Arrays.deepToString(cases));
            
            // R�cup�ration des coordonn�es de d�part et d'arriv�e
            String[] coordonnees = lines[M+1].split(" ");
            
            D1 = Integer.parseInt(coordonnees[0]);
			D2 = Integer.parseInt(coordonnees[1]);
			F1 = Integer.parseInt(coordonnees[2]);
			F2 = Integer.parseInt(coordonnees[3]);
			orientationD = coordonnees[4];
			
			System.out.println("Coordonn�es:");
			System.out.println(D1 + " " + D2 + " " + F1 + " " + F2 + " " + orientationD);
			
			Dijkstra dijkstra = new Dijkstra(M, N, cases, D1, D2, F1, F2, orientationD);
			resultats.add(dijkstra.resolution());
        }
        
        writeResults(resultats);
        
    }
	
	/**
	 * Lit le fichier d'entr�e.
	 * 
	 * @return ensemble des blocs qui repr�sentent chacun une instance d'un probl�me.
	 */
	private static String[] readblocs() {
		String[] blocs = null;
        try
        {
        	BufferedReader br = new BufferedReader(new FileReader("./in.txt"));
	        StringBuffer sb = new StringBuffer();
	        while (true)
	        {
	            String line = br.readLine();
	            if (line.equals("0 0"))
	            	break; // d�tection de la fin du fichier
	            sb.append(line).append("\n");
	        }
	        
	        blocs = sb.toString().split("\n\n"); // s�paration des blocs
	        br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return blocs;
	}
	
	/**
	 * Ecrit le fichier de r�sultats dans le fichier de sortie.
	 * 
	 * @param resultats
	 */
	private static void writeResults(List<String> resultats)
	{
        try
        {
        	String contenu = "";	
			for(int index = 0 ; index < resultats.size() ; index++)
			{
	        	String resultat = resultats.get(index);
	        	if (index == resultats.size()-1)
	        		contenu += resultat;
	        	else
	        		contenu += resultat + "\n\n";
	        }
			System.out.println("FICHIER DE RESULTATS :\n" + contenu);

			File file = new File("./out.txt");
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
