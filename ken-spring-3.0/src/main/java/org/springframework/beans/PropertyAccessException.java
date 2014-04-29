/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyChangeEvent;
/*  4:   */ import org.springframework.core.ErrorCoded;
/*  5:   */ 
/*  6:   */ public abstract class PropertyAccessException
/*  7:   */   extends BeansException
/*  8:   */   implements ErrorCoded
/*  9:   */ {
/* 10:   */   private transient PropertyChangeEvent propertyChangeEvent;
/* 11:   */   
/* 12:   */   public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable cause)
/* 13:   */   {
/* 14:42 */     super(msg, cause);
/* 15:43 */     this.propertyChangeEvent = propertyChangeEvent;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public PropertyAccessException(String msg, Throwable cause)
/* 19:   */   {
/* 20:52 */     super(msg, cause);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public PropertyChangeEvent getPropertyChangeEvent()
/* 24:   */   {
/* 25:62 */     return this.propertyChangeEvent;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String getPropertyName()
/* 29:   */   {
/* 30:69 */     return this.propertyChangeEvent != null ? this.propertyChangeEvent.getPropertyName() : null;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public Object getValue()
/* 34:   */   {
/* 35:76 */     return this.propertyChangeEvent != null ? this.propertyChangeEvent.getNewValue() : null;
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyAccessException
 * JD-Core Version:    0.7.0.1
 */