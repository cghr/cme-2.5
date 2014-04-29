/*   1:    */ package org.springframework.core.type.classreading;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashSet;
/*   4:    */ import java.util.Set;
/*   5:    */ import org.springframework.asm.AnnotationVisitor;
/*   6:    */ import org.springframework.asm.Attribute;
/*   7:    */ import org.springframework.asm.ClassVisitor;
/*   8:    */ import org.springframework.asm.FieldVisitor;
/*   9:    */ import org.springframework.asm.MethodVisitor;
/*  10:    */ import org.springframework.asm.commons.EmptyVisitor;
/*  11:    */ import org.springframework.core.type.ClassMetadata;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ 
/*  14:    */ class ClassMetadataReadingVisitor
/*  15:    */   implements ClassVisitor, ClassMetadata
/*  16:    */ {
/*  17:    */   private String className;
/*  18:    */   private boolean isInterface;
/*  19:    */   private boolean isAbstract;
/*  20:    */   private boolean isFinal;
/*  21:    */   private String enclosingClassName;
/*  22:    */   private boolean independentInnerClass;
/*  23:    */   private String superClassName;
/*  24:    */   private String[] interfaces;
/*  25: 61 */   private Set<String> memberClassNames = new LinkedHashSet();
/*  26:    */   
/*  27:    */   public void visit(int version, int access, String name, String signature, String supername, String[] interfaces)
/*  28:    */   {
/*  29: 65 */     this.className = ClassUtils.convertResourcePathToClassName(name);
/*  30: 66 */     this.isInterface = ((access & 0x200) != 0);
/*  31: 67 */     this.isAbstract = ((access & 0x400) != 0);
/*  32: 68 */     this.isFinal = ((access & 0x10) != 0);
/*  33: 69 */     if (supername != null) {
/*  34: 70 */       this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
/*  35:    */     }
/*  36: 72 */     this.interfaces = new String[interfaces.length];
/*  37: 73 */     for (int i = 0; i < interfaces.length; i++) {
/*  38: 74 */       this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void visitOuterClass(String owner, String name, String desc)
/*  43:    */   {
/*  44: 79 */     this.enclosingClassName = ClassUtils.convertResourcePathToClassName(owner);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void visitInnerClass(String name, String outerName, String innerName, int access)
/*  48:    */   {
/*  49: 83 */     if (outerName != null)
/*  50:    */     {
/*  51: 84 */       String fqName = ClassUtils.convertResourcePathToClassName(name);
/*  52: 85 */       String fqOuterName = ClassUtils.convertResourcePathToClassName(outerName);
/*  53: 86 */       if (this.className.equals(fqName))
/*  54:    */       {
/*  55: 87 */         this.enclosingClassName = fqOuterName;
/*  56: 88 */         this.independentInnerClass = ((access & 0x8) != 0);
/*  57:    */       }
/*  58: 90 */       else if (this.className.equals(fqOuterName))
/*  59:    */       {
/*  60: 91 */         this.memberClassNames.add(fqName);
/*  61:    */       }
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void visitSource(String source, String debug) {}
/*  66:    */   
/*  67:    */   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
/*  68:    */   {
/*  69:102 */     return new EmptyVisitor();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void visitAttribute(Attribute attr) {}
/*  73:    */   
/*  74:    */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
/*  75:    */   {
/*  76:111 */     return new EmptyVisitor();
/*  77:    */   }
/*  78:    */   
/*  79:    */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
/*  80:    */   {
/*  81:116 */     return new EmptyVisitor();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void visitEnd() {}
/*  85:    */   
/*  86:    */   public String getClassName()
/*  87:    */   {
/*  88:125 */     return this.className;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean isInterface()
/*  92:    */   {
/*  93:129 */     return this.isInterface;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public boolean isAbstract()
/*  97:    */   {
/*  98:133 */     return this.isAbstract;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public boolean isConcrete()
/* 102:    */   {
/* 103:137 */     return (!this.isInterface) && (!this.isAbstract);
/* 104:    */   }
/* 105:    */   
/* 106:    */   public boolean isFinal()
/* 107:    */   {
/* 108:141 */     return this.isFinal;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public boolean isIndependent()
/* 112:    */   {
/* 113:145 */     return (this.enclosingClassName == null) || (this.independentInnerClass);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean hasEnclosingClass()
/* 117:    */   {
/* 118:149 */     return this.enclosingClassName != null;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public String getEnclosingClassName()
/* 122:    */   {
/* 123:153 */     return this.enclosingClassName;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public boolean hasSuperClass()
/* 127:    */   {
/* 128:157 */     return this.superClassName != null;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public String getSuperClassName()
/* 132:    */   {
/* 133:161 */     return this.superClassName;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String[] getInterfaceNames()
/* 137:    */   {
/* 138:165 */     return this.interfaces;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public String[] getMemberClassNames()
/* 142:    */   {
/* 143:169 */     return (String[])this.memberClassNames.toArray(new String[this.memberClassNames.size()]);
/* 144:    */   }
/* 145:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.ClassMetadataReadingVisitor
 * JD-Core Version:    0.7.0.1
 */