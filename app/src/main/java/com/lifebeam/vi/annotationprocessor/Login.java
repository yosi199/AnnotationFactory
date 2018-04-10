package com.lifebeam.vi.annotationprocessor;

import com.lifebeam.vi.annotations.RequestModel;


@RequestModel(objectTypes = {int.class, String.class, String.class, Boolean.class, String[].class}, names = {"id", "name", "lastName", "rich", "children"})
public interface Login {
}
