package org.springframework.mail.javamail;

import javax.mail.internet.MimeMessage;

public abstract interface MimeMessagePreparator
{
  public abstract void prepare(MimeMessage paramMimeMessage)
    throws Exception;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.javamail.MimeMessagePreparator
 * JD-Core Version:    0.7.0.1
 */