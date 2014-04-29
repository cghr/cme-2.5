/*   1:    */ package org.springframework.remoting.httpinvoker;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.ObjectInputStream;
/*   7:    */ import java.io.ObjectOutputStream;
/*   8:    */ import java.io.OutputStream;
/*   9:    */ import java.rmi.RemoteException;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  13:    */ import org.springframework.remoting.rmi.CodebaseAwareObjectInputStream;
/*  14:    */ import org.springframework.remoting.support.RemoteInvocation;
/*  15:    */ import org.springframework.remoting.support.RemoteInvocationResult;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ 
/*  18:    */ public abstract class AbstractHttpInvokerRequestExecutor
/*  19:    */   implements HttpInvokerRequestExecutor, BeanClassLoaderAware
/*  20:    */ {
/*  21:    */   public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-java-serialized-object";
/*  22:    */   protected static final String HTTP_METHOD_POST = "POST";
/*  23:    */   protected static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language";
/*  24:    */   protected static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
/*  25:    */   protected static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
/*  26:    */   protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
/*  27:    */   protected static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
/*  28:    */   protected static final String ENCODING_GZIP = "gzip";
/*  29:    */   private static final int SERIALIZED_INVOCATION_BYTE_ARRAY_INITIAL_SIZE = 1024;
/*  30: 73 */   protected final Log logger = LogFactory.getLog(getClass());
/*  31: 75 */   private String contentType = "application/x-java-serialized-object";
/*  32: 77 */   private boolean acceptGzipEncoding = true;
/*  33:    */   private ClassLoader beanClassLoader;
/*  34:    */   
/*  35:    */   public void setContentType(String contentType)
/*  36:    */   {
/*  37: 87 */     Assert.notNull(contentType, "'contentType' must not be null");
/*  38: 88 */     this.contentType = contentType;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getContentType()
/*  42:    */   {
/*  43: 95 */     return this.contentType;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setAcceptGzipEncoding(boolean acceptGzipEncoding)
/*  47:    */   {
/*  48:105 */     this.acceptGzipEncoding = acceptGzipEncoding;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean isAcceptGzipEncoding()
/*  52:    */   {
/*  53:113 */     return this.acceptGzipEncoding;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  57:    */   {
/*  58:117 */     this.beanClassLoader = classLoader;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected ClassLoader getBeanClassLoader()
/*  62:    */   {
/*  63:124 */     return this.beanClassLoader;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public final RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration config, RemoteInvocation invocation)
/*  67:    */     throws Exception
/*  68:    */   {
/*  69:131 */     ByteArrayOutputStream baos = getByteArrayOutputStream(invocation);
/*  70:132 */     if (this.logger.isDebugEnabled()) {
/*  71:133 */       this.logger.debug("Sending HTTP invoker request for service at [" + config.getServiceUrl() + 
/*  72:134 */         "], with size " + baos.size());
/*  73:    */     }
/*  74:136 */     return doExecuteRequest(config, baos);
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected ByteArrayOutputStream getByteArrayOutputStream(RemoteInvocation invocation)
/*  78:    */     throws IOException
/*  79:    */   {
/*  80:146 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/*  81:147 */     writeRemoteInvocation(invocation, baos);
/*  82:148 */     return baos;
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected void writeRemoteInvocation(RemoteInvocation invocation, OutputStream os)
/*  86:    */     throws IOException
/*  87:    */   {
/*  88:165 */     ObjectOutputStream oos = new ObjectOutputStream(decorateOutputStream(os));
/*  89:    */     try
/*  90:    */     {
/*  91:167 */       doWriteRemoteInvocation(invocation, oos);
/*  92:    */     }
/*  93:    */     finally
/*  94:    */     {
/*  95:170 */       oos.close();
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected OutputStream decorateOutputStream(OutputStream os)
/* 100:    */     throws IOException
/* 101:    */   {
/* 102:183 */     return os;
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected void doWriteRemoteInvocation(RemoteInvocation invocation, ObjectOutputStream oos)
/* 106:    */     throws IOException
/* 107:    */   {
/* 108:198 */     oos.writeObject(invocation);
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected abstract RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration paramHttpInvokerClientConfiguration, ByteArrayOutputStream paramByteArrayOutputStream)
/* 112:    */     throws Exception;
/* 113:    */   
/* 114:    */   protected RemoteInvocationResult readRemoteInvocationResult(InputStream is, String codebaseUrl)
/* 115:    */     throws IOException, ClassNotFoundException
/* 116:    */   {
/* 117:239 */     ObjectInputStream ois = createObjectInputStream(decorateInputStream(is), codebaseUrl);
/* 118:    */     try
/* 119:    */     {
/* 120:241 */       return doReadRemoteInvocationResult(ois);
/* 121:    */     }
/* 122:    */     finally
/* 123:    */     {
/* 124:244 */       ois.close();
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected InputStream decorateInputStream(InputStream is)
/* 129:    */     throws IOException
/* 130:    */   {
/* 131:257 */     return is;
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected ObjectInputStream createObjectInputStream(InputStream is, String codebaseUrl)
/* 135:    */     throws IOException
/* 136:    */   {
/* 137:271 */     return new CodebaseAwareObjectInputStream(is, getBeanClassLoader(), codebaseUrl);
/* 138:    */   }
/* 139:    */   
/* 140:    */   protected RemoteInvocationResult doReadRemoteInvocationResult(ObjectInputStream ois)
/* 141:    */     throws IOException, ClassNotFoundException
/* 142:    */   {
/* 143:290 */     Object obj = ois.readObject();
/* 144:291 */     if (!(obj instanceof RemoteInvocationResult)) {
/* 145:292 */       throw new RemoteException("Deserialized object needs to be assignable to type [" + 
/* 146:293 */         RemoteInvocationResult.class.getName() + "]: " + obj);
/* 147:    */     }
/* 148:295 */     return (RemoteInvocationResult)obj;
/* 149:    */   }
/* 150:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor
 * JD-Core Version:    0.7.0.1
 */