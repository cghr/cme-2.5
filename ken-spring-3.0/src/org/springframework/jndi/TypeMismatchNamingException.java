/*  1:   */ package org.springframework.jndi;
/*  2:   */ 
/*  3:   */ import javax.naming.NamingException;
/*  4:   */ 
/*  5:   */ public class TypeMismatchNamingException
/*  6:   */   extends NamingException
/*  7:   */ {
/*  8:   */   private Class requiredType;
/*  9:   */   private Class actualType;
/* 10:   */   
/* 11:   */   public TypeMismatchNamingException(String jndiName, Class requiredType, Class actualType)
/* 12:   */   {
/* 13:45 */     super("Object of type [" + actualType + "] available at JNDI location [" + jndiName + "] is not assignable to [" + requiredType.getName() + "]");
/* 14:46 */     this.requiredType = requiredType;
/* 15:47 */     this.actualType = actualType;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public TypeMismatchNamingException(String explanation)
/* 19:   */   {
/* 20:55 */     super(explanation);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public final Class getRequiredType()
/* 24:   */   {
/* 25:63 */     return this.requiredType;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public final Class getActualType()
/* 29:   */   {
/* 30:70 */     return this.actualType;
/* 31:   */   }
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.TypeMismatchNamingException
 * JD-Core Version:    0.7.0.1
 */