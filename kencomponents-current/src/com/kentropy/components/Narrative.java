package com.kentropy.components;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.im.InputContext;
import java.io.FileReader;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;

import com.kentropy.db.TestXUIDB;
import com.kentropy.model.KenList;

//import com.sun.java.swing.plaf.nimbus.CheckBoxMenuItemPainter;

import net.xoetrope.swing.XButton;
import net.xoetrope.swing.XComboBox;
import net.xoetrope.swing.XLabel;
import net.xoetrope.swing.XPanel;
import net.xoetrope.data.XDataSource;
import net.xoetrope.swing.XCheckbox;
import net.xoetrope.xui.XProjectManager;
import net.xoetrope.xui.data.XBaseModel;
import net.xoetrope.xui.data.XModel;

public class Narrative extends XPanel implements ActionListener, KeyListener,
		HyperlinkListener, MouseListener {
	public String lang="en";
	public String type="adult";
	public String savePath="";
	public XModel rootModel = null;
	JCheckBox[] checkboxArray = null;
	JCheckBox[] selectedCheckbox = null;
	Box symptomCheckList = Box.createVerticalBox();
	JPanel selectionPanel = new JPanel();
	JPanel selectedPanel = new JPanel();
	XPanel listPanel = new XPanel();
	JTextPane narrativeArea = new JTextPane();
	JTextPane noteArea = new JTextPane();
	JLabel guidelinesLabel = new JLabel();
	XLabel narrativePanelHeading = new XLabel();
	XLabel noteHeading = new XLabel();
	XPanel displayPanel = new XPanel();
	XButton nextButton = new XButton();
	XComboBox languageBox = new XComboBox();
	XButton languageButton = new XButton();
	XButton finishButton = new XButton();
	String text1 = "";
	public XModel filterM = new XBaseModel();
	public Vector positive = new Vector();
	public Vector covered = new Vector();
	private static int PANEL_WIDTH = 1000;
	private static int PANEL_HEIGHT = 570;
	XPanel lang1 = new XPanel();
//	List masterList = new List();
//	List selectionList = new List();

	public void init() {
		this.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		this.setLayout(null);
		lang1.setBounds(0, 0, 250, 100);
		lang1.setBackground(Color.red);
		//this.add(lang1);
		selectionPanel.setBounds(0, 0, 220, PANEL_HEIGHT - 50);
		selectionPanel.setBackground(Color.pink);
		// this.initSelectionPanel1();
		this.add(selectionPanel);// , BorderLayout.WEST);
		//nextButton.setVisible(false);
		loadFilters();
		loadSymptoms();

		// selectionPanel.setLayout(new FlowLayout());
		// selectionPanel.setVisible(false);
		// this.setBackground(Color.yellow);
		// language="en";
		// loadFilters();
		// loadSymptoms();
		createLayout();

		this.initLanguageSelector();
		// selectionPanel.setVisible(true);
		// selectionPanel.doLayout();
		// createLayout();
		// actselectionPanel.setVisible(false);
	}

	public void initLanguageSelector() {
		/*languageBox.add("English");
		languageBox.add("Hindi");
		languageBox.add("Kannada");

		languageButton.addActionListener(this);

		lang1.add(languageBox, BorderLayout.WEST);
		languageButton.setText("Select");
		languageButton.setBounds(0, 40, 80, 20);
		lang1.add(languageButton, BorderLayout.WEST);*/
	}

	public void listPanelInit() {
		listPanel.setBounds(0, 0, 350, PANEL_HEIGHT);
		listPanel.setLayout(new FlowLayout());
	}

	public void loadFilters() {

		//filterM = (XModel) rootModel.get("symptomlist_"
			//	+ language.substring(0, 2).toLowerCase());
		filterM = (XModel) rootModel.get("symptomlist_"+type);
		System.out.println(" No of nodes " + filterM.getNumChildren());//
		// + " " + xm.getNumChildren() + " " + xm.get(0).getId() + " "
		// + xm.get(0).getNumChildren());
	}

	Box symptomBox = null;

	public void initSelectionPanel() {

		selectionPanel.setBackground(Color.LIGHT_GRAY);
		// selectionPanel.setBounds(0, 0, 250, PANEL_HEIGHT);
		selectionPanel.setLayout(new FlowLayout());
		XLabel selectionPanelHeading = new XLabel();
		selectionPanelHeading.setText("Positive symptoms");
		selectionPanelHeading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		selectionPanelHeading.setForeground(Color.white);
		selectionPanel.repaint();

		symptomBox = Box.createVerticalBox();
		symptomBox.setBounds(0, 0, 240, 500);
		symptomBox.setBackground(Color.white);
		// symptomBox.setVisible(true);
		// symptomBox.add(selectionPanelHeading);
		selectionPanel.add(selectionPanelHeading);
		// System.out.println("length::"+checkboxArray.length);
		for (int i = 0; i < checkboxArray.length; i++) {
			// checkboxArray[i].setVisible(true);
			// symptomBox.add(checkboxArray[i]);
			selectionPanel.add(checkboxArray[i]);
		}
		nextButton.setLabel("Next");
		nextButton.addActionListener(this);
		// symptomBox.add(nextButton);
		selectionPanel.add(nextButton);
		// selectionPanel.add(symptomBox);
		// this.add(selectionPanel, BorderLayout.WEST);
		// selectionPanel.setVisible(false);
	}

	public void initSelectionPanel1() {

		selectionPanel.setLayout(null);
		selectionPanel.setBackground(new Color(176, 19, 58));
		// selectionPanel.setBounds(0, 0, 250, PANEL_HEIGHT);

		XLabel selectionPanelHeading = new XLabel();
		selectionPanelHeading.setText("Positive symptoms");
		selectionPanelHeading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		selectionPanelHeading.setBounds(0, 0, 150, 20);
		selectionPanelHeading.setForeground(Color.white);
		// symptomBox = Box.createVerticalBox();
		// symptomBox.setBounds(0,0,240,500);
		// symptomBox.setBackground(Color.white);
		// symptomBox.setVisible(true);
		// symptomBox.add(selectionPanelHeading);
		selectionPanel.add(selectionPanelHeading);
		/*
		selectionPanel.add(masterList);
		
		selectionPanel.setVisible(false);
		selectionList.setMultipleMode(true);
//		selectionList.setEnabled(false);
		selectionList.setBounds(20,50,200,210);
		selectionList.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent arg0) {
				int index = (Integer) arg0.getItem();
				
				XModel guidelineModel = (XModel) filterM.get(selectionList.getItem(index));
				showGuidelines(guidelineModel);
				
				if(selectionList.isIndexSelected(index)) {
					selectionList.deselect(index);
				} else {
					selectionList.select(index);
				}
			}
		});
		masterList.setBounds(20,280,200,210);
		masterList.setMultipleMode(true);
		selectionPanel.add(selectionList);
		selectionPanel.add(masterList);
		*/
		nextButton.setLabel("Next");
		nextButton.addActionListener(this);
		// symptomBox.add(nextButton);
		nextButton.setBounds(0, 500, 100, 20);// , arg3);//
		selectionPanel.add(nextButton);
		// selectionPanel.add(symptomBox);
		// this.add(selectionPanel, BorderLayout.WEST);
		// selectionPanel.setVisible(false);
	}
	
	public void initListPanel() {
		listPanel.setBounds(0, 0, 250, 600);
		listPanel.setLayout(null);

		XLabel listPanelHeading = new XLabel();
		listPanelHeading.setText("Positive symptoms");
		listPanelHeading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		listPanelHeading.setSize(listPanel.getWidth(), 15);
		listPanel.add(listPanelHeading);

		// this.add(listPanel, BorderLayout.WEST);
		listPanel.setVisible(false);
	}
public void save()
{
	try {
		System.out.println(narrativeArea.getDocument().getText(0, narrativeArea.getDocument().getLength()));
		TestXUIDB.getInstance().saveKeyValue("resource", savePath+"/narrative", narrativeArea.getDocument().getText(0, narrativeArea.getDocument().getLength()));
		TestXUIDB.getInstance().saveKeyValue("resource", savePath+"/notes", this.noteArea.getDocument().getText(0,noteArea.getDocument().getLength()));
		TestXUIDB.getInstance().saveKeyValue("resource", savePath+"/symptoms", getValues());
		TestXUIDB.getInstance().saveKeyValue("resource", savePath+"/language", lang);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		
		e.printStackTrace();
	}
	//narrative
	//note
	//symptoms
	
}
private String getValues() {
	// TODO Auto-generated method stub
	String str="";
	int count=0;
	for(int i=0; i<checkboxArray.length ;i++)
	{
		if(checkboxArray[i].isSelected())
		{
		str+=(count==0?"":",")+checkboxArray[i].getName();
		count++;
		}
	}
	return str;
}

public void read()
{
	//narrative
	//note
	//symptoms
	
}
	public void initGuidelinesArea() {
		//guidelinesLabel.setBounds(PANEL_WIDTH - 260, 0, 250, PANEL_HEIGHT+200);
		guidelinesLabel.setBounds(0, 0, 290, PANEL_HEIGHT+100);
		guidelinesLabel.setPreferredSize( new Dimension(290, PANEL_HEIGHT+100));
		guidelinesLabel.setVisible(false);
		guidelinesLabel.setVerticalAlignment(JLabel.TOP);
		JScrollPane scroll= new JScrollPane(guidelinesLabel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(PANEL_WIDTH - 300, 10, 290, PANEL_HEIGHT-100);
		
		this.add(scroll, BorderLayout.EAST);
	}
JScrollPane narrativeScrollPane=null;
	public void initNarrativePanel() {
		noteArea.setBounds(230, 30, 450, PANEL_HEIGHT - 430);
		noteArea.getDocument().putProperty(
				DefaultEditorKit.EndOfLineStringProperty, "<br />");
		noteArea.addHyperlinkListener(this);

		noteArea.setVisible(false);
		noteArea.setContentType("text/html");

		noteArea.addKeyListener(this);
		noteArea.setBackground(new Color(245, 245, 245));
		this.add(noteArea);
	
		
		noteHeading.setText("Notes");
		noteHeading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		noteHeading.setBounds(260, 0, 480, 20);
		noteHeading.setVisible(false);
		this.add(noteHeading);
		narrativeScrollPane= new JScrollPane(narrativeArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		narrativeScrollPane.setBounds(230, 230, 450, PANEL_HEIGHT - 300);
		
	//	narrativeArea.setBounds(230, 230, 450, PANEL_HEIGHT - 300);
		narrativeArea.getDocument().putProperty(
				DefaultEditorKit.EndOfLineStringProperty, "<br />");
		narrativeArea.addHyperlinkListener(this);
		narrativeScrollPane.setVisible(false);
		narrativeArea.setVisible(false);
		narrativeArea.setContentType("text/html");

		narrativeArea.addKeyListener(this);
		narrativeArea.setBackground(new Color(245, 245, 245));

		narrativePanelHeading
				.setText("Type the narrative in chronological sequence:");
		narrativePanelHeading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		// narrativePanelHeading.setBounds(listPanel.getWidth(), 0,
		// PANEL_WIDTH-listPanel.getWidth()-300, 20);
		narrativePanelHeading.setBounds(260, 200, 480, 20);
		narrativePanelHeading.setVisible(false);
		this.add(narrativePanelHeading);// , BorderLayout.NORTH);

		// narrativeArea.setBounds(listPanel.getWidth(),
		// narrativePanelHeading.getHeight(), PANEL_WIDTH
		// - listPanel.getWidth() - 300, PANEL_HEIGHT-90);
		//this.add(narrativeArea);// , BorderLayout.CENTER);
		this.add(narrativeScrollPane);
String narrStr=TestXUIDB.getInstance().getValue("resource", savePath+"/narrative");
if(narrStr!=null)
{
	narrativeArea.setText(narrStr);
	
}
String noteStr=TestXUIDB.getInstance().getValue("resource", savePath+"/notes");
if(noteStr!=null)
{
	noteArea.setText(noteStr);

}
		// finishButton.setBounds(listPanel.getWidth(),narrativeArea.getY()+narrativeArea.getHeight(),narrativeArea.getWidth(),20);
		finishButton.setBounds(550,0,100,20);
				//narrativeArea.getY() + narrativeArea.getHeight(),
			//	narrativeArea.getWidth(), 20);
		finishButton.setText("Save");
		finishButton.setVisible(false);
		finishButton.addActionListener(this);
		//selectionPanel.add(finishButton);
		//selectionPanel.validate();
		this.add(finishButton);// , BorderLayout.SOUTH);
		this.validate();
	}

	public void createLayout() {
		initSelectionPanel1();
		// initListPanel();
		initGuidelinesArea();
		initNarrativePanel();
	}

	public void loadSymptoms() {
		// String[] symptomList = { "Symptom1", "Symptom2", "Symptom3",
		// "Symptom4", "Symptom5", "Symptom6", "Symptom7", "Symptom8",
		// "Symptom9" };
		String sympStr=TestXUIDB.getInstance().getValue("resource", savePath+"/symptoms");
		Vector v= new Vector();
		if(sympStr!=null)
		{
String[] symps=sympStr.split(",");
		
		for(int i=0; i<symps.length;i++)
		{
			v.add(symps[i]);
		}
		}
		
		checkboxArray = new JCheckBox[filterM.getNumChildren() - 1];
		System.out.println("filterM Children:" + filterM.getNumChildren());
		int count=0;
		for (int i = 0; i < filterM.getNumChildren(); i++) {
			if (filterM.get(i).getId().equals("keywords")) {
				continue;
			}
			
			System.out.println("filterM " + filterM.get(i).getId());
			System.out.println(filterM.get(i).getId()+"  "+ TestXUIDB.getInstance().getTranslation(filterM.get(i).getId(),lang));
			checkboxArray[count] = new JCheckBox();
			checkboxArray[count].setName(filterM.get(i).getId());
			if(v.contains(filterM.get(i).getId()))
			{
				checkboxArray[count].setSelected(true);
			}
			checkboxArray[count].setText(TestXUIDB.getInstance().getTranslation(filterM.get(i).getId(),lang));
			//checkboxArray[i].setText(filterM.get(i).getId());
			checkboxArray[count].addMouseListener(this);
			// symptomBox.add(checkboxArray[i]);
			checkboxArray[count].setBounds(10, 20 * count + 40, 200, 20);
			selectionPanel.add(checkboxArray[count]);
			count++;
			/*
			masterList.add(filterM.get(i).getId());
			*/
		}
		selectionPanel.setVisible(true);
		selectionPanel.repaint();
		
	}
	
	public void addSymptoms(JCheckBox[] checkboxArray) {
		int count=0;
		selectionPanel.removeAll();
		for (int i = 0; i < checkboxArray.length; i++) {
			if(checkboxArray[i].isSelected()) {
				checkboxArray[i].setBounds(10, 20 * count + 40, 200, 20);
				selectionPanel.add(checkboxArray[i]);
				count++;
			}
		}
		
		for (int i = 0; i < checkboxArray.length; i++) {
			if(!checkboxArray[i].isSelected()) {
				checkboxArray[i].setBounds(10, 20 * count + 40, 200, 20);
				selectionPanel.add(checkboxArray[i]);
				count++;
			}
		}
		selectionPanel.repaint();
		selectionPanel.validate();
		
	}
	

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == nextButton) {
			nextButton.setVisible(false);
			/*
			for(int i=0;i<masterList.getItemCount();i++) {
				if(masterList.isIndexSelected(i)) {
					selectionList.add(masterList.getItem(i));
					masterList.remove(i);
				}
			}
			*/
			
			for (int i = 0; i < checkboxArray.length; i++) {
				if (checkboxArray[i].isSelected()) {
					// positive.add(checkboxArray[i].getLabel());
					// checkboxArray[i].setState(true);
					// checkboxArray[i].setEnabled(false);
					checkboxArray[i].setForeground(new Color(176, 19, 58));
					// checkboxArray[i].setF
					// checkboxArray[i].set
					// symptomCheckList.add(checkboxArray[i]);
				} else {
					checkboxArray[i].setEnabled(true);
					// checkboxArray[i].setForeground(Color.bro);
				}
			}
			
			addSymptoms(checkboxArray);
			
			// listPanel.add(symptomCheckList);
			// selectionPanel.setVisible(false);
			// listPanel.setVisible(true);
			guidelinesLabel.setVisible(true);
			showNarrativePanel();
		} else if (ae.getSource() == finishButton) {
			if (!validateFinish()) {
				JOptionPane.showMessageDialog((Frame) this.getParent(),
						"Narrative does not cover all the symptoms.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				save();
				this.setVisible(false);
				((Frame)this.getParent()).dispose();
				
			}
		} else if (ae.getSource() == languageButton) {
			// selectionPanel.setAttribute("border", "1");

			language = (String)languageBox.getSelectedItem();
			// languageBox.setVisible(false);
			// languageButton.setVisible(false);
			loadFilters();
			loadSymptoms();
			// createLayout();
			// selectionPanel.setVisible(true);
			lang1.setVisible(false);
			nextButton.setVisible(true);

			// initSelectionPanel();

			System.out.println("child comps:"
					+ selectionPanel.getComponentCount());
			// selectionPanel.doLayout();
		}
	}

	public String language = "";

	public void showNarrativePanel() {
		noteArea.setVisible(true);
		noteHeading.setVisible(true);
		narrativeScrollPane.setVisible(true);
		narrativeArea.setVisible(true);
		narrativePanelHeading.setVisible(true);
		finishButton.setVisible(true);
		replace(noteArea);
		replace(narrativeArea);
		this.validate();
	}

	public boolean validateFinish() {
		Component[] cArray = symptomCheckList.getComponents();
		for (int i = 0; i < cArray.length; i++) {
			if (cArray[i] instanceof Checkbox) {
				if (!((Checkbox) cArray[i]).getState()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Frame frame = new Frame("Narrative");

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				Frame frame = (Frame) arg0.getSource();
				frame.dispose();
			}
		});
		XDataSource ds = new XDataSource();
		ds.read(new FileReader("datasets.xml"));

		XModel xm = XProjectManager.getModel();

		XProjectManager.getCurrentProject().setAppFrame(frame);
		
		Narrative narrative = new Narrative();
		narrative.setAutoscrolls(true);
		narrative.rootModel = xm;
		narrative.lang=args[0];
//JScrollPane scroll=new JScrollPane(narrative,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//sroll.setPreferredSize()
narrative.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
//narrative.setSize(800,700);
//scroll.setSize(PANEL_WIDTH,PANEL_HEIGHT);
//scroll.add(narrative);
		frame.add(narrative);
narrative.type="adult";
		frame.setVisible(true);
		narrative.init();
		frame.setSize(1000, 600);
		frame.validate();
	}

	public void keyPressed(KeyEvent arg0) {
		System.out.println(arg0);
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE||arg0.getKeyCode() == KeyEvent.VK_F1) {
			narrativeArea.setEditable(false);
			replace(narrativeArea);
			noteArea.setEditable(false);
			replace(noteArea);
			System.out.println("Escape pressed");
		}
		/*
		 * if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
		 * System.out.println("Enter pressed"); if(!narrativeArea.isEditable())
		 * { narrativeArea.setEditable(true); } } if (arg0.getKeyCode() ==
		 * KeyEvent.VK_BACK_SPACE) { System.out.println("Backspace pressed");
		 * if(!narrativeArea.isEditable()) { narrativeArea.setEditable(true); }
		 * } if (arg0.isActionKey()) { System.out.println("Enter pressed");
		 * if(!narrativeArea.isEditable()) { narrativeArea.setEditable(true); }
		 * }
		 */
		else {
			System.out.println("Enter pressed");
			if (!narrativeArea.isEditable()) {
				narrativeArea.setEditable(true);
			}
			if (!noteArea.isEditable()) {
				noteArea.setEditable(true);
			}
		}

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void replace(JTextPane narrativeArea) {

		String text = "";// narrativeArea.getText();
		try {
			// narrativeArea.getDocument().putProperty(JEditorPane., arg1)
			text = narrativeArea.getDocument().getText(0,
					narrativeArea.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		System.out.println(text);
		String highlightText = "test";
		XModel keyM = ((XModel) filterM.get("keywords"));
		/*
		 * for (int i = 0; i < keyM.getNumChildren(); i++) { highlightText =
		 * keyM.get(i).getId(); if (text.contains(highlightText)) {
		 * System.out.println("match found");
		 * System.out.println("guideline::"+keyM.get(i).getAttribValue(2)); text
		 * = text.replace("<a href=\"" + keyM.get(i).getAttribValue(2) + "\">" +
		 * highlightText + "</a>", highlightText);
		 * 
		 * } }
		 */

		// text = text;

		System.out.println(" ----" + text + "---");
		// clearSelection();
		for (int i = 0; i < keyM.getNumChildren(); i++) {
			highlightText = keyM.get(i).getId();
			highlightText=TestXUIDB.getInstance().getTranslation(highlightText,lang);
			System.out.println(" HIlight "+highlightText);
			int guideline=Integer.parseInt(keyM.get(i).get("@guideline").toString());
			System.out.println(" Guideline "+guideline);
			if (text.toLowerCase().contains(highlightText)) {
		//		String symptom=keyM.get(i).getAttribValueAsInt(2))+"";
				//addSymptom(filterM.get(keyM.get(i).getAttribValueAsInt(2))
					//	.getId());
				checkboxArray[guideline].setSelected(true);
				System.out.println("match found");
				text = text.replaceAll("(?i)" + highlightText,
						"<a href=\"" + keyM.get(i).getAttribValue(2) + "\">"
								+ highlightText.toUpperCase() + "</a>");
			}
		}
		text = text.trim();
		text = text.replace("-", "<br/>-");
		text = text.replace("\n", "<br/>-");
		narrativeArea.setText(text);// .startsWith("-")?text:("-"+text));
		// narrativeArea.getDocument().
		addSymptoms(checkboxArray);
	}

	public void clearSelection() {
		Component[] cArray = checkboxArray;// symptomCheckList.getComponents();
		for (int i = 0; i < cArray.length; i++) {
			if (cArray[i] instanceof Checkbox
					&& cArray[i].getForeground().equals(new Color(176, 19, 58))) {
				((Checkbox) cArray[i]).setState(false);
			}
		}
	}
	/*
	private int getPositiveIndex(String str) {
		for(int i=0;i<selectionList.getItemCount();i++) {
			if(selectionList.getItem(i).equals(str)) {
				return i;
			}
		}
		return -1;
	}
	
	private int getMasterIndex(String str) {
		for(int i=0;i<masterList.getItemCount();i++) {
			if(masterList.getItem(i).equals(str)) {
				return i;
			}
		}
		return -1;
	}
	*/
	public void addSymptom(String symptom) {
		/*
		int index = getMasterIndex(symptom);
		
		if(index != -1) {
			selectionList.add(masterList.getItem(index));
			masterList.remove(index);
		}
		
		index = getPositiveIndex(symptom);
		selectionList.select(index);
		*/
		
		System.out.println(" Symtom to add "+symptom);
		for (int j = 0; j < checkboxArray.length; j++) {
			System.out.println(" Name "+j+" "+checkboxArray[j].getName());
			if (checkboxArray[j].getName().equals(symptom)) {
				// symptomCheckList.add(checkboxArray[j]);
				checkboxArray[j].setSelected(true);
				// checkboxArray[j].setEnabled(false);
//				checkboxArray[j].setForeground(Color.BLACK);
				// listPanel.doLayout();
				break;
			}
		}
		
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void keyTyped1(KeyEvent arg0) {
		String text = narrativeArea.getText();
		String highlightText = "test";

		if (text.contains(highlightText)) {
			System.out.println("Match found");
			Pattern p = Pattern.compile("<a href.+" + highlightText + "</a>");
			Pattern p1 = Pattern.compile(highlightText);
			Matcher m = p.matcher(text);
			text = m.replaceAll(highlightText);

			Matcher m1 = p.matcher(text);
			System.out.println("Match:" + m1.matches());
			text = m1.replaceAll("<a href=\"" + highlightText + "\">"
					+ highlightText + "</a>");
			System.out.println(text);
			narrativeArea.setText(text);
		}
	}

	public void showGuidelines(XModel guidelineModel) {
		guidelinesLabel.setText("");

		System.out.println("Childern::" + guidelineModel.getNumChildren());
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<html><body>");
		strBuf.append("<h2>" + TestXUIDB.getInstance().getTranslation(guidelineModel.getId(),lang) + "</h2>");
		strBuf.append("<ul>");
		for (int i = 0; i < guidelineModel.getNumChildren(); i++) {
			String q=guidelineModel.get(i).get("@question").toString();
			q=TestXUIDB.getInstance().getTranslation(q,lang);
			strBuf.append("<li>"
					+ q
					+ "</li>");
		}
		strBuf.append("</ul></body></html>");
		guidelinesLabel.setText(strBuf.toString());
	}

	public void hyperlinkUpdate(HyperlinkEvent arg0) {
		// AttributeSet atts = arg0.getSourceElement().getAttributes();
		// Enumeration<?> e = atts.getAttributeNames();
		// System.out.println("Hyperlink clicked"
		// + atts.getAttribute(e.nextElement()));
		XModel guidelineModel = filterM.get(Integer.parseInt(arg0
				.getDescription()));
		showGuidelines(guidelineModel);

	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		arg0.consume();
	}

	public void mouseEntered(MouseEvent arg0) {
		System.out.println("Mouse entered");
		if (arg0.getSource() instanceof JCheckBox) {
			String symptom = ((JCheckBox) arg0.getSource()).getName();
			try {
				//symptom=TestXUIDB.getInstance().getTranslation1(symptom, lang);
				XModel guidelineModel = (XModel) filterM.get(symptom);
				showGuidelines(guidelineModel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
