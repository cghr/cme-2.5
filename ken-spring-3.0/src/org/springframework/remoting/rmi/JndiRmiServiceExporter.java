/*   1:    */ package org.springframework.remoting.rmi;
/*   2:    */ 
/*   3:    */ import java.rmi.NoSuchObjectException;
/*   4:    */ import java.rmi.Remote;
/*   5:    */ import java.rmi.RemoteException;
/*   6:    */ import java.util.Properties;
/*   7:    */ import javax.naming.NamingException;
/*   8:    */ import javax.rmi.PortableRemoteObject;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.springframework.beans.factory.DisposableBean;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.jndi.JndiTemplate;
/*  13:    */ 
/*  14:    */ public class JndiRmiServiceExporter
/*  15:    */   extends RmiBasedExporter
/*  16:    */   implements InitializingBean, DisposableBean
/*  17:    */ {
/*  18: 71 */   private JndiTemplate jndiTemplate = new JndiTemplate();
/*  19:    */   private String jndiName;
/*  20:    */   private Remote exportedObject;
/*  21:    */   
/*  22:    */   public void setJndiTemplate(JndiTemplate jndiTemplate)
/*  23:    */   {
/*  24: 84 */     this.jndiTemplate = (jndiTemplate != null ? jndiTemplate : new JndiTemplate());
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setJndiEnvironment(Properties jndiEnvironment)
/*  28:    */   {
/*  29: 93 */     this.jndiTemplate = new JndiTemplate(jndiEnvironment);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setJndiName(String jndiName)
/*  33:    */   {
/*  34:100 */     this.jndiName = jndiName;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void afterPropertiesSet()
/*  38:    */     throws NamingException, RemoteException
/*  39:    */   {
/*  40:105 */     prepare();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void prepare()
/*  44:    */     throws NamingException, RemoteException
/*  45:    */   {
/*  46:114 */     if (this.jndiName == null) {
/*  47:115 */       throw new IllegalArgumentException("Property 'jndiName' is required");
/*  48:    */     }
/*  49:119 */     this.exportedObject = getObjectToExport();
/*  50:120 */     PortableRemoteObject.exportObject(this.exportedObject);
/*  51:    */     
/*  52:122 */     rebind();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void rebind()
/*  56:    */     throws NamingException
/*  57:    */   {
/*  58:131 */     if (this.logger.isInfoEnabled()) {
/*  59:132 */       this.logger.info("Binding RMI service to JNDI location [" + this.jndiName + "]");
/*  60:    */     }
/*  61:134 */     this.jndiTemplate.rebind(this.jndiName, this.exportedObject);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void destroy()
/*  65:    */     throws NamingException, NoSuchObjectException
/*  66:    */   {
/*  67:141 */     if (this.logger.isInfoEnabled()) {
/*  68:142 */       this.logger.info("Unbinding RMI service from JNDI location [" + this.jndiName + "]");
/*  69:    */     }
/*  70:144 */     this.jndiTemplate.unbind(this.jndiName);
/*  71:145 */     PortableRemoteObject.unexportObject(this.exportedObject);
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.JndiRmiServiceExporter
 * JD-Core Version:    0.7.0.1
 */