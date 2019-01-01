package textsum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import Jama.Matrix;
import static java.lang.Math.max;
import java.util.HashMap;

public class PreProcessor {

    private String[] words;
    private String[] sentences;
    private static String[] stopWords;
    public int longest = 0;

    public String[] getWords() {
        return words;
    }

    public String[] getSentences() {
        return sentences;
    }

    public String[] getStopwords() {
        return stopWords;
    }

    //class constractor - initlize major used variables
    public PreProcessor(String text, String path_to_stop_words_file) {

        sentences = paragraph_to_sentences(text);
        stopWords = read_stop_words(path_to_stop_words_file);

        List<String> wordslist = new ArrayList<String>();

        for (String s : sentences) {
            String[] swords = sentence_to_words(s);
            longest = max(swords.length, longest);
            for (String w : remove_stop_words(swords)) {
                wordslist.add(stemTerm(w));
            }

        }

        Set<String> wordset = new LinkedHashSet<String>(Arrays.asList(wordslist.toArray(new String[wordslist.size()])));
        words = wordset.toArray(new String[wordset.size()]);

    }

    // Divide paragraph into sentences
    private String[] paragraph_to_sentences(String paragraph) {

        SentenceModel model;
        InputStream modelIn = null;
        String sentences[] = null;
        try {
            modelIn = new FileInputStream("src\\en-sent.bin");

            model = new SentenceModel(modelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            sentences = sentenceDetector.sentDetect(paragraph);
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (IOException e2) {
                }
            }
        }

        return sentences;
    }

    // Divides sentences to words
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

    // word stemming using Stemmer class
    public static String stemTerm(String term) {
        Stemmer stemmer = new Stemmer();
        for (char c : term.toCharArray()) {
            stemmer.add(c);
        }

        stemmer.stem();

        return stemmer.toString();
    }

    //Word stemming using lucene-snowball
    private String stemTerm2(String term) {
        PorterStemmer stemmer = new PorterStemmer();
        return stemmer.stem(term);
    }

    //Read stop words from a file
    private String[] read_stop_words(String path) {
        String[] stringArr = null;

        String str;

        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            List<String> list = new ArrayList<String>();
            while ((str = in.readLine()) != null) {
                list.add(str);
            }

            stringArr = list.toArray(new String[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringArr;

    }

    // takes two arrays. first array contains the words of a sentence
    // and the second one contains the stopwords
    // and then returns a new array that contains the non stop words of the sentence.
    public static String[] remove_stop_words(String[] words) {
        List<String> wlist = new ArrayList<String>(Arrays.asList(words));
        List<String> slist = new ArrayList<String>(Arrays.asList(stopWords));

        wlist.removeAll(slist);

        String[] result = wlist.toArray(new String[wlist.size()]);

        return result;

    }

    //matrix creation using binary representation
    public Matrix get_numiric_matrix() {
        double[][] result = new double[this.words.length][this.sentences.length];

        for (int i = 0; i < this.sentences.length; i++) {
            for (int j = 0; j < this.words.length; j++) {
                if (Arrays.asList(sentence_to_words(this.sentences[i])).contains(this.words[j])) {
                    result[j][i] = 1;
                } else {
                    result[j][i] = 0;
                }
            }
        }

        return new Matrix(result);

    }

    public Matrix get_freq_matrix() {
        double[][] result = new double[this.words.length][this.sentences.length];

        for (int i = 0; i < this.sentences.length; i++) {
            for (int j = 0; j < this.words.length; j++) {

                List asList = Arrays.asList(sentence_to_words(this.sentences[i]));
                //Set<String> set = new HashSet<String>(asList);
                result[j][i] = Collections.frequency(asList, this.words[j]);

                //System.out.print(" count "+words[j]+" in "+asList.toString()+" is "+result[j][i]);
                //System.out.println();
                //System.out.println();
            }
        }

        return new Matrix(result);

    }
    
    public Matrix getTfMatrix() {
        double[][] result = new double[this.words.length][this.sentences.length];
        HashMap<Integer, Double> wordFreq = new HashMap();
        for (int i = 0; i < this.sentences.length; i++) {
            double t = 0;
            String[] senWord = sentence_to_words(this.sentences[i]);
            for (int j = 0; j < this.words.length; j++) {
                int occurrences = Collections.frequency(Arrays.asList(senWord), this.words[j]);
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
        }

        return new Matrix(result);
    }

}
