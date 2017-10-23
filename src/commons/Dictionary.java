package commons;

import javafx.beans.property.SimpleStringProperty;

public class Dictionary {
	
	private final SimpleStringProperty rMaori;
	private final SimpleStringProperty rNumber;
	
	public Dictionary(String rNumber, String rMaori) {
		this.rMaori = new SimpleStringProperty(rMaori);
		this.rNumber = new SimpleStringProperty(rNumber);
	}
	
	public String getMaori(){
        return rMaori.get();
    }

    public void setMaori(String v){
        rMaori.set(v);
    }
    
    public String getNumber(){
        return rNumber.get();
    }

    public void setNumber(String v){
    	rNumber.set(v);
    }

}
