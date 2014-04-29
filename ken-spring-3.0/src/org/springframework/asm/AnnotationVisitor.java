package org.springframework.asm;

public abstract interface AnnotationVisitor
{
  public abstract void visit(String paramString, Object paramObject);
  
  public abstract void visitEnum(String paramString1, String paramString2, String paramString3);
  
  public abstract AnnotationVisitor visitAnnotation(String paramString1, String paramString2);
  
  public abstract AnnotationVisitor visitArray(String paramString);
  
  public abstract void visitEnd();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.AnnotationVisitor
 * JD-Core Version:    0.7.0.1
 */