/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class Dgg
/*   8:    */   extends EscherAtom
/*   9:    */ {
/*  10: 36 */   private static Logger logger = Logger.getLogger(Dgg.class);
/*  11:    */   private byte[] data;
/*  12:    */   private int numClusters;
/*  13:    */   private int maxShapeId;
/*  14:    */   private int shapesSaved;
/*  15:    */   private int drawingsSaved;
/*  16:    */   private ArrayList clusters;
/*  17:    */   
/*  18:    */   static final class Cluster
/*  19:    */   {
/*  20:    */     int drawingGroupId;
/*  21:    */     int shapeIdsUsed;
/*  22:    */     
/*  23:    */     Cluster(int dgId, int sids)
/*  24:    */     {
/*  25: 91 */       this.drawingGroupId = dgId;
/*  26: 92 */       this.shapeIdsUsed = sids;
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Dgg(EscherRecordData erd)
/*  31:    */   {
/*  32:103 */     super(erd);
/*  33:104 */     this.clusters = new ArrayList();
/*  34:105 */     byte[] bytes = getBytes();
/*  35:106 */     this.maxShapeId = IntegerHelper.getInt(
/*  36:107 */       bytes[0], bytes[1], bytes[2], bytes[3]);
/*  37:108 */     this.numClusters = IntegerHelper.getInt(
/*  38:109 */       bytes[4], bytes[5], bytes[6], bytes[7]);
/*  39:110 */     this.shapesSaved = IntegerHelper.getInt(
/*  40:111 */       bytes[8], bytes[9], bytes[10], bytes[11]);
/*  41:112 */     this.drawingsSaved = IntegerHelper.getInt(
/*  42:113 */       bytes[12], bytes[13], bytes[14], bytes[15]);
/*  43:    */     
/*  44:115 */     int pos = 16;
/*  45:116 */     for (int i = 0; i < this.numClusters; i++)
/*  46:    */     {
/*  47:118 */       int dgId = IntegerHelper.getInt(bytes[pos], bytes[(pos + 1)]);
/*  48:119 */       int sids = IntegerHelper.getInt(bytes[(pos + 2)], bytes[(pos + 3)]);
/*  49:120 */       Cluster c = new Cluster(dgId, sids);
/*  50:121 */       this.clusters.add(c);
/*  51:122 */       pos += 4;
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public Dgg(int numShapes, int numDrawings)
/*  56:    */   {
/*  57:134 */     super(EscherRecordType.DGG);
/*  58:135 */     this.shapesSaved = numShapes;
/*  59:136 */     this.drawingsSaved = numDrawings;
/*  60:137 */     this.clusters = new ArrayList();
/*  61:    */   }
/*  62:    */   
/*  63:    */   void addCluster(int dgid, int sids)
/*  64:    */   {
/*  65:148 */     Cluster c = new Cluster(dgid, sids);
/*  66:149 */     this.clusters.add(c);
/*  67:    */   }
/*  68:    */   
/*  69:    */   byte[] getData()
/*  70:    */   {
/*  71:159 */     this.numClusters = this.clusters.size();
/*  72:160 */     this.data = new byte[16 + this.numClusters * 4];
/*  73:    */     
/*  74:    */ 
/*  75:163 */     IntegerHelper.getFourBytes(1024 + this.shapesSaved, this.data, 0);
/*  76:    */     
/*  77:    */ 
/*  78:166 */     IntegerHelper.getFourBytes(this.numClusters, this.data, 4);
/*  79:    */     
/*  80:    */ 
/*  81:169 */     IntegerHelper.getFourBytes(this.shapesSaved, this.data, 8);
/*  82:    */     
/*  83:    */ 
/*  84:    */ 
/*  85:173 */     IntegerHelper.getFourBytes(1, this.data, 12);
/*  86:    */     
/*  87:175 */     int pos = 16;
/*  88:176 */     for (int i = 0; i < this.numClusters; i++)
/*  89:    */     {
/*  90:178 */       Cluster c = (Cluster)this.clusters.get(i);
/*  91:179 */       IntegerHelper.getTwoBytes(c.drawingGroupId, this.data, pos);
/*  92:180 */       IntegerHelper.getTwoBytes(c.shapeIdsUsed, this.data, pos + 2);
/*  93:181 */       pos += 4;
/*  94:    */     }
/*  95:184 */     return setHeaderData(this.data);
/*  96:    */   }
/*  97:    */   
/*  98:    */   int getShapesSaved()
/*  99:    */   {
/* 100:194 */     return this.shapesSaved;
/* 101:    */   }
/* 102:    */   
/* 103:    */   int getDrawingsSaved()
/* 104:    */   {
/* 105:204 */     return this.drawingsSaved;
/* 106:    */   }
/* 107:    */   
/* 108:    */   Cluster getCluster(int i)
/* 109:    */   {
/* 110:215 */     return (Cluster)this.clusters.get(i);
/* 111:    */   }
/* 112:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Dgg
 * JD-Core Version:    0.7.0.1
 */