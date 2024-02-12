package com.evri.interview.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class Courier {
    @NotNull
    Long id;
    @NotNull
    @Size(min = 5, max = 129)
    String name;
    @NotNull
    Boolean active;
}
