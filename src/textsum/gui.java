package textsum;

import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import javax.swing.JScrollPane;
import java.util.Iterator;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.max;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class gui {

    public JFrame frame;
    public JSlider js1 = new JSlider(), s1 = new JSlider(), s2 = new JSlider(), s3 = new JSlider(), s4 = new JSlider(), s5 = new JSlider();
    public JSpinner rnk = new JSpinner();
    public JPanel pnl2 = new JPanel();
    public JList jList1 = new javax.swing.JList<>(), jList2 = new javax.swing.JList<>();
    public DefaultListModel model1 = new DefaultListModel(), model2 = new DefaultListModel();
    public JTextField search = new JTextField();
    public JLabel startb, starta, submit = new JLabel(), submit2, clear = new JLabel(), t1, t2, t3, t4, t5, note, brief, desce, desca, background, arr_right, arr_left, lbl_or, lbl_0, lbl_90, head_text, head_output, sec_mag, compare, step;
    public JRadioButton im, or, r1;
    public JScrollPane scrollPane, scrollPane2, jScrollPane1, jScrollPane2;
    public static JTextPane textArea;
    public static JTextArea outputArea;
    public static DecimalFormat df;
    public static Matrix A, U, V, Vt, X, Y, S, Sr, Ur, Vr, Vtr, Q, SrVtr;
    public String[] sentences, words, ans;
    public static Matrix[] Sentenices_indecies, words_indeceis;
    public double[] distances;
    public int[] selectedqu, all;
    public String title, get;
    int m, n, rank, longest, type = 3, press = 0, x, y, mx, my, notnow = 0;
    public static ArrayList<String> topSen = new ArrayList<>(0);
    public HashMap<String, Double> senRepeat = new HashMap();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    gui window = new gui();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public gui() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    //Custom GUI
    private void initialize() {

        df = new DecimalFormat("#.0000");

        //mx = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        //my = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        mx = 1366;
        my = 800;
        
        x = (mx - 1000) / 2;
        y = (my - 700) / 2 + 30;
        
        int maxh = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int maxv = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

        frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        frame.setLocation(maxh/2 - mx / 2 ,maxv/2 - my / 2 );
        

        background = new javax.swing.JLabel();

        JLabel min = new JLabel();
        min.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        min.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                frame.setState(frame.ICONIFIED);
            }
        });
        min.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_min.png"))); // NOI18N
        frame.getContentPane().add(min, new org.netbeans.lib.awtextra.AbsoluteConstraints(mx - 70, 0, 35, 35));

        JLabel exit = new JLabel();
        exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                System.exit(0);
            }
        });
        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_exit.png"))); // NOI18N
        frame.getContentPane().add(exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(mx - 35, 0, 35, 35));

        submit2 = new JLabel();
        submit2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        submit2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                step1();
                submit2.setVisible(false);
            }
        });
        submit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_start.png"))); // NOI18N
        frame.getContentPane().add(submit2, new org.netbeans.lib.awtextra.AbsoluteConstraints(mx / 2 - 100, my / 2 + 200, -1, -1));

        startb = new JLabel("<html><p>By using \"Optimization Text Summary\" you can get a text summary,<br>"
                + "extracting the most useful sentences that help you to take decisions <br>"
                + "in time. the program offers a generous collection of choices to get <br>"
                + "the most accurate results by following easy steps.</p></html>");
        startb.setFont(new java.awt.Font("Nexa Bold", 0, 22)); // NOI18N
        startb.setForeground(Color.WHITE);
        frame.getContentPane().add(startb, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 100, y + 100, -1, -1));

        starta = new JLabel("<html><p align=\"right\">يمكنك باستخدام برنامج \"Optimization Text Summary\" الحصول على ملخص نصي <br>"
                + " باستخراج اهم الجمل المعلوماتيه التى تساعد على اتخاذ القرار فى الوقت المناسب،ويوفر<br>"
                + "لك البرنامج خيارات متعددة للوصول إلى أدق النتائج باتباع خطوات بسيطة.</p></html>");
        starta.setFont(new java.awt.Font("Hacen Samra", 0, 24)); // NOI18N
        starta.setForeground(Color.WHITE);
        frame.getContentPane().add(starta, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 220, y + 250, -1, -1));

        submit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        submit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (notnow == 1) {
                    return;
                }
                notnow = 1;
                if (press < 4) {
                    submit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_next_press.png"))); // NOI18N
                }
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                notnow = 0;
                if (press < 4) {
                    submit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_next.png"))); // NOI18N
                }
                if (press == 0) {
                    step2();
                    next();
                    press = 1;
                } else if (press == 1) {
                    step3();
                    submit();
                    press = 2;
                } else if (press == 2) {
                    step4();
                    submit2();
                    press = 3;
                } else if (press == 3) {
                    submited();
                    submit3();
                    press = 4;
                } else {
                    print();
                    submit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_print.png"))); // NOI18N
                }
            }
        });
        submit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_next.png"))); // NOI18N
        frame.getContentPane().add(submit, new org.netbeans.lib.awtextra.AbsoluteConstraints(630 + x, 630 + y, -1, -1));
        submit.setVisible(false);

        arr_right = new JLabel();
        arr_right.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        arr_right.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                forward();
            }
        });
        arr_right.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/arr_right.png"))); // NOI18N
        frame.getContentPane().add(arr_right, new org.netbeans.lib.awtextra.AbsoluteConstraints(600 + x, 450 + y, -1, -1));
        arr_right.setVisible(false);

        arr_left = new JLabel();
        arr_left.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        arr_left.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                bacword();
            }
        });
        arr_left.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/arr_left.png"))); // NOI18N
        frame.getContentPane().add(arr_left, new org.netbeans.lib.awtextra.AbsoluteConstraints(600 + x, 505 + y, -1, -1));
        arr_left.setVisible(false);

        JLabel pr_name = new JLabel("Optimization Text Summary");
        pr_name.setFont(new java.awt.Font("Nexa Bold", 0, 18)); // NOI18N
        pr_name.setForeground(new java.awt.Color(153, 153, 153));
        frame.getContentPane().add(pr_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, -1, 35));

        desce = new JLabel("<html>Enter the text you want to summerize, and <br>"
                + "hit the “Next” button, note that the first line <br>"
                + "must be the document title.</html>");
        desce.setFont(new java.awt.Font("Nexa Bold", 0, 20)); // NOI18N
        desce.setForeground(Color.WHITE);
        frame.getContentPane().add(desce, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 365, -1, -1));
        desce.setVisible(false);
        desca = new JLabel("<html>ادخل النص المراد تلخيصه في صندوق النص بالأعلى ثم اضغط<p align=\"right\">"
                + "زر “Next”، لاحظ أن أول سطر يجب أن يكون عنوان النص أو <br>"
                + "الوثيقة المدخلة.</p></html>");
        desca.setFont(new java.awt.Font("Hacen Samra", 0, 20)); // NOI18N
        desca.setForeground(Color.WHITE);
        frame.getContentPane().add(desca, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 510, y + 460, -1, -1));
        desca.setVisible(false);

        lbl_or = new JLabel("OR");
        lbl_or.setFont(new java.awt.Font("Nexa Bold", 0, 24)); // NOI18N
        lbl_or.setForeground(Color.WHITE);
        frame.getContentPane().add(lbl_or, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 590, y + 325, -1, -1));
        lbl_or.setVisible(false);

        lbl_0 = new JLabel("0%");
        lbl_0.setFont(new java.awt.Font("Nexa Bold", 0, 20)); // NOI18N
        lbl_0.setForeground(Color.WHITE);
        lbl_0.setVisible(false);

        lbl_90 = new JLabel("90%");
        lbl_90.setFont(new java.awt.Font("Nexa Bold", 0, 20)); // NOI18N
        lbl_90.setForeground(Color.WHITE);
        lbl_90.setVisible(false);

        JLabel bar = new JLabel();
        bar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/bar.png"))); // NOI18N
        frame.getContentPane().add(bar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, mx, 35));

        brief = new JLabel();
        brief.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/breif.png"))); // NOI18N
        frame.getContentPane().add(brief, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 30, y + 30, -1, -1));
        brief.setVisible(false);

        note = new JLabel();
        note.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/note.png"))); // NOI18N
        frame.getContentPane().add(note, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 922, y + 94, -1, -1));
        note.setVisible(false);

        head_text = new JLabel();
        head_text.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/head_text.png"))); // NOI18N
        frame.getContentPane().add(head_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(300 + x, 0 + y, -1, -1));
        head_text.setVisible(false);

        head_output = new JLabel();
        head_output.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/head_out.png"))); // NOI18N
        head_output.setVisible(false);
        frame.getContentPane().add(head_output, new org.netbeans.lib.awtextra.AbsoluteConstraints(300 + x, 335 + y, -1, -1));

        SpinnerModel sm = new SpinnerNumberModel(1, 1, 100, 1); //default value,lower bound,upper bound,increment by
        rnk.setModel(sm);
        rnk.setVisible(false);
        frame.getContentPane().add(rnk, new org.netbeans.lib.awtextra.AbsoluteConstraints(390 + x, 310 + y, 140, 40));

        js1.setMajorTickSpacing(1);
        js1.setMaximum(9);
        js1.setMinimum(0);
        js1.setMinorTickSpacing(1);
        js1.setPaintLabels(false);
        js1.setOpaque(false);
        js1.setPaintTicks(true);
        js1.setSnapToTicks(true);
        js1.setValue(0);
        frame.getContentPane().add(js1, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 670, y + 310, 250, 40));
        js1.setVisible(false);

        scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 30, 600, 250));
        scrollPane.setVisible(false);

        final DefaultStyledDocument document = new DefaultStyledDocument();
        textArea = new JTextPane(document);
        scrollPane.setViewportView(textArea);

        scrollPane2 = new JScrollPane();
        frame.getContentPane().add(scrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 390, 600, 230));
        scrollPane2.setVisible(false);

        outputArea = new JTextArea();
        scrollPane2.setViewportView(outputArea);

        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        sec_mag = new JLabel();
        sec_mag.setVisible(false);
        sec_mag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/sec.png"))); // NOI18N
        frame.getContentPane().add(sec_mag, new org.netbeans.lib.awtextra.AbsoluteConstraints(535 + x, 400 + y, -1, -1));

        ButtonGroup bg = new ButtonGroup();
        im = new JRadioButton("Impotance");
        bg.add(im);
        im.setSelected(true);
        im.setForeground(Color.WHITE);
        im.setOpaque(false);
        im.setContentAreaFilled(false);
        im.setBorderPainted(false);
        frame.getContentPane().add(im, new org.netbeans.lib.awtextra.AbsoluteConstraints(700 + x, 365 + y, -1, -1));
        im.setVisible(false);
        im.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText(switched(true));
            }
        });

        or = new JRadioButton("Apperance");
        bg.add(or);
        or.setOpaque(false);
        or.setContentAreaFilled(false);
        or.setBorderPainted(false);
        or.setForeground(Color.WHITE);
        frame.getContentPane().add(or, new org.netbeans.lib.awtextra.AbsoluteConstraints(800 + x, 365 + y, -1, -1));
        or.setVisible(false);
        or.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText(switched(false));
            }
        });

        search.setBorder(null);
        search.setBorder(BorderFactory.createCompoundBorder(
                search.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
//       search.setFont(new java.awt.Font("Nexa Bold", 0, 18));
        search.setForeground(Color.WHITE);
        search.setBackground(new java.awt.Color(153, 153, 153));
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search();
            }
        });
        frame.getContentPane().add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 400, 250, 30));
        search.setVisible(false);

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1.setBorder(null);
        jList1.setBorder(BorderFactory.createCompoundBorder(
                jList1.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportView(jList1);
        frame.getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 430, 250, 185));
        jScrollPane1.setVisible(false);

        jScrollPane2 = new javax.swing.JScrollPane();
        jList2.setBorder(null);
        jList2.setBorder(BorderFactory.createCompoundBorder(
                jList2.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        jScrollPane2.setBorder(null);
        jScrollPane2.setViewportView(jList2);
        jScrollPane2.setVisible(false);

        frame.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 670, y + 400, 250, 215));

        clear = new JLabel();
        clear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                restart();
            }
        });
        clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_restart.png"))); // NOI18N
        frame.getContentPane().add(clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(450 + x, 630 + y, -1, -1));
        clear.setVisible(false);

        compare = new JLabel();
        compare.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        compare.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                //highlight selected sentences in the input Area
                StyleContext context = new StyleContext();
                Style style = context.addStyle("test", null);
                Style style2 = context.addStyle("test2", null);
                StyleConstants.setForeground(style, Color.BLUE);
                StyleConstants.setForeground(style2, Color.black);

                textArea.setText("");
                try {
                    document.remove(0, document.getLength());
                } catch (BadLocationException ex) {
                    Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
                }
                String lines[] = ans[type].split("\\r?\\n");
                HashMap<String, Double> pos = new HashMap<String, Double>();
                for (int i = 0; i < lines.length; i++) {
                    pos.put(lines[i], (double) get.indexOf(lines[i]));
                }
                pos = sortHashMapByValues(pos);
                int l = 0;
                for (String key : pos.keySet()) {
                    try {
                        int x = pos.get(key).intValue();
                        if (x != -1) {
                            document.insertString(document.getLength(), get.substring(l, x), style2);
                            document.insertString(document.getLength(), key, style);
                            l = x + key.length();
                        }
                    } catch (BadLocationException ex) {
                        Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    document.insertString(document.getLength(), get.substring(l), style2);
                } catch (BadLocationException ex) {
                    Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
                }
                textArea.setStyledDocument(document);
            }
        });
        compare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_compare.png"))); // NOI18N
        frame.getContentPane().add(compare, new org.netbeans.lib.awtextra.AbsoluteConstraints(530 + x, 290 + y, -1, -1));
        compare.setVisible(false);

        pnl2 = new JPanel();
        pnl2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnl2.setBackground(Color.WHITE);
        pnl2.setVisible(false);
        frame.getContentPane().add(pnl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 30, y + 290, 230, 330));

        ButtonGroup rb = new ButtonGroup();
        r1 = new JRadioButton("Selector");
        JRadioButton r2 = new JRadioButton("LSA");
        JRadioButton r3 = new JRadioButton("Gong&Liu");
        JRadioButton r4 = new JRadioButton("VSM");
        rb.add(r4);
        customRB(r1);
        customRB(r2);
        customRB(r3);
        customRB(r4);
        rb.add(r3);
        rb.add(r2);
        rb.add(r1);
        r1.setSelected(true);
        pnl2.add(r1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 20, -1, -1));
        pnl2.add(r2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 60, -1, -1));
        pnl2.add(r3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 100, -1, -1));
        pnl2.add(r4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 140, -1, -1));

        t1 = new JLabel("Title Feature");
        t1.setFont(new java.awt.Font("Nexa Bold", 0, 18));
        t1.setForeground(Color.WHITE);
        customJS(s1);
        frame.getContentPane().add(t1, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 400, y + 100, -1, -1));
        frame.getContentPane().add(s1, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 570, y + 100, -1, -1));

        t2 = new JLabel("Sentence Length");
        t2.setFont(new java.awt.Font("Nexa Bold", 0, 18));
        t2.setForeground(Color.WHITE);
        customJS(s2);
        frame.getContentPane().add(t2, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 400, y + 140, -1, -1));
        frame.getContentPane().add(s2, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 570, y + 140, -1, -1));

        t3 = new JLabel("Sentence Position");
        t3.setFont(new java.awt.Font("Nexa Bold", 0, 18));
        t3.setForeground(Color.WHITE);
        customJS(s3);
        frame.getContentPane().add(t3, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 400, y + 180, -1, -1));
        frame.getContentPane().add(s3, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 570, y + 180, -1, -1));

        t4 = new JLabel("Numeric Data");
        t4.setFont(new java.awt.Font("Nexa Bold", 0, 18));
        t4.setForeground(Color.WHITE);
        customJS(s4);
        frame.getContentPane().add(t4, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 400, y + 220, -1, -1));
        frame.getContentPane().add(s4, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 570, y + 220, -1, -1));

        t5 = new JLabel("Themtic Words");
        t5.setFont(new java.awt.Font("Nexa Bold", 0, 18));
        t5.setForeground(Color.WHITE);
        customJS(s5);
        frame.getContentPane().add(t5, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 400, y + 260, -1, -1));
        frame.getContentPane().add(s5, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 570, y + 260, -1, -1));

        pnl(false);

        step = new JLabel();
        step.setFont(new java.awt.Font("Nexa Bold", 0, 24)); // NOI18N
        step.setForeground(Color.WHITE);
        frame.getContentPane().add(step, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 300, y + 20, -1, -1));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/bg.png"))); // NOI18N
        frame.getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, mx, my));

        frame.pack();
    }

    public void customJS(JSlider js1) {
        js1.setMajorTickSpacing(1);
        js1.setMaximum(5);
        js1.setMinimum(0);
        js1.setMinorTickSpacing(1);
        js1.setPaintLabels(false);
        js1.setOpaque(false);
        js1.setPaintTicks(true);
        js1.setSnapToTicks(true);
        js1.setValue(5);
    }

    public void customRB(final JRadioButton im) {
        im.setForeground(Color.gray);
        im.setOpaque(false);
        im.setContentAreaFilled(false);
        im.setBorderPainted(false);
        im.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != im.getText()) {
                    switch (im.getText()) {
                        case "Selector":
                            type = 0;
                            break;
                        case "LSA":
                            type = 1;
                            break;
                        case "Gong&Liu":
                            type = 2;
                            break;
                        case "Tf&Idf":
                            type = 3;
                            break;
                        default:
                            break;
                    }
                }
                outputArea.setText(switched(!or.isSelected()));
            }
        });
    }
    //end of GUI

    public void restart() {
        submit2.setVisible(true);
        startb.setVisible(true);
        starta.setVisible(true);
        submit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_next.png"))); // NOI18N
        press = 0;
        final DefaultStyledDocument document = new DefaultStyledDocument();
        textArea = new JTextPane(document);
        scrollPane.setViewportView(textArea);
        outputArea.setText("");
        im.setSelected(true);
        r1.setSelected(true);
        js1.setValue(0);
        rnk.setValue(1);
        model1.clear();
        model2.clear();
        s1.setValue(5);
        s2.setValue(5);
        s3.setValue(5);
        s4.setValue(5);
        s5.setValue(5);
        compare.setVisible(false);
        scrollPane.setVisible(false);
        scrollPane2.setVisible(false);
        head_text.setVisible(false);
        head_output.setVisible(false);
        rnk.setVisible(false);
        lbl_or.setVisible(false);
        lbl_0.setVisible(false);
        lbl_90.setVisible(false);
        js1.setVisible(false);
        im.setVisible(false);
        or.setVisible(false);
        brief.setVisible(false);
        submit.setVisible(false);
        clear.setVisible(false);
        jScrollPane1.setVisible(false);
        jScrollPane2.setVisible(false);
        desca.setVisible(false);
        desce.setVisible(false);
        note.setVisible(false);
        step.setVisible(false);
        jScrollPane2.setVisible(false);
        arr_left.setVisible(false);
        arr_right.setVisible(false);
        pnl(false);
        search.setVisible(false);
        sec_mag.setVisible(false);
        pnl2.setVisible(false);
        desce.setText("<html>Enter the text you want to summerize, and <br>"
                + "hit the “Next” button, note that the first line <br>"
                + "must be the document title.</html>");
        frame.getContentPane().add(desce, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 365, -1, -1));
        desca.setText("<html>ادخل النص المراد تلخيصه في صندوق النص بالأعلى ثم اضغط<p align=\"right\">"
                + "زر “Next”، لاحظ أن أول سطر يجب أن يكون عنوان النص أو <br>"
                + "الوثيقة المدخلة.</p></html>");
        frame.getContentPane().add(desca, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 510, y + 460, -1, -1));
        frame.getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, mx, my));
    }

    public void step1() {
        step.setText("STEP 1 :");
        starta.setVisible(false);
        startb.setVisible(false);
        rnk.setVisible(false);
        lbl_or.setVisible(false);
        js1.setVisible(false);
        scrollPane.setVisible(true);
        note.setVisible(true);
        desca.setVisible(true);
        desce.setVisible(true);
        brief.setVisible(true);
        submit.setVisible(true);
        clear.setVisible(true);
        head_text.setVisible(true);
        jScrollPane1.setVisible(false);
        jScrollPane2.setVisible(false);
        arr_left.setVisible(false);
        arr_right.setVisible(false);
        pnl(false);
        search.setVisible(false);
        sec_mag.setVisible(false);
        frame.getContentPane().add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 90, 600, 250));
        frame.getContentPane().add(head_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 300, y + 60, -1, -1));
        frame.getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, mx, my));
    }

    public void step2() {
        step.setText("STEP 2:");
        rnk.setVisible(true);
        lbl_or.setVisible(true);
        lbl_0.setVisible(true);
        lbl_90.setVisible(true);
        js1.setVisible(true);
        scrollPane.setVisible(false);
        arr_left.setVisible(false);
        arr_right.setVisible(false);
        pnl(false);
        note.setVisible(false);
        search.setVisible(false);
        sec_mag.setVisible(false);
        head_text.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/head_rank.png"))); // NOI18N
        desce.setText("<html>Choose number of sentence required<br>"
                + "in the summrized text from the spinner<br>"
                + ", or choose a percent from the slider.</html>"
        );

        desca.setText(
                "<html>قم باختيار عدد الجمل المراد الحصول عليها في النص<p align=\"right\">"
                + "ملخص (المخرج) من خلال“spinner” ، أو قم باختيار<br>"
                + "نسبة مئوية من طول النص الأصلي من خلال المزلاق.</p></html>"
        );
        frame.getContentPane().add(rnk, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 550, y + 110, 140, 40));
        frame.getContentPane().add(lbl_or, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 600, y + 180, -1, -1));
        frame.getContentPane().add(lbl_0, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 450, y + 220, -1, -1));
        frame.getContentPane().add(lbl_90, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 740, y + 220, -1, -1));
        frame.getContentPane().add(js1, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 490, y + 215, 250, 40));
        frame.getContentPane().add(desce, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 305, -1, -1));
        frame.getContentPane().add(desca, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 510, y + 420, -1, -1));
        frame.getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, mx, my));
    }

    public void step3() {
        step.setText("STEP 3: (Optional)");
        rnk.setVisible(false);
        lbl_or.setVisible(false);
        lbl_0.setVisible(false);
        lbl_90.setVisible(false);
        js1.setVisible(false);
        jScrollPane1.setVisible(true);
        jScrollPane2.setVisible(true);
        scrollPane.setVisible(false);
        arr_left.setVisible(true);
        arr_right.setVisible(true);
        sec_mag.setVisible(true);
        search.setVisible(true);
        head_text.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/head_query.png"))); // NOI18N
        desce.setText("<html>If you want a feature-based summrize you can<br>"
                + "just pass this step, if you want query-based<br>"
                + "summrize please select the words that matters<br>"
                + "you most.</html>"
        );

        desca.setText(
                "<html>لو كنت ترغب في تلخيص يعتمد على الخصائص فقط،<p align=\"right\">"
                + "يمكنك تجاهل هذه الخطوة، ولكن إذا كنت ترغب في<br>"
                + "تلخيص أكثر دقة يمكنك اختيار الكلمات التي تهمك.</p></html>"
        );

        frame.getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 120, 250, 265));
        frame.getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 670, y + 90, 250, 295));
        frame.getContentPane().add(sec_mag, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 540, y + 90, 30, 30));
        frame.getContentPane().add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 90, 250, 30));
        frame.getContentPane().add(arr_right, new org.netbeans.lib.awtextra.AbsoluteConstraints(600 + x, 200 + y, -1, -1));
        frame.getContentPane().add(arr_left, new org.netbeans.lib.awtextra.AbsoluteConstraints(600 + x, 250 + y, -1, -1));
        frame.getContentPane().add(desce, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 420, -1, -1));
        frame.getContentPane().add(desca, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 570, y + 500, -1, -1));
        frame.getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, mx, my));
    }

    public void step4() {
        step.setText("STEP 4: (Optional)");
        jScrollPane1.setVisible(false);
        jScrollPane2.setVisible(false);
        arr_left.setVisible(false);
        arr_right.setVisible(false);
        search.setVisible(false);
        sec_mag.setVisible(false);
        pnl(true);
        head_text.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/head_feature.png"))); // NOI18N
        desce.setText("<html>Choose how important is every feature to you,<br>"
                + "in order they are ,how much sentence is related<br>"
                + "to the document title, How long the sentence is,<br>"
                + "where is that sentence in the document,How much<br>"
                + "Numbers it contain, and how much it is realted to<br>"
                + "the other sentence in the docment.</html>"
        );

        desca.setText(
                "<html><p align=\"right\">قم باختيار أهمية كل خاصية بالنسبة لك، الخصائص حسب، <br>"
                + "الترتيب هي، مدى تقارب الجملة مع العنوان، طول الجملة  <br>"
                + "مقارنة بباقي الجمل، موقع الجملة في النص،المعلومات <br>"
                + "  الرقمية الموجودة بها، مدى توافقها مع باقي جمل النص.</p></html>"
        );

        frame.getContentPane().add(desce, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 320, -1, -1));
        frame.getContentPane().add(desca, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 570, y + 450, -1, -1));
        frame.getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, mx, my));
    }

    public void next() {
        get = textArea.getText();
        String lines[] = textArea.getText().split("\\r?\\n");
        title = lines[0];
        String text = "";
        for (int i = 1; i < lines.length; i++) {
            text += lines[i] + "\n";
        }
        if (lines.length == 1) {
            title = "";
            text = get;
        }

        PreProcessor pp = new PreProcessor(text, "src\\stopwords_en.txt");
        sentences = pp.getSentences();
        words = pp.getWords();

        m = words.length;
        n = sentences.length;
        longest = pp.longest;

        // creating the matrix using the get numeric method  
        //Matrix A= pp.get_numiric_matrix();
        A = pp.getTfMatrix();

        // applying the SVD
        SingularValueDecomposition svd = A.svd();

        // U matrix
        U = svd.getU();

        // printing Sigma matrix	
        S = svd.getS();

        V = svd.getV();

        Vt = svd.getV().transpose();

        X = U.times(S);

        Y = S.times(V.transpose());

        SpinnerModel sm = new SpinnerNumberModel(1, 1, sentences.length - 1, 1);
        rnk.setModel(sm);

        for (String w : words) {
            model1.addElement(w);
        }
        jList1.setModel(model1);

    }

    public void submit3() {
        if (model2.size() == 0) {
            noneQuery();//handle query without array
            return;
        }
        ans[3] = "";
        writeoutput("######Optimized answer#######");
        writeoutput("######importance#######");
        String[] printSen = finalSen();
        int k = 0;
        int[] PrintSen = new int[rank];
        for (int i = printSen.length - 1; i >= printSen.length - rank; i--) {
            writeoutput(printSen[i]);
            ans[3] += printSen[i] + "\n";
            PrintSen[k] = Arrays.asList(sentences).indexOf(printSen[i]);
            k++;
        }

        writeoutput("######apperance#######");
        Arrays.sort(PrintSen);
        for (int i = 0; i < rank; i++) {
            writeoutput(sentences[PrintSen[i]]);
        }
        outputArea.setText(ans[0]);
    }

    public void submit2() {
        if (model2.size() == 0) {
            return;
        }
        selectedqu = new int[model2.size()];
        for (int i = 0; i < model2.size(); i++) {
            for (int j = 0; j < words.length; j++) {
                if (words[j].equals(model2.getElementAt(i).toString())) {
                    selectedqu[i] = j;
                    Q.plusEquals(words_indeceis[j]);
                    break;
                }
            }
        }

        double d = 1.0 / SelectRank.r;
        Q.timesEquals(d);

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

            distances[i] = 1 - numerator / denominator;

            map.put(i, distances[i]);

        }

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

        }
        writeoutput("###LSI#######");
        ans[0] = "";
        for (int i = 0; i < rank; i++) {
            Integer key = ((Map.Entry<Integer, Double>) sortedMap.entrySet().toArray()[i]).getKey();
            writeoutput("\tSentence " + key + " : " + sentences[key]);
            addSen(sentences[key]);
            ans[0] += sentences[key] + "\n";
        }

        writeoutput("###Gong and Liu (2001)#######");
        int r = rank;
        int[] maxposition = new int[r];
        for (int i = 0; i < r; i++) {

            maxposition[i] = 0;
            for (int j = 0; j < n; j++) {
                if (Vt.getArray()[i][j] > Vt.getArray()[i][maxposition[i]] && contains(maxposition, j)) {
                    maxposition[i] = j;
                }
            }
        }
        ans[1] = "";
        for (int mp : maxposition) {
            writeoutput(sentences[mp]);
            addSen(sentences[mp]);
            ans[1] += sentences[mp] + "\n";
        }
        writeoutput("###PSO#######");
        PSO ps = new PSO(words, sentences);
        Matrix tf = ps.getTfMatrix();
        Matrix vc = ps.getweightMatrix(tf);
        //              writeoutput("### Based on PSO , the following sentences are the top " + rank + " related to the give query are ###");
        ans[2] = "";
        int[] topScore = ps.getTopScore(selectedqu, vc);
        for (int i = sentences.length - 1; i >= sentences.length - rank; i--) {
            writeoutput(sentences[topScore[i]]);
            addSen(sentences[topScore[i]]);
            ans[2] += sentences[topScore[i]] + "\n";
        }
        all = ps.getTop10();
    }

    public void submit() {
        ans = new String[8];

        rank = max(js1.getValue() * sentences.length / 10, Integer.parseInt(rnk.getValue().toString()));

        Sr = S.getMatrix(0, SelectRank.r - 1, 0, SelectRank.r - 1);
        Ur = U.getMatrix(0, U.getRowDimension() - 1, 0, SelectRank.r - 1);
        Vr = V.getMatrix(0, SelectRank.r - 1, 0, SelectRank.r - 1);
        Vtr = Vt.getMatrix(0, SelectRank.r - 1, 0, Vt.getColumnDimension() - 1);

        Matrix UrSr = Ur.times(Sr);

        words_indeceis = new Matrix[words.length];
        Sentenices_indecies = new Matrix[sentences.length];

        for (int i = 0; i < words.length; i++) {
            words_indeceis[i] = UrSr.getMatrix(i, i, 0, SelectRank.r - 1).transpose();
        }

        SrVtr = Sr.times(Vtr);

        for (int i = 0; i < sentences.length; i++) {
            Sentenices_indecies[i] = SrVtr.getMatrix(0, SelectRank.r - 1, i, i);
        }
        Q = new Matrix(SelectRank.r, 1);
    }

    public void submited() {
        pnl(false);
        step.setVisible(false);
        desca.setVisible(false);
        desce.setVisible(false);
        scrollPane2.setVisible(true);
        scrollPane.setVisible(true);
        pnl2.setVisible(true);
        im.setVisible(true);
        or.setVisible(true);
        head_output.setVisible(true);
        head_text.setVisible(true);
        compare.setVisible(true);

        frame.getContentPane().add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(x + 320, y + 30, 600, 250));

        head_text.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/head_text.png"))); // NOI18N
        frame.getContentPane().add(head_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(300 + x, 0 + y, -1, -1));

        submit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_print.png"))); // NOI18N
        frame.getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, mx, my));
    }

 
    public void print() {
        try {
            outputArea.print();
        } catch (PrinterException ex) {
            Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void holdon(int time) {
        notnow = 1;
        submit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_next_press.png"))); // NOI18N
        Timer timer;
        timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                notnow = 0;
                submit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/btn_next.png"))); // NOI18N
            }
        };
        timer.schedule(tt, time);
    }

    public void noneQuery() {
        PSO ps = new PSO(words, sentences);
        Matrix tf = ps.getTfMatrix();
        Matrix vc = ps.getweightMatrix(tf);
        all = ps.getTop10();
        double mx = 0;
        features f = new features(title, longest, Sentenices_indecies.length);
        String[] temp = getTw();
        for (String sentence : sentences) {
            mx = max(mx, f.ThematicWord(sentence, temp));
        }
        HashMap<String, Double> senScore = new HashMap();
        String[] answ;
        answ = new String[sentences.length];

        for (String sentence : sentences) {
            senScore.put(sentence, getSenTotal(sentence, mx, false));
        }

        int i = 0;
        senScore = sortHashMapByValues(senScore);
        for (String key : senScore.keySet()) {
            answ[i] = key;
            i++;
        }
        int k = 0;
        ans[5] = "";
        for (int j = answ.length - 1; j >= answ.length - rank; j--) {
            ans[5] += answ[j] + "\n";
            type = 5;
            k++;
        }

        outputArea.setText(ans[5]);
        pnl2.setVisible(false);
    }

    public void forward() {
        Object[] query = jList1.getSelectedValues();
        for (Object query1 : query) {
            model2.addElement(query1.toString());
            model1.removeElement(query1.toString());
        }
        jList1.setModel(model1);
        jList2.setModel(model2);
    }

    public void bacword() {
        Object[] query = jList2.getSelectedValues();
        for (Object query1 : query) {
            model1.addElement(query1.toString());
            model2.removeElement(query1.toString());
        }
        jList1.setModel(model1);
        jList2.setModel(model2);
    }

    public void search() {
        DefaultListModel temp = new DefaultListModel();
        String ref = search.getText();
        for (int i = 0; i < model1.size(); i++) {
            String x = model1.getElementAt(i).toString().substring(0, ref.length());
            if (ref == null ? x == null : ref.equals(x)) {
                temp.addElement(model1.getElementAt(i).toString());
            }
        }
        jList1.setModel(temp);
    }

    public void pnl(boolean tx) {
        t1.setVisible(tx);
        t2.setVisible(tx);
        t3.setVisible(tx);
        t4.setVisible(tx);
        t5.setVisible(tx);
        s1.setVisible(tx);
        s2.setVisible(tx);
        s3.setVisible(tx);
        s4.setVisible(tx);
        s5.setVisible(tx);
    }

    public void restartApplication() {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            /* is it a jar file? */
            if (!currentJar.getName().endsWith(".jar")) {
                return;
            }

            /* Build command: java -jar application.jar */
            final ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean contains(final int[] array, final int key) {
        Arrays.sort(array);
        int[] sorted = array.clone();
        return Arrays.binarySearch(sorted, key) != -1;
    }

    public static void writeoutput(String s) {
        //textArea.append("\n" + s);
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

    //get the top 
    public double maxTw() {
        double mx = 0;
        features f = new features(title, longest, Sentenices_indecies.length);
        String[] temp = getTw();
        for (int i = 0; i < topSen.size(); i++) {
            mx = max(mx, f.ThematicWord(topSen.get(i), temp));
        }
        return mx;
    }

    public String[] finalSen() {
        HashMap<String, Double> senScore = new HashMap();
        String[] ans;
        ans = new String[topSen.size()];

        double maxtw = maxTw();

        for (int i = 0; i < topSen.size(); i++) {
            senScore.put(topSen.get(i), getSenTotal(topSen.get(i), maxtw, true));
        }

        int i = 0;
        senScore = sortHashMapByValues(senScore);
        for (String key : senScore.keySet()) {
            ans[i] = key;
            i++;
        }
        return ans;
    }

    public double getSenTotal(String sen, double tw, boolean selector) {
        double total = 0;
        features f = new features(title, longest, Sentenices_indecies.length);
        double SL = f.SentenceLength(sen) * (s2.getValue() / 5d);
        total += SL;
        double TF = f.TitleFeature(sen) * (s1.getValue() / 5d);
        total += TF;
        double ND = f.NumericalData(sen) * (s4.getValue() / 5d);
        total += ND;
        double SP = 0;
        for (int i = 0; i < sentences.length; i++) {
            if (sen.equals(sentences[i])) {
                SP = f.SentencePosition(i) * (s3.getValue() / 5d);
                total += SP;
                break;
            }
        }
        String[] temp = getTw();
        double TW = f.ThematicWord(sen, temp) / tw;
        total += TW * (s5.getValue() / 5d);
        if (selector) {
            total += senRepeat.get(sen);
        }
        return total;
    }

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

    public String byOrder(String x) {
        int[] PrintSen = new int[rank];
        String lines[] = x.split("\\r?\\n");
        for (int i = 0; i < rank; i++) {
            PrintSen[i] = Arrays.asList(sentences).indexOf(lines[i]);
        }
        Arrays.sort(PrintSen);
        String answer = "";
        for (int i = 0; i < rank; i++) {
            answer += sentences[PrintSen[i]] + "\n";
        }
        return answer;
    }

    public String switched(boolean imp) {
        String answer;
        answer = ans[type];
        if (imp) {
            return answer;
        } else {
            return byOrder(answer);
        }
    }

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
