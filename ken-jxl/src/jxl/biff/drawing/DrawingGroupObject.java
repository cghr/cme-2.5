package jxl.biff.drawing;

import java.io.IOException;
import jxl.write.biff.File;

public abstract interface DrawingGroupObject
{
  public abstract void setObjectId(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract int getObjectId();
  
  public abstract int getBlipId();
  
  public abstract int getShapeId();
  
  public abstract MsoDrawingRecord getMsoDrawingRecord();
  
  public abstract EscherContainer getSpContainer();
  
  public abstract void setDrawingGroup(DrawingGroup paramDrawingGroup);
  
  public abstract DrawingGroup getDrawingGroup();
  
  public abstract Origin getOrigin();
  
  public abstract int getReferenceCount();
  
  public abstract void setReferenceCount(int paramInt);
  
  public abstract double getX();
  
  public abstract void setX(double paramDouble);
  
  public abstract double getY();
  
  public abstract void setY(double paramDouble);
  
  public abstract double getWidth();
  
  public abstract void setWidth(double paramDouble);
  
  public abstract double getHeight();
  
  public abstract void setHeight(double paramDouble);
  
  public abstract ShapeType getType();
  
  public abstract byte[] getImageData();
  
  public abstract byte[] getImageBytes()
    throws IOException;
  
  public abstract String getImageFilePath();
  
  public abstract void writeAdditionalRecords(File paramFile)
    throws IOException;
  
  public abstract void writeTailRecords(File paramFile)
    throws IOException;
  
  public abstract boolean isFirst();
  
  public abstract boolean isFormObject();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.DrawingGroupObject
 * JD-Core Version:    0.7.0.1
 */