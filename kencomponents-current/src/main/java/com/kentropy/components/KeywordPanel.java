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

public class KeywordPanel extends JPanel implements MouseListener,
		ActionListener, FocusListener, ItemListener {
	KeywordPanel kp = null;

	Vector physicians = new Vector();

	public Vector getPhysicians() {
		return physicians;
	}

	public void setPhysicians(Vector physicians) {
		this.physicians = physicians;
	}
	
	public KeywordPanel(XPanel mainPanel, JTextArea commentArea, XButton[] xb) {
		this.editPanel = mainPanel;
		this.commentArea = commentArea;
		saveButton = null;
		editButton = null;
		closeButton = null;
		cancelButton = null;
		deleteButton = null;
	}
	
	public KeywordPanel() {
		
	}

	XPage page = null;

	Container parent = null;
	
	private static int BUTTON_WIDTH = 50;
	private static int BUTTON_HEIGHT = 20;

	int currentSelected = -1;
	XScrollPane scr = new XScrollPane();
	public XPanel mainPanel = null;
	XPanel listPanel = null;
	public XPanel editPanel = null;
	private static final int ROWS = 1;
	private static final int COLUMNS = 8;
	private String deleteFunc = null;
	private String saveFunc = null;
	private String cancelFunc = null;
	private String closeFunc = null;
	private String selectFunc = null;
	private String currentPhysician = null;
	private String report = null;
	private String stage = null;
	private JTextArea commentArea = null;
	private XLabel heading = null;

//	XComboBox heading = null;

	private XButton saveButton = null;
	private XButton editButton = null;
	private XButton closeButton = null;
	private XButton cancelButton = null;
	private XButton deleteButton = null;
//	private XButton hideMainPanelButton = new XButton();
//	private XButton showMainPanelButton = new XButton();
	public String keywordPhysician = null;

	public String getKeywordPhysician() {
		return keywordPhysician;
	}

	public void remove()
	{
		try{
		if(mainPanel.getParent()!=null)
		mainPanel.getParent().remove(mainPanel);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try{
	if(editPanel.getParent()!=null)
	editPanel.getParent().remove(editPanel);
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
		String str = "";
		if (keywordPhysician.equals(currentPhysician)) {
			str = ("Your Keywords");
		} else {
			str = ("Others Keywords");
		}
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

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return this.stage;
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

	public void setModel(XModel cm) {
		clearKeywords();
		int currentY = 10;
		for (int i = 0; i < cm.getNumChildren(); i++) {
			System.out.println(">>kp " + cm.get(i).getId());
			JTextArea keywordArea = new JTextArea();
			keywordArea.setLineWrap(true);
			keywordArea.setWrapStyleWord(true);
			String text = ((XModel) cm.get(i).get("text")).get().toString();
			// keywordArea.setColumns(20);
			// keywordArea.setRows(text.length()/20+1);
			keywordArea.setBounds(1, currentY, 125,
					(text.length() / 20 + 1) * 15);
			currentY += (text.length() / 20 + 1) * 15;
			keywordArea.setText(/* "<html><center>"+ */text/*
															 * +
															 * "</center></html>"
															 */);
			keywordArea.setName(cm.get(i).getId());
			keywordArea.addMouseListener(this);
			// if(currentSelected != -1 && i==currentSelected) {
			// keywordArea.setBackground(Color.RED);
			// }
			// else
			keywordArea.setBackground(getRowColor(i));
			// keywordArea.setSize(keywordArea.getPreferredSize());
			keywordArea.setEditable(false);
			// keywordArea.setName(i + "");
			listPanel.add(keywordArea);
			// if (currentPhysician.equals(keywordPhysician)) {
			// heading.select("Your Keywords");
			// } else {
			// heading.select("Others Keywords");
			// }
		}
		scr.doLayout();
		listPanel.repaint();
	}

	public void setCommentModel(XModel cm) {
		this.cm = cm;
		display();
	}

	public void display() {
		/*
		 * XModel xm = new XBaseModel(); for (int i = 0; i <
		 * cm.getNumChildren(); i++) { XModel row = new XBaseModel();
		 * row.setId("" + cm.get(i).getId()); row.setTagName("tr"); XModel cmtM
		 * = (XModel) cm.get(i).get("text"); String val =
		 * cmtM.get().toString().length() > 12 ? cmtM.get()
		 * .toString().substring(0, 12) + "..." : cmtM.get().toString();
		 * ((XModel) row.get(cm.get(i).getId())).set(val); xm.append(row); }
		 * System.out.println(" Children " + xm.getNumChildren());
		 */
		this.setModel(cm);
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

	public void clearKeywords() {
		if(listPanel!=null) {
			Component[] components = listPanel.getComponents();
			
			for (Component c : components) {
				if (c instanceof JTextArea
						&& !((JTextArea) c).getName().equals("commentArea")) {
					listPanel.remove(c);
				}
			}
		}
	}

	Vector items = new Vector();
	public void setCommentActionListeners()
	{saveButton.addActionListener(this);
	editButton.addActionListener(this);
	deleteButton.addActionListener(this);
	closeButton.addActionListener(this);
	cancelButton.addActionListener(this);
		
	}
public void initCommentBox()
{
	Font buttonFont = new Font("SANS_SERIF", Font.BOLD, 12);
	Color buttonColor = new Color(215, 25, 32);
	
	editPanel.setBackground(new Color(220, 220, 220));
	editPanel.setBounds(560, 150, 250, 200);
//	editPanel.setAttribute("border", "2");
	// editPanel.setVisible(false);

//	commentArea = new JTextArea();
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
	editPanel.add(saveButton);
	editPanel.add(editButton);
	editPanel.add(deleteButton);
	editPanel.add(closeButton);
	editPanel.add(cancelButton);
	editPanel.add(commentArea);
	editPanel.add(commentLabel);
	commentArea.setName("commentArea");
	commentArea.setBounds(5, 25, editPanel.getWidth() - 10, 140);
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
	setCommentActionListeners();
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
	
}
	public void init() {
		Font buttonFont = new Font("SANS_SERIF", Font.BOLD, 12);
		Color buttonColor = new Color(215, 25, 32);
		//listPanel = new XPanel();

		this.setLayout(new FlowLayout());

		initCommentBox();
	
		
		System.out.println(" parent=" + this.getParent());
	
	}
	
	public XPanel getMainPanel() {
		return mainPanel;
	}
	
	public XPanel getEditPanel() {
		return editPanel;
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

		KeywordPanel1 kp = new KeywordPanel1(new XPanel(), new JTextArea(), null);
		kp.setBounds(0, 0, 100, 500);

		XModel xm = new XBaseModel();
		String parentPath = "/cme/01200001_01_02/Coding/Comments/9";
		kp.setCurrentPhysician("9");
		kp.setReport("01200001_01_02");
		TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", parentPath);

		kp.setParent(frame);
		kp.init();
		kp.setCommentModel(xm);
		kp.display();

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

	public void selectComment(String comment) {
		System.out.println("CurrentSelected::" + currentSelected);
		if (currentSelected != -1
				&& listPanel.getComponents().length > currentSelected) {
			listPanel.getComponent(currentSelected).setBackground(
					getRowColor(currentSelected));
		}
		setEditable(false);
		for (int i = 0; i < cm.getNumChildren(); i++) {
			if (cm.get(i).getId().equals(comment)) {
				// commentLst.setSelectedRow(i);
				this.currentSelected = i;
				commentArea.setText(((XModel) cm.get(i).get("text")).get()
						.toString());
				listPanel.getComponent(i).setBackground(Color.YELLOW);
				System.out.println("currPhy:" + currentPhysician
						+ " keywordPhy::" + keywordPhysician);
				page.evaluateAttribute("${" + selectFunc + "(" + this.stage
						+ "-" + keywordPhysician + "-" + comment + ")}");

				if(currentPhysician.equals(keywordPhysician))
				{
//					editButton.setVisible(true);
//					deleteButton.setVisible(true);
//					setEditable(true);
					setMode("showEditable");
				}
				else
				{
//					editButton.setVisible(false);
//					deleteButton.setVisible(false);
//					setEditable(false);
					setMode("showUnEditable");
				}
				break;
			}
		}
//		setEditable(false);
		// display();
	}

	public void addComment(XModel cm1) {
		String commId = (getLastComment() + 1) + "";
		cm1.setId("comments" + commId);
		cm.append(cm1);
		display();
		this.selectComment("comments" + commId);
		this.showCommentPanel(true);
		this.setEditable(true);
		newComment = true;
	}

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

	public boolean saveComment() throws Exception {
		System.out.println("Inside KeywordPanel1.saveComment()");

		String path = "/cme/" + report + "/Coding/Comments/" + currentPhysician;

		String text = commentArea.getText();

		if (text.trim().equals("")) {
			System.out.println("Keyword is empty");
			page.evaluateAttribute("${"+changeKeywordPrompt+"()}");
			return false;
		}
		System.out.println("Keyword is not empty");

		text = text.replaceAll("[^A-Z^a-z^0-9^\\s]", " ");
		((XModel) this.getSelectedComment().get("text")).set(text);
		System.out.println("Text::" + text);

		TestXUIDB.getInstance().saveTree(this.getSelectedComment(), "keyvalue",
				path);
		if (saveFunc != null) {
			page.evaluateAttribute("${" + saveFunc + "()}");
		}
		this.showCommentPanel(false);
		display();
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
		if (disableSelect) {
			return;
		}
		commentArea.setText(((JTextArea) arg0.getSource()).getText());
		// currentSelected = Integer.parseInt(((JTextArea) arg0.getSource())
		// .getName());
		System.out.println("TextArea::"
				+ ((JTextArea) arg0.getSource()).getName());
		selectComment(((JTextArea) arg0.getSource()).getName());
		// selectComment(((JTextArea) arg0.getSource()).getText());
		showCommentPanel(true);
//		setEditable(false);
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
		editPanel.repaint();
	}

	public void showMainPanel() {
		// mainPanel.setBounds(810, 90, 185, 500);
		mainPanel.setBounds(315, 90, 150, 209);
		/*
		 * if(editPanel.isVisible()) { //editPanel.setBounds(10, 10, 200, 250);
		 * // this.setBounds(600, 90, 400, 270); // mainPanel.setBounds(210,
		 * -80, mainPanel.getWidth(), //mainPanel.getHeight()); mainPanel. }
		 * else { this.setBounds(800, 90, 200, 270); mainPanel.setBounds(10,
		 * -80, mainPanel.getWidth(), mainPanel.getHeight()); }
		 */
		System.out.println(" Hiding 1");
		// showMainPanelButton.setVisible(false);
		System.out.println(" Hiding 2");
//		this.hideMainPanelButton.setVisible(true);
		// mainPanel.repaint();
		// heading.setActionCommand("hide");
	}

	public void hideMainPanel() {
		// mainPanel.setBounds(810 + hideMainPanelButton.getX(), 90,
		// showMainPanelButton.getWidth() + 10,
		// showMainPanelButton.getHeight() + 10);
		mainPanel.setBounds(315, 90, 150, 30);

		// showMainPanelButton.getWidth() + 10,
		// showMainPanelButton.getHeight() + 10));
		/*
		 * mainPanel .setBounds(mainPanel.getX() + mainPanel.getWidth() -
		 * showMainPanelButton.getWidth()-10, mainPanel.getY(),
		 * showMainPanelButton.getWidth()+10,
		 * showMainPanelButton.getHeight()+10);
		 */
		System.out.println(" Hiding 1");
//		showMainPanelButton.setVisible(true);
		System.out.println(" Hiding 2");
		// heading.setActionCommand("show");
//		this.hideMainPanelButton.setVisible(false);
		mainPanel.doLayout();
		// mainPanel.repaint();

	}

	public void removeMainPanel() {
		parent.remove(mainPanel);
	}

	public void addMainPanel() {
		parent.add(mainPanel, 0);
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
		return cm.get(currentSelected);
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
			display();
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

		editPanel.setVisible(b);
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
				if(this.saveComment()) {
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
			editPanel.setVisible(false);
			page.evaluateAttribute("${" + cancelFunc + "()}");
			display();
			newComment = false;
		} else if (arg0.getActionCommand().equals("hide")) {
			hideMainPanel();

		} else if (arg0.getActionCommand().equals("show")) {
			showMainPanel();
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
		hideMainPanel();
	}
	
	public void itemStateChanged(ItemEvent arg0) {
		XComboBox xc = (XComboBox) arg0.getSource();
		System.out.println("Item::" + arg0.getItem());
		String otherPhysician = physicians.get(0).equals(currentPhysician) ? physicians
				.get(1).toString() : physicians.get(0).toString();
		if (getStage().equals("Adjudication")) {
			if(arg0.getItem().equals("First Physician")) {
				page.evaluateAttribute("${" + changePhyFunc + "("
						+ physicians.get(0) + ")}");
			} else if (arg0.getItem().equals("Second Physician")) {
				page.evaluateAttribute("${" + changePhyFunc + "("
						+ physicians.get(1) + ")}");
			}
		} else {
			if (arg0.getItem().equals("Your Keywords")) {
				page.evaluateAttribute("${" + changePhyFunc + "("
						+ currentPhysician + ")}");
			} else if (arg0.getItem().equals("Others Keywords")) {
				page.evaluateAttribute("${" + changePhyFunc + "("
						+ otherPhysician + ")}");
			}
		}
	}

	public void setTitle(String title) {
		heading.setText(title);
		this.repaint();
	}
}
