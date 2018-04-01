package com.lifebeam.vi.processor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

class AnnotationReader<T extends Annotation> {

    private final String TAG;

    private StringBuilder sb = new StringBuilder();
    private Class<T> typedClass;
    private ElementKind[] kinds;
    private Messager messager;
    private String packageName = null;
    private List<TypeElement> models = new ArrayList<>();

    AnnotationReader(Class<T> typedParameterClass, ElementKind[] kinds, Messager messager) {
        this.typedClass = typedParameterClass;
        this.kinds = kinds;
        this.messager = messager;
        this.TAG = "Reader: " + typedClass.getSimpleName();
    }

    public Class<T> getType() {
        return typedClass;
    }

    public ElementKind[] getKinds() {
        return kinds;
    }

    public boolean supportsKind(ElementKind kind) {
        for (ElementKind k : kinds) {
            if (k == kind) {
                return true;
            }
        }
        return false;
    }

    public void addTypeElement(TypeElement t) {
        if (!supportsKind(t.getKind())) {
            wrongAnnotationTypeError(t, this);
            return;
        }

//        System.out.println("*********** Elements *************");
//
//        System.out.println("AnnotationReader " + t.getNestingKind());
//        System.out.println("AnnotationReader " + t.getAnnotationMirrors());
//        System.out.println("AnnotationReader " + t.getKind());
//        System.out.println("AnnotationReader " + t.getModifiers());
//        System.out.println("AnnotationReader " + t.getClass().getSimpleName());
//        System.out.println("AnnotationReader " + t.getTypeParameters().toString());
//        System.out.println("AnnotationReader " + t.getSimpleName());
//        System.out.println("AnnotationReader " + t.getInterfaces());
//        System.out.println("AnnotationReader " + t.getSuperclass());
//        System.out.println("AnnotationReader " + t.getQualifiedName());
//        System.out.println("AnnotationReader " + Arrays.toString(t.getAnnotationsByType(typedClass)));

        models.add(t);
        System.out.println(TAG + " Added typeElement");
    }

    private void wrongAnnotationTypeError(Element e, AnnotationReader reader) {
        sb.append(e.getKind().toString());
        sb.append(" ");
        sb.append(e);
        sb.append(" cannot be annotated with @");
        String message = sb.toString();
        messager.printMessage(Diagnostic.Kind.ERROR, message + reader.getType().getSimpleName());
    }

}
