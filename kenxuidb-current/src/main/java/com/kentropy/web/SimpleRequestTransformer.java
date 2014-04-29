/*  1:   */ package com.kentropy.web;
/*  2:   */ 
/*  3:   */ import java.util.Enumeration;
/*  4:   */ import java.util.Vector;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ import net.xoetrope.xui.data.XModel;
/*  7:   */ 
/*  8:   */ public class SimpleRequestTransformer
/*  9:   */   implements RequestTransformer
/* 10:   */ {
/* 11:11 */   public Vector multiValueFields = new Vector();
/* 12:13 */   public String sep = ",";
/* 13:   */   
/* 14:   */   public SimpleRequestTransformer(String multi)
/* 15:   */   {
/* 16:17 */     String[] str = multi.split(",");
/* 17:18 */     for (int i = 0; i < str.length; i++) {
/* 18:20 */       this.multiValueFields.add(str[i]);
/* 19:   */     }
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String transformToJSON(XModel xm)
/* 23:   */   {
/* 24:26 */     String json = "[";
/* 25:27 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 26:   */     {
/* 27:29 */       XModel rowM = xm.get(i);
/* 28:30 */       for (int j = 0; j < rowM.getNumChildren(); j++) {
/* 29:31 */         json = json + "{name:'" + rowM.get(j).getId() + "',value:'" + rowM.get(j).get() + "'},";
/* 30:   */       }
/* 31:   */     }
/* 32:34 */     json = json + "]";
/* 33:35 */     return json;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String join(String[] values, String sep)
/* 37:   */   {
/* 38:40 */     String val = "";
/* 39:41 */     for (int i = 0; i < values.length; i++) {
/* 40:43 */       if (i == 0) {
/* 41:44 */         val = val + values[i];
/* 42:   */       } else {
/* 43:46 */         val = val + sep + values[i];
/* 44:   */       }
/* 45:   */     }
/* 46:48 */     return val;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public void transform(HttpServletRequest req, XModel xm)
/* 50:   */   {
/* 51:53 */     Enumeration e = req.getParameterNames();
/* 52:54 */     if (e.hasMoreElements())
/* 53:   */     {
/* 54:56 */       String name = e.nextElement().toString();
/* 55:57 */       String value = "";
/* 56:58 */       if (this.multiValueFields.contains(name)) {
/* 57:60 */         value = join(req.getParameterValues(name), this.sep);
/* 58:   */       } else {
/* 59:63 */         value = req.getParameter(name);
/* 60:   */       }
/* 61:64 */       ((XModel)xm.get(name)).set(value);
/* 62:   */     }
/* 63:   */   }
/* 64:   */   
/* 65:   */   public static void main(String[] args) {}
/* 66:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.web.SimpleRequestTransformer
 * JD-Core Version:    0.7.0.1
 */