import java.util.ArrayList;
import java.util.List;

public class Sommet extends Point implements Comparable<Sommet>
{
    private String orientation;
    private List<Edge> voisins = new ArrayList<Edge>();
    private int minDistance = Integer.MAX_VALUE;
    private Sommet sommetPredecesseur;
    private Edge edgePredecesseur;
    
    public Sommet(String orientation, int ligne, int colonne)
    { 
    	super(ligne, colonne);
    	this.orientation = orientation;
    }
    
    public int compareTo(Sommet other)
    {
        return Double.compare(minDistance, other.minDistance);
    }

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public List<Edge> getVoisins() {
		return voisins;
	}

	public void setVoisins(List<Edge> voisins) {
		this.voisins = voisins;
	}

	public int getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}

	public Sommet getSommetPredecesseur() {
		return sommetPredecesseur;
	}

	public void setSommetPredecesseur(Sommet predecesseur) {
		this.sommetPredecesseur = predecesseur;
	}

	public Edge getEdgePredecesseur() {
		return edgePredecesseur;
	}

	public void setEdgePredecesseur(Edge edgePredecesseur) {
		this.edgePredecesseur = edgePredecesseur;
	}

}
