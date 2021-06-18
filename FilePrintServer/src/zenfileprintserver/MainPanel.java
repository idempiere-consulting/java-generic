package zenfileprintserver;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

public class MainPanel extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 848570939862823894L;
	public HashMap<String, PrinterMap> map = new HashMap<>();
	PrinterMapperDialog kmd;
	HashMap<String, String> validJobs = new HashMap<>(); private static Hashtable<String, String> initPars; private static String workingPath; private String configFile;
	private FileLock lock;
	private boolean running = false;
	private JToggleButton addB;
	private JToggleButton editB;
	private TextArea infoTA;
	private JPanel jPanel4;
	private JPanel jPanel5;
	private JScrollPane jScrollPane1;
	private JTable mainTable_JT;
	private JToggleButton removeB;
	private JToggleButton startStopB;

	public MainPanel() {
		initComponents();
		if (isThereAnotherApplicationInstanceRunningByFileLock()) {
			showErrorMessage("C'è già un'altra istanza in esecuzione");
			System.exit(0);
		} 
		getWorkingPath();
		readClientIni();
		readDeviceStoredMaps();
		enableStartStopButton();
		this.editB.setVisible(false);
		this.editB.setEnabled(false);
		if (this.startStopB.isEnabled() && initPars.get("autoStartup") != null && ((String)initPars.get("autoStartup")).equals("Y")) {


			this.startStopB.setSelected(true);
			startStop();
		} 
	}

	private void initComponents() {
		this.jPanel4 = new JPanel();
		this.jScrollPane1 = new JScrollPane();
		this.mainTable_JT = new JTable();
		this.addB = new JToggleButton();
		this.removeB = new JToggleButton();
		this.editB = new JToggleButton();
		this.jPanel5 = new JPanel();
		this.startStopB = new JToggleButton();
		this.infoTA = new TextArea();

		setDefaultCloseOperation(3);
		setTitle("ZeNFilePrintServer v1.1");
		setLocationByPlatform(true);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent evt) {
				MainPanel.this.formWindowOpened(evt);
			}
		});

		this.jPanel4.setBorder(BorderFactory.createTitledBorder("Mappe"));

		this.mainTable_JT.setModel(new DefaultTableModel(new Object[0][], (Object[])new String[] { "Etichetta", "File" })
		{
			Class<?>[] types = new Class<?>[] { String.class, String.class };
			boolean[] canEdit = new boolean[] { false, false };

			public Class<?> getColumnClass(int columnIndex) {
				return this.types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return this.canEdit[columnIndex];
			}
		});
		this.mainTable_JT.setRowSelectionAllowed(false);
		this.mainTable_JT.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				MainPanel.this.mainTable_JTMousePressed(evt);
			}
		});
		this.jScrollPane1.setViewportView(this.mainTable_JT);

		this.addB.setIcon(new ImageIcon(getClass().getResource("/icons/plusS.jpg")));
		this.addB.setToolTipText("Aggiungi");
		this.addB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainPanel.this.addBActionPerformed(evt);
			}
		});

		this.removeB.setIcon(new ImageIcon(getClass().getResource("/icons/minusS.jpg")));
		this.removeB.setToolTipText("Cancella selezionati");
		this.removeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainPanel.this.removeBActionPerformed(evt);
			}
		});

		this.editB.setIcon(new ImageIcon(getClass().getResource("/icons/editS.jpg")));
		this.editB.setToolTipText("Modifica selezionati");
		this.editB.setEnabled(false);
		this.editB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainPanel.this.editBActionPerformed(evt);
			}
		});

		GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
		this.jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(this.jScrollPane1, -1, 819, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.addB, -2, 33, -2).addComponent(this.removeB, GroupLayout.Alignment.TRAILING, -2, 13, -2)).addComponent(this.editB, GroupLayout.Alignment.TRAILING, -2, 33, -2)).addContainerGap()));
		jPanel4Layout.linkSize(0, new Component[] { this.addB, this.editB, this.removeB });
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane1, -1, 391, 32767).addGroup(jPanel4Layout.createSequentialGroup().addComponent(this.addB).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.removeB, -2, 21, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.editB, -2, 19, -2).addGap(0, 0, 32767))).addContainerGap()));
		jPanel4Layout.linkSize(1, new Component[] { this.addB, this.editB, this.removeB });

		this.jPanel5.setBorder(BorderFactory.createTitledBorder("Stato"));

		this.startStopB.setIcon(new ImageIcon(getClass().getResource("/icons/play.jpg")));
		this.startStopB.setToolTipText("Avvia");
		this.startStopB.setMaximumSize(new Dimension(49, 33));
		this.startStopB.setMinimumSize(new Dimension(49, 33));
		this.startStopB.setPreferredSize(new Dimension(49, 33));
		this.startStopB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainPanel.this.startStopBActionPerformed(evt);
			}
		});

		this.infoTA.setEditable(false);

		GroupLayout jPanel5Layout = new GroupLayout(this.jPanel5);
		this.jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.infoTA, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.startStopB, -2, 33, -2)));
		jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(this.startStopB, -2, 33, -2).addGap(0, 0, 32767)).addComponent(this.infoTA, -1, 100, 32767));
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanel4, -1, -1, 32767).addComponent(this.jPanel5, -1, -1, 32767)).addContainerGap()));     
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel4, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.jPanel5, -2, -1, -2).addContainerGap()));

		pack();
	}

	private void formWindowOpened(WindowEvent evt) {
		checkSystem();
	}

	private void startStopBActionPerformed(ActionEvent evt) {
		startStop();
	}

	private void editBActionPerformed(ActionEvent evt) {
		editItem();
		this.editB.setSelected(false);
		enableStartStopButton();
	}

	private void removeBActionPerformed(ActionEvent evt) {
		deleteSelected();
		this.removeB.setSelected(false);
		enableStartStopButton();
	}

	private void addBActionPerformed(ActionEvent evt) {
		newItem();
		this.addB.setSelected(false);
		enableStartStopButton();
	}

	private void mainTable_JTMousePressed(MouseEvent evt) {
		if (evt.getClickCount() == 2 && !this.running) {
			editItem();
			this.editB.setSelected(false);
		} 
	}

	private boolean startDemon() {
		this.running = true;
		Thread processThread = new Thread() {
			public void run() {
				try {
					Thread.sleep(2000L);
				} catch (Exception exc) {
					System.out.println(exc.toString());
				} 

				String textToPrint = " Servizio attivato sulle seguenti cartelle:\n";
				for (String key : MainPanel.this.validJobs.keySet()) {
					textToPrint = textToPrint + "                                  - " + key + " --> " + (String)MainPanel.this.validJobs.get(key) + "\n";
				}
				MainPanel.this.writeOnPanel(textToPrint);
				while (MainPanel.this.running) {
					for (String key : MainPanel.this.validJobs.keySet()) {
						int errors = MainPanel.this.printFiles(key, MainPanel.this.validJobs.get(key));
						if (errors > 0) {
							MainPanel.this.writeOnPanel(key + ": " + errors + " errori di stampa");
						}
					} 
					try {
						long dt = 5000L;
						try {
							dt = (new Long((String)MainPanel.initPars.get("cycleTimer"))).longValue();
						} catch (Exception ex) {
							System.out.println(dt);
						} 
						Thread.sleep(dt);
					}
					catch (Exception exc) {
						System.out.println(exc.toString());
					} 
				} 
			}
		};



		processThread.start();
		return true;
	}

	private boolean stopDemon() {
		this.infoTA.append(getFileStamp() + " Servizio farmato\n");
		this.running = false;
		return true;
	}

	private void startStop() {
		if (this.mainTable_JT.getModel().getRowCount() > 0) {
			if (this.startStopB.isSelected()) {
				if (startDemon()) {
					this.startStopB.setIcon(new ImageIcon(getClass().getResource("/icons/stop.jpg")));
					this.startStopB.setToolTipText("Ferma");
				} else {

					this.startStopB.setSelected(false);
				}

			} else if (stopDemon()) {
				this.startStopB.setToolTipText("Avvia");
				this.startStopB.setIcon(new ImageIcon(getClass().getResource("/icons/play.jpg")));
			} else {
				this.startStopB.setSelected(true);
			} 

			this.addB.setEnabled(!this.running);
			this.removeB.setEnabled(!this.running);
		} else {
			showErrorMessage("Non sono state definite associazioni cartelle-stampanti");
		} 
	}

	private void enableStartStopButton() {
		this.startStopB.setEnabled((this.mainTable_JT.getModel().getRowCount() > 0));
	}



	private void writeOnPanel(String text) {
		this.infoTA.append(getFileStamp() + " " + text + "\n");
	}



	private void newItem() {
		this.kmd = new PrinterMapperDialog(this, true);
		this.kmd.setLocationRelativeTo(this);
		this.kmd.setVisible(true);
		PrinterMap cm = this.kmd.getKeyMap();
		if (cm.getComplete()) {
			this.map.put(cm.getKey(), cm);
			updateMaps();
		} 
		if (this.kmd != null) {
			this.kmd = null;
		}
	}

	private void editItem() {
		int[] selectedRows = this.mainTable_JT.getSelectedRows();
		int noe = selectedRows.length;
		boolean OK = false;
		if (noe == 1) {
			int selecetdRow = this.mainTable_JT.convertRowIndexToModel(selectedRows[0]);
			String key = ((MapsTableModel)this.mainTable_JT.getModel()).getKeyValueAt(selecetdRow);
			PrinterMap cm = this.map.get(key);
			cm.setComplete(false);
			this.kmd = new PrinterMapperDialog(this, true, cm);
			this.kmd.setLocationRelativeTo(this);
			this.kmd.setVisible(true);
			cm = this.kmd.getKeyMap();
			if (cm.getComplete()) {
				this.map.put(key, cm);
			}
			if (this.kmd != null) {
				this.kmd = null;
			}
			updateMaps();
		} 
	}

	private void deleteSelected() {
		int[] selectedRows = this.mainTable_JT.getSelectedRows();
		int noe = selectedRows.length;
		boolean OK = false;
		if (noe == 1) {
			Toolkit.getDefaultToolkit().beep();
			int answer = JOptionPane.showConfirmDialog(this, "Una mappatura verrà  cancellata: continuare?");
			if (answer != 1 && answer != 2)
			{
				OK = true;
			}
		} else if (noe > 0) {
			Toolkit.getDefaultToolkit().beep();
			int answer = JOptionPane.showConfirmDialog(this, noe + " mappature verranno cancellate: continuare?");
			if (answer != 1 && answer != 2)
			{
				OK = true;
			}
		} 
		if (OK) {
			String[] keys = new String[selectedRows.length];
			for (int ii = 0; ii < selectedRows.length; ii++) {
				keys[ii] = ((MapsTableModel)this.mainTable_JT.getModel()).getKeyValueAt(this.mainTable_JT.convertRowIndexToModel(selectedRows[ii]));
			}

			for (int jj = 0; jj < keys.length; jj++) {
				this.map.remove(keys[jj]);
			}
			updateMaps();
		} 
	}

	private void checkSystem() {
		if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
			showErrorMessage("Il sistema non supporta l'apertura dei file d java.. Spiacente!");
			System.exit(-1);
		} 
		try {
			File configFileDir = new File("cnf");
			if (!configFileDir.exists()) {
				configFileDir.mkdir();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} 
		String msg = checkPrintersAndDirs();
		if (!msg.isEmpty()) {
			showErrorMessage("Non sono state trovate le seguenti risorse:\n" + msg);
		}
	}

	private void doSomething(String keyCode) {
		String path = ((PrinterMap)this.map.get(keyCode)).getFilePath();
		File fileToOpen = new File(path);
		if (fileToOpen.exists() && fileToOpen.isFile()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.open(fileToOpen);
			} catch (IOException e) {
				showErrorMessage("Errore durante l'apertura del file: " + e.toString());
				e.printStackTrace();
			} 
		} else {
			showErrorMessage("Il file " + path + " non esiste!");
		} 
	}

	private void readDeviceStoredMaps() {
		XmlFileReader read = new XmlFileReader();
		this.map = read.readConfig(this.configFile);
		updateTable();
	}

	private boolean writeDeviceStoredMaps() {
		try {
			XmlFileWriter write = new XmlFileWriter();
			write.setFile(this.configFile, this.map);
			write.saveConfig();
			return true;
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		} 
	}

	private boolean updateMaps() {
		if (writeDeviceStoredMaps()) {
			readDeviceStoredMaps();
			return true;
		} 
		return false;
	}


	private void updateTable() {
		boolean[] ec = { false, false, false };
		Class<?>[] ct = { String.class, String.class, String.class };
		String[] cn = { "Etichetta", "File", "Stampante" };
		String[] rcn = { "key", "filePath", "printer" };
		this.mainTable_JT.setModel(new MapsTableModel(hashMapToVector(this.map), cn, rcn, ec, ct, 0, 3));
		this.mainTable_JT.updateUI();
	}

	private void showErrorMessage(String errorMsg) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(this, errorMsg, null, 0);
	}

	public static void main(String[] args) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				} 
			} 
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, (String)null, ex);
		} 



		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainPanel kntp = new MainPanel();
				kntp.setVisible(true);
				kntp.setLocationRelativeTo(null);
			}
		});
	}

	private Vector<Vector<String>> hashMapToVector(HashMap<String, PrinterMap> map) {
		Vector<Vector<String>> outVal = new Vector();
		List<String> keys = new ArrayList<>(map.keySet());
		Collections.sort(keys, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		});
		for (String key : keys) {
			Vector<String> row = new Vector<>();
			PrinterMap rc = map.get(key);
			row.add(rc.getKey());
			row.add(rc.getFilePath());
			row.add(rc.getPrinter());
			outVal.add(row);
		} 
		return outVal;
	}

	private String checkPrintersAndDirs() {
		String outVals = "";
		String printers = "";
		String directories = "";
		DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
		Vector<String> ap = new Vector<>();
		for (int ii = 0; ii < services.length; ii++) {
			ap.add(services[ii].getName());
		}
		for (String key : this.map.keySet()) {
			boolean ok = true;
			PrinterMap rc = this.map.get(key);
			if (!ap.contains(rc.getPrinter())) {
				if (!printers.isEmpty()) {
					printers = printers + "\n";
				}
				printers = printers + "  - " + rc.getPrinter() + " (" + key + ")";
				ok = false;
			} 
			if (!(new File(rc.getFilePath())).isDirectory()) {
				if (!directories.isEmpty()) {
					directories = directories + "\n";
				}
				directories = directories + "  - " + rc.getFilePath() + " (" + key + ")";
				ok = false;
			} 
			if (ok) {
				this.validJobs.put(rc.getFilePath(), rc.getPrinter());
			}
		} 
		if (!printers.isEmpty()) {
			outVals = outVals + "- stampanti:\n" + printers;
		}
		if (!directories.isEmpty()) {
			if (!outVals.isEmpty()) {
				outVals = outVals + "\n";
			}
			outVals = outVals + "- cartelle:\n" + directories;
		} 
		return outVals;
	}

	public static String getFileStamp() {
		Calendar timeStamp = new GregorianCalendar();
		Date cTime = new Date(System.currentTimeMillis());
		timeStamp.setTime(cTime);
		String timeStampS = formatNumber(timeStamp.get(5), 2) + "/" + formatNumber(timeStamp.get(2) + 1, 2) + "/" + formatNumber(timeStamp.get(1), 4) + " " + formatNumber(timeStamp.get(11), 2) + ":" + formatNumber(timeStamp.get(12), 2) + ":" + formatNumber(timeStamp.get(13), 2);

		return timeStampS;
	}

	private static String formatNumber(int inVal, int numb) {
		String inValS = "" + inVal;
		while (inValS.length() < numb) {
			inValS = "0" + inValS;
		}
		return inValS;
	}

	private void getWorkingPath() {
		File crf = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		String cJarPath = crf.getParent().replace("%20", " ").replace("build", "");
		if (!cJarPath.endsWith(File.separator)) {
			cJarPath = cJarPath + File.separator;
		}
		workingPath = cJarPath;
		System.out.println(workingPath);
		this.configFile = workingPath + "cnf" + File.separator + "config.xml";
		if (!(new File(this.configFile)).exists()) {
			this.infoTA.append(getFileStamp() + " File di configurazione " + this.configFile + " non esistente");
		}
	}

	public boolean isThereAnotherApplicationInstanceRunningByFileLock() {
		File lockFile = new File("lock");
		try {
			if (!lockFile.exists() && 
					!lockFile.createNewFile()) {
				return true;
			}


			if (lockFile.canRead()) {

				FileOutputStream in = new FileOutputStream(lockFile);

				this.lock = in.getChannel().tryLock();
				return (this.lock == null);
			} 
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Single application instance test error (file lock): " + ex.toString());
			return true;
		} 
	}

	private void writeOnLog(String text) {
		try {
//			this; 
			String logFile = workingPath + "log.txt";
			File lf = new File(logFile);
			if (!lf.exists()) {
				try {
					lf.createNewFile();
					System.out.println(lf.getAbsolutePath());
				} catch (Exception ee) {
					System.out.println(ee.toString());
				} 
			}
			text = getFileStamp() + " " + text + "\n";
			Files.write(lf.toPath(), text.getBytes(), new OpenOption[] { StandardOpenOption.APPEND });
		} catch (IOException e) {
			System.out.println(e.toString());
		} 
	}

	private int printFiles(String directory, String printer) {
		int errors = 0;
		File folder = new File(directory);
		if (folder.isDirectory()) {
			File[] listOfFiles = folder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return filename.endsWith("." + (String)MainPanel.initPars.get("fileExtentionFilter"));
				}
			});
			for (File file : listOfFiles) {
				if (file.isFile()) {
					if (!printPdf(file, printer)) {
						writeOnLog(file.getAbsolutePath() + " PRINT ERROR");
						errors++;
					} else {
						try {
							file.delete();
							writeOnLog(file.getAbsolutePath() + " OK");
						} catch (Exception exc) {
							writeOnLog(file.getAbsolutePath() + " DELETE ERROR");
						} 
					} 
				}
			} 
		} 

		return errors;
	}

	public static boolean printPdf(File pdfFile, String printerName) {
		Boolean correct = Boolean.valueOf(true);

		FileInputStream psStream = null;
		try {
			psStream = new FileInputStream(pdfFile);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			correct = Boolean.valueOf(false);
		} 
		if (psStream != null) {

			DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
			Doc myDoc = new SimpleDoc(psStream, psInFormat, null);
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
			PrintService[] services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);

			PrintService myPrinter = null;
			for (int i = 0; i < services.length; i++) {
				String svcName = services[i].toString();

				if (svcName.contains(printerName)) {
					myPrinter = services[i];

					break;
				} 
			} 
			if (myPrinter != null) {
				DocPrintJob job = myPrinter.createPrintJob();
				try {
					job.print(myDoc, aset);
				} catch (PrintException ex) {
					ex.printStackTrace();
					correct = Boolean.valueOf(false);
				} 
			} else {
				System.out.println("No printer services found");
				correct = Boolean.valueOf(false);
			} 
		} else {
			correct = Boolean.valueOf(false);
		} 

		return correct.booleanValue();
	}

	private void readClientIni() {
		Properties properties = new Properties();
		String iniFile = workingPath + "cnf" + File.separator + "server.ini";
		try {
			properties.load(new FileInputStream(iniFile));
			Enumeration<Object> keys = properties.keys();
			initPars = new Hashtable<>();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String val = properties.getProperty(key);
				initPars.put(key, val);
			}

		} catch (Exception exc) {
			System.out.println(exc.toString());
			showErrorMessage("Impossibile trovare il file " + iniFile + ": l'applicazione non partirà");
			System.exit(-1);
		} 
	}
	
}