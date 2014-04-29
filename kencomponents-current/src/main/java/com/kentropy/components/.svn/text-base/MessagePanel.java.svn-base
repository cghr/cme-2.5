package com.kentropy.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.xoetrope.awt.XButton;
import net.xoetrope.awt.XLabel;
import net.xoetrope.awt.XPanel;
import net.xoetrope.awt.XTextArea;
import net.xoetrope.xui.XPage;
import net.xoetrope.xui.XProjectManager;

public class MessagePanel extends XPanel implements ActionListener {

	XLabel titleLabel = new XLabel();
	public XButton okButton = new XButton();
	public XButton cancelButton = new XButton();
	XLabel textLabel = new XLabel();
//	XPage page = null;

	public static final int HEADING = 15;
	public static final int BORDER = 3;
	public static final int BUTTON_HEIGHT = 20;
	public static final int BUTTON_WIDTH = 80;

	public void init() {
		this.setBackground(Color.BLUE);
		titleLabel.setBounds(BORDER, BORDER, this.getWidth() - (2 * BORDER),
				HEADING);
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
		titleLabel.setForeground(Color.WHITE);

		textLabel.setBounds(BORDER, (BORDER * 2) + HEADING, this.getWidth()
				- (2 * BORDER), this.getHeight() - (3 * BORDER) - HEADING);
		textLabel.setBackground(new Color(240, 240, 240));

		okButton.setName("OK");
		okButton.setLabel("OK");
		okButton.setBounds((this.getWidth() - 2 * BUTTON_WIDTH) / 3,
				(this.getHeight() - 3 * BORDER - BUTTON_HEIGHT), BUTTON_WIDTH,
				BUTTON_HEIGHT);
//		okButton.addActionListener(this);

		cancelButton.setName("Cancel");
		cancelButton.setLabel("Cancel");
		cancelButton.setBounds(2 * ((this.getWidth() - 2 * BUTTON_WIDTH) / 3)
				+ BUTTON_WIDTH,
				(this.getHeight() - 3 * BORDER - BUTTON_HEIGHT), BUTTON_WIDTH,
				BUTTON_HEIGHT);
		cancelButton.addActionListener(this);
		int x = (this.getWidth() - 2 * BUTTON_WIDTH) / 3;

		this.add(titleLabel);
		this.add(okButton);
		this.add(cancelButton);
		this.add(textLabel);
	}

//	public XPage getPage() {
//		return page;
//	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	public void setMessage(String msg) {
		textLabel.setText(msg);
	}

//	public void setPage(XPage page) {
//		this.page = page;
//	}

	public void setAction(String action, String cmd) {
		if (action.equals("OK")) {
			okButton.setActionCommand(cmd);
			// page.addActionHandler(okButton,function);
		}

	}
	
	private String okFunc;

	public void setCallback(String action, String function) {
		if (action.equals("OK")) {
			okFunc = function;
		}
	}

	public void setCallback(String action, ActionListener al) {
		if (action.equals("OK")) {
			okButton.addActionListener(al);// ,function);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Frame frame = new Frame("test");
		frame.setLayout(new FlowLayout());

		XProjectManager.getCurrentProject().setAppFrame(frame);
		// XProjectManager.getCurrentProject().setStartupFile("startup.properties");
		XProjectManager.getCurrentProject().initialise("startup.properties");
		frame.setBounds(10, 10, 800, 600);
		MessagePanel mp = new MessagePanel();
		mp.setBounds(10, 10, 300, 200);
		mp.init();
		frame.add(mp);
		// kp.selectComment("comments3");
		mp.setTitle("Delete Comment");
		mp.setMessage("Are you sure you want to Delete this comment?");
		mp.init();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent w) {
				System.exit((0));
			}
		});
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		((XButton) arg0.getSource()).getParent().setVisible(false);
	}
}
