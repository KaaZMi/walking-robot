
public class Edge
{
	private final Sommet cible;
	private final int poids;
	private String name;
    
    public Edge(Sommet cible, int poids, String name)
    {
    	this.cible = cible;
    	this.poids = poids;
    	this.name = name;
    }
    
    public String toString()
    { 
    	return this.name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sommet getCible() {
		return cible;
	}

	public int getPoids() {
		return poids;
	}
}
