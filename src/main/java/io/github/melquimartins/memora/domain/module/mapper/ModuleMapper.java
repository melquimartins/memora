package io.github.melquimartins.memora.domain.module.mapper;

import io.github.melquimartins.memora.domain.module.Module;
import io.github.melquimartins.memora.domain.module.dto.ModuleResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModuleMapper {

    public ModuleResponse toResponse(Module module) {
        return new ModuleResponse(
                module.getId(),
                module.getUuid(),
                module.getTitle(),
                module.getDescription(),
                module.getCreatedAt(),
                module.getUpdatedAt()
        );
    }

    public List<ModuleResponse> toResponseList(List<Module> modules) {
        return modules.stream().map(this::toResponse).toList();
    }

}
