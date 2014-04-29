/*  1:   */ package org.springframework.remoting.caucho;
/*  2:   */ 
/*  3:   */ import com.caucho.burlap.io.BurlapInput;
/*  4:   */ import com.caucho.burlap.io.BurlapOutput;
/*  5:   */ import com.caucho.burlap.server.BurlapSkeleton;
/*  6:   */ import java.io.IOException;
/*  7:   */ import java.io.InputStream;
/*  8:   */ import java.io.OutputStream;
/*  9:   */ import org.springframework.beans.factory.InitializingBean;
/* 10:   */ import org.springframework.remoting.support.RemoteExporter;
/* 11:   */ import org.springframework.util.Assert;
/* 12:   */ 
/* 13:   */ public class BurlapExporter
/* 14:   */   extends RemoteExporter
/* 15:   */   implements InitializingBean
/* 16:   */ {
/* 17:   */   private BurlapSkeleton skeleton;
/* 18:   */   
/* 19:   */   public void afterPropertiesSet()
/* 20:   */   {
/* 21:51 */     prepare();
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void prepare()
/* 25:   */   {
/* 26:58 */     checkService();
/* 27:59 */     checkServiceInterface();
/* 28:60 */     this.skeleton = new BurlapSkeleton(getProxyForService(), getServiceInterface());
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void invoke(InputStream inputStream, OutputStream outputStream)
/* 32:   */     throws Throwable
/* 33:   */   {
/* 34:71 */     Assert.notNull(this.skeleton, "Burlap exporter has not been initialized");
/* 35:72 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader();
/* 36:   */     try
/* 37:   */     {
/* 38:74 */       this.skeleton.invoke(new BurlapInput(inputStream), new BurlapOutput(outputStream));
/* 39:   */     }
/* 40:   */     finally
/* 41:   */     {
/* 42:   */       try
/* 43:   */       {
/* 44:78 */         inputStream.close();
/* 45:   */       }
/* 46:   */       catch (IOException localIOException1) {}
/* 47:   */       try
/* 48:   */       {
/* 49:84 */         outputStream.close();
/* 50:   */       }
/* 51:   */       catch (IOException localIOException2) {}
/* 52:89 */       resetThreadContextClassLoader(originalClassLoader);
/* 53:   */     }
/* 54:   */   }
/* 55:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.caucho.BurlapExporter
 * JD-Core Version:    0.7.0.1
 */