/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import jxl.common.Assert;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ public class DrawingData
/*   8:    */   implements EscherStream
/*   9:    */ {
/*  10: 36 */   private static Logger logger = Logger.getLogger(DrawingData.class);
/*  11:    */   private byte[] drawingData;
/*  12:    */   private int numDrawings;
/*  13:    */   private boolean initialized;
/*  14:    */   private EscherRecord[] spContainers;
/*  15:    */   
/*  16:    */   public DrawingData()
/*  17:    */   {
/*  18: 63 */     this.numDrawings = 0;
/*  19: 64 */     this.drawingData = null;
/*  20: 65 */     this.initialized = false;
/*  21:    */   }
/*  22:    */   
/*  23:    */   private void initialize()
/*  24:    */   {
/*  25: 73 */     EscherRecordData er = new EscherRecordData(this, 0);
/*  26: 74 */     Assert.verify(er.isContainer());
/*  27:    */     
/*  28: 76 */     EscherContainer dgContainer = new EscherContainer(er);
/*  29: 77 */     EscherRecord[] children = dgContainer.getChildren();
/*  30:    */     
/*  31: 79 */     children = dgContainer.getChildren();
/*  32:    */     
/*  33:    */ 
/*  34: 82 */     EscherContainer spgrContainer = null;
/*  35: 84 */     for (int i = 0; (i < children.length) && (spgrContainer == null); i++)
/*  36:    */     {
/*  37: 86 */       EscherRecord child = children[i];
/*  38: 87 */       if (child.getType() == EscherRecordType.SPGR_CONTAINER) {
/*  39: 89 */         spgrContainer = (EscherContainer)child;
/*  40:    */       }
/*  41:    */     }
/*  42: 92 */     Assert.verify(spgrContainer != null);
/*  43:    */     
/*  44: 94 */     EscherRecord[] spgrChildren = spgrContainer.getChildren();
/*  45:    */     
/*  46:    */ 
/*  47: 97 */     boolean nestedContainers = false;
/*  48: 98 */     for (int i = 0; (i < spgrChildren.length) && (!nestedContainers); i++) {
/*  49:100 */       if (spgrChildren[i].getType() == EscherRecordType.SPGR_CONTAINER) {
/*  50:102 */         nestedContainers = true;
/*  51:    */       }
/*  52:    */     }
/*  53:108 */     if (!nestedContainers)
/*  54:    */     {
/*  55:110 */       this.spContainers = spgrChildren;
/*  56:    */     }
/*  57:    */     else
/*  58:    */     {
/*  59:115 */       ArrayList sps = new ArrayList();
/*  60:116 */       getSpContainers(spgrContainer, sps);
/*  61:117 */       this.spContainers = new EscherRecord[sps.size()];
/*  62:118 */       this.spContainers = ((EscherRecord[])sps.toArray(this.spContainers));
/*  63:    */     }
/*  64:121 */     this.initialized = true;
/*  65:    */   }
/*  66:    */   
/*  67:    */   private void getSpContainers(EscherContainer spgrContainer, ArrayList sps)
/*  68:    */   {
/*  69:132 */     EscherRecord[] spgrChildren = spgrContainer.getChildren();
/*  70:133 */     for (int i = 0; i < spgrChildren.length; i++) {
/*  71:135 */       if (spgrChildren[i].getType() == EscherRecordType.SP_CONTAINER) {
/*  72:137 */         sps.add(spgrChildren[i]);
/*  73:139 */       } else if (spgrChildren[i].getType() == EscherRecordType.SPGR_CONTAINER) {
/*  74:141 */         getSpContainers((EscherContainer)spgrChildren[i], sps);
/*  75:    */       } else {
/*  76:145 */         logger.warn("Spgr Containers contains a record other than Sp/Spgr containers");
/*  77:    */       }
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void addData(byte[] data)
/*  82:    */   {
/*  83:158 */     addRawData(data);
/*  84:159 */     this.numDrawings += 1;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void addRawData(byte[] data)
/*  88:    */   {
/*  89:171 */     if (this.drawingData == null)
/*  90:    */     {
/*  91:173 */       this.drawingData = data;
/*  92:174 */       return;
/*  93:    */     }
/*  94:178 */     byte[] newArray = new byte[this.drawingData.length + data.length];
/*  95:179 */     System.arraycopy(this.drawingData, 0, newArray, 0, this.drawingData.length);
/*  96:180 */     System.arraycopy(data, 0, newArray, this.drawingData.length, data.length);
/*  97:181 */     this.drawingData = newArray;
/*  98:    */     
/*  99:    */ 
/* 100:184 */     this.initialized = false;
/* 101:    */   }
/* 102:    */   
/* 103:    */   final int getNumDrawings()
/* 104:    */   {
/* 105:194 */     return this.numDrawings;
/* 106:    */   }
/* 107:    */   
/* 108:    */   EscherContainer getSpContainer(int drawingNum)
/* 109:    */   {
/* 110:205 */     if (!this.initialized) {
/* 111:207 */       initialize();
/* 112:    */     }
/* 113:210 */     if (drawingNum + 1 >= this.spContainers.length) {
/* 114:212 */       throw new DrawingDataException();
/* 115:    */     }
/* 116:215 */     EscherContainer spContainer = 
/* 117:216 */       (EscherContainer)this.spContainers[(drawingNum + 1)];
/* 118:    */     
/* 119:218 */     Assert.verify(spContainer != null);
/* 120:    */     
/* 121:220 */     return spContainer;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public byte[] getData()
/* 125:    */   {
/* 126:230 */     return this.drawingData;
/* 127:    */   }
/* 128:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.DrawingData
 * JD-Core Version:    0.7.0.1
 */