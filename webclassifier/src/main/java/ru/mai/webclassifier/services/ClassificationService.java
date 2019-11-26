package ru.mai.webclassifier.services;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import ru.mai.webclassifier.database.DatabaseDriver;
import ru.mai.webclassifier.database.models.*;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class ClassificationService {
    private DatabaseDriver database;
    private Classifier classifier;
    private ArrayList<String> discardedWords;
    private ArrayList<String> bagWords;
    
    public ClassificationService() throws Exception {
        database = new DatabaseDriver();
        
        discardedWords = database.getDiscardedWords();
        ArrayList<BagWord> allBagWords = database.getBagWords();
        Collections.sort(allBagWords, (x,y)-> Integer.compare(x.getPositionIndex(), y.getPositionIndex()));
        bagWords = new ArrayList<>();
        for (BagWord bagWord: allBagWords) {
            bagWords.add(bagWord.getWord());
        }
        
        classifier = (Classifier) SerializationHelper.read(new FileInputStream("TextClassifier.model"));
    }
    
    public TextClass predict(String text) throws Exception {
        Instances vectorizedText = vectorizeText(text);
        return TextClass.values()[(int)classifier.classifyInstance(vectorizedText.firstInstance())];
    }
    
    private String normalizeWord(String word) {
        String normalizedWord = word.toLowerCase();
        normalizedWord = normalizedWord.replaceAll("[.,\\/#!\\?$%\\^&\\*;:{}=_`~()]", "");
        normalizedWord = normalizedWord.trim();
        return normalizedWord;
    }
    
    private ArrayList<String> getWordsFromText(String text) {
        ArrayList<String> goodWords = new ArrayList<>();
        
        String[] allWords = text.split("\\s+");
        for (int i = 0; i < allWords.length; ++i) {
            String checkedWord = normalizeWord(allWords[i]);
            if (checkedWord != "") {
                if (!discardedWords.contains(checkedWord)) {
                    goodWords.add(checkedWord);
                }
            }
        }
        
        return goodWords;
    }
    
    private double[] createVector(ArrayList<String> words) {
        double[] vector = new double[bagWords.size() + 1];
        for (int i = 0; i < vector.length; ++i) {
            vector[i] = 0;
        }
        
        for (int i = 0; i < words.size(); ++i) {
            Integer attributeIndex = bagWords.indexOf(words.get(i));
            if (attributeIndex != -1) {
                vector[attributeIndex] = vector[attributeIndex] + 1;
            }
        }
        
        return vector;
    }
    
    private Instances vectorizeText(String text) {
        ArrayList<String> words = getWordsFromText(text);
        
        double[] vector = createVector(words);
        
        Instances vectorizedText;
        
        ArrayList<String> textClasses = new ArrayList<>();
        for (int i = 0; i < TextClass.values().length; ++i) {
            textClasses.add(TextClass.values()[i].toString().toLowerCase());
        }
        Attribute classAttribute = new Attribute("Class", textClasses);
        ArrayList<Attribute> wordAttributes = new ArrayList<>();
        for (int i = 0; i < bagWords.size(); ++i) {
            wordAttributes.add(new Attribute("Attribute " + (i + 1)));
        }
        wordAttributes.add(classAttribute);
        
        vectorizedText = new Instances("Text", wordAttributes, 0);
        vectorizedText.setClass(classAttribute);
        vectorizedText.add(new DenseInstance(1.0, vector));
        vectorizedText.instance(0).setClassMissing();
        
        return vectorizedText;
    }
}
