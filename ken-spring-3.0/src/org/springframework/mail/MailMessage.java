package org.springframework.mail;

import java.util.Date;

public abstract interface MailMessage
{
  public abstract void setFrom(String paramString)
    throws MailParseException;
  
  public abstract void setReplyTo(String paramString)
    throws MailParseException;
  
  public abstract void setTo(String paramString)
    throws MailParseException;
  
  public abstract void setTo(String[] paramArrayOfString)
    throws MailParseException;
  
  public abstract void setCc(String paramString)
    throws MailParseException;
  
  public abstract void setCc(String[] paramArrayOfString)
    throws MailParseException;
  
  public abstract void setBcc(String paramString)
    throws MailParseException;
  
  public abstract void setBcc(String[] paramArrayOfString)
    throws MailParseException;
  
  public abstract void setSentDate(Date paramDate)
    throws MailParseException;
  
  public abstract void setSubject(String paramString)
    throws MailParseException;
  
  public abstract void setText(String paramString)
    throws MailParseException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.MailMessage
 * JD-Core Version:    0.7.0.1
 */