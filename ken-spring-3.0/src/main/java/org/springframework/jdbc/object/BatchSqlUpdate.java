/*   1:    */ package org.springframework.jdbc.object;
/*   2:    */ 
/*   3:    */ import java.sql.PreparedStatement;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.LinkedList;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.sql.DataSource;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.springframework.dao.DataAccessException;
/*  12:    */ import org.springframework.jdbc.core.BatchPreparedStatementSetter;
/*  13:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  14:    */ import org.springframework.jdbc.core.PreparedStatementSetter;
/*  15:    */ 
/*  16:    */ public class BatchSqlUpdate
/*  17:    */   extends SqlUpdate
/*  18:    */ {
/*  19: 51 */   public static int DEFAULT_BATCH_SIZE = 5000;
/*  20: 54 */   private int batchSize = DEFAULT_BATCH_SIZE;
/*  21: 56 */   private boolean trackRowsAffected = true;
/*  22: 58 */   private final LinkedList<Object[]> parameterQueue = new LinkedList();
/*  23: 60 */   private final List<Integer> rowsAffected = new ArrayList();
/*  24:    */   
/*  25:    */   public BatchSqlUpdate() {}
/*  26:    */   
/*  27:    */   public BatchSqlUpdate(DataSource ds, String sql)
/*  28:    */   {
/*  29: 79 */     super(ds, sql);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public BatchSqlUpdate(DataSource ds, String sql, int[] types)
/*  33:    */   {
/*  34: 92 */     super(ds, sql, types);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public BatchSqlUpdate(DataSource ds, String sql, int[] types, int batchSize)
/*  38:    */   {
/*  39:108 */     super(ds, sql, types);
/*  40:109 */     setBatchSize(batchSize);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setBatchSize(int batchSize)
/*  44:    */   {
/*  45:123 */     this.batchSize = batchSize;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setTrackRowsAffected(boolean trackRowsAffected)
/*  49:    */   {
/*  50:134 */     this.trackRowsAffected = trackRowsAffected;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected boolean supportsLobParameters()
/*  54:    */   {
/*  55:142 */     return false;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int update(Object... params)
/*  59:    */     throws DataAccessException
/*  60:    */   {
/*  61:162 */     validateParameters(params);
/*  62:163 */     this.parameterQueue.add((Object[])params.clone());
/*  63:165 */     if (this.parameterQueue.size() == this.batchSize)
/*  64:    */     {
/*  65:166 */       if (this.logger.isDebugEnabled()) {
/*  66:167 */         this.logger.debug("Triggering auto-flush because queue reached batch size of " + this.batchSize);
/*  67:    */       }
/*  68:169 */       flush();
/*  69:    */     }
/*  70:172 */     return -1;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int[] flush()
/*  74:    */   {
/*  75:180 */     if (this.parameterQueue.isEmpty()) {
/*  76:181 */       return new int[0];
/*  77:    */     }
/*  78:184 */     int[] rowsAffected = getJdbcTemplate().batchUpdate(
/*  79:185 */       getSql(), 
/*  80:186 */       new BatchPreparedStatementSetter()
/*  81:    */       {
/*  82:    */         public int getBatchSize()
/*  83:    */         {
/*  84:188 */           return BatchSqlUpdate.this.parameterQueue.size();
/*  85:    */         }
/*  86:    */         
/*  87:    */         public void setValues(PreparedStatement ps, int index)
/*  88:    */           throws SQLException
/*  89:    */         {
/*  90:191 */           Object[] params = (Object[])BatchSqlUpdate.this.parameterQueue.removeFirst();
/*  91:192 */           BatchSqlUpdate.this.newPreparedStatementSetter(params).setValues(ps);
/*  92:    */         }
/*  93:    */       });
/*  94:196 */     for (int rowCount : rowsAffected)
/*  95:    */     {
/*  96:197 */       checkRowsAffected(rowCount);
/*  97:198 */       if (this.trackRowsAffected) {
/*  98:199 */         this.rowsAffected.add(Integer.valueOf(rowCount));
/*  99:    */       }
/* 100:    */     }
/* 101:203 */     return rowsAffected;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public int getQueueCount()
/* 105:    */   {
/* 106:211 */     return this.parameterQueue.size();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int getExecutionCount()
/* 110:    */   {
/* 111:218 */     return this.rowsAffected.size();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public int[] getRowsAffected()
/* 115:    */   {
/* 116:229 */     int[] result = new int[this.rowsAffected.size()];
/* 117:230 */     int i = 0;
/* 118:231 */     for (Iterator<Integer> it = this.rowsAffected.iterator(); it.hasNext(); i++) {
/* 119:232 */       result[i] = ((Integer)it.next()).intValue();
/* 120:    */     }
/* 121:234 */     return result;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void reset()
/* 125:    */   {
/* 126:242 */     this.parameterQueue.clear();
/* 127:243 */     this.rowsAffected.clear();
/* 128:    */   }
/* 129:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.object.BatchSqlUpdate
 * JD-Core Version:    0.7.0.1
 */