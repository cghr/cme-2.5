/*   1:    */ package jxl;
/*   2:    */ 
/*   3:    */ public final class HeaderFooter
/*   4:    */   extends jxl.biff.HeaderFooter
/*   5:    */ {
/*   6:    */   public HeaderFooter() {}
/*   7:    */   
/*   8:    */   public static class Contents
/*   9:    */     extends jxl.biff.HeaderFooter.Contents
/*  10:    */   {
/*  11:    */     Contents() {}
/*  12:    */     
/*  13:    */     Contents(String s)
/*  14:    */     {
/*  15: 48 */       super();
/*  16:    */     }
/*  17:    */     
/*  18:    */     Contents(Contents copy)
/*  19:    */     {
/*  20: 58 */       super();
/*  21:    */     }
/*  22:    */     
/*  23:    */     public void append(String txt)
/*  24:    */     {
/*  25: 68 */       super.append(txt);
/*  26:    */     }
/*  27:    */     
/*  28:    */     public void toggleBold()
/*  29:    */     {
/*  30: 79 */       super.toggleBold();
/*  31:    */     }
/*  32:    */     
/*  33:    */     public void toggleUnderline()
/*  34:    */     {
/*  35: 90 */       super.toggleUnderline();
/*  36:    */     }
/*  37:    */     
/*  38:    */     public void toggleItalics()
/*  39:    */     {
/*  40:101 */       super.toggleItalics();
/*  41:    */     }
/*  42:    */     
/*  43:    */     public void toggleStrikethrough()
/*  44:    */     {
/*  45:112 */       super.toggleStrikethrough();
/*  46:    */     }
/*  47:    */     
/*  48:    */     public void toggleDoubleUnderline()
/*  49:    */     {
/*  50:123 */       super.toggleDoubleUnderline();
/*  51:    */     }
/*  52:    */     
/*  53:    */     public void toggleSuperScript()
/*  54:    */     {
/*  55:134 */       super.toggleSuperScript();
/*  56:    */     }
/*  57:    */     
/*  58:    */     public void toggleSubScript()
/*  59:    */     {
/*  60:145 */       super.toggleSubScript();
/*  61:    */     }
/*  62:    */     
/*  63:    */     public void toggleOutline()
/*  64:    */     {
/*  65:156 */       super.toggleOutline();
/*  66:    */     }
/*  67:    */     
/*  68:    */     public void toggleShadow()
/*  69:    */     {
/*  70:167 */       super.toggleShadow();
/*  71:    */     }
/*  72:    */     
/*  73:    */     public void setFontName(String fontName)
/*  74:    */     {
/*  75:181 */       super.setFontName(fontName);
/*  76:    */     }
/*  77:    */     
/*  78:    */     public boolean setFontSize(int size)
/*  79:    */     {
/*  80:200 */       return super.setFontSize(size);
/*  81:    */     }
/*  82:    */     
/*  83:    */     public void appendPageNumber()
/*  84:    */     {
/*  85:208 */       super.appendPageNumber();
/*  86:    */     }
/*  87:    */     
/*  88:    */     public void appendTotalPages()
/*  89:    */     {
/*  90:216 */       super.appendTotalPages();
/*  91:    */     }
/*  92:    */     
/*  93:    */     public void appendDate()
/*  94:    */     {
/*  95:224 */       super.appendDate();
/*  96:    */     }
/*  97:    */     
/*  98:    */     public void appendTime()
/*  99:    */     {
/* 100:232 */       super.appendTime();
/* 101:    */     }
/* 102:    */     
/* 103:    */     public void appendWorkbookName()
/* 104:    */     {
/* 105:240 */       super.appendWorkbookName();
/* 106:    */     }
/* 107:    */     
/* 108:    */     public void appendWorkSheetName()
/* 109:    */     {
/* 110:248 */       super.appendWorkSheetName();
/* 111:    */     }
/* 112:    */     
/* 113:    */     public void clear()
/* 114:    */     {
/* 115:256 */       super.clear();
/* 116:    */     }
/* 117:    */     
/* 118:    */     public boolean empty()
/* 119:    */     {
/* 120:266 */       return super.empty();
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public HeaderFooter(HeaderFooter hf)
/* 125:    */   {
/* 126:285 */     super(hf);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public HeaderFooter(String s)
/* 130:    */   {
/* 131:296 */     super(s);
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String toString()
/* 135:    */   {
/* 136:307 */     return super.toString();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public Contents getRight()
/* 140:    */   {
/* 141:317 */     return (Contents)super.getRightText();
/* 142:    */   }
/* 143:    */   
/* 144:    */   public Contents getCentre()
/* 145:    */   {
/* 146:327 */     return (Contents)super.getCentreText();
/* 147:    */   }
/* 148:    */   
/* 149:    */   public Contents getLeft()
/* 150:    */   {
/* 151:337 */     return (Contents)super.getLeftText();
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void clear()
/* 155:    */   {
/* 156:345 */     super.clear();
/* 157:    */   }
/* 158:    */   
/* 159:    */   protected jxl.biff.HeaderFooter.Contents createContents()
/* 160:    */   {
/* 161:355 */     return new Contents();
/* 162:    */   }
/* 163:    */   
/* 164:    */   protected jxl.biff.HeaderFooter.Contents createContents(String s)
/* 165:    */   {
/* 166:366 */     return new Contents(s);
/* 167:    */   }
/* 168:    */   
/* 169:    */   protected jxl.biff.HeaderFooter.Contents createContents(jxl.biff.HeaderFooter.Contents c)
/* 170:    */   {
/* 171:378 */     return new Contents((Contents)c);
/* 172:    */   }
/* 173:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.HeaderFooter
 * JD-Core Version:    0.7.0.1
 */