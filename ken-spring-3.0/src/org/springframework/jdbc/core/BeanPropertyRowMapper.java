/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.sql.ResultSet;
/*   5:    */ import java.sql.ResultSetMetaData;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.HashSet;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.beans.BeanUtils;
/*  14:    */ import org.springframework.beans.BeanWrapper;
/*  15:    */ import org.springframework.beans.NotWritablePropertyException;
/*  16:    */ import org.springframework.beans.PropertyAccessorFactory;
/*  17:    */ import org.springframework.beans.TypeMismatchException;
/*  18:    */ import org.springframework.dao.DataRetrievalFailureException;
/*  19:    */ import org.springframework.dao.InvalidDataAccessApiUsageException;
/*  20:    */ import org.springframework.jdbc.support.JdbcUtils;
/*  21:    */ import org.springframework.util.Assert;
/*  22:    */ 
/*  23:    */ public class BeanPropertyRowMapper<T>
/*  24:    */   implements RowMapper<T>
/*  25:    */ {
/*  26: 74 */   protected final Log logger = LogFactory.getLog(getClass());
/*  27:    */   private Class<T> mappedClass;
/*  28: 80 */   private boolean checkFullyPopulated = false;
/*  29: 83 */   private boolean primitivesDefaultedForNullValue = false;
/*  30:    */   private Map<String, PropertyDescriptor> mappedFields;
/*  31:    */   private Set<String> mappedProperties;
/*  32:    */   
/*  33:    */   public BeanPropertyRowMapper() {}
/*  34:    */   
/*  35:    */   public BeanPropertyRowMapper(Class<T> mappedClass)
/*  36:    */   {
/*  37:108 */     initialize(mappedClass);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public BeanPropertyRowMapper(Class<T> mappedClass, boolean checkFullyPopulated)
/*  41:    */   {
/*  42:118 */     initialize(mappedClass);
/*  43:119 */     this.checkFullyPopulated = checkFullyPopulated;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setMappedClass(Class<T> mappedClass)
/*  47:    */   {
/*  48:127 */     if (this.mappedClass == null) {
/*  49:128 */       initialize(mappedClass);
/*  50:131 */     } else if (!this.mappedClass.equals(mappedClass)) {
/*  51:132 */       throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " + 
/*  52:133 */         mappedClass + " since it is already providing mapping for " + this.mappedClass);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void initialize(Class<T> mappedClass)
/*  57:    */   {
/*  58:143 */     this.mappedClass = mappedClass;
/*  59:144 */     this.mappedFields = new HashMap();
/*  60:145 */     this.mappedProperties = new HashSet();
/*  61:146 */     PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
/*  62:147 */     for (PropertyDescriptor pd : pds) {
/*  63:148 */       if (pd.getWriteMethod() != null)
/*  64:    */       {
/*  65:149 */         this.mappedFields.put(pd.getName().toLowerCase(), pd);
/*  66:150 */         String underscoredName = underscoreName(pd.getName());
/*  67:151 */         if (!pd.getName().toLowerCase().equals(underscoredName)) {
/*  68:152 */           this.mappedFields.put(underscoredName, pd);
/*  69:    */         }
/*  70:154 */         this.mappedProperties.add(pd.getName());
/*  71:    */       }
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   private String underscoreName(String name)
/*  76:    */   {
/*  77:166 */     StringBuilder result = new StringBuilder();
/*  78:167 */     if ((name != null) && (name.length() > 0))
/*  79:    */     {
/*  80:168 */       result.append(name.substring(0, 1).toLowerCase());
/*  81:169 */       for (int i = 1; i < name.length(); i++)
/*  82:    */       {
/*  83:170 */         String s = name.substring(i, i + 1);
/*  84:171 */         if (s.equals(s.toUpperCase()))
/*  85:    */         {
/*  86:172 */           result.append("_");
/*  87:173 */           result.append(s.toLowerCase());
/*  88:    */         }
/*  89:    */         else
/*  90:    */         {
/*  91:176 */           result.append(s);
/*  92:    */         }
/*  93:    */       }
/*  94:    */     }
/*  95:180 */     return result.toString();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public final Class<T> getMappedClass()
/*  99:    */   {
/* 100:187 */     return this.mappedClass;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setCheckFullyPopulated(boolean checkFullyPopulated)
/* 104:    */   {
/* 105:197 */     this.checkFullyPopulated = checkFullyPopulated;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean isCheckFullyPopulated()
/* 109:    */   {
/* 110:205 */     return this.checkFullyPopulated;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue)
/* 114:    */   {
/* 115:214 */     this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public boolean isPrimitivesDefaultedForNullValue()
/* 119:    */   {
/* 120:222 */     return this.primitivesDefaultedForNullValue;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public T mapRow(ResultSet rs, int rowNumber)
/* 124:    */     throws SQLException
/* 125:    */   {
/* 126:232 */     Assert.state(this.mappedClass != null, "Mapped class was not specified");
/* 127:233 */     T mappedObject = BeanUtils.instantiate(this.mappedClass);
/* 128:234 */     BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
/* 129:235 */     initBeanWrapper(bw);
/* 130:    */     
/* 131:237 */     ResultSetMetaData rsmd = rs.getMetaData();
/* 132:238 */     int columnCount = rsmd.getColumnCount();
/* 133:239 */     Set<String> populatedProperties = isCheckFullyPopulated() ? new HashSet() : null;
/* 134:241 */     for (int index = 1; index <= columnCount; index++)
/* 135:    */     {
/* 136:242 */       String column = JdbcUtils.lookupColumnName(rsmd, index);
/* 137:243 */       PropertyDescriptor pd = (PropertyDescriptor)this.mappedFields.get(column.replaceAll(" ", "").toLowerCase());
/* 138:244 */       if (pd != null) {
/* 139:    */         try
/* 140:    */         {
/* 141:246 */           Object value = getColumnValue(rs, index, pd);
/* 142:247 */           if ((this.logger.isDebugEnabled()) && (rowNumber == 0)) {
/* 143:248 */             this.logger.debug("Mapping column '" + column + "' to property '" + 
/* 144:249 */               pd.getName() + "' of type " + pd.getPropertyType());
/* 145:    */           }
/* 146:    */           try
/* 147:    */           {
/* 148:252 */             bw.setPropertyValue(pd.getName(), value);
/* 149:    */           }
/* 150:    */           catch (TypeMismatchException e)
/* 151:    */           {
/* 152:255 */             if ((value == null) && (this.primitivesDefaultedForNullValue)) {
/* 153:256 */               this.logger.debug("Intercepted TypeMismatchException for row " + rowNumber + 
/* 154:257 */                 " and column '" + column + "' with value " + value + 
/* 155:258 */                 " when setting property '" + pd.getName() + "' of type " + pd.getPropertyType() + 
/* 156:259 */                 " on object: " + mappedObject);
/* 157:    */             } else {
/* 158:262 */               throw e;
/* 159:    */             }
/* 160:    */           }
/* 161:265 */           if (populatedProperties != null) {
/* 162:266 */             populatedProperties.add(pd.getName());
/* 163:    */           }
/* 164:    */         }
/* 165:    */         catch (NotWritablePropertyException ex)
/* 166:    */         {
/* 167:270 */           throw new DataRetrievalFailureException(
/* 168:271 */             "Unable to map column " + column + " to property " + pd.getName(), ex);
/* 169:    */         }
/* 170:    */       }
/* 171:    */     }
/* 172:276 */     if ((populatedProperties != null) && (!populatedProperties.equals(this.mappedProperties))) {
/* 173:277 */       throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields necessary to populate object of class [" + 
/* 174:278 */         this.mappedClass + "]: " + this.mappedProperties);
/* 175:    */     }
/* 176:281 */     return mappedObject;
/* 177:    */   }
/* 178:    */   
/* 179:    */   protected void initBeanWrapper(BeanWrapper bw) {}
/* 180:    */   
/* 181:    */   protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd)
/* 182:    */     throws SQLException
/* 183:    */   {
/* 184:308 */     return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass)
/* 188:    */   {
/* 189:318 */     BeanPropertyRowMapper<T> newInstance = new BeanPropertyRowMapper();
/* 190:319 */     newInstance.setMappedClass(mappedClass);
/* 191:320 */     return newInstance;
/* 192:    */   }
/* 193:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.BeanPropertyRowMapper
 * JD-Core Version:    0.7.0.1
 */