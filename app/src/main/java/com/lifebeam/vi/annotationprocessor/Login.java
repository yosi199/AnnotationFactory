package com.lifebeam.vi.annotationprocessor;

import com.lifebeam.vi.annotations.RequestModel;

@RequestModel(objectTypes = {int.class, String.class}, names = {"id", "name"})
public interface Login {
}
