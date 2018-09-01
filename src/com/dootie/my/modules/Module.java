package com.dootie.my.modules;

import java.util.ArrayList;
import java.util.List;


public abstract class Module {
    public static List<Module> modules = new ArrayList<Module>();
    
    public static void executeModules(){
        for(Module module : Module.modules) module.run();
    }
    
    public abstract void run();
}