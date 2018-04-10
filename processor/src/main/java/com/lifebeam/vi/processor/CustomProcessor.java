package com.lifebeam.vi.processor;

import com.lifebeam.vi.annotations.SpecificType;
import com.lifebeam.vi.annotations.RequestModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import tools.AnnotationReader;
import tools.RequestReader;

public class CustomProcessor extends AbstractProcessor {

    private ElementKind[] requestModelKinds = {ElementKind.INTERFACE};
    private List<AnnotationReader> readers = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        readers.add(new RequestReader<>(RequestModel.class, requestModelKinds, processingEnvironment));
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for (AnnotationReader reader : readers) {
            @SuppressWarnings("unchecked")
            Collection<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(reader.getGenericType());
            List<TypeElement> types = ElementFilter.typesIn(elements);

            for (TypeElement t : types) {
                reader.addTypeElement(t);
            }
        }

        if (roundEnvironment.processingOver()) {
            for (AnnotationReader reader : readers) {
                reader.finish();
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(RequestModel.class.getName());
        set.add(SpecificType.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
