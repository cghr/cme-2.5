/*  1:   */ package org.springframework.remoting.soap;
/*  2:   */ 
/*  3:   */ import javax.xml.namespace.QName;
/*  4:   */ import org.springframework.remoting.RemoteInvocationFailureException;
/*  5:   */ 
/*  6:   */ public abstract class SoapFaultException
/*  7:   */   extends RemoteInvocationFailureException
/*  8:   */ {
/*  9:   */   protected SoapFaultException(String msg, Throwable cause)
/* 10:   */   {
/* 11:40 */     super(msg, cause);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public abstract String getFaultCode();
/* 15:   */   
/* 16:   */   public abstract QName getFaultCodeAsQName();
/* 17:   */   
/* 18:   */   public abstract String getFaultString();
/* 19:   */   
/* 20:   */   public abstract String getFaultActor();
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.soap.SoapFaultException
 * JD-Core Version:    0.7.0.1
 */