/*   1:    */ package org.springframework.beans.factory;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.io.PrintWriter;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.List;
/*   7:    */ import org.springframework.beans.FatalBeanException;
/*   8:    */ import org.springframework.core.NestedRuntimeException;
/*   9:    */ 
/*  10:    */ public class BeanCreationException
/*  11:    */   extends FatalBeanException
/*  12:    */ {
/*  13:    */   private String beanName;
/*  14:    */   private String resourceDescription;
/*  15:    */   private List<Throwable> relatedCauses;
/*  16:    */   
/*  17:    */   public BeanCreationException(String msg)
/*  18:    */   {
/*  19: 47 */     super(msg);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public BeanCreationException(String msg, Throwable cause)
/*  23:    */   {
/*  24: 56 */     super(msg, cause);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public BeanCreationException(String beanName, String msg)
/*  28:    */   {
/*  29: 65 */     super("Error creating bean with name '" + beanName + "': " + msg);
/*  30: 66 */     this.beanName = beanName;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public BeanCreationException(String beanName, String msg, Throwable cause)
/*  34:    */   {
/*  35: 76 */     this(beanName, msg);
/*  36: 77 */     initCause(cause);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public BeanCreationException(String resourceDescription, String beanName, String msg)
/*  40:    */   {
/*  41: 89 */     super("Error creating bean with name '" + beanName + "'" + (resourceDescription != null ? " defined in " + resourceDescription : "") + ": " + msg);
/*  42: 90 */     this.resourceDescription = resourceDescription;
/*  43: 91 */     this.beanName = beanName;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public BeanCreationException(String resourceDescription, String beanName, String msg, Throwable cause)
/*  47:    */   {
/*  48:103 */     this(resourceDescription, beanName, msg);
/*  49:104 */     initCause(cause);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getBeanName()
/*  53:    */   {
/*  54:112 */     return this.beanName;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public String getResourceDescription()
/*  58:    */   {
/*  59:120 */     return this.resourceDescription;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void addRelatedCause(Throwable ex)
/*  63:    */   {
/*  64:130 */     if (this.relatedCauses == null) {
/*  65:131 */       this.relatedCauses = new LinkedList();
/*  66:    */     }
/*  67:133 */     this.relatedCauses.add(ex);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Throwable[] getRelatedCauses()
/*  71:    */   {
/*  72:141 */     if (this.relatedCauses == null) {
/*  73:142 */       return null;
/*  74:    */     }
/*  75:144 */     return (Throwable[])this.relatedCauses.toArray(new Throwable[this.relatedCauses.size()]);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public String toString()
/*  79:    */   {
/*  80:150 */     StringBuilder sb = new StringBuilder(super.toString());
/*  81:151 */     if (this.relatedCauses != null) {
/*  82:152 */       for (Throwable relatedCause : this.relatedCauses)
/*  83:    */       {
/*  84:153 */         sb.append("\nRelated cause: ");
/*  85:154 */         sb.append(relatedCause);
/*  86:    */       }
/*  87:    */     }
/*  88:157 */     return sb.toString();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void printStackTrace(PrintStream ps)
/*  92:    */   {
/*  93:162 */     synchronized (ps)
/*  94:    */     {
/*  95:163 */       super.printStackTrace(ps);
/*  96:164 */       if (this.relatedCauses != null) {
/*  97:165 */         for (Throwable relatedCause : this.relatedCauses)
/*  98:    */         {
/*  99:166 */           ps.println("Related cause:");
/* 100:167 */           relatedCause.printStackTrace(ps);
/* 101:    */         }
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void printStackTrace(PrintWriter pw)
/* 107:    */   {
/* 108:175 */     synchronized (pw)
/* 109:    */     {
/* 110:176 */       super.printStackTrace(pw);
/* 111:177 */       if (this.relatedCauses != null) {
/* 112:178 */         for (Throwable relatedCause : this.relatedCauses)
/* 113:    */         {
/* 114:179 */           pw.println("Related cause:");
/* 115:180 */           relatedCause.printStackTrace(pw);
/* 116:    */         }
/* 117:    */       }
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   public boolean contains(Class exClass)
/* 122:    */   {
/* 123:188 */     if (super.contains(exClass)) {
/* 124:189 */       return true;
/* 125:    */     }
/* 126:191 */     if (this.relatedCauses != null) {
/* 127:192 */       for (Throwable relatedCause : this.relatedCauses) {
/* 128:193 */         if (((relatedCause instanceof NestedRuntimeException)) && 
/* 129:194 */           (((NestedRuntimeException)relatedCause).contains(exClass))) {
/* 130:195 */           return true;
/* 131:    */         }
/* 132:    */       }
/* 133:    */     }
/* 134:199 */     return false;
/* 135:    */   }
/* 136:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanCreationException
 * JD-Core Version:    0.7.0.1
 */