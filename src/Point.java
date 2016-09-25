
public class Point
{ 
	private int ligne; 
	private int colonne; 
	  
	public Point(int ligne, int colonne)
	{ 
		this.ligne = ligne; 
		this.colonne = colonne; 
	}
	
    public String toString()
    { 
    	return "" + this.ligne + this.colonne;
    }
	
	public int getLigne() {
		return ligne;
	}
	
	public void setLigne(int ligne) {
		this.ligne = ligne;
	}
	
	public int getColonne() {
		return colonne;
	}
	
	public void setColonne(int colonne) {
		this.colonne = colonne;
	} 
}
