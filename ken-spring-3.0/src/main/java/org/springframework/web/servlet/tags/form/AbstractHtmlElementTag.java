/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.jsp.JspException;
/*   6:    */ import javax.servlet.jsp.tagext.DynamicAttributes;
/*   7:    */ import org.springframework.util.CollectionUtils;
/*   8:    */ import org.springframework.util.ObjectUtils;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ import org.springframework.web.servlet.support.BindStatus;
/*  11:    */ 
/*  12:    */ public abstract class AbstractHtmlElementTag
/*  13:    */   extends AbstractDataBoundFormElementTag
/*  14:    */   implements DynamicAttributes
/*  15:    */ {
/*  16:    */   public static final String CLASS_ATTRIBUTE = "class";
/*  17:    */   public static final String STYLE_ATTRIBUTE = "style";
/*  18:    */   public static final String LANG_ATTRIBUTE = "lang";
/*  19:    */   public static final String TITLE_ATTRIBUTE = "title";
/*  20:    */   public static final String DIR_ATTRIBUTE = "dir";
/*  21:    */   public static final String TABINDEX_ATTRIBUTE = "tabindex";
/*  22:    */   public static final String ONCLICK_ATTRIBUTE = "onclick";
/*  23:    */   public static final String ONDBLCLICK_ATTRIBUTE = "ondblclick";
/*  24:    */   public static final String ONMOUSEDOWN_ATTRIBUTE = "onmousedown";
/*  25:    */   public static final String ONMOUSEUP_ATTRIBUTE = "onmouseup";
/*  26:    */   public static final String ONMOUSEOVER_ATTRIBUTE = "onmouseover";
/*  27:    */   public static final String ONMOUSEMOVE_ATTRIBUTE = "onmousemove";
/*  28:    */   public static final String ONMOUSEOUT_ATTRIBUTE = "onmouseout";
/*  29:    */   public static final String ONKEYPRESS_ATTRIBUTE = "onkeypress";
/*  30:    */   public static final String ONKEYUP_ATTRIBUTE = "onkeyup";
/*  31:    */   public static final String ONKEYDOWN_ATTRIBUTE = "onkeydown";
/*  32:    */   private String cssClass;
/*  33:    */   private String cssErrorClass;
/*  34:    */   private String cssStyle;
/*  35:    */   private String lang;
/*  36:    */   private String title;
/*  37:    */   private String dir;
/*  38:    */   private String tabindex;
/*  39:    */   private String onclick;
/*  40:    */   private String ondblclick;
/*  41:    */   private String onmousedown;
/*  42:    */   private String onmouseup;
/*  43:    */   private String onmouseover;
/*  44:    */   private String onmousemove;
/*  45:    */   private String onmouseout;
/*  46:    */   private String onkeypress;
/*  47:    */   private String onkeyup;
/*  48:    */   private String onkeydown;
/*  49:    */   private Map<String, Object> dynamicAttributes;
/*  50:    */   
/*  51:    */   public void setCssClass(String cssClass)
/*  52:    */   {
/*  53:119 */     this.cssClass = cssClass;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected String getCssClass()
/*  57:    */   {
/*  58:127 */     return this.cssClass;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setCssErrorClass(String cssErrorClass)
/*  62:    */   {
/*  63:135 */     this.cssErrorClass = cssErrorClass;
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected String getCssErrorClass()
/*  67:    */   {
/*  68:143 */     return this.cssErrorClass;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setCssStyle(String cssStyle)
/*  72:    */   {
/*  73:151 */     this.cssStyle = cssStyle;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected String getCssStyle()
/*  77:    */   {
/*  78:159 */     return this.cssStyle;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setLang(String lang)
/*  82:    */   {
/*  83:167 */     this.lang = lang;
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected String getLang()
/*  87:    */   {
/*  88:175 */     return this.lang;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setTitle(String title)
/*  92:    */   {
/*  93:183 */     this.title = title;
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected String getTitle()
/*  97:    */   {
/*  98:191 */     return this.title;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setDir(String dir)
/* 102:    */   {
/* 103:199 */     this.dir = dir;
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected String getDir()
/* 107:    */   {
/* 108:207 */     return this.dir;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setTabindex(String tabindex)
/* 112:    */   {
/* 113:215 */     this.tabindex = tabindex;
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected String getTabindex()
/* 117:    */   {
/* 118:223 */     return this.tabindex;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setOnclick(String onclick)
/* 122:    */   {
/* 123:231 */     this.onclick = onclick;
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected String getOnclick()
/* 127:    */   {
/* 128:239 */     return this.onclick;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setOndblclick(String ondblclick)
/* 132:    */   {
/* 133:247 */     this.ondblclick = ondblclick;
/* 134:    */   }
/* 135:    */   
/* 136:    */   protected String getOndblclick()
/* 137:    */   {
/* 138:255 */     return this.ondblclick;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setOnmousedown(String onmousedown)
/* 142:    */   {
/* 143:263 */     this.onmousedown = onmousedown;
/* 144:    */   }
/* 145:    */   
/* 146:    */   protected String getOnmousedown()
/* 147:    */   {
/* 148:271 */     return this.onmousedown;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setOnmouseup(String onmouseup)
/* 152:    */   {
/* 153:279 */     this.onmouseup = onmouseup;
/* 154:    */   }
/* 155:    */   
/* 156:    */   protected String getOnmouseup()
/* 157:    */   {
/* 158:287 */     return this.onmouseup;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void setOnmouseover(String onmouseover)
/* 162:    */   {
/* 163:295 */     this.onmouseover = onmouseover;
/* 164:    */   }
/* 165:    */   
/* 166:    */   protected String getOnmouseover()
/* 167:    */   {
/* 168:303 */     return this.onmouseover;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void setOnmousemove(String onmousemove)
/* 172:    */   {
/* 173:311 */     this.onmousemove = onmousemove;
/* 174:    */   }
/* 175:    */   
/* 176:    */   protected String getOnmousemove()
/* 177:    */   {
/* 178:319 */     return this.onmousemove;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public void setOnmouseout(String onmouseout)
/* 182:    */   {
/* 183:327 */     this.onmouseout = onmouseout;
/* 184:    */   }
/* 185:    */   
/* 186:    */   protected String getOnmouseout()
/* 187:    */   {
/* 188:334 */     return this.onmouseout;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public void setOnkeypress(String onkeypress)
/* 192:    */   {
/* 193:342 */     this.onkeypress = onkeypress;
/* 194:    */   }
/* 195:    */   
/* 196:    */   protected String getOnkeypress()
/* 197:    */   {
/* 198:350 */     return this.onkeypress;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void setOnkeyup(String onkeyup)
/* 202:    */   {
/* 203:358 */     this.onkeyup = onkeyup;
/* 204:    */   }
/* 205:    */   
/* 206:    */   protected String getOnkeyup()
/* 207:    */   {
/* 208:366 */     return this.onkeyup;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public void setOnkeydown(String onkeydown)
/* 212:    */   {
/* 213:374 */     this.onkeydown = onkeydown;
/* 214:    */   }
/* 215:    */   
/* 216:    */   protected String getOnkeydown()
/* 217:    */   {
/* 218:382 */     return this.onkeydown;
/* 219:    */   }
/* 220:    */   
/* 221:    */   protected Map<String, Object> getDynamicAttributes()
/* 222:    */   {
/* 223:389 */     return this.dynamicAttributes;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setDynamicAttribute(String uri, String localName, Object value)
/* 227:    */     throws JspException
/* 228:    */   {
/* 229:396 */     if (this.dynamicAttributes == null) {
/* 230:397 */       this.dynamicAttributes = new HashMap();
/* 231:    */     }
/* 232:399 */     this.dynamicAttributes.put(localName, value);
/* 233:    */   }
/* 234:    */   
/* 235:    */   protected void writeDefaultAttributes(TagWriter tagWriter)
/* 236:    */     throws JspException
/* 237:    */   {
/* 238:408 */     super.writeDefaultAttributes(tagWriter);
/* 239:409 */     writeOptionalAttributes(tagWriter);
/* 240:    */   }
/* 241:    */   
/* 242:    */   protected void writeOptionalAttributes(TagWriter tagWriter)
/* 243:    */     throws JspException
/* 244:    */   {
/* 245:418 */     tagWriter.writeOptionalAttributeValue("class", resolveCssClass());
/* 246:419 */     tagWriter.writeOptionalAttributeValue("style", 
/* 247:420 */       ObjectUtils.getDisplayString(evaluate("cssStyle", getCssStyle())));
/* 248:421 */     writeOptionalAttribute(tagWriter, "lang", getLang());
/* 249:422 */     writeOptionalAttribute(tagWriter, "title", getTitle());
/* 250:423 */     writeOptionalAttribute(tagWriter, "dir", getDir());
/* 251:424 */     writeOptionalAttribute(tagWriter, "tabindex", getTabindex());
/* 252:425 */     writeOptionalAttribute(tagWriter, "onclick", getOnclick());
/* 253:426 */     writeOptionalAttribute(tagWriter, "ondblclick", getOndblclick());
/* 254:427 */     writeOptionalAttribute(tagWriter, "onmousedown", getOnmousedown());
/* 255:428 */     writeOptionalAttribute(tagWriter, "onmouseup", getOnmouseup());
/* 256:429 */     writeOptionalAttribute(tagWriter, "onmouseover", getOnmouseover());
/* 257:430 */     writeOptionalAttribute(tagWriter, "onmousemove", getOnmousemove());
/* 258:431 */     writeOptionalAttribute(tagWriter, "onmouseout", getOnmouseout());
/* 259:432 */     writeOptionalAttribute(tagWriter, "onkeypress", getOnkeypress());
/* 260:433 */     writeOptionalAttribute(tagWriter, "onkeyup", getOnkeyup());
/* 261:434 */     writeOptionalAttribute(tagWriter, "onkeydown", getOnkeydown());
/* 262:436 */     if (!CollectionUtils.isEmpty(this.dynamicAttributes)) {
/* 263:437 */       for (String attr : this.dynamicAttributes.keySet()) {
/* 264:438 */         tagWriter.writeOptionalAttributeValue(attr, getDisplayString(this.dynamicAttributes.get(attr)));
/* 265:    */       }
/* 266:    */     }
/* 267:    */   }
/* 268:    */   
/* 269:    */   protected String resolveCssClass()
/* 270:    */     throws JspException
/* 271:    */   {
/* 272:448 */     if ((getBindStatus().isError()) && (StringUtils.hasText(getCssErrorClass()))) {
/* 273:449 */       return ObjectUtils.getDisplayString(evaluate("cssErrorClass", getCssErrorClass()));
/* 274:    */     }
/* 275:452 */     return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
/* 276:    */   }
/* 277:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
 * JD-Core Version:    0.7.0.1
 */