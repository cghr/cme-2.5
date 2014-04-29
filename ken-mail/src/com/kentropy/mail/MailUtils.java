package com.kentropy.mail;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kentropy.util.SpringApplicationContext;


public class MailUtils {
	
	static Properties props;
	static String host=null;
	static String user=null;
	static String password=null;
	private static Logger log=Logger.getLogger(MailUtils.class);
	static
	{
		try {
			
			props=(Properties) SpringApplicationContext.getBean("mail");
		   // props=(Properties) new ClassPathXmlApplicationContext("appContext.xml").getBean("mail");
			
			  host=props.getProperty("host");
				 user=props.getProperty("smtpuser");
				 password=props.getProperty("smtppassword");
			
		log.debug("Initializing to send Emails...");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	public boolean sendMail(String from,String to,String subject,String msg) 
	{
	
       try{
		
		Email email=new SimpleEmail();
		email.setHostName("localhost");
		email.setFrom(from);
		email.addTo(to);
		email.setSubject(subject);
		email.setMsg(msg);
		email.send();
		log.info("Sent mail to "+to);
		
       }
    
       catch (Exception e) {
		e.printStackTrace();
	}
		return true;
	}
	public boolean sendHTMLMail(String from,String to,String subject,String msg,String attachmentUrl,String cc) 
	{
		try
		{
		log.debug("==> Inside SendHtmlMail Method");
		HtmlEmail email=new HtmlEmail();
		
		email.setHostName(host);
		email.setFrom(from);
		email.addTo(to);
		email.setSubject(subject);
		email.setHtmlMsg(msg);
		
		
		if(attachmentUrl!=null)
		{
		EmailAttachment attachment=new EmailAttachment();
		attachment.setURL(new URL(attachmentUrl));
		email.attach(attachment);
		}
		if(cc!=null)
		{
			email.addCc(cc);
			log.info("==> Sent cc to "+to);
			
		}
		
		email.send();
		
		log.info("==> Sent mail to "+to);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
public static void main(String[] args) {
	new MailUtils().sendMail("support@kentropy.com","ravi.tej@kentropy.com"," subject"," msg");
}	

}
