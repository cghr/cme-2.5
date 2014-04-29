/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.BeansException;
/*   4:    */ import org.springframework.context.ApplicationContext;
/*   5:    */ import org.springframework.core.io.FileSystemResource;
/*   6:    */ import org.springframework.core.io.Resource;
/*   7:    */ 
/*   8:    */ public class FileSystemXmlApplicationContext
/*   9:    */   extends AbstractXmlApplicationContext
/*  10:    */ {
/*  11:    */   public FileSystemXmlApplicationContext() {}
/*  12:    */   
/*  13:    */   public FileSystemXmlApplicationContext(ApplicationContext parent)
/*  14:    */   {
/*  15: 74 */     super(parent);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public FileSystemXmlApplicationContext(String configLocation)
/*  19:    */     throws BeansException
/*  20:    */   {
/*  21: 84 */     this(new String[] { configLocation }, true, null);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public FileSystemXmlApplicationContext(String... configLocations)
/*  25:    */     throws BeansException
/*  26:    */   {
/*  27: 94 */     this(configLocations, true, null);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public FileSystemXmlApplicationContext(String[] configLocations, ApplicationContext parent)
/*  31:    */     throws BeansException
/*  32:    */   {
/*  33:106 */     this(configLocations, true, parent);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public FileSystemXmlApplicationContext(String[] configLocations, boolean refresh)
/*  37:    */     throws BeansException
/*  38:    */   {
/*  39:120 */     this(configLocations, refresh, null);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public FileSystemXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
/*  43:    */     throws BeansException
/*  44:    */   {
/*  45:137 */     super(parent);
/*  46:138 */     setConfigLocations(configLocations);
/*  47:139 */     if (refresh) {
/*  48:140 */       refresh();
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected Resource getResourceByPath(String path)
/*  53:    */   {
/*  54:156 */     if ((path != null) && (path.startsWith("/"))) {
/*  55:157 */       path = path.substring(1);
/*  56:    */     }
/*  57:159 */     return new FileSystemResource(path);
/*  58:    */   }
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.FileSystemXmlApplicationContext
 * JD-Core Version:    0.7.0.1
 */