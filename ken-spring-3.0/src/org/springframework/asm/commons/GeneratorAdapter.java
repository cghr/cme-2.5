package org.springframework.asm.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Type;

public class GeneratorAdapter
  extends LocalVariablesSorter
{
  private static final Type BYTE_TYPE = Type.getType("Ljava/lang/Byte;");
  private static final Type BOOLEAN_TYPE = Type.getType("Ljava/lang/Boolean;");
  private static final Type SHORT_TYPE = Type.getType("Ljava/lang/Short;");
  private static final Type CHARACTER_TYPE = Type.getType("Ljava/lang/Character;");
  private static final Type INTEGER_TYPE = Type.getType("Ljava/lang/Integer;");
  private static final Type FLOAT_TYPE = Type.getType("Ljava/lang/Float;");
  private static final Type LONG_TYPE = Type.getType("Ljava/lang/Long;");
  private static final Type DOUBLE_TYPE = Type.getType("Ljava/lang/Double;");
  private static final Type NUMBER_TYPE = Type.getType("Ljava/lang/Number;");
  private static final Type OBJECT_TYPE = Type.getType("Ljava/lang/Object;");
  private static final Method BOOLEAN_VALUE = Method.getMethod("boolean booleanValue()");
  private static final Method CHAR_VALUE = Method.getMethod("char charValue()");
  private static final Method INT_VALUE = Method.getMethod("int intValue()");
  private static final Method FLOAT_VALUE = Method.getMethod("float floatValue()");
  private static final Method LONG_VALUE = Method.getMethod("long longValue()");
  private static final Method DOUBLE_VALUE = Method.getMethod("double doubleValue()");
  public static final int ADD = 96;
  public static final int SUB = 100;
  public static final int MUL = 104;
  public static final int DIV = 108;
  public static final int REM = 112;
  public static final int NEG = 116;
  public static final int SHL = 120;
  public static final int SHR = 122;
  public static final int USHR = 124;
  public static final int AND = 126;
  public static final int OR = 128;
  public static final int XOR = 130;
  public static final int EQ = 153;
  public static final int NE = 154;
  public static final int LT = 155;
  public static final int GE = 156;
  public static final int GT = 157;
  public static final int LE = 158;
  private final int access;
  private final Type returnType;
  private final Type[] argumentTypes;
  private final List localTypes;
  
  public GeneratorAdapter(MethodVisitor paramMethodVisitor, int paramInt, String paramString1, String paramString2)
  {
    super(paramInt, paramString2, paramMethodVisitor);
    this.access = paramInt;
    this.returnType = Type.getReturnType(paramString2);
    this.argumentTypes = Type.getArgumentTypes(paramString2);
    this.localTypes = new ArrayList();
  }
  
  public GeneratorAdapter(int paramInt, Method paramMethod, MethodVisitor paramMethodVisitor)
  {
    super(paramInt, paramMethod.getDescriptor(), paramMethodVisitor);
    this.access = paramInt;
    this.returnType = paramMethod.getReturnType();
    this.argumentTypes = paramMethod.getArgumentTypes();
    this.localTypes = new ArrayList();
  }
  
  public GeneratorAdapter(int paramInt, Method paramMethod, String paramString, Type[] paramArrayOfType, ClassVisitor paramClassVisitor)
  {
    this(paramInt, paramMethod, paramClassVisitor.visitMethod(paramInt, paramMethod.getName(), paramMethod.getDescriptor(), paramString, getInternalNames(paramArrayOfType)));
  }
  
  private static String[] getInternalNames(Type[] paramArrayOfType)
  {
    if (paramArrayOfType == null) {
      return null;
    }
    String[] arrayOfString = new String[paramArrayOfType.length];
    for (int i = 0; i < arrayOfString.length; i++) {
      arrayOfString[i] = paramArrayOfType[i].getInternalName();
    }
    return arrayOfString;
  }
  
  public void push(boolean paramBoolean)
  {
    push(paramBoolean ? 1 : 0);
  }
  
  public void push(int paramInt)
  {
    if ((paramInt >= -1) && (paramInt <= 5)) {
      this.mv.visitInsn(3 + paramInt);
    } else if ((paramInt >= -128) && (paramInt <= 127)) {
      this.mv.visitIntInsn(16, paramInt);
    } else if ((paramInt >= -32768) && (paramInt <= 32767)) {
      this.mv.visitIntInsn(17, paramInt);
    } else {
      this.mv.visitLdcInsn(new Integer(paramInt));
    }
  }
  
  public void push(long paramLong)
  {
    if ((paramLong == 0L) || (paramLong == 1L)) {
      this.mv.visitInsn(9 + (int)paramLong);
    } else {
      this.mv.visitLdcInsn(new Long(paramLong));
    }
  }
  
  public void push(float paramFloat)
  {
    int i = Float.floatToIntBits(paramFloat);
    if ((i == 0L) || (i == 1065353216) || (i == 1073741824)) {
      this.mv.visitInsn(11 + (int)paramFloat);
    } else {
      this.mv.visitLdcInsn(new Float(paramFloat));
    }
  }
  
  public void push(double paramDouble)
  {
    long l = Double.doubleToLongBits(paramDouble);
    if ((l == 0L) || (l == 4607182418800017408L)) {
      this.mv.visitInsn(14 + (int)paramDouble);
    } else {
      this.mv.visitLdcInsn(new Double(paramDouble));
    }
  }
  
  public void push(String paramString)
  {
    if (paramString == null) {
      this.mv.visitInsn(1);
    } else {
      this.mv.visitLdcInsn(paramString);
    }
  }
  
  public void push(Type paramType)
  {
    if (paramType == null) {
      this.mv.visitInsn(1);
    } else {
      this.mv.visitLdcInsn(paramType);
    }
  }
  
  private int getArgIndex(int paramInt)
  {
    int i = (this.access & 0x8) == 0 ? 1 : 0;
    for (int j = 0; j < paramInt; j++) {
      i += this.argumentTypes[j].getSize();
    }
    return i;
  }
  
  private void loadInsn(Type paramType, int paramInt)
  {
    this.mv.visitVarInsn(paramType.getOpcode(21), paramInt);
  }
  
  private void storeInsn(Type paramType, int paramInt)
  {
    this.mv.visitVarInsn(paramType.getOpcode(54), paramInt);
  }
  
  public void loadThis()
  {
    if ((this.access & 0x8) != 0) {
      throw new IllegalStateException("no 'this' pointer within static method");
    }
    this.mv.visitVarInsn(25, 0);
  }
  
  public void loadArg(int paramInt)
  {
    loadInsn(this.argumentTypes[paramInt], getArgIndex(paramInt));
  }
  
  public void loadArgs(int paramInt1, int paramInt2)
  {
    int i = getArgIndex(paramInt1);
    for (int j = 0; j < paramInt2; j++)
    {
      Type localType = this.argumentTypes[(paramInt1 + j)];
      loadInsn(localType, i);
      i += localType.getSize();
    }
  }
  
  public void loadArgs()
  {
    loadArgs(0, this.argumentTypes.length);
  }
  
  public void loadArgArray()
  {
    push(this.argumentTypes.length);
    newArray(OBJECT_TYPE);
    for (int i = 0; i < this.argumentTypes.length; i++)
    {
      dup();
      push(i);
      loadArg(i);
      box(this.argumentTypes[i]);
      arrayStore(OBJECT_TYPE);
    }
  }
  
  public void storeArg(int paramInt)
  {
    storeInsn(this.argumentTypes[paramInt], getArgIndex(paramInt));
  }
  
  public int newLocal(Type paramType)
  {
    int i = super.newLocal(paramType.getSize());
    setLocalType(i, paramType);
    return i;
  }
  
  public Type getLocalType(int paramInt)
  {
    return (Type)this.localTypes.get(paramInt - this.firstLocal);
  }
  
  private void setLocalType(int paramInt, Type paramType)
  {
    int i = paramInt - this.firstLocal;
    while (this.localTypes.size() < i + 1) {
      this.localTypes.add(null);
    }
    this.localTypes.set(i, paramType);
  }
  
  public void loadLocal(int paramInt)
  {
    loadInsn(getLocalType(paramInt), paramInt);
  }
  
  public void loadLocal(int paramInt, Type paramType)
  {
    setLocalType(paramInt, paramType);
    loadInsn(paramType, paramInt);
  }
  
  public void storeLocal(int paramInt)
  {
    storeInsn(getLocalType(paramInt), paramInt);
  }
  
  public void storeLocal(int paramInt, Type paramType)
  {
    setLocalType(paramInt, paramType);
    storeInsn(paramType, paramInt);
  }
  
  public void arrayLoad(Type paramType)
  {
    this.mv.visitInsn(paramType.getOpcode(46));
  }
  
  public void arrayStore(Type paramType)
  {
    this.mv.visitInsn(paramType.getOpcode(79));
  }
  
  public void pop()
  {
    this.mv.visitInsn(87);
  }
  
  public void pop2()
  {
    this.mv.visitInsn(88);
  }
  
  public void dup()
  {
    this.mv.visitInsn(89);
  }
  
  public void dup2()
  {
    this.mv.visitInsn(92);
  }
  
  public void dupX1()
  {
    this.mv.visitInsn(90);
  }
  
  public void dupX2()
  {
    this.mv.visitInsn(91);
  }
  
  public void dup2X1()
  {
    this.mv.visitInsn(93);
  }
  
  public void dup2X2()
  {
    this.mv.visitInsn(94);
  }
  
  public void swap()
  {
    this.mv.visitInsn(95);
  }
  
  public void swap(Type paramType1, Type paramType2)
  {
    if (paramType2.getSize() == 1)
    {
      if (paramType1.getSize() == 1)
      {
        swap();
      }
      else
      {
        dupX2();
        pop();
      }
    }
    else if (paramType1.getSize() == 1)
    {
      dup2X1();
      pop2();
    }
    else
    {
      dup2X2();
      pop2();
    }
  }
  
  public void math(int paramInt, Type paramType)
  {
    this.mv.visitInsn(paramType.getOpcode(paramInt));
  }
  
  public void not()
  {
    this.mv.visitInsn(4);
    this.mv.visitInsn(130);
  }
  
  public void iinc(int paramInt1, int paramInt2)
  {
    this.mv.visitIincInsn(paramInt1, paramInt2);
  }
  
  public void cast(Type paramType1, Type paramType2)
  {
    if (paramType1 != paramType2) {
      if (paramType1 == Type.DOUBLE_TYPE)
      {
        if (paramType2 == Type.FLOAT_TYPE)
        {
          this.mv.visitInsn(144);
        }
        else if (paramType2 == Type.LONG_TYPE)
        {
          this.mv.visitInsn(143);
        }
        else
        {
          this.mv.visitInsn(142);
          cast(Type.INT_TYPE, paramType2);
        }
      }
      else if (paramType1 == Type.FLOAT_TYPE)
      {
        if (paramType2 == Type.DOUBLE_TYPE)
        {
          this.mv.visitInsn(141);
        }
        else if (paramType2 == Type.LONG_TYPE)
        {
          this.mv.visitInsn(140);
        }
        else
        {
          this.mv.visitInsn(139);
          cast(Type.INT_TYPE, paramType2);
        }
      }
      else if (paramType1 == Type.LONG_TYPE)
      {
        if (paramType2 == Type.DOUBLE_TYPE)
        {
          this.mv.visitInsn(138);
        }
        else if (paramType2 == Type.FLOAT_TYPE)
        {
          this.mv.visitInsn(137);
        }
        else
        {
          this.mv.visitInsn(136);
          cast(Type.INT_TYPE, paramType2);
        }
      }
      else if (paramType2 == Type.BYTE_TYPE) {
        this.mv.visitInsn(145);
      } else if (paramType2 == Type.CHAR_TYPE) {
        this.mv.visitInsn(146);
      } else if (paramType2 == Type.DOUBLE_TYPE) {
        this.mv.visitInsn(135);
      } else if (paramType2 == Type.FLOAT_TYPE) {
        this.mv.visitInsn(134);
      } else if (paramType2 == Type.LONG_TYPE) {
        this.mv.visitInsn(133);
      } else if (paramType2 == Type.SHORT_TYPE) {
        this.mv.visitInsn(147);
      }
    }
  }
  
  public void box(Type paramType)
  {
    if ((paramType.getSort() == 10) || (paramType.getSort() == 9)) {
      return;
    }
    if (paramType == Type.VOID_TYPE)
    {
      push((String)null);
    }
    else
    {
      Type localType = paramType;
      switch (paramType.getSort())
      {
      case 3: 
        localType = BYTE_TYPE;
        break;
      case 1: 
        localType = BOOLEAN_TYPE;
        break;
      case 4: 
        localType = SHORT_TYPE;
        break;
      case 2: 
        localType = CHARACTER_TYPE;
        break;
      case 5: 
        localType = INTEGER_TYPE;
        break;
      case 6: 
        localType = FLOAT_TYPE;
        break;
      case 7: 
        localType = LONG_TYPE;
        break;
      case 8: 
        localType = DOUBLE_TYPE;
      }
      newInstance(localType);
      if (paramType.getSize() == 2)
      {
        dupX2();
        dupX2();
        pop();
      }
      else
      {
        dupX1();
        swap();
      }
      invokeConstructor(localType, new Method("<init>", Type.VOID_TYPE, new Type[] { paramType }));
    }
  }
  
  public void unbox(Type paramType)
  {
    Type localType = NUMBER_TYPE;
    Method localMethod = null;
    switch (paramType.getSort())
    {
    case 0: 
      return;
    case 2: 
      localType = CHARACTER_TYPE;
      localMethod = CHAR_VALUE;
      break;
    case 1: 
      localType = BOOLEAN_TYPE;
      localMethod = BOOLEAN_VALUE;
      break;
    case 8: 
      localMethod = DOUBLE_VALUE;
      break;
    case 6: 
      localMethod = FLOAT_VALUE;
      break;
    case 7: 
      localMethod = LONG_VALUE;
      break;
    case 3: 
    case 4: 
    case 5: 
      localMethod = INT_VALUE;
    }
    if (localMethod == null)
    {
      checkCast(paramType);
    }
    else
    {
      checkCast(localType);
      invokeVirtual(localType, localMethod);
    }
  }
  
  public Label newLabel()
  {
    return new Label();
  }
  
  public void mark(Label paramLabel)
  {
    this.mv.visitLabel(paramLabel);
  }
  
  public Label mark()
  {
    Label localLabel = new Label();
    this.mv.visitLabel(localLabel);
    return localLabel;
  }
  
  public void ifCmp(Type paramType, int paramInt, Label paramLabel)
  {
    int i = -1;
    int j = paramInt;
    switch (paramInt)
    {
    case 156: 
      j = 155;
      break;
    case 158: 
      j = 157;
    }
    switch (paramType.getSort())
    {
    case 7: 
      this.mv.visitInsn(148);
      break;
    case 8: 
      this.mv.visitInsn(152);
      break;
    case 6: 
      this.mv.visitInsn(150);
      break;
    case 9: 
    case 10: 
      switch (paramInt)
      {
      case 153: 
        this.mv.visitJumpInsn(165, paramLabel);
        return;
      case 154: 
        this.mv.visitJumpInsn(166, paramLabel);
        return;
      }
      throw new IllegalArgumentException("Bad comparison for type " + paramType);
    default: 
      switch (paramInt)
      {
      case 153: 
        i = 159;
        break;
      case 154: 
        i = 160;
        break;
      case 156: 
        i = 162;
        break;
      case 155: 
        i = 161;
        break;
      case 158: 
        i = 164;
        break;
      case 157: 
        i = 163;
      }
      this.mv.visitJumpInsn(i, paramLabel);
      return;
    }
    this.mv.visitJumpInsn(j, paramLabel);
  }
  
  public void ifICmp(int paramInt, Label paramLabel)
  {
    ifCmp(Type.INT_TYPE, paramInt, paramLabel);
  }
  
  public void ifZCmp(int paramInt, Label paramLabel)
  {
    this.mv.visitJumpInsn(paramInt, paramLabel);
  }
  
  public void ifNull(Label paramLabel)
  {
    this.mv.visitJumpInsn(198, paramLabel);
  }
  
  public void ifNonNull(Label paramLabel)
  {
    this.mv.visitJumpInsn(199, paramLabel);
  }
  
  public void goTo(Label paramLabel)
  {
    this.mv.visitJumpInsn(167, paramLabel);
  }
  
  public void ret(int paramInt)
  {
    this.mv.visitVarInsn(169, paramInt);
  }
  
  public void tableSwitch(int[] paramArrayOfInt, TableSwitchGenerator paramTableSwitchGenerator)
  {
    float f;
    if (paramArrayOfInt.length == 0) {
      f = 0.0F;
    } else {
      f = paramArrayOfInt.length / (paramArrayOfInt[(paramArrayOfInt.length - 1)] - paramArrayOfInt[0] + 1);
    }
    tableSwitch(paramArrayOfInt, paramTableSwitchGenerator, f >= 0.5F);
  }
  
  public void tableSwitch(int[] paramArrayOfInt, TableSwitchGenerator paramTableSwitchGenerator, boolean paramBoolean)
  {
    for (int i = 1; i < paramArrayOfInt.length; i++) {
      if (paramArrayOfInt[i] < paramArrayOfInt[(i - 1)]) {
        throw new IllegalArgumentException("keys must be sorted ascending");
      }
    }
    Label localLabel1 = newLabel();
    Label localLabel2 = newLabel();
    if (paramArrayOfInt.length > 0)
    {
      int j = paramArrayOfInt.length;
      int k = paramArrayOfInt[0];
      int m = paramArrayOfInt[(j - 1)];
      int n = m - k + 1;
      Label[] arrayOfLabel;
      int i1;
      if (paramBoolean)
      {
        arrayOfLabel = new Label[n];
        Arrays.fill(arrayOfLabel, localLabel1);
        for (i1 = 0; i1 < j; i1++) {
          arrayOfLabel[(paramArrayOfInt[i1] - k)] = newLabel();
        }
        this.mv.visitTableSwitchInsn(k, m, localLabel1, arrayOfLabel);
        for (i1 = 0; i1 < n; i1++)
        {
          Label localLabel3 = arrayOfLabel[i1];
          if (localLabel3 != localLabel1)
          {
            mark(localLabel3);
            paramTableSwitchGenerator.generateCase(i1 + k, localLabel2);
          }
        }
      }
      else
      {
        arrayOfLabel = new Label[j];
        for (i1 = 0; i1 < j; i1++) {
          arrayOfLabel[i1] = newLabel();
        }
        this.mv.visitLookupSwitchInsn(localLabel1, paramArrayOfInt, arrayOfLabel);
        for (i1 = 0; i1 < j; i1++)
        {
          mark(arrayOfLabel[i1]);
          paramTableSwitchGenerator.generateCase(paramArrayOfInt[i1], localLabel2);
        }
      }
    }
    mark(localLabel1);
    paramTableSwitchGenerator.generateDefault();
    mark(localLabel2);
  }
  
  public void returnValue()
  {
    this.mv.visitInsn(this.returnType.getOpcode(172));
  }
  
  private void fieldInsn(int paramInt, Type paramType1, String paramString, Type paramType2)
  {
    this.mv.visitFieldInsn(paramInt, paramType1.getInternalName(), paramString, paramType2.getDescriptor());
  }
  
  public void getStatic(Type paramType1, String paramString, Type paramType2)
  {
    fieldInsn(178, paramType1, paramString, paramType2);
  }
  
  public void putStatic(Type paramType1, String paramString, Type paramType2)
  {
    fieldInsn(179, paramType1, paramString, paramType2);
  }
  
  public void getField(Type paramType1, String paramString, Type paramType2)
  {
    fieldInsn(180, paramType1, paramString, paramType2);
  }
  
  public void putField(Type paramType1, String paramString, Type paramType2)
  {
    fieldInsn(181, paramType1, paramString, paramType2);
  }
  
  private void invokeInsn(int paramInt, Type paramType, Method paramMethod)
  {
    String str = paramType.getSort() == 9 ? paramType.getDescriptor() : paramType.getInternalName();
    this.mv.visitMethodInsn(paramInt, str, paramMethod.getName(), paramMethod.getDescriptor());
  }
  
  public void invokeVirtual(Type paramType, Method paramMethod)
  {
    invokeInsn(182, paramType, paramMethod);
  }
  
  public void invokeConstructor(Type paramType, Method paramMethod)
  {
    invokeInsn(183, paramType, paramMethod);
  }
  
  public void invokeStatic(Type paramType, Method paramMethod)
  {
    invokeInsn(184, paramType, paramMethod);
  }
  
  public void invokeInterface(Type paramType, Method paramMethod)
  {
    invokeInsn(185, paramType, paramMethod);
  }
  
  private void typeInsn(int paramInt, Type paramType)
  {
    String str;
    if (paramType.getSort() == 9) {
      str = paramType.getDescriptor();
    } else {
      str = paramType.getInternalName();
    }
    this.mv.visitTypeInsn(paramInt, str);
  }
  
  public void newInstance(Type paramType)
  {
    typeInsn(187, paramType);
  }
  
  public void newArray(Type paramType)
  {
    int i;
    switch (paramType.getSort())
    {
    case 1: 
      i = 4;
      break;
    case 2: 
      i = 5;
      break;
    case 3: 
      i = 8;
      break;
    case 4: 
      i = 9;
      break;
    case 5: 
      i = 10;
      break;
    case 6: 
      i = 6;
      break;
    case 7: 
      i = 11;
      break;
    case 8: 
      i = 7;
      break;
    default: 
      typeInsn(189, paramType);
      return;
    }
    this.mv.visitIntInsn(188, i);
  }
  
  public void arrayLength()
  {
    this.mv.visitInsn(190);
  }
  
  public void throwException()
  {
    this.mv.visitInsn(191);
  }
  
  public void throwException(Type paramType, String paramString)
  {
    newInstance(paramType);
    dup();
    push(paramString);
    invokeConstructor(paramType, Method.getMethod("void <init> (String)"));
    throwException();
  }
  
  public void checkCast(Type paramType)
  {
    if (!paramType.equals(OBJECT_TYPE)) {
      typeInsn(192, paramType);
    }
  }
  
  public void instanceOf(Type paramType)
  {
    typeInsn(193, paramType);
  }
  
  public void monitorEnter()
  {
    this.mv.visitInsn(194);
  }
  
  public void monitorExit()
  {
    this.mv.visitInsn(195);
  }
  
  public void endMethod()
  {
    if ((this.access & 0x400) == 0) {
      this.mv.visitMaxs(0, 0);
    }
  }
  
  public void catchException(Label paramLabel1, Label paramLabel2, Type paramType)
  {
    this.mv.visitTryCatchBlock(paramLabel1, paramLabel2, mark(), paramType.getInternalName());
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.commons.GeneratorAdapter
 * JD-Core Version:    0.7.0.1
 */