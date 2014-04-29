/*   1:    */ package org.springframework.core.type;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Modifier;
/*   4:    */ import java.util.LinkedHashSet;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public class StandardClassMetadata
/*   8:    */   implements ClassMetadata
/*   9:    */ {
/*  10:    */   private final Class introspectedClass;
/*  11:    */   
/*  12:    */   public StandardClassMetadata(Class introspectedClass)
/*  13:    */   {
/*  14: 41 */     Assert.notNull(introspectedClass, "Class must not be null");
/*  15: 42 */     this.introspectedClass = introspectedClass;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public final Class getIntrospectedClass()
/*  19:    */   {
/*  20: 49 */     return this.introspectedClass;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String getClassName()
/*  24:    */   {
/*  25: 54 */     return this.introspectedClass.getName();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean isInterface()
/*  29:    */   {
/*  30: 58 */     return this.introspectedClass.isInterface();
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean isAbstract()
/*  34:    */   {
/*  35: 62 */     return Modifier.isAbstract(this.introspectedClass.getModifiers());
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean isConcrete()
/*  39:    */   {
/*  40: 66 */     return (!isInterface()) && (!isAbstract());
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isFinal()
/*  44:    */   {
/*  45: 70 */     return Modifier.isFinal(this.introspectedClass.getModifiers());
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean isIndependent()
/*  49:    */   {
/*  50: 76 */     return (!hasEnclosingClass()) || ((this.introspectedClass.getDeclaringClass() != null) && (Modifier.isStatic(this.introspectedClass.getModifiers())));
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean hasEnclosingClass()
/*  54:    */   {
/*  55: 80 */     return this.introspectedClass.getEnclosingClass() != null;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getEnclosingClassName()
/*  59:    */   {
/*  60: 84 */     Class enclosingClass = this.introspectedClass.getEnclosingClass();
/*  61: 85 */     return enclosingClass != null ? enclosingClass.getName() : null;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean hasSuperClass()
/*  65:    */   {
/*  66: 89 */     return this.introspectedClass.getSuperclass() != null;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String getSuperClassName()
/*  70:    */   {
/*  71: 93 */     Class superClass = this.introspectedClass.getSuperclass();
/*  72: 94 */     return superClass != null ? superClass.getName() : null;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String[] getInterfaceNames()
/*  76:    */   {
/*  77: 98 */     Class[] ifcs = this.introspectedClass.getInterfaces();
/*  78: 99 */     String[] ifcNames = new String[ifcs.length];
/*  79:100 */     for (int i = 0; i < ifcs.length; i++) {
/*  80:101 */       ifcNames[i] = ifcs[i].getName();
/*  81:    */     }
/*  82:103 */     return ifcNames;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String[] getMemberClassNames()
/*  86:    */   {
/*  87:107 */     LinkedHashSet<String> memberClassNames = new LinkedHashSet();
/*  88:108 */     for (Class<?> nestedClass : this.introspectedClass.getDeclaredClasses()) {
/*  89:109 */       memberClassNames.add(nestedClass.getName());
/*  90:    */     }
/*  91:111 */     return (String[])memberClassNames.toArray(new String[memberClassNames.size()]);
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.StandardClassMetadata
 * JD-Core Version:    0.7.0.1
 */