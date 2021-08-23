import java.util.Calendar;
import java.util.*;

public class Kunden {
   
	private int id;
    
    private String vorname;
    
    private String nachname;
    
    private Adresse adresse;
    
    private String geschlecht ;
    
    private Date geburtsdatum ;
    
      
    public Kunden (String vorname, int id, Adresse adresse, String geschlecht, 
    	    int jahre, int monat, int tag) {
    	
        this.vorname  = vorname;
        this.id = id;
        this.geschlecht = geschlecht;
        initGeburtsdatum(jahre, monat, tag);
    }
    private void initGeburtsdatum(int jahre, int monat, int tag) {
        if (jahre > 0 && monat > 0 && tag > 0) {
            Calendar cal = Calendar.getInstance();
            cal.set(jahre, monat-1 , tag, 0, 0, 0);
            setGeburtsdatum(cal.getTime());
        }
    }
    
    public int getId () {
	return this.id;}
    public void setId (int neuid) {
	this.id = neuid;}
    
    public String getVorname () {
		return this.vorname;}
    public void setVorname (String neuvorname) {
		this.vorname = neuvorname;}

    public String getNachname () {
		return this.nachname;}
    public void setNachname (String neunachname) {
		this.nachname = neunachname;}

    public Adresse getAdresse () {
		return this.adresse;}
    public void setAdresse (Adresse neuadresse) {
		this.adresse = neuadresse;}

    public String getGeschlecht () {
		return this.geschlecht;}
    public void setGeschlecht (String neugeschlecht) {
		this.geschlecht = neugeschlecht;}
    
	public Date getGeburtsdatum() {
		return geburtsdatum;
	}
	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

   
}