/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textsum;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import static textsum.PreProcessor.remove_stop_words;
import static textsum.PreProcessor.stemTerm;

/**
 *
 * @author abdul
 */
public class features {

    String title;
    int longestWord;
    int countTotal;
    int MaxTw;

    public features(String Title, int Longest, int Total) {
        title = Title;
        longestWord = Longest;
        countTotal = Total;
    }

    //measure sentence relative to Title score
    public double TitleFeature(String sentence) {
        double tf;
        String[] titleWord = sentence_to_words(title);
        String[] senWord = sentence_to_words(sentence);
        int intersection = 0;
        for (String titleWord1 : titleWord) {
            for (String senWord1 : senWord) {
                if (titleWord1.equals(senWord1)) {
                    intersection++;
                    break;
                }
            }
        }
        tf = (double) (intersection) / titleWord.length;
        return tf;
    }

    //measure sentence length score
    public double SentenceLength(String sentence) {
        double sl;
        String[] senWord = sentence_to_words(sentence);
        sl = (double) (senWord.length) / longestWord;
        return sl;
    }

    //measure sentence position score
    public double SentencePosition(int senIndex) {
        double sp;
        sp = (double) (senIndex) / countTotal;
        return sp;
    }

    //measure sentence numeric data score
    public double NumericalData(String sentence) {
        double nd;
        int all = 0;
        String[] words = sentence.split(" ", -1);
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length(); j++) {
                if (sentence.charAt(j) >= '0' && sentence.charAt(j) <= '9') {
                    all++;
                    break;
                }
            }
        }
        nd = (double) (all) / words.length;
        return nd;
        
    }

    //measure sentence most frequent words score
    public double ThematicWord(String sentence, String[] themticWords) {
        double tw;
        String[] senWord = sentence_to_words(sentence);
        int intersection = 0;
        for (String themticWords1 : themticWords) {
            for (String senWord1 : senWord) {
                if (themticWords1.equals(senWord1)) {
                    intersection++;
                    break;
                }
            }
        }
        tw = (double) (intersection);
        return tw;
    }

    //get words out of sentence
    public static String[] sentence_to_words(String sentence) {
        // remove all non letters characters
        sentence = sentence.replaceAll("[^a-zA-Z ]", " ").toLowerCase();

        String[] words = null;
        InputStream modelIn = null;
        TokenizerModel model;
        Tokenizer tokenizer;

        try {
            modelIn = new FileInputStream("src\\en-token.bin");
            model = new TokenizerModel(modelIn);
            tokenizer = new TokenizerME(model);
            words = remove_stop_words(tokenizer.tokenize(sentence));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (IOException e) {
                }
            }
        }

        String[] result = new String[words.length];

        for (int i = 0; i < words.length; i++) {
            result[i] = stemTerm(words[i]);
        }

        return result;

    }
}
