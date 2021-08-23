public class Adresse {
   private String strasse;
   private int hausnummer;
   private int turnummer;
   private String stadt;
   private String land;
   private int zip;
   
   
   public Adresse (String strasse, int hausnummer, int turnummer, String stadt, String land, int zip) {
	super();
	this.strasse = strasse;
	this.hausnummer = hausnummer;
	this.turnummer = turnummer;
	this.stadt = stadt;
	this.land = land;
	this.zip = zip;
	}

   public String getStrasse () {
			return this.strasse;}
   public void setStrasse (String neustrasse) {
			this.strasse = neustrasse;}

   public int getHausnummer () {
		return this.hausnummer;}
   public void setHausnummer (int neuhausnummer) {
		this.hausnummer = neuhausnummer;}
   
   public int getTurnummer () {
		return this.turnummer;}
   public void setTurnummer (int neuturnummer) {
		this.turnummer = neuturnummer;}

   public String getStadt () {
			return this.stadt;}
   public void setStadt (String neustadt) {
			this.stadt = neustadt;}

   public String getLand () {
			return this.land;}
   public void setLand (String neuland) {
			this.land = neuland;}

   public int getZip () {
			return this.zip;}
   public void setZip (int neuzip) {
			this.zip = neuzip;}



  @Override
  public String toString () {
	return "Adress [strasse=" + this.strasse + ", stadt=" + this.stadt + ", land=" + this.land + ", zip=" + this.zip
		+ "]";
  }
}
   