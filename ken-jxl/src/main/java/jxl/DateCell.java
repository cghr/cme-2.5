package jxl;

import java.text.DateFormat;
import java.util.Date;

public abstract interface DateCell
  extends Cell
{
  public abstract Date getDate();
  
  public abstract boolean isTime();
  
  public abstract DateFormat getDateFormat();
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.DateCell
 * JD-Core Version:    0.7.0.1
 */