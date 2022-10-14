//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.agent;

import java.lang.instrument.Instrumentation;

public class MyAgent {
    public static String ClassName = "org.apache.catalina.core.ApplicationFilterChain";

    public MyAgent() {
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        inst.addTransformer(new MyTransformer(), true);
        Class[] loadedClasses = inst.getAllLoadedClasses();

        for(int i = 0; i < loadedClasses.length; ++i) {
            Class clazz = loadedClasses[i];
            if (clazz.getName().equals(ClassName)) {
                try {
                    inst.retransformClasses(new Class[]{clazz});
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }
        }

    }

    public static void premain(String args, Instrumentation inst) throws Exception {
    }
}
