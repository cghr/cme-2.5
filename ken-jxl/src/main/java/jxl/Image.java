package jxl;

import java.io.File;
import jxl.common.LengthUnit;

public abstract interface Image
{
  public abstract double getColumn();
  
  public abstract double getRow();
  
  public abstract double getWidth();
  
  public abstract double getHeight();
  
  public abstract File getImageFile();
  
  public abstract byte[] getImageData();
  
  public abstract double getWidth(LengthUnit paramLengthUnit);
  
  public abstract double getHeight(LengthUnit paramLengthUnit);
  
  public abstract int getImageWidth();
  
  public abstract int getImageHeight();
  
  public abstract double getHorizontalResolution(LengthUnit paramLengthUnit);
  
  public abstract double getVerticalResolution(LengthUnit paramLengthUnit);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.Image
 * JD-Core Version:    0.7.0.1
 */