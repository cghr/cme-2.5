/*   1:    */ package org.springframework.jca.cci.core;
/*   2:    */ 
/*   3:    */ import java.sql.SQLException;
/*   4:    */ import javax.resource.NotSupportedException;
/*   5:    */ import javax.resource.ResourceException;
/*   6:    */ import javax.resource.cci.Connection;
/*   7:    */ import javax.resource.cci.ConnectionFactory;
/*   8:    */ import javax.resource.cci.ConnectionSpec;
/*   9:    */ import javax.resource.cci.IndexedRecord;
/*  10:    */ import javax.resource.cci.Interaction;
/*  11:    */ import javax.resource.cci.InteractionSpec;
/*  12:    */ import javax.resource.cci.MappedRecord;
/*  13:    */ import javax.resource.cci.Record;
/*  14:    */ import javax.resource.cci.RecordFactory;
/*  15:    */ import javax.resource.cci.ResultSet;
/*  16:    */ import org.apache.commons.logging.Log;
/*  17:    */ import org.apache.commons.logging.LogFactory;
/*  18:    */ import org.springframework.dao.DataAccessException;
/*  19:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  20:    */ import org.springframework.jca.cci.CannotCreateRecordException;
/*  21:    */ import org.springframework.jca.cci.CciOperationNotSupportedException;
/*  22:    */ import org.springframework.jca.cci.InvalidResultSetAccessException;
/*  23:    */ import org.springframework.jca.cci.RecordTypeNotSupportedException;
/*  24:    */ import org.springframework.jca.cci.connection.ConnectionFactoryUtils;
/*  25:    */ import org.springframework.jca.cci.connection.NotSupportedRecordFactory;
/*  26:    */ import org.springframework.util.Assert;
/*  27:    */ 
/*  28:    */ public class CciTemplate
/*  29:    */   implements CciOperations
/*  30:    */ {
/*  31: 72 */   private final Log logger = LogFactory.getLog(getClass());
/*  32:    */   private ConnectionFactory connectionFactory;
/*  33:    */   private ConnectionSpec connectionSpec;
/*  34:    */   private RecordCreator outputRecordCreator;
/*  35:    */   
/*  36:    */   public CciTemplate() {}
/*  37:    */   
/*  38:    */   public CciTemplate(ConnectionFactory connectionFactory)
/*  39:    */   {
/*  40: 95 */     setConnectionFactory(connectionFactory);
/*  41: 96 */     afterPropertiesSet();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public CciTemplate(ConnectionFactory connectionFactory, ConnectionSpec connectionSpec)
/*  45:    */   {
/*  46:107 */     setConnectionFactory(connectionFactory);
/*  47:108 */     setConnectionSpec(connectionSpec);
/*  48:109 */     afterPropertiesSet();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setConnectionFactory(ConnectionFactory connectionFactory)
/*  52:    */   {
/*  53:117 */     this.connectionFactory = connectionFactory;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public ConnectionFactory getConnectionFactory()
/*  57:    */   {
/*  58:124 */     return this.connectionFactory;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setConnectionSpec(ConnectionSpec connectionSpec)
/*  62:    */   {
/*  63:132 */     this.connectionSpec = connectionSpec;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public ConnectionSpec getConnectionSpec()
/*  67:    */   {
/*  68:139 */     return this.connectionSpec;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setOutputRecordCreator(RecordCreator creator)
/*  72:    */   {
/*  73:155 */     this.outputRecordCreator = creator;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public RecordCreator getOutputRecordCreator()
/*  77:    */   {
/*  78:162 */     return this.outputRecordCreator;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void afterPropertiesSet()
/*  82:    */   {
/*  83:166 */     if (getConnectionFactory() == null) {
/*  84:167 */       throw new IllegalArgumentException("Property 'connectionFactory' is required");
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   public CciTemplate getDerivedTemplate(ConnectionSpec connectionSpec)
/*  89:    */   {
/*  90:182 */     CciTemplate derived = new CciTemplate();
/*  91:183 */     derived.setConnectionFactory(getConnectionFactory());
/*  92:184 */     derived.setConnectionSpec(connectionSpec);
/*  93:185 */     derived.setOutputRecordCreator(getOutputRecordCreator());
/*  94:186 */     return derived;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public <T> T execute(ConnectionCallback<T> action)
/*  98:    */     throws DataAccessException
/*  99:    */   {
/* 100:191 */     Assert.notNull(action, "Callback object must not be null");
/* 101:192 */     Connection con = ConnectionFactoryUtils.getConnection(getConnectionFactory(), getConnectionSpec());
/* 102:    */     try
/* 103:    */     {
/* 104:194 */       return action.doInConnection(con, getConnectionFactory());
/* 105:    */     }
/* 106:    */     catch (NotSupportedException ex)
/* 107:    */     {
/* 108:197 */       throw new CciOperationNotSupportedException("CCI operation not supported by connector", ex);
/* 109:    */     }
/* 110:    */     catch (ResourceException ex)
/* 111:    */     {
/* 112:200 */       throw new DataAccessResourceFailureException("CCI operation failed", ex);
/* 113:    */     }
/* 114:    */     catch (SQLException ex)
/* 115:    */     {
/* 116:203 */       throw new InvalidResultSetAccessException("Parsing of CCI ResultSet failed", ex);
/* 117:    */     }
/* 118:    */     finally
/* 119:    */     {
/* 120:206 */       ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory());
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public <T> T execute(final InteractionCallback<T> action)
/* 125:    */     throws DataAccessException
/* 126:    */   {
/* 127:211 */     Assert.notNull(action, "Callback object must not be null");
/* 128:212 */     execute(new ConnectionCallback()
/* 129:    */     {
/* 130:    */       public T doInConnection(Connection connection, ConnectionFactory connectionFactory)
/* 131:    */         throws ResourceException, SQLException, DataAccessException
/* 132:    */       {
/* 133:215 */         Interaction interaction = connection.createInteraction();
/* 134:    */         try
/* 135:    */         {
/* 136:217 */           return action.doInInteraction(interaction, connectionFactory);
/* 137:    */         }
/* 138:    */         finally
/* 139:    */         {
/* 140:220 */           CciTemplate.this.closeInteraction(interaction);
/* 141:    */         }
/* 142:    */       }
/* 143:    */     });
/* 144:    */   }
/* 145:    */   
/* 146:    */   public Record execute(InteractionSpec spec, Record inputRecord)
/* 147:    */     throws DataAccessException
/* 148:    */   {
/* 149:227 */     return (Record)doExecute(spec, inputRecord, null, new SimpleRecordExtractor(null));
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void execute(InteractionSpec spec, Record inputRecord, Record outputRecord)
/* 153:    */     throws DataAccessException
/* 154:    */   {
/* 155:231 */     doExecute(spec, inputRecord, outputRecord, null);
/* 156:    */   }
/* 157:    */   
/* 158:    */   public Record execute(InteractionSpec spec, RecordCreator inputCreator)
/* 159:    */     throws DataAccessException
/* 160:    */   {
/* 161:235 */     return (Record)doExecute(spec, createRecord(inputCreator), null, new SimpleRecordExtractor(null));
/* 162:    */   }
/* 163:    */   
/* 164:    */   public <T> T execute(InteractionSpec spec, Record inputRecord, RecordExtractor<T> outputExtractor)
/* 165:    */     throws DataAccessException
/* 166:    */   {
/* 167:241 */     return doExecute(spec, inputRecord, null, outputExtractor);
/* 168:    */   }
/* 169:    */   
/* 170:    */   public <T> T execute(InteractionSpec spec, RecordCreator inputCreator, RecordExtractor<T> outputExtractor)
/* 171:    */     throws DataAccessException
/* 172:    */   {
/* 173:247 */     return doExecute(spec, createRecord(inputCreator), null, outputExtractor);
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected <T> T doExecute(final InteractionSpec spec, final Record inputRecord, final Record outputRecord, final RecordExtractor<T> outputExtractor)
/* 177:    */     throws DataAccessException
/* 178:    */   {
/* 179:265 */     execute(new InteractionCallback()
/* 180:    */     {
/* 181:    */       public T doInInteraction(Interaction interaction, ConnectionFactory connectionFactory)
/* 182:    */         throws ResourceException, SQLException, DataAccessException
/* 183:    */       {
/* 184:268 */         Record outputRecordToUse = outputRecord;
/* 185:    */         try
/* 186:    */         {
/* 187:270 */           if ((outputRecord != null) || (CciTemplate.this.getOutputRecordCreator() != null))
/* 188:    */           {
/* 189:272 */             if (outputRecord == null)
/* 190:    */             {
/* 191:273 */               RecordFactory recordFactory = CciTemplate.this.getRecordFactory(connectionFactory);
/* 192:274 */               outputRecordToUse = CciTemplate.this.getOutputRecordCreator().createRecord(recordFactory);
/* 193:    */             }
/* 194:276 */             interaction.execute(spec, inputRecord, outputRecordToUse);
/* 195:    */           }
/* 196:    */           else
/* 197:    */           {
/* 198:279 */             outputRecordToUse = interaction.execute(spec, inputRecord);
/* 199:    */           }
/* 200:281 */           return outputExtractor != null ? outputExtractor.extractData(outputRecordToUse) : null;
/* 201:    */         }
/* 202:    */         finally
/* 203:    */         {
/* 204:284 */           if ((outputRecordToUse instanceof ResultSet)) {
/* 205:285 */             CciTemplate.this.closeResultSet((ResultSet)outputRecordToUse);
/* 206:    */           }
/* 207:    */         }
/* 208:    */       }
/* 209:    */     });
/* 210:    */   }
/* 211:    */   
/* 212:    */   public IndexedRecord createIndexedRecord(String name)
/* 213:    */     throws DataAccessException
/* 214:    */   {
/* 215:    */     try
/* 216:    */     {
/* 217:303 */       RecordFactory recordFactory = getRecordFactory(getConnectionFactory());
/* 218:304 */       return recordFactory.createIndexedRecord(name);
/* 219:    */     }
/* 220:    */     catch (NotSupportedException ex)
/* 221:    */     {
/* 222:307 */       throw new RecordTypeNotSupportedException("Creation of indexed Record not supported by connector", ex);
/* 223:    */     }
/* 224:    */     catch (ResourceException ex)
/* 225:    */     {
/* 226:310 */       throw new CannotCreateRecordException("Creation of indexed Record failed", ex);
/* 227:    */     }
/* 228:    */   }
/* 229:    */   
/* 230:    */   public MappedRecord createMappedRecord(String name)
/* 231:    */     throws DataAccessException
/* 232:    */   {
/* 233:    */     try
/* 234:    */     {
/* 235:324 */       RecordFactory recordFactory = getRecordFactory(getConnectionFactory());
/* 236:325 */       return recordFactory.createMappedRecord(name);
/* 237:    */     }
/* 238:    */     catch (NotSupportedException ex)
/* 239:    */     {
/* 240:328 */       throw new RecordTypeNotSupportedException("Creation of mapped Record not supported by connector", ex);
/* 241:    */     }
/* 242:    */     catch (ResourceException ex)
/* 243:    */     {
/* 244:331 */       throw new CannotCreateRecordException("Creation of mapped Record failed", ex);
/* 245:    */     }
/* 246:    */   }
/* 247:    */   
/* 248:    */   protected Record createRecord(RecordCreator recordCreator)
/* 249:    */     throws DataAccessException
/* 250:    */   {
/* 251:    */     try
/* 252:    */     {
/* 253:346 */       RecordFactory recordFactory = getRecordFactory(getConnectionFactory());
/* 254:347 */       return recordCreator.createRecord(recordFactory);
/* 255:    */     }
/* 256:    */     catch (NotSupportedException ex)
/* 257:    */     {
/* 258:350 */       throw new RecordTypeNotSupportedException(
/* 259:351 */         "Creation of the desired Record type not supported by connector", ex);
/* 260:    */     }
/* 261:    */     catch (ResourceException ex)
/* 262:    */     {
/* 263:354 */       throw new CannotCreateRecordException("Creation of the desired Record failed", ex);
/* 264:    */     }
/* 265:    */   }
/* 266:    */   
/* 267:    */   protected RecordFactory getRecordFactory(ConnectionFactory connectionFactory)
/* 268:    */     throws ResourceException
/* 269:    */   {
/* 270:    */     try
/* 271:    */     {
/* 272:371 */       return connectionFactory.getRecordFactory();
/* 273:    */     }
/* 274:    */     catch (NotSupportedException localNotSupportedException) {}
/* 275:374 */     return new NotSupportedRecordFactory();
/* 276:    */   }
/* 277:    */   
/* 278:    */   private void closeInteraction(Interaction interaction)
/* 279:    */   {
/* 280:386 */     if (interaction != null) {
/* 281:    */       try
/* 282:    */       {
/* 283:388 */         interaction.close();
/* 284:    */       }
/* 285:    */       catch (ResourceException ex)
/* 286:    */       {
/* 287:391 */         this.logger.trace("Could not close CCI Interaction", ex);
/* 288:    */       }
/* 289:    */       catch (Throwable ex)
/* 290:    */       {
/* 291:395 */         this.logger.trace("Unexpected exception on closing CCI Interaction", ex);
/* 292:    */       }
/* 293:    */     }
/* 294:    */   }
/* 295:    */   
/* 296:    */   private void closeResultSet(ResultSet resultSet)
/* 297:    */   {
/* 298:407 */     if (resultSet != null) {
/* 299:    */       try
/* 300:    */       {
/* 301:409 */         resultSet.close();
/* 302:    */       }
/* 303:    */       catch (SQLException ex)
/* 304:    */       {
/* 305:412 */         this.logger.trace("Could not close CCI ResultSet", ex);
/* 306:    */       }
/* 307:    */       catch (Throwable ex)
/* 308:    */       {
/* 309:416 */         this.logger.trace("Unexpected exception on closing CCI ResultSet", ex);
/* 310:    */       }
/* 311:    */     }
/* 312:    */   }
/* 313:    */   
/* 314:    */   private static class SimpleRecordExtractor
/* 315:    */     implements RecordExtractor<Record>
/* 316:    */   {
/* 317:    */     public Record extractData(Record record)
/* 318:    */     {
/* 319:425 */       return record;
/* 320:    */     }
/* 321:    */   }
/* 322:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.core.CciTemplate
 * JD-Core Version:    0.7.0.1
 */