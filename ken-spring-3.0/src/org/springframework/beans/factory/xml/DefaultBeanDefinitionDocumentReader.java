/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.net.URI;
/*   5:    */ import java.net.URISyntaxException;
/*   6:    */ import java.net.URL;
/*   7:    */ import java.util.LinkedHashSet;
/*   8:    */ import java.util.Set;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  12:    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*  13:    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*  14:    */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*  15:    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  16:    */ import org.springframework.core.env.Environment;
/*  17:    */ import org.springframework.core.io.Resource;
/*  18:    */ import org.springframework.core.io.support.ResourcePatternUtils;
/*  19:    */ import org.springframework.util.Assert;
/*  20:    */ import org.springframework.util.ResourceUtils;
/*  21:    */ import org.springframework.util.StringUtils;
/*  22:    */ import org.w3c.dom.Document;
/*  23:    */ import org.w3c.dom.Element;
/*  24:    */ import org.w3c.dom.Node;
/*  25:    */ import org.w3c.dom.NodeList;
/*  26:    */ 
/*  27:    */ public class DefaultBeanDefinitionDocumentReader
/*  28:    */   implements BeanDefinitionDocumentReader
/*  29:    */ {
/*  30:    */   public static final String BEAN_ELEMENT = "bean";
/*  31:    */   public static final String NESTED_BEANS_ELEMENT = "beans";
/*  32:    */   public static final String ALIAS_ELEMENT = "alias";
/*  33:    */   public static final String NAME_ATTRIBUTE = "name";
/*  34:    */   public static final String ALIAS_ATTRIBUTE = "alias";
/*  35:    */   public static final String IMPORT_ELEMENT = "import";
/*  36:    */   public static final String RESOURCE_ATTRIBUTE = "resource";
/*  37:    */   public static final String PROFILE_ATTRIBUTE = "profile";
/*  38: 79 */   protected final Log logger = LogFactory.getLog(getClass());
/*  39:    */   private XmlReaderContext readerContext;
/*  40:    */   private Environment environment;
/*  41:    */   private BeanDefinitionParserDelegate delegate;
/*  42:    */   
/*  43:    */   public void setEnvironment(Environment environment)
/*  44:    */   {
/*  45: 95 */     this.environment = environment;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
/*  49:    */   {
/*  50:106 */     this.readerContext = readerContext;
/*  51:    */     
/*  52:108 */     this.logger.debug("Loading bean definitions");
/*  53:109 */     Element root = doc.getDocumentElement();
/*  54:    */     
/*  55:111 */     doRegisterBeanDefinitions(root);
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected void doRegisterBeanDefinitions(Element root)
/*  59:    */   {
/*  60:121 */     String profileSpec = root.getAttribute("profile");
/*  61:122 */     if (StringUtils.hasText(profileSpec))
/*  62:    */     {
/*  63:123 */       Assert.state(this.environment != null, "environment property must not be null");
/*  64:124 */       String[] specifiedProfiles = StringUtils.tokenizeToStringArray(profileSpec, ",; ");
/*  65:125 */       if (!this.environment.acceptsProfiles(specifiedProfiles)) {
/*  66:126 */         return;
/*  67:    */       }
/*  68:    */     }
/*  69:136 */     BeanDefinitionParserDelegate parent = this.delegate;
/*  70:137 */     this.delegate = createHelper(this.readerContext, root, parent);
/*  71:    */     
/*  72:139 */     preProcessXml(root);
/*  73:140 */     parseBeanDefinitions(root, this.delegate);
/*  74:141 */     postProcessXml(root);
/*  75:    */     
/*  76:143 */     this.delegate = parent;
/*  77:    */   }
/*  78:    */   
/*  79:    */   protected BeanDefinitionParserDelegate createHelper(XmlReaderContext readerContext, Element root, BeanDefinitionParserDelegate parentDelegate)
/*  80:    */   {
/*  81:147 */     BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext, this.environment);
/*  82:148 */     delegate.initDefaults(root, parentDelegate);
/*  83:149 */     return delegate;
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected final XmlReaderContext getReaderContext()
/*  87:    */   {
/*  88:156 */     return this.readerContext;
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected Object extractSource(Element ele)
/*  92:    */   {
/*  93:164 */     return this.readerContext.extractSource(ele);
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate)
/*  97:    */   {
/*  98:174 */     if (delegate.isDefaultNamespace(root))
/*  99:    */     {
/* 100:175 */       NodeList nl = root.getChildNodes();
/* 101:176 */       for (int i = 0; i < nl.getLength(); i++)
/* 102:    */       {
/* 103:177 */         Node node = nl.item(i);
/* 104:178 */         if ((node instanceof Element))
/* 105:    */         {
/* 106:179 */           Element ele = (Element)node;
/* 107:180 */           if (delegate.isDefaultNamespace(ele)) {
/* 108:181 */             parseDefaultElement(ele, delegate);
/* 109:    */           } else {
/* 110:184 */             delegate.parseCustomElement(ele);
/* 111:    */           }
/* 112:    */         }
/* 113:    */       }
/* 114:    */     }
/* 115:    */     else
/* 116:    */     {
/* 117:190 */       delegate.parseCustomElement(root);
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate)
/* 122:    */   {
/* 123:195 */     if (delegate.nodeNameEquals(ele, "import")) {
/* 124:196 */       importBeanDefinitionResource(ele);
/* 125:198 */     } else if (delegate.nodeNameEquals(ele, "alias")) {
/* 126:199 */       processAliasRegistration(ele);
/* 127:201 */     } else if (delegate.nodeNameEquals(ele, "bean")) {
/* 128:202 */       processBeanDefinition(ele, delegate);
/* 129:204 */     } else if (delegate.nodeNameEquals(ele, "beans")) {
/* 130:206 */       doRegisterBeanDefinitions(ele);
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   protected void importBeanDefinitionResource(Element ele)
/* 135:    */   {
/* 136:215 */     String location = ele.getAttribute("resource");
/* 137:216 */     if (!StringUtils.hasText(location))
/* 138:    */     {
/* 139:217 */       getReaderContext().error("Resource location must not be empty", ele);
/* 140:218 */       return;
/* 141:    */     }
/* 142:222 */     location = this.environment.resolveRequiredPlaceholders(location);
/* 143:    */     
/* 144:224 */     Set<Resource> actualResources = new LinkedHashSet(4);
/* 145:    */     
/* 146:    */ 
/* 147:227 */     boolean absoluteLocation = false;
/* 148:    */     try
/* 149:    */     {
/* 150:229 */       absoluteLocation = (ResourcePatternUtils.isUrl(location)) || (ResourceUtils.toURI(location).isAbsolute());
/* 151:    */     }
/* 152:    */     catch (URISyntaxException localURISyntaxException) {}
/* 153:237 */     if (absoluteLocation) {
/* 154:    */       try
/* 155:    */       {
/* 156:239 */         int importCount = getReaderContext().getReader().loadBeanDefinitions(location, actualResources);
/* 157:240 */         if (!this.logger.isDebugEnabled()) {
/* 158:    */           break label389;
/* 159:    */         }
/* 160:241 */         this.logger.debug("Imported " + importCount + " bean definitions from URL location [" + location + "]");
/* 161:    */       }
/* 162:    */       catch (BeanDefinitionStoreException ex)
/* 163:    */       {
/* 164:245 */         getReaderContext().error(
/* 165:246 */           "Failed to import bean definitions from URL location [" + location + "]", ele, ex);
/* 166:    */       }
/* 167:    */     } else {
/* 168:    */       try
/* 169:    */       {
/* 170:253 */         Resource relativeResource = getReaderContext().getResource().createRelative(location);
/* 171:    */         int importCount;
/* 172:254 */         if (relativeResource.exists())
/* 173:    */         {
/* 174:255 */           int importCount = getReaderContext().getReader().loadBeanDefinitions(relativeResource);
/* 175:256 */           actualResources.add(relativeResource);
/* 176:    */         }
/* 177:    */         else
/* 178:    */         {
/* 179:259 */           String baseLocation = getReaderContext().getResource().getURL().toString();
/* 180:260 */           importCount = getReaderContext().getReader().loadBeanDefinitions(
/* 181:261 */             StringUtils.applyRelativePath(baseLocation, location), actualResources);
/* 182:    */         }
/* 183:263 */         if (this.logger.isDebugEnabled()) {
/* 184:264 */           this.logger.debug("Imported " + importCount + " bean definitions from relative location [" + location + "]");
/* 185:    */         }
/* 186:    */       }
/* 187:    */       catch (IOException ex)
/* 188:    */       {
/* 189:268 */         getReaderContext().error("Failed to resolve current resource location", ele, ex);
/* 190:    */       }
/* 191:    */       catch (BeanDefinitionStoreException ex)
/* 192:    */       {
/* 193:271 */         getReaderContext().error("Failed to import bean definitions from relative location [" + location + "]", 
/* 194:272 */           ele, ex);
/* 195:    */       }
/* 196:    */     }
/* 197:    */     label389:
/* 198:275 */     Resource[] actResArray = (Resource[])actualResources.toArray(new Resource[actualResources.size()]);
/* 199:276 */     getReaderContext().fireImportProcessed(location, actResArray, extractSource(ele));
/* 200:    */   }
/* 201:    */   
/* 202:    */   protected void processAliasRegistration(Element ele)
/* 203:    */   {
/* 204:283 */     String name = ele.getAttribute("name");
/* 205:284 */     String alias = ele.getAttribute("alias");
/* 206:285 */     boolean valid = true;
/* 207:286 */     if (!StringUtils.hasText(name))
/* 208:    */     {
/* 209:287 */       getReaderContext().error("Name must not be empty", ele);
/* 210:288 */       valid = false;
/* 211:    */     }
/* 212:290 */     if (!StringUtils.hasText(alias))
/* 213:    */     {
/* 214:291 */       getReaderContext().error("Alias must not be empty", ele);
/* 215:292 */       valid = false;
/* 216:    */     }
/* 217:294 */     if (valid)
/* 218:    */     {
/* 219:    */       try
/* 220:    */       {
/* 221:296 */         getReaderContext().getRegistry().registerAlias(name, alias);
/* 222:    */       }
/* 223:    */       catch (Exception ex)
/* 224:    */       {
/* 225:299 */         getReaderContext().error("Failed to register alias '" + alias + 
/* 226:300 */           "' for bean with name '" + name + "'", ele, ex);
/* 227:    */       }
/* 228:302 */       getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
/* 229:    */     }
/* 230:    */   }
/* 231:    */   
/* 232:    */   protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate)
/* 233:    */   {
/* 234:311 */     BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
/* 235:312 */     if (bdHolder != null)
/* 236:    */     {
/* 237:313 */       bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
/* 238:    */       try
/* 239:    */       {
/* 240:316 */         BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
/* 241:    */       }
/* 242:    */       catch (BeanDefinitionStoreException ex)
/* 243:    */       {
/* 244:319 */         getReaderContext().error("Failed to register bean definition with name '" + 
/* 245:320 */           bdHolder.getBeanName() + "'", ele, ex);
/* 246:    */       }
/* 247:323 */       getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
/* 248:    */     }
/* 249:    */   }
/* 250:    */   
/* 251:    */   protected void preProcessXml(Element root) {}
/* 252:    */   
/* 253:    */   protected void postProcessXml(Element root) {}
/* 254:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader
 * JD-Core Version:    0.7.0.1
 */