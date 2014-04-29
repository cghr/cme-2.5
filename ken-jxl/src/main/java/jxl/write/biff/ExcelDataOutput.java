package jxl.write.biff;

import java.io.IOException;
import java.io.OutputStream;

abstract interface ExcelDataOutput
{
  public abstract void write(byte[] paramArrayOfByte)
    throws IOException;
  
  public abstract int getPosition()
    throws IOException;
  
  public abstract void setData(byte[] paramArrayOfByte, int paramInt)
    throws IOException;
  
  public abstract void writeData(OutputStream paramOutputStream)
    throws IOException;
  
  public abstract void close()
    throws IOException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ExcelDataOutput
 * JD-Core Version:    0.7.0.1
 */