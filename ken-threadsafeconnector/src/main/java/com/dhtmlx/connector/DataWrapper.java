/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ 
/*   5:    */ public abstract class DataWrapper
/*   6:    */ {
/*   7:    */   protected DataConfig config;
/*   8:    */   protected Object connection;
/*   9: 22 */   private TransactionType transaction_type = TransactionType.NONE;
/*  10:    */   
/*  11:    */   public void init(Object connection, DataConfig external_config)
/*  12:    */   {
/*  13: 32 */     this.connection = connection;
/*  14: 33 */     this.config = external_config;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public abstract void insert(DataAction paramDataAction, DataRequest paramDataRequest)
/*  18:    */     throws ConnectorOperationException;
/*  19:    */   
/*  20:    */   public abstract void delete(DataAction paramDataAction, DataRequest paramDataRequest)
/*  21:    */     throws ConnectorOperationException;
/*  22:    */   
/*  23:    */   public abstract void update(DataAction paramDataAction, DataRequest paramDataRequest)
/*  24:    */     throws ConnectorOperationException;
/*  25:    */   
/*  26:    */   public abstract ConnectorResultSet select(DataRequest paramDataRequest)
/*  27:    */     throws ConnectorOperationException;
/*  28:    */   
/*  29:    */   public abstract String get_size(DataRequest paramDataRequest)
/*  30:    */     throws ConnectorOperationException;
/*  31:    */   
/*  32:    */   public abstract ConnectorResultSet get_variants(DataRequest paramDataRequest)
/*  33:    */     throws ConnectorOperationException;
/*  34:    */   
/*  35:    */   public String get_sql(OperationType name, HashMap<String, String> data)
/*  36:    */     throws ConnectorConfigException
/*  37:    */   {
/*  38:110 */     return "";
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void begin_transaction()
/*  42:    */     throws ConnectorConfigException, ConnectorOperationException
/*  43:    */   {
/*  44:121 */     throw new ConnectorConfigException("Data wrapper not supports transactions.");
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void commit_transaction()
/*  48:    */     throws ConnectorConfigException, ConnectorOperationException
/*  49:    */   {
/*  50:131 */     throw new ConnectorConfigException("Data wrapper not supports transactions.");
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void rollback_transaction()
/*  54:    */     throws ConnectorConfigException, ConnectorOperationException
/*  55:    */   {
/*  56:141 */     throw new ConnectorConfigException("Data wrapper not supports transactions.");
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void set_transaction_mode(TransactionType mode)
/*  60:    */   {
/*  61:150 */     this.transaction_type = mode;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean is_global_transaction()
/*  65:    */   {
/*  66:159 */     return this.transaction_type == TransactionType.GLOBAL;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public boolean is_record_transaction()
/*  70:    */   {
/*  71:168 */     return this.transaction_type == TransactionType.OPERATION;
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DataWrapper
 * JD-Core Version:    0.7.0.1
 */