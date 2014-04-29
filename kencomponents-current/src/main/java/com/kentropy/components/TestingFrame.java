package com.kentropy.components;

import java.awt.Frame;

import com.kentropy.db.TestXUIDB;

import net.xoetrope.awt.XPanel;
import net.xoetrope.xui.XProjectManager;
import net.xoetrope.xui.data.XBaseModel;
import net.xoetrope.xui.data.XModel;

public class TestingFrame extends XPanel {
	
	TestingFrame() {
		Frame frame= new Frame("test");

		XProjectManager.getCurrentProject().setAppFrame(frame);
		//XProjectManager.getCurrentProject().setStartupFile("startup.properties");
		XProjectManager.getCurrentProject().initialise("startup.properties");
				frame.setBounds(10,10,800,600);
				KeywordPanel kp= new KeywordPanel();
				kp.setBounds(10,10,500,300);
				XModel xm= new XBaseModel();
				String parentPath="/cme/01200001_01_02/Coding/Comments/9";
				TestXUIDB.getInstance().getKeyValues(xm, "keyvalue", parentPath);
			
				kp.init();
				System.out.println(xm.get(0).getNumChildren());
				System.out.println(xm.get(1).getId());
				frame.add(kp);
			kp.setCommentModel(xm);
				
				frame.setVisible(true);
			}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestingFrame testingFrame = new TestingFrame();
	}

}
