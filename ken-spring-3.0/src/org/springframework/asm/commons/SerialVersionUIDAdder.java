package org.springframework.asm.commons;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.asm.ClassAdapter;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;

public class SerialVersionUIDAdder
  extends ClassAdapter
{
  protected boolean computeSVUID;
  protected boolean hasSVUID;
  protected int access;
  protected String name;
  protected String[] interfaces;
  protected Collection svuidFields = new ArrayList();
  protected boolean hasStaticInitializer;
  protected Collection svuidConstructors = new ArrayList();
  protected Collection svuidMethods = new ArrayList();
  
  public SerialVersionUIDAdder(ClassVisitor paramClassVisitor)
  {
    super(paramClassVisitor);
  }
  
  public void visit(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString)
  {
    this.computeSVUID = ((paramInt2 & 0x200) == 0);
    if (this.computeSVUID)
    {
      this.name = paramString1;
      this.access = paramInt2;
      this.interfaces = paramArrayOfString;
    }
    super.visit(paramInt1, paramInt2, paramString1, paramString2, paramString3, paramArrayOfString);
  }
  
  public MethodVisitor visitMethod(int paramInt, String paramString1, String paramString2, String paramString3, String[] paramArrayOfString)
  {
    if (this.computeSVUID)
    {
      if (paramString1.equals("<clinit>")) {
        this.hasStaticInitializer = true;
      }
      int i = paramInt & 0xD3F;
      if ((paramInt & 0x2) == 0) {
        if (paramString1.equals("<init>")) {
          this.svuidConstructors.add(new SerialVersionUIDAdder.Item(paramString1, i, paramString2));
        } else if (!paramString1.equals("<clinit>")) {
          this.svuidMethods.add(new SerialVersionUIDAdder.Item(paramString1, i, paramString2));
        }
      }
    }
    return this.cv.visitMethod(paramInt, paramString1, paramString2, paramString3, paramArrayOfString);
  }
  
  public FieldVisitor visitField(int paramInt, String paramString1, String paramString2, String paramString3, Object paramObject)
  {
    if (this.computeSVUID)
    {
      if (paramString1.equals("serialVersionUID"))
      {
        this.computeSVUID = false;
        this.hasSVUID = true;
      }
      int i = paramInt & 0xDF;
      if (((paramInt & 0x2) == 0) || ((paramInt & 0x88) == 0)) {
        this.svuidFields.add(new SerialVersionUIDAdder.Item(paramString1, i, paramString2));
      }
    }
    return super.visitField(paramInt, paramString1, paramString2, paramString3, paramObject);
  }
  
  public void visitEnd()
  {
    if ((this.computeSVUID) && (!this.hasSVUID)) {
      try
      {
        this.cv.visitField(24, "serialVersionUID", "J", null, new Long(computeSVUID()));
      }
      catch (Throwable localThrowable)
      {
        throw new RuntimeException("Error while computing SVUID for " + this.name, localThrowable);
      }
    }
    super.visitEnd();
  }
  
  protected long computeSVUID()
    throws IOException
  {
    if (this.hasSVUID) {
      return 0L;
    }
    ByteArrayOutputStream localByteArrayOutputStream = null;
    DataOutputStream localDataOutputStream = null;
    long l = 0L;
    try
    {
      localByteArrayOutputStream = new ByteArrayOutputStream();
      localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
      localDataOutputStream.writeUTF(this.name.replace('/', '.'));
      localDataOutputStream.writeInt(this.access & 0x611);
      Arrays.sort(this.interfaces);
      for (int i = 0; i < this.interfaces.length; i++) {
        localDataOutputStream.writeUTF(this.interfaces[i].replace('/', '.'));
      }
      writeItems(this.svuidFields, localDataOutputStream, false);
      if (this.hasStaticInitializer)
      {
        localDataOutputStream.writeUTF("<clinit>");
        localDataOutputStream.writeInt(8);
        localDataOutputStream.writeUTF("()V");
      }
      writeItems(this.svuidConstructors, localDataOutputStream, true);
      writeItems(this.svuidMethods, localDataOutputStream, true);
      localDataOutputStream.flush();
      byte[] arrayOfByte = computeSHAdigest(localByteArrayOutputStream.toByteArray());
      for (int j = Math.min(arrayOfByte.length, 8) - 1; j >= 0; j--) {
        l = l << 8 | arrayOfByte[j] & 0xFF;
      }
    }
    finally
    {
      if (localDataOutputStream != null) {
        localDataOutputStream.close();
      }
    }
    return l;
  }
  
  protected byte[] computeSHAdigest(byte[] paramArrayOfByte)
  {
    try
    {
      return MessageDigest.getInstance("SHA").digest(paramArrayOfByte);
    }
    catch (Exception localException)
    {
      throw new UnsupportedOperationException(localException);
    }
  }
  
  private void writeItems(Collection paramCollection, DataOutputStream paramDataOutputStream, boolean paramBoolean)
    throws IOException
  {
    int i = paramCollection.size();
    SerialVersionUIDAdder.Item[] arrayOfItem = (SerialVersionUIDAdder.Item[])paramCollection.toArray(new SerialVersionUIDAdder.Item[i]);
    Arrays.sort(arrayOfItem);
    for (int j = 0; j < i; j++)
    {
      paramDataOutputStream.writeUTF(arrayOfItem[j].name);
      paramDataOutputStream.writeInt(arrayOfItem[j].access);
      paramDataOutputStream.writeUTF(paramBoolean ? arrayOfItem[j].desc.replace('/', '.') : arrayOfItem[j].desc);
    }
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.asm.commons.SerialVersionUIDAdder
 * JD-Core Version:    0.7.0.1
 */