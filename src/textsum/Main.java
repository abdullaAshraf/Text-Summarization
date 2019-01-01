package textsum;
//import java.awt.List;

import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.awt.event.ActionEvent;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import javax.swing.JScrollPane;
import java.util.Iterator;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import static java.lang.Math.max;
import java.util.Arrays;
import javax.swing.JLabel;
import org.jfree.ui.RefineryUtilities;

public class Main {

    public JFrame frame;
    public static SelectRank sr;
    public static SelectQuery sq;
    public static JTextArea textArea;
    public static DecimalFormat df;
    public static Matrix A, U, V, Vt, X, Y, S, Sr, Ur, Vr, Vtr, Q, SrVtr;
    public String[] sentences, words;
    public static Matrix[] Sentenices_indecies, words_indeceis;
    public double[] distances;
    public int[] selectedqu, all;
    public String title;
    int m, n, rank, longest;
    public static ArrayList<String> topSen = new ArrayList<>(0);
    public HashMap<String, Double> senRepeat = new HashMap();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    gui g = new gui();
                    g.frame.setVisible(true);
                    //Main window = new Main();
                    //window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        df = new DecimalFormat("#.0000");

        frame = new JFrame();
        frame.setBounds(100, 100, 510, 374);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);

        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(true);

        final JButton btnNewButton = new JButton("Summerize");
        frame.getContentPane().add(btnNewButton, BorderLayout.SOUTH);

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // Summerize botton event handler starts here

                String lines[] = textArea.getText().split("\\r?\\n");
                title = lines[0];
                String text = "";
                for (int i = 1; i < lines.length; i++) {
                    text += lines[i] + "\n";
                }

                // for simplicity can test with the below text
                // The man walked the dog. The man took the dog to the park. The dog went to the park. 
                // instantiating pre processor class and giving it the stopwords file 
                PreProcessor pp = new PreProcessor(text, "src\\stopwords_en.txt");
                //paragraph sentences
                sentences = pp.getSentences();
                // paragraph words
                words = pp.getWords();

                //List<String> wl=Arrays.asList(pp.getWords());
                m = words.length;
                n = sentences.length;
                longest = pp.longest;

                // printing all sentences
                for (int i = 0; i < sentences.length; i++) {
                    writeoutput("d" + i + " : " + sentences[i]);

                    // unhash the below lines to print the words of each sentence  
                    //for ( String w :PreProcessor.sentence_to_words(s))
                    //writeoutput(w+":"+PreProcessor.stemTerm(w));
                    //writeoutput();
                }

                // printing all the words of the paragraph ( after stemming and removing stop words )
                for (String w : words) {
                    writeoutput(w);
                }

                // creating the matrix using the get numeric method  
                //Matrix A= pp.get_numiric_matrix();
                A = pp.get_freq_matrix();

                // printing the matrix that represents the paragraph ( rows as words and sentences as columns , values of cells are binary values represents
                // the existance  of a specific word in a specific sentence 
                //A.print(5,0);
                writeMatrix(A);
                writeoutput();
                writeoutput("######################################");
                writeoutput("######################################");
                writeoutput();

                // applying the SVD
                SingularValueDecomposition svd = A.svd();

                // U matrix
                U = svd.getU();
                writeoutput("U = ");
                //U.print(9, 6);
                writeMatrix(U);
                writeoutput();
                writeoutput("######################################");
                writeoutput("######################################");
                writeoutput();

                // printing Sigma matrix	
                writeoutput("Sigma = ");
                S = svd.getS();
                //S.print(9, 6);

                writeMatrix(S);
                writeoutput();
                writeoutput("######################################");
                writeoutput("######################################");
                writeoutput();

                // printing V matrix	
                writeoutput("V = ");
                V = svd.getV();
                writeMatrix(V);
                //V.print(9, 6);

                writeoutput();
                writeoutput("######################################");
                writeoutput("######################################");
                writeoutput();

                // printing V matrix	
                writeoutput("Vt = ");
                Vt = svd.getV().transpose();
                //  Vt.print(9, 6);
                writeMatrix(Vt);
                writeoutput("######################################");
                writeoutput("######################################");

                // printing U * Sigma matrix	
                writeoutput("U * S");
                X = U.times(S);
                //X.print(9, 6);
                writeMatrix(X);

                writeoutput("######################################");
                writeoutput("######################################");

                // printing Sigma * Vt matrix	
                writeoutput("S * Vt");
                Y = S.times(V.transpose());
                //Y.print(9, 6);
                writeMatrix(Y);

                /// sentence selection
                /// Gong and Liu (2001)
                sr = new SelectRank(n);
                sr.setVisible(true);

                sr.okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {

                        SelectRank.r = (int) sr.cboxrank.getSelectedItem();
                        rank = (int) sr.cboxrank.getSelectedItem();
                        sr.setVisible(false);

                        Main.writeoutput(Integer.toString(SelectRank.r));

                        textArea.append("\n" + Integer.toString(SelectRank.r));

                        Sr = S.getMatrix(0, SelectRank.r - 1, 0, SelectRank.r - 1);
                        Ur = U.getMatrix(0, U.getRowDimension() - 1, 0, SelectRank.r - 1);
                        Vr = V.getMatrix(0, SelectRank.r - 1, 0, SelectRank.r - 1);
                        Vtr = Vt.getMatrix(0, SelectRank.r - 1, 0, Vt.getColumnDimension() - 1);

                        textArea.append("\n" + "S" + SelectRank.r + "=\n");
                        Main.writeMatrix(Sr);

                        textArea.append("\n" + "U" + SelectRank.r + "=\n");
                        Main.writeMatrix(Ur);

                        textArea.append("\n" + "V" + SelectRank.r + "=\n");
                        Main.writeMatrix(Vr);

                        textArea.append("\n" + "Vt" + SelectRank.r + "=\n");
                        Main.writeMatrix(Vtr);

                        Matrix UrSr = Ur.times(Sr);
                        textArea.append("\n" + "U" + SelectRank.r + "*S" + SelectRank.r + "=\n");
                        Main.writeMatrix(UrSr);

                        words_indeceis = new Matrix[words.length];
                        Sentenices_indecies = new Matrix[sentences.length];

                        for (int i = 0; i < words.length; i++) {
                            writeoutput(words[i]);
                            words_indeceis[i] = UrSr.getMatrix(i, i, 0, SelectRank.r - 1).transpose();
                            writeMatrix(words_indeceis[i]);
                        }

                        SrVtr = Sr.times(Vtr);
                        textArea.append("\n" + "S" + SelectRank.r + "*Vt" + SelectRank.r + "=\n");
                        Main.writeMatrix(SrVtr);

                        for (int i = 0; i < sentences.length; i++) {
                            writeoutput("d" + i + " : ");
                            //System.out.println(Integer.toString(i)+"  "+ 0+" "+Integer.toBinaryString(SelectRank.r-1)+"  "+ Integer.toString(i) );
                            Sentenices_indecies[i] = SrVtr.getMatrix(0, SelectRank.r - 1, i, i);
                            writeMatrix(Sentenices_indecies[i]);

                        }

                        sq = new SelectQuery();
                        sq.setVisible(true);

                        for (String w : words) {
                            sq.model.addElement(w);
                        }

                        writeoutput("Selected Query");
                        Q = new Matrix(SelectRank.r, 1);
                        sq.okButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent arg0) {

                                btnNewButton.setText("show");

                                btnNewButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                        LineChartDemo6 demo = new LineChartDemo6("Line Chart Demo 6");
                                        demo.pack();
                                        RefineryUtilities.centerFrameOnScreen(demo);
                                        demo.setVisible(true);

                                    }
                                });

                                selectedqu = sq.list.getSelectedIndices();

                                for (int i : sq.list.getSelectedIndices()) {
                                    writeoutput(words[i]);
                                    Q.plusEquals(words_indeceis[i]);
                                }

                                double d = 1.0 / SelectRank.r;
                                Q.timesEquals(d);

                                writeoutput("Q = ");
                                writeMatrix(Q);

                                sq.setVisible(false);

                                distances = new double[sentences.length];
                                Map<Integer, Double> map = new HashMap<Integer, Double>();

                                for (int i = 0; i < sentences.length; i++) {
                                    double numerator = 0;
                                    double A = 0;
                                    double B = 0;
                                    double denominator = 0;

                                    for (int j = 0; j < Sentenices_indecies[i].getRowDimension(); j++) {

                                        numerator += Sentenices_indecies[i].get(j, 0) * Q.get(j, 0);
                                        A += Sentenices_indecies[i].get(j, 0) * Sentenices_indecies[i].get(j, 0);
                                        B += Q.get(j, 0) * Q.get(j, 0);
                                    }

                                    denominator = Math.sqrt(A) * Math.sqrt(B);

                                    //writeoutput(String.valueOf(numerator));
                                    distances[i] = 1 - numerator / denominator;
                                    //writeoutput("Similarity of d"+i+": "+String.valueOf(1-distances[i]));
                                    //writeoutput("Distance of d"+i+": "+String.valueOf(distances[i]));
                                    //writeMatrix(distances[i]);

                                    map.put(i, distances[i]);

                                }

                                writeoutput("            ");
                                writeoutput("            ");
                                writeoutput("            ");

                                List<Integer> mapKeys = new ArrayList<>(map.keySet());
                                List<Double> mapValues = new ArrayList<>(map.values());
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
                                        Double comp1 = map.get(key);
                                        Double comp2 = val;

                                        if (comp1.equals(comp2)) {
                                            keyIt.remove();
                                            sortedMap.put(key, val);
                                            break;
                                        }
                                    }
                                }

                                for (Map.Entry<Integer, Double> entry : sortedMap.entrySet()) {
                                    Integer key = entry.getKey();
                                    Double value = entry.getValue();
                                    writeoutput("d " + key);
                                    appendoutput("\tDistance : " + String.valueOf(value));
                                    appendoutput("\tSimilarity : " + String.valueOf(1 - value));
                                    appendoutput("\tSentence : " + sentences[key]);

                                }

                                writeoutput("            ");

                                writeoutput("### Based on LSI , the following sentences are the top " + sr.r + " related to the give query are #######");

                                for (int i = 0; i < sr.r; i++) {
                                    Integer key = ((Map.Entry<Integer, Double>) sortedMap.entrySet().toArray()[i]).getKey();
                                    writeoutput("\tSentence " + key + " : " + sentences[key]);
                                    addSen(sentences[key]);
                                }

                                writeoutput("            ");
                                writeoutput("#############################");
                                writeoutput("###Gong and Liu (2001)#######");
                                writeoutput("#############################");
                                int r = rank;
                                int[] maxposition = new int[r];
                                for (int i = 0; i < r; i++) {

                                    maxposition[i] = 0;
                                    for (int j = 0; j < n; j++) {
                                        //Arrays.asList(maxposition.contains(j) 
                                        if (Vt.getArray()[i][j] > Vt.getArray()[i][maxposition[i]] && contains(maxposition, j)) {
                                            maxposition[i] = j;
                                        }
                                        Main.writeoutput(Double.toString(Vt.getArray()[i][j]) + "   ");
                                    }
                                    writeoutput();
                                }

                                writeoutput("selected sentences");
                                for (int mp : maxposition) {
                                    writeoutput(sentences[mp]);
                                    addSen(sentences[mp]);
                                }

                                writeoutput("            ");
                                writeoutput("#############################");
                                writeoutput("#Particle swarm optimization#");
                                writeoutput("#############################");

                                PSO ps = new PSO(words, sentences);
                                writeoutput("            ");
                                Matrix tf = ps.getTfMatrix();
                                writeMatrix(tf);
                                writeoutput("            ");
                                Matrix vc = ps.getweightMatrix(tf);
                                writeMatrix(vc);
                                writeoutput("            ");
                                writeoutput("### Based on PSO , the following sentences are the top " + rank + " related to the give query are ###");
                                writeoutput("            ");
                                int[] topScore = ps.getTopScore(selectedqu, vc);
                                for (int i = sentences.length - 1; i >= sentences.length - rank; i--) {
                                    writeoutput(sentences[topScore[i]]);
                                    addSen(sentences[topScore[i]]);
                                }
                                all = ps.getTop10();

                                writeoutput("            ");
                                writeoutput("#############################");
                                writeoutput("######Optimized answer#######");
                                writeoutput("#############################");

                                writeoutput("            ");
                                writeoutput("### Based on feaures , the following sentences are the top " + rank + " higher score ###");
                                writeoutput("            ");
                                String[] printSen = finalSen();
                                int k = 0;
                                int[] PrintSen = new int[rank];
                                for (int i = printSen.length - 1; i >= printSen.length - rank; i--) {
                                    writeoutput(printSen[i]);
                                    PrintSen[k] = Arrays.asList(sentences).indexOf(printSen[i]);
                                    k++;
                                }
                                writeoutput("            ");
                                writeoutput("### In order of apperance ###");
                                writeoutput("            ");
                                Arrays.sort(PrintSen);
                                for (int i = 0; i < rank; i++) {
                                    writeoutput(sentences[PrintSen[i]]);
                                }
                            }
                        });

                    }
                });

            } // Summerize botton event handler ends here
        });

    }

    //check if an array contain an element
    public boolean contains(final int[] array, final int key) {
        Arrays.sort(array);
        int[] sorted = array.clone();
        return Arrays.binarySearch(sorted, key) != -1;
    }

    public static void writeoutput(String s) {
        textArea.append("\n" + s);
    }

    public static void appendoutput(String s) {
        textArea.append(" " + s);
    }

    public static void writeoutput() {
        textArea.append("\n");
    }

    public static void writeMatrix(Matrix m) {
        textArea.append("\n");
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                textArea.append("\t" + df.format(m.get(i, j)));
            }
            textArea.append("\n");

        }

    }

    //get top 10 most used words in the document
    public String[] getTw() {
        String[] top;
        top = new String[10];
        int k = 0;
        for (int i = all.length - 1; i >= all.length - 10; i--) {
            top[k] = words[i];
            k++;
        }
        return top;
    }

    //get the top Thementic words
    public double maxTw() {
        double mx = 0;
        features f = new features(title, longest, Sentenices_indecies.length);
        String[] temp = getTw();
        for (int i = 0; i < topSen.size(); i++) {
            mx = max(mx, f.ThematicWord(topSen.get(i), temp));
        }
        return mx;
    }

    //sort sentnce depend on their scores
    public String[] finalSen() {
        HashMap<String, Double> senScore = new HashMap();
        String[] ans;
        ans = new String[topSen.size()];

        double maxtw = maxTw();

        for (int i = 0; i < topSen.size(); i++) {
            senScore.put(topSen.get(i), getSenTotal(topSen.get(i), maxtw));
        }

        int i = 0;
        senScore = sortHashMapByValues(senScore);
        for (String key : senScore.keySet()) {
            ans[i] = key;
            i++;
        }
        return ans;
    }

    //get any sentence score depended on features
    public double getSenTotal(String sen, double tw) {
        double total = 0;
        features f = new features(title, longest, Sentenices_indecies.length);
        double SL = f.SentenceLength(sen);
        total += SL;
        double TF = f.TitleFeature(sen);
        total += TF;
        double ND = f.NumericalData(sen);
        total += ND;
        double SP = 0;
        for (int i = 0; i < sentences.length; i++) {
            if (sen.equals(sentences[i])) {
                SP = f.SentencePosition(i);
                total += SP;
                break;
            }
        }
        String[] temp = getTw();
        double TW = f.ThematicWord(sen, temp) / tw;
        total += TW;
        total += senRepeat.get(sen);

        System.out.println(total + ": " + sen);
        System.out.println(SL + " " + SP + " " + ND + " " + TF + " " + TW + " " + senRepeat.get(sen));

        return total;
    }

    //add a new sentence to the selector choices
    public void addSen(String sen) {
        for (int i = 0; i < topSen.size(); i++) {
            if (sen.equals(topSen.get(i))) {
                senRepeat.put(sen, senRepeat.get(sen) + 0.5);
                return;
            }
        }
        topSen.add(sen);
        senRepeat.put(sen, 0.5);
    }

    //sort map by values
    public LinkedHashMap<String, Double> sortHashMapByValues(
            HashMap<String, Double> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Double> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Double> sortedMap
                = new LinkedHashMap<>();

        Iterator<Double> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Double val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
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
