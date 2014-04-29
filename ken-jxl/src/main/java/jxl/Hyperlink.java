package jxl;

import java.io.File;
import java.net.URL;

public abstract interface Hyperlink
{
  public abstract int getRow();
  
  public abstract int getColumn();
  
  public abstract Range getRange();
  
  public abstract boolean isFile();
  
  public abstract boolean isURL();
  
  public abstract boolean isLocation();
  
  public abstract int getLastRow();
  
  public abstract int getLastColumn();
  
  public abstract URL getURL();
  
  public abstract File getFile();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.Hyperlink
 * JD-Core Version:    0.7.0.1
 */