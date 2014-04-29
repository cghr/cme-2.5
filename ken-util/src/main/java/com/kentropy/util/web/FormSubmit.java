/*   1:    */ package com.kentropy.util.web;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbUtil;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.FileNotFoundException;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.PrintWriter;
/*   8:    */ import java.util.Enumeration;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import javax.servlet.ServletContext;
/*  13:    */ import javax.servlet.ServletException;
/*  14:    */ import javax.servlet.http.HttpServlet;
/*  15:    */ import javax.servlet.http.HttpServletRequest;
/*  16:    */ import javax.servlet.http.HttpServletResponse;
/*  17:    */ import javax.servlet.http.HttpSession;
/*  18:    */ import org.apache.commons.fileupload.FileItem;
/*  19:    */ import org.apache.commons.fileupload.FileUpload;
/*  20:    */ import org.apache.commons.fileupload.disk.DiskFileItemFactory;
/*  21:    */ import org.apache.commons.fileupload.servlet.ServletFileUpload;
/*  22:    */ import org.apache.commons.lang3.StringEscapeUtils;
/*  23:    */ import org.jsoup.Jsoup;
/*  24:    */ import org.jsoup.safety.Whitelist;
/*  25:    */ import org.springframework.web.servlet.ModelAndView;
/*  26:    */ import org.springframework.web.servlet.mvc.Controller;
/*  27:    */ 
/*  28:    */ public class FormSubmit
/*  29:    */   extends HttpServlet
/*  30:    */   implements Controller
/*  31:    */ {
/*  32:    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*  33:    */     throws ServletException, IOException
/*  34:    */   {
/*  35: 42 */     String jsonId = "";
/*  36: 43 */     boolean isMultipart = FileUpload.isMultipartContent(request);
/*  37: 44 */     String successMsg = "Submitted Successfully !";
/*  38: 45 */     boolean allowHtml = false;
/*  39:    */     
/*  40:    */ 
/*  41:    */ 
/*  42:    */ 
/*  43:    */ 
/*  44:    */ 
/*  45:    */ 
/*  46:    */ 
/*  47: 54 */     DiskFileItemFactory factory = new DiskFileItemFactory();
/*  48:    */     
/*  49: 56 */     ServletFileUpload dskfu = new ServletFileUpload(factory);
/*  50: 57 */     List items2 = null;
/*  51: 58 */     if (isMultipart) {
/*  52:    */       try
/*  53:    */       {
/*  54: 62 */         List items = dskfu.parseRequest(request);
/*  55: 63 */         items2 = items;
/*  56: 66 */         for (int i = 0; i < items.size(); i++)
/*  57:    */         {
/*  58: 68 */           FileItem item = (FileItem)items.get(i);
/*  59: 69 */           if (item.isFormField()) {
/*  60: 71 */             if (item.getFieldName().equals("jsonId")) {
/*  61: 72 */               jsonId = item.getString();
/*  62:    */             }
/*  63:    */           }
/*  64:    */         }
/*  65:    */       }
/*  66:    */       catch (Exception localException) {}
/*  67:    */     } else {
/*  68: 87 */       jsonId = request.getParameter("jsonId");
/*  69:    */     }
/*  70: 90 */     DbUtil dbutil = new DbUtil();
/*  71:    */     
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78: 98 */     Map<String, String> map = (Map)dbutil.getDataAsListofMaps("json_datadict", "*", "id='" + jsonId + "'").get(0);
/*  79:    */     
/*  80:100 */     String table = (String)map.get("jsontable");
/*  81:101 */     String keyField = (String)map.get("keyfield");
/*  82:102 */     int htmlFlag = Integer.parseInt((String)map.get("html"));
/*  83:104 */     if (htmlFlag == 1) {
/*  84:105 */       allowHtml = true;
/*  85:    */     }
/*  86:109 */     Map<String, String> row = new HashMap();
/*  87:    */     
/*  88:111 */     String keyFieldValue = "";
/*  89:113 */     if (!isMultipart)
/*  90:    */     {
/*  91:115 */       Enumeration keys = request.getParameterNames();
/*  92:117 */       while (keys.hasMoreElements())
/*  93:    */       {
/*  94:119 */         String key = (String)keys.nextElement();
/*  95:125 */         if ((!key.equals("submit")) && (!key.equals("jsonId")) && (!key.equals("successMsg")))
/*  96:    */         {
/*  97:127 */           String[] valuesForKey = request.getParameterValues(key);
/*  98:    */           String value;
/*  99:    */           String value;
/* 100:128 */           if (valuesForKey.length > 1) {
/* 101:129 */             value = getFormattedValues(valuesForKey).toString();
/* 102:    */           } else {
/* 103:132 */             value = cleanInput(request.getParameter(key));
/* 104:    */           }
/* 105:136 */           if (key.equals(keyField)) {
/* 106:137 */             keyFieldValue = value;
/* 107:    */           }
/* 108:142 */           if (value.equals("null")) {
/* 109:143 */             value = null;
/* 110:    */           }
/* 111:144 */           row.put(key, value);
/* 112:    */         }
/* 113:    */       }
/* 114:149 */       if (request.getParameter("successMsg") != null) {
/* 115:150 */         successMsg = request.getParameter("successMsg");
/* 116:    */       }
/* 117:    */     }
/* 118:    */     else
/* 119:    */     {
/* 120:    */       try
/* 121:    */       {
/* 122:167 */         for (int i = 0; i < items2.size(); i++)
/* 123:    */         {
/* 124:171 */           FileItem item = (FileItem)items2.get(i);
/* 125:172 */           if (item.isFormField())
/* 126:    */           {
/* 127:174 */             if ((!item.getFieldName().equals("submit")) && (!item.getFieldName().equals("jsonId")) && (!item.equals("successMsg")))
/* 128:    */             {
/* 129:177 */               String key = item.getFieldName();
/* 130:    */               
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:184 */               String value = cleanInput(item.getString());
/* 137:187 */               if (item.getFieldName().equals(keyField)) {
/* 138:188 */                 keyFieldValue = value;
/* 139:    */               }
/* 140:192 */               if (value.equals("null")) {
/* 141:193 */                 value = null;
/* 142:    */               }
/* 143:195 */               row.put(key, value);
/* 144:    */             }
/* 145:    */           }
/* 146:    */           else
/* 147:    */           {
/* 148:204 */             String path = request.getSession().getServletContext().getRealPath("/") + (String)map.get("path");
/* 149:    */             try
/* 150:    */             {
/* 151:207 */               File file = new File(path, item.getName());
/* 152:208 */               item.write(file);
/* 153:    */               
/* 154:    */ 
/* 155:211 */               row.put(item.getFieldName(), cleanInput(item.getName()));
/* 156:    */             }
/* 157:    */             catch (FileNotFoundException localFileNotFoundException) {}
/* 158:    */           }
/* 159:    */         }
/* 160:    */       }
/* 161:    */       catch (Exception localException1) {}
/* 162:    */     }
/* 163:237 */     dbutil.saveDataFromMap(table, keyField + "='" + keyFieldValue + "'", row);
/* 164:    */     
/* 165:    */ 
/* 166:240 */     response.setContentType("text/html");
/* 167:    */     
/* 168:242 */     response.getWriter().println("<div style='margin-bottom:10px;width: auto;padding: 4px;border: solid 1px #DEDEDE;background: #FFFFCC;text-align:center;font-weight:bold;color:  #191970;position:fixed;left:40%;top:0px;'>" + successMsg + "</div>");
/* 169:    */   }
/* 170:    */   
/* 171:    */   public String cleanInput(String input)
/* 172:    */   {
/* 173:261 */     return Jsoup.clean(StringEscapeUtils.unescapeHtml4(input), Whitelist.basic());
/* 174:    */   }
/* 175:    */   
/* 176:    */   public StringBuffer getFormattedValues(String[] valuesForKey)
/* 177:    */   {
/* 178:280 */     StringBuffer result = new StringBuffer();
/* 179:281 */     for (int i = 0; i < valuesForKey.length; i++) {
/* 180:282 */       result.append(cleanInput(valuesForKey[i]) + ",");
/* 181:    */     }
/* 182:285 */     result.deleteCharAt(result.length() - 1);
/* 183:    */     
/* 184:    */ 
/* 185:288 */     return result;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 189:    */     throws Exception
/* 190:    */   {
/* 191:297 */     service(request, response);
/* 192:298 */     return null;
/* 193:    */   }
/* 194:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.web.FormSubmit
 * JD-Core Version:    0.7.0.1
 */