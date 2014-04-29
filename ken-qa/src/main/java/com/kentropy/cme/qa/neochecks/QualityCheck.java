package com.kentropy.cme.qa.neochecks;

import java.sql.ResultSet;
import java.util.Hashtable;

public abstract interface QualityCheck
{
  public abstract boolean validate(ResultSet paramResultSet1, ResultSet paramResultSet2, String paramString1, String paramString2, StringBuffer paramStringBuffer, String paramString3);
  
  public abstract boolean validate(Hashtable paramHashtable1, Hashtable paramHashtable2, ResultSet paramResultSet, String paramString1, String paramString2, StringBuffer paramStringBuffer, String paramString3);
  
  public abstract boolean validate(Hashtable paramHashtable1, Hashtable paramHashtable2, Hashtable paramHashtable3, String paramString1, String paramString2, StringBuffer paramStringBuffer, String paramString3);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-qa\ken-qa.jar
 * Qualified Name:     com.kentropy.cme.qa.neochecks.QualityCheck
 * JD-Core Version:    0.7.0.1
 */