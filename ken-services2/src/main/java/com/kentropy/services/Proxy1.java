/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.net.HttpURLConnection;
/*   9:    */ import java.net.URL;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import javax.servlet.http.HttpServletResponse;
/*  12:    */ import org.springframework.web.servlet.ModelAndView;
/*  13:    */ import org.springframework.web.servlet.mvc.Controller;
/*  14:    */ 
/*  15:    */ public class Proxy1
/*  16:    */   implements Controller
/*  17:    */ {
/*  18: 22 */   public static String SERVER_URL = "http://54.243.251.166/cme/";
/*  19:    */   
/*  20:    */   public static void main(String[] args) {}
/*  21:    */   
/*  22:    */   public Proxy1()
/*  23:    */   {
/*  24: 33 */     SERVER_URL = TestXUIDB.getInstance().getProperty("SERVER_URL");
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static void copyStream(InputStream in, OutputStream out)
/*  28:    */     throws IOException
/*  29:    */   {
/*  30: 37 */     byte[] buf = new byte[2048];
/*  31:    */     int count;
/*  32: 40 */     while ((count = in.read(buf)) != -1)
/*  33:    */     {
/*  34:    */       int count;
/*  35: 41 */       out.write(buf, 0, count);
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void proxy(String url, HttpServletRequest request, HttpServletResponse response)
/*  40:    */     throws Exception
/*  41:    */   {
/*  42: 53 */     System.out.println(url);
/*  43: 54 */     HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
/*  44: 55 */     con.setDoInput(true);
/*  45: 56 */     con.setRequestProperty("Content-Type", 
/*  46: 57 */       "application/binary");
/*  47:    */     
/*  48: 59 */     con.setRequestProperty("Content-Length", 
/*  49: 60 */       "20000");
/*  50: 61 */     con.setRequestProperty("Connection", "Keep-Alive");
/*  51: 62 */     con.setRequestMethod(request.getMethod());
/*  52: 63 */     con.setDoOutput(true);
/*  53: 64 */     con.connect();
/*  54: 65 */     InputStream reqIs = request.getInputStream();
/*  55: 66 */     OutputStream serverOs = con.getOutputStream();
/*  56: 67 */     copyStream(reqIs, serverOs);
/*  57:    */     
/*  58: 69 */     InputStream serverIs = con.getInputStream();
/*  59: 70 */     OutputStream respOs = response.getOutputStream();
/*  60: 71 */     copyStream(serverIs, respOs);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/*  64:    */     throws Exception
/*  65:    */   {
/*  66: 81 */     String path = request.getParameter("path");
/*  67: 82 */     System.out.println(SERVER_URL + path);
/*  68: 83 */     proxy(SERVER_URL + path, request, response);
/*  69:    */     
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:    */ 
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:    */ 
/*  85:    */ 
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:104 */     return null;
/*  90:    */   }
/*  91:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.Proxy1
 * JD-Core Version:    0.7.0.1
 */