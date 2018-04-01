package com.lifebeam.vi.annotationprocessor;


import com.lifebeam.vi.annotations.Identifier;
import com.lifebeam.vi.annotations.RequestModel;

@Identifier
@RequestModel(objectTypes = {int.class, String.class},
        names = {"count, name"})
public class HelloWorld {
}
