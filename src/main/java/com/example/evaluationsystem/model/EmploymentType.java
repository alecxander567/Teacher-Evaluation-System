// src/main/java/com/example/evaluationsystem/model/EmploymentType.java
package com.example.evaluationsystem.model;

public enum EmploymentType {
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    CONTRACTUAL("Contractual");

    private final String displayName;

    EmploymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}