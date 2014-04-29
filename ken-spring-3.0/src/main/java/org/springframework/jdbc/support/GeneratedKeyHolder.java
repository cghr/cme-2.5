/*  1:   */ package org.springframework.jdbc.support;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import java.util.LinkedList;
/*  6:   */ import java.util.List;
/*  7:   */ import java.util.Map;
/*  8:   */ import org.springframework.dao.DataRetrievalFailureException;
/*  9:   */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/* 10:   */ 
/* 11:   */ public class GeneratedKeyHolder
/* 12:   */   implements KeyHolder
/* 13:   */ {
/* 14:   */   private final List<Map<String, Object>> keyList;
/* 15:   */   
/* 16:   */   public GeneratedKeyHolder()
/* 17:   */   {
/* 18:48 */     this.keyList = new LinkedList();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public GeneratedKeyHolder(List<Map<String, Object>> keyList)
/* 22:   */   {
/* 23:56 */     this.keyList = keyList;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Number getKey()
/* 27:   */     throws InvalidDataAccessApiUsageException, DataRetrievalFailureException
/* 28:   */   {
/* 29:61 */     if (this.keyList.size() == 0) {
/* 30:62 */       return null;
/* 31:   */     }
/* 32:64 */     if ((this.keyList.size() > 1) || (((Map)this.keyList.get(0)).size() > 1)) {
/* 33:65 */       throw new InvalidDataAccessApiUsageException(
/* 34:66 */         "The getKey method should only be used when a single key is returned.  The current key entry contains multiple keys: " + 
/* 35:67 */         this.keyList);
/* 36:   */     }
/* 37:69 */     Iterator<Object> keyIter = ((Map)this.keyList.get(0)).values().iterator();
/* 38:70 */     if (keyIter.hasNext())
/* 39:   */     {
/* 40:71 */       Object key = keyIter.next();
/* 41:72 */       if (!(key instanceof Number)) {
/* 42:73 */         throw new DataRetrievalFailureException(
/* 43:74 */           "The generated key is not of a supported numeric type. Unable to cast [" + (
/* 44:75 */           key != null ? key.getClass().getName() : null) + 
/* 45:76 */           "] to [" + Number.class.getName() + "]");
/* 46:   */       }
/* 47:78 */       return (Number)key;
/* 48:   */     }
/* 49:81 */     throw new DataRetrievalFailureException("Unable to retrieve the generated key. Check that the table has an identity column enabled.");
/* 50:   */   }
/* 51:   */   
/* 52:   */   public Map<String, Object> getKeys()
/* 53:   */     throws InvalidDataAccessApiUsageException
/* 54:   */   {
/* 55:87 */     if (this.keyList.size() == 0) {
/* 56:88 */       return null;
/* 57:   */     }
/* 58:90 */     if (this.keyList.size() > 1) {
/* 59:91 */       throw new InvalidDataAccessApiUsageException(
/* 60:92 */         "The getKeys method should only be used when keys for a single row are returned.  The current key list contains keys for multiple rows: " + 
/* 61:93 */         this.keyList);
/* 62:   */     }
/* 63:94 */     return (Map)this.keyList.get(0);
/* 64:   */   }
/* 65:   */   
/* 66:   */   public List<Map<String, Object>> getKeyList()
/* 67:   */   {
/* 68:98 */     return this.keyList;
/* 69:   */   }
/* 70:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.GeneratedKeyHolder
 * JD-Core Version:    0.7.0.1
 */