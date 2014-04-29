/*  1:   */ package org.springframework.web.bind;
/*  2:   */ 
/*  3:   */ import java.util.Iterator;
/*  4:   */ import java.util.Map;
/*  5:   */ import java.util.Map.Entry;
/*  6:   */ import java.util.Set;
/*  7:   */ import org.springframework.util.ObjectUtils;
/*  8:   */ import org.springframework.util.StringUtils;
/*  9:   */ 
/* 10:   */ public class UnsatisfiedServletRequestParameterException
/* 11:   */   extends ServletRequestBindingException
/* 12:   */ {
/* 13:   */   private final String[] paramConditions;
/* 14:   */   private final Map<String, String[]> actualParams;
/* 15:   */   
/* 16:   */   public UnsatisfiedServletRequestParameterException(String[] paramConditions, Map actualParams)
/* 17:   */   {
/* 18:48 */     super("");
/* 19:49 */     this.paramConditions = paramConditions;
/* 20:50 */     this.actualParams = actualParams;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getMessage()
/* 24:   */   {
/* 25:56 */     return 
/* 26:57 */       "Parameter conditions \"" + StringUtils.arrayToDelimitedString(this.paramConditions, ", ") + "\" not met for actual request parameters: " + requestParameterMapToString(this.actualParams);
/* 27:   */   }
/* 28:   */   
/* 29:   */   private static String requestParameterMapToString(Map<String, String[]> actualParams)
/* 30:   */   {
/* 31:61 */     StringBuilder result = new StringBuilder();
/* 32:62 */     for (Iterator<Map.Entry<String, String[]>> it = actualParams.entrySet().iterator(); it.hasNext();)
/* 33:   */     {
/* 34:63 */       Map.Entry<String, String[]> entry = (Map.Entry)it.next();
/* 35:64 */       result.append((String)entry.getKey()).append('=').append(ObjectUtils.nullSafeToString((Object[])entry.getValue()));
/* 36:65 */       if (it.hasNext()) {
/* 37:66 */         result.append(", ");
/* 38:   */       }
/* 39:   */     }
/* 40:69 */     return result.toString();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public final String[] getParamConditions()
/* 44:   */   {
/* 45:77 */     return this.paramConditions;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public final Map<String, String[]> getActualParams()
/* 49:   */   {
/* 50:85 */     return this.actualParams;
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.UnsatisfiedServletRequestParameterException
 * JD-Core Version:    0.7.0.1
 */