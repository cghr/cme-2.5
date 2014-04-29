/*  1:   */ package org.springframework.remoting.jaxrpc;
/*  2:   */ 
/*  3:   */ import javax.xml.namespace.QName;
/*  4:   */ import javax.xml.rpc.soap.SOAPFaultException;
/*  5:   */ import org.springframework.remoting.soap.SoapFaultException;
/*  6:   */ 
/*  7:   */ @Deprecated
/*  8:   */ public class JaxRpcSoapFaultException
/*  9:   */   extends SoapFaultException
/* 10:   */ {
/* 11:   */   public JaxRpcSoapFaultException(SOAPFaultException original)
/* 12:   */   {
/* 13:40 */     super(original.getMessage(), original);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public final SOAPFaultException getOriginalException()
/* 17:   */   {
/* 18:47 */     return (SOAPFaultException)getCause();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getFaultCode()
/* 22:   */   {
/* 23:53 */     return getOriginalException().getFaultCode().toString();
/* 24:   */   }
/* 25:   */   
/* 26:   */   public QName getFaultCodeAsQName()
/* 27:   */   {
/* 28:58 */     return getOriginalException().getFaultCode();
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String getFaultString()
/* 32:   */   {
/* 33:63 */     return getOriginalException().getFaultString();
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getFaultActor()
/* 37:   */   {
/* 38:68 */     return getOriginalException().getFaultActor();
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxrpc.JaxRpcSoapFaultException
 * JD-Core Version:    0.7.0.1
 */