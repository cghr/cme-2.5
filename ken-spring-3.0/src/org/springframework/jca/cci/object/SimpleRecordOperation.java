/*  1:   */ package org.springframework.jca.cci.object;
/*  2:   */ 
/*  3:   */ import javax.resource.cci.ConnectionFactory;
/*  4:   */ import javax.resource.cci.InteractionSpec;
/*  5:   */ import javax.resource.cci.Record;
/*  6:   */ import org.springframework.dao.DataAccessException;
/*  7:   */ import org.springframework.jca.cci.core.CciTemplate;
/*  8:   */ 
/*  9:   */ public class SimpleRecordOperation
/* 10:   */   extends EisOperation
/* 11:   */ {
/* 12:   */   public SimpleRecordOperation() {}
/* 13:   */   
/* 14:   */   public SimpleRecordOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec)
/* 15:   */   {
/* 16:46 */     getCciTemplate().setConnectionFactory(connectionFactory);
/* 17:47 */     setInteractionSpec(interactionSpec);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public Record execute(Record inputRecord)
/* 21:   */     throws DataAccessException
/* 22:   */   {
/* 23:60 */     return getCciTemplate().execute(getInteractionSpec(), inputRecord);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void execute(Record inputRecord, Record outputRecord)
/* 27:   */     throws DataAccessException
/* 28:   */   {
/* 29:73 */     getCciTemplate().execute(getInteractionSpec(), inputRecord, outputRecord);
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.object.SimpleRecordOperation
 * JD-Core Version:    0.7.0.1
 */