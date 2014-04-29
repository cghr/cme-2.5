/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ 
/*   5:    */ public class CustomizableThreadCreator
/*   6:    */   implements Serializable
/*   7:    */ {
/*   8:    */   private String threadNamePrefix;
/*   9: 36 */   private int threadPriority = 5;
/*  10: 38 */   private boolean daemon = false;
/*  11:    */   private ThreadGroup threadGroup;
/*  12: 42 */   private int threadCount = 0;
/*  13: 44 */   private final Object threadCountMonitor = new SerializableMonitor(null);
/*  14:    */   
/*  15:    */   public CustomizableThreadCreator()
/*  16:    */   {
/*  17: 51 */     this.threadNamePrefix = getDefaultThreadNamePrefix();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public CustomizableThreadCreator(String threadNamePrefix)
/*  21:    */   {
/*  22: 59 */     this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setThreadNamePrefix(String threadNamePrefix)
/*  26:    */   {
/*  27: 68 */     this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
/*  28:    */   }
/*  29:    */   
/*  30:    */   public String getThreadNamePrefix()
/*  31:    */   {
/*  32: 76 */     return this.threadNamePrefix;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setThreadPriority(int threadPriority)
/*  36:    */   {
/*  37: 85 */     this.threadPriority = threadPriority;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int getThreadPriority()
/*  41:    */   {
/*  42: 92 */     return this.threadPriority;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setDaemon(boolean daemon)
/*  46:    */   {
/*  47:106 */     this.daemon = daemon;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean isDaemon()
/*  51:    */   {
/*  52:113 */     return this.daemon;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setThreadGroupName(String name)
/*  56:    */   {
/*  57:121 */     this.threadGroup = new ThreadGroup(name);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setThreadGroup(ThreadGroup threadGroup)
/*  61:    */   {
/*  62:129 */     this.threadGroup = threadGroup;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public ThreadGroup getThreadGroup()
/*  66:    */   {
/*  67:137 */     return this.threadGroup;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Thread createThread(Runnable runnable)
/*  71:    */   {
/*  72:149 */     Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
/*  73:150 */     thread.setPriority(getThreadPriority());
/*  74:151 */     thread.setDaemon(isDaemon());
/*  75:152 */     return thread;
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected String nextThreadName()
/*  79:    */   {
/*  80:163 */     int threadNumber = 0;
/*  81:164 */     synchronized (this.threadCountMonitor)
/*  82:    */     {
/*  83:165 */       this.threadCount += 1;
/*  84:166 */       threadNumber = this.threadCount;
/*  85:    */     }
/*  86:168 */     return getThreadNamePrefix() + threadNumber;
/*  87:    */   }
/*  88:    */   
/*  89:    */   protected String getDefaultThreadNamePrefix()
/*  90:    */   {
/*  91:176 */     return ClassUtils.getShortName(getClass()) + "-";
/*  92:    */   }
/*  93:    */   
/*  94:    */   private static class SerializableMonitor
/*  95:    */     implements Serializable
/*  96:    */   {}
/*  97:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.CustomizableThreadCreator
 * JD-Core Version:    0.7.0.1
 */