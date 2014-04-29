/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.springframework.beans.BeanWrapper;
/*   5:    */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*   6:    */ import org.springframework.beans.PropertyAccessorFactory;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class BeanPropertyBindingResult
/*  10:    */   extends AbstractPropertyBindingResult
/*  11:    */   implements Serializable
/*  12:    */ {
/*  13:    */   private final Object target;
/*  14:    */   private final boolean autoGrowNestedPaths;
/*  15:    */   private final int autoGrowCollectionLimit;
/*  16:    */   private transient BeanWrapper beanWrapper;
/*  17:    */   
/*  18:    */   public BeanPropertyBindingResult(Object target, String objectName)
/*  19:    */   {
/*  20: 61 */     this(target, objectName, true, 2147483647);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public BeanPropertyBindingResult(Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit)
/*  24:    */   {
/*  25: 72 */     super(objectName);
/*  26: 73 */     this.target = target;
/*  27: 74 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*  28: 75 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public final Object getTarget()
/*  32:    */   {
/*  33: 81 */     return this.target;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public final ConfigurablePropertyAccessor getPropertyAccessor()
/*  37:    */   {
/*  38: 91 */     if (this.beanWrapper == null)
/*  39:    */     {
/*  40: 92 */       this.beanWrapper = createBeanWrapper();
/*  41: 93 */       this.beanWrapper.setExtractOldValueForEditor(true);
/*  42: 94 */       this.beanWrapper.setAutoGrowNestedPaths(this.autoGrowNestedPaths);
/*  43: 95 */       this.beanWrapper.setAutoGrowCollectionLimit(this.autoGrowCollectionLimit);
/*  44:    */     }
/*  45: 97 */     return this.beanWrapper;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected BeanWrapper createBeanWrapper()
/*  49:    */   {
/*  50:105 */     Assert.state(this.target != null, "Cannot access properties on null bean instance '" + getObjectName() + "'!");
/*  51:106 */     return PropertyAccessorFactory.forBeanPropertyAccess(this.target);
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.BeanPropertyBindingResult
 * JD-Core Version:    0.7.0.1
 */