package org.springframework.asm;

public class Label
{
  int k;
  boolean a;
  int b;
  boolean c;
  private int d;
  private int[] e;
  int f;
  int g;
  Edge h;
  Label i;
  boolean j;
  
  public int getOffset()
  {
    if (!this.a) {
      throw new IllegalStateException("Label offset position has not been resolved yet");
    }
    return this.b;
  }
  
  void a(MethodWriter paramMethodWriter, ByteVector paramByteVector, int paramInt, boolean paramBoolean)
  {
    if (this.a)
    {
      if (paramBoolean) {
        paramByteVector.putInt(this.b - paramInt);
      } else {
        paramByteVector.putShort(this.b - paramInt);
      }
    }
    else if (paramBoolean)
    {
      a(-1 - paramInt, paramByteVector.b);
      paramByteVector.putInt(-1);
    }
    else
    {
      a(paramInt, paramByteVector.b);
      paramByteVector.putShort(-1);
    }
  }
  
  private void a(int paramInt1, int paramInt2)
  {
    if (this.e == null) {
      this.e = new int[6];
    }
    if (this.d >= this.e.length)
    {
      int[] arrayOfInt = new int[this.e.length + 6];
      System.arraycopy(this.e, 0, arrayOfInt, 0, this.e.length);
      this.e = arrayOfInt;
    }
    this.e[(this.d++)] = paramInt1;
    this.e[(this.d++)] = paramInt2;
  }
  
  boolean a(MethodWriter paramMethodWriter, int paramInt, byte[] paramArrayOfByte)
  {
    boolean bool = false;
    this.a = true;
    this.b = paramInt;
    int m = 0;
    while (m < this.d)
    {
      int n = this.e[(m++)];
      int i1 = this.e[(m++)];
      int i2;
      if (n >= 0)
      {
        i2 = paramInt - n;
        if ((i2 < -32768) || (i2 > 32767))
        {
          int i3 = paramArrayOfByte[(i1 - 1)] & 0xFF;
          if (i3 <= 168) {
            paramArrayOfByte[(i1 - 1)] = ((byte)(i3 + 49));
          } else {
            paramArrayOfByte[(i1 - 1)] = ((byte)(i3 + 20));
          }
          bool = true;
        }
        paramArrayOfByte[(i1++)] = ((byte)(i2 >>> 8));
        paramArrayOfByte[i1] = ((byte)i2);
      }
      else
      {
        i2 = paramInt + n + 1;
        paramArrayOfByte[(i1++)] = ((byte)(i2 >>> 24));
        paramArrayOfByte[(i1++)] = ((byte)(i2 >>> 16));
        paramArrayOfByte[(i1++)] = ((byte)(i2 >>> 8));
        paramArrayOfByte[i1] = ((byte)i2);
      }
    }
    return bool;
  }
  
  public String toString()
  {
    return "L" + System.identityHashCode(this);
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.Label
 * JD-Core Version:    0.7.0.1
 */