package tools;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public abstract class AnnotationReader<T extends Annotation> {

    final ProcessingEnvironment env;
    private final String TAG;
    private final Class<T> typedClass;
    private final ElementKind[] kinds;
    private StringBuilder sb = new StringBuilder();
    private List<TypeElement> types = new ArrayList<>();

    AnnotationReader(Class<T> typedParameterClass, ElementKind[] kinds, ProcessingEnvironment env) {
        this.typedClass = typedParameterClass;
        this.kinds = kinds;
        this.env = env;
        this.TAG = "Reader: " + typedClass.getSimpleName();
    }

    public Class<T> getGenericType() {
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

//        printDebug(t);

        types.add(t);
        System.out.println(TAG + " Added typeElement");
    }

    protected List<TypeElement> getTypes() {
        return types;
    }

    public abstract void finish();

    private void printDebug(TypeElement t) {
        System.out.println("*********** Elements *************");

        System.out.println(TAG + " NestingKind: " + t.getNestingKind());
        System.out.println(TAG + " AnnotationMirror: " + t.getAnnotationMirrors());
        System.out.println(TAG + " Kind: " + t.getKind());
        System.out.println(TAG + " Modifiers: " + t.getModifiers());
        System.out.println(TAG + " ClassSimpleName: " + t.getClass().getSimpleName());
        System.out.println(TAG + " TypeParameter: " + t.getTypeParameters().toString());
        System.out.println(TAG + " SimpleName: " + t.getSimpleName());
        System.out.println(TAG + " Interfaces: " + t.getInterfaces());
        System.out.println(TAG + " SuperClass: " + t.getSuperclass());
        System.out.println(TAG + " QualifiedName: " + t.getQualifiedName());
        System.out.println(TAG + " AnnotationsByType: " + Arrays.toString(t.getAnnotationsByType(typedClass)));
        System.out.println("*********** /Elements *************");

    }

    private void wrongAnnotationTypeError(Element e, AnnotationReader reader) {
        sb.append(e.getKind().toString());
        sb.append(" ");
        sb.append(e);
        sb.append(" cannot be annotated with @");
        String message = sb.toString();
        env.getMessager().printMessage(Diagnostic.Kind.ERROR, message + reader.getGenericType().getSimpleName());
    }

}
