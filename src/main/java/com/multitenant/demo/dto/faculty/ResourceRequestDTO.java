package com.multitenant.demo.dto.faculty;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceRequestDTO {
    private String resourceName;

    @JsonCreator
    public ResourceRequestDTO(String resourceName) {
        this.resourceName = resourceName;
    }
}
