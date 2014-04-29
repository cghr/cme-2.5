/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import jxl.common.Assert;
/*   7:    */ import jxl.write.biff.File;
/*   8:    */ 
/*   9:    */ public class Fonts
/*  10:    */ {
/*  11:    */   private ArrayList fonts;
/*  12:    */   private static final int numDefaultFonts = 4;
/*  13:    */   
/*  14:    */   public Fonts()
/*  15:    */   {
/*  16: 50 */     this.fonts = new ArrayList();
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void addFont(FontRecord f)
/*  20:    */   {
/*  21: 63 */     if (!f.isInitialized())
/*  22:    */     {
/*  23: 65 */       int pos = this.fonts.size();
/*  24: 68 */       if (pos >= 4) {
/*  25: 70 */         pos++;
/*  26:    */       }
/*  27: 73 */       f.initialize(pos);
/*  28: 74 */       this.fonts.add(f);
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public FontRecord getFont(int index)
/*  33:    */   {
/*  34: 88 */     if (index > 4) {
/*  35: 90 */       index--;
/*  36:    */     }
/*  37: 93 */     return (FontRecord)this.fonts.get(index);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void write(File outputFile)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43:104 */     Iterator i = this.fonts.iterator();
/*  44:    */     
/*  45:106 */     FontRecord font = null;
/*  46:107 */     while (i.hasNext())
/*  47:    */     {
/*  48:109 */       font = (FontRecord)i.next();
/*  49:110 */       outputFile.write(font);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   IndexMapping rationalize()
/*  54:    */   {
/*  55:121 */     IndexMapping mapping = new IndexMapping(this.fonts.size() + 1);
/*  56:    */     
/*  57:    */ 
/*  58:124 */     ArrayList newfonts = new ArrayList();
/*  59:125 */     FontRecord fr = null;
/*  60:126 */     int numremoved = 0;
/*  61:129 */     for (int i = 0; i < 4; i++)
/*  62:    */     {
/*  63:131 */       fr = (FontRecord)this.fonts.get(i);
/*  64:132 */       newfonts.add(fr);
/*  65:133 */       mapping.setMapping(fr.getFontIndex(), fr.getFontIndex());
/*  66:    */     }
/*  67:137 */     Iterator it = null;
/*  68:138 */     FontRecord fr2 = null;
/*  69:139 */     boolean duplicate = false;
/*  70:140 */     for (int i = 4; i < this.fonts.size(); i++)
/*  71:    */     {
/*  72:142 */       fr = (FontRecord)this.fonts.get(i);
/*  73:    */       
/*  74:    */ 
/*  75:145 */       duplicate = false;
/*  76:146 */       it = newfonts.iterator();
/*  77:147 */       while ((it.hasNext()) && (!duplicate))
/*  78:    */       {
/*  79:149 */         fr2 = (FontRecord)it.next();
/*  80:150 */         if (fr.equals(fr2))
/*  81:    */         {
/*  82:152 */           duplicate = true;
/*  83:153 */           mapping.setMapping(fr.getFontIndex(), 
/*  84:154 */             mapping.getNewIndex(fr2.getFontIndex()));
/*  85:155 */           numremoved++;
/*  86:    */         }
/*  87:    */       }
/*  88:159 */       if (!duplicate)
/*  89:    */       {
/*  90:162 */         newfonts.add(fr);
/*  91:163 */         int newindex = fr.getFontIndex() - numremoved;
/*  92:164 */         Assert.verify(newindex > 4);
/*  93:165 */         mapping.setMapping(fr.getFontIndex(), newindex);
/*  94:    */       }
/*  95:    */     }
/*  96:170 */     it = newfonts.iterator();
/*  97:171 */     while (it.hasNext())
/*  98:    */     {
/*  99:173 */       fr = (FontRecord)it.next();
/* 100:174 */       fr.initialize(mapping.getNewIndex(fr.getFontIndex()));
/* 101:    */     }
/* 102:177 */     this.fonts = newfonts;
/* 103:    */     
/* 104:179 */     return mapping;
/* 105:    */   }
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.Fonts
 * JD-Core Version:    0.7.0.1
 */