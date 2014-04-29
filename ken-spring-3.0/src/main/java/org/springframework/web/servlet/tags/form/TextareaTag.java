/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.JspException;
/*   4:    */ 
/*   5:    */ public class TextareaTag
/*   6:    */   extends AbstractHtmlInputElementTag
/*   7:    */ {
/*   8:    */   public static final String ROWS_ATTRIBUTE = "rows";
/*   9:    */   public static final String COLS_ATTRIBUTE = "cols";
/*  10:    */   public static final String ONSELECT_ATTRIBUTE = "onselect";
/*  11:    */   public static final String READONLY_ATTRIBUTE = "readonly";
/*  12:    */   private String rows;
/*  13:    */   private String cols;
/*  14:    */   private String onselect;
/*  15:    */   
/*  16:    */   public void setRows(String rows)
/*  17:    */   {
/*  18: 51 */     this.rows = rows;
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected String getRows()
/*  22:    */   {
/*  23: 58 */     return this.rows;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setCols(String cols)
/*  27:    */   {
/*  28: 66 */     this.cols = cols;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected String getCols()
/*  32:    */   {
/*  33: 73 */     return this.cols;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setOnselect(String onselect)
/*  37:    */   {
/*  38: 81 */     this.onselect = onselect;
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected String getOnselect()
/*  42:    */   {
/*  43: 88 */     return this.onselect;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected int writeTagContent(TagWriter tagWriter)
/*  47:    */     throws JspException
/*  48:    */   {
/*  49: 94 */     tagWriter.startTag("textarea");
/*  50: 95 */     writeDefaultAttributes(tagWriter);
/*  51: 96 */     writeOptionalAttribute(tagWriter, "rows", getRows());
/*  52: 97 */     writeOptionalAttribute(tagWriter, "cols", getCols());
/*  53: 98 */     writeOptionalAttribute(tagWriter, "onselect", getOnselect());
/*  54: 99 */     String value = getDisplayString(getBoundValue(), getPropertyEditor());
/*  55:100 */     tagWriter.appendValue(processFieldValue(getName(), value, "textarea"));
/*  56:101 */     tagWriter.endTag();
/*  57:102 */     return 0;
/*  58:    */   }
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.TextareaTag
 * JD-Core Version:    0.7.0.1
 */