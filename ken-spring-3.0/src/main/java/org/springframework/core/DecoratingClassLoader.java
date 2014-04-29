/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.util.HashSet;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public abstract class DecoratingClassLoader
/*   8:    */   extends ClassLoader
/*   9:    */ {
/*  10: 35 */   private final Set<String> excludedPackages = new HashSet();
/*  11: 37 */   private final Set<String> excludedClasses = new HashSet();
/*  12: 39 */   private final Object exclusionMonitor = new Object();
/*  13:    */   
/*  14:    */   public DecoratingClassLoader() {}
/*  15:    */   
/*  16:    */   public DecoratingClassLoader(ClassLoader parent)
/*  17:    */   {
/*  18: 53 */     super(parent);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void excludePackage(String packageName)
/*  22:    */   {
/*  23: 64 */     Assert.notNull(packageName, "Package name must not be null");
/*  24: 65 */     synchronized (this.exclusionMonitor)
/*  25:    */     {
/*  26: 66 */       this.excludedPackages.add(packageName);
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void excludeClass(String className)
/*  31:    */   {
/*  32: 77 */     Assert.notNull(className, "Class name must not be null");
/*  33: 78 */     synchronized (this.exclusionMonitor)
/*  34:    */     {
/*  35: 79 */       this.excludedClasses.add(className);
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected boolean isExcluded(String className)
/*  40:    */   {
/*  41: 93 */     synchronized (this.exclusionMonitor)
/*  42:    */     {
/*  43: 94 */       if (this.excludedClasses.contains(className)) {
/*  44: 95 */         return true;
/*  45:    */       }
/*  46: 97 */       for (String packageName : this.excludedPackages) {
/*  47: 98 */         if (className.startsWith(packageName)) {
/*  48: 99 */           return true;
/*  49:    */         }
/*  50:    */       }
/*  51:    */     }
/*  52:103 */     return false;
/*  53:    */   }
/*  54:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.DecoratingClassLoader
 * JD-Core Version:    0.7.0.1
 */