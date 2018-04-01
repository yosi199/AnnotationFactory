package com.lifebeam.vi.processor;

import com.google.auto.service.AutoService;
import com.lifebeam.vi.annotations.Identifier;
import com.lifebeam.vi.annotations.RequestModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;


@AutoService(Processor.class)
public class CustomProcessor extends AbstractProcessor {

    private Messager messager;
    private ElementKind[] requestModelKinds = {ElementKind.INTERFACE};

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Collection<? extends Element> requestModels = roundEnvironment.getElementsAnnotatedWith(RequestModel.class);
        List<TypeElement> requestModelTypes = ElementFilter.typesIn(requestModels);

        AnnotationReader<RequestModel> requestModelReader = new AnnotationReader<>(RequestModel.class, requestModelKinds, messager);

        for (TypeElement t : requestModelTypes) {
            requestModelReader.addTypeElement(t);
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(RequestModel.class.getName());
        set.add(Identifier.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
