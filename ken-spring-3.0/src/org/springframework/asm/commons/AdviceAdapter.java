package org.springframework.asm.commons;

import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.Type;

public abstract class AdviceAdapter
  extends GeneratorAdapter
  implements Opcodes
{
  private static final Object THIS = new Object();
  private static final Object OTHER = new Object();
  protected int methodAccess;
  protected String methodDesc;
  private boolean constructor;
  private boolean superInitialized;
  private ArrayList stackFrame;
  private HashMap branches;
  
  public AdviceAdapter(MethodVisitor paramMethodVisitor, int paramInt, String paramString1, String paramString2)
  {
    super(paramMethodVisitor, paramInt, paramString1, paramString2);
    this.methodAccess = paramInt;
    this.methodDesc = paramString2;
    this.constructor = "<init>".equals(paramString1);
    if (!this.constructor)
    {
      this.superInitialized = true;
      onMethodEnter();
    }
    else
    {
      this.stackFrame = new ArrayList();
      this.branches = new HashMap();
    }
  }
  
  public void visitLabel(Label paramLabel)
  {
    this.mv.visitLabel(paramLabel);
    if ((this.constructor) && (this.branches != null))
    {
      ArrayList localArrayList = (ArrayList)this.branches.get(paramLabel);
      if (localArrayList != null)
      {
        this.stackFrame = localArrayList;
        this.branches.remove(paramLabel);
      }
    }
  }
  
  public void visitInsn(int paramInt)
  {
    if (this.constructor)
    {
      Object localObject1;
      Object localObject2;
      Object localObject3;
      switch (paramInt)
      {
      case 177: 
        onMethodExit(paramInt);
        break;
      case 172: 
      case 174: 
      case 176: 
      case 191: 
        popValue();
        popValue();
        onMethodExit(paramInt);
        break;
      case 173: 
      case 175: 
        popValue();
        popValue();
        onMethodExit(paramInt);
        break;
      case 0: 
      case 47: 
      case 49: 
      case 116: 
      case 117: 
      case 118: 
      case 119: 
      case 134: 
      case 138: 
      case 139: 
      case 143: 
      case 145: 
      case 146: 
      case 147: 
      case 190: 
        break;
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 11: 
      case 12: 
      case 13: 
      case 133: 
      case 135: 
      case 140: 
      case 141: 
        pushValue(OTHER);
        break;
      case 9: 
      case 10: 
      case 14: 
      case 15: 
        pushValue(OTHER);
        pushValue(OTHER);
        break;
      case 46: 
      case 48: 
      case 50: 
      case 51: 
      case 52: 
      case 53: 
      case 87: 
      case 96: 
      case 98: 
      case 100: 
      case 102: 
      case 104: 
      case 106: 
      case 108: 
      case 110: 
      case 112: 
      case 114: 
      case 120: 
      case 121: 
      case 122: 
      case 123: 
      case 124: 
      case 125: 
      case 126: 
      case 128: 
      case 130: 
      case 136: 
      case 137: 
      case 142: 
      case 144: 
      case 149: 
      case 150: 
      case 194: 
      case 195: 
        popValue();
        break;
      case 88: 
      case 97: 
      case 99: 
      case 101: 
      case 103: 
      case 105: 
      case 107: 
      case 109: 
      case 111: 
      case 113: 
      case 115: 
      case 127: 
      case 129: 
      case 131: 
        popValue();
        popValue();
        break;
      case 79: 
      case 81: 
      case 83: 
      case 84: 
      case 85: 
      case 86: 
      case 148: 
      case 151: 
      case 152: 
        popValue();
        popValue();
        popValue();
        break;
      case 80: 
      case 82: 
        popValue();
        popValue();
        popValue();
        popValue();
        break;
      case 89: 
        pushValue(peekValue());
        break;
      case 90: 
        localObject1 = popValue();
        localObject2 = popValue();
        pushValue(localObject1);
        pushValue(localObject2);
        pushValue(localObject1);
        break;
      case 91: 
        localObject1 = popValue();
        localObject2 = popValue();
        localObject3 = popValue();
        pushValue(localObject1);
        pushValue(localObject3);
        pushValue(localObject2);
        pushValue(localObject1);
        break;
      case 92: 
        localObject1 = popValue();
        localObject2 = popValue();
        pushValue(localObject2);
        pushValue(localObject1);
        pushValue(localObject2);
        pushValue(localObject1);
        break;
      case 93: 
        localObject1 = popValue();
        localObject2 = popValue();
        localObject3 = popValue();
        pushValue(localObject2);
        pushValue(localObject1);
        pushValue(localObject3);
        pushValue(localObject2);
        pushValue(localObject1);
        break;
      case 94: 
        localObject1 = popValue();
        localObject2 = popValue();
        localObject3 = popValue();
        Object localObject4 = popValue();
        pushValue(localObject2);
        pushValue(localObject1);
        pushValue(localObject4);
        pushValue(localObject3);
        pushValue(localObject2);
        pushValue(localObject1);
        break;
      case 95: 
        localObject1 = popValue();
        localObject2 = popValue();
        pushValue(localObject1);
        pushValue(localObject2);
      }
    }
    else
    {
      switch (paramInt)
      {
      case 172: 
      case 173: 
      case 174: 
      case 175: 
      case 176: 
      case 177: 
      case 191: 
        onMethodExit(paramInt);
      }
    }
    this.mv.visitInsn(paramInt);
  }
  
  public void visitVarInsn(int paramInt1, int paramInt2)
  {
    super.visitVarInsn(paramInt1, paramInt2);
    if (this.constructor) {
      switch (paramInt1)
      {
      case 21: 
      case 23: 
        pushValue(OTHER);
        break;
      case 22: 
      case 24: 
        pushValue(OTHER);
        pushValue(OTHER);
        break;
      case 25: 
        pushValue(paramInt2 == 0 ? THIS : OTHER);
        break;
      case 54: 
      case 56: 
      case 58: 
        popValue();
        break;
      case 55: 
      case 57: 
        popValue();
        popValue();
      }
    }
  }
  
  public void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    this.mv.visitFieldInsn(paramInt, paramString1, paramString2, paramString3);
    if (this.constructor)
    {
      int i = paramString3.charAt(0);
      int j = (i == 74) || (i == 68) ? 1 : 0;
      switch (paramInt)
      {
      case 178: 
        pushValue(OTHER);
        if (j != 0) {
          pushValue(OTHER);
        }
        break;
      case 179: 
        popValue();
        if (j != 0) {
          popValue();
        }
        break;
      case 181: 
        popValue();
        if (j != 0)
        {
          popValue();
          popValue();
        }
        break;
      case 180: 
      default: 
        if (j != 0) {
          pushValue(OTHER);
        }
        break;
      }
    }
  }
  
  public void visitIntInsn(int paramInt1, int paramInt2)
  {
    this.mv.visitIntInsn(paramInt1, paramInt2);
    if (this.constructor) {
      switch (paramInt1)
      {
      case 16: 
      case 17: 
        pushValue(OTHER);
      }
    }
  }
  
  public void visitLdcInsn(Object paramObject)
  {
    this.mv.visitLdcInsn(paramObject);
    if (this.constructor)
    {
      pushValue(OTHER);
      if (((paramObject instanceof Double)) || ((paramObject instanceof Long))) {
        pushValue(OTHER);
      }
    }
  }
  
  public void visitMultiANewArrayInsn(String paramString, int paramInt)
  {
    this.mv.visitMultiANewArrayInsn(paramString, paramInt);
    if (this.constructor)
    {
      for (int i = 0; i < paramInt; i++) {
        popValue();
      }
      pushValue(OTHER);
    }
  }
  
  public void visitTypeInsn(int paramInt, String paramString)
  {
    this.mv.visitTypeInsn(paramInt, paramString);
    if ((this.constructor) && (paramInt == 187)) {
      pushValue(OTHER);
    }
  }
  
  public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    this.mv.visitMethodInsn(paramInt, paramString1, paramString2, paramString3);
    if (this.constructor)
    {
      Type[] arrayOfType = Type.getArgumentTypes(paramString3);
      for (int i = 0; i < arrayOfType.length; i++)
      {
        popValue();
        if (arrayOfType[i].getSize() == 2) {
          popValue();
        }
      }
      switch (paramInt)
      {
      case 182: 
      case 185: 
        popValue();
        break;
      case 183: 
        localObject = popValue();
        if ((localObject == THIS) && (!this.superInitialized))
        {
          onMethodEnter();
          this.superInitialized = true;
          this.constructor = false;
        }
        break;
      }
      Object localObject = Type.getReturnType(paramString3);
      if (localObject != Type.VOID_TYPE)
      {
        pushValue(OTHER);
        if (((Type)localObject).getSize() == 2) {
          pushValue(OTHER);
        }
      }
    }
  }
  
  public void visitJumpInsn(int paramInt, Label paramLabel)
  {
    this.mv.visitJumpInsn(paramInt, paramLabel);
    if (this.constructor)
    {
      switch (paramInt)
      {
      case 153: 
      case 154: 
      case 155: 
      case 156: 
      case 157: 
      case 158: 
      case 198: 
      case 199: 
        popValue();
        break;
      case 159: 
      case 160: 
      case 161: 
      case 162: 
      case 163: 
      case 164: 
      case 165: 
      case 166: 
        popValue();
        popValue();
        break;
      case 168: 
        pushValue(OTHER);
      }
      addBranch(paramLabel);
    }
  }
  
  public void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfInt, Label[] paramArrayOfLabel)
  {
    this.mv.visitLookupSwitchInsn(paramLabel, paramArrayOfInt, paramArrayOfLabel);
    if (this.constructor)
    {
      popValue();
      addBranches(paramLabel, paramArrayOfLabel);
    }
  }
  
  public void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label[] paramArrayOfLabel)
  {
    this.mv.visitTableSwitchInsn(paramInt1, paramInt2, paramLabel, paramArrayOfLabel);
    if (this.constructor)
    {
      popValue();
      addBranches(paramLabel, paramArrayOfLabel);
    }
  }
  
  private void addBranches(Label paramLabel, Label[] paramArrayOfLabel)
  {
    addBranch(paramLabel);
    for (int i = 0; i < paramArrayOfLabel.length; i++) {
      addBranch(paramArrayOfLabel[i]);
    }
  }
  
  private void addBranch(Label paramLabel)
  {
    if (this.branches.containsKey(paramLabel)) {
      return;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(this.stackFrame);
    this.branches.put(paramLabel, localArrayList);
  }
  
  private Object popValue()
  {
    return this.stackFrame.remove(this.stackFrame.size() - 1);
  }
  
  private Object peekValue()
  {
    return this.stackFrame.get(this.stackFrame.size() - 1);
  }
  
  private void pushValue(Object paramObject)
  {
    this.stackFrame.add(paramObject);
  }
  
  protected abstract void onMethodEnter();
  
  protected abstract void onMethodExit(int paramInt);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.commons.AdviceAdapter
 * JD-Core Version:    0.7.0.1
 */