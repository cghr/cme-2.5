/*   1:    */ package org.springframework.web.servlet.view.xslt;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.Reader;
/*   6:    */ import java.net.URI;
/*   7:    */ import java.util.Enumeration;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Properties;
/*  11:    */ import javax.servlet.http.HttpServletRequest;
/*  12:    */ import javax.servlet.http.HttpServletResponse;
/*  13:    */ import javax.xml.transform.ErrorListener;
/*  14:    */ import javax.xml.transform.Result;
/*  15:    */ import javax.xml.transform.Source;
/*  16:    */ import javax.xml.transform.Templates;
/*  17:    */ import javax.xml.transform.Transformer;
/*  18:    */ import javax.xml.transform.TransformerConfigurationException;
/*  19:    */ import javax.xml.transform.TransformerFactory;
/*  20:    */ import javax.xml.transform.TransformerFactoryConfigurationError;
/*  21:    */ import javax.xml.transform.URIResolver;
/*  22:    */ import javax.xml.transform.dom.DOMSource;
/*  23:    */ import javax.xml.transform.stream.StreamResult;
/*  24:    */ import javax.xml.transform.stream.StreamSource;
/*  25:    */ import org.apache.commons.logging.Log;
/*  26:    */ import org.springframework.beans.BeansException;
/*  27:    */ import org.springframework.context.ApplicationContext;
/*  28:    */ import org.springframework.context.ApplicationContextException;
/*  29:    */ import org.springframework.core.io.Resource;
/*  30:    */ import org.springframework.util.Assert;
/*  31:    */ import org.springframework.util.CollectionUtils;
/*  32:    */ import org.springframework.util.StringUtils;
/*  33:    */ import org.springframework.util.xml.SimpleTransformErrorListener;
/*  34:    */ import org.springframework.util.xml.TransformerUtils;
/*  35:    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*  36:    */ import org.w3c.dom.Document;
/*  37:    */ import org.w3c.dom.Node;
/*  38:    */ 
/*  39:    */ public class XsltView
/*  40:    */   extends AbstractUrlBasedView
/*  41:    */ {
/*  42:    */   private Class transformerFactoryClass;
/*  43:    */   private String sourceKey;
/*  44:    */   private URIResolver uriResolver;
/*  45: 82 */   private ErrorListener errorListener = new SimpleTransformErrorListener(this.logger);
/*  46: 84 */   private boolean indent = true;
/*  47:    */   private Properties outputProperties;
/*  48: 88 */   private boolean cacheTemplates = true;
/*  49:    */   private TransformerFactory transformerFactory;
/*  50:    */   private Templates cachedTemplates;
/*  51:    */   
/*  52:    */   public void setTransformerFactoryClass(Class transformerFactoryClass)
/*  53:    */   {
/*  54:101 */     Assert.isAssignable(TransformerFactory.class, transformerFactoryClass);
/*  55:102 */     this.transformerFactoryClass = transformerFactoryClass;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setSourceKey(String sourceKey)
/*  59:    */   {
/*  60:115 */     this.sourceKey = sourceKey;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setUriResolver(URIResolver uriResolver)
/*  64:    */   {
/*  65:123 */     this.uriResolver = uriResolver;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setErrorListener(ErrorListener errorListener)
/*  69:    */   {
/*  70:136 */     this.errorListener = (errorListener != null ? errorListener : new SimpleTransformErrorListener(this.logger));
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setIndent(boolean indent)
/*  74:    */   {
/*  75:147 */     this.indent = indent;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setOutputProperties(Properties outputProperties)
/*  79:    */   {
/*  80:157 */     this.outputProperties = outputProperties;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setCacheTemplates(boolean cacheTemplates)
/*  84:    */   {
/*  85:166 */     this.cacheTemplates = cacheTemplates;
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected void initApplicationContext()
/*  89:    */     throws BeansException
/*  90:    */   {
/*  91:175 */     this.transformerFactory = newTransformerFactory(this.transformerFactoryClass);
/*  92:176 */     this.transformerFactory.setErrorListener(this.errorListener);
/*  93:177 */     if (this.uriResolver != null) {
/*  94:178 */       this.transformerFactory.setURIResolver(this.uriResolver);
/*  95:    */     }
/*  96:180 */     if (this.cacheTemplates) {
/*  97:181 */       this.cachedTemplates = loadTemplates();
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected TransformerFactory newTransformerFactory(Class transformerFactoryClass)
/* 102:    */   {
/* 103:199 */     if (transformerFactoryClass != null) {
/* 104:    */       try
/* 105:    */       {
/* 106:201 */         return (TransformerFactory)transformerFactoryClass.newInstance();
/* 107:    */       }
/* 108:    */       catch (Exception ex)
/* 109:    */       {
/* 110:204 */         throw new TransformerFactoryConfigurationError(ex, "Could not instantiate TransformerFactory");
/* 111:    */       }
/* 112:    */     }
/* 113:208 */     return TransformerFactory.newInstance();
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected final TransformerFactory getTransformerFactory()
/* 117:    */   {
/* 118:217 */     return this.transformerFactory;
/* 119:    */   }
/* 120:    */   
/* 121:    */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 122:    */     throws Exception
/* 123:    */   {
/* 124:226 */     Templates templates = this.cachedTemplates;
/* 125:227 */     if (templates == null) {
/* 126:228 */       templates = loadTemplates();
/* 127:    */     }
/* 128:231 */     Transformer transformer = createTransformer(templates);
/* 129:232 */     configureTransformer(model, response, transformer);
/* 130:233 */     configureResponse(model, response, transformer);
/* 131:234 */     Source source = null;
/* 132:    */     try
/* 133:    */     {
/* 134:236 */       source = locateSource(model);
/* 135:237 */       if (source == null) {
/* 136:238 */         throw new IllegalArgumentException("Unable to locate Source object in model: " + model);
/* 137:    */       }
/* 138:240 */       transformer.transform(source, createResult(response));
/* 139:    */     }
/* 140:    */     finally
/* 141:    */     {
/* 142:243 */       closeSourceIfNecessary(source);
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   protected Result createResult(HttpServletResponse response)
/* 147:    */     throws Exception
/* 148:    */   {
/* 149:256 */     return new StreamResult(response.getOutputStream());
/* 150:    */   }
/* 151:    */   
/* 152:    */   protected Source locateSource(Map<String, Object> model)
/* 153:    */     throws Exception
/* 154:    */   {
/* 155:272 */     if (this.sourceKey != null) {
/* 156:273 */       return convertSource(model.get(this.sourceKey));
/* 157:    */     }
/* 158:275 */     Object source = CollectionUtils.findValueOfType(model.values(), getSourceTypes());
/* 159:276 */     return source != null ? convertSource(source) : null;
/* 160:    */   }
/* 161:    */   
/* 162:    */   protected Class[] getSourceTypes()
/* 163:    */   {
/* 164:287 */     return new Class[] { Source.class, Document.class, Node.class, Reader.class, InputStream.class, Resource.class };
/* 165:    */   }
/* 166:    */   
/* 167:    */   protected Source convertSource(Object source)
/* 168:    */     throws Exception
/* 169:    */   {
/* 170:298 */     if ((source instanceof Source)) {
/* 171:299 */       return (Source)source;
/* 172:    */     }
/* 173:301 */     if ((source instanceof Document)) {
/* 174:302 */       return new DOMSource(((Document)source).getDocumentElement());
/* 175:    */     }
/* 176:304 */     if ((source instanceof Node)) {
/* 177:305 */       return new DOMSource((Node)source);
/* 178:    */     }
/* 179:307 */     if ((source instanceof Reader)) {
/* 180:308 */       return new StreamSource((Reader)source);
/* 181:    */     }
/* 182:310 */     if ((source instanceof InputStream)) {
/* 183:311 */       return new StreamSource((InputStream)source);
/* 184:    */     }
/* 185:313 */     if ((source instanceof Resource))
/* 186:    */     {
/* 187:314 */       Resource resource = (Resource)source;
/* 188:315 */       return new StreamSource(resource.getInputStream(), resource.getURI().toASCIIString());
/* 189:    */     }
/* 190:318 */     throw new IllegalArgumentException("Value '" + source + "' cannot be converted to XSLT Source");
/* 191:    */   }
/* 192:    */   
/* 193:    */   protected void configureTransformer(Map<String, Object> model, HttpServletResponse response, Transformer transformer)
/* 194:    */   {
/* 195:337 */     copyModelParameters(model, transformer);
/* 196:338 */     copyOutputProperties(transformer);
/* 197:339 */     configureIndentation(transformer);
/* 198:    */   }
/* 199:    */   
/* 200:    */   protected final void configureIndentation(Transformer transformer)
/* 201:    */   {
/* 202:349 */     if (this.indent) {
/* 203:350 */       TransformerUtils.enableIndenting(transformer);
/* 204:    */     } else {
/* 205:353 */       TransformerUtils.disableIndenting(transformer);
/* 206:    */     }
/* 207:    */   }
/* 208:    */   
/* 209:    */   protected final void copyOutputProperties(Transformer transformer)
/* 210:    */   {
/* 211:364 */     if (this.outputProperties != null)
/* 212:    */     {
/* 213:365 */       Enumeration en = this.outputProperties.propertyNames();
/* 214:366 */       while (en.hasMoreElements())
/* 215:    */       {
/* 216:367 */         String name = (String)en.nextElement();
/* 217:368 */         transformer.setOutputProperty(name, this.outputProperties.getProperty(name));
/* 218:    */       }
/* 219:    */     }
/* 220:    */   }
/* 221:    */   
/* 222:    */   protected final void copyModelParameters(Map<String, Object> model, Transformer transformer)
/* 223:    */   {
/* 224:381 */     for (Map.Entry<String, Object> entry : model.entrySet()) {
/* 225:382 */       transformer.setParameter((String)entry.getKey(), entry.getValue());
/* 226:    */     }
/* 227:    */   }
/* 228:    */   
/* 229:    */   protected void configureResponse(Map<String, Object> model, HttpServletResponse response, Transformer transformer)
/* 230:    */   {
/* 231:398 */     String contentType = getContentType();
/* 232:399 */     String mediaType = transformer.getOutputProperty("media-type");
/* 233:400 */     String encoding = transformer.getOutputProperty("encoding");
/* 234:401 */     if (StringUtils.hasText(mediaType)) {
/* 235:402 */       contentType = mediaType;
/* 236:    */     }
/* 237:404 */     if (StringUtils.hasText(encoding)) {
/* 238:406 */       if ((contentType != null) && (!contentType.toLowerCase().contains(";charset="))) {
/* 239:407 */         contentType = contentType + ";charset=" + encoding;
/* 240:    */       }
/* 241:    */     }
/* 242:410 */     response.setContentType(contentType);
/* 243:    */   }
/* 244:    */   
/* 245:    */   private Templates loadTemplates()
/* 246:    */     throws ApplicationContextException
/* 247:    */   {
/* 248:417 */     Source stylesheetSource = getStylesheetSource();
/* 249:    */     try
/* 250:    */     {
/* 251:419 */       Templates templates = this.transformerFactory.newTemplates(stylesheetSource);
/* 252:420 */       if (this.logger.isDebugEnabled()) {
/* 253:421 */         this.logger.debug("Loading templates '" + templates + "'");
/* 254:    */       }
/* 255:423 */       return templates;
/* 256:    */     }
/* 257:    */     catch (TransformerConfigurationException ex)
/* 258:    */     {
/* 259:426 */       throw new ApplicationContextException("Can't load stylesheet from '" + getUrl() + "'", ex);
/* 260:    */     }
/* 261:    */     finally
/* 262:    */     {
/* 263:429 */       closeSourceIfNecessary(stylesheetSource);
/* 264:    */     }
/* 265:    */   }
/* 266:    */   
/* 267:    */   protected Transformer createTransformer(Templates templates)
/* 268:    */     throws TransformerConfigurationException
/* 269:    */   {
/* 270:442 */     Transformer transformer = templates.newTransformer();
/* 271:443 */     if (this.uriResolver != null) {
/* 272:444 */       transformer.setURIResolver(this.uriResolver);
/* 273:    */     }
/* 274:446 */     return transformer;
/* 275:    */   }
/* 276:    */   
/* 277:    */   protected Source getStylesheetSource()
/* 278:    */   {
/* 279:454 */     String url = getUrl();
/* 280:455 */     if (this.logger.isDebugEnabled()) {
/* 281:456 */       this.logger.debug("Loading XSLT stylesheet from '" + url + "'");
/* 282:    */     }
/* 283:    */     try
/* 284:    */     {
/* 285:459 */       Resource resource = getApplicationContext().getResource(url);
/* 286:460 */       return new StreamSource(resource.getInputStream(), resource.getURI().toASCIIString());
/* 287:    */     }
/* 288:    */     catch (IOException ex)
/* 289:    */     {
/* 290:463 */       throw new ApplicationContextException("Can't load XSLT stylesheet from '" + url + "'", ex);
/* 291:    */     }
/* 292:    */   }
/* 293:    */   
/* 294:    */   private void closeSourceIfNecessary(Source source)
/* 295:    */   {
/* 296:473 */     if ((source instanceof StreamSource))
/* 297:    */     {
/* 298:474 */       StreamSource streamSource = (StreamSource)source;
/* 299:475 */       if (streamSource.getReader() != null) {
/* 300:    */         try
/* 301:    */         {
/* 302:477 */           streamSource.getReader().close();
/* 303:    */         }
/* 304:    */         catch (IOException localIOException1) {}
/* 305:    */       }
/* 306:483 */       if (streamSource.getInputStream() != null) {
/* 307:    */         try
/* 308:    */         {
/* 309:485 */           streamSource.getInputStream().close();
/* 310:    */         }
/* 311:    */         catch (IOException localIOException2) {}
/* 312:    */       }
/* 313:    */     }
/* 314:    */   }
/* 315:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.xslt.XsltView
 * JD-Core Version:    0.7.0.1
 */