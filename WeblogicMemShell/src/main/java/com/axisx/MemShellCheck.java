package com.axisx;

import com.sun.org.apache.bcel.internal.Repository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
        String action=req.getParameter("action");
        try {
            Object context=getContext(); //WebAppContext
            if(action.equals("scan")){
                /**
                 * Listener
                 */
                resp.getWriter().println("/**\n" + "* Listener\n" + "*/\n");
                List<Object> listeners=(List<Object>) getFV(getFV(context,"eventsManager"),"requestListeners");
                if (listeners != null || listeners.size() != 0) {
                    List<ServletRequestListener> newListeners = new ArrayList<>();
                    for (Object o : listeners) {
                        if (o instanceof ServletRequestListener) {
                            newListeners.add((ServletRequestListener) o);
                        }
                    }
                    String listenerTName=null;
                    for (ServletRequestListener listener : newListeners) {
                        listenerTName=listener.getClass().getName();
                        int idw=IDGenerate();
                        resp.getWriter().println(Template(idw,listener.hashCode(),null,null,listenerTName,listener.getClass().getClassLoader().getClass().getName(),listener.getClass()));
                        HashMap<String, Object> memShellInfo= CheckStruct.newMemShellInfo(null,null,listenerTName,listener.getClass().getClassLoader().getClass().getName(),listener.getClass(),"Listener");
                        CheckStruct.set(listener.hashCode(),memShellInfo);
                    }
                }


                /**
                 * Filter
                 */
                resp.getWriter().println("/**\n" + "* Filter\n" + "*/\n");

                List<Object> filtermappings=(List<Object> )getFV(getFV(context,"filterManager"),"filterPatternList");
                for (Object o : filtermappings) {
                    String filterName= (String) getFV(o,"filterName");
                    Map map= (Map) getFV(getFV(o,"map"),"matchMap");
                    String urlpattern= (String) map.keySet().iterator().next();
                    Map<String,Object> mapfilter= (Map<String,Object>) getFV(getFV(context,"filterManager"),"filters");
                    for (Map.Entry<String, Object> entry : mapfilter.entrySet()) {
                        if (entry.getKey().equals(filterName)){
                            Object filterwrapper=entry.getValue();
                            Object filter=getFV(filterwrapper,"filter");
                            String filterClassName = filter.getClass().getName();
                            String filterClassLoaderName = filter.getClass().getClassLoader().getClass().getName();
                            int idw=IDGenerate();
                            resp.getWriter().println(Template(idw,filter.hashCode(),filterName,urlpattern,filterClassName,filterClassLoaderName,filter.getClass()));
                            HashMap<String, Object> memShellInfo= CheckStruct.newMemShellInfo(filterName,urlpattern,filterClassName,filterClassLoaderName,filter.getClass(),"Filter");
                            CheckStruct.set(filter.hashCode(),memShellInfo);
                        }
                    }
                }


                /**
                 * Servlet
                 */
                resp.getWriter().println("/**\n" + "* Servlet\n" + "*/\n");

                Map<String,Object> mapservlet=(Map<String,Object>)getFV(getFV(context,"servletMapping"),"matchMap");
                Map<String,Object> cachedClasses=(Map<String,Object>)getFV(getFV(context,"classLoader"),"cachedClasses");
                for (Map.Entry<String, Object> entry : mapservlet.entrySet()) {
                    String urlpattern=entry.getKey();
                    Object servletstub=getFV(getFV(entry.getValue(),"exactValue"),"servletStub");
                    String servletName1= (String) getFV(servletstub,"name");
                    String className= (String) getFV(servletstub,"className");
                    for (Map.Entry<String, Object> entry1 : cachedClasses.entrySet()) {
                        if (entry1.getKey().equals(className)) {
                            Class servletClass= (Class) entry1.getValue();
                            int idw=IDGenerate();
                            String servletClassName = servletClass.getClass().getName();
                            String servletClassLoaderName = null;
                            try {
                                servletClassLoaderName = servletClass.getClass().getClassLoader().getClass().getName();
                            } catch (Exception e) {}
                            resp.getWriter().println(Template(idw,servletClass.hashCode(),servletName1,urlpattern,servletClassName,servletClassLoaderName,servletClass));
                            HashMap<String, Object> memShellInfo= CheckStruct.newMemShellInfo(servletName1,urlpattern,servletClassName,servletClassLoaderName,servletClass,"Servlet");
                            CheckStruct.set(servletClass.hashCode(),memShellInfo);
                        }
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

        } catch (Exception e) {e.printStackTrace();}
//        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    /**
     * Weblogic
     */
    public static synchronized Object getContext() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        ThreadGroup threadGroup=Thread.currentThread().getThreadGroup();
        Field field=threadGroup.getClass().getDeclaredField("threads");
        field.setAccessible(true);
        Thread[] threads=(Thread[])field.get(threadGroup);
        for (Thread thread : threads) {
            if (thread.getName().contains("ACTIVE")) {
                Field f1 = thread.getClass().getDeclaredField("workEntry");
                f1.setAccessible(true);
                Object o1 = f1.get(thread);

                Field f2 = o1.getClass().getDeclaredField("connectionHandler");
                f2.setAccessible(true);
                Object o2 = f2.get(o1);

                Field f3 = o2.getClass().getDeclaredField("request");
                f3.setAccessible(true);
                Object o3 = f3.get(o2);

                Field f4 = o3.getClass().getDeclaredField("context");
                f4.setAccessible(true);
                Object o4 = f4.get(o3);
                return o4;
            }
        }
        return null;
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
