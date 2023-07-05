package ru.job4j.tracker.model;

import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Permission {

    @Getter
    @EqualsAndHashCode.Include
    private int id;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String name;

    @Getter
    @Setter
    @Singular("addRule")
    private List<String> rules;

}