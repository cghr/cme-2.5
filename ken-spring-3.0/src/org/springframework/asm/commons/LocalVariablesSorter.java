package org.springframework.asm.commons;

import org.springframework.asm.Label;
import org.springframework.asm.MethodAdapter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;

public class LocalVariablesSorter
  extends MethodAdapter
{
  private int[] mapping = new int[40];
  protected final int firstLocal;
  private int nextLocal;
  
  public LocalVariablesSorter(int paramInt, String paramString, MethodVisitor paramMethodVisitor)
  {
    super(paramMethodVisitor);
    Type[] arrayOfType = Type.getArgumentTypes(paramString);
    this.nextLocal = ((0x8 & paramInt) != 0 ? 0 : 1);
    for (int i = 0; i < arrayOfType.length; i++) {
      this.nextLocal += arrayOfType[i].getSize();
    }
    this.firstLocal = this.nextLocal;
  }
  
  public void visitVarInsn(int paramInt1, int paramInt2)
  {
    int i;
    switch (paramInt1)
    {
    case 22: 
    case 24: 
    case 55: 
    case 57: 
      i = 2;
      break;
    default: 
      i = 1;
    }
    this.mv.visitVarInsn(paramInt1, remap(paramInt2, i));
  }
  
  public void visitIincInsn(int paramInt1, int paramInt2)
  {
    this.mv.visitIincInsn(remap(paramInt1, 1), paramInt2);
  }
  
  public void visitMaxs(int paramInt1, int paramInt2)
  {
    this.mv.visitMaxs(paramInt1, this.nextLocal);
  }
  
  public void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt)
  {
    int i = ("J".equals(paramString2)) || ("D".equals(paramString2)) ? 2 : 1;
    this.mv.visitLocalVariable(paramString1, paramString2, paramString3, paramLabel1, paramLabel2, remap(paramInt, i));
  }
  
  protected int newLocal(int paramInt)
  {
    int i = this.nextLocal;
    this.nextLocal += paramInt;
    return i;
  }
  
  private int remap(int paramInt1, int paramInt2)
  {
    if (paramInt1 < this.firstLocal) {
      return paramInt1;
    }
    int i = 2 * paramInt1 + paramInt2 - 1;
    int j = this.mapping.length;
    if (i >= j)
    {
      int[] arrayOfInt = new int[Math.max(2 * j, i + 1)];
      System.arraycopy(this.mapping, 0, arrayOfInt, 0, j);
      this.mapping = arrayOfInt;
    }
    int k = this.mapping[i];
    if (k == 0)
    {
      k = this.nextLocal + 1;
      this.mapping[i] = k;
      this.nextLocal += paramInt2;
    }
    return k - 1;
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.commons.LocalVariablesSorter
 * JD-Core Version:    0.7.0.1
 */