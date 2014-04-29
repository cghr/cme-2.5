/*   1:    */ package org.springframework.remoting.caucho;
/*   2:    */ 
/*   3:    */ import com.caucho.hessian.io.AbstractHessianInput;
/*   4:    */ import com.caucho.hessian.io.AbstractHessianOutput;
/*   5:    */ import com.caucho.hessian.io.Hessian2Input;
/*   6:    */ import com.caucho.hessian.io.Hessian2Output;
/*   7:    */ import com.caucho.hessian.io.HessianDebugInputStream;
/*   8:    */ import com.caucho.hessian.io.HessianDebugOutputStream;
/*   9:    */ import com.caucho.hessian.io.HessianInput;
/*  10:    */ import com.caucho.hessian.io.HessianOutput;
/*  11:    */ import com.caucho.hessian.io.SerializerFactory;
/*  12:    */ import com.caucho.hessian.server.HessianSkeleton;
/*  13:    */ import java.io.BufferedInputStream;
/*  14:    */ import java.io.IOException;
/*  15:    */ import java.io.InputStream;
/*  16:    */ import java.io.OutputStream;
/*  17:    */ import java.io.PrintWriter;
/*  18:    */ import org.apache.commons.logging.Log;
/*  19:    */ import org.springframework.beans.factory.InitializingBean;
/*  20:    */ import org.springframework.remoting.support.RemoteExporter;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ import org.springframework.util.CommonsLogWriter;
/*  23:    */ 
/*  24:    */ public class HessianExporter
/*  25:    */   extends RemoteExporter
/*  26:    */   implements InitializingBean
/*  27:    */ {
/*  28:    */   public static final String CONTENT_TYPE_HESSIAN = "application/x-hessian";
/*  29: 61 */   private SerializerFactory serializerFactory = new SerializerFactory();
/*  30:    */   private Log debugLogger;
/*  31:    */   private HessianSkeleton skeleton;
/*  32:    */   
/*  33:    */   public void setSerializerFactory(SerializerFactory serializerFactory)
/*  34:    */   {
/*  35: 75 */     this.serializerFactory = (serializerFactory != null ? serializerFactory : new SerializerFactory());
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setSendCollectionType(boolean sendCollectionType)
/*  39:    */   {
/*  40: 83 */     this.serializerFactory.setSendCollectionType(sendCollectionType);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setDebug(boolean debug)
/*  44:    */   {
/*  45: 92 */     this.debugLogger = (debug ? this.logger : null);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void afterPropertiesSet()
/*  49:    */   {
/*  50: 97 */     prepare();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void prepare()
/*  54:    */   {
/*  55:104 */     checkService();
/*  56:105 */     checkServiceInterface();
/*  57:106 */     this.skeleton = new HessianSkeleton(getProxyForService(), getServiceInterface());
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void invoke(InputStream inputStream, OutputStream outputStream)
/*  61:    */     throws Throwable
/*  62:    */   {
/*  63:117 */     Assert.notNull(this.skeleton, "Hessian exporter has not been initialized");
/*  64:118 */     doInvoke(this.skeleton, inputStream, outputStream);
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected void doInvoke(HessianSkeleton skeleton, InputStream inputStream, OutputStream outputStream)
/*  68:    */     throws Throwable
/*  69:    */   {
/*  70:131 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/*  71:    */     try
/*  72:    */     {
/*  73:133 */       InputStream isToUse = inputStream;
/*  74:134 */       OutputStream osToUse = outputStream;
/*  75:136 */       if ((this.debugLogger != null) && (this.debugLogger.isDebugEnabled()))
/*  76:    */       {
/*  77:137 */         PrintWriter debugWriter = new PrintWriter(new CommonsLogWriter(this.debugLogger));
/*  78:138 */         HessianDebugInputStream dis = new HessianDebugInputStream(inputStream, debugWriter);
/*  79:139 */         dis.startTop2();
/*  80:140 */         HessianDebugOutputStream dos = new HessianDebugOutputStream(outputStream, debugWriter);
/*  81:141 */         dos.startTop2();
/*  82:142 */         isToUse = dis;
/*  83:143 */         osToUse = dos;
/*  84:    */       }
/*  85:146 */       if (!isToUse.markSupported())
/*  86:    */       {
/*  87:147 */         isToUse = new BufferedInputStream(isToUse);
/*  88:148 */         isToUse.mark(1);
/*  89:    */       }
/*  90:151 */       int code = isToUse.read();
/*  91:158 */       if (code == 72)
/*  92:    */       {
/*  93:160 */         int major = isToUse.read();
/*  94:161 */         int minor = isToUse.read();
/*  95:162 */         if (major != 2) {
/*  96:163 */           throw new IOException("Version " + major + "." + minor + " is not understood");
/*  97:    */         }
/*  98:165 */         AbstractHessianInput in = new Hessian2Input(isToUse);
/*  99:166 */         AbstractHessianOutput out = new Hessian2Output(osToUse);
/* 100:167 */         in.readCall();
/* 101:    */       }
/* 102:169 */       else if (code == 67)
/* 103:    */       {
/* 104:171 */         isToUse.reset();
/* 105:172 */         AbstractHessianInput in = new Hessian2Input(isToUse);
/* 106:173 */         AbstractHessianOutput out = new Hessian2Output(osToUse);
/* 107:174 */         in.readCall();
/* 108:    */       }
/* 109:    */       else
/* 110:    */       {
/* 111:    */         AbstractHessianOutput out;
/* 112:176 */         if (code == 99)
/* 113:    */         {
/* 114:178 */           int major = isToUse.read();
/* 115:179 */           int minor = isToUse.read();
/* 116:180 */           AbstractHessianInput in = new HessianInput(isToUse);
/* 117:    */           AbstractHessianOutput out;
/* 118:181 */           if (major >= 2) {
/* 119:182 */             out = new Hessian2Output(osToUse);
/* 120:    */           } else {
/* 121:185 */             out = new HessianOutput(osToUse);
/* 122:    */           }
/* 123:    */         }
/* 124:    */         else
/* 125:    */         {
/* 126:189 */           throw new IOException("Expected 'H'/'C' (Hessian 2.0) or 'c' (Hessian 1.0) in hessian input at " + code);
/* 127:    */         }
/* 128:    */       }
/* 129:    */       AbstractHessianOutput out;
/* 130:    */       AbstractHessianInput in;
/* 131:192 */       if (this.serializerFactory != null)
/* 132:    */       {
/* 133:193 */         in.setSerializerFactory(this.serializerFactory);
/* 134:194 */         out.setSerializerFactory(this.serializerFactory);
/* 135:    */       }
/* 136:    */       try
/* 137:    */       {
/* 138:198 */         skeleton.invoke(in, out);
/* 139:    */       }
/* 140:    */       finally
/* 141:    */       {
/* 142:    */         try
/* 143:    */         {
/* 144:202 */           in.close();
/* 145:203 */           isToUse.close();
/* 146:    */         }
/* 147:    */         catch (IOException localIOException1) {}
/* 148:    */         try
/* 149:    */         {
/* 150:209 */           out.close();
/* 151:210 */           osToUse.close();
/* 152:    */         }
/* 153:    */         catch (IOException localIOException2) {}
/* 154:    */       }
/* 155:    */       try
/* 156:    */       {
/* 157:209 */         out.close();
/* 158:210 */         osToUse.close();
/* 159:    */       }
/* 160:    */       catch (IOException localIOException4) {}
/* 161:    */     }
/* 162:    */     finally
/* 163:    */     {
/* 164:218 */       resetThreadContextClassLoader(originalClassLoader);
/* 165:    */     }
/* 166:    */   }
/* 167:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.caucho.HessianExporter
 * JD-Core Version:    0.7.0.1
 */