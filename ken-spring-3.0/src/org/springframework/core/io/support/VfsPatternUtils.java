/*  1:   */ package org.springframework.core.io.support;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.lang.reflect.InvocationHandler;
/*  5:   */ import java.lang.reflect.Proxy;
/*  6:   */ import java.net.URL;
/*  7:   */ import org.springframework.core.io.VfsUtils;
/*  8:   */ 
/*  9:   */ abstract class VfsPatternUtils
/* 10:   */   extends VfsUtils
/* 11:   */ {
/* 12:   */   static Object getVisitorAttribute()
/* 13:   */   {
/* 14:36 */     return doGetVisitorAttribute();
/* 15:   */   }
/* 16:   */   
/* 17:   */   static String getPath(Object resource)
/* 18:   */   {
/* 19:40 */     return doGetPath(resource);
/* 20:   */   }
/* 21:   */   
/* 22:   */   static Object findRoot(URL url)
/* 23:   */     throws IOException
/* 24:   */   {
/* 25:44 */     return getRoot(url);
/* 26:   */   }
/* 27:   */   
/* 28:   */   static void visit(Object resource, InvocationHandler visitor)
/* 29:   */     throws IOException
/* 30:   */   {
/* 31:48 */     Object visitorProxy = Proxy.newProxyInstance(VIRTUAL_FILE_VISITOR_INTERFACE.getClassLoader(), 
/* 32:49 */       new Class[] { VIRTUAL_FILE_VISITOR_INTERFACE }, visitor);
/* 33:50 */     invokeVfsMethod(VIRTUAL_FILE_METHOD_VISIT, resource, new Object[] { visitorProxy });
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.VfsPatternUtils
 * JD-Core Version:    0.7.0.1
 */