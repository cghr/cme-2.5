/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import javax.servlet.ServletRequest;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.jsp.JspException;
/*   7:    */ import javax.servlet.jsp.PageContext;
/*   8:    */ import org.springframework.core.Conventions;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ import org.springframework.web.servlet.support.BindStatus;
/*  11:    */ import org.springframework.web.servlet.support.RequestContext;
/*  12:    */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*  13:    */ import org.springframework.web.servlet.tags.EditorAwareTag;
/*  14:    */ 
/*  15:    */ public abstract class AbstractDataBoundFormElementTag
/*  16:    */   extends AbstractFormTag
/*  17:    */   implements EditorAwareTag
/*  18:    */ {
/*  19:    */   protected static final String NESTED_PATH_VARIABLE_NAME = "nestedPath";
/*  20:    */   @Deprecated
/*  21: 61 */   public static final String COMMAND_NAME_VARIABLE_NAME = Conventions.getQualifiedAttributeName(AbstractFormTag.class, "commandName");
/*  22:    */   private String path;
/*  23:    */   private String id;
/*  24:    */   private BindStatus bindStatus;
/*  25:    */   
/*  26:    */   public void setPath(String path)
/*  27:    */   {
/*  28: 85 */     this.path = path;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected final String getPath()
/*  32:    */     throws JspException
/*  33:    */   {
/*  34: 93 */     String resolvedPath = (String)evaluate("path", this.path);
/*  35: 94 */     return resolvedPath != null ? resolvedPath : "";
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setId(String id)
/*  39:    */   {
/*  40:104 */     this.id = id;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getId()
/*  44:    */   {
/*  45:112 */     return this.id;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected void writeDefaultAttributes(TagWriter tagWriter)
/*  49:    */     throws JspException
/*  50:    */   {
/*  51:126 */     writeOptionalAttribute(tagWriter, "id", resolveId());
/*  52:127 */     writeOptionalAttribute(tagWriter, "name", getName());
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected String resolveId()
/*  56:    */     throws JspException
/*  57:    */   {
/*  58:137 */     Object id = evaluate("id", getId());
/*  59:138 */     if (id != null)
/*  60:    */     {
/*  61:139 */       String idString = id.toString();
/*  62:140 */       return StringUtils.hasText(idString) ? idString : null;
/*  63:    */     }
/*  64:142 */     return autogenerateId();
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected String autogenerateId()
/*  68:    */     throws JspException
/*  69:    */   {
/*  70:151 */     return StringUtils.deleteAny(getName(), "[]");
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected String getName()
/*  74:    */     throws JspException
/*  75:    */   {
/*  76:164 */     return getPropertyPath();
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected BindStatus getBindStatus()
/*  80:    */     throws JspException
/*  81:    */   {
/*  82:171 */     if (this.bindStatus == null)
/*  83:    */     {
/*  84:173 */       String nestedPath = getNestedPath();
/*  85:174 */       String pathToUse = nestedPath != null ? nestedPath + getPath() : getPath();
/*  86:175 */       if (pathToUse.endsWith(".")) {
/*  87:176 */         pathToUse = pathToUse.substring(0, pathToUse.length() - 1);
/*  88:    */       }
/*  89:178 */       this.bindStatus = new BindStatus(getRequestContext(), pathToUse, false);
/*  90:    */     }
/*  91:180 */     return this.bindStatus;
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected String getNestedPath()
/*  95:    */   {
/*  96:188 */     return (String)this.pageContext.getAttribute("nestedPath", 2);
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected String getPropertyPath()
/* 100:    */     throws JspException
/* 101:    */   {
/* 102:198 */     String expression = getBindStatus().getExpression();
/* 103:199 */     return expression != null ? expression : "";
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected final Object getBoundValue()
/* 107:    */     throws JspException
/* 108:    */   {
/* 109:207 */     return getBindStatus().getValue();
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected PropertyEditor getPropertyEditor()
/* 113:    */     throws JspException
/* 114:    */   {
/* 115:214 */     return getBindStatus().getEditor();
/* 116:    */   }
/* 117:    */   
/* 118:    */   public final PropertyEditor getEditor()
/* 119:    */     throws JspException
/* 120:    */   {
/* 121:222 */     return getPropertyEditor();
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected String convertToDisplayString(Object value)
/* 125:    */     throws JspException
/* 126:    */   {
/* 127:230 */     PropertyEditor editor = value != null ? getBindStatus().findEditor(value.getClass()) : null;
/* 128:231 */     return getDisplayString(value, editor);
/* 129:    */   }
/* 130:    */   
/* 131:    */   protected final String processFieldValue(String name, String value, String type)
/* 132:    */   {
/* 133:239 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 134:240 */     ServletRequest request = this.pageContext.getRequest();
/* 135:241 */     if ((processor != null) && ((request instanceof HttpServletRequest))) {
/* 136:242 */       value = processor.processFormFieldValue((HttpServletRequest)request, name, value, type);
/* 137:    */     }
/* 138:244 */     return value;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void doFinally()
/* 142:    */   {
/* 143:252 */     super.doFinally();
/* 144:253 */     this.bindStatus = null;
/* 145:    */   }
/* 146:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag
 * JD-Core Version:    0.7.0.1
 */