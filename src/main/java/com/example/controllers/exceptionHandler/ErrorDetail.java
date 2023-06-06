package com.example.controllers.exceptionHandler;

public class ErrorDetail {
    private final String fieldName;
    private final Object invalidValue;
    private final String constraint;

    public ErrorDetail(String fieldName, Object invalidValue, String constraint) {
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
        this.constraint = constraint;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public String getConstraint() {
        return constraint;
    }
}
