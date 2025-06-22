package com.proxy.cloudplugin.gradle_7.transform;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.proxy.cloudplugin.PluginImpl;
import com.proxy.cloudplugin.asm.ServiceInject;
import com.proxy.cloudplugin.gradle_7.utils.FileUtils;
import com.proxy.cloudplugin.gradle_7.utils.JarUtils;
import org.gradle.api.Project;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * cangHX
 * on 2024/3/19 15:11
 */
public class ServiceTransform extends Transform {

    public ServiceTransform(Project project){
    }

    @Override
    public String getName() {
        return "ServiceTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        HashSet<QualifiedContent.ContentType> set = new HashSet<>();
        set.add(QualifiedContent.DefaultContentType.CLASSES);
        return set;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        HashSet<QualifiedContent.Scope> set = new HashSet<>();
        set.add(QualifiedContent.Scope.PROJECT);
        set.add(QualifiedContent.Scope.SUB_PROJECTS);
        set.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES);
        return set;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    private final ArrayList<String> services = new ArrayList<>();

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        services.clear();

        AtomicReference<JarInput> dataJarFile = new AtomicReference<>();

        TransformOutputProvider output = transformInvocation.getOutputProvider();
        output.deleteAll();

        transformInvocation.getInputs().forEach ( input ->{
            input.getDirectoryInputs().forEach (dir ->{
                try (Stream<Path> stream = Files.walk(dir.getFile().toPath())) {
                    stream.forEach(path -> {
                        File file = path.toFile();
                        if (!file.isFile()) {
                            return;
                        }
                        findService(file.getAbsolutePath());
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                File outPath = output.getContentLocation(
                    dir.getName(),
                    dir.getContentTypes(),
                    dir.getScopes(),
                    Format.DIRECTORY
                );
                FileUtils.copyDirectory(dir.getFile(), outPath);
            });
            input.getJarInputs().forEach (jar ->{
                AtomicBoolean isDataJarFile = new AtomicBoolean(false);

                checkJar(jar, (jarFile, jarEntry) -> {
                    if (jarEntry.getName().endsWith(PluginImpl.className)) {
                        isDataJarFile.set(true);
                        dataJarFile.set(jar);
                        return;
                    }
                    findService(jarEntry.getName());
                });

                if (!isDataJarFile.get()) {
                    copyJar(jar, output);
                }
            });
        });

        if (dataJarFile.get() !=null){

            checkJar(dataJarFile.get(), (jarFile, jarEntry) -> {
                if (jarEntry.getName().endsWith(PluginImpl.className)) {
                    try {
                        InputStream inputStream = jarFile.getInputStream(jarEntry);
                        JarUtils.write(
                                ServiceInject.inject(services, inputStream),
                                new File(dataJarFile.get().getFile().getParentFile(), PluginImpl.className)
                        );
                        inputStream.close();
                    }catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }
            });
            JarUtils.refreshJar(dataJarFile.get().getFile());
            copyJar(dataJarFile.get(), output);
        }
    }

    private void findService(String jarEntryName) {
        if (jarEntryName.contains(PluginImpl.servicePath)) {
            String[] str = jarEntryName.split("/");
            String className = str[str.length-1];
            String service = "com.cloud.service." + className.substring(
                0,
                className.length() - 6
            );
            if (!services.contains(service)) {
                services.add(service);
            }
        }
    }

    private void copyJar(JarInput jar, TransformOutputProvider output) {
        var jarName = jar.getName();

        String md5Name = JarUtils.md5Hex(jar.getFile().getAbsolutePath());
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4);
        }
        File outPath =
                output.getContentLocation(jarName + md5Name, jar.getContentTypes(), jar.getScopes(), Format.JAR);
        FileUtils.copyFile(jar.getFile(), outPath);
    }

    public void checkJar(JarInput jarInput, BiConsumer<JarFile, JarEntry> callback) {
        try {
            JarFile jarFile = new JarFile(jarInput.getFile());
            Enumeration<JarEntry> entries = jarFile.entries();


            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (!jarEntry.getName().endsWith(".class")) {
                    continue;
                }
                if (jarEntry.getName().contains("META-INF")) {
                    continue;
                }

                callback.accept(jarFile, jarEntry);
            }
            jarFile.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}