package ru.mai.webclassifier.database.models;

public class BagWord {
    private String word;
    private int positionIndex;
    
    public BagWord(String word, int positionIndex) {
        this.word = word;
        this.positionIndex = positionIndex;
    }
    
    public String getWord() {
        return word;
    }
    
    public void setWord(String word) {
        this.word = word;
    }
    
    public int getPositionIndex() {
        return positionIndex;
    }
    
    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }
}
