/*  1:   */ package org.springframework.remoting.jaxws;
/*  2:   */ 
/*  3:   */ import javax.xml.namespace.QName;
/*  4:   */ import javax.xml.soap.SOAPFault;
/*  5:   */ import javax.xml.ws.soap.SOAPFaultException;
/*  6:   */ import org.springframework.remoting.soap.SoapFaultException;
/*  7:   */ 
/*  8:   */ public class JaxWsSoapFaultException
/*  9:   */   extends SoapFaultException
/* 10:   */ {
/* 11:   */   public JaxWsSoapFaultException(SOAPFaultException original)
/* 12:   */   {
/* 13:39 */     super(original.getMessage(), original);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public final SOAPFault getFault()
/* 17:   */   {
/* 18:46 */     return ((SOAPFaultException)getCause()).getFault();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getFaultCode()
/* 22:   */   {
/* 23:52 */     return getFault().getFaultCode();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public QName getFaultCodeAsQName()
/* 27:   */   {
/* 28:57 */     return getFault().getFaultCodeAsQName();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String getFaultString()
/* 32:   */   {
/* 33:62 */     return getFault().getFaultString();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getFaultActor()
/* 37:   */   {
/* 38:67 */     return getFault().getFaultActor();
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxws.JaxWsSoapFaultException
 * JD-Core Version:    0.7.0.1
 */