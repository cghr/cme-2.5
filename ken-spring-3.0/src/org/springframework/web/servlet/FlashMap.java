/*   1:    */ package org.springframework.web.servlet;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Set;
/*   7:    */ import org.springframework.util.LinkedMultiValueMap;
/*   8:    */ import org.springframework.util.MultiValueMap;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ 
/*  11:    */ public final class FlashMap
/*  12:    */   extends HashMap<String, Object>
/*  13:    */   implements Comparable<FlashMap>
/*  14:    */ {
/*  15:    */   private static final long serialVersionUID = 1L;
/*  16:    */   private String targetRequestPath;
/*  17: 55 */   private final MultiValueMap<String, String> targetRequestParams = new LinkedMultiValueMap();
/*  18:    */   private long expirationStartTime;
/*  19:    */   private int timeToLive;
/*  20:    */   private final int createdBy;
/*  21:    */   
/*  22:    */   public FlashMap(int createdBy)
/*  23:    */   {
/*  24: 68 */     this.createdBy = createdBy;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public FlashMap()
/*  28:    */   {
/*  29: 75 */     this.createdBy = 0;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setTargetRequestPath(String path)
/*  33:    */   {
/*  34: 85 */     this.targetRequestPath = path;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String getTargetRequestPath()
/*  38:    */   {
/*  39: 92 */     return this.targetRequestPath;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public FlashMap addTargetRequestParams(MultiValueMap<String, String> params)
/*  43:    */   {
/*  44:101 */     if (params != null)
/*  45:    */     {
/*  46:    */       Iterator localIterator2;
/*  47:102 */       for (Iterator localIterator1 = params.keySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/*  48:    */       {
/*  49:102 */         String key = (String)localIterator1.next();
/*  50:103 */         localIterator2 = ((List)params.get(key)).iterator(); continue;String value = (String)localIterator2.next();
/*  51:104 */         addTargetRequestParam(key, value);
/*  52:    */       }
/*  53:    */     }
/*  54:108 */     return this;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public FlashMap addTargetRequestParam(String name, String value)
/*  58:    */   {
/*  59:117 */     if ((StringUtils.hasText(name)) && (StringUtils.hasText(value))) {
/*  60:118 */       this.targetRequestParams.add(name, value);
/*  61:    */     }
/*  62:120 */     return this;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public MultiValueMap<String, String> getTargetRequestParams()
/*  66:    */   {
/*  67:127 */     return this.targetRequestParams;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void startExpirationPeriod(int timeToLive)
/*  71:    */   {
/*  72:135 */     this.expirationStartTime = System.currentTimeMillis();
/*  73:136 */     this.timeToLive = timeToLive;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean isExpired()
/*  77:    */   {
/*  78:144 */     if (this.expirationStartTime != 0L) {
/*  79:145 */       return System.currentTimeMillis() - this.expirationStartTime > this.timeToLive * 1000;
/*  80:    */     }
/*  81:148 */     return false;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean isCreatedBy(int createdBy)
/*  85:    */   {
/*  86:156 */     return this.createdBy == createdBy;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int compareTo(FlashMap other)
/*  90:    */   {
/*  91:165 */     int thisUrlPath = this.targetRequestPath != null ? 1 : 0;
/*  92:166 */     int otherUrlPath = other.targetRequestPath != null ? 1 : 0;
/*  93:167 */     if (thisUrlPath != otherUrlPath) {
/*  94:168 */       return otherUrlPath - thisUrlPath;
/*  95:    */     }
/*  96:171 */     return other.targetRequestParams.size() - this.targetRequestParams.size();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public String toString()
/* 100:    */   {
/* 101:177 */     StringBuilder sb = new StringBuilder();
/* 102:178 */     sb.append("[Attributes=").append(super.toString());
/* 103:179 */     sb.append(", targetRequestPath=").append(this.targetRequestPath);
/* 104:180 */     sb.append(", targetRequestParams=").append(this.targetRequestParams).append("]");
/* 105:181 */     return sb.toString();
/* 106:    */   }
/* 107:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.FlashMap
 * JD-Core Version:    0.7.0.1
 */