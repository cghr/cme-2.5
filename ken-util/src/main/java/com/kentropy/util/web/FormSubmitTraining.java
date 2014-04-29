/*   1:    */ package com.kentropy.util.web;
/*   2:    */ 
/*   3:    */ import com.kentropy.cmetraining.CMETrainingHandler;
/*   4:    */ import com.kentropy.util.DbUtil;
/*   5:    */ import java.io.File;
/*   6:    */ import java.io.FileNotFoundException;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.io.PrintWriter;
/*  10:    */ import java.util.Enumeration;
/*  11:    */ import java.util.HashMap;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Map;
/*  14:    */ import javax.servlet.ServletContext;
/*  15:    */ import javax.servlet.ServletException;
/*  16:    */ import javax.servlet.http.HttpServlet;
/*  17:    */ import javax.servlet.http.HttpServletRequest;
/*  18:    */ import javax.servlet.http.HttpServletResponse;
/*  19:    */ import javax.servlet.http.HttpSession;
/*  20:    */ import org.apache.commons.fileupload.FileItem;
/*  21:    */ import org.apache.commons.fileupload.FileUpload;
/*  22:    */ import org.apache.commons.fileupload.disk.DiskFileItemFactory;
/*  23:    */ import org.apache.commons.fileupload.servlet.ServletFileUpload;
/*  24:    */ import org.springframework.web.servlet.ModelAndView;
/*  25:    */ import org.springframework.web.servlet.mvc.Controller;
/*  26:    */ 
/*  27:    */ public class FormSubmitTraining
/*  28:    */   extends HttpServlet
/*  29:    */   implements Controller
/*  30:    */ {
/*  31:    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*  32:    */     throws ServletException, IOException
/*  33:    */   {
/*  34: 39 */     String jsonId = "";
/*  35: 40 */     boolean isMultipart = FileUpload.isMultipartContent(request);
/*  36: 41 */     String successMsg = "Submitted Successfully !";
/*  37: 42 */     boolean isSurveyForm = false;
/*  38: 43 */     DbUtil db = new DbUtil();
/*  39: 46 */     if (request.getParameter("jsonId") != null) {
/*  40: 48 */       if (request.getParameter("jsonId").equals("survey")) {
/*  41: 49 */         isSurveyForm = true;
/*  42:    */       }
/*  43:    */     }
/*  44: 54 */     DiskFileItemFactory factory = new DiskFileItemFactory();
/*  45:    */     
/*  46: 56 */     ServletFileUpload dskfu = new ServletFileUpload(factory);
/*  47: 57 */     List items2 = null;
/*  48: 58 */     if (isMultipart) {
/*  49:    */       try
/*  50:    */       {
/*  51: 62 */         List items = dskfu.parseRequest(request);
/*  52: 63 */         items2 = items;
/*  53: 66 */         for (int i = 0; i < items.size(); i++)
/*  54:    */         {
/*  55: 68 */           FileItem item = (FileItem)items.get(i);
/*  56: 69 */           if (item.isFormField()) {
/*  57: 71 */             if (item.getFieldName().equals("jsonId")) {
/*  58: 72 */               jsonId = item.getString();
/*  59:    */             }
/*  60:    */           }
/*  61:    */         }
/*  62:    */       }
/*  63:    */       catch (Exception localException) {}
/*  64:    */     } else {
/*  65: 87 */       jsonId = request.getParameter("jsonId");
/*  66:    */     }
/*  67: 90 */     DbUtil dbutil = new DbUtil();
/*  68:    */     
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75: 98 */     Map<String, String> map = (Map)dbutil.getDataAsListofMaps("json_datadict", "*", "id='" + jsonId + "'").get(0);
/*  76:    */     
/*  77:100 */     String table = (String)map.get("jsontable");
/*  78:101 */     String keyField = (String)map.get("keyfield");
/*  79:    */     
/*  80:    */ 
/*  81:    */ 
/*  82:    */ 
/*  83:106 */     Map<String, String> row = new HashMap();
/*  84:    */     
/*  85:108 */     String keyFieldValue = "";
/*  86:110 */     if (!isMultipart)
/*  87:    */     {
/*  88:112 */       Enumeration keys = request.getParameterNames();
/*  89:114 */       while (keys.hasMoreElements())
/*  90:    */       {
/*  91:116 */         String key = (String)keys.nextElement();
/*  92:122 */         if ((!key.equals("submit")) && (!key.equals("jsonId")) && (!key.equals("successMsg")))
/*  93:    */         {
/*  94:124 */           String[] valuesForKey = request.getParameterValues(key);
/*  95:    */           String value;
/*  96:    */           String value;
/*  97:125 */           if (valuesForKey.length > 1) {
/*  98:127 */             value = getFormattedValues(valuesForKey).toString();
/*  99:    */           } else {
/* 100:130 */             value = request.getParameter(key);
/* 101:    */           }
/* 102:132 */           if (key.equals(keyField)) {
/* 103:133 */             keyFieldValue = value;
/* 104:    */           }
/* 105:138 */           if (value.equals("null")) {
/* 106:139 */             value = null;
/* 107:    */           }
/* 108:140 */           row.put(key, value);
/* 109:    */         }
/* 110:    */       }
/* 111:145 */       if (request.getParameter("successMsg") != null) {
/* 112:146 */         successMsg = request.getParameter("successMsg");
/* 113:    */       }
/* 114:    */     }
/* 115:    */     else
/* 116:    */     {
/* 117:    */       try
/* 118:    */       {
/* 119:163 */         for (int i = 0; i < items2.size(); i++)
/* 120:    */         {
/* 121:167 */           FileItem item = (FileItem)items2.get(i);
/* 122:168 */           if (item.isFormField())
/* 123:    */           {
/* 124:170 */             if ((!item.getFieldName().equals("submit")) && (!item.getFieldName().equals("jsonId")) && (!item.equals("successMsg")))
/* 125:    */             {
/* 126:173 */               String key = item.getFieldName();
/* 127:    */               
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:180 */               String value = item.getString();
/* 134:181 */               if (item.getFieldName().equals(keyField)) {
/* 135:182 */                 keyFieldValue = value;
/* 136:    */               }
/* 137:186 */               if (value.equals("null")) {
/* 138:187 */                 value = null;
/* 139:    */               }
/* 140:189 */               row.put(key, value);
/* 141:    */             }
/* 142:    */           }
/* 143:    */           else
/* 144:    */           {
/* 145:198 */             String path = request.getSession().getServletContext().getRealPath("/") + (String)map.get("path");
/* 146:    */             try
/* 147:    */             {
/* 148:201 */               File file = new File(path, item.getName());
/* 149:202 */               item.write(file);
/* 150:    */               
/* 151:    */ 
/* 152:205 */               row.put(item.getFieldName(), item.getName());
/* 153:    */             }
/* 154:    */             catch (FileNotFoundException localFileNotFoundException) {}
/* 155:    */           }
/* 156:    */         }
/* 157:    */       }
/* 158:    */       catch (Exception localException1) {}
/* 159:    */     }
/* 160:231 */     dbutil.saveDataFromMap(table, keyField + "='" + keyFieldValue + "'", row);
/* 161:233 */     if (isSurveyForm)
/* 162:    */     {
/* 163:235 */       CMETrainingHandler handler = new CMETrainingHandler();
/* 164:236 */       String email = request.getParameter("email");
/* 165:237 */       String timestampOfSurvey = db.uniqueResult("survey", "id", "email=?", new Object[] { email });
/* 166:    */       
/* 167:239 */       String physicianId = db.uniqueResult("physician", "id", "username=?", new Object[] { email });
/* 168:240 */       handler.sendWelcomeMailAfterSurvey(physicianId);
/* 169:241 */       System.out.println("Physician Id =" + physicianId);
/* 170:    */     }
/* 171:247 */     response.getWriter().println("<div style='margin-bottom:10px;width: auto;padding: 4px;border: solid 1px #DEDEDE;background: #FFFFCC;text-align:center;font-weight:bold;color:  #191970;position:fixed;left:45%;top:0px;'>" + successMsg + "</div>");
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String cleanInput(String input)
/* 175:    */   {
/* 176:262 */     return input.replaceAll("['\")(;|`,<>]", "");
/* 177:    */   }
/* 178:    */   
/* 179:    */   public StringBuffer getFormattedValues(String[] valuesForKey)
/* 180:    */   {
/* 181:268 */     StringBuffer result = new StringBuffer();
/* 182:269 */     for (int i = 0; i < valuesForKey.length; i++) {
/* 183:270 */       result.append(valuesForKey[i] + ",");
/* 184:    */     }
/* 185:271 */     result.deleteCharAt(result.length() - 1);
/* 186:    */     
/* 187:    */ 
/* 188:274 */     return result;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 192:    */     throws Exception
/* 193:    */   {
/* 194:283 */     service(request, response);
/* 195:284 */     return null;
/* 196:    */   }
/* 197:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.web.FormSubmitTraining
 * JD-Core Version:    0.7.0.1
 */