/*  1:   */ package org.springframework.core.type.classreading;
/*  2:   */ 
/*  3:   */ import java.util.LinkedHashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.springframework.asm.AnnotationVisitor;
/*  6:   */ import org.springframework.asm.MethodAdapter;
/*  7:   */ import org.springframework.asm.Type;
/*  8:   */ import org.springframework.asm.commons.EmptyVisitor;
/*  9:   */ import org.springframework.core.type.MethodMetadata;
/* 10:   */ import org.springframework.util.MultiValueMap;
/* 11:   */ 
/* 12:   */ final class MethodMetadataReadingVisitor
/* 13:   */   extends MethodAdapter
/* 14:   */   implements MethodMetadata
/* 15:   */ {
/* 16:   */   private final String name;
/* 17:   */   private final int access;
/* 18:   */   private String declaringClassName;
/* 19:   */   private final ClassLoader classLoader;
/* 20:   */   private final MultiValueMap<String, MethodMetadata> methodMetadataMap;
/* 21:52 */   private final Map<String, Map<String, Object>> attributeMap = new LinkedHashMap(2);
/* 22:   */   
/* 23:   */   public MethodMetadataReadingVisitor(String name, int access, String declaringClassName, ClassLoader classLoader, MultiValueMap<String, MethodMetadata> methodMetadataMap)
/* 24:   */   {
/* 25:56 */     super(new EmptyVisitor());
/* 26:57 */     this.name = name;
/* 27:58 */     this.access = access;
/* 28:59 */     this.declaringClassName = declaringClassName;
/* 29:60 */     this.classLoader = classLoader;
/* 30:61 */     this.methodMetadataMap = methodMetadataMap;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
/* 34:   */   {
/* 35:66 */     String className = Type.getType(desc).getClassName();
/* 36:67 */     this.methodMetadataMap.add(className, this);
/* 37:68 */     return new AnnotationAttributesReadingVisitor(className, this.attributeMap, null, this.classLoader);
/* 38:   */   }
/* 39:   */   
/* 40:   */   public String getMethodName()
/* 41:   */   {
/* 42:72 */     return this.name;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean isStatic()
/* 46:   */   {
/* 47:76 */     return (this.access & 0x8) != 0;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public boolean isFinal()
/* 51:   */   {
/* 52:80 */     return (this.access & 0x10) != 0;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public boolean isOverridable()
/* 56:   */   {
/* 57:84 */     return (!isStatic()) && (!isFinal()) && ((this.access & 0x2) == 0);
/* 58:   */   }
/* 59:   */   
/* 60:   */   public boolean isAnnotated(String annotationType)
/* 61:   */   {
/* 62:88 */     return this.attributeMap.containsKey(annotationType);
/* 63:   */   }
/* 64:   */   
/* 65:   */   public Map<String, Object> getAnnotationAttributes(String annotationType)
/* 66:   */   {
/* 67:92 */     return (Map)this.attributeMap.get(annotationType);
/* 68:   */   }
/* 69:   */   
/* 70:   */   public String getDeclaringClassName()
/* 71:   */   {
/* 72:96 */     return this.declaringClassName;
/* 73:   */   }
/* 74:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.classreading.MethodMetadataReadingVisitor
 * JD-Core Version:    0.7.0.1
 */