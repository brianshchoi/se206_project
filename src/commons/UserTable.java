package commons;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserTable {
	//Table property responsible for Ranking

	private final SimpleIntegerProperty rScore;
    private final SimpleStringProperty rNickname;
    private final SimpleStringProperty rDate;
    
    public UserTable(int rTotal, String rNickname, String rDate){
        this.rScore = new SimpleIntegerProperty(rTotal);
        this.rNickname = new SimpleStringProperty(rNickname);
        this.rDate = new SimpleStringProperty(rDate);
    }
    
    public Integer getScore(){
        return rScore.get();
    }

    public void setScore(Integer v){
    	rScore.set(v);
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
