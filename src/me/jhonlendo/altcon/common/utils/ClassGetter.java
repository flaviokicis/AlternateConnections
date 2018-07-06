package me.jhonlendo.altcon.common.utils;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.jhonlendo.altcon.common.networking.packets.TPacket;

public class ClassGetter {

    public static HashSet<Class<? extends TPacket>> getClassesInPackage(Object plugin, String packageName) {
        HashSet<Class<? extends TPacket>> classes = new HashSet<>();

        CodeSource src = plugin.getClass().getProtectionDomain().getCodeSource();
        if (src != null) {
            URL resource = src.getLocation();
            resource.getPath();
            runThroughJAR(resource, packageName, classes);
        }
        return classes;
    }

    private static Class<?> initClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }

    private static void runThroughJAR(URL resource, String packageName, HashSet<Class<? extends TPacket>> classes) {
        String relPath = packageName.replace('.', '/');
        String resPath = resource.getPath().replace("%20", " ");
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        JarFile jarFile;
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'. Do you have strange characters in your folders? Such as #?", e);
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath)
                    && entryName.length() > (relPath.length() + "/".length())) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className != null) {
                Class<?> c = initClass(className);
                if (c != null && (TPacket.class.isAssignableFrom(c)))
                    classes.add((Class<? extends TPacket>) c);
            }
        }
    }
}