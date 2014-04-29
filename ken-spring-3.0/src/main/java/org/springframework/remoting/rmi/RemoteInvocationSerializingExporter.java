/*   1:    */ package org.springframework.remoting.rmi;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.ObjectInputStream;
/*   6:    */ import java.io.ObjectOutputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.rmi.RemoteException;
/*   9:    */ import org.springframework.beans.factory.InitializingBean;
/*  10:    */ import org.springframework.remoting.support.RemoteInvocation;
/*  11:    */ import org.springframework.remoting.support.RemoteInvocationBasedExporter;
/*  12:    */ import org.springframework.remoting.support.RemoteInvocationResult;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.ClassUtils;
/*  15:    */ 
/*  16:    */ public abstract class RemoteInvocationSerializingExporter
/*  17:    */   extends RemoteInvocationBasedExporter
/*  18:    */   implements InitializingBean
/*  19:    */ {
/*  20:    */   public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-java-serialized-object";
/*  21: 58 */   private String contentType = "application/x-java-serialized-object";
/*  22: 60 */   private boolean acceptProxyClasses = true;
/*  23:    */   private Object proxy;
/*  24:    */   
/*  25:    */   public void setContentType(String contentType)
/*  26:    */   {
/*  27: 70 */     Assert.notNull(contentType, "'contentType' must not be null");
/*  28: 71 */     this.contentType = contentType;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String getContentType()
/*  32:    */   {
/*  33: 78 */     return this.contentType;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setAcceptProxyClasses(boolean acceptProxyClasses)
/*  37:    */   {
/*  38: 86 */     this.acceptProxyClasses = acceptProxyClasses;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean isAcceptProxyClasses()
/*  42:    */   {
/*  43: 93 */     return this.acceptProxyClasses;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void afterPropertiesSet()
/*  47:    */   {
/*  48: 98 */     prepare();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void prepare()
/*  52:    */   {
/*  53:105 */     this.proxy = getProxyForService();
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected final Object getProxy()
/*  57:    */   {
/*  58:109 */     Assert.notNull(this.proxy, ClassUtils.getShortName(getClass()) + " has not been initialized");
/*  59:110 */     return this.proxy;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected ObjectInputStream createObjectInputStream(InputStream is)
/*  63:    */     throws IOException
/*  64:    */   {
/*  65:122 */     return new CodebaseAwareObjectInputStream(is, getBeanClassLoader(), isAcceptProxyClasses());
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected RemoteInvocation doReadRemoteInvocation(ObjectInputStream ois)
/*  69:    */     throws IOException, ClassNotFoundException
/*  70:    */   {
/*  71:141 */     Object obj = ois.readObject();
/*  72:142 */     if (!(obj instanceof RemoteInvocation)) {
/*  73:143 */       throw new RemoteException("Deserialized object needs to be assignable to type [" + 
/*  74:144 */         RemoteInvocation.class.getName() + "]: " + obj);
/*  75:    */     }
/*  76:146 */     return (RemoteInvocation)obj;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected ObjectOutputStream createObjectOutputStream(OutputStream os)
/*  80:    */     throws IOException
/*  81:    */   {
/*  82:158 */     return new ObjectOutputStream(os);
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected void doWriteRemoteInvocationResult(RemoteInvocationResult result, ObjectOutputStream oos)
/*  86:    */     throws IOException
/*  87:    */   {
/*  88:175 */     oos.writeObject(result);
/*  89:    */   }
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RemoteInvocationSerializingExporter
 * JD-Core Version:    0.7.0.1
 */