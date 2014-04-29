/*   1:    */ package org.springframework.jca.cci.object;
/*   2:    */ 
/*   3:    */ import java.sql.SQLException;
/*   4:    */ import javax.resource.ResourceException;
/*   5:    */ import javax.resource.cci.ConnectionFactory;
/*   6:    */ import javax.resource.cci.InteractionSpec;
/*   7:    */ import javax.resource.cci.Record;
/*   8:    */ import javax.resource.cci.RecordFactory;
/*   9:    */ import org.springframework.dao.DataAccessException;
/*  10:    */ import org.springframework.jca.cci.core.CciTemplate;
/*  11:    */ import org.springframework.jca.cci.core.RecordCreator;
/*  12:    */ import org.springframework.jca.cci.core.RecordExtractor;
/*  13:    */ 
/*  14:    */ public abstract class MappingRecordOperation
/*  15:    */   extends EisOperation
/*  16:    */ {
/*  17:    */   public MappingRecordOperation() {}
/*  18:    */   
/*  19:    */   public MappingRecordOperation(ConnectionFactory connectionFactory, InteractionSpec interactionSpec)
/*  20:    */   {
/*  21: 61 */     getCciTemplate().setConnectionFactory(connectionFactory);
/*  22: 62 */     setInteractionSpec(interactionSpec);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setOutputRecordCreator(RecordCreator creator)
/*  26:    */   {
/*  27: 77 */     getCciTemplate().setOutputRecordCreator(creator);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Object execute(Object inputObject)
/*  31:    */     throws DataAccessException
/*  32:    */   {
/*  33: 90 */     return getCciTemplate().execute(
/*  34: 91 */       getInteractionSpec(), new RecordCreatorImpl(inputObject), new RecordExtractorImpl());
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected abstract Record createInputRecord(RecordFactory paramRecordFactory, Object paramObject)
/*  38:    */     throws ResourceException, DataAccessException;
/*  39:    */   
/*  40:    */   protected abstract Object extractOutputData(Record paramRecord)
/*  41:    */     throws ResourceException, SQLException, DataAccessException;
/*  42:    */   
/*  43:    */   protected class RecordCreatorImpl
/*  44:    */     implements RecordCreator
/*  45:    */   {
/*  46:    */     private final Object inputObject;
/*  47:    */     
/*  48:    */     public RecordCreatorImpl(Object inObject)
/*  49:    */     {
/*  50:129 */       this.inputObject = inObject;
/*  51:    */     }
/*  52:    */     
/*  53:    */     public Record createRecord(RecordFactory recordFactory)
/*  54:    */       throws ResourceException, DataAccessException
/*  55:    */     {
/*  56:133 */       return MappingRecordOperation.this.createInputRecord(recordFactory, this.inputObject);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected class RecordExtractorImpl
/*  61:    */     implements RecordExtractor
/*  62:    */   {
/*  63:    */     protected RecordExtractorImpl() {}
/*  64:    */     
/*  65:    */     public Object extractData(Record record)
/*  66:    */       throws ResourceException, SQLException, DataAccessException
/*  67:    */     {
/*  68:145 */       return MappingRecordOperation.this.extractOutputData(record);
/*  69:    */     }
/*  70:    */   }
/*  71:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.object.MappingRecordOperation
 * JD-Core Version:    0.7.0.1
 */