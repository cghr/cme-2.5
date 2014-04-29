package org.springframework.asm;

import java.io.IOException;
import java.io.InputStream;

public class ClassReader
{
  public final byte[] b;
  private int[] a;
  private String[] c;
  private int d;
  public final int header;
  
  public ClassReader(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public ClassReader(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.b = paramArrayOfByte;
    this.a = new int[readUnsignedShort(paramInt1 + 8)];
    int i = this.a.length;
    this.c = new String[i];
    int j = 0;
    int k = paramInt1 + 10;
    for (int m = 1; m < i; m++)
    {
      this.a[m] = (k + 1);
      int n = paramArrayOfByte[k];
      int i1;
      switch (n)
      {
      case 3: 
      case 4: 
      case 9: 
      case 10: 
      case 11: 
      case 12: 
        i1 = 5;
        break;
      case 5: 
      case 6: 
        i1 = 9;
        m++;
        break;
      case 1: 
        i1 = 3 + readUnsignedShort(k + 1);
        if (i1 > j) {
          j = i1;
        }
        break;
      case 2: 
      case 7: 
      case 8: 
      default: 
        i1 = 3;
      }
      k += i1;
    }
    this.d = j;
    this.header = k;
  }
  
  void a(ClassWriter paramClassWriter)
  {
    char[] arrayOfChar = new char[this.d];
    int i = this.a.length;
    Item[] arrayOfItem = new Item[i];
    for (int j = 1; j < i; j++)
    {
      int k = this.a[j];
      int m = this.b[(k - 1)];
      Item localItem = new Item(j);
      switch (m)
      {
      case 9: 
      case 10: 
      case 11: 
        int n = this.a[readUnsignedShort(k + 2)];
        localItem.a(m, readClass(k, arrayOfChar), readUTF8(n, arrayOfChar), readUTF8(n + 2, arrayOfChar));
        break;
      case 3: 
        localItem.a(readInt(k));
        break;
      case 4: 
        localItem.a(Float.intBitsToFloat(readInt(k)));
        break;
      case 12: 
        localItem.a(m, readUTF8(k, arrayOfChar), readUTF8(k + 2, arrayOfChar), null);
        break;
      case 5: 
        localItem.a(readLong(k));
        j++;
        break;
      case 6: 
        localItem.a(Double.longBitsToDouble(readLong(k)));
        j++;
        break;
      case 1: 
        String str = this.c[j];
        if (str == null)
        {
          k = this.a[j];
          str = this.c[j] =  = a(k + 2, readUnsignedShort(k), arrayOfChar);
        }
        localItem.a(m, str, null, null);
        break;
      case 2: 
      case 7: 
      case 8: 
      default: 
        localItem.a(m, readUTF8(k, arrayOfChar), null, null);
      }
      int i1 = localItem.j % arrayOfItem.length;
      localItem.k = arrayOfItem[i1];
      arrayOfItem[i1] = localItem;
    }
    j = this.a[1] - 1;
    paramClassWriter.d.putByteArray(this.b, j, this.header - j);
    paramClassWriter.e = arrayOfItem;
    paramClassWriter.f = ((int)(0.75D * i));
    paramClassWriter.c = i;
  }
  
  public ClassReader(InputStream paramInputStream)
    throws IOException
  {
    this(a(paramInputStream));
  }
  
  public ClassReader(String paramString)
    throws IOException
  {
    this(ClassLoader.getSystemResourceAsStream(paramString.replace('.', '/') + ".class"));
  }
  
  private static byte[] a(InputStream paramInputStream)
    throws IOException
  {
    if (paramInputStream == null) {
      throw new IOException("Class not found");
    }
    Object localObject = new byte[paramInputStream.available()];
    int i = 0;
    for (;;)
    {
      int j = paramInputStream.read((byte[])localObject, i, localObject.length - i);
      byte[] arrayOfByte;
      if (j == -1)
      {
        if (i < localObject.length)
        {
          arrayOfByte = new byte[i];
          System.arraycopy(localObject, 0, arrayOfByte, 0, i);
          localObject = arrayOfByte;
        }
        return localObject;
      }
      i += j;
      if (i == localObject.length)
      {
        arrayOfByte = new byte[localObject.length + 1000];
        System.arraycopy(localObject, 0, arrayOfByte, 0, i);
        localObject = arrayOfByte;
      }
    }
  }
  
  public void accept(ClassVisitor paramClassVisitor, boolean paramBoolean)
  {
    accept(paramClassVisitor, new Attribute[0], paramBoolean);
  }
  
  public void accept(ClassVisitor paramClassVisitor, Attribute[] paramArrayOfAttribute, boolean paramBoolean)
  {
    byte[] arrayOfByte = this.b;
    char[] arrayOfChar = new char[this.d];
    int i = 0;
    int j = 0;
    Object localObject1 = null;
    int k = this.header;
    int m = readUnsignedShort(k);
    String str1 = readClass(k + 2, arrayOfChar);
    int n = this.a[readUnsignedShort(k + 4)];
    String str2 = n == 0 ? null : readUTF8(n, arrayOfChar);
    String[] arrayOfString1 = new String[readUnsignedShort(k + 6)];
    int i1 = 0;
    k += 8;
    for (int i2 = 0; i2 < arrayOfString1.length; i2++)
    {
      arrayOfString1[i2] = readClass(k, arrayOfChar);
      k += 2;
    }
    n = k;
    i2 = readUnsignedShort(n);
    n += 2;
    int i3;
    while (i2 > 0)
    {
      i3 = readUnsignedShort(n + 6);
      n += 8;
      while (i3 > 0)
      {
        n += 6 + readInt(n + 2);
        i3--;
      }
      i2--;
    }
    i2 = readUnsignedShort(n);
    n += 2;
    while (i2 > 0)
    {
      i3 = readUnsignedShort(n + 6);
      n += 8;
      while (i3 > 0)
      {
        n += 6 + readInt(n + 2);
        i3--;
      }
      i2--;
    }
    String str3 = null;
    String str4 = null;
    String str5 = null;
    String str6 = null;
    String str7 = null;
    String str8 = null;
    i2 = readUnsignedShort(n);
    n += 2;
    String str9;
    int i4;
    Attribute localAttribute;
    while (i2 > 0)
    {
      str9 = readUTF8(n, arrayOfChar);
      if (str9.equals("SourceFile"))
      {
        str4 = readUTF8(n + 6, arrayOfChar);
      }
      else if (str9.equals("Deprecated"))
      {
        m |= 0x20000;
      }
      else if (str9.equals("Synthetic"))
      {
        m |= 0x1000;
      }
      else if (str9.equals("Annotation"))
      {
        m |= 0x2000;
      }
      else if (str9.equals("Enum"))
      {
        m |= 0x4000;
      }
      else if (str9.equals("InnerClasses"))
      {
        i1 = n + 6;
      }
      else if (str9.equals("Signature"))
      {
        str3 = readUTF8(n + 6, arrayOfChar);
      }
      else if (str9.equals("SourceDebugExtension"))
      {
        i4 = readInt(n + 2);
        str5 = a(n + 6, i4, new char[i4]);
      }
      else if (str9.equals("EnclosingMethod"))
      {
        str6 = readClass(n + 6, arrayOfChar);
        i4 = readUnsignedShort(n + 8);
        if (i4 != 0)
        {
          str7 = readUTF8(this.a[i4], arrayOfChar);
          str8 = readUTF8(this.a[i4] + 2, arrayOfChar);
        }
      }
      else if (str9.equals("RuntimeVisibleAnnotations"))
      {
        i = n + 6;
      }
      else if (str9.equals("RuntimeInvisibleAnnotations"))
      {
        j = n + 6;
      }
      else
      {
        localAttribute = a(paramArrayOfAttribute, str9, n + 6, readInt(n + 2), arrayOfChar, -1, null);
        if (localAttribute != null)
        {
          localAttribute.a = localObject1;
          localObject1 = localAttribute;
        }
      }
      n += 6 + readInt(n + 2);
      i2--;
    }
    paramClassVisitor.visit(readInt(4), m, str1, str3, str2, arrayOfString1);
    if ((str4 != null) || (str5 != null)) {
      paramClassVisitor.visitSource(str4, str5);
    }
    if (str6 != null) {
      paramClassVisitor.visitOuterClass(str6, str7, str8);
    }
    String str10;
    for (i2 = 1; i2 >= 0; i2--)
    {
      n = i2 == 0 ? j : i;
      if (n != 0)
      {
        i3 = readUnsignedShort(n);
        n += 2;
        while (i3 > 0)
        {
          str10 = readUTF8(n, arrayOfChar);
          n += 2;
          n = a(n, arrayOfChar, paramClassVisitor.visitAnnotation(str10, i2 != 0));
          i3--;
        }
      }
    }
    while (localObject1 != null)
    {
      localAttribute = localObject1.a;
      localObject1.a = null;
      paramClassVisitor.visitAttribute(localObject1);
      localObject1 = localAttribute;
    }
    if (i1 != 0)
    {
      i2 = readUnsignedShort(i1);
      i1 += 2;
      while (i2 > 0)
      {
        paramClassVisitor.visitInnerClass(readUnsignedShort(i1) == 0 ? null : readClass(i1, arrayOfChar), readUnsignedShort(i1 + 2) == 0 ? null : readClass(i1 + 2, arrayOfChar), readUnsignedShort(i1 + 4) == 0 ? null : readUTF8(i1 + 4, arrayOfChar), readUnsignedShort(i1 + 6));
        i1 += 8;
        i2--;
      }
    }
    i2 = readUnsignedShort(k);
    k += 2;
    int i7;
    while (i2 > 0)
    {
      m = readUnsignedShort(k);
      str1 = readUTF8(k + 2, arrayOfChar);
      str10 = readUTF8(k + 4, arrayOfChar);
      i4 = 0;
      str3 = null;
      i = 0;
      j = 0;
      localObject1 = null;
      i3 = readUnsignedShort(k + 6);
      k += 8;
      while (i3 > 0)
      {
        str9 = readUTF8(k, arrayOfChar);
        if (str9.equals("ConstantValue"))
        {
          i4 = readUnsignedShort(k + 6);
        }
        else if (str9.equals("Synthetic"))
        {
          m |= 0x1000;
        }
        else if (str9.equals("Deprecated"))
        {
          m |= 0x20000;
        }
        else if (str9.equals("Enum"))
        {
          m |= 0x4000;
        }
        else if (str9.equals("Signature"))
        {
          str3 = readUTF8(k + 6, arrayOfChar);
        }
        else if (str9.equals("RuntimeVisibleAnnotations"))
        {
          i = k + 6;
        }
        else if (str9.equals("RuntimeInvisibleAnnotations"))
        {
          j = k + 6;
        }
        else
        {
          localAttribute = a(paramArrayOfAttribute, str9, k + 6, readInt(k + 2), arrayOfChar, -1, null);
          if (localAttribute != null)
          {
            localAttribute.a = localObject1;
            localObject1 = localAttribute;
          }
        }
        k += 6 + readInt(k + 2);
        i3--;
      }
      Object localObject2 = i4 == 0 ? null : readConst(i4, arrayOfChar);
      FieldVisitor localFieldVisitor = paramClassVisitor.visitField(m, str1, str10, str3, localObject2);
      if (localFieldVisitor != null)
      {
        for (i3 = 1; i3 >= 0; i3--)
        {
          n = i3 == 0 ? j : i;
          if (n != 0)
          {
            i7 = readUnsignedShort(n);
            n += 2;
            while (i7 > 0)
            {
              str10 = readUTF8(n, arrayOfChar);
              n += 2;
              n = a(n, arrayOfChar, localFieldVisitor.visitAnnotation(str10, i3 != 0));
              i7--;
            }
          }
        }
        while (localObject1 != null)
        {
          localAttribute = localObject1.a;
          localObject1.a = null;
          localFieldVisitor.visitAttribute(localObject1);
          localObject1 = localAttribute;
        }
        localFieldVisitor.visitEnd();
      }
      i2--;
    }
    i2 = readUnsignedShort(k);
    k += 2;
    while (i2 > 0)
    {
      i4 = k + 6;
      m = readUnsignedShort(k);
      str1 = readUTF8(k + 2, arrayOfChar);
      str10 = readUTF8(k + 4, arrayOfChar);
      str3 = null;
      i = 0;
      j = 0;
      int i5 = 0;
      int i6 = 0;
      int i8 = 0;
      localObject1 = null;
      n = 0;
      i1 = 0;
      i3 = readUnsignedShort(k + 6);
      k += 8;
      while (i3 > 0)
      {
        str9 = readUTF8(k, arrayOfChar);
        k += 2;
        int i9 = readInt(k);
        k += 4;
        if (str9.equals("Code"))
        {
          n = k;
        }
        else if (str9.equals("Exceptions"))
        {
          i1 = k;
        }
        else if (str9.equals("Synthetic"))
        {
          m |= 0x1000;
        }
        else if (str9.equals("Varargs"))
        {
          m |= 0x80;
        }
        else if (str9.equals("Bridge"))
        {
          m |= 0x40;
        }
        else if (str9.equals("Deprecated"))
        {
          m |= 0x20000;
        }
        else if (str9.equals("Signature"))
        {
          str3 = readUTF8(k, arrayOfChar);
        }
        else if (str9.equals("AnnotationDefault"))
        {
          i5 = k;
        }
        else if (str9.equals("RuntimeVisibleAnnotations"))
        {
          i = k;
        }
        else if (str9.equals("RuntimeInvisibleAnnotations"))
        {
          j = k;
        }
        else if (str9.equals("RuntimeVisibleParameterAnnotations"))
        {
          i6 = k;
        }
        else if (str9.equals("RuntimeInvisibleParameterAnnotations"))
        {
          i8 = k;
        }
        else
        {
          localAttribute = a(paramArrayOfAttribute, str9, k, i9, arrayOfChar, -1, null);
          if (localAttribute != null)
          {
            localAttribute.a = localObject1;
            localObject1 = localAttribute;
          }
        }
        k += i9;
        i3--;
      }
      String[] arrayOfString2;
      if (i1 == 0)
      {
        arrayOfString2 = null;
      }
      else
      {
        arrayOfString2 = new String[readUnsignedShort(i1)];
        i1 += 2;
        for (i3 = 0; i3 < arrayOfString2.length; i3++)
        {
          arrayOfString2[i3] = readClass(i1, arrayOfChar);
          i1 += 2;
        }
      }
      MethodVisitor localMethodVisitor = paramClassVisitor.visitMethod(m, str1, str10, str3, arrayOfString2);
      int i11;
      if (localMethodVisitor != null)
      {
        Object localObject3;
        if ((localMethodVisitor instanceof MethodWriter))
        {
          localObject3 = (MethodWriter)localMethodVisitor;
          if ((((MethodWriter)localObject3).b.D == this) && (str3 == ((MethodWriter)localObject3).g))
          {
            i11 = 0;
            if (arrayOfString2 == null)
            {
              i11 = ((MethodWriter)localObject3).h == 0 ? 1 : 0;
            }
            else if (arrayOfString2.length == ((MethodWriter)localObject3).h)
            {
              i11 = 1;
              for (i3 = arrayOfString2.length - 1; i3 >= 0; i3--)
              {
                i1 -= 2;
                if (localObject3.i[i3] != readUnsignedShort(i1))
                {
                  i11 = 0;
                  break;
                }
              }
            }
            if (i11 != 0)
            {
              ((MethodWriter)localObject3).I = i4;
              ((MethodWriter)localObject3).J = (k - i4);
              break label4712;
            }
          }
        }
        if (i5 != 0)
        {
          localObject3 = localMethodVisitor.visitAnnotationDefault();
          a(i5, arrayOfChar, null, (AnnotationVisitor)localObject3);
          ((AnnotationVisitor)localObject3).visitEnd();
        }
        for (i3 = 1; i3 >= 0; i3--)
        {
          i1 = i3 == 0 ? j : i;
          if (i1 != 0)
          {
            i7 = readUnsignedShort(i1);
            i1 += 2;
            while (i7 > 0)
            {
              str10 = readUTF8(i1, arrayOfChar);
              i1 += 2;
              i1 = a(i1, arrayOfChar, localMethodVisitor.visitAnnotation(str10, i3 != 0));
              i7--;
            }
          }
        }
        if (i6 != 0) {
          a(i6, arrayOfChar, true, localMethodVisitor);
        }
        if (i8 != 0) {
          a(i8, arrayOfChar, false, localMethodVisitor);
        }
        while (localObject1 != null)
        {
          localAttribute = localObject1.a;
          localObject1.a = null;
          localMethodVisitor.visitAttribute(localObject1);
          localObject1 = localAttribute;
        }
      }
      else
      {
        if ((localMethodVisitor != null) && (n != 0))
        {
          int i10 = readUnsignedShort(n);
          i11 = readUnsignedShort(n + 2);
          int i12 = readInt(n + 4);
          n += 8;
          int i13 = n;
          int i14 = n + i12;
          localMethodVisitor.visitCode();
          Label[] arrayOfLabel1 = new Label[i12 + 1];
          int i17;
          while (n < i14)
          {
            int i15 = arrayOfByte[n] & 0xFF;
            switch (ClassWriter.a[i15])
            {
            case 0: 
            case 4: 
              n++;
              break;
            case 8: 
              i17 = n - i13 + readShort(n + 1);
              if (arrayOfLabel1[i17] == null) {
                arrayOfLabel1[i17] = new Label();
              }
              n += 3;
              break;
            case 9: 
              i17 = n - i13 + readInt(n + 1);
              if (arrayOfLabel1[i17] == null) {
                arrayOfLabel1[i17] = new Label();
              }
              n += 5;
              break;
            case 16: 
              i15 = arrayOfByte[(n + 1)] & 0xFF;
              if (i15 == 132) {
                n += 6;
              } else {
                n += 4;
              }
              break;
            case 13: 
              i1 = n - i13;
              n = n + 4 - (i1 & 0x3);
              i17 = i1 + readInt(n);
              n += 4;
              if (arrayOfLabel1[i17] == null) {
                arrayOfLabel1[i17] = new Label();
              }
              i3 = readInt(n);
              n += 4;
              i3 = readInt(n) - i3 + 1;
              n += 4;
            case 14: 
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
              while (i3 > 0)
              {
                i17 = i1 + readInt(n);
                n += 4;
                if (arrayOfLabel1[i17] == null) {
                  arrayOfLabel1[i17] = new Label();
                }
                i3--;
                continue;
                i1 = n - i13;
                n = n + 4 - (i1 & 0x3);
                i17 = i1 + readInt(n);
                n += 4;
                if (arrayOfLabel1[i17] == null) {
                  arrayOfLabel1[i17] = new Label();
                }
                i3 = readInt(n);
                n += 4;
                while (i3 > 0)
                {
                  n += 4;
                  i17 = i1 + readInt(n);
                  n += 4;
                  if (arrayOfLabel1[i17] == null) {
                    arrayOfLabel1[i17] = new Label();
                  }
                  i3--;
                  continue;
                  n += 2;
                  break;
                  n += 3;
                  break;
                  n += 5;
                  break;
                  n += 4;
                }
              }
            }
          }
          i3 = readUnsignedShort(n);
          n += 2;
          int i19;
          while (i3 > 0)
          {
            i17 = readUnsignedShort(n);
            Label localLabel1 = arrayOfLabel1[i17];
            if (localLabel1 == null)
            {
              void tmp2909_2906 = new Label();
              localLabel1 = tmp2909_2906;
              arrayOfLabel1[i17] = tmp2909_2906;
            }
            i17 = readUnsignedShort(n + 2);
            Label localLabel2 = arrayOfLabel1[i17];
            if (localLabel2 == null)
            {
              void tmp2946_2943 = new Label();
              localLabel2 = tmp2946_2943;
              arrayOfLabel1[i17] = tmp2946_2943;
            }
            i17 = readUnsignedShort(n + 4);
            localLabel3 = arrayOfLabel1[i17];
            if (localLabel3 == null)
            {
              void tmp2983_2980 = new Label();
              localLabel3 = tmp2983_2980;
              arrayOfLabel1[i17] = tmp2983_2980;
            }
            i19 = readUnsignedShort(n + 6);
            if (i19 == 0) {
              localMethodVisitor.visitTryCatchBlock(localLabel1, localLabel2, localLabel3, null);
            } else {
              localMethodVisitor.visitTryCatchBlock(localLabel1, localLabel2, localLabel3, readUTF8(this.a[i19], arrayOfChar));
            }
            n += 8;
            i3--;
          }
          int i16 = 0;
          int i18 = 0;
          localObject1 = null;
          i3 = readUnsignedShort(n);
          n += 2;
          while (i3 > 0)
          {
            str9 = readUTF8(n, arrayOfChar);
            if (str9.equals("LocalVariableTable"))
            {
              if (!paramBoolean)
              {
                i16 = n + 6;
                i7 = readUnsignedShort(n + 6);
                i1 = n + 8;
                while (i7 > 0)
                {
                  i17 = readUnsignedShort(i1);
                  if (arrayOfLabel1[i17] == null) {
                    arrayOfLabel1[i17] = new Label();
                  }
                  i17 += readUnsignedShort(i1 + 2);
                  if (arrayOfLabel1[i17] == null) {
                    arrayOfLabel1[i17] = new Label();
                  }
                  i1 += 10;
                  i7--;
                }
              }
            }
            else if (str9.equals("LocalVariableTypeTable")) {
              i18 = n + 6;
            } else if (str9.equals("LineNumberTable"))
            {
              if (!paramBoolean)
              {
                i7 = readUnsignedShort(n + 6);
                i1 = n + 8;
                while (i7 > 0)
                {
                  i17 = readUnsignedShort(i1);
                  if (arrayOfLabel1[i17] == null) {
                    arrayOfLabel1[i17] = new Label();
                  }
                  arrayOfLabel1[i17].k = readUnsignedShort(i1 + 2);
                  i1 += 4;
                  i7--;
                }
              }
            }
            else {
              for (i7 = 0; i7 < paramArrayOfAttribute.length; i7++) {
                if (paramArrayOfAttribute[i7].type.equals(str9))
                {
                  localAttribute = paramArrayOfAttribute[i7].read(this, n + 6, readInt(n + 2), arrayOfChar, i13 - 8, arrayOfLabel1);
                  if (localAttribute != null)
                  {
                    localAttribute.a = localObject1;
                    localObject1 = localAttribute;
                  }
                }
              }
            }
            n += 6 + readInt(n + 2);
            i3--;
          }
          n = i13;
          int i20;
          int i21;
          Object localObject4;
          while (n < i14)
          {
            i1 = n - i13;
            localLabel3 = arrayOfLabel1[i1];
            if (localLabel3 != null)
            {
              localMethodVisitor.visitLabel(localLabel3);
              if ((!paramBoolean) && (localLabel3.k > 0)) {
                localMethodVisitor.visitLineNumber(localLabel3.k, localLabel3);
              }
            }
            i19 = arrayOfByte[n] & 0xFF;
            switch (ClassWriter.a[i19])
            {
            case 0: 
              localMethodVisitor.visitInsn(i19);
              n++;
              break;
            case 4: 
              if (i19 > 54)
              {
                i19 -= 59;
                localMethodVisitor.visitVarInsn(54 + (i19 >> 2), i19 & 0x3);
              }
              else
              {
                i19 -= 26;
                localMethodVisitor.visitVarInsn(21 + (i19 >> 2), i19 & 0x3);
              }
              n++;
              break;
            case 8: 
              localMethodVisitor.visitJumpInsn(i19, arrayOfLabel1[(i1 + readShort(n + 1))]);
              n += 3;
              break;
            case 9: 
              localMethodVisitor.visitJumpInsn(i19 - 33, arrayOfLabel1[(i1 + readInt(n + 1))]);
              n += 5;
              break;
            case 16: 
              i19 = arrayOfByte[(n + 1)] & 0xFF;
              if (i19 == 132)
              {
                localMethodVisitor.visitIincInsn(readUnsignedShort(n + 2), readShort(n + 4));
                n += 6;
              }
              else
              {
                localMethodVisitor.visitVarInsn(i19, readUnsignedShort(n + 2));
                n += 4;
              }
              break;
            case 13: 
              n = n + 4 - (i1 & 0x3);
              i17 = i1 + readInt(n);
              n += 4;
              i20 = readInt(n);
              n += 4;
              i21 = readInt(n);
              n += 4;
              Label[] arrayOfLabel2 = new Label[i21 - i20 + 1];
              for (i3 = 0; i3 < arrayOfLabel2.length; i3++)
              {
                arrayOfLabel2[i3] = arrayOfLabel1[(i1 + readInt(n))];
                n += 4;
              }
              localMethodVisitor.visitTableSwitchInsn(i20, i21, arrayOfLabel1[i17], arrayOfLabel2);
              break;
            case 14: 
              n = n + 4 - (i1 & 0x3);
              i17 = i1 + readInt(n);
              n += 4;
              i3 = readInt(n);
              n += 4;
              localObject4 = new int[i3];
              Label[] arrayOfLabel3 = new Label[i3];
              for (i3 = 0; i3 < localObject4.length; i3++)
              {
                localObject4[i3] = readInt(n);
                n += 4;
                arrayOfLabel3[i3] = arrayOfLabel1[(i1 + readInt(n))];
                n += 4;
              }
              localMethodVisitor.visitLookupSwitchInsn(arrayOfLabel1[i17], (int[])localObject4, arrayOfLabel3);
              break;
            case 3: 
              localMethodVisitor.visitVarInsn(i19, arrayOfByte[(n + 1)] & 0xFF);
              n += 2;
              break;
            case 1: 
              localMethodVisitor.visitIntInsn(i19, arrayOfByte[(n + 1)]);
              n += 2;
              break;
            case 2: 
              localMethodVisitor.visitIntInsn(i19, readShort(n + 1));
              n += 3;
              break;
            case 10: 
              localMethodVisitor.visitLdcInsn(readConst(arrayOfByte[(n + 1)] & 0xFF, arrayOfChar));
              n += 2;
              break;
            case 11: 
              localMethodVisitor.visitLdcInsn(readConst(readUnsignedShort(n + 1), arrayOfChar));
              n += 3;
              break;
            case 6: 
            case 7: 
              int i24 = this.a[readUnsignedShort(n + 1)];
              String str11 = readClass(i24, arrayOfChar);
              i24 = this.a[readUnsignedShort(i24 + 2)];
              String str12 = readUTF8(i24, arrayOfChar);
              String str13 = readUTF8(i24 + 2, arrayOfChar);
              if (i19 < 182) {
                localMethodVisitor.visitFieldInsn(i19, str11, str12, str13);
              } else {
                localMethodVisitor.visitMethodInsn(i19, str11, str12, str13);
              }
              if (i19 == 185) {
                n += 5;
              } else {
                n += 3;
              }
              break;
            case 5: 
              localMethodVisitor.visitTypeInsn(i19, readClass(n + 1, arrayOfChar));
              n += 3;
              break;
            case 12: 
              localMethodVisitor.visitIincInsn(arrayOfByte[(n + 1)] & 0xFF, arrayOfByte[(n + 2)]);
              n += 3;
              break;
            case 15: 
            default: 
              localMethodVisitor.visitMultiANewArrayInsn(readClass(n + 1, arrayOfChar), arrayOfByte[(n + 3)] & 0xFF);
              n += 4;
            }
          }
          Label localLabel3 = arrayOfLabel1[(i14 - i13)];
          if (localLabel3 != null) {
            localMethodVisitor.visitLabel(localLabel3);
          }
          if ((!paramBoolean) && (i16 != 0))
          {
            int[] arrayOfInt = null;
            if (i18 != 0)
            {
              i1 = i18;
              i7 = readUnsignedShort(i1) * 3;
              i1 += 2;
              arrayOfInt = new int[i7];
              while (i7 > 0)
              {
                arrayOfInt[(--i7)] = (i1 + 6);
                arrayOfInt[(--i7)] = readUnsignedShort(i1 + 8);
                arrayOfInt[(--i7)] = readUnsignedShort(i1);
                i1 += 10;
              }
            }
            i1 = i16;
            i7 = readUnsignedShort(i1);
            i1 += 2;
            while (i7 > 0)
            {
              i20 = readUnsignedShort(i1);
              i21 = readUnsignedShort(i1 + 2);
              int i22 = readUnsignedShort(i1 + 8);
              localObject4 = null;
              if (arrayOfInt != null) {
                for (int i23 = 0; i23 < arrayOfInt.length; i23 += 3) {
                  if ((arrayOfInt[i23] == i20) && (arrayOfInt[(i23 + 1)] == i22))
                  {
                    localObject4 = readUTF8(arrayOfInt[(i23 + 2)], arrayOfChar);
                    break;
                  }
                }
              }
              localMethodVisitor.visitLocalVariable(readUTF8(i1 + 4, arrayOfChar), readUTF8(i1 + 6, arrayOfChar), (String)localObject4, arrayOfLabel1[i20], arrayOfLabel1[(i20 + i21)], i22);
              i1 += 10;
              i7--;
            }
          }
          while (localObject1 != null)
          {
            localAttribute = localObject1.a;
            localObject1.a = null;
            localMethodVisitor.visitAttribute(localObject1);
            localObject1 = localAttribute;
          }
          localMethodVisitor.visitMaxs(i10, i11);
        }
        if (localMethodVisitor != null) {
          localMethodVisitor.visitEnd();
        }
      }
      label4712:
      i2--;
    }
    paramClassVisitor.visitEnd();
  }
  
  private void a(int paramInt, char[] paramArrayOfChar, boolean paramBoolean, MethodVisitor paramMethodVisitor)
  {
    int i = this.b[(paramInt++)] & 0xFF;
    for (int j = 0; j < i; j++)
    {
      int k = readUnsignedShort(paramInt);
      paramInt += 2;
      while (k > 0)
      {
        String str = readUTF8(paramInt, paramArrayOfChar);
        paramInt += 2;
        AnnotationVisitor localAnnotationVisitor = paramMethodVisitor.visitParameterAnnotation(j, str, paramBoolean);
        paramInt = a(paramInt, paramArrayOfChar, localAnnotationVisitor);
        k--;
      }
    }
  }
  
  private int a(int paramInt, char[] paramArrayOfChar, AnnotationVisitor paramAnnotationVisitor)
  {
    int i = readUnsignedShort(paramInt);
    paramInt += 2;
    while (i > 0)
    {
      String str = readUTF8(paramInt, paramArrayOfChar);
      paramInt += 2;
      paramInt = a(paramInt, paramArrayOfChar, str, paramAnnotationVisitor);
      i--;
    }
    paramAnnotationVisitor.visitEnd();
    return paramInt;
  }
  
  private int a(int paramInt, char[] paramArrayOfChar, String paramString, AnnotationVisitor paramAnnotationVisitor)
  {
    switch (readByte(paramInt++))
    {
    case 68: 
    case 70: 
    case 73: 
    case 74: 
      paramAnnotationVisitor.visit(paramString, readConst(readUnsignedShort(paramInt), paramArrayOfChar));
      paramInt += 2;
      break;
    case 66: 
      paramAnnotationVisitor.visit(paramString, new Byte((byte)readInt(this.a[readUnsignedShort(paramInt)])));
      paramInt += 2;
      break;
    case 90: 
      int i = readInt(this.a[readUnsignedShort(paramInt)]) == 0 ? 1 : 0;
      paramAnnotationVisitor.visit(paramString, i != 0 ? Boolean.FALSE : Boolean.TRUE);
      paramInt += 2;
      break;
    case 83: 
      paramAnnotationVisitor.visit(paramString, new Short((short)readInt(this.a[readUnsignedShort(paramInt)])));
      paramInt += 2;
      break;
    case 67: 
      paramAnnotationVisitor.visit(paramString, new Character((char)readInt(this.a[readUnsignedShort(paramInt)])));
      paramInt += 2;
      break;
    case 115: 
      paramAnnotationVisitor.visit(paramString, readUTF8(paramInt, paramArrayOfChar));
      paramInt += 2;
      break;
    case 101: 
      paramAnnotationVisitor.visitEnum(paramString, readUTF8(paramInt, paramArrayOfChar), readUTF8(paramInt + 2, paramArrayOfChar));
      paramInt += 4;
      break;
    case 99: 
      paramAnnotationVisitor.visit(paramString, Type.getType(readUTF8(paramInt, paramArrayOfChar)));
      paramInt += 2;
      break;
    case 64: 
      String str = readUTF8(paramInt, paramArrayOfChar);
      paramInt += 2;
      paramInt = a(paramInt, paramArrayOfChar, paramAnnotationVisitor.visitAnnotation(paramString, str));
      break;
    case 91: 
      int j = readUnsignedShort(paramInt);
      paramInt += 2;
      if (j == 0)
      {
        paramAnnotationVisitor.visitArray(paramString).visitEnd();
        return paramInt;
      }
      int k;
      switch (readByte(paramInt++))
      {
      case 66: 
        byte[] arrayOfByte = new byte[j];
        for (k = 0; k < j; k++)
        {
          arrayOfByte[k] = ((byte)readInt(this.a[readUnsignedShort(paramInt)]));
          paramInt += 3;
        }
        paramAnnotationVisitor.visit(paramString, arrayOfByte);
        paramInt--;
        break;
      case 90: 
        boolean[] arrayOfBoolean = new boolean[j];
        for (k = 0; k < j; k++)
        {
          arrayOfBoolean[k] = (readInt(this.a[readUnsignedShort(paramInt)]) != 0 ? 1 : false);
          paramInt += 3;
        }
        paramAnnotationVisitor.visit(paramString, arrayOfBoolean);
        paramInt--;
        break;
      case 83: 
        short[] arrayOfShort = new short[j];
        for (k = 0; k < j; k++)
        {
          arrayOfShort[k] = ((short)readInt(this.a[readUnsignedShort(paramInt)]));
          paramInt += 3;
        }
        paramAnnotationVisitor.visit(paramString, arrayOfShort);
        paramInt--;
        break;
      case 67: 
        char[] arrayOfChar = new char[j];
        for (k = 0; k < j; k++)
        {
          arrayOfChar[k] = ((char)readInt(this.a[readUnsignedShort(paramInt)]));
          paramInt += 3;
        }
        paramAnnotationVisitor.visit(paramString, arrayOfChar);
        paramInt--;
        break;
      case 73: 
        int[] arrayOfInt = new int[j];
        for (k = 0; k < j; k++)
        {
          arrayOfInt[k] = readInt(this.a[readUnsignedShort(paramInt)]);
          paramInt += 3;
        }
        paramAnnotationVisitor.visit(paramString, arrayOfInt);
        paramInt--;
        break;
      case 74: 
        long[] arrayOfLong = new long[j];
        for (k = 0; k < j; k++)
        {
          arrayOfLong[k] = readLong(this.a[readUnsignedShort(paramInt)]);
          paramInt += 3;
        }
        paramAnnotationVisitor.visit(paramString, arrayOfLong);
        paramInt--;
        break;
      case 70: 
        float[] arrayOfFloat = new float[j];
        for (k = 0; k < j; k++)
        {
          arrayOfFloat[k] = Float.intBitsToFloat(readInt(this.a[readUnsignedShort(paramInt)]));
          paramInt += 3;
        }
        paramAnnotationVisitor.visit(paramString, arrayOfFloat);
        paramInt--;
        break;
      case 68: 
        double[] arrayOfDouble = new double[j];
        for (k = 0; k < j; k++)
        {
          arrayOfDouble[k] = Double.longBitsToDouble(readLong(this.a[readUnsignedShort(paramInt)]));
          paramInt += 3;
        }
        paramAnnotationVisitor.visit(paramString, arrayOfDouble);
        paramInt--;
        break;
      case 69: 
      case 71: 
      case 72: 
      case 75: 
      case 76: 
      case 77: 
      case 78: 
      case 79: 
      case 80: 
      case 81: 
      case 82: 
      case 84: 
      case 85: 
      case 86: 
      case 87: 
      case 88: 
      case 89: 
      default: 
        paramInt--;
        AnnotationVisitor localAnnotationVisitor = paramAnnotationVisitor.visitArray(paramString);
        for (k = j; k > 0; k--) {
          paramInt = a(paramInt, paramArrayOfChar, null, localAnnotationVisitor);
        }
        localAnnotationVisitor.visitEnd();
      }
      break;
    }
    return paramInt;
  }
  
  private Attribute a(Attribute[] paramArrayOfAttribute, String paramString, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, Label[] paramArrayOfLabel)
  {
    for (int i = 0; i < paramArrayOfAttribute.length; i++) {
      if (paramArrayOfAttribute[i].type.equals(paramString)) {
        return paramArrayOfAttribute[i].read(this, paramInt1, paramInt2, paramArrayOfChar, paramInt3, paramArrayOfLabel);
      }
    }
    return new Attribute(paramString).read(this, paramInt1, paramInt2, null, -1, null);
  }
  
  public int getItem(int paramInt)
  {
    return this.a[paramInt];
  }
  
  public int readByte(int paramInt)
  {
    return this.b[paramInt] & 0xFF;
  }
  
  public int readUnsignedShort(int paramInt)
  {
    byte[] arrayOfByte = this.b;
    return (arrayOfByte[paramInt] & 0xFF) << 8 | arrayOfByte[(paramInt + 1)] & 0xFF;
  }
  
  public short readShort(int paramInt)
  {
    byte[] arrayOfByte = this.b;
    return (short)((arrayOfByte[paramInt] & 0xFF) << 8 | arrayOfByte[(paramInt + 1)] & 0xFF);
  }
  
  public int readInt(int paramInt)
  {
    byte[] arrayOfByte = this.b;
    return (arrayOfByte[paramInt] & 0xFF) << 24 | (arrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (arrayOfByte[(paramInt + 2)] & 0xFF) << 8 | arrayOfByte[(paramInt + 3)] & 0xFF;
  }
  
  public long readLong(int paramInt)
  {
    long l1 = readInt(paramInt);
    long l2 = readInt(paramInt + 4) & 0xFFFFFFFF;
    return l1 << 32 | l2;
  }
  
  public String readUTF8(int paramInt, char[] paramArrayOfChar)
  {
    int i = readUnsignedShort(paramInt);
    String str = this.c[i];
    if (str != null) {
      return str;
    }
    paramInt = this.a[i];
    return this.c[i] =  = a(paramInt + 2, readUnsignedShort(paramInt), paramArrayOfChar);
  }
  
  private String a(int paramInt1, int paramInt2, char[] paramArrayOfChar)
  {
    int i = paramInt1 + paramInt2;
    byte[] arrayOfByte = this.b;
    int j = 0;
    while (paramInt1 < i)
    {
      int k = arrayOfByte[(paramInt1++)] & 0xFF;
      int m;
      switch (k >> 4)
      {
      case 0: 
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
        paramArrayOfChar[(j++)] = ((char)k);
        break;
      case 12: 
      case 13: 
        m = arrayOfByte[(paramInt1++)];
        paramArrayOfChar[(j++)] = ((char)((k & 0x1F) << 6 | m & 0x3F));
        break;
      case 8: 
      case 9: 
      case 10: 
      case 11: 
      default: 
        m = arrayOfByte[(paramInt1++)];
        int n = arrayOfByte[(paramInt1++)];
        paramArrayOfChar[(j++)] = ((char)((k & 0xF) << 12 | (m & 0x3F) << 6 | n & 0x3F));
      }
    }
    return new String(paramArrayOfChar, 0, j);
  }
  
  public String readClass(int paramInt, char[] paramArrayOfChar)
  {
    return readUTF8(this.a[readUnsignedShort(paramInt)], paramArrayOfChar);
  }
  
  public Object readConst(int paramInt, char[] paramArrayOfChar)
  {
    int i = this.a[paramInt];
    switch (this.b[(i - 1)])
    {
    case 3: 
      return new Integer(readInt(i));
    case 4: 
      return new Float(Float.intBitsToFloat(readInt(i)));
    case 5: 
      return new Long(readLong(i));
    case 6: 
      return new Double(Double.longBitsToDouble(readLong(i)));
    case 7: 
      String str = readUTF8(i, paramArrayOfChar);
      return Type.getType("L" + str + ";");
    }
    return readUTF8(i, paramArrayOfChar);
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.ClassReader
 * JD-Core Version:    0.7.0.1
 */