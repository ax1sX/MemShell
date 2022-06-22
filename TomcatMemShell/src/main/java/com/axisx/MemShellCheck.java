package com.axisx;

import com.sun.org.apache.bcel.internal.Repository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemShellCheck implements Filter {
    private static Integer count=0;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        count=0;
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-type", "text/plain;charset=UTF-8");
        String id = req.getParameter("id");
        String action = req.getParameter("action");
        try {
            Object context = getContext();
            if (action.equals("scan")) {
                /**
                 * Listener
                 */
                resp.getWriter().println("/**\n" + "* Listener\n" + "*/\n");
                List<Object> listeners= (List<Object>) getFV(context,"applicationEventListenersList");
                if (listeners != null) {
                    List<ServletRequestListener> newListeners = new ArrayList<>();
                    for (Object o : listeners) {
                        if (o instanceof ServletRequestListener) {
                            newListeners.add((ServletRequestListener) o);
                        }
                    }
                    String listenerTName = null;
                    for (ServletRequestListener listener : newListeners) {
                        listenerTName = listener.getClass().getName();
                        int idw = IDGenerate();
                        resp.getWriter().println(Template(idw, listener.hashCode(), null, null, listenerTName, listener.getClass().getClassLoader().getClass().getName(), listener.getClass()));
                        HashMap<String, Object> memShellInfo= CheckStruct.newMemShellInfo(null,null,listenerTName,listener.getClass().getClassLoader().getClass().getName(),listener.getClass(),"Listener");
                        CheckStruct.set(listener.hashCode(),memShellInfo);
                    }
                }


                /**
                 * Filter
                 */
                resp.getWriter().println("/**\n" + "* Filter\n" + "*/\n");
                Object[] filterMaps1 = getFilterMaps(req);
                for (int i = 0; i < filterMaps1.length; i++) {
                    try {
                        Object fm = filterMaps1[i];
                        Method getFilterName = fm.getClass().getDeclaredMethod("getFilterName");
                        getFilterName.setAccessible(true);
                        String filterName = (String) getFilterName.invoke(fm, null);

                        HashMap<String, Object> filterConfigs = (HashMap<String, Object>) getFV(context, "filterConfigs");
                        Object appFilterConfig = filterConfigs.get(filterName);


                        Method getURLPatterns = fm.getClass().getDeclaredMethod("getURLPatterns");
                        getFilterName.setAccessible(true);
                        String[] urlpatterns = (String[]) getURLPatterns.invoke(fm, null);

                        if (appFilterConfig != null) {
                            Object filter = getFV(appFilterConfig, "filter");
                            String filterClassName = filter.getClass().getName();
                            String filterClassLoaderName = filter.getClass().getClassLoader().getClass().getName();
                            int idw = IDGenerate();
                            resp.getWriter().println(Template(idw, filter.hashCode(), filterName, arrayToString(urlpatterns), filterClassName, filterClassLoaderName, filter.getClass()));
                            HashMap<String, Object> memShellInfo= CheckStruct.newMemShellInfo(filterName,arrayToString(urlpatterns),filterClassName,filterClassLoaderName,filter.getClass(),"Filter");
                            CheckStruct.set(filter.hashCode(),memShellInfo);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /**
                 * Servlet
                 */
                resp.getWriter().println("/**\n" + "* Servlet\n" + "*/\n");
                Map<String, Object> children = (Map<String, Object>) getFV(context, "children"); //Tomcat此处是HashMap
                HashMap<String, String> servletMappings = (HashMap<String, String>) getFV(context, "servletMappings");

                for (Map.Entry<String, String> map : servletMappings.entrySet()) {
                    String servletMapPath = map.getKey();
                    String servletName1 = map.getValue();
                    Object wrapper = children.get(servletName1);

                    Class servletClass = null;
                    Class wrapperC = Class.forName("org.apache.catalina.core.StandardWrapper");
                    try {
                        Method getServletClass = wrapperC.getDeclaredMethod("getServletClass");
                        getServletClass.setAccessible(true);
                        servletClass = Class.forName((String) getServletClass.invoke(wrapper));
                    } catch (Exception e) {
                        Method getServletClass = wrapperC.getDeclaredMethod("getServlet");
                        getServletClass.setAccessible(true);
                        Object servlet = getServletClass.invoke(wrapper);
                        if (servlet != null) {
                            servletClass = servlet.getClass();
                        }
                    }
                    if (servletClass != null) {
                        String servletClassName = servletClass.getName();
                        String servletClassLoaderName = null;
                        try {
                            servletClassLoaderName = servletClass.getClassLoader().getClass().getName();
                        } catch (Exception e) {
                        }
                        int idw = IDGenerate();
                        resp.getWriter().println(Template(idw, servletClass.hashCode(), servletName1, servletMapPath, servletClassName, servletClassLoaderName, servletClass));
                        HashMap<String, Object> memShellInfo= CheckStruct.newMemShellInfo(servletName1,servletMapPath,servletClassName,servletClassLoaderName,servletClass,"Servlet");
                        CheckStruct.set(servletClass.hashCode(),memShellInfo);
                    }
                }
            }else if (action.equals("dump")){
                String className= (String) CheckStruct.get(Integer.valueOf(id),"class");
                byte[] classBytes = Repository.lookupClass(Class.forName(className)).getBytes();
                resp.addHeader("content-Type", "application/octet-stream");
                String filename = Class.forName(className).getSimpleName() + ".class";

                String agent = req.getHeader("User-Agent");
                if (agent.toLowerCase().indexOf("chrome") > 0) {
                    resp.addHeader("content-Disposition", "attachment;filename=" + new String(filename.getBytes("UTF-8"), "ISO8859-1"));
                } else {
                    resp.addHeader("content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                }
                ServletOutputStream outDumper = resp.getOutputStream();
                outDumper.write(classBytes, 0, classBytes.length);
                outDumper.close();
                }


    }catch (Exception e2){e2.printStackTrace();}
//        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    public Object getContext() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ThreadGroup threadGroup=Thread.currentThread().getThreadGroup();
        Field field=threadGroup.getClass().getDeclaredField("threads");
        field.setAccessible(true);
        Thread[] threads=(Thread[])field.get(threadGroup);
        for (Thread thread : threads) {
            if (thread.getName().contains("Acceptor") && thread.getName().contains("http")) {
                Field tfield = thread.getClass().getDeclaredField("target");
                tfield.setAccessible(true);
                Object NioEndpoint$ = tfield.get(thread);

                Field thisField = NioEndpoint$.getClass().getDeclaredField("this$0");
                thisField.setAccessible(true);
                Object NioEndpointO = thisField.get(NioEndpoint$);

                Field handlerField = NioEndpointO.getClass().getSuperclass().getSuperclass().getDeclaredField("handler");
                ;
                handlerField.setAccessible(true);
                Object AbstractProtocol = handlerField.get(NioEndpointO);

                Field globalField = AbstractProtocol.getClass().getDeclaredField("proto");
                globalField.setAccessible(true);
                Object Http11NioProtocol = globalField.get(AbstractProtocol);

                Field adapterField = Http11NioProtocol.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("adapter");
                adapterField.setAccessible(true);
                Object CoyoteAdapter = adapterField.get(Http11NioProtocol);

                Field connectorField = CoyoteAdapter.getClass().getDeclaredField("connector");
                connectorField.setAccessible(true);
                Object Connector = connectorField.get(CoyoteAdapter);

                Field serviceField = Connector.getClass().getDeclaredField("service");
                serviceField.setAccessible(true);
                Object StandardService = serviceField.get(Connector);

                Field engineField = StandardService.getClass().getDeclaredField("engine");
                engineField.setAccessible(true);
                Object StandardEngine = engineField.get(StandardService);

                Field childrenField = StandardEngine.getClass().getSuperclass().getDeclaredField("children");
                childrenField.setAccessible(true);
                HashMap HashMap = (java.util.HashMap) childrenField.get(StandardEngine);

                Object StandardHost = HashMap.values().iterator().next();
                Field childrenFieldH = StandardHost.getClass().getSuperclass().getDeclaredField("children");
                childrenFieldH.setAccessible(true);
                HashMap hashMap = (HashMap) childrenFieldH.get(StandardHost);

                Object standardContext = hashMap.values().iterator().next();
                return standardContext;
            }
        }
        return null;
    }

    public Object[] getFilterMaps(HttpServletRequest request) throws Exception {
        Object standardContext = getContext();
        Object filterMaps=getFV(standardContext,"filterMaps");
        Object[] filterArray = null;
        try {
            // tomcat 789
            filterArray= (Object[])getFV(filterMaps,"array");
        } catch (Exception e) {
            // tomcat 6
            filterArray = (Object[]) filterMaps;
        }

        return filterArray;
    }

    public String Template(Integer count,Integer ID,String Name,String Pattern,String Class,String ClassLoader,Class<?> aClass){
        if (Pattern!=null){
            return String.format("Count:%d\nID: %d\nName: %s\nPattern: %s\nClass: %s\nClassLoader: %s\nFile Path: %s\n",count,ID, Name,Pattern,Class,ClassLoader,classFileIsExists(aClass));
        }else {
            return String.format("Count:%d\nID: %d\nClass: %s\nClassLoader: %s\nFile Path: %s\n",count,ID,Class,ClassLoader,classFileIsExists(aClass));
        }

    }

    public Integer IDGenerate(){
        count=count+1;
        return count;
    }

    public Object getFV(Object var0, String var1) throws Exception {
        Field var2 = null;
        Class var3 = var0.getClass();

        while(var3 != Object.class) {
            try {
                var2 = var3.getDeclaredField(var1);
                break;
            } catch (NoSuchFieldException var5) {
                var3 = var3.getSuperclass();
            }
        }

        if (var2 == null) {
            throw new NoSuchFieldException(var1);
        } else {
            var2.setAccessible(true);
            return var2.get(var0);
        }
    }

    public static String arrayToString(String[] str) {
        String res = "";
        for (String s : str) {
            res += String.format("%s,", s);
        }
        res = res.substring(0, res.length() - 1);
        return res;
    }

    public static String classFileIsExists(Class clazz) {
        if (clazz == null) {
            return "class is null";
        }
        String className = clazz.getName();
        String classNamePath = className.replace(".", "/") + ".class";
        URL is = clazz.getClassLoader().getResource(classNamePath);
        if (is == null) {
            return "There is no corresponding class file on disk, it may be MemShell";
        } else {
            return is.getPath();
        }
    }

    public static class CheckStruct {

        static HashMap<Integer, HashMap<String, Object>> memShells = new HashMap<>();

        public static boolean set(Integer id, HashMap<String, Object> info) throws Exception {
            if (memShells.containsKey(id)){
                System.out.println(String.format("%s already exists !!!", id));
                return false;
            }
            if (memShells.put(id, info) != null){
                return true;
            }
            else {
                return false;
            }
        }

        public static Object get(Integer id, String key) {
            try{
                return memShells.get(id).get(key);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public static HashMap<String, Object> newMemShellInfo(Object name,Object pattern,Object classC,Object ClassLoader,Object filePath,Object type){
            HashMap<String, Object> memShellInfo = new HashMap<String, Object>();
            memShellInfo.put("name", name);
            memShellInfo.put("pattern", pattern);
            memShellInfo.put("class", classC);
            memShellInfo.put("classloader", ClassLoader);
            memShellInfo.put("filePath", filePath);
            memShellInfo.put("type", type);
            return memShellInfo;
        }

    }
}
