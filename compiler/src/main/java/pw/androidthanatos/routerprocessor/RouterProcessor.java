package pw.androidthanatos.routerprocessor;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import pw.androidthanatos.annotation.Module;
import pw.androidthanatos.annotation.Modules;
import pw.androidthanatos.annotation.Path;


@SupportedAnnotationTypes({"pw.androidthanatos.annotation.Path",
        "pw.androidthanatos.annotation.Module",
        "pw.androidthanatos.annotation.Modules"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor{

    private boolean hasModule = false;
    private boolean hasModules = false;
    private Filer mFiler;
    private RoundEnvironment mEnv;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mFiler = processingEnv.getFiler();
        mEnv = roundEnv;
        Set<? extends Element> moduleList = roundEnv.getElementsAnnotatedWith(Module.class);
        Set<? extends Element> modulesList = roundEnv.getElementsAnnotatedWith(Modules.class);

        String moduleName = Constans.MODULE_PREFIX;
        String[] moduleNames = null;
        if (moduleList != null && !moduleList.isEmpty()){
            Module module = moduleList.iterator().next().getAnnotation(Module.class);
            moduleName += module.value();
            hasModule = true;
        }
        if (modulesList != null && !modulesList.isEmpty()){
            Modules modules = modulesList.iterator().next().getAnnotation(Modules.class);
            moduleNames = modules.value();
            hasModules = true;
        }

        //普通app
        if (!hasModules && !hasModule){
            createrAppRouterHelper();
        }

        //组件化开发app
        if (hasModule){
            //保存每个module中的路由表
            createrModuleHelper(moduleName);
        }
        if (hasModules){
            //合并每个module中的路由表
            createrRouterHelper(moduleNames);
        }
        return true;
    }

    /**
     * 普通app下获取路由表
     */
    private void createrAppRouterHelper() {

        StringBuilder builder = new StringBuilder();
        builder.append("package pw.androidthanatos.router;\n\n")
                .append("public class RouterHelper {\n\n")
                .append("\tpublic static void merge(){\n");
        Set<? extends Element> aliasList = mEnv.getElementsAnnotatedWith(Path.class);
        if (aliasList == null || aliasList.isEmpty()){
            return;
        }
        for (Element element: aliasList) {
            Path path = element.getAnnotation(Path.class);
            TypeElement type = (TypeElement) element;
            builder.append("\t\tpw.androidthanatos.router.RouterTabs.map(")
                    .append("\""+path.value()+"\"")
                    .append(", ")
                    .append(type.getQualifiedName()+".class")
                    .append(");\n");

        }
        builder.append("\t}\n}");
        if (mFiler != null){
            try {
                JavaFileObject file = this.mFiler.createSourceFile("pw.androidthanatos.router.RouterHelper");
                Writer writer = file.openWriter();
                writer.append(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 组件app下合并路由表
     */
    private void createrRouterHelper(String[] moduleNames) {
        if (moduleNames == null || moduleNames.length == 0){
            return;
        }
        //String module = "";
        StringBuilder builder = new StringBuilder();
        builder.append("package pw.androidthanatos.router;\n\n")
                .append("public class RouterHelper {\n\n")
                .append("\tpublic static void merge(){\n");
        for (String name: moduleNames) {
            builder.append("\t\tpw.androidthanatos.router.")
                    .append(Constans.MODULE_PREFIX)
                    .append(name)
                    .append(".map();\n");

        }
        builder.append("\t}\n}");
        if (mFiler != null){
            try {
                JavaFileObject file = this.mFiler.createSourceFile("pw.androidthanatos.router.RouterHelper");
                Writer writer = file.openWriter();
                writer.append(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取每个module里面的路由表
     * @param moduleName module名字
     */
    private void createrModuleHelper(String moduleName) {
        if (Constans.MODULE_PREFIX.equals(moduleName)){
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("package pw.androidthanatos.router;\n")
                .append("public final class ")
                .append(moduleName)
                .append(" {\n\n")
                .append("\tpublic static void map(){\n");

        //遍历每个module中存在别名的activity
        Set<? extends Element> aliasList = mEnv.getElementsAnnotatedWith(Path.class);
        if (mEnv != null && aliasList != null && !aliasList.isEmpty()){
            for (Element element: aliasList) {
                Path path = element.getAnnotation(Path.class);
                TypeElement type = (TypeElement) element;
                builder.append("\t\tpw.androidthanatos.router.RouterTabs.map(")
                        .append("\""+path.value()+"\"")
                        .append(", ")
                        .append(type.getQualifiedName()+".class")
                        .append(");\n");
            }
        }
        builder.append("\t}\n}");
        if (mFiler != null){
            try {
                JavaFileObject file = this.mFiler.createSourceFile("pw.androidthanatos.router."+moduleName);
                Writer writer = file.openWriter();
                writer.append(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
