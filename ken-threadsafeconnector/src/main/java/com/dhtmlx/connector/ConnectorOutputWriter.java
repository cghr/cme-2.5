/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.Writer;
/*   6:    */ import javax.servlet.ServletOutputStream;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ 
/*   9:    */ public class ConnectorOutputWriter
/*  10:    */ {
/*  11:    */   StringBuffer start;
/*  12:    */   StringBuffer end;
/*  13:    */   ExportServiceType type;
/*  14:    */   private String name;
/*  15:    */   private Boolean inline;
/*  16:    */   private ByteArrayOutputStream bdata;
/*  17:    */   
/*  18:    */   public ConnectorOutputWriter(String start, String end)
/*  19:    */   {
/*  20: 18 */     this.start = new StringBuffer(start);
/*  21: 19 */     this.end = new StringBuffer(end);
/*  22: 20 */     this.type = ExportServiceType.XML;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void add(String data)
/*  26:    */   {
/*  27: 24 */     this.start.append(data);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void add(ByteArrayOutputStream data)
/*  31:    */   {
/*  32: 28 */     this.bdata = data;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void reset()
/*  36:    */   {
/*  37: 32 */     this.start.delete(0, this.start.length());
/*  38: 33 */     this.end.delete(0, this.end.length());
/*  39: 34 */     this.bdata = null;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void set_type(ExportServiceType type)
/*  43:    */   {
/*  44: 38 */     this.type = type;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void set_type(ExportServiceType type, String name, Boolean inline)
/*  48:    */   {
/*  49: 41 */     this.type = type;
/*  50: 42 */     this.name = name;
/*  51: 43 */     this.inline = inline;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void output(HttpServletResponse http_response, String encoding)
/*  55:    */   {
/*  56: 47 */     http_response.reset();
/*  57:    */     try
/*  58:    */     {
/*  59: 49 */       if (this.type == ExportServiceType.XML)
/*  60:    */       {
/*  61: 50 */         http_response.addHeader("Content-type", "text/xml;charset=" + encoding);
/*  62:    */         
/*  63: 52 */         Writer out = http_response.getWriter();
/*  64: 53 */         out.write(toString().toCharArray());
/*  65: 54 */         out.close();
/*  66: 55 */         http_response.flushBuffer();
/*  67:    */       }
/*  68: 57 */       else if (this.type == ExportServiceType.PDF)
/*  69:    */       {
/*  70: 58 */         http_response.setContentType("application/pdf");
/*  71: 59 */         asFile(http_response);
/*  72:    */       }
/*  73: 60 */       else if (this.type == ExportServiceType.Excel)
/*  74:    */       {
/*  75: 61 */         http_response.setContentType("application/vnd.ms-excel");
/*  76: 62 */         asFile(http_response);
/*  77:    */       }
/*  78:    */     }
/*  79:    */     catch (IOException e)
/*  80:    */     {
/*  81: 65 */       LogManager.getInstance().log("Error during data outputing");
/*  82: 66 */       LogManager.getInstance().log(e.getMessage());
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   private void asFile(HttpServletResponse http_response)
/*  87:    */     throws IOException
/*  88:    */   {
/*  89: 71 */     http_response.addHeader("Content-Type", "application/force-download");
/*  90: 72 */     http_response.addHeader("Content-Type", "application/octet-stream");
/*  91: 73 */     http_response.addHeader("Content-Type", "application/download");
/*  92: 74 */     http_response.addHeader("Content-Transfer-Encoding", "binary");
/*  93:    */     
/*  94: 76 */     http_response.addHeader("Content-Length", Integer.toString(getSize()));
/*  95: 77 */     if (this.inline.booleanValue()) {
/*  96: 78 */       http_response.addHeader("Content-Disposition", "inline; filename=\"" + this.name + "\";");
/*  97:    */     } else {
/*  98: 80 */       http_response.addHeader("Content-Disposition", "attachment; filename=\"" + this.name + "\";");
/*  99:    */     }
/* 100: 82 */     ServletOutputStream out = http_response.getOutputStream();
/* 101: 83 */     out.write(toBytes());
/* 102: 84 */     out.flush();
/* 103: 85 */     out.close();
/* 104:    */   }
/* 105:    */   
/* 106:    */   private int getSize()
/* 107:    */   {
/* 108: 89 */     if (this.bdata != null) {
/* 109: 90 */       return this.bdata.size();
/* 110:    */     }
/* 111: 91 */     return this.start.length() + this.end.length();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public String toString()
/* 115:    */   {
/* 116: 95 */     return this.start.toString() + this.end.toString();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public byte[] toBytes()
/* 120:    */   {
/* 121: 98 */     if (this.bdata != null) {
/* 122: 99 */       return this.bdata.toByteArray();
/* 123:    */     }
/* 124:100 */     return toString().getBytes();
/* 125:    */   }
/* 126:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ConnectorOutputWriter
 * JD-Core Version:    0.7.0.1
 */