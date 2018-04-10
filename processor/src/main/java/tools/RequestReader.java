package tools;

import com.lifebeam.vi.annotations.RequestModel;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

public class RequestReader<T extends Annotation> extends AnnotationReader<T> {
    private Map<String, Class> fieldsMap = new HashMap<>();


    public RequestReader(Class<T> typedParameterClass, ElementKind[] kinds, ProcessingEnvironment env) {
        super(typedParameterClass, kinds, env);
    }

    @Override
    public void finish() {
        for (TypeElement t : getTypes()) {

            RequestModel request = (RequestModel) t.getAnnotation(getGenericType());
            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(t.getSimpleName().toString() + "Generated")
                    .addModifiers(Modifier.PUBLIC);

            try {
                Object[] objectTypes = request.objectTypes();
            } catch (MirroredTypesException e) {
                List<? extends TypeMirror> typeMirrors = e.getTypeMirrors();
                for (int i = 0; i < typeMirrors.size(); i++) {
                    TypeMirror tm = typeMirrors.get(i);
                    Class c = ClassUtils.getClass(tm.toString());
                    fieldsMap.put(request.names()[i], c);
                }
            }

            for (Map.Entry<String, Class> e : fieldsMap.entrySet()) {
                FieldSpec fieldSpec = FieldSpec.builder(e.getValue(), e.getKey(), Modifier.PUBLIC).build();
                typeBuilder.addField(fieldSpec);
            }

            String qualifiedName = t.getQualifiedName().toString();
            int index = qualifiedName.indexOf(t.getSimpleName().toString());
            String packageName = qualifiedName.substring(0, index - 1);

            try {
                JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build()).build();
                javaFile.writeTo(env.getFiler());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
