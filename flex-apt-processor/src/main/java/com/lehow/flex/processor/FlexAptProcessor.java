package com.lehow.flex.processor;

import com.google.auto.service.AutoService;
import com.lehow.flex.annotations.FlexEntity;
import com.lehow.flex.annotations.FlexField;
import com.lehow.flex.annotations.InjectActivityAndArrayRes;
import com.lehow.flex.annotations.ValueDependence;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class) @SupportedAnnotationTypes({
    "com.lehow.flex.annotations.FlexEntity"
}) @SupportedSourceVersion(SourceVersion.RELEASE_7)

public class FlexAptProcessor extends AbstractProcessor {

  private Filer mFiler; //文件相关的辅助类
  private Elements mElementUtils; //元素相关的辅助类
  private Messager mMessager; //日志相关的辅助类

  @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    mFiler = processingEnv.getFiler();
    mElementUtils = processingEnv.getElementUtils();
    mMessager = processingEnv.getMessager();
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    info("==process==");
    Set<? extends Element> flexEntityElements =
        roundEnvironment.getElementsAnnotatedWith(FlexEntity.class);
    for (Element flexEntityElement : flexEntityElements) {
      //读取该FlexEntity下的FlexField字段

      List<? extends Element> enclosedElements = flexEntityElement.getEnclosedElements();
      MethodSpec.Builder creteListBuilder = MethodSpec.methodBuilder("createFieldList")
          .addModifiers(Modifier.PROTECTED)
          .returns(TypeName.VOID);
      MethodSpec.Builder createDependence = MethodSpec.methodBuilder("createDependence")
          .addModifiers(Modifier.PROTECTED)
          .returns(TypeName.VOID);
      for (Element enclosedElement : enclosedElements) {
        FlexField flexField = enclosedElement.getAnnotation(FlexField.class);
        if (flexField != null) {
          String key = flexField.key();
          //如果key为空，则默认为字段名
          key = (key == null || key.isEmpty()) ? enclosedElement.getSimpleName().toString() : key;
          if (getFieldAnnotationClass(enclosedElement, FlexField.class.getName(), "fieldProcessor")
              == null) {
            creteListBuilder.addStatement("add(new $T(\""
                    + key
                    + "\",entity."
                    + enclosedElement.getSimpleName().toString()
                    + ")\n"
                    + "                .setTitle(\""
                    + flexField.title()
                    + "\")\n"
                    + "                .setSummary(\""
                    + flexField.summary()
                    + "\")\n"
                    + "                .setHint(\""
                    + flexField.hint()
                    + "\")\n"
                    + "                .setProxyViewType(getProxyViewType($T.class))\n"
                    + "                .setFlexFieldProcessor(null))",
                ClassName.bestGuess("com.lehow.flex.base.FlexField"),
                getFieldAnnotationClass(enclosedElement, FlexField.class.getName(),
                    "proxyAdapter"));
          } else {//这个给 fieldProcessor 添加构造器

            InjectActivityAndArrayRes simpleArrayRes =
                enclosedElement.getAnnotation(InjectActivityAndArrayRes.class);
            StringBuilder stringBuilder = new StringBuilder();
            if (simpleArrayRes != null) {
              stringBuilder.append("activity");
              for (int i : simpleArrayRes.value()) {
                stringBuilder.append("," + i);
              }
            }

            creteListBuilder.addStatement("add(new $T(\""
                    + key
                    + "\",entity."
                    + enclosedElement.getSimpleName().toString()
                    + ")\n"
                    + "                .setTitle(\""
                    + flexField.title()
                    + "\")\n"
                    + "                .setSummary(\""
                    + flexField.summary()
                    + "\")\n"
                    + "                .setHint(\""
                    + flexField.hint()
                    + "\")\n"
                    + "                .setProxyViewType(getProxyViewType($T.class))\n"
                    + "                .setFlexFieldProcessor(new $T($L)))",
                ClassName.bestGuess("com.lehow.flex.base.FlexField"),
                getFieldAnnotationClass(enclosedElement, FlexField.class.getName(), "proxyAdapter"),
                getFieldAnnotationClass(enclosedElement, FlexField.class.getName(),
                    "fieldProcessor"), stringBuilder.toString());
          }

          info(flexField.title());
          ValueDependence valueDependence = enclosedElement.getAnnotation(ValueDependence.class);
          if (valueDependence == null) continue;
          String[] keys = valueDependence.dependenOn();
          TypeName typeName =
              getFieldAnnotationClass(enclosedElement, ValueDependence.class.getName(), "func");
          if (keys == null || keys.length == 0) {
            //忽略掉
            continue;
          } else {
            if (keys.length == 1) {
              createDependence.addStatement("findFlexField(\""
                  + keys[0]
                  + "\").getValueObservable().map(new $T() {\n"
                  + "    }).subscribe(findFlexField(\""
                  + key
                  + "\"))", typeName);
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              for (String s : keys) {
                stringBuilder.append("findFlexField(\"" + s + "\").getValueObservable(),");
              }
              createDependence.addStatement("$T.combineLatest("
                  + stringBuilder
                  + "new $T()).subscribe(findFlexField(\""
                  + key
                  + "\"))", ClassName.bestGuess("io.reactivex.Observable"), typeName);
            }
          }
        }
      }
      MethodSpec constructor = MethodSpec.constructorBuilder()
          .addModifiers(Modifier.PRIVATE)
          .addParameter(
              ParameterSpec.builder(ClassName.bestGuess("android.app.Activity"), "activity")
                  .build())
          .addParameter(
              ParameterSpec.builder(TypeName.get(flexEntityElement.asType()), "entity").build())
          .addStatement("super(activity,entity)")
          .build();

      TypeSpec flexEntityTypeSpec =
          TypeSpec.classBuilder(flexEntityElement.getSimpleName() + "$$FlexEntity")
              .addModifiers(Modifier.PUBLIC)
              .superclass(
                  ParameterizedTypeName.get(ClassName.get(com.lehow.flex.base.FlexEntity.class),
                      TypeName.get(flexEntityElement.asType())))
              .addMethod(constructor)
              .addMethod(creteListBuilder.build())
              .addMethod(createDependence.build())
              .build();

      String packageName =
          mElementUtils.getPackageOf(flexEntityElement).getQualifiedName().toString();
      try {
        JavaFile.builder(packageName, flexEntityTypeSpec).build().writeTo(mFiler);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  private TypeName getFieldAnnotationClass(Element flexFieldElement, String annotationName,
      String property) {
    for (AnnotationMirror annotationMirror : flexFieldElement.getAnnotationMirrors()) {
      if (annotationMirror.getAnnotationType().toString().equals(annotationName)) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror
            .getElementValues()
            .entrySet()) {
          if (entry.getKey().getSimpleName().toString().equals(property)) {
            return ClassName.get((TypeMirror) (entry.getValue().getValue()));
          }
        }
      }
    }
    return null;
  }


  private void info(String msg) {
    mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
  }

  private void info(Element e, String msg, Object... args) {
    mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args), e);
  }
}
