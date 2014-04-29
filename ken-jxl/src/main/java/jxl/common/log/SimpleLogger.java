/*   1:    */ package jxl.common.log;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ public class SimpleLogger
/*   7:    */   extends Logger
/*   8:    */ {
/*   9:    */   private boolean suppressWarnings;
/*  10:    */   
/*  11:    */   public SimpleLogger()
/*  12:    */   {
/*  13: 39 */     this.suppressWarnings = false;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public void debug(Object message)
/*  17:    */   {
/*  18: 47 */     if (!this.suppressWarnings)
/*  19:    */     {
/*  20: 49 */       System.out.print("Debug: ");
/*  21: 50 */       System.out.println(message);
/*  22:    */     }
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void debug(Object message, Throwable t)
/*  26:    */   {
/*  27: 59 */     if (!this.suppressWarnings)
/*  28:    */     {
/*  29: 61 */       System.out.print("Debug: ");
/*  30: 62 */       System.out.println(message);
/*  31: 63 */       t.printStackTrace();
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void error(Object message)
/*  36:    */   {
/*  37: 72 */     System.err.print("Error: ");
/*  38: 73 */     System.err.println(message);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void error(Object message, Throwable t)
/*  42:    */   {
/*  43: 81 */     System.err.print("Error: ");
/*  44: 82 */     System.err.println(message);
/*  45: 83 */     t.printStackTrace();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void fatal(Object message)
/*  49:    */   {
/*  50: 91 */     System.err.print("Fatal: ");
/*  51: 92 */     System.err.println(message);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void fatal(Object message, Throwable t)
/*  55:    */   {
/*  56:100 */     System.err.print("Fatal:  ");
/*  57:101 */     System.err.println(message);
/*  58:102 */     t.printStackTrace();
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void info(Object message)
/*  62:    */   {
/*  63:110 */     if (!this.suppressWarnings) {
/*  64:112 */       System.out.println(message);
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void info(Object message, Throwable t)
/*  69:    */   {
/*  70:122 */     if (!this.suppressWarnings)
/*  71:    */     {
/*  72:124 */       System.out.println(message);
/*  73:125 */       t.printStackTrace();
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void warn(Object message)
/*  78:    */   {
/*  79:134 */     if (!this.suppressWarnings)
/*  80:    */     {
/*  81:136 */       System.err.print("Warning:  ");
/*  82:137 */       System.err.println(message);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void warn(Object message, Throwable t)
/*  87:    */   {
/*  88:146 */     if (!this.suppressWarnings)
/*  89:    */     {
/*  90:148 */       System.err.print("Warning:  ");
/*  91:149 */       System.err.println(message);
/*  92:150 */       t.printStackTrace();
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected Logger getLoggerImpl(Class c)
/*  97:    */   {
/*  98:159 */     return this;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setSuppressWarnings(boolean w)
/* 102:    */   {
/* 103:174 */     this.suppressWarnings = w;
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.common.log.SimpleLogger
 * JD-Core Version:    0.7.0.1
 */