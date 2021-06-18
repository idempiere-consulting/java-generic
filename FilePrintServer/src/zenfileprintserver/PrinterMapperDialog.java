 package zenfileprintserver;
 import java.awt.Dimension;
 import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.io.File;
 import java.util.Vector;
 import javax.print.DocFlavor;
 import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
 import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
 import javax.swing.ImageIcon;
 import javax.swing.JButton;
 import javax.swing.JComboBox;
 import javax.swing.JDialog;
 import javax.swing.JFileChooser;
 import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
 import javax.swing.LayoutStyle;
 
 public class PrinterMapperDialog extends JDialog {
   /**
	 * 
	 */
	private static final long serialVersionUID = 2519100744266297270L;
private PrinterMap kmap;
   private Vector<String> printerList = new Vector<>(); private boolean updateStatus = false;
   private MainPanel mp;
   private String oldPath = ""; private JTextField fileSelectorT; private JFileChooser file_FC; private JLabel jLabel2; private JLabel jLabel3; private JLabel jLabel4; private JTextField labelT;
   private JComboBox printerCB;
   private JButton saveB;
   private JButton undoB;
   
   public PrinterMapperDialog(MainPanel parent, boolean modal, PrinterMap kmap) {
     super(parent, modal);
     this.mp = parent;
     this.updateStatus = true;
     initComponents();
     initPrinterCB();
     this.kmap = kmap;
     this.labelT.setText(kmap.getKey());
     this.fileSelectorT.setText(kmap.getFilePath());
     this.oldPath = kmap.getFilePath();
     if (this.printerList.contains(kmap.getPrinter())) {
       this.printerCB.setSelectedIndex(this.printerList.indexOf(kmap.getPrinter()));
     } else {
       this.printerCB.setSelectedIndex(0);
     }  this.labelT.setEnabled(!this.updateStatus);
   }
   
   public PrinterMapperDialog(MainPanel parent, boolean modal) {
     super(parent, modal);
     this.mp = parent;
     this.updateStatus = false;
     initComponents();
     initPrinterCB();
     this.kmap = new PrinterMap();
 
 
     
     this.fileSelectorT.setText("");
     this.labelT.setEnabled(!this.updateStatus);
   }
   
   private void initPrinterCB() {
     DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
     PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
     PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
     this.printerCB.removeAllItems();
     this.printerList.add("");
     this.printerCB.addItem("");
     for (int ii = 0; ii < services.length; ii++) {
       this.printerList.add(services[ii].getName());
       this.printerCB.addItem(services[ii].getName());
     } 
   }
   
   private void initComponents() {
     this.file_FC = new JFileChooser();
     this.jLabel2 = new JLabel();
     this.jLabel3 = new JLabel();
     this.fileSelectorT = new JTextField();
     this.saveB = new JButton();
     this.undoB = new JButton();
     this.printerCB = new JComboBox();
     this.jLabel4 = new JLabel();
     this.labelT = new JTextField();
     
     this.file_FC.setFileSelectionMode(1);
     
     setDefaultCloseOperation(2);
     
     this.jLabel2.setFont(new Font("DejaVu Sans", 1, 12));
     this.jLabel2.setText("Stampante:");
     
     this.jLabel3.setFont(new Font("DejaVu Sans", 1, 12));
     this.jLabel3.setText("File path:");
     
     this.fileSelectorT.setEditable(false);
     this.fileSelectorT.setToolTipText("Click to browse");
     this.fileSelectorT.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent evt) {
             PrinterMapperDialog.this.fileSelectorTMouseClicked(evt);
           }
         });
     this.fileSelectorT.addKeyListener(new KeyAdapter() {
           public void keyPressed(KeyEvent evt) {
             PrinterMapperDialog.this.fileSelectorTKeyPressed(evt);
           }
         });
     
     this.saveB.setIcon(new ImageIcon(getClass().getResource("/icons/save_icon_32.png")));
     this.saveB.setToolTipText("Salva");
     this.saveB.setPreferredSize(new Dimension(32, 32));
     this.saveB.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt) {
             PrinterMapperDialog.this.saveBActionPerformed(evt);
           }
         });
     
     this.undoB.setIcon(new ImageIcon(getClass().getResource("/icons/undo_icon_32.png")));
     this.undoB.setToolTipText("Cancella");
     this.undoB.setPreferredSize(new Dimension(32, 32));
     this.undoB.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt) {
             PrinterMapperDialog.this.undoBActionPerformed(evt);
           }
         });
     
     this.printerCB.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
     
     this.jLabel4.setFont(new Font("DejaVu Sans", 1, 12));
     this.jLabel4.setText("Etichetta:");
     
     GroupLayout layout = new GroupLayout(getContentPane());
     getContentPane().setLayout(layout);
     layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(0, 0, 32767).addComponent(this.undoB, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.saveB, -2, -1, -2)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel2).addComponent(this.jLabel3).addComponent(this.jLabel4)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.labelT).addComponent(this.printerCB, 0, 581, 32767).addComponent(this.fileSelectorT)))).addContainerGap()));
     layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel4).addComponent(this.labelT, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel2).addComponent(this.printerCB, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel3).addComponent(this.fileSelectorT, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.saveB, -2, -1, -2).addComponent(this.undoB, -2, -1, -2)).addContainerGap(-1, 32767)));
     
     pack();
   }
   
   private void fileSelectorTMouseClicked(MouseEvent evt) {
     showFileSelector();
   }
   
   private void fileSelectorTKeyPressed(KeyEvent evt) {
     if (evt.getKeyCode() == 10) {
       showFileSelector();
     }
   }
   
   private void saveBActionPerformed(ActionEvent evt) {
     boolean isAfile = false;
     try {
       isAfile = (new File(this.fileSelectorT.getText())).isDirectory();
     } catch (Exception exc) {
       isAfile = false;
     } 
     boolean pt = !((String)this.printerCB.getSelectedItem()).isEmpty();
     boolean lt = !this.labelT.getText().isEmpty();
     if (lt && isAfile && pt) {
       String cld = checkLabelAndDir();
       if (cld.isEmpty()) {
         
         this.kmap.setPrinter((String)this.printerCB.getSelectedItem());
         this.kmap.setFilePath(this.fileSelectorT.getText());
         this.kmap.setKey(this.labelT.getText());
         this.kmap.setComplete(true);
         dispose();
       } else {
         showErrorMessage("Impossibile salvare: " + cld);
       } 
     } else {
       String errorMsg = "Impossibile salvare: dati non completi:";
       if (!isAfile)
         errorMsg = errorMsg + "\n- directory non valida"; 
       if (!pt)
         errorMsg = errorMsg + "\n- stampante non selezionata"; 
       if (!lt)
         errorMsg = errorMsg + "\n- etichetta non vuota"; 
       showErrorMessage(errorMsg);
     } 
   }
   
   private void undoBActionPerformed(ActionEvent evt) {
     dispose();
   }

   private String checkLabelAndDir() {
     String exitString = "";
     System.out.println((((this.mp == null) ? 1 : 0) + "" + this.mp.map == null));
     for (String key : this.mp.map.keySet()) {
       PrinterMap cpm = this.mp.map.get(key);
       boolean pc = (cpm.getFilePath().equals(this.fileSelectorT.getText()) && (!this.updateStatus || (this.updateStatus && !key.equals(this.kmap.getKey()))));
       
       boolean lc = (cpm.getKey().equals(this.labelT.getText()) && !this.updateStatus);
       if (pc)
         return "cartella già mappata"; 
       if (lc) {
         return "etichetta già in uso";
       }
     } 
     return "";
   }
   
   private void showFileSelector() {
     File cd;
     if (this.fileSelectorT.getText().length() > 0) {
       cd = new File(this.fileSelectorT.getText());
     } else {
       cd = new File(System.getProperty("user.home"));
     } 
     if (cd.isDirectory()) {
       this.file_FC.setCurrentDirectory(cd);
     }
     int retval = this.file_FC.showDialog(this, "Select");
     File theFile = this.file_FC.getSelectedFile();
     if (theFile != null) {
       String cPath = theFile.getAbsolutePath();
       if (theFile.isDirectory())
       {
         
         this.fileSelectorT.setText(cPath);
       
       }
     }
     else {
       showErrorMessage("Selezionare un file.");
     } 
   }
   
   public PrinterMap getKeyMap() {
     return this.kmap;
   }
   
   private void showErrorMessage(String messge) {
     Toolkit.getDefaultToolkit().beep();
     JOptionPane.showMessageDialog(this, messge, null, 0);
   }
 }