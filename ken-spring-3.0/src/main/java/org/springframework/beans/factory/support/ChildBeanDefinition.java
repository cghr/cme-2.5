/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.MutablePropertyValues;
/*   4:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*   5:    */ import org.springframework.util.ObjectUtils;
/*   6:    */ 
/*   7:    */ public class ChildBeanDefinition
/*   8:    */   extends AbstractBeanDefinition
/*   9:    */ {
/*  10:    */   private String parentName;
/*  11:    */   
/*  12:    */   public ChildBeanDefinition(String parentName)
/*  13:    */   {
/*  14: 65 */     this.parentName = parentName;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public ChildBeanDefinition(String parentName, MutablePropertyValues pvs)
/*  18:    */   {
/*  19: 74 */     super(null, pvs);
/*  20: 75 */     this.parentName = parentName;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public ChildBeanDefinition(String parentName, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*  24:    */   {
/*  25: 87 */     super(cargs, pvs);
/*  26: 88 */     this.parentName = parentName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public ChildBeanDefinition(String parentName, Class beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*  30:    */   {
/*  31:102 */     super(cargs, pvs);
/*  32:103 */     this.parentName = parentName;
/*  33:104 */     setBeanClass(beanClass);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public ChildBeanDefinition(String parentName, String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs)
/*  37:    */   {
/*  38:119 */     super(cargs, pvs);
/*  39:120 */     this.parentName = parentName;
/*  40:121 */     setBeanClassName(beanClassName);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public ChildBeanDefinition(ChildBeanDefinition original)
/*  44:    */   {
/*  45:130 */     super(original);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setParentName(String parentName)
/*  49:    */   {
/*  50:135 */     this.parentName = parentName;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String getParentName()
/*  54:    */   {
/*  55:139 */     return this.parentName;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void validate()
/*  59:    */     throws BeanDefinitionValidationException
/*  60:    */   {
/*  61:144 */     super.validate();
/*  62:145 */     if (this.parentName == null) {
/*  63:146 */       throw new BeanDefinitionValidationException("'parentName' must be set in ChildBeanDefinition");
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public AbstractBeanDefinition cloneBeanDefinition()
/*  68:    */   {
/*  69:153 */     return new ChildBeanDefinition(this);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public boolean equals(Object other)
/*  73:    */   {
/*  74:158 */     if (this == other) {
/*  75:159 */       return true;
/*  76:    */     }
/*  77:161 */     if (!(other instanceof ChildBeanDefinition)) {
/*  78:162 */       return false;
/*  79:    */     }
/*  80:164 */     ChildBeanDefinition that = (ChildBeanDefinition)other;
/*  81:165 */     return (ObjectUtils.nullSafeEquals(this.parentName, that.parentName)) && (super.equals(other));
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int hashCode()
/*  85:    */   {
/*  86:170 */     return ObjectUtils.nullSafeHashCode(this.parentName) * 29 + super.hashCode();
/*  87:    */   }
/*  88:    */   
/*  89:    */   public String toString()
/*  90:    */   {
/*  91:175 */     StringBuilder sb = new StringBuilder("Child bean with parent '");
/*  92:176 */     sb.append(this.parentName).append("': ").append(super.toString());
/*  93:177 */     return sb.toString();
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.ChildBeanDefinition
 * JD-Core Version:    0.7.0.1
 */