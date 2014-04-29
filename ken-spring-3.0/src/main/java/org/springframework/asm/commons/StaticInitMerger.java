package org.springframework.asm.commons;

import org.springframework.asm.ClassAdapter;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;

public class StaticInitMerger
  extends ClassAdapter
{
  private String name;
  private MethodVisitor clinit;
  private String prefix;
  private int counter;
  
  public StaticInitMerger(String paramString, ClassVisitor paramClassVisitor)
  {
    super(paramClassVisitor);
    this.prefix = paramString;
  }
  
  public void visit(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString)
  {
    this.cv.visit(paramInt1, paramInt2, paramString1, paramString2, paramString3, paramArrayOfString);
    this.name = paramString1;
  }
  
  public MethodVisitor visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString)
  {
    MethodVisitor localMethodVisitor;
    if (paramString1.equals("<clinit>"))
    {
      int i = 10;
      String str = this.prefix + this.counter++;
      localMethodVisitor = this.cv.visitMethod(i, str, paramString2, paramString3, paramArrayOfString);
      if (this.clinit == null) {
        this.clinit = this.cv.visitMethod(i, paramString1, paramString2, null, null);
      }
      this.clinit.visitMethodInsn(184, this.name, str, paramString2);
    }
    else
    {
      localMethodVisitor = this.cv.visitMethod(paramInt, paramString1, paramString2, paramString3, paramArrayOfString);
    }
    return localMethodVisitor;
  }
  
  public void visitEnd()
  {
    if (this.clinit != null)
    {
      this.clinit.visitInsn(177);
      this.clinit.visitMaxs(0, 0);
    }
    this.cv.visitEnd();
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.commons.StaticInitMerger
 * JD-Core Version:    0.7.0.1
 */