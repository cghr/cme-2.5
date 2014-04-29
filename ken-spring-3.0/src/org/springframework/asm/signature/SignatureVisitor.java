package org.springframework.asm.signature;

public abstract interface SignatureVisitor
{
  public static final char EXTENDS = '+';
  public static final char SUPER = '-';
  public static final char INSTANCEOF = '=';
  
  public abstract void visitFormalTypeParameter(String paramString);
  
  public abstract SignatureVisitor visitClassBound();
  
  public abstract SignatureVisitor visitInterfaceBound();
  
  public abstract SignatureVisitor visitSuperclass();
  
  public abstract SignatureVisitor visitInterface();
  
  public abstract SignatureVisitor visitParameterType();
  
  public abstract SignatureVisitor visitReturnType();
  
  public abstract SignatureVisitor visitExceptionType();
  
  public abstract void visitBaseType(char paramChar);
  
  public abstract void visitTypeVariable(String paramString);
  
  public abstract SignatureVisitor visitArrayType();
  
  public abstract void visitClassType(String paramString);
  
  public abstract void visitInnerClassType(String paramString);
  
  public abstract void visitTypeArgument();
  
  public abstract SignatureVisitor visitTypeArgument(char paramChar);
  
  public abstract void visitEnd();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.signature.SignatureVisitor
 * JD-Core Version:    0.7.0.1
 */