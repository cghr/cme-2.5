/*   1:    */ package com.kentropy.formtest;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbUtil;
/*   4:    */ import com.kentropy.util.SpringUtils;
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
/*  24:    */ import org.apache.commons.lang3.StringEscapeUtils;
/*  25:    */ import org.jsoup.Jsoup;
/*  26:    */ import org.jsoup.safety.Whitelist;
/*  27:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  28:    */ import org.springframework.web.servlet.ModelAndView;
/*  29:    */ import org.springframework.web.servlet.mvc.Controller;
/*  30:    */ 
/*  31:    */ public class FormSubmitTest
/*  32:    */   extends HttpServlet
/*  33:    */   implements Controller
/*  34:    */ {
/*  35:    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*  36:    */     throws ServletException, IOException
/*  37:    */   {
/*  38: 44 */     System.out.print(" test 1");
/*  39:    */     
/*  40: 46 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  41: 47 */     HttpSession session = request.getSession();
/*  42: 48 */     String username = (String)session.getAttribute("username");
/*  43:    */     
/*  44: 50 */     String form_jsonId = "";
/*  45: 51 */     String qry = "insert into form_history(jsonId,user,fields,time) values(?,?,?,NOW())";
/*  46: 52 */     String output = "{";
/*  47: 53 */     String jsonId = "";
/*  48:    */     
/*  49: 55 */     boolean isMultipart = FileUpload.isMultipartContent(request);
/*  50: 56 */     String successMsg = "Submitted Successfully !";
/*  51: 57 */     boolean allowHtml = false;
/*  52:    */     
/*  53: 59 */     DiskFileItemFactory factory = new DiskFileItemFactory();
/*  54:    */     
/*  55: 61 */     ServletFileUpload dskfu = new ServletFileUpload(factory);
/*  56: 62 */     List items2 = null;
/*  57: 63 */     if (isMultipart)
/*  58:    */     {
/*  59: 65 */       System.out.println("Inside is Multipart form submit");
/*  60:    */       try
/*  61:    */       {
/*  62: 69 */         List items = dskfu.parseRequest(request);
/*  63:    */         
/*  64: 71 */         items2 = items;
/*  65: 73 */         for (int i = 0; i < items.size(); i++)
/*  66:    */         {
/*  67: 76 */           FileItem item = (FileItem)items.get(i);
/*  68: 77 */           if (item.isFormField())
/*  69:    */           {
/*  70: 78 */             String name = item.getFieldName();
/*  71: 79 */             String value = item.getString();
/*  72: 80 */             output = output + name + ":['" + value + "'],";
/*  73:    */           }
/*  74: 86 */           if ((item.isFormField()) && (item.getFieldName().equals("jsonId"))) {
/*  75: 88 */             jsonId = item.getString();
/*  76:    */           }
/*  77:    */         }
/*  78: 92 */         output = output + "}";
/*  79:    */         
/*  80: 94 */         String[] temp = output.split("'");
/*  81: 95 */         form_jsonId = temp[1];
/*  82:    */       }
/*  83:    */       catch (Exception localException) {}
/*  84:    */     }
/*  85:    */     else
/*  86:    */     {
/*  87:105 */       System.out.println(" Not Inside isMultipart form submit ");
/*  88:106 */       jsonId = request.getParameter("jsonId");
/*  89:    */       
/*  90:108 */       form_jsonId = jsonId;
/*  91:    */     }
/*  92:111 */     DbUtil dbutil = new DbUtil();
/*  93:    */     
/*  94:    */ 
/*  95:114 */     System.out.println("Requested jsonId = " + jsonId);
/*  96:115 */     Map map = (Map)dbutil.getDataAsListofMaps("json_datadict", "*", "id='" + jsonId + "'").get(0);
/*  97:    */     
/*  98:117 */     String table = (String)map.get("jsontable");
/*  99:118 */     String keyField = (String)map.get("keyfield");
/* 100:119 */     int htmlFlag = Integer.parseInt((String)map.get("html"));
/* 101:121 */     if (htmlFlag == 1) {
/* 102:122 */       allowHtml = true;
/* 103:    */     }
/* 104:125 */     Map row = new HashMap();
/* 105:    */     
/* 106:127 */     String keyFieldValue = "";
/* 107:129 */     if (!isMultipart)
/* 108:    */     {
/* 109:131 */       Enumeration keys = request.getParameterNames();
/* 110:133 */       while (keys.hasMoreElements())
/* 111:    */       {
/* 112:135 */         String key = (String)keys.nextElement();
/* 113:137 */         if ((!key.equals("submit")) && (!key.equals("jsonId")) && (!key.equals("successMsg")) && (!key.equals("csrfGuard")))
/* 114:    */         {
/* 115:139 */           String[] valuesForKey = request.getParameterValues(key);
/* 116:    */           String value;
/* 117:    */           String value;
/* 118:142 */           if (valuesForKey.length > 1) {
/* 119:143 */             value = getFormattedValues(valuesForKey).toString();
/* 120:    */           } else {
/* 121:146 */             value = cleanInput(request.getParameter(key));
/* 122:    */           }
/* 123:149 */           if (key.equals(keyField)) {
/* 124:150 */             keyFieldValue = value;
/* 125:    */           }
/* 126:153 */           if (value.equals("null")) {
/* 127:154 */             value = null;
/* 128:    */           }
/* 129:155 */           row.put(key, value);
/* 130:    */           
/* 131:157 */           output = output + key + ":['" + value + "'],";
/* 132:    */         }
/* 133:    */       }
/* 134:159 */       output = output + "}";
/* 135:160 */       if (request.getParameter("successMsg") != null) {
/* 136:161 */         successMsg = request.getParameter("successMsg");
/* 137:    */       }
/* 138:    */     }
/* 139:    */     else
/* 140:    */     {
/* 141:    */       try
/* 142:    */       {
/* 143:169 */         for (int i = 0; i < items2.size(); i++)
/* 144:    */         {
/* 145:171 */           FileItem item = (FileItem)items2.get(i);
/* 146:172 */           if (item.isFormField())
/* 147:    */           {
/* 148:174 */             if ((!item.getFieldName().equals("submit")) && (!item.getFieldName().equals("jsonId")) && (!item.getFieldName().equals("successMsg")) && (!item.getFieldName().equals("csrfGuard")))
/* 149:    */             {
/* 150:177 */               String key = item.getFieldName();
/* 151:    */               
/* 152:179 */               String value = cleanInput(item.getString());
/* 153:181 */               if (item.getFieldName().equals(keyField)) {
/* 154:182 */                 keyFieldValue = value;
/* 155:    */               }
/* 156:185 */               if (value.equals("null")) {
/* 157:186 */                 value = null;
/* 158:    */               }
/* 159:188 */               row.put(key, value);
/* 160:    */             }
/* 161:    */           }
/* 162:    */           else
/* 163:    */           {
/* 164:192 */             String path = request.getSession().getServletContext().getRealPath("/") + (String)map.get("path");
/* 165:    */             try
/* 166:    */             {
/* 167:195 */               File file = new File(path, item.getName());
/* 168:196 */               item.write(file);
/* 169:    */               
/* 170:198 */               row.put(item.getFieldName(), cleanInput(item.getName()));
/* 171:    */             }
/* 172:    */             catch (FileNotFoundException localFileNotFoundException) {}
/* 173:    */           }
/* 174:    */         }
/* 175:    */       }
/* 176:    */       catch (Exception localException1) {}
/* 177:    */     }
/* 178:215 */     dbutil.saveDataFromMap(table, keyField + "='" + keyFieldValue + "'", row);
/* 179:    */     
/* 180:    */ 
/* 181:218 */     System.out.println("form_jsonid_formHistory:" + form_jsonId);
/* 182:219 */     System.out.println("form_history_field:" + output);
/* 183:220 */     jt.update(qry, new Object[] { form_jsonId, username, output });
/* 184:    */     
/* 185:    */ 
/* 186:223 */     response.setContentType("text/html");
/* 187:    */     
/* 188:225 */     response.getWriter().println("<div style='margin-bottom:10px;width: auto;padding: 4px;border: solid 1px #DEDEDE;background: #FFFFCC;text-align:center;font-weight:bold;color:  #191970;position:fixed;left:40%;top:0px;'>" + successMsg + "</div>");
/* 189:    */   }
/* 190:    */   
/* 191:    */   public String cleanInput(String input)
/* 192:    */   {
/* 193:230 */     return Jsoup.clean(StringEscapeUtils.unescapeHtml4(input), Whitelist.basic());
/* 194:    */   }
/* 195:    */   
/* 196:    */   public StringBuffer getFormattedValues(String[] valuesForKey)
/* 197:    */   {
/* 198:235 */     StringBuffer result = new StringBuffer();
/* 199:236 */     for (int i = 0; i < valuesForKey.length; i++) {
/* 200:237 */       result.append(cleanInput(valuesForKey[i]) + ",");
/* 201:    */     }
/* 202:240 */     result.deleteCharAt(result.length() - 1);
/* 203:    */     
/* 204:242 */     return result;
/* 205:    */   }
/* 206:    */   
/* 207:    */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 208:    */     throws Exception
/* 209:    */   {
/* 210:248 */     service(request, response);
/* 211:249 */     return null;
/* 212:    */   }
/* 213:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.formtest.FormSubmitTest
 * JD-Core Version:    0.7.0.1
 */