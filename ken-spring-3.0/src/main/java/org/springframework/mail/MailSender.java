package org.springframework.mail;

public abstract interface MailSender
{
  public abstract void send(SimpleMailMessage paramSimpleMailMessage)
    throws MailException;
  
  public abstract void send(SimpleMailMessage[] paramArrayOfSimpleMailMessage)
    throws MailException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.MailSender
 * JD-Core Version:    0.7.0.1
 */