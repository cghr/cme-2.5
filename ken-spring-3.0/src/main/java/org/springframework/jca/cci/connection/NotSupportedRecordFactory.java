/*  1:   */ package org.springframework.jca.cci.connection;
/*  2:   */ 
/*  3:   */ import javax.resource.NotSupportedException;
/*  4:   */ import javax.resource.ResourceException;
/*  5:   */ import javax.resource.cci.IndexedRecord;
/*  6:   */ import javax.resource.cci.MappedRecord;
/*  7:   */ import javax.resource.cci.RecordFactory;
/*  8:   */ 
/*  9:   */ public class NotSupportedRecordFactory
/* 10:   */   implements RecordFactory
/* 11:   */ {
/* 12:   */   public MappedRecord createMappedRecord(String name)
/* 13:   */     throws ResourceException
/* 14:   */   {
/* 15:45 */     throw new NotSupportedException("The RecordFactory facility is not supported by the connector");
/* 16:   */   }
/* 17:   */   
/* 18:   */   public IndexedRecord createIndexedRecord(String name)
/* 19:   */     throws ResourceException
/* 20:   */   {
/* 21:49 */     throw new NotSupportedException("The RecordFactory facility is not supported by the connector");
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.connection.NotSupportedRecordFactory
 * JD-Core Version:    0.7.0.1
 */