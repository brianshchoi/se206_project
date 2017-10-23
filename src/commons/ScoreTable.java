package commons;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ScoreTable {
    //Table property responsible for Ranking

    private final SimpleIntegerProperty rQuestion;
    private final SimpleIntegerProperty rNumber;
    private final SimpleStringProperty rCorrect;
    private final SimpleStringProperty rUserRecording;
    private final SimpleStringProperty rMaori;

    public ScoreTable(int rQuestion, int rNumber, String rCorrect, String rUserRecording, String rMaori){
        this.rQuestion = new SimpleIntegerProperty(rQuestion);
        this.rNumber = new SimpleIntegerProperty(rNumber);
        this.rCorrect = new SimpleStringProperty(rCorrect);
        this.rUserRecording = new SimpleStringProperty(rUserRecording);
        this.rMaori = new SimpleStringProperty(rMaori);
    }

    public Integer getQuestion(){
        return rQuestion.get();
    }

    public void setQuestion(Integer v){
        rQuestion.set(v);
    }

    public Integer getNumber(){
        return rNumber.get();
    }

    public void setNumber(Integer v){
        rNumber.set(v);
    }

    public String getCorrect(){
        return rCorrect.get();
    }

    public void setCorrect(String v){
        rCorrect.set(v);
    }

    public String getUserRecording(){
        return rUserRecording.get();
    }

    public void setUserRecording(String v){
        rUserRecording.set(v);
    }

    public String getMaori(){
        return rMaori.get();
    }

    public void setMaori(String v){
        rMaori.set(v);
    }

}
