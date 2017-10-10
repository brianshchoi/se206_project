package commons;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class userTable {
	
	private final SimpleIntegerProperty rTotal;
    private final SimpleStringProperty rNickname;
    private final SimpleStringProperty rDate;
    
    public userTable(int rTotal, String rNickname, String rDate){
        this.rTotal = new SimpleIntegerProperty(rTotal);
        this.rNickname = new SimpleStringProperty(rNickname);
        this.rDate = new SimpleStringProperty(rDate);
    }
    
    public Integer getTotal(){
        return rTotal.get();
    }

    public void setTotal (Integer v){
    	rTotal.set(v);
    }

    public String getNickname(){
        return rNickname.get();
    }

    public void setNickname(String v){
    	rNickname.set(v);
    }

    public String getDate(){
        return rDate.get();
    }

    public void setDate(String v){
    	rDate.set(v);
    }

}
