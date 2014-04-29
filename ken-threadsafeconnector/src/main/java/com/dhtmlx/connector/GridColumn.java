/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ public class GridColumn
/*   4:    */ {
/*   5:    */   private String header;
/*   6:    */   private String type;
/*   7:    */   private String id;
/*   8:    */   private int width;
/*   9:    */   private String align;
/*  10:    */   private String sort;
/*  11:    */   private String color;
/*  12:    */   private String valign;
/*  13:    */   private Boolean hidden;
/*  14:    */   
/*  15:    */   public String getHeader()
/*  16:    */   {
/*  17: 15 */     return this.header;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setHeader(String value)
/*  21:    */   {
/*  22: 18 */     this.header = value;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getType()
/*  26:    */   {
/*  27: 22 */     return this.type;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setType(String value)
/*  31:    */   {
/*  32: 25 */     this.type = value;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getId()
/*  36:    */   {
/*  37: 29 */     return this.id;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setId(String value)
/*  41:    */   {
/*  42: 32 */     this.id = value;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int getWidth()
/*  46:    */   {
/*  47: 37 */     return this.width;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setWidth(int value)
/*  51:    */   {
/*  52: 40 */     this.width = value;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public String getAlign()
/*  56:    */   {
/*  57: 45 */     return this.align;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setAlign(String value)
/*  61:    */   {
/*  62: 48 */     this.align = value;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String getVAlign()
/*  66:    */   {
/*  67: 52 */     return this.valign;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setVAlign(String value)
/*  71:    */   {
/*  72: 55 */     this.valign = value;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String getSort()
/*  76:    */   {
/*  77: 59 */     return this.sort;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setSort(String value)
/*  81:    */   {
/*  82: 62 */     this.sort = value;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public String getColor()
/*  86:    */   {
/*  87: 67 */     return this.color;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setColor(String value)
/*  91:    */   {
/*  92: 70 */     this.color = value;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public Boolean isHidden()
/*  96:    */   {
/*  97: 75 */     return this.hidden;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setHidden(Boolean value)
/* 101:    */   {
/* 102: 78 */     this.hidden = value;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public GridColumn()
/* 106:    */   {
/* 107: 83 */     this.header = "";
/* 108: 84 */     this.type = "ro";
/* 109: 85 */     this.id = "";
/* 110: 86 */     this.width = 100;
/* 111: 87 */     this.align = "left";
/* 112: 88 */     this.sort = "str";
/* 113: 89 */     this.color = "";
/* 114: 90 */     this.valign = "";
/* 115: 91 */     this.hidden = Boolean.valueOf(false);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public GridColumn(String header, String type, String id, int width, String align, String valign, String sort, String color, Boolean hidden)
/* 119:    */   {
/* 120: 95 */     if (header != null) {
/* 121: 96 */       this.header = header;
/* 122:    */     }
/* 123: 98 */     if (type != null) {
/* 124: 99 */       this.type = type;
/* 125:    */     }
/* 126:101 */     if (id != null) {
/* 127:102 */       this.id = id;
/* 128:    */     }
/* 129:104 */     if (width > 0) {
/* 130:105 */       this.width = width;
/* 131:    */     }
/* 132:107 */     if (align != null) {
/* 133:108 */       this.align = align;
/* 134:    */     }
/* 135:110 */     if (valign != null) {
/* 136:111 */       this.valign = valign;
/* 137:    */     }
/* 138:113 */     if (sort != null) {
/* 139:114 */       this.sort = sort;
/* 140:    */     }
/* 141:116 */     if (color != null) {
/* 142:117 */       this.color = color;
/* 143:    */     }
/* 144:119 */     if (hidden != null) {
/* 145:120 */       this.hidden = hidden;
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public String toXML()
/* 150:    */   {
/* 151:124 */     StringBuffer xml = new StringBuffer();
/* 152:    */     
/* 153:126 */     xml.append("<column ");
/* 154:128 */     if (!this.type.equals("")) {
/* 155:128 */       xml.append(" type='" + this.type + "'");
/* 156:    */     }
/* 157:129 */     if (this.width > 0) {
/* 158:129 */       xml.append(" width='" + Integer.toString(this.width) + "'");
/* 159:    */     }
/* 160:130 */     if (!this.id.equals("")) {
/* 161:130 */       xml.append(" id='" + this.id + "'");
/* 162:    */     }
/* 163:131 */     if (!this.align.equals("")) {
/* 164:131 */       xml.append(" align='" + this.align + "'");
/* 165:    */     }
/* 166:132 */     if (!this.valign.equals("")) {
/* 167:132 */       xml.append(" valign='" + this.valign + "'");
/* 168:    */     }
/* 169:133 */     if (!this.sort.equals("")) {
/* 170:133 */       xml.append(" sort='" + this.sort + "'");
/* 171:    */     }
/* 172:134 */     if (!this.color.equals("")) {
/* 173:134 */       xml.append(" color='" + this.color + "'");
/* 174:    */     }
/* 175:135 */     if (this.hidden.booleanValue()) {
/* 176:135 */       xml.append(" hidden='true'");
/* 177:    */     }
/* 178:137 */     xml.append(">");
/* 179:138 */     xml.append(this.header);
/* 180:139 */     xml.append("</column>");
/* 181:    */     
/* 182:141 */     return xml.toString();
/* 183:    */   }
/* 184:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.GridColumn
 * JD-Core Version:    0.7.0.1
 */