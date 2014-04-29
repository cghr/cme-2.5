package com.kentropy.cme.qa.transformation;

import java.sql.ResultSet;
import java.util.Hashtable;

public abstract interface Transformation
{
  public abstract Object transform(ResultSet paramResultSet, String paramString1, String paramString2, StringBuffer paramStringBuffer, String paramString3)
    throws Exception;
  
  public abstract Object transform(Hashtable paramHashtable, String paramString1, String paramString2, StringBuffer paramStringBuffer, String paramString3);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa-transformations\ken-qa-transformations.jar
 * Qualified Name:     com.kentropy.cme.qa.transformation.Transformation
 * JD-Core Version:    0.7.0.1
 */