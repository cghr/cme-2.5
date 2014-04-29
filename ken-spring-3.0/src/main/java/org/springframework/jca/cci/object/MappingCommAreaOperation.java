/*  1:   */ package org.springframework.jca.cci.object;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.resource.cci.ConnectionFactory;
/*  5:   */ import javax.resource.cci.InteractionSpec;
/*  6:   */ import javax.resource.cci.Record;
/*  7:   */ import javax.resource.cci.RecordFactory;
/*  8:   */ import org.springframework.dao.DataAccessException;
/*  9:   */ import org.springframework.dao.DataRetrievalFailureException;
/* 10:   */ import org.springframework.jca.cci.core.support.CommAreaRecord;
/* 11:   */ 
/* 12:   */ public abstract class MappingCommAreaOperation
/* 13:   */   extends MappingRecordOperation
/* 14:   */ {
/* 15:   */   public MappingCommAreaOperation() {}
/* 16:   */   
/* 17:   */   public MappingCommAreaOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec)
/* 18:   */   {
/* 19:53 */     super(connectionFactory, interactionSpec);
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected final Record createInputRecord(RecordFactory recordFactory, Object inObject)
/* 23:   */   {
/* 24:   */     try
/* 25:   */     {
/* 26:60 */       return new CommAreaRecord(objectToBytes(inObject));
/* 27:   */     }
/* 28:   */     catch (IOException ex)
/* 29:   */     {
/* 30:63 */       throw new DataRetrievalFailureException("I/O exception during bytes conversion", ex);
/* 31:   */     }
/* 32:   */   }
/* 33:   */   
/* 34:   */   protected final Object extractOutputData(Record record)
/* 35:   */     throws DataAccessException
/* 36:   */   {
/* 37:69 */     CommAreaRecord commAreaRecord = (CommAreaRecord)record;
/* 38:   */     try
/* 39:   */     {
/* 40:71 */       return bytesToObject(commAreaRecord.toByteArray());
/* 41:   */     }
/* 42:   */     catch (IOException ex)
/* 43:   */     {
/* 44:74 */       throw new DataRetrievalFailureException("I/O exception during bytes conversion", ex);
/* 45:   */     }
/* 46:   */   }
/* 47:   */   
/* 48:   */   protected abstract byte[] objectToBytes(Object paramObject)
/* 49:   */     throws IOException, DataAccessException;
/* 50:   */   
/* 51:   */   protected abstract Object bytesToObject(byte[] paramArrayOfByte)
/* 52:   */     throws IOException, DataAccessException;
/* 53:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.object.MappingCommAreaOperation
 * JD-Core Version:    0.7.0.1
 */