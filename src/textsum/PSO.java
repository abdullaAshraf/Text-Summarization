/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textsum;

import Jama.Matrix;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.min;
import java.util.*;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import static textsum.Main.df;
import static textsum.PreProcessor.remove_stop_words;
import static textsum.PreProcessor.stemTerm;

/**
 *
 * @author abdul
 */
public class PSO {

    private String[] words;
    private String[] sentences;
    private int[] SwithT;
    private int[] top10;

    public String[] getWords() {
        return words;
    }

    public String[] getSentences() {
        return sentences;
    }

    public int[] getTop10() {
        return top10;
    }

    public PSO(String[] word, String[] sen) {
        words = word;
        sentences = sen;
        SwithT = new int[word.length];

    }

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

    //get term frequency matrix
    public Matrix getTfMatrix() {
        double[][] result = new double[this.words.length][this.sentences.length];
        HashMap<Integer, Double> wordFreq = new HashMap();
        double[] total = new double[this.sentences.length];
        for (int i = 0; i < this.sentences.length; i++) {
            double t = 0;
            String[] senWord = sentence_to_words(this.sentences[i]);
            for (int j = 0; j < this.words.length; j++) {
                int occurrences = Collections.frequency(Arrays.asList(senWord), this.words[j]);
                if (occurrences > 0) {
                    SwithT[j]++;
                }
                if (wordFreq.containsKey(j)) {
                    double temp = wordFreq.get(j);
                    wordFreq.remove(j);
                    wordFreq.put(j, (double) (occurrences) + temp);
                } else {
                    wordFreq.put(j, (double) (occurrences));
                }
                result[j][i] = occurrences;
                t += occurrences*occurrences;
            }
            total[i] = Math.sqrt(t);
        }

        //normalize tf matrix
        for (int i = 0; i < this.words.length; i++) {
            for (int j = 0; j < this.sentences.length; j++) {
                result[i][j] = result[i][j] / total[j];
            }
        }

        //top 10 words
        top10 = new int[this.words.length];
        int k = 0;
        sortHashMapByValues(wordFreq);
        for (Integer key : wordFreq.keySet()) {
            top10[k] = key;
            k++;
        }
        return new Matrix(result);
    }

    //weights matrix(tf*idf)
    public Matrix getweightMatrix(Matrix m) {
        double[][] result = new double[this.words.length][this.sentences.length];
        int total = 0;
        for (int i = 0; i < this.words.length; i++) {
            for (int j = 0; j < this.sentences.length; j++) {
                //IDF(t) = log_e(Total number of documents / Number of documents with term t in it).
                //w(t, d) = tft,d × idft
                if (SwithT[i] == 0) {
                    result[i][j] = 0;
                } else {
                    result[i][j] = m.get(i, j) * Math.log(sentences.length / SwithT[i]);
                }
            }
        }

        return new Matrix(result);
    }

    //check if required
    public boolean contains2(int[] array, int v) {
        for (int e : array) {
            if (e == v) {
                return true;
            }
        }
        return false;
    }

    //get highst scores
    public int[] getTopScore(int[] qu, Matrix m) {
        int[] x = new int[sentences.length];
        HashMap<Integer, Double> senScore = new HashMap();
        for (int i = 0; i < m.getColumnDimension(); i++) {
            for (int j = 0; j < m.getRowDimension(); j++) {
                if (contains2(qu, j)) {
                    if (senScore.containsKey(i)) {
                        double temp = senScore.get(i);
                        senScore.remove(i);
                        senScore.put(i, temp + m.get(j, i));
                    } else {
                        senScore.put(i, m.get(j, i));
                    }
                    //System.out.println(senScore.size());
                }
            }
        }
        int i = 0;
        senScore = sortHashMapByValues(senScore);
        for (Integer key : senScore.keySet()) {
            x[i] = key;
            i++;
        }
        return x;
    }

    //get two vectors similrty
    public double similarity(double[] v1,double[] v2){
        double ans=0;
        for (int i=0; i<min(v1.length,v2.length); i++){
            ans += v1[i]*v2[i];
        }
        return ans;
    }
    
    //normalize vector
    public double[] normalize (double[] vec){
        double t=0;
        for (int i=0; i<vec.length; i++)
            t+= vec[i]*vec[i];
        t = Math.sqrt(t);
        for (int i=0; i<vec.length; i++)
            vec[i] = vec[i]/t;
        return vec;
    }
    
    public static void writeMatrix(Matrix m) {
        System.out.println("\n");
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                System.out.println("   " + df.format(m.get(i, j)));
            }
            System.out.println("\n");

        }

    }

    //sort map by values
    public LinkedHashMap<Integer, Double> sortHashMapByValues(
            HashMap<Integer, Double> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Double> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, Double> sortedMap
                = new LinkedHashMap<>();

        Iterator<Double> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Double val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Integer key = keyIt.next();
                Double comp1 = passedMap.get(key);
                Double comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
