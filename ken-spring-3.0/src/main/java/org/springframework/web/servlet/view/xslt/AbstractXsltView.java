/*   1:    */ package org.springframework.web.servlet.view.xslt;
/*   2:    */ 
/*   3:    */ import java.io.BufferedOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.net.URL;
/*   6:    */ import java.util.Enumeration;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Properties;
/*  11:    */ import java.util.Set;
/*  12:    */ import javax.servlet.http.HttpServletRequest;
/*  13:    */ import javax.servlet.http.HttpServletResponse;
/*  14:    */ import javax.xml.transform.ErrorListener;
/*  15:    */ import javax.xml.transform.Result;
/*  16:    */ import javax.xml.transform.Source;
/*  17:    */ import javax.xml.transform.Templates;
/*  18:    */ import javax.xml.transform.Transformer;
/*  19:    */ import javax.xml.transform.TransformerConfigurationException;
/*  20:    */ import javax.xml.transform.TransformerException;
/*  21:    */ import javax.xml.transform.TransformerFactory;
/*  22:    */ import javax.xml.transform.TransformerFactoryConfigurationError;
/*  23:    */ import javax.xml.transform.URIResolver;
/*  24:    */ import javax.xml.transform.dom.DOMSource;
/*  25:    */ import javax.xml.transform.stream.StreamResult;
/*  26:    */ import javax.xml.transform.stream.StreamSource;
/*  27:    */ import org.apache.commons.logging.Log;
/*  28:    */ import org.springframework.context.ApplicationContextException;
/*  29:    */ import org.springframework.core.io.Resource;
/*  30:    */ import org.springframework.util.Assert;
/*  31:    */ import org.springframework.util.xml.SimpleTransformErrorListener;
/*  32:    */ import org.springframework.util.xml.TransformerUtils;
/*  33:    */ import org.springframework.web.servlet.view.AbstractView;
/*  34:    */ import org.springframework.web.util.NestedServletException;
/*  35:    */ import org.w3c.dom.Node;
/*  36:    */ 
/*  37:    */ @Deprecated
/*  38:    */ public abstract class AbstractXsltView
/*  39:    */   extends AbstractView
/*  40:    */ {
/*  41:    */   public static final String XML_CONTENT_TYPE = "text/xml;charset=ISO-8859-1";
/*  42:    */   public static final String DEFAULT_ROOT = "DocRoot";
/*  43: 94 */   private boolean customContentTypeSet = false;
/*  44:    */   private Class transformerFactoryClass;
/*  45:    */   private Resource stylesheetLocation;
/*  46:100 */   private String root = "DocRoot";
/*  47:102 */   private boolean useSingleModelNameAsRoot = true;
/*  48:    */   private URIResolver uriResolver;
/*  49:106 */   private ErrorListener errorListener = new SimpleTransformErrorListener(this.logger);
/*  50:108 */   private boolean indent = true;
/*  51:    */   private Properties outputProperties;
/*  52:112 */   private boolean cache = true;
/*  53:    */   private TransformerFactory transformerFactory;
/*  54:    */   private volatile Templates cachedTemplates;
/*  55:    */   
/*  56:    */   protected AbstractXsltView()
/*  57:    */   {
/*  58:127 */     super.setContentType("text/xml;charset=ISO-8859-1");
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setContentType(String contentType)
/*  62:    */   {
/*  63:133 */     super.setContentType(contentType);
/*  64:134 */     this.customContentTypeSet = true;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setTransformerFactoryClass(Class transformerFactoryClass)
/*  68:    */   {
/*  69:143 */     Assert.isAssignable(TransformerFactory.class, transformerFactoryClass);
/*  70:144 */     this.transformerFactoryClass = transformerFactoryClass;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setStylesheetLocation(Resource stylesheetLocation)
/*  74:    */   {
/*  75:157 */     this.stylesheetLocation = stylesheetLocation;
/*  76:    */     
/*  77:159 */     resetCachedTemplates();
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected Resource getStylesheetLocation()
/*  81:    */   {
/*  82:166 */     return this.stylesheetLocation;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setRoot(String root)
/*  86:    */   {
/*  87:176 */     this.root = root;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setUseSingleModelNameAsRoot(boolean useSingleModelNameAsRoot)
/*  91:    */   {
/*  92:192 */     this.useSingleModelNameAsRoot = useSingleModelNameAsRoot;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setUriResolver(URIResolver uriResolver)
/*  96:    */   {
/*  97:200 */     this.uriResolver = uriResolver;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setErrorListener(ErrorListener errorListener)
/* 101:    */   {
/* 102:213 */     this.errorListener = errorListener;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void setIndent(boolean indent)
/* 106:    */   {
/* 107:224 */     this.indent = indent;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setOutputProperties(Properties outputProperties)
/* 111:    */   {
/* 112:234 */     this.outputProperties = outputProperties;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void setCache(boolean cache)
/* 116:    */   {
/* 117:244 */     this.cache = cache;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public final void resetCachedTemplates()
/* 121:    */   {
/* 122:254 */     this.cachedTemplates = null;
/* 123:    */   }
/* 124:    */   
/* 125:    */   protected final void initApplicationContext()
/* 126:    */     throws ApplicationContextException
/* 127:    */   {
/* 128:264 */     this.transformerFactory = newTransformerFactory(this.transformerFactoryClass);
/* 129:265 */     this.transformerFactory.setErrorListener(this.errorListener);
/* 130:266 */     if (this.uriResolver != null) {
/* 131:267 */       this.transformerFactory.setURIResolver(this.uriResolver);
/* 132:    */     }
/* 133:269 */     if ((getStylesheetLocation() != null) && (!this.customContentTypeSet)) {
/* 134:272 */       super.setContentType("text/html;charset=ISO-8859-1");
/* 135:    */     }
/* 136:    */     try
/* 137:    */     {
/* 138:275 */       getTemplates();
/* 139:    */     }
/* 140:    */     catch (TransformerConfigurationException ex)
/* 141:    */     {
/* 142:278 */       throw new ApplicationContextException("Cannot load stylesheet for XSLT view '" + getBeanName() + "'", ex);
/* 143:    */     }
/* 144:    */   }
/* 145:    */   
/* 146:    */   protected TransformerFactory newTransformerFactory(Class transformerFactoryClass)
/* 147:    */   {
/* 148:297 */     if (transformerFactoryClass != null) {
/* 149:    */       try
/* 150:    */       {
/* 151:299 */         return (TransformerFactory)transformerFactoryClass.newInstance();
/* 152:    */       }
/* 153:    */       catch (Exception ex)
/* 154:    */       {
/* 155:302 */         throw new TransformerFactoryConfigurationError(ex, "Could not instantiate TransformerFactory");
/* 156:    */       }
/* 157:    */     }
/* 158:306 */     return TransformerFactory.newInstance();
/* 159:    */   }
/* 160:    */   
/* 161:    */   protected final TransformerFactory getTransformerFactory()
/* 162:    */   {
/* 163:315 */     return this.transformerFactory;
/* 164:    */   }
/* 165:    */   
/* 166:    */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 167:    */     throws Exception
/* 168:    */   {
/* 169:323 */     response.setContentType(getContentType());
/* 170:    */     
/* 171:325 */     Source source = null;
/* 172:326 */     String docRoot = null;
/* 173:    */     
/* 174:328 */     Object singleModel = null;
/* 175:330 */     if ((this.useSingleModelNameAsRoot) && (model.size() == 1))
/* 176:    */     {
/* 177:331 */       docRoot = (String)model.keySet().iterator().next();
/* 178:332 */       if (this.logger.isDebugEnabled()) {
/* 179:333 */         this.logger.debug("Single model object received, key [" + docRoot + "] will be used as root tag");
/* 180:    */       }
/* 181:335 */       singleModel = model.get(docRoot);
/* 182:    */     }
/* 183:339 */     if (((singleModel instanceof Node)) || ((singleModel instanceof Source)))
/* 184:    */     {
/* 185:343 */       this.logger.debug("No need to domify: was passed an XML Node or Source");
/* 186:344 */       source = (singleModel instanceof Node) ? new DOMSource((Node)singleModel) : (Source)singleModel;
/* 187:    */     }
/* 188:    */     else
/* 189:    */     {
/* 190:348 */       source = createXsltSource(model, docRoot != null ? docRoot : this.root, request, response);
/* 191:    */     }
/* 192:351 */     doTransform(model, source, request, response);
/* 193:    */   }
/* 194:    */   
/* 195:    */   protected Source createXsltSource(Map<String, Object> model, String root, HttpServletRequest request, HttpServletResponse response)
/* 196:    */     throws Exception
/* 197:    */   {
/* 198:374 */     return null;
/* 199:    */   }
/* 200:    */   
/* 201:    */   protected void doTransform(Map<String, Object> model, Source source, HttpServletRequest request, HttpServletResponse response)
/* 202:    */     throws Exception
/* 203:    */   {
/* 204:397 */     Map<String, Object> parameters = getParameters(model, request);
/* 205:398 */     Result result = useWriter() ? 
/* 206:399 */       new StreamResult(response.getWriter()) : 
/* 207:400 */       new StreamResult(new BufferedOutputStream(response.getOutputStream()));
/* 208:401 */     String encoding = response.getCharacterEncoding();
/* 209:402 */     doTransform(source, parameters, result, encoding);
/* 210:    */   }
/* 211:    */   
/* 212:    */   protected Map getParameters(Map<String, Object> model, HttpServletRequest request)
/* 213:    */   {
/* 214:417 */     return getParameters(request);
/* 215:    */   }
/* 216:    */   
/* 217:    */   protected Map getParameters(HttpServletRequest request)
/* 218:    */   {
/* 219:431 */     return null;
/* 220:    */   }
/* 221:    */   
/* 222:    */   protected boolean useWriter()
/* 223:    */   {
/* 224:446 */     return false;
/* 225:    */   }
/* 226:    */   
/* 227:    */   protected void doTransform(Source source, Map<String, Object> parameters, Result result, String encoding)
/* 228:    */     throws Exception
/* 229:    */   {
/* 230:    */     try
/* 231:    */     {
/* 232:463 */       Transformer trans = buildTransformer(parameters);
/* 233:466 */       if (this.uriResolver != null) {
/* 234:467 */         trans.setURIResolver(this.uriResolver);
/* 235:    */       }
/* 236:471 */       trans.setOutputProperty("encoding", encoding);
/* 237:472 */       if (this.indent) {
/* 238:473 */         TransformerUtils.enableIndenting(trans);
/* 239:    */       }
/* 240:477 */       if (this.outputProperties != null)
/* 241:    */       {
/* 242:478 */         Enumeration propsEnum = this.outputProperties.propertyNames();
/* 243:479 */         while (propsEnum.hasMoreElements())
/* 244:    */         {
/* 245:480 */           String propName = (String)propsEnum.nextElement();
/* 246:481 */           trans.setOutputProperty(propName, this.outputProperties.getProperty(propName));
/* 247:    */         }
/* 248:    */       }
/* 249:486 */       trans.transform(source, result);
/* 250:    */     }
/* 251:    */     catch (TransformerConfigurationException ex)
/* 252:    */     {
/* 253:489 */       throw new NestedServletException(
/* 254:490 */         "Couldn't create XSLT transformer in XSLT view with name [" + getBeanName() + "]", ex);
/* 255:    */     }
/* 256:    */     catch (TransformerException ex)
/* 257:    */     {
/* 258:493 */       throw new NestedServletException(
/* 259:494 */         "Couldn't perform transform in XSLT view with name [" + getBeanName() + "]", ex);
/* 260:    */     }
/* 261:    */   }
/* 262:    */   
/* 263:    */   protected Transformer buildTransformer(Map<String, Object> parameters)
/* 264:    */     throws TransformerConfigurationException
/* 265:    */   {
/* 266:508 */     Templates templates = getTemplates();
/* 267:509 */     Transformer transformer = 
/* 268:510 */       templates != null ? templates.newTransformer() : getTransformerFactory().newTransformer();
/* 269:511 */     applyTransformerParameters(parameters, transformer);
/* 270:512 */     return transformer;
/* 271:    */   }
/* 272:    */   
/* 273:    */   protected Templates getTemplates()
/* 274:    */     throws TransformerConfigurationException
/* 275:    */   {
/* 276:530 */     if (this.cachedTemplates != null) {
/* 277:531 */       return this.cachedTemplates;
/* 278:    */     }
/* 279:533 */     Resource location = getStylesheetLocation();
/* 280:534 */     if (location != null)
/* 281:    */     {
/* 282:535 */       Templates templates = getTransformerFactory().newTemplates(getStylesheetSource(location));
/* 283:536 */       if (this.cache) {
/* 284:537 */         this.cachedTemplates = templates;
/* 285:    */       }
/* 286:539 */       return templates;
/* 287:    */     }
/* 288:541 */     return null;
/* 289:    */   }
/* 290:    */   
/* 291:    */   protected void applyTransformerParameters(Map<String, Object> parameters, Transformer transformer)
/* 292:    */   {
/* 293:551 */     if (parameters != null) {
/* 294:552 */       for (Map.Entry<String, Object> entry : parameters.entrySet()) {
/* 295:553 */         transformer.setParameter((String)entry.getKey(), entry.getValue());
/* 296:    */       }
/* 297:    */     }
/* 298:    */   }
/* 299:    */   
/* 300:    */   protected Source getStylesheetSource(Resource stylesheetLocation)
/* 301:    */     throws ApplicationContextException
/* 302:    */   {
/* 303:565 */     if (this.logger.isDebugEnabled()) {
/* 304:566 */       this.logger.debug("Loading XSLT stylesheet from " + stylesheetLocation);
/* 305:    */     }
/* 306:    */     try
/* 307:    */     {
/* 308:569 */       URL url = stylesheetLocation.getURL();
/* 309:570 */       String urlPath = url.toString();
/* 310:571 */       String systemId = urlPath.substring(0, urlPath.lastIndexOf('/') + 1);
/* 311:572 */       return new StreamSource(url.openStream(), systemId);
/* 312:    */     }
/* 313:    */     catch (IOException ex)
/* 314:    */     {
/* 315:575 */       throw new ApplicationContextException("Can't load XSLT stylesheet from " + stylesheetLocation, ex);
/* 316:    */     }
/* 317:    */   }
/* 318:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.xslt.AbstractXsltView
 * JD-Core Version:    0.7.0.1
 */