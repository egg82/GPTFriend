package me.egg82.gpt.hooks;

public interface PluginHook {
    void unload();

    void lightUnload();

    void lightReload();
}
