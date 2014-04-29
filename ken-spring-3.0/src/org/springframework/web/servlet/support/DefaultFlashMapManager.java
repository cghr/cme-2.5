/*   1:    */ package org.springframework.web.servlet.support;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Set;
/*   8:    */ import java.util.concurrent.CopyOnWriteArrayList;
/*   9:    */ import javax.servlet.http.HttpServletRequest;
/*  10:    */ import javax.servlet.http.HttpSession;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.util.CollectionUtils;
/*  14:    */ import org.springframework.util.MultiValueMap;
/*  15:    */ import org.springframework.util.StringUtils;
/*  16:    */ import org.springframework.web.servlet.FlashMap;
/*  17:    */ import org.springframework.web.servlet.FlashMapManager;
/*  18:    */ import org.springframework.web.util.UrlPathHelper;
/*  19:    */ 
/*  20:    */ public class DefaultFlashMapManager
/*  21:    */   implements FlashMapManager
/*  22:    */ {
/*  23: 45 */   private static final String FLASH_MAPS_SESSION_ATTRIBUTE = DefaultFlashMapManager.class + ".FLASH_MAPS";
/*  24: 47 */   private static final Log logger = LogFactory.getLog(DefaultFlashMapManager.class);
/*  25: 49 */   private int flashTimeout = 180;
/*  26: 51 */   private final UrlPathHelper urlPathHelper = new UrlPathHelper();
/*  27:    */   
/*  28:    */   public void setFlashMapTimeout(int flashTimeout)
/*  29:    */   {
/*  30: 59 */     this.flashTimeout = flashTimeout;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void requestStarted(HttpServletRequest request)
/*  34:    */   {
/*  35: 67 */     if (request.getAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE) != null) {
/*  36: 68 */       return;
/*  37:    */     }
/*  38: 71 */     FlashMap inputFlashMap = lookupFlashMap(request);
/*  39: 72 */     if (inputFlashMap != null) {
/*  40: 73 */       request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
/*  41:    */     }
/*  42: 76 */     FlashMap outputFlashMap = new FlashMap(hashCode());
/*  43: 77 */     request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, outputFlashMap);
/*  44:    */     
/*  45: 79 */     removeExpiredFlashMaps(request);
/*  46:    */   }
/*  47:    */   
/*  48:    */   private FlashMap lookupFlashMap(HttpServletRequest request)
/*  49:    */   {
/*  50: 88 */     List<FlashMap> allFlashMaps = retrieveFlashMaps(request, false);
/*  51: 89 */     if (CollectionUtils.isEmpty(allFlashMaps)) {
/*  52: 90 */       return null;
/*  53:    */     }
/*  54: 92 */     if (logger.isDebugEnabled()) {
/*  55: 93 */       logger.debug("Retrieved FlashMap(s): " + allFlashMaps);
/*  56:    */     }
/*  57: 95 */     List<FlashMap> result = new ArrayList();
/*  58: 96 */     for (FlashMap flashMap : allFlashMaps) {
/*  59: 97 */       if (isFlashMapForRequest(flashMap, request)) {
/*  60: 98 */         result.add(flashMap);
/*  61:    */       }
/*  62:    */     }
/*  63:101 */     if (!result.isEmpty())
/*  64:    */     {
/*  65:102 */       Collections.sort(result);
/*  66:103 */       if (logger.isDebugEnabled()) {
/*  67:104 */         logger.debug("Found matching FlashMap(s): " + result);
/*  68:    */       }
/*  69:106 */       FlashMap match = (FlashMap)result.remove(0);
/*  70:107 */       allFlashMaps.remove(match);
/*  71:108 */       return match;
/*  72:    */     }
/*  73:110 */     return null;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected boolean isFlashMapForRequest(FlashMap flashMap, HttpServletRequest request)
/*  77:    */   {
/*  78:119 */     if (flashMap.getTargetRequestPath() != null)
/*  79:    */     {
/*  80:120 */       String requestUri = this.urlPathHelper.getRequestUri(request);
/*  81:121 */       if ((!requestUri.equals(flashMap.getTargetRequestPath())) && 
/*  82:122 */         (!requestUri.equals(flashMap.getTargetRequestPath() + "/"))) {
/*  83:123 */         return false;
/*  84:    */       }
/*  85:    */     }
/*  86:126 */     MultiValueMap<String, String> params = flashMap.getTargetRequestParams();
/*  87:    */     Iterator localIterator2;
/*  88:127 */     for (Iterator localIterator1 = params.keySet().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/*  89:    */     {
/*  90:127 */       String key = (String)localIterator1.next();
/*  91:128 */       localIterator2 = ((List)params.get(key)).iterator(); continue;String value = (String)localIterator2.next();
/*  92:129 */       if (!value.equals(request.getParameter(key))) {
/*  93:130 */         return false;
/*  94:    */       }
/*  95:    */     }
/*  96:134 */     return true;
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request, boolean allowCreate)
/* 100:    */   {
/* 101:148 */     HttpSession session = request.getSession(allowCreate);
/* 102:149 */     if (session == null) {
/* 103:150 */       return null;
/* 104:    */     }
/* 105:152 */     List<FlashMap> allFlashMaps = (List)session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE);
/* 106:153 */     if ((allFlashMaps == null) && (allowCreate)) {
/* 107:154 */       synchronized (this)
/* 108:    */       {
/* 109:155 */         allFlashMaps = (List)session.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE);
/* 110:156 */         if (allFlashMaps == null)
/* 111:    */         {
/* 112:157 */           allFlashMaps = new CopyOnWriteArrayList();
/* 113:158 */           session.setAttribute(FLASH_MAPS_SESSION_ATTRIBUTE, allFlashMaps);
/* 114:    */         }
/* 115:    */       }
/* 116:    */     }
/* 117:162 */     return allFlashMaps;
/* 118:    */   }
/* 119:    */   
/* 120:    */   private void removeExpiredFlashMaps(HttpServletRequest request)
/* 121:    */   {
/* 122:169 */     List<FlashMap> allMaps = retrieveFlashMaps(request, false);
/* 123:170 */     if (CollectionUtils.isEmpty(allMaps)) {
/* 124:171 */       return;
/* 125:    */     }
/* 126:173 */     List<FlashMap> expiredMaps = new ArrayList();
/* 127:174 */     for (FlashMap flashMap : allMaps) {
/* 128:175 */       if (flashMap.isExpired())
/* 129:    */       {
/* 130:176 */         if (logger.isDebugEnabled()) {
/* 131:177 */           logger.debug("Removing expired FlashMap: " + flashMap);
/* 132:    */         }
/* 133:179 */         expiredMaps.add(flashMap);
/* 134:    */       }
/* 135:    */     }
/* 136:182 */     if (!expiredMaps.isEmpty()) {
/* 137:183 */       allMaps.removeAll(expiredMaps);
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void requestCompleted(HttpServletRequest request)
/* 142:    */   {
/* 143:192 */     FlashMap flashMap = (FlashMap)request.getAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE);
/* 144:193 */     if (flashMap == null) {
/* 145:194 */       throw new IllegalStateException("requestCompleted called but \"output\" FlashMap was never created");
/* 146:    */     }
/* 147:196 */     if ((!flashMap.isEmpty()) && (flashMap.isCreatedBy(hashCode())))
/* 148:    */     {
/* 149:197 */       if (logger.isDebugEnabled()) {
/* 150:198 */         logger.debug("Saving FlashMap=" + flashMap);
/* 151:    */       }
/* 152:200 */       onSaveFlashMap(flashMap, request);
/* 153:201 */       retrieveFlashMaps(request, true).add(flashMap);
/* 154:    */     }
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected void onSaveFlashMap(FlashMap flashMap, HttpServletRequest request)
/* 158:    */   {
/* 159:213 */     String targetPath = flashMap.getTargetRequestPath();
/* 160:214 */     flashMap.setTargetRequestPath(decodeAndNormalizePath(targetPath, request));
/* 161:215 */     flashMap.startExpirationPeriod(this.flashTimeout);
/* 162:    */   }
/* 163:    */   
/* 164:    */   private String decodeAndNormalizePath(String path, HttpServletRequest request)
/* 165:    */   {
/* 166:219 */     if (path != null)
/* 167:    */     {
/* 168:220 */       path = this.urlPathHelper.decodeRequestString(request, path);
/* 169:221 */       if (path.charAt(0) != '/')
/* 170:    */       {
/* 171:222 */         String requestUri = this.urlPathHelper.getRequestUri(request);
/* 172:223 */         path = requestUri.substring(0, requestUri.lastIndexOf('/') + 1) + path;
/* 173:224 */         path = StringUtils.cleanPath(path);
/* 174:    */       }
/* 175:    */     }
/* 176:227 */     return path;
/* 177:    */   }
/* 178:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.support.DefaultFlashMapManager
 * JD-Core Version:    0.7.0.1
 */