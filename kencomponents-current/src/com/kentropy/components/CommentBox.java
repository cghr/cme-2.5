package com.kentropy.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.xoetrope.awt.XButton;
import net.xoetrope.awt.XComboBox;
import net.xoetrope.awt.XLabel;
import net.xoetrope.awt.XPanel;
import net.xoetrope.awt.XScrollPane;
import net.xoetrope.awt.XToolTip;
import net.xoetrope.xui.XPage;
import net.xoetrope.xui.XProjectManager;
import net.xoetrope.xui.data.XBaseModel;
import net.xoetrope.xui.data.XModel;

import com.inet.jortho.SpellChecker;
import com.kentropy.db.TestXUIDB;

public class CommentBox extends XPanel implements MouseListener,
		ActionListener, FocusListener, ItemListener {
	public KeywordPanel1 kp = null;

	Vector physicians = new Vector();

	public Vector getPhysicians() {
		return physicians;
	}

	public void setPhysicians(Vector physicians) {
		this.physicians = physicians;
	}
	
	public CommentBox(XPanel mainPanel, JTextArea commentArea, XButton[] xb) {
		
	
	}
	
	public CommentBox() {
		
	}

	XPage page = null;

	Container parent = null;
	
	private static int BUTTON_WIDTH = 50;
	private static int BUTTON_HEIGHT = 20;

	int currentSelected = -1;
	XScrollPane scr = new XScrollPane();
	//public XPanel mainPanel = null;
	//XPanel listPanel = null;
	//public XPanel editPanel = null;
	private static final int ROWS = 1;
	private static final int COLUMNS = 8;
	private String deleteFunc = null;
	private String saveFunc = null;
	private String cancelFunc = null;
	private String closeFunc = null;
	private String selectFunc = null;
	private String currentPhysician = null;
	private String report = null;
	private String currentStage = null;
	private String keywordStage = null;
	public String getKeywordStage() {
		return keywordStage;
	}

	public void setKeywordStage(String keywordStage) {
		this.keywordStage = keywordStage;
	}

	private JTextArea commentArea = null;
	//private XLabel heading = null;

//	XComboBox heading = null;

	private XButton saveButton = new XButton();
	private XButton editButton = new XButton();
	private XButton closeButton = new XButton();
	private XButton cancelButton = new XButton();
	private XButton deleteButton = new XButton();
//	private XButton hideMainPanelButton = new XButton();
//	private XButton showMainPanelButton = new XButton();
	public String keywordPhysician = null;

	public String getKeywordPhysician() {
		return keywordPhysician;
	}

	public void remove()
	{
		try{
	if(this.getParent()!=null)
	this.getParent().remove(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	//	listPanel.getParent().remove(mainPanel);
	}
	public void setKeywordPhysician(String keywordPhysician) {
		System.out.println("setting physician:"+keywordPhysician);
		this.keywordPhysician = keywordPhysician;
//		String str = "";
//		if (keywordPhysician.equals(currentPhysician)) {
//			str = ("Your Keywords");
//		} else {
//			str = ("Others Keywords");
//		}
//		if (!str.equals(heading.getSelectedItem())) {
//			heading.select(str);
//		}
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}

	public void setCurrentPhysician(String currentPhysician) {
		System.out.println("Setting current physician:"+currentPhysician);
		this.currentPhysician = currentPhysician;
	}

	public String getCurrentPhysician() {
		return this.currentPhysician;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getReport() {
		return this.report;
	}

	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

	public String getCurrentStage() {
		return this.currentStage;
	}

	String[] keywords = { "Key1", "Lorem Ipsium",
			"This is a multiline keyword that spans a paragraph",
			"Lots of Keywords", "Even More Keywords", "Comments gone crazy" };

	XModel cm;

	public void setPage(XPage page) {
		this.page = page;
	}

	private Color getRowColor(int i) {
		if (i % 2 == 0) {
			return new Color(220, 220, 220);
		} else {
			return Color.WHITE;
		}
	}

	

	

	

	String changePhyFunc = null;
	String confirmDelete = null;
	String changeKeywordPrompt = null;

	public void setCallback(String action, String function) {

		if (action.equals("Delete")) {
			deleteFunc = function;
			// pg.addActionHandler(okButton,function);
		}
		if (action.equals("Save")) {
			saveFunc = function;
			// pg.addActionHandler(cancelButton,function);
		}
		if (action.equals("Cancel")) {
			cancelFunc = function;
			// pg.addActionHandler(cancelButton,function);
		}
		if (action.equals("Select")) {
			selectFunc = function;
			// pg.addActionHandler(cancelButton,function);
		}
		if (action.equals("Close")) {
			closeFunc = function;
			// pg.addActionHandler(cancelButton,function);
		}
		if (action.equals("ChangePhy")) {
			changePhyFunc = function;
		}
		if (action.equals("ConfirmDelete")) {
			confirmDelete = function;
		}
		if (action.equals("ChangeKeywordPrompt")) {
			changeKeywordPrompt = function;
		}
	}

	

	Vector items = new Vector();

	public void init() {
		Font buttonFont = new Font("SANS_SERIF", Font.BOLD, 12);
		Color buttonColor = new Color(215, 25, 32);
		//listPanel = new XPanel();

		this.setLayout(null);
//		editPanel = new XPanel();
		// editPanel.setLayout(new BorderLayout());
		// editPanel.setLayout(new FlowLayout());
		this.setBackground(new Color(220, 220, 220));
		this.setBounds(560, 150, 250, 200);
//		editPanel.setAttribute("border", "2");
		// editPanel.setVisible(false);

		commentArea = new JTextArea();
		commentArea.setOpaque(true);
		commentArea.setLineWrap(true);
		commentArea.setWrapStyleWord(true);

		SpellChecker.registerDictionaries(null, "en");
		SpellChecker.register(commentArea);
		

		XLabel commentLabel = new XLabel();
		commentLabel.setText("Keywords:");
		commentLabel.setFont(buttonFont);
		commentLabel.setBounds(3, 3, 150, 20);
		commentLabel.setForeground(Color.BLACK);

		commentArea.setName("commentArea");
		commentArea.setBounds(5, 25, this.getWidth() - 10, 140);
		// commentArea.setBackground(Color.yellow);

		commentArea.setBackground(Color.WHITE);//new Color(225, 225, 250));
		commentArea.setForeground(Color.BLACK);

		saveButton.setName("Save");
		saveButton.setLabel("Save");
		saveButton.setBounds(commentArea.getLocation().x, commentArea.getY()
				+ commentArea.getHeight() + 10, BUTTON_WIDTH, BUTTON_HEIGHT);
		editButton.setName("Edit");
		editButton.setLabel("Edit");
		editButton.setBounds(commentArea.getLocation().x, commentArea.getY()
				+ commentArea.getHeight() + 10, BUTTON_WIDTH, BUTTON_HEIGHT);
		deleteButton.setName("Delete");
		deleteButton.setLabel("Delete");
		deleteButton.setBounds(saveButton.getX() + saveButton.getWidth() + 20,
				saveButton.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);
		closeButton.setName("Close");
		closeButton.setLabel("Close");
		closeButton.setBounds(deleteButton.getX() + deleteButton.getWidth()
				+ 20, deleteButton.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);
		cancelButton.setName("Cancel");
		cancelButton.setLabel("Cancel");
		cancelButton.setBounds(deleteButton.getX() + deleteButton.getWidth()
				+ 20, deleteButton.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);
		cancelButton.setVisible(false);

		saveButton.setBackground(buttonColor);
		saveButton.setForeground(Color.WHITE);
		saveButton.setFont(buttonFont);
		new XToolTip("Click to Save changes", saveButton);
		editButton.setBackground(buttonColor);
		editButton.setForeground(Color.WHITE);
		editButton.setFont(buttonFont);
		new XToolTip("Click to Edit this comment", editButton);
		deleteButton.setBackground(buttonColor);
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setFont(buttonFont);
		new XToolTip("Click to Delete this comment", deleteButton);
		closeButton.setBackground(buttonColor);
		closeButton.setForeground(Color.WHITE);
		closeButton.setFont(buttonFont);
		new XToolTip("Close Comment Box", closeButton);
		cancelButton.setBackground(buttonColor);
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setFont(buttonFont);
		new XToolTip("Cancel Changes", cancelButton);
/*
		hideMainPanelButton.setText("_");// ("hide");
		// hideMainPanelButton.setLabel(">");
		hideMainPanelButton.setBackground(buttonColor);
		hideMainPanelButton.setForeground(Color.WHITE);
		hideMainPanelButton.setFont(buttonFont);
		new XToolTip("Hide Keywords", hideMainPanelButton);
		hideMainPanelButton.setActionCommand("hide");
		hideMainPanelButton.setBounds(130, 5, 20, 20);
		hideMainPanelButton.addActionListener(this);
		hideMainPanelButton.addFocusListener(this);
		showMainPanelButton.setText("\u25A1");
		// showMainPanelButton.setLabel("<");
		showMainPanelButton.setBackground(buttonColor);
		showMainPanelButton.setForeground(Color.WHITE);
		showMainPanelButton.setFont(buttonFont);
		// // showMainPanelButton.addT
		new XToolTip("Show Keywords", showMainPanelButton);
		showMainPanelButton.setActionCommand("show");
		showMainPanelButton.setBounds(130, 5, 20, 20);
		showMainPanelButton.addActionListener(this);
		*/

		saveButton.addActionListener(this);
		editButton.addActionListener(this);
		deleteButton.addActionListener(this);
		closeButton.addActionListener(this);
		cancelButton.addActionListener(this);
		// showMainPanelButton.addFocusListener(this);
		// scr = new XPanel();
		scr.setBounds(0, 20, 150, 180);
	//	listPanel = new XPanel();
		// listPanel.setLayout(new FlowLayout());
	//	listPanel.setBounds(2, 2, 170, 1000);
		// this.setBounds(30, 30, 20, 200);
		FlowLayout flow = new FlowLayout();
		// BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		// panel.setLayout(flow);
	//	scr.add(listPanel);

		this.add(saveButton);
		this.add(editButton);
		this.add(deleteButton);
		this.add(closeButton);
		this.add(cancelButton);
		this.add(commentArea);
		this.add(commentLabel);
		// editPanel.doLayout();

		// this.add(commentArea);
		// this.add(saveButton);
		// this.add(deleteButton);
		// this.add(editButton);
		// this.add(closeButton);
	
		/*
		if(getStage().equals("Coding")) {
			heading.add("Your Keywords");
		} else if(getStage().equals("Reconciliation")) {
			heading.add("Your Keywords");
			heading.add("Others Keywords");
		} else {
			heading.add("First Physician");
			heading.add("Second Physician");
		}
		heading.addItemListener(this);
		*/
		// heading.addActionListener(this);
		// heading.setActionCommand("ChangePhy");
		
		
		// editPanel.setBounds(10, 10, 200, 250);
		// this.add(editPanel);
		// this.add(mainPanel);
		System.out.println(" parent=" + this.getParent());
//		parent.add(editPanel,0);
//		addMainPanel();

//		showCommentPanel(false);
//		this.hideMainPanel();
	}
	
	
	
	public void displayComment(String comment)
	{
		commentArea.setText(comment);
		if(currentPhysician.equals(keywordPhysician) && currentStage.equals(keywordStage)) {
			setMode("showEditable");
		} else {
			setMode("showUnEditable");
		}
	}
	
	public void displayComment(String comment, String phy)
	{
		
	}
	
	
	
	public static void main(String[] args) {
		String[] strArray = { "Hello! This is a very long comment",
				"This is another comment" };

		// TODO Auto-generated method stub
		Frame frame = new Frame("KeywordPanel1");

		XProjectManager.getCurrentProject().setAppFrame(frame);
		// XProjectManager.getCurrentProject().setStartupFile("startup.properties");
		XProjectManager.getCurrentProject().initialise("startup.properties");
		frame.setBounds(10, 10, 800, 600);

		CommentBox kp = new CommentBox();//new XPanel(), new JTextArea(), null);
		kp.setBounds(0, 0, 100, 500);

		XModel xm = new XBaseModel();
		String parentPath = "/cme/01200001_01_02/Coding/Comments/9";
		kp.setCurrentPhysician("9");
		kp.setReport("01200001_01_02");
//		TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", parentPath);

		kp.setParent(frame);
		kp.init();
		//kp.setCommentModel(xm);
		

		// XModel comm1 = new XBaseModel();
		// ((XModel) comm1.get("x")).set("100");
		// ((XModel) comm1.get("y")).set("100");
		// ((XModel) comm1.get("stage")).set("coding");
		// ((XModel) comm1.get("endx")).set("110");
		// ((XModel) comm1.get("endy")).set("110");
		// ((XModel) comm1.get("text")).set("New Comment");
		// kp.addComment(comm1);

		// kp.clearKeywords();

		frame.add(kp);
		frame.repaint();

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}

	public void mouseClicked(MouseEvent arg0) {

		// kp = new KeywordPanel();
		// kp.setBounds(200, 10, 200, 200);
		// XModel xm = new XBaseModel();
		// String parentPath = "/cme/01200001_01_02/Coding/Comments/9";
		// TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", parentPath);
		// kp.setCallback("Delete", deleteFunc);
		// kp.setCallback("Save", saveFunc);
		// kp.setCallback("Cancel", cancelFunc);
		// kp.setCommentModel(xm);
		// kp.setCurrentPhysician("9");
		// kp.setReport("01200001_01_02");
		// kp.setStage("Reconciliation");
		// kp.init();

		// XModel comm1 = new XBaseModel();
		// ((XModel) comm1.get("x")).set("100");
		// ((XModel) comm1.get("y")).set("100");
		// ((XModel) comm1.get("stage")).set("Coding");
		// ((XModel) comm1.get("endx")).set("110");
		// ((XModel) comm1.get("endy")).set("110");
		// ((XModel) comm1.get("text")).set("New Comment");
		// kp.addComment(comm1);

		// kp.setVisible(true);
		// this.add(kp);
	}
	
	public String getText() {
		return commentArea.getText();
	}

	public void setMode(String mode) {
		System.out.println("KeywordPanel1.setMode("+mode+")");
		if (mode.equals("New")) {
			setEditable(true);
			deleteButton.setVisible(false);
		} else if (mode.equals("Edit")) {
			setEditable(true);
		} else if (mode.equals("showEditable")) {
			setEditable(false);
		} else if (mode.equals("showUnEditable")) {
			setEditable(false);
			deleteButton.setVisible(false);
			editButton.setVisible(false);
		}
		showCommentPanel(true);
	}

	public int getLastComment() {
		String maxId = "";
		for (int i = 0; i < cm.getNumChildren(); i++) {

			if (cm.get(i).getId().compareTo(maxId) > 0) {
				maxId = cm.get(i).getId();
			}
		}
		if (maxId.equals("")) {
			return 0;
		}
		return Integer.parseInt(maxId.substring("comments".length()));
	}

	String mode = "Edit";

	

	public void changeKeywordPrompt() {
		
//		mp.setPage(page);
		mp.setAction("OK", "DeleteOK");
		mp.okButton.addActionListener(this);
		mp.setTitle("Enter Keyword");
		mp.setMessage("Please enter a keyword to continue");
		mp.setBounds(600, 200, 300, 100);
		mp.init();
		mp.remove(mp.cancelButton);
		mp.okButton.setBounds((mp.getWidth()-mp.BUTTON_WIDTH)/2,(mp.getHeight() - 3 * mp.BORDER - mp.BUTTON_HEIGHT), mp.BUTTON_WIDTH, mp.BUTTON_HEIGHT);
		// this.add(mp);
//		parent.add(mp);
//		parent.setComponentZOrder(mp, 0);
		mp.setVisible(true);
		
		// mp.setCallback("Ok", deleteFunc);
	}
	
	XModel comment = null;

	public void setComment(XModel comment) {
		this.comment=comment;
		commentArea.setText(((XModel)comment.get("text")).get().toString());
		keywordStage = ((XModel)comment.get("stage")).get().toString();
	}
	
	public XModel getComment() {
		return this.comment;
	}
	
	
	
	public boolean saveComment() throws Exception {
		System.out.println("Inside KeywordPanel1.saveComment()");

		String path = "/cme/" + report + "/Coding/Comments/" + currentPhysician;
		System.out.println("report::"+report);

		String text = commentArea.getText();

		if (text.trim().equals("")) {
			System.out.println("Keyword is empty");
			page.evaluateAttribute("${"+changeKeywordPrompt+"()}");
			return false;
		}
		System.out.println("Keyword is not empty");

		text = text.replaceAll("[^A-Z^a-z^0-9^\\s]", " ");
		((XModel) this.getComment().get("text")).set(text);
		System.out.println("Text::" + text);

		TestXUIDB.getInstance().saveTree(this.getSelectedComment(), "keyvalue",
				path);
		if (saveFunc != null) {
			page.evaluateAttribute("${" + saveFunc + "()}");
		}
		this.showCommentPanel(false);
	
		disableSelect = false;
		this.setEditable(false);
//		this.setVisible(false);
		// showCommentArea(false);
		return true;
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mousePressed(MouseEvent arg0) {
	
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void setEditable(boolean status) {
		System.out.println("KeywordPanel.setEditable("+status+")");
		disableSelect = status;
		saveButton.setVisible(status);
		editButton.setVisible(!status);
		deleteButton.setVisible(!status);
		commentArea.setEditable(status);
		closeButton.setVisible(!status);
		cancelButton.setVisible(status);
//		if(status) {
//			saveButton.resize(BUTTON_WIDTH, BUTTON_HEIGHT);
//			editButton.resize(0,0);
//			deleteButton.resize(BUTTON_WIDTH, BUTTON_HEIGHT);
//			closeButton.resize(0,0);
//			cancelButton.resize(BUTTON_WIDTH, BUTTON_HEIGHT);
//		} else {
//			saveButton.resize(0,0);
//			editButton.resize(BUTTON_WIDTH, BUTTON_HEIGHT);
//			deleteButton.resize(0,0);
//			closeButton.resize(BUTTON_WIDTH, BUTTON_HEIGHT);
//			cancelButton.resize(0,0);
//		}
		// this.repaint();

		// if (status) {
		// btn.setVisible(true);
		// btn1.setVisible(false);
		//
		// closeButton.setVisible(!status);
		//
		// cancelButton.setVisible(status);
		// } else {
		// closeButton.setVisible(true);
		// cancelButton.setVisible(false);
		// // btn.setVisible(false);
		// }
		System.out.println(currentPhysician + " " + keywordPhysician);
		if (currentPhysician.equals(keywordPhysician)) {
			// editButton.setVisible(true);
			// deleteButton.setVisible(true);
		} else {
			editButton.setVisible(false);
			deleteButton.setVisible(false);
		}
		this.repaint();
	}

	
	


	public MessagePanel mp = new MessagePanel();

	public void confirmDelete() {
		mp = new MessagePanel();
//		mp.setPage(page);
		mp.setAction("OK", "DeleteOK");
		mp.okButton.addActionListener(this);
		mp.setTitle("Delete Keyword");
		mp.setMessage("Are you sure you want to delete this comment?");
		mp.init();
		mp.repaint();
		// this.add(mp);
//		parent.add(mp);
//		parent.setComponentZOrder(mp, 0);
//		mp.setVisible(true);
		mp.repaint();
		// mp.setCallback("Ok", deleteFunc);
	}

	public XModel getSelectedComment() {
//		return cm.get(currentSelected);
		return comment;
	}

	boolean disableSelect = false;

	public void deleteComment1() {

		String path = "/cme/" + report + "/Coding/Comments/"
				+ currentPhysician + "/" + getSelectedComment().getId();
		try {
			TestXUIDB.getInstance().deleteKeyValue("keyvalue", path + "/text");
			TestXUIDB.getInstance().deleteKeyValue("keyvalue", path + "/x");
			TestXUIDB.getInstance().deleteKeyValue("keyvalue", path + "/y");
			TestXUIDB.getInstance().deleteKeyValue("keyvalue", path + "/endx");
			TestXUIDB.getInstance().deleteKeyValue("keyvalue", path + "/endy");
			TestXUIDB.getInstance().deleteKeyValue("keyvalue", path + "/stage");

			if (deleteFunc != null) {
				page.evaluateAttribute("${" + deleteFunc + "()}");
			}
			((XBaseModel) cm).removeChild(this.getSelectedComment().getId());
			// this.setVisible(false);
		
			this.setEditable(false);
			// hideViewComment();
			// this.callHotspot();
			// this.refreshScrollPane();
			disableSelect = false;
			showCommentPanel(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showCommentPanel(boolean b) {
		this.repaint();
		this.setVisible(b);
		// if (b) {
		// editPanel.setBounds(610, 100, 200, 250);
		// this.setBounds(600, 90, 400, 270);
		// mainPanel.setBounds(210, -80, mainPanel.getWidth(),
		// mainPanel.getHeight());
		// mainPanel.setBounds(810, 10,
		// mainPanel.getWidth(),mainPanel.getHeight());
		// this.doLayout();
		// }

		// else {
		// editPanel.setVisible(false);
		// editPanel.setBounds();
		// editPanel.setSize(0, 0);
		// this.setBounds(800, 90, 200, 270);
		// mainPanel.setBounds(10, -80, mainPanel.getWidth(),
		// mainPanel.getHeight());
		// this.doLayout();
		// }

		// commentArea.setVisible(b);
		// saveButton.setVisible(b);
		// editButton.setVisible(b);
		// closeButton.setVisible(b);
		// deleteButton.setVisible(b);
	}

	private boolean newComment = false;

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getActionCommand().equals("Save")) {
			try {
				if(kp.saveComment()) {
					newComment = false;
					setEditable(false);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			setEditable(false);
		} else if (arg0.getActionCommand().equals("Edit")) {
			setEditable(true);
		} else if (arg0.getActionCommand().equals("Close")) {
			disableSelect = false;
			page.evaluateAttribute("${" + closeFunc + "()}");
			// this.setVisible(false);
			showCommentPanel(false);
		} else if ((arg0.getActionCommand().equals("Delete"))) {
			System.out.println("Confirm");
			page.evaluateAttribute("${"+confirmDelete+"()}");
//			this.confirmDelete();
		} else if ((arg0.getActionCommand().equals("DeleteOK"))) {
			System.out.println("Deleting Comment");
			try {
				deleteComment1();
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
			// display();
		} else if (arg0.getActionCommand().equals("Cancel")) {
			if (newComment) {
				// deleteComment1();
				((XBaseModel) cm)
						.removeChild(this.getSelectedComment().getId());
			}
			disableSelect = false;
			this.setVisible(false);
			page.evaluateAttribute("${" + cancelFunc + "()}");
		//	display();
			newComment = false;
		} else if (arg0.getActionCommand().equals("hide")) {
			//hideMainPanel();

		} else if (arg0.getActionCommand().equals("show")) {
			//showMainPanel();
		} else if (arg0.getActionCommand().equals("ChangePhy")) {
			String phy = null;
			if (keywordPhysician.equals(physicians.get(0))) {
				phy = (String) physicians.get(1);
			} else if(keywordPhysician.equals(physicians.get(1))){
				phy = (String) physicians.get(0);
			}
			System.out.println("changePhyFunc::" + "${" + changePhyFunc + "("
					+ phy + ")}");
			page.evaluateAttribute("${" + changePhyFunc + "(" + phy + ")}");
		}
	}

	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(" Focus Lost 111");
		// arg0.setSource(null);
	}

	public void reset() {
	//	hideMainPanel();
	}
	
	public void itemStateChanged(ItemEvent arg0) {
	
	}

	public void setTitle(String title) {
		
	}
}
