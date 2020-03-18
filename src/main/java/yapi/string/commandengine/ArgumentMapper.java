package yapi.string.commandengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentMapper {

    private List<ArgumentMapping> argumentMappings = new ArrayList<>();

    public ArgumentMapper() {

    }

    public ArgumentMapper(ArgumentMapping... argumentMappings) {
        this.argumentMappings = Arrays.stream(argumentMappings).collect(Collectors.toList());
    }

    public ArgumentMapper addArgumentMapping(ArgumentMapping argumentMapping) {
        argumentMappings.add(argumentMapping);
        return this;
    }

}
