/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.Writer;
/*   5:    */ import java.util.Stack;
/*   6:    */ import javax.servlet.jsp.JspException;
/*   7:    */ import javax.servlet.jsp.PageContext;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ 
/*  11:    */ public class TagWriter
/*  12:    */ {
/*  13:    */   private final SafeWriter writer;
/*  14: 48 */   private final Stack tagState = new Stack();
/*  15:    */   
/*  16:    */   public TagWriter(PageContext pageContext)
/*  17:    */   {
/*  18: 57 */     Assert.notNull(pageContext, "PageContext must not be null");
/*  19: 58 */     this.writer = new SafeWriter(pageContext);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public TagWriter(Writer writer)
/*  23:    */   {
/*  24: 67 */     Assert.notNull(writer, "Writer must not be null");
/*  25: 68 */     this.writer = new SafeWriter(writer);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void startTag(String tagName)
/*  29:    */     throws JspException
/*  30:    */   {
/*  31: 78 */     if (inTag()) {
/*  32: 79 */       closeTagAndMarkAsBlock();
/*  33:    */     }
/*  34: 81 */     push(tagName);
/*  35: 82 */     this.writer.append("<").append(tagName);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void writeAttribute(String attributeName, String attributeValue)
/*  39:    */     throws JspException
/*  40:    */   {
/*  41: 92 */     if (currentState().isBlockTag()) {
/*  42: 93 */       throw new IllegalStateException("Cannot write attributes after opening tag is closed.");
/*  43:    */     }
/*  44: 96 */     this.writer.append(" ").append(attributeName).append("=\"").append(attributeValue).append("\"");
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void writeOptionalAttributeValue(String attributeName, String attributeValue)
/*  48:    */     throws JspException
/*  49:    */   {
/*  50:105 */     if (StringUtils.hasText(attributeValue)) {
/*  51:106 */       writeAttribute(attributeName, attributeValue);
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void appendValue(String value)
/*  56:    */     throws JspException
/*  57:    */   {
/*  58:116 */     if (!inTag()) {
/*  59:117 */       throw new IllegalStateException("Cannot write tag value. No open tag available.");
/*  60:    */     }
/*  61:119 */     closeTagAndMarkAsBlock();
/*  62:120 */     this.writer.append(value);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void forceBlock()
/*  66:    */     throws JspException
/*  67:    */   {
/*  68:131 */     if (currentState().isBlockTag()) {
/*  69:132 */       return;
/*  70:    */     }
/*  71:134 */     closeTagAndMarkAsBlock();
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void endTag()
/*  75:    */     throws JspException
/*  76:    */   {
/*  77:143 */     endTag(false);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void endTag(boolean enforceClosingTag)
/*  81:    */     throws JspException
/*  82:    */   {
/*  83:154 */     if (!inTag()) {
/*  84:155 */       throw new IllegalStateException("Cannot write end of tag. No open tag available.");
/*  85:    */     }
/*  86:157 */     boolean renderClosingTag = true;
/*  87:158 */     if (!currentState().isBlockTag()) {
/*  88:160 */       if (enforceClosingTag)
/*  89:    */       {
/*  90:161 */         this.writer.append(">");
/*  91:    */       }
/*  92:    */       else
/*  93:    */       {
/*  94:164 */         this.writer.append("/>");
/*  95:165 */         renderClosingTag = false;
/*  96:    */       }
/*  97:    */     }
/*  98:168 */     if (renderClosingTag) {
/*  99:169 */       this.writer.append("</").append(currentState().getTagName()).append(">");
/* 100:    */     }
/* 101:171 */     this.tagState.pop();
/* 102:    */   }
/* 103:    */   
/* 104:    */   private void push(String tagName)
/* 105:    */   {
/* 106:179 */     this.tagState.push(new TagStateEntry(tagName));
/* 107:    */   }
/* 108:    */   
/* 109:    */   private void closeTagAndMarkAsBlock()
/* 110:    */     throws JspException
/* 111:    */   {
/* 112:186 */     if (!currentState().isBlockTag())
/* 113:    */     {
/* 114:187 */       currentState().markAsBlockTag();
/* 115:188 */       this.writer.append(">");
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   private boolean inTag()
/* 120:    */   {
/* 121:193 */     return this.tagState.size() > 0;
/* 122:    */   }
/* 123:    */   
/* 124:    */   private TagStateEntry currentState()
/* 125:    */   {
/* 126:197 */     return (TagStateEntry)this.tagState.peek();
/* 127:    */   }
/* 128:    */   
/* 129:    */   private static class TagStateEntry
/* 130:    */   {
/* 131:    */     private final String tagName;
/* 132:    */     private boolean blockTag;
/* 133:    */     
/* 134:    */     public TagStateEntry(String tagName)
/* 135:    */     {
/* 136:211 */       this.tagName = tagName;
/* 137:    */     }
/* 138:    */     
/* 139:    */     public String getTagName()
/* 140:    */     {
/* 141:215 */       return this.tagName;
/* 142:    */     }
/* 143:    */     
/* 144:    */     public void markAsBlockTag()
/* 145:    */     {
/* 146:219 */       this.blockTag = true;
/* 147:    */     }
/* 148:    */     
/* 149:    */     public boolean isBlockTag()
/* 150:    */     {
/* 151:223 */       return this.blockTag;
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   private static final class SafeWriter
/* 156:    */   {
/* 157:    */     private PageContext pageContext;
/* 158:    */     private Writer writer;
/* 159:    */     
/* 160:    */     public SafeWriter(PageContext pageContext)
/* 161:    */     {
/* 162:239 */       this.pageContext = pageContext;
/* 163:    */     }
/* 164:    */     
/* 165:    */     public SafeWriter(Writer writer)
/* 166:    */     {
/* 167:243 */       this.writer = writer;
/* 168:    */     }
/* 169:    */     
/* 170:    */     public SafeWriter append(String value)
/* 171:    */       throws JspException
/* 172:    */     {
/* 173:    */       try
/* 174:    */       {
/* 175:248 */         getWriterToUse().write(String.valueOf(value));
/* 176:249 */         return this;
/* 177:    */       }
/* 178:    */       catch (IOException ex)
/* 179:    */       {
/* 180:252 */         throw new JspException("Unable to write to JspWriter", ex);
/* 181:    */       }
/* 182:    */     }
/* 183:    */     
/* 184:    */     private Writer getWriterToUse()
/* 185:    */     {
/* 186:257 */       return this.pageContext != null ? this.pageContext.getOut() : this.writer;
/* 187:    */     }
/* 188:    */   }
/* 189:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.TagWriter
 * JD-Core Version:    0.7.0.1
 */