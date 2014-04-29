/*   1:    */ package jxl.write;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import jxl.biff.drawing.Drawing;
/*   5:    */ import jxl.biff.drawing.Drawing.ImageAnchorProperties;
/*   6:    */ import jxl.biff.drawing.DrawingGroup;
/*   7:    */ import jxl.biff.drawing.DrawingGroupObject;
/*   8:    */ 
/*   9:    */ public class WritableImage
/*  10:    */   extends Drawing
/*  11:    */ {
/*  12: 44 */   public static Drawing.ImageAnchorProperties MOVE_AND_SIZE_WITH_CELLS = Drawing.MOVE_AND_SIZE_WITH_CELLS;
/*  13: 51 */   public static Drawing.ImageAnchorProperties MOVE_WITH_CELLS = Drawing.MOVE_WITH_CELLS;
/*  14: 58 */   public static Drawing.ImageAnchorProperties NO_MOVE_OR_SIZE_WITH_CELLS = Drawing.NO_MOVE_OR_SIZE_WITH_CELLS;
/*  15:    */   
/*  16:    */   public WritableImage(double x, double y, double width, double height, File image)
/*  17:    */   {
/*  18: 73 */     super(x, y, width, height, image);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public WritableImage(double x, double y, double width, double height, byte[] imageData)
/*  22:    */   {
/*  23: 91 */     super(x, y, width, height, imageData);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public WritableImage(DrawingGroupObject d, DrawingGroup dg)
/*  27:    */   {
/*  28:102 */     super(d, dg);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public double getColumn()
/*  32:    */   {
/*  33:112 */     return super.getX();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setColumn(double c)
/*  37:    */   {
/*  38:122 */     super.setX(c);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public double getRow()
/*  42:    */   {
/*  43:132 */     return super.getY();
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setRow(double c)
/*  47:    */   {
/*  48:142 */     super.setY(c);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double getWidth()
/*  52:    */   {
/*  53:152 */     return super.getWidth();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setWidth(double c)
/*  57:    */   {
/*  58:164 */     super.setWidth(c);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public double getHeight()
/*  62:    */   {
/*  63:174 */     return super.getHeight();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void setHeight(double c)
/*  67:    */   {
/*  68:186 */     super.setHeight(c);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public File getImageFile()
/*  72:    */   {
/*  73:196 */     return super.getImageFile();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public byte[] getImageData()
/*  77:    */   {
/*  78:206 */     return super.getImageData();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setImageAnchor(Drawing.ImageAnchorProperties iap)
/*  82:    */   {
/*  83:214 */     super.setImageAnchor(iap);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public Drawing.ImageAnchorProperties getImageAnchor()
/*  87:    */   {
/*  88:222 */     return super.getImageAnchor();
/*  89:    */   }
/*  90:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.WritableImage
 * JD-Core Version:    0.7.0.1
 */