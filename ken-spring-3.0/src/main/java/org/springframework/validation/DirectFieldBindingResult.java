/*  1:   */ package org.springframework.validation;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*  4:   */ import org.springframework.beans.PropertyAccessorFactory;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class DirectFieldBindingResult
/*  8:   */   extends AbstractPropertyBindingResult
/*  9:   */ {
/* 10:   */   private final Object target;
/* 11:   */   private transient ConfigurablePropertyAccessor directFieldAccessor;
/* 12:   */   
/* 13:   */   public DirectFieldBindingResult(Object target, String objectName)
/* 14:   */   {
/* 15:51 */     super(objectName);
/* 16:52 */     this.target = target;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public final Object getTarget()
/* 20:   */   {
/* 21:58 */     return this.target;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public final ConfigurablePropertyAccessor getPropertyAccessor()
/* 25:   */   {
/* 26:68 */     if (this.directFieldAccessor == null)
/* 27:   */     {
/* 28:69 */       this.directFieldAccessor = createDirectFieldAccessor();
/* 29:70 */       this.directFieldAccessor.setExtractOldValueForEditor(true);
/* 30:   */     }
/* 31:72 */     return this.directFieldAccessor;
/* 32:   */   }
/* 33:   */   
/* 34:   */   protected ConfigurablePropertyAccessor createDirectFieldAccessor()
/* 35:   */   {
/* 36:80 */     Assert.state(this.target != null, "Cannot access fields on null target instance '" + getObjectName() + "'!");
/* 37:81 */     return PropertyAccessorFactory.forDirectFieldAccess(this.target);
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.DirectFieldBindingResult
 * JD-Core Version:    0.7.0.1
 */