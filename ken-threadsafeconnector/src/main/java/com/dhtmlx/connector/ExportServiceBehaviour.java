/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayOutputStream;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.io.OutputStreamWriter;
/*  6:   */ import java.net.HttpURLConnection;
/*  7:   */ import java.net.URL;
/*  8:   */ import java.net.URLEncoder;
/*  9:   */ import javax.servlet.http.HttpServletRequest;
/* 10:   */ import javax.servlet.http.HttpServletResponse;
/* 11:   */ 
/* 12:   */ public class ExportServiceBehaviour
/* 13:   */   extends ConnectorBehavior
/* 14:   */ {
/* 15:   */   private String url;
/* 16:   */   private ExportServiceType type;
/* 17:   */   protected String name;
/* 18:   */   protected Boolean inline;
/* 19:   */   
/* 20:   */   public ExportServiceBehaviour(String url, ExportServiceType type)
/* 21:   */   {
/* 22:21 */     this.url = url;
/* 23:22 */     this.type = type;
/* 24:23 */     this.inline = Boolean.valueOf(false);
/* 25:24 */     if (this.type == ExportServiceType.PDF) {
/* 26:25 */       this.name = "report.pdf";
/* 27:   */     } else {
/* 28:27 */       this.name = "report.xls";
/* 29:   */     }
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void beforeOutput(ConnectorOutputWriter out, HttpServletRequest http_request, HttpServletResponse http_response)
/* 33:   */   {
/* 34:32 */     String temp_data = out.toString().replace("<head>", "<head><columns>").replace("</head>", "</columns></head>");
/* 35:   */     try
/* 36:   */     {
/* 37:35 */       URL conversion = new URL(this.url);
/* 38:36 */       HttpURLConnection process = (HttpURLConnection)conversion.openConnection();
/* 39:   */       
/* 40:38 */       process.setDoInput(true);
/* 41:39 */       process.setDoOutput(true);
/* 42:40 */       process.setUseCaches(false);
/* 43:41 */       process.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/* 44:   */       
/* 45:   */ 
/* 46:44 */       OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
/* 47:   */       
/* 48:   */ 
/* 49:47 */       writer.write(URLEncoder.encode("grid_xml", "UTF-8") + "=" + URLEncoder.encode(temp_data, "UTF-8"));
/* 50:48 */       writer.flush();
/* 51:49 */       writer.close();
/* 52:50 */       process.connect();
/* 53:   */       
/* 54:52 */       InputStream is = process.getInputStream();
/* 55:53 */       ByteArrayOutputStream data = new ByteArrayOutputStream();
/* 56:54 */       byte[] byteChunk = new byte[4096];
/* 57:   */       int n;
/* 58:57 */       while ((n = is.read(byteChunk)) > 0)
/* 59:   */       {
/* 60:   */         int n;
/* 61:58 */         data.write(byteChunk, 0, n);
/* 62:   */       }
/* 63:62 */       is.close();
/* 64:   */       
/* 65:64 */       out.reset();
/* 66:65 */       out.add(data);
/* 67:66 */       out.set_type(this.type, this.name, this.inline);
/* 68:   */     }
/* 69:   */     catch (Throwable e)
/* 70:   */     {
/* 71:68 */       e.printStackTrace();
/* 72:   */     }
/* 73:71 */     super.beforeOutput(out, http_request, http_response);
/* 74:   */   }
/* 75:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ExportServiceBehaviour
 * JD-Core Version:    0.7.0.1
 */