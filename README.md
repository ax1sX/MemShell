# MemShell

A List of Memory Shell related component relationships for some middleware
```
Middleware: Tomcat、Weblogic、Jetty、JBoss、Glassfish、Resin、TongWeb
Framework: Spring
MemShell: Listener、Filter、Servlet、Agent

Version:
Tomcat: 8.5.61
Weblogic: 12.2.1.3.0
Resin: 4.0.65
Jetty: 9.4.44.v20210927
JBoss: 6.1.0.Final
Glassfish: 4.1.11
TongWeb: 6.1
```

## (1) Tomcat
The code structure
```
org.apache.catalina.core.StandardContext
  (Listener)
  ——applicationListeners("com.test.MyListener")
  ——applicationEventListenersList(MyListener@2995)
  (Filter)
  ——filterConfigs("MyFilter"->{ApplicationFilterConfig@3869}"ApplicationFilterConfig[name=MyFilter,filterClass=com.test.MyFilter]")
    ——"MyFilter"
      ——value
        ——filterDef->{FilterDef@3883}"FilterDef[filterName=MyFilter,filiterClass=com.test.MyFilter]"
  ——filterDefs("MyFilter"->{FilterDef@3877}"FilterDef[filterName=Myfilter,filterClass=com.test.MyFilter]")
  ——filterMaps
    ——array(Tomcat 7,8,9; no array in Tomcat 6)
      ——0("FilterMap[filterName=MyFilter,urlPattern=/mem]")
  (Servlet)
  ——servletMappings
    ——"/my"->"MyServlet"
  ——children
    ——"MyServlet"->{StandardWrapper@3903}"StandardEngine[Catalina].StandardHost[localhost].StandardContext[/xxx]"
```
Differences between versions
```
// Filter
// Tomcat 8
org.apache.tomcat.util.descriptor.web.FilterMap
org.apache.tomcat.util.descriptor.web.FilterDef
// Tomcat 7
org.apache.catalina.deploy.FilterMap
org.apache.catalina.deploy.FilterDef
```
The way to get Context
```
ThreadGroup.threads ->
  http-nio-8088-Acceptor-0 ->
    NioEndpoint$Acceptor ->
      NioEndpoint ->
        AbstractProtocol$ConnectionHandler ->
          Http11NioProtocol ->
            CoyoteAdapter ->
              Connector ->
                StandardService ->
                  StandardEngine ->
                    StandardHost ->
                      StandardContext
```

## (2) Weblogic
The code structure
```
WebAppServletContext
  (Listener)
  ——eventsManager
    ——requestListeners(type of ServletRequestListener)
    ——ctxListeners(type of WebLoigcServletContextListener)
  (Filter)
  ——filterManager
    ——filters
      ——key="MyFilter"
      ——value={FilterWrapper@24882}
        ——filterName="MyFilter"
        ——filterClassName="com.test.MyFilter"
        ——filter={MyFilter@24899}
    ——filterPatternList
       ——0
        ——filterName="MyFilter"
        ——map={ServletMapping@24888}
          ——matchMap
            ——"/mem"
  (Servlet)
  ——servletMapping
    ——matchMap
      ——"/test"->{StandardURLMapping$FullMatchNode@18313}
        ——key="/test"
        ——value={StandardURLMapping$FullMatchNode@26097}
          ——exactValue={URLMatchHelper@26114}
            ——pattern="/test"
            ——servletStub={ServletStubImpl}
    ——extensionMap
  ——servletStubs
    ——"MyServlet"->{ServletStubImpl@18288}
      ——key="MyServlet"
      ——value={ServletStubImpl@26064}
        ——name="MyServlet"
        ——className="com.test.MyServlet"
  ——classLoader
    ——cachedClasses
      ——"com.test.MyServlet"
      ——"com.test.MyFilter"
      ——"com.test.MyListener"
        ——key="com.test.MyListener"
        ——value={Class@17035}
          ——name="com.test.MyListener"
          ——classLoader
```
The way to get Context
```
ThreadGroup.threads ->
  ExecuteThread.workEntry ->
    ContainerSupportProviderImpl$WlsRequestExecutor ->
      HttpConnectionHandler ->
        ServletRequestImpl ->
          WebAppServletContext


Thread(weblogic.timers.internal.TimerThread) ->
  TimerManagerImpl ->
    ServerTimeImpl ->
      AsyncContextTimer ->
        WebAppServletContext
        
some threads:
weblogic.server.channels.ServerListenThread
weblogic.work.ExecuteThread
weblogic.kernel.ServerExecuteThread
weblogic.timers.internal.TimerThread$Thread
com.oracle.common.internal.util.TimerThread
com.octetstring.vde.DoSManager
com.octetstring.vde.backend.standard.TransactionProcessor
...
```

## (3) Resin
The code structure
```
com.caucho.server.webapp.WebApp
  (Listener)
  ——_listeners
    ——0
      ——_listenerClass={Class@3501}
        ——name="com.test.MyListener"
      ——_object={MyListener@3557}  
  (Filter)
  ——_filterManager
    ——_filters
      ——"MyFilter"
        ——key="MyFilter"
        ——vlaue={FilterConfigImpl}
          ——_filterName="MyFilter"
          ——_filterClassName="com.test.MyFilter"
          ——_filterClass={Class@3480}"com.test.MyFilter"
          ——_filterManager
  ——_filterMapper
    ——_filterMap
      ——0
        ——_urlPattern="/mem"
        ——_filterName="MyFilter"
  (Servlet)
  ——_servletManager
    ——_servlets
      ——"MyServlet"(ServletConfigImpl@5758)
  ——_servletMapper
    ——_urlPatterns
      ——"MyServlet"
        ——key="MyServlet"
        ——value={HashSet@5800}
          ——0="/test"
```
Differences between versions
```
// filter
// Resin 4
_webApp.getClass().getDeclaredMethod("addFilter",FilterConfigImpl.class)
_webApp.getClass().getDeclaredField("_filterMapper")
_webApp.getClass().getDeclaredField("_loginFilterMapper")

// Resin 3
_webApp.getClass().getSuperClass().getDeclaredMethod("addFilter",FilterConfigImpl.class)
_webApp.getClass().getSuperClass().getDeclaredField("_loginFilterMapper")

//Listener
// Resin 4
com.caucho.server.webapp.ListenerConfig
_webApp.getClass().getDeclaredMethod("addListener",ListenerConfig.class)

// Resin 3
com.caucho.server.webapp.Listener
_webApp.getClass().getSuperClass().getDeclaredMethod("addListener",Listener.class)
```
The way to get Context
```
com.caucho.server.dispatch.ServletInvocation ->
  HttpServletRequestImpl ->
    WebApp
```


## (4) Jetty
The code structure
```
org.eclipse.jetty.webapp.WebAppContext
  (Listener)
  ——_eventListeners(WebAppContext->ServletContextHandler->ContextHandler)
    ——0={IntrospectorCleaner@3941}
    ——1={MyListener@3341}
  ——_servletRequestListeners
    ——0={MyListener@3341}
  ——_servletHandler
    (Filter)
    ——_filters
      ——0={FilterHolder@3970}
        ——_filter={MyFilter@3974}
        ——_name="MyFilter"
        ——_class={Class@3152}"class com.test.MyFilter"
        ——_className="com.test.MyFilter"
    ——_filterMappings
      ——0={FilterMapping@3987}"[/filter]/[]/[REQUEST]=>MyFilter"
         ——_filterName="MyFilter"
         ——_holder={FilterHolder@3970} "MyFilter==test.MyFilter@7f2f9ebc{inst=true,async=false,src=DESCRIPTOR:file:///xxx/WEB-INF/web.xml}"
         ——_pathSpecs={String[1]@3995}["/filter"]
    ——_filterNameMap
      ——"MyFilter" -> {FilterHolder@3970}
        ——key="MyFilter"
        ——value={FilterHolder@3970}
    (Servlet)
    ——_servlets
      ——0={ServletHolder@4073}
        ——_name="MyServlet"
        ——_class={Class@3152}"class com.test.MyServlet"
        ——_className="com.test.MyServlet"
    ——_servletMappings
      ——0={ServletMapping@4085}[/servlet]=>MyServlet
        ——_servletname="MyServlet"
        ——_pathSpecs={String[1]@3995}["/servlet"]
```
The way to get Context
```
java.lang.ThreadLocal$ThreadLocalMap$Entry ->
  org.eclipse.jetty.webapp.WebAppContext$Context ->
    WebAppContext
```

## (5) JBoss
The code structure（Similar to Tomcat）
```
org.apache.catalina.core.StandardContext
  (Listener)
  ——applicationListeners("com.test.MyListener")
  ——applicationEventListenersInstances(Object[1]@12599)
    ——0={MyListener@11482}
  ——listenersInstances(Object[1]@12599)
    ——0={MyListener@11482}
  (Filter)
  ——filterConfigs("MyFilter"->{ApplicationFilterConfig@3869}"ApplicationFilterConfig[name=MyFilter,filterClass=com.test.MyFilter]")
    ——"MyFilter"
      ——value
        ——filterDef->{FilterDef@3883}"FilterDef[filterName=MyFilter,filiterClass=com.test.MyFilter]"
        ——filter={MyFilter@12685}
  ——filterDefs("MyFilter"->{FilterDef@3877}"FilterDef[filterName=Myfilter,filterClass=com.test.MyFilter]")
  ——filterMaps
    ——0={FilterMap@12703}"FilterMap[filterName=MyFilter,urlPattern=/filter]"
  (Servlet)
  ——servletMappings
    ——"/my"->"MyServlet"
  ——children
    ——"MyServlet"->{StandardWrapper@3903}"StandardEngine[Catalina].StandardHost[localhost].StandardContext[/xxx]"
```

Differences between versions
```
// Filter
FilterMap: addURLPattern in JBoss 6; setURLPattern in JBoss 4
```
The way to get Context
```
ThreadGroup.threads ->
  http-8088-Acceptor-0 ->
    JioEndpoint$Acceptor ->
      JioEndpoint ->
        Http11NioProtocol$Http11ConnectionHandler ->
          RequestGroupInfo ->
            Request ->
              StandardContext
```

## (6) Glassfish
The code structure（Similar to Tomcat）
```
com.sun.enterprise.web.WebModule(.Superclass.Superclass-> org.apache.catalina.core.StandardContext)
  (Listener)
  ——eventListeners(ArrayList@8376)
    ——0={MyListener@7079}
  (Filter)
  ——filterConfigs("MyFilter"->{ApplicationFilterConfig@3869}"ApplicationFilterConfig[name=MyFilter,filterClass=com.test.MyFilter]")
    ——"MyFilter"
      ——key="MyFilter"
      ——value
        ——filterDef->{FilterDef@3883}"FilterDef[filterName=MyFilter,filiterClass=com.test.MyFilter]"
        ——filter={MyFilter@12685}
  ——filterDefs("MyFilter"->{FilterDef@3877}"FilterDef[filterName=Myfilter,filterClass=com.test.MyFilter]")
  ——filterMaps
    ——0={FilterMap@12703}"FilterMap[filterName=MyFilter,urlPattern=/filter]"
      ——filterName="MyFilter"
      ——urlPattern="/filter"
  (Servlet)
  ——servletMappings
    ——"/my"->"MyServlet"
  ——children
    ——"MyServlet"->{StandardWrapper@3903}"StandardEngine[Catalina].StandardHost[localhost].StandardContext[/xxx]"
  ——servletRegisMap={ConcurrentHashMap@7141}
    ——"MyServlet"->{WebServletRegistrationImpl@7222}
```
The way to get Context
```
ThreadGroup.threads ->
  Thread[ContainerBackgroundProcessor] ->
    ContainerBase$ContainerBackgroundProcessor ->
      WebModule
```

## (7) TongWeb
The code structure（Similar to Tomcat）
```
com.tongweb.web.thor.core.ThorStandardContext(.Superclass-> com.tongweb.web.thor.core.StandardContext)
  (Listener)
  ——applicationListeners
  (Filter)
  ——filterConfigs={HaashMap@11475}
    ——table
      ——0
        ——key="MyFilter"
        ——value={ApplicationFilterConfig@11543}
          ——filter={MyFilter@12685}
          ——filterDef->{FilterDef@3883}"FilterDef[filterName=MyFilter,filiterClass=com.test.MyFilter]"
            ——filterClass="com.test.MyFilter"
            ——filterName="MyFilter"
  ——filterDefs={HaashMap@11476}
    ——table
      ——0
       ——key="MyFilter"
       ——value={FilterDef@11547}
        ——filterClass="com.test.MyFilter"
        ——filterName="MyFilter"
  ——filterMaps
    ——array={FilterMap[2]@11558}
      ——0={FilterMap@11559}"FilterMap[filterName=MyFilter,urlPattern=/filter]"
        ——filterName="MyFilter"
        ——urlPatterns={String[1]@11563}
          ——0="/filter"
  (Servlet)
  ——servletMappings(HashMap@11490)
    ——table={HashMap$Entry[16]@11566}
      ——0={HashMap$Entry@11568}
        ——key="/servlet"
        ——value="MyServlet"
  ——children(HashMap@11510)
    ——table
      ——0={HashMap$Entry@11583}
        ——key="MyServlet"
        ——value={ThorStandardWrapper@11452}
          ——instance={MyFilter@11368}
          ——servletClass="com.test.MyFilter"
          ——mappings={ArrayList@11592}
            ——elementData={Object[2]@11610}
              ——0="/servlet"
          ——name="MyFilter"
```
The way to get Context
```
ThreadGroup.threads ->
  http-nio-8088-Acceptor-0 ->
    NioEndpoint$Acceptor ->
      NioEndpoint ->
        ThorHttp11NioProtocol$ThorHttpConnectionHandler ->
          RequestGroupInfo ->
            ThorRequest ->
              ThorStandardContext
```



## Some features of MemShell
(1) The listener/filter/servlet is not configured in web.xml  
(2) There is no class file for this listener/filter/servlet  
(3) The loader for this listener/filter/servlet is com.sun.org.apache.bcel.internal.util.ClassLoader/com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl$TransletClassLoader and so on








