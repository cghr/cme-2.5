/*  1:   */ package com.dhtmlx.xml2excel;
/*  2:   */ 
/*  3:   */ import java.util.Hashtable;
/*  4:   */ import java.util.regex.Matcher;
/*  5:   */ import java.util.regex.Pattern;
/*  6:   */ import jxl.format.Colour;
/*  7:   */ import jxl.write.WritableWorkbook;
/*  8:   */ 
/*  9:   */ public class RGBColor
/* 10:   */ {
/* 11:10 */   int colorCounter = 63;
/* 12:11 */   Hashtable<String, Colour> parsedColors = new Hashtable();
/* 13:   */   
/* 14:   */   public Colour getColor(String color, WritableWorkbook wb)
/* 15:   */   {
/* 16:15 */     String original = color;
/* 17:16 */     if (!this.parsedColors.containsKey(color))
/* 18:   */     {
/* 19:17 */       color = processColorForm(color);
/* 20:18 */       int[] result = new int[3];
/* 21:19 */       String r = color.substring(0, 2);
/* 22:20 */       String g = color.substring(2, 4);
/* 23:21 */       String b = color.substring(4, 6);
/* 24:22 */       result[0] = Integer.parseInt(r, 16);
/* 25:23 */       result[1] = Integer.parseInt(g, 16);
/* 26:24 */       result[2] = Integer.parseInt(b, 16);
/* 27:   */       
/* 28:26 */       Colour col = new Colour(this.colorCounter, "custom", result[0], result[1], result[2]);
/* 29:27 */       wb.setColourRGB(col, result[0], result[1], result[2]);
/* 30:28 */       this.parsedColors.put(original, col);
/* 31:   */       
/* 32:30 */       this.colorCounter -= 1;
/* 33:31 */       if (this.colorCounter < 32) {
/* 34:31 */         this.colorCounter = 63;
/* 35:   */       }
/* 36:33 */       return col;
/* 37:   */     }
/* 38:35 */     return (Colour)this.parsedColors.get(color);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String processColorForm(String color)
/* 42:   */   {
/* 43:40 */     if (color.equals("transparent")) {
/* 44:41 */       return "";
/* 45:   */     }
/* 46:44 */     Pattern p1 = Pattern.compile("#[0-9A-Fa-f]{6}");
/* 47:45 */     Matcher m1 = p1.matcher(color);
/* 48:46 */     if (m1.matches()) {
/* 49:47 */       return color.substring(1);
/* 50:   */     }
/* 51:50 */     Pattern p2 = Pattern.compile("[0-9A-Fa-f]{6}");
/* 52:51 */     Matcher m2 = p2.matcher(color);
/* 53:52 */     if (m2.matches()) {
/* 54:53 */       return color;
/* 55:   */     }
/* 56:56 */     Pattern p3 = Pattern.compile("rgb\\s?\\(\\s?(\\d{1,3})\\s?,\\s?(\\d{1,3})\\s?,\\s?(\\d{1,3})\\s?\\)");
/* 57:57 */     Matcher m3 = p3.matcher(color);
/* 58:59 */     if (m3.matches())
/* 59:   */     {
/* 60:60 */       color = "";
/* 61:61 */       String r = m3.group(1);
/* 62:62 */       String g = m3.group(2);
/* 63:63 */       String b = m3.group(3);
/* 64:64 */       r = Integer.toHexString(Integer.parseInt(r));
/* 65:65 */       r = r.length() == 1 ? "0" + r : r;
/* 66:66 */       g = Integer.toHexString(Integer.parseInt(g));
/* 67:67 */       g = g.length() == 1 ? "0" + g : g;
/* 68:68 */       b = Integer.toHexString(Integer.parseInt(b));
/* 69:69 */       b = b.length() == 1 ? "0" + b : b;
/* 70:70 */       color = r + g + b;
/* 71:71 */       return color;
/* 72:   */     }
/* 73:73 */     return "";
/* 74:   */   }
/* 75:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-xml2excel\ken-xml2excel.jar
 * Qualified Name:     com.dhtmlx.xml2excel.RGBColor
 * JD-Core Version:    0.7.0.1
 */