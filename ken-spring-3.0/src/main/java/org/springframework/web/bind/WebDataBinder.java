/*   1:    */ package org.springframework.web.bind;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*   8:    */ import org.springframework.beans.MutablePropertyValues;
/*   9:    */ import org.springframework.beans.PropertyValue;
/*  10:    */ import org.springframework.validation.DataBinder;
/*  11:    */ import org.springframework.web.multipart.MultipartFile;
/*  12:    */ 
/*  13:    */ public class WebDataBinder
/*  14:    */   extends DataBinder
/*  15:    */ {
/*  16:    */   public static final String DEFAULT_FIELD_MARKER_PREFIX = "_";
/*  17:    */   public static final String DEFAULT_FIELD_DEFAULT_PREFIX = "!";
/*  18: 74 */   private String fieldMarkerPrefix = "_";
/*  19: 76 */   private String fieldDefaultPrefix = "!";
/*  20: 78 */   private boolean bindEmptyMultipartFiles = true;
/*  21:    */   
/*  22:    */   public WebDataBinder(Object target)
/*  23:    */   {
/*  24: 88 */     super(target);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public WebDataBinder(Object target, String objectName)
/*  28:    */   {
/*  29: 98 */     super(target, objectName);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setFieldMarkerPrefix(String fieldMarkerPrefix)
/*  33:    */   {
/*  34:125 */     this.fieldMarkerPrefix = fieldMarkerPrefix;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String getFieldMarkerPrefix()
/*  38:    */   {
/*  39:132 */     return this.fieldMarkerPrefix;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setFieldDefaultPrefix(String fieldDefaultPrefix)
/*  43:    */   {
/*  44:151 */     this.fieldDefaultPrefix = fieldDefaultPrefix;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getFieldDefaultPrefix()
/*  48:    */   {
/*  49:158 */     return this.fieldDefaultPrefix;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setBindEmptyMultipartFiles(boolean bindEmptyMultipartFiles)
/*  53:    */   {
/*  54:170 */     this.bindEmptyMultipartFiles = bindEmptyMultipartFiles;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isBindEmptyMultipartFiles()
/*  58:    */   {
/*  59:177 */     return this.bindEmptyMultipartFiles;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected void doBind(MutablePropertyValues mpvs)
/*  63:    */   {
/*  64:189 */     checkFieldDefaults(mpvs);
/*  65:190 */     checkFieldMarkers(mpvs);
/*  66:191 */     super.doBind(mpvs);
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected void checkFieldDefaults(MutablePropertyValues mpvs)
/*  70:    */   {
/*  71:203 */     if (getFieldDefaultPrefix() != null)
/*  72:    */     {
/*  73:204 */       String fieldDefaultPrefix = getFieldDefaultPrefix();
/*  74:205 */       PropertyValue[] pvArray = mpvs.getPropertyValues();
/*  75:206 */       for (PropertyValue pv : pvArray) {
/*  76:207 */         if (pv.getName().startsWith(fieldDefaultPrefix))
/*  77:    */         {
/*  78:208 */           String field = pv.getName().substring(fieldDefaultPrefix.length());
/*  79:209 */           if ((getPropertyAccessor().isWritableProperty(field)) && (!mpvs.contains(field))) {
/*  80:210 */             mpvs.add(field, pv.getValue());
/*  81:    */           }
/*  82:212 */           mpvs.removePropertyValue(pv);
/*  83:    */         }
/*  84:    */       }
/*  85:    */     }
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected void checkFieldMarkers(MutablePropertyValues mpvs)
/*  89:    */   {
/*  90:230 */     if (getFieldMarkerPrefix() != null)
/*  91:    */     {
/*  92:231 */       String fieldMarkerPrefix = getFieldMarkerPrefix();
/*  93:232 */       PropertyValue[] pvArray = mpvs.getPropertyValues();
/*  94:233 */       for (PropertyValue pv : pvArray) {
/*  95:234 */         if (pv.getName().startsWith(fieldMarkerPrefix))
/*  96:    */         {
/*  97:235 */           String field = pv.getName().substring(fieldMarkerPrefix.length());
/*  98:236 */           if ((getPropertyAccessor().isWritableProperty(field)) && (!mpvs.contains(field)))
/*  99:    */           {
/* 100:237 */             Class fieldType = getPropertyAccessor().getPropertyType(field);
/* 101:238 */             mpvs.add(field, getEmptyValue(field, fieldType));
/* 102:    */           }
/* 103:240 */           mpvs.removePropertyValue(pv);
/* 104:    */         }
/* 105:    */       }
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected Object getEmptyValue(String field, Class fieldType)
/* 110:    */   {
/* 111:256 */     if (((fieldType != null) && (Boolean.TYPE.equals(fieldType))) || (Boolean.class.equals(fieldType))) {
/* 112:258 */       return Boolean.FALSE;
/* 113:    */     }
/* 114:260 */     if ((fieldType != null) && (fieldType.isArray())) {
/* 115:262 */       return Array.newInstance(fieldType.getComponentType(), 0);
/* 116:    */     }
/* 117:266 */     return null;
/* 118:    */   }
/* 119:    */   
/* 120:    */   @Deprecated
/* 121:    */   protected void bindMultipartFiles(Map<String, MultipartFile> multipartFiles, MutablePropertyValues mpvs)
/* 122:    */   {
/* 123:285 */     for (Map.Entry<String, MultipartFile> entry : multipartFiles.entrySet())
/* 124:    */     {
/* 125:286 */       String key = (String)entry.getKey();
/* 126:287 */       MultipartFile value = (MultipartFile)entry.getValue();
/* 127:288 */       if ((isBindEmptyMultipartFiles()) || (!value.isEmpty())) {
/* 128:289 */         mpvs.add(key, value);
/* 129:    */       }
/* 130:    */     }
/* 131:    */   }
/* 132:    */   
/* 133:    */   protected void bindMultipart(Map<String, List<MultipartFile>> multipartFiles, MutablePropertyValues mpvs)
/* 134:    */   {
/* 135:305 */     for (Map.Entry<String, List<MultipartFile>> entry : multipartFiles.entrySet())
/* 136:    */     {
/* 137:306 */       String key = (String)entry.getKey();
/* 138:307 */       List<MultipartFile> values = (List)entry.getValue();
/* 139:308 */       if (values.size() == 1)
/* 140:    */       {
/* 141:309 */         MultipartFile value = (MultipartFile)values.get(0);
/* 142:310 */         if ((isBindEmptyMultipartFiles()) || (!value.isEmpty())) {
/* 143:311 */           mpvs.add(key, value);
/* 144:    */         }
/* 145:    */       }
/* 146:    */       else
/* 147:    */       {
/* 148:315 */         mpvs.add(key, values);
/* 149:    */       }
/* 150:    */     }
/* 151:    */   }
/* 152:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.WebDataBinder
 * JD-Core Version:    0.7.0.1
 */