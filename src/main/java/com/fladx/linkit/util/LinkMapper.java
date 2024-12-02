package com.fladx.linkit.util;

import com.fladx.linkit.dto.EditLinkDto;
import com.fladx.linkit.model.Link;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LinkMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateLinkFromDTO(EditLinkDto linkDto, @MappingTarget Link link);
}
