package org.springframework.asm;

class MethodWriter
  implements MethodVisitor
{
  MethodWriter a;
  ClassWriter b;
  private int c;
  private int d;
  private int e;
  private String f;
  int I;
  int J;
  String g;
  int h;
  int[] i;
  private ByteVector j;
  private AnnotationWriter k;
  private AnnotationWriter l;
  private AnnotationWriter[] m;
  private AnnotationWriter[] n;
  private Attribute o;
  private ByteVector p = new ByteVector();
  private int q;
  private int r;
  private int s;
  private Handler t;
  private Handler K;
  private int u;
  private ByteVector v;
  private int w;
  private ByteVector x;
  private int y;
  private ByteVector z;
  private Attribute A;
  private boolean B;
  private final boolean C;
  private int D;
  private int E;
  private Label F;
  private Label G;
  private static final int[] H;
  
  MethodWriter(ClassWriter paramClassWriter, int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, boolean paramBoolean)
  {
    if (paramClassWriter.A == null) {
      paramClassWriter.A = this;
    } else {
      paramClassWriter.B.a = this;
    }
    paramClassWriter.B = this;
    this.b = paramClassWriter;
    this.c = paramInt;
    this.d = paramClassWriter.newUTF8(paramString1);
    this.e = paramClassWriter.newUTF8(paramString2);
    this.f = paramString2;
    this.g = paramString3;
    int i1;
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      this.h = paramArrayOfString.length;
      this.i = new int[this.h];
      for (i1 = 0; i1 < this.h; i1++) {
        this.i[i1] = paramClassWriter.newClass(paramArrayOfString[i1]);
      }
    }
    this.C = paramBoolean;
    if (paramBoolean)
    {
      i1 = a(paramString2) >> 2;
      if ((paramInt & 0x8) != 0) {
        i1--;
      }
      this.r = i1;
      this.F = new Label();
      this.F.j = true;
      this.G = this.F;
    }
  }
  
  public AnnotationVisitor visitAnnotationDefault()
  {
    this.j = new ByteVector();
    return new AnnotationWriter(this.b, false, this.j, null, 0);
  }
  
  public AnnotationVisitor visitAnnotation(String paramString, boolean paramBoolean)
  {
    ByteVector localByteVector = new ByteVector();
    localByteVector.putShort(this.b.newUTF8(paramString)).putShort(0);
    AnnotationWriter localAnnotationWriter = new AnnotationWriter(this.b, true, localByteVector, localByteVector, 2);
    if (paramBoolean)
    {
      localAnnotationWriter.g = this.k;
      this.k = localAnnotationWriter;
    }
    else
    {
      localAnnotationWriter.g = this.l;
      this.l = localAnnotationWriter;
    }
    return localAnnotationWriter;
  }
  
  public AnnotationVisitor visitParameterAnnotation(int paramInt, String paramString, boolean paramBoolean)
  {
    ByteVector localByteVector = new ByteVector();
    localByteVector.putShort(this.b.newUTF8(paramString)).putShort(0);
    AnnotationWriter localAnnotationWriter = new AnnotationWriter(this.b, true, localByteVector, localByteVector, 2);
    if (paramBoolean)
    {
      if (this.m == null) {
        this.m = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
      }
      localAnnotationWriter.g = this.m[paramInt];
      this.m[paramInt] = localAnnotationWriter;
    }
    else
    {
      if (this.n == null) {
        this.n = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
      }
      localAnnotationWriter.g = this.n[paramInt];
      this.n[paramInt] = localAnnotationWriter;
    }
    return localAnnotationWriter;
  }
  
  public void visitAttribute(Attribute paramAttribute)
  {
    if (paramAttribute.isCodeAttribute())
    {
      paramAttribute.a = this.A;
      this.A = paramAttribute;
    }
    else
    {
      paramAttribute.a = this.o;
      this.o = paramAttribute;
    }
  }
  
  public void visitCode() {}
  
  public void visitInsn(int paramInt)
  {
    if (this.C)
    {
      int i1 = this.D + H[paramInt];
      if (i1 > this.E) {
        this.E = i1;
      }
      this.D = i1;
      if (((paramInt >= 172) && (paramInt <= 177)) || ((paramInt == 191) && (this.F != null)))
      {
        this.F.g = this.E;
        this.F = null;
      }
    }
    this.p.putByte(paramInt);
  }
  
  public void visitIntInsn(int paramInt1, int paramInt2)
  {
    if ((this.C) && (paramInt1 != 188))
    {
      int i1 = this.D + 1;
      if (i1 > this.E) {
        this.E = i1;
      }
      this.D = i1;
    }
    if (paramInt1 == 17) {
      this.p.b(paramInt1, paramInt2);
    } else {
      this.p.a(paramInt1, paramInt2);
    }
  }
  
  public void visitVarInsn(int paramInt1, int paramInt2)
  {
    int i1;
    if (this.C)
    {
      if (paramInt1 == 169)
      {
        if (this.F != null)
        {
          this.F.g = this.E;
          this.F = null;
        }
      }
      else
      {
        i1 = this.D + H[paramInt1];
        if (i1 > this.E) {
          this.E = i1;
        }
        this.D = i1;
      }
      if ((paramInt1 == 22) || (paramInt1 == 24) || (paramInt1 == 55) || (paramInt1 == 57)) {
        i1 = paramInt2 + 2;
      } else {
        i1 = paramInt2 + 1;
      }
      if (i1 > this.r) {
        this.r = i1;
      }
    }
    if ((paramInt2 < 4) && (paramInt1 != 169))
    {
      if (paramInt1 < 54) {
        i1 = 26 + (paramInt1 - 21 << 2) + paramInt2;
      } else {
        i1 = 59 + (paramInt1 - 54 << 2) + paramInt2;
      }
      this.p.putByte(i1);
    }
    else if (paramInt2 >= 256)
    {
      this.p.putByte(196).b(paramInt1, paramInt2);
    }
    else
    {
      this.p.a(paramInt1, paramInt2);
    }
  }
  
  public void visitTypeInsn(int paramInt, String paramString)
  {
    if ((this.C) && (paramInt == 187))
    {
      int i1 = this.D + 1;
      if (i1 > this.E) {
        this.E = i1;
      }
      this.D = i1;
    }
    this.p.b(paramInt, this.b.newClass(paramString));
  }
  
  public void visitFieldInsn(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    if (this.C)
    {
      int i1 = paramString3.charAt(0);
      int i2;
      switch (paramInt)
      {
      case 178: 
        i2 = this.D + ((i1 == 68) || (i1 == 74) ? 2 : 1);
        break;
      case 179: 
        i2 = this.D + ((i1 == 68) || (i1 == 74) ? -2 : -1);
        break;
      case 180: 
        i2 = this.D + ((i1 == 68) || (i1 == 74) ? 1 : 0);
        break;
      default: 
        i2 = this.D + ((i1 == 68) || (i1 == 74) ? -3 : -2);
      }
      if (i2 > this.E) {
        this.E = i2;
      }
      this.D = i2;
    }
    this.p.b(paramInt, this.b.newField(paramString1, paramString2, paramString3));
  }
  
  public void visitMethodInsn(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    boolean bool = paramInt == 185;
    Item localItem = this.b.a(paramString1, paramString2, paramString3, bool);
    int i1 = localItem.c;
    if (this.C)
    {
      if (i1 == 0)
      {
        i1 = a(paramString3);
        localItem.c = i1;
      }
      int i2;
      if (paramInt == 184) {
        i2 = this.D - (i1 >> 2) + (i1 & 0x3) + 1;
      } else {
        i2 = this.D - (i1 >> 2) + (i1 & 0x3);
      }
      if (i2 > this.E) {
        this.E = i2;
      }
      this.D = i2;
    }
    if (bool)
    {
      if ((!this.C) && (i1 == 0))
      {
        i1 = a(paramString3);
        localItem.c = i1;
      }
      this.p.b(185, localItem.a).a(i1 >> 2, 0);
    }
    else
    {
      this.p.b(paramInt, localItem.a);
    }
  }
  
  public void visitJumpInsn(int paramInt, Label paramLabel)
  {
    if (this.C) {
      if (paramInt == 167)
      {
        if (this.F != null)
        {
          this.F.g = this.E;
          a(this.D, paramLabel);
          this.F = null;
        }
      }
      else if (paramInt == 168)
      {
        if (this.F != null) {
          a(this.D + 1, paramLabel);
        }
      }
      else
      {
        this.D += H[paramInt];
        if (this.F != null) {
          a(this.D, paramLabel);
        }
      }
    }
    if ((paramLabel.a) && (paramLabel.b - this.p.b < -32768))
    {
      if (paramInt == 167)
      {
        this.p.putByte(200);
      }
      else if (paramInt == 168)
      {
        this.p.putByte(201);
      }
      else
      {
        this.p.putByte(paramInt <= 166 ? (paramInt + 1 ^ 0x1) - 1 : paramInt ^ 0x1);
        this.p.putShort(8);
        this.p.putByte(200);
      }
      paramLabel.a(this, this.p, this.p.b - 1, true);
    }
    else
    {
      this.p.putByte(paramInt);
      paramLabel.a(this, this.p, this.p.b - 1, false);
    }
  }
  
  public void visitLabel(Label paramLabel)
  {
    if (this.C)
    {
      if (this.F != null)
      {
        this.F.g = this.E;
        a(this.D, paramLabel);
      }
      this.F = paramLabel;
      this.D = 0;
      this.E = 0;
    }
    this.B |= paramLabel.a(this, this.p.b, this.p.a);
  }
  
  public void visitLdcInsn(Object paramObject)
  {
    Item localItem = this.b.a(paramObject);
    if (this.C)
    {
      if ((localItem.b == 5) || (localItem.b == 6)) {
        i1 = this.D + 2;
      } else {
        i1 = this.D + 1;
      }
      if (i1 > this.E) {
        this.E = i1;
      }
      this.D = i1;
    }
    int i1 = localItem.a;
    if ((localItem.b == 5) || (localItem.b == 6)) {
      this.p.b(20, i1);
    } else if (i1 >= 256) {
      this.p.b(19, i1);
    } else {
      this.p.a(18, i1);
    }
  }
  
  public void visitIincInsn(int paramInt1, int paramInt2)
  {
    if (this.C)
    {
      int i1 = paramInt1 + 1;
      if (i1 > this.r) {
        this.r = i1;
      }
    }
    if ((paramInt1 > 255) || (paramInt2 > 127) || (paramInt2 < -128)) {
      this.p.putByte(196).b(132, paramInt1).putShort(paramInt2);
    } else {
      this.p.putByte(132).a(paramInt1, paramInt2);
    }
  }
  
  public void visitTableSwitchInsn(int paramInt1, int paramInt2, Label paramLabel, Label[] paramArrayOfLabel)
  {
    if (this.C)
    {
      this.D -= 1;
      if (this.F != null)
      {
        this.F.g = this.E;
        a(this.D, paramLabel);
        for (i1 = 0; i1 < paramArrayOfLabel.length; i1++) {
          a(this.D, paramArrayOfLabel[i1]);
        }
        this.F = null;
      }
    }
    int i1 = this.p.b;
    this.p.putByte(170);
    while (this.p.b % 4 != 0) {
      this.p.putByte(0);
    }
    paramLabel.a(this, this.p, i1, true);
    this.p.putInt(paramInt1).putInt(paramInt2);
    for (int i2 = 0; i2 < paramArrayOfLabel.length; i2++) {
      paramArrayOfLabel[i2].a(this, this.p, i1, true);
    }
  }
  
  public void visitLookupSwitchInsn(Label paramLabel, int[] paramArrayOfInt, Label[] paramArrayOfLabel)
  {
    if (this.C)
    {
      this.D -= 1;
      if (this.F != null)
      {
        this.F.g = this.E;
        a(this.D, paramLabel);
        for (i1 = 0; i1 < paramArrayOfLabel.length; i1++) {
          a(this.D, paramArrayOfLabel[i1]);
        }
        this.F = null;
      }
    }
    int i1 = this.p.b;
    this.p.putByte(171);
    while (this.p.b % 4 != 0) {
      this.p.putByte(0);
    }
    paramLabel.a(this, this.p, i1, true);
    this.p.putInt(paramArrayOfLabel.length);
    for (int i2 = 0; i2 < paramArrayOfLabel.length; i2++)
    {
      this.p.putInt(paramArrayOfInt[i2]);
      paramArrayOfLabel[i2].a(this, this.p, i1, true);
    }
  }
  
  public void visitMultiANewArrayInsn(String paramString, int paramInt)
  {
    if (this.C) {
      this.D += 1 - paramInt;
    }
    this.p.b(197, this.b.newClass(paramString)).putByte(paramInt);
  }
  
  public void visitTryCatchBlock(Label paramLabel1, Label paramLabel2, Label paramLabel3, String paramString)
  {
    if ((this.C) && (!paramLabel3.j))
    {
      paramLabel3.f = 1;
      paramLabel3.j = true;
      paramLabel3.i = this.G;
      this.G = paramLabel3;
    }
    this.s += 1;
    Handler localHandler = new Handler();
    localHandler.a = paramLabel1;
    localHandler.b = paramLabel2;
    localHandler.c = paramLabel3;
    localHandler.d = paramString;
    localHandler.e = (paramString != null ? this.b.newClass(paramString) : 0);
    if (this.K == null) {
      this.t = localHandler;
    } else {
      this.K.f = localHandler;
    }
    this.K = localHandler;
  }
  
  public void visitLocalVariable(String paramString1, String paramString2, String paramString3, Label paramLabel1, Label paramLabel2, int paramInt)
  {
    if (paramString3 != null)
    {
      if (this.x == null) {
        this.x = new ByteVector();
      }
      this.w += 1;
      this.x.putShort(paramLabel1.b).putShort(paramLabel2.b - paramLabel1.b).putShort(this.b.newUTF8(paramString1)).putShort(this.b.newUTF8(paramString3)).putShort(paramInt);
    }
    if (this.v == null) {
      this.v = new ByteVector();
    }
    this.u += 1;
    this.v.putShort(paramLabel1.b).putShort(paramLabel2.b - paramLabel1.b).putShort(this.b.newUTF8(paramString1)).putShort(this.b.newUTF8(paramString2)).putShort(paramInt);
    if (this.C)
    {
      int i1 = paramString2.charAt(0);
      int i2 = paramInt + ((i1 == 76) || (i1 == 68) ? 2 : 1);
      if (i2 > this.r) {
        this.r = i2;
      }
    }
  }
  
  public void visitLineNumber(int paramInt, Label paramLabel)
  {
    if (this.z == null) {
      this.z = new ByteVector();
    }
    this.y += 1;
    this.z.putShort(paramLabel.b);
    this.z.putShort(paramInt);
  }
  
  public void visitMaxs(int paramInt1, int paramInt2)
  {
    if (this.C)
    {
      int i1 = 0;
      Object localObject1 = this.G;
      while (localObject1 != null)
      {
        Object localObject2 = localObject1;
        localObject1 = ((Label)localObject1).i;
        int i2 = ((Label)localObject2).f;
        int i3 = i2 + ((Label)localObject2).g;
        if (i3 > i1) {
          i1 = i3;
        }
        for (Edge localEdge = ((Label)localObject2).h; localEdge != null; localEdge = localEdge.c)
        {
          localObject2 = localEdge.b;
          if (!((Label)localObject2).j)
          {
            ((Label)localObject2).f = (i2 + localEdge.a);
            ((Label)localObject2).j = true;
            ((Label)localObject2).i = ((Label)localObject1);
            localObject1 = localObject2;
          }
        }
      }
      this.q = i1;
    }
    else
    {
      this.q = paramInt1;
      this.r = paramInt2;
    }
  }
  
  public void visitEnd() {}
  
  private static int a(String paramString)
  {
    int i1 = 1;
    int i2 = 1;
    for (;;)
    {
      int i3 = paramString.charAt(i2++);
      if (i3 == 41)
      {
        i3 = paramString.charAt(i2);
        return i1 << 2 | ((i3 == 68) || (i3 == 74) ? 2 : i3 == 86 ? 0 : 1);
      }
      if (i3 == 76)
      {
        while (paramString.charAt(i2++) != ';') {}
        i1++;
      }
      else if (i3 == 91)
      {
        while ((i3 = paramString.charAt(i2)) == '[') {
          i2++;
        }
        if ((i3 == 68) || (i3 == 74)) {
          i1--;
        }
      }
      else if ((i3 == 68) || (i3 == 74))
      {
        i1 += 2;
      }
      else
      {
        i1++;
      }
    }
  }
  
  private void a(int paramInt, Label paramLabel)
  {
    Edge localEdge = new Edge();
    localEdge.a = paramInt;
    localEdge.b = paramLabel;
    localEdge.c = this.F.h;
    this.F.h = localEdge;
  }
  
  final int a()
  {
    if (this.I != 0) {
      return 6 + this.J;
    }
    if (this.B) {
      a(new int[0], new int[0], 0);
    }
    int i1 = 8;
    if (this.p.b > 0)
    {
      this.b.newUTF8("Code");
      i1 += 18 + this.p.b + 8 * this.s;
      if (this.v != null)
      {
        this.b.newUTF8("LocalVariableTable");
        i1 += 8 + this.v.b;
      }
      if (this.x != null)
      {
        this.b.newUTF8("LocalVariableTypeTable");
        i1 += 8 + this.x.b;
      }
      if (this.z != null)
      {
        this.b.newUTF8("LineNumberTable");
        i1 += 8 + this.z.b;
      }
      if (this.A != null) {
        i1 += this.A.a(this.b, this.p.a, this.p.b, this.q, this.r);
      }
    }
    if (this.h > 0)
    {
      this.b.newUTF8("Exceptions");
      i1 += 8 + 2 * this.h;
    }
    if (((this.c & 0x1000) != 0) && ((this.b.b & 0xFFFF) < 49))
    {
      this.b.newUTF8("Synthetic");
      i1 += 6;
    }
    if ((this.c & 0x20000) != 0)
    {
      this.b.newUTF8("Deprecated");
      i1 += 6;
    }
    if (this.b.b == 48)
    {
      if ((this.c & 0x80) != 0)
      {
        this.b.newUTF8("Varargs");
        i1 += 6;
      }
      if ((this.c & 0x40) != 0)
      {
        this.b.newUTF8("Bridge");
        i1 += 6;
      }
    }
    if (this.g != null)
    {
      this.b.newUTF8("Signature");
      this.b.newUTF8(this.g);
      i1 += 8;
    }
    if (this.j != null)
    {
      this.b.newUTF8("AnnotationDefault");
      i1 += 6 + this.j.b;
    }
    if (this.k != null)
    {
      this.b.newUTF8("RuntimeVisibleAnnotations");
      i1 += 8 + this.k.a();
    }
    if (this.l != null)
    {
      this.b.newUTF8("RuntimeInvisibleAnnotations");
      i1 += 8 + this.l.a();
    }
    int i2;
    if (this.m != null)
    {
      this.b.newUTF8("RuntimeVisibleParameterAnnotations");
      i1 += 7 + 2 * this.m.length;
      for (i2 = this.m.length - 1; i2 >= 0; i2--) {
        i1 += (this.m[i2] == null ? 0 : this.m[i2].a());
      }
    }
    if (this.n != null)
    {
      this.b.newUTF8("RuntimeInvisibleParameterAnnotations");
      i1 += 7 + 2 * this.n.length;
      for (i2 = this.n.length - 1; i2 >= 0; i2--) {
        i1 += (this.n[i2] == null ? 0 : this.n[i2].a());
      }
    }
    if (this.o != null) {
      i1 += this.o.a(this.b, null, 0, -1, -1);
    }
    return i1;
  }
  
  final void a(ByteVector paramByteVector)
  {
    paramByteVector.putShort(this.c).putShort(this.d).putShort(this.e);
    if (this.I != 0)
    {
      paramByteVector.putByteArray(this.b.D.b, this.I, this.J);
      return;
    }
    int i1 = 0;
    if (this.p.b > 0) {
      i1++;
    }
    if (this.h > 0) {
      i1++;
    }
    if (((this.c & 0x1000) != 0) && ((this.b.b & 0xFFFF) < 49)) {
      i1++;
    }
    if ((this.c & 0x20000) != 0) {
      i1++;
    }
    if (this.b.b == 48)
    {
      if ((this.c & 0x80) != 0) {
        i1++;
      }
      if ((this.c & 0x40) != 0) {
        i1++;
      }
    }
    if (this.g != null) {
      i1++;
    }
    if (this.j != null) {
      i1++;
    }
    if (this.k != null) {
      i1++;
    }
    if (this.l != null) {
      i1++;
    }
    if (this.m != null) {
      i1++;
    }
    if (this.n != null) {
      i1++;
    }
    if (this.o != null) {
      i1 += this.o.a();
    }
    paramByteVector.putShort(i1);
    int i2;
    if (this.p.b > 0)
    {
      i2 = 12 + this.p.b + 8 * this.s;
      if (this.v != null) {
        i2 += 8 + this.v.b;
      }
      if (this.x != null) {
        i2 += 8 + this.x.b;
      }
      if (this.z != null) {
        i2 += 8 + this.z.b;
      }
      if (this.A != null) {
        i2 += this.A.a(this.b, this.p.a, this.p.b, this.q, this.r);
      }
      paramByteVector.putShort(this.b.newUTF8("Code")).putInt(i2);
      paramByteVector.putShort(this.q).putShort(this.r);
      paramByteVector.putInt(this.p.b).putByteArray(this.p.a, 0, this.p.b);
      paramByteVector.putShort(this.s);
      if (this.s > 0) {
        for (Handler localHandler = this.t; localHandler != null; localHandler = localHandler.f) {
          paramByteVector.putShort(localHandler.a.b).putShort(localHandler.b.b).putShort(localHandler.c.b).putShort(localHandler.e);
        }
      }
      i1 = 0;
      if (this.v != null) {
        i1++;
      }
      if (this.x != null) {
        i1++;
      }
      if (this.z != null) {
        i1++;
      }
      if (this.A != null) {
        i1 += this.A.a();
      }
      paramByteVector.putShort(i1);
      if (this.v != null)
      {
        paramByteVector.putShort(this.b.newUTF8("LocalVariableTable"));
        paramByteVector.putInt(this.v.b + 2).putShort(this.u);
        paramByteVector.putByteArray(this.v.a, 0, this.v.b);
      }
      if (this.x != null)
      {
        paramByteVector.putShort(this.b.newUTF8("LocalVariableTypeTable"));
        paramByteVector.putInt(this.x.b + 2).putShort(this.w);
        paramByteVector.putByteArray(this.x.a, 0, this.x.b);
      }
      if (this.z != null)
      {
        paramByteVector.putShort(this.b.newUTF8("LineNumberTable"));
        paramByteVector.putInt(this.z.b + 2).putShort(this.y);
        paramByteVector.putByteArray(this.z.a, 0, this.z.b);
      }
      if (this.A != null) {
        this.A.a(this.b, this.p.a, this.p.b, this.r, this.q, paramByteVector);
      }
    }
    if (this.h > 0)
    {
      paramByteVector.putShort(this.b.newUTF8("Exceptions")).putInt(2 * this.h + 2);
      paramByteVector.putShort(this.h);
      for (i2 = 0; i2 < this.h; i2++) {
        paramByteVector.putShort(this.i[i2]);
      }
    }
    if (((this.c & 0x1000) != 0) && ((this.b.b & 0xFFFF) < 49)) {
      paramByteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
    }
    if ((this.c & 0x20000) != 0) {
      paramByteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
    }
    if (this.b.b == 48)
    {
      if ((this.c & 0x80) != 0) {
        paramByteVector.putShort(this.b.newUTF8("Varargs")).putInt(0);
      }
      if ((this.c & 0x40) != 0) {
        paramByteVector.putShort(this.b.newUTF8("Bridge")).putInt(0);
      }
    }
    if (this.g != null) {
      paramByteVector.putShort(this.b.newUTF8("Signature")).putInt(2).putShort(this.b.newUTF8(this.g));
    }
    if (this.j != null)
    {
      paramByteVector.putShort(this.b.newUTF8("AnnotationDefault"));
      paramByteVector.putInt(this.j.b);
      paramByteVector.putByteArray(this.j.a, 0, this.j.b);
    }
    if (this.k != null)
    {
      paramByteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
      this.k.a(paramByteVector);
    }
    if (this.l != null)
    {
      paramByteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
      this.l.a(paramByteVector);
    }
    if (this.m != null)
    {
      paramByteVector.putShort(this.b.newUTF8("RuntimeVisibleParameterAnnotations"));
      AnnotationWriter.a(this.m, paramByteVector);
    }
    if (this.n != null)
    {
      paramByteVector.putShort(this.b.newUTF8("RuntimeInvisibleParameterAnnotations"));
      AnnotationWriter.a(this.n, paramByteVector);
    }
    if (this.o != null) {
      this.o.a(this.b, null, 0, -1, -1, paramByteVector);
    }
  }
  
  private int[] a(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    byte[] arrayOfByte = this.p.a;
    Object localObject1 = new int[paramInt];
    Object localObject2 = new int[paramInt];
    System.arraycopy(paramArrayOfInt1, 0, localObject1, 0, paramInt);
    System.arraycopy(paramArrayOfInt2, 0, localObject2, 0, paramInt);
    boolean[] arrayOfBoolean = new boolean[this.p.b];
    int i1 = 3;
    int i4;
    int i5;
    int i6;
    Object localObject3;
    do
    {
      if (i1 == 3) {
        i1 = 2;
      }
      i2 = 0;
      while (i2 < arrayOfByte.length)
      {
        int i3 = arrayOfByte[i2] & 0xFF;
        i4 = 0;
        switch (ClassWriter.a[i3])
        {
        case 0: 
        case 4: 
          i2++;
          break;
        case 8: 
          if (i3 > 201)
          {
            i3 = i3 < 218 ? i3 - 49 : i3 - 20;
            i5 = i2 + c(arrayOfByte, i2 + 1);
          }
          else
          {
            i5 = i2 + b(arrayOfByte, i2 + 1);
          }
          i6 = a((int[])localObject1, (int[])localObject2, i2, i5);
          if (((i6 < -32768) || (i6 > 32767)) && (arrayOfBoolean[i2] == 0))
          {
            if ((i3 == 167) || (i3 == 168)) {
              i4 = 2;
            } else {
              i4 = 5;
            }
            arrayOfBoolean[i2] = true;
          }
          i2 += 3;
          break;
        case 9: 
          i2 += 5;
          break;
        case 13: 
          if (i1 == 1)
          {
            i6 = a((int[])localObject1, (int[])localObject2, 0, i2);
            i4 = -(i6 & 0x3);
          }
          else if (arrayOfBoolean[i2] == 0)
          {
            i4 = i2 & 0x3;
            arrayOfBoolean[i2] = true;
          }
          i2 = i2 + 4 - (i2 & 0x3);
          i2 += 4 * (a(arrayOfByte, i2 + 8) - a(arrayOfByte, i2 + 4) + 1) + 12;
          break;
        case 14: 
          if (i1 == 1)
          {
            i6 = a((int[])localObject1, (int[])localObject2, 0, i2);
            i4 = -(i6 & 0x3);
          }
          else if (arrayOfBoolean[i2] == 0)
          {
            i4 = i2 & 0x3;
            arrayOfBoolean[i2] = true;
          }
          i2 = i2 + 4 - (i2 & 0x3);
          i2 += 8 * a(arrayOfByte, i2 + 4) + 8;
          break;
        case 16: 
          i3 = arrayOfByte[(i2 + 1)] & 0xFF;
          if (i3 == 132) {
            i2 += 6;
          } else {
            i2 += 4;
          }
          break;
        case 1: 
        case 3: 
        case 10: 
          i2 += 2;
          break;
        case 2: 
        case 5: 
        case 6: 
        case 11: 
        case 12: 
          i2 += 3;
          break;
        case 7: 
          i2 += 5;
          break;
        case 15: 
        default: 
          i2 += 4;
        }
        if (i4 != 0)
        {
          localObject3 = new int[localObject1.length + 1];
          int[] arrayOfInt = new int[localObject2.length + 1];
          System.arraycopy(localObject1, 0, localObject3, 0, localObject1.length);
          System.arraycopy(localObject2, 0, arrayOfInt, 0, localObject2.length);
          localObject3[localObject1.length] = i2;
          arrayOfInt[localObject2.length] = i4;
          localObject1 = localObject3;
          localObject2 = arrayOfInt;
          if (i4 > 0) {
            i1 = 3;
          }
        }
      }
      if (i1 < 3) {
        i1--;
      }
    } while (i1 != 0);
    ByteVector localByteVector = new ByteVector(this.p.b);
    int i2 = 0;
    while (i2 < this.p.b)
    {
      for (i7 = localObject1.length - 1; i7 >= 0; i7--) {
        if ((localObject1[i7] == i2) && (i7 < paramInt))
        {
          if (paramArrayOfInt2[i7] > 0) {
            localByteVector.putByteArray(null, 0, paramArrayOfInt2[i7]);
          } else {
            localByteVector.b += paramArrayOfInt2[i7];
          }
          paramArrayOfInt1[i7] = localByteVector.b;
        }
      }
      i4 = arrayOfByte[i2] & 0xFF;
      int i8;
      int i9;
      switch (ClassWriter.a[i4])
      {
      case 0: 
      case 4: 
        localByteVector.putByte(i4);
        i2++;
        break;
      case 8: 
        if (i4 > 201)
        {
          i4 = i4 < 218 ? i4 - 49 : i4 - 20;
          i5 = i2 + c(arrayOfByte, i2 + 1);
        }
        else
        {
          i5 = i2 + b(arrayOfByte, i2 + 1);
        }
        i6 = a((int[])localObject1, (int[])localObject2, i2, i5);
        if (arrayOfBoolean[i2] != 0)
        {
          if (i4 == 167)
          {
            localByteVector.putByte(200);
          }
          else if (i4 == 168)
          {
            localByteVector.putByte(201);
          }
          else
          {
            localByteVector.putByte(i4 <= 166 ? (i4 + 1 ^ 0x1) - 1 : i4 ^ 0x1);
            localByteVector.putShort(8);
            localByteVector.putByte(200);
            i6 -= 3;
          }
          localByteVector.putInt(i6);
        }
        else
        {
          localByteVector.putByte(i4);
          localByteVector.putShort(i6);
        }
        i2 += 3;
        break;
      case 9: 
        i5 = i2 + a(arrayOfByte, i2 + 1);
        i6 = a((int[])localObject1, (int[])localObject2, i2, i5);
        localByteVector.putByte(i4);
        localByteVector.putInt(i6);
        i2 += 5;
        break;
      case 13: 
        i8 = i2;
        i2 = i2 + 4 - (i8 & 0x3);
        localByteVector.putByte(170);
        while (localByteVector.b % 4 != 0) {
          localByteVector.putByte(0);
        }
        i5 = i8 + a(arrayOfByte, i2);
        i2 += 4;
        i6 = a((int[])localObject1, (int[])localObject2, i8, i5);
        localByteVector.putInt(i6);
        i9 = a(arrayOfByte, i2);
        i2 += 4;
        localByteVector.putInt(i9);
        i9 = a(arrayOfByte, i2) - i9 + 1;
        i2 += 4;
        localByteVector.putInt(a(arrayOfByte, i2 - 4));
      case 14: 
      case 16: 
      case 1: 
      case 3: 
      case 10: 
      case 2: 
      case 5: 
      case 6: 
      case 11: 
      case 12: 
      case 7: 
      case 15: 
      default: 
        while (i9 > 0)
        {
          i5 = i8 + a(arrayOfByte, i2);
          i2 += 4;
          i6 = a((int[])localObject1, (int[])localObject2, i8, i5);
          localByteVector.putInt(i6);
          i9--;
          continue;
          i8 = i2;
          i2 = i2 + 4 - (i8 & 0x3);
          localByteVector.putByte(171);
          while (localByteVector.b % 4 != 0) {
            localByteVector.putByte(0);
          }
          i5 = i8 + a(arrayOfByte, i2);
          i2 += 4;
          i6 = a((int[])localObject1, (int[])localObject2, i8, i5);
          localByteVector.putInt(i6);
          i9 = a(arrayOfByte, i2);
          i2 += 4;
          localByteVector.putInt(i9);
          while (i9 > 0)
          {
            localByteVector.putInt(a(arrayOfByte, i2));
            i2 += 4;
            i5 = i8 + a(arrayOfByte, i2);
            i2 += 4;
            i6 = a((int[])localObject1, (int[])localObject2, i8, i5);
            localByteVector.putInt(i6);
            i9--;
            continue;
            i4 = arrayOfByte[(i2 + 1)] & 0xFF;
            if (i4 == 132)
            {
              localByteVector.putByteArray(arrayOfByte, i2, 6);
              i2 += 6;
            }
            else
            {
              localByteVector.putByteArray(arrayOfByte, i2, 4);
              i2 += 4;
              break;
              localByteVector.putByteArray(arrayOfByte, i2, 2);
              i2 += 2;
              break;
              localByteVector.putByteArray(arrayOfByte, i2, 3);
              i2 += 3;
              break;
              localByteVector.putByteArray(arrayOfByte, i2, 5);
              i2 += 5;
              break;
              localByteVector.putByteArray(arrayOfByte, i2, 4);
              i2 += 4;
            }
          }
        }
      }
    }
    for (Handler localHandler = this.t; localHandler != null; localHandler = localHandler.f)
    {
      a((int[])localObject1, (int[])localObject2, localHandler.a);
      a((int[])localObject1, (int[])localObject2, localHandler.b);
      a((int[])localObject1, (int[])localObject2, localHandler.c);
    }
    for (int i7 = 0; i7 < 2; i7++)
    {
      localObject3 = i7 == 0 ? this.v : this.x;
      if (localObject3 != null)
      {
        arrayOfByte = ((ByteVector)localObject3).a;
        for (i2 = 0; i2 < ((ByteVector)localObject3).b; i2 += 10)
        {
          i5 = c(arrayOfByte, i2);
          i6 = a((int[])localObject1, (int[])localObject2, 0, i5);
          a(arrayOfByte, i2, i6);
          i5 += c(arrayOfByte, i2 + 2);
          i6 = a((int[])localObject1, (int[])localObject2, 0, i5) - i6;
          a(arrayOfByte, i2 + 2, i6);
        }
      }
    }
    if (this.z != null)
    {
      arrayOfByte = this.z.a;
      for (i2 = 0; i2 < this.z.b; i2 += 4) {
        a(arrayOfByte, i2, a((int[])localObject1, (int[])localObject2, 0, c(arrayOfByte, i2)));
      }
    }
    while (this.A != null)
    {
      localObject3 = this.A.getLabels();
      if (localObject3 != null) {
        for (i7 = localObject3.length - 1; i7 >= 0; i7--) {
          if (!localObject3[i7].c)
          {
            localObject3[i7].b = a((int[])localObject1, (int[])localObject2, 0, localObject3[i7].b);
            localObject3[i7].c = true;
          }
        }
      }
    }
    this.p = localByteVector;
    return paramArrayOfInt1;
  }
  
  static int c(byte[] paramArrayOfByte, int paramInt)
  {
    return (paramArrayOfByte[paramInt] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 1)] & 0xFF;
  }
  
  static short b(byte[] paramArrayOfByte, int paramInt)
  {
    return (short)((paramArrayOfByte[paramInt] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 1)] & 0xFF);
  }
  
  static int a(byte[] paramArrayOfByte, int paramInt)
  {
    return (paramArrayOfByte[paramInt] & 0xFF) << 24 | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 3)] & 0xFF;
  }
  
  static void a(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt1] = ((byte)(paramInt2 >>> 8));
    paramArrayOfByte[(paramInt1 + 1)] = ((byte)paramInt2);
  }
  
  static int a(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2)
  {
    int i1 = paramInt2 - paramInt1;
    for (int i2 = 0; i2 < paramArrayOfInt1.length; i2++) {
      if ((paramInt1 < paramArrayOfInt1[i2]) && (paramArrayOfInt1[i2] <= paramInt2)) {
        i1 += paramArrayOfInt2[i2];
      } else if ((paramInt2 < paramArrayOfInt1[i2]) && (paramArrayOfInt1[i2] <= paramInt1)) {
        i1 -= paramArrayOfInt2[i2];
      }
    }
    return i1;
  }
  
  static void a(int[] paramArrayOfInt1, int[] paramArrayOfInt2, Label paramLabel)
  {
    if (!paramLabel.c)
    {
      paramLabel.b = a(paramArrayOfInt1, paramArrayOfInt2, 0, paramLabel.b);
      paramLabel.c = true;
    }
  }
  
  static
  {
    int[] arrayOfInt = new int['Ê'];
    String str = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
    for (int i1 = 0; i1 < arrayOfInt.length; i1++) {
      arrayOfInt[i1] = (str.charAt(i1) - 'E');
    }
    H = arrayOfInt;
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.MethodWriter
 * JD-Core Version:    0.7.0.1
 */