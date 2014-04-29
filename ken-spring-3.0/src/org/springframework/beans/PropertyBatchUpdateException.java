/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.io.PrintWriter;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ import org.springframework.util.ObjectUtils;
/*   7:    */ 
/*   8:    */ public class PropertyBatchUpdateException
/*   9:    */   extends BeansException
/*  10:    */ {
/*  11:    */   private PropertyAccessException[] propertyAccessExceptions;
/*  12:    */   
/*  13:    */   public PropertyBatchUpdateException(PropertyAccessException[] propertyAccessExceptions)
/*  14:    */   {
/*  15: 49 */     super(null);
/*  16: 50 */     Assert.notEmpty(propertyAccessExceptions, "At least 1 PropertyAccessException required");
/*  17: 51 */     this.propertyAccessExceptions = propertyAccessExceptions;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public final int getExceptionCount()
/*  21:    */   {
/*  22: 59 */     return this.propertyAccessExceptions.length;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public final PropertyAccessException[] getPropertyAccessExceptions()
/*  26:    */   {
/*  27: 67 */     return this.propertyAccessExceptions;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public PropertyAccessException getPropertyAccessException(String propertyName)
/*  31:    */   {
/*  32: 74 */     for (PropertyAccessException pae : this.propertyAccessExceptions) {
/*  33: 75 */       if (ObjectUtils.nullSafeEquals(propertyName, pae.getPropertyName())) {
/*  34: 76 */         return pae;
/*  35:    */       }
/*  36:    */     }
/*  37: 79 */     return null;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String getMessage()
/*  41:    */   {
/*  42: 85 */     StringBuilder sb = new StringBuilder("Failed properties: ");
/*  43: 86 */     for (int i = 0; i < this.propertyAccessExceptions.length; i++)
/*  44:    */     {
/*  45: 87 */       sb.append(this.propertyAccessExceptions[i].getMessage());
/*  46: 88 */       if (i < this.propertyAccessExceptions.length - 1) {
/*  47: 89 */         sb.append("; ");
/*  48:    */       }
/*  49:    */     }
/*  50: 92 */     return sb.toString();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String toString()
/*  54:    */   {
/*  55: 97 */     StringBuilder sb = new StringBuilder();
/*  56: 98 */     sb.append(getClass().getName()).append("; nested PropertyAccessExceptions (");
/*  57: 99 */     sb.append(getExceptionCount()).append(") are:");
/*  58:100 */     for (int i = 0; i < this.propertyAccessExceptions.length; i++)
/*  59:    */     {
/*  60:101 */       sb.append('\n').append("PropertyAccessException ").append(i + 1).append(": ");
/*  61:102 */       sb.append(this.propertyAccessExceptions[i]);
/*  62:    */     }
/*  63:104 */     return sb.toString();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void printStackTrace(PrintStream ps)
/*  67:    */   {
/*  68:109 */     synchronized (ps)
/*  69:    */     {
/*  70:110 */       ps.println(getClass().getName() + "; nested PropertyAccessException details (" + 
/*  71:111 */         getExceptionCount() + ") are:");
/*  72:112 */       for (int i = 0; i < this.propertyAccessExceptions.length; i++)
/*  73:    */       {
/*  74:113 */         ps.println("PropertyAccessException " + (i + 1) + ":");
/*  75:114 */         this.propertyAccessExceptions[i].printStackTrace(ps);
/*  76:    */       }
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void printStackTrace(PrintWriter pw)
/*  81:    */   {
/*  82:121 */     synchronized (pw)
/*  83:    */     {
/*  84:122 */       pw.println(getClass().getName() + "; nested PropertyAccessException details (" + 
/*  85:123 */         getExceptionCount() + ") are:");
/*  86:124 */       for (int i = 0; i < this.propertyAccessExceptions.length; i++)
/*  87:    */       {
/*  88:125 */         pw.println("PropertyAccessException " + (i + 1) + ":");
/*  89:126 */         this.propertyAccessExceptions[i].printStackTrace(pw);
/*  90:    */       }
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   public boolean contains(Class exType)
/*  95:    */   {
/*  96:133 */     if (exType == null) {
/*  97:134 */       return false;
/*  98:    */     }
/*  99:136 */     if (exType.isInstance(this)) {
/* 100:137 */       return true;
/* 101:    */     }
/* 102:139 */     for (PropertyAccessException pae : this.propertyAccessExceptions) {
/* 103:140 */       if (pae.contains(exType)) {
/* 104:141 */         return true;
/* 105:    */       }
/* 106:    */     }
/* 107:144 */     return false;
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyBatchUpdateException
 * JD-Core Version:    0.7.0.1
 */