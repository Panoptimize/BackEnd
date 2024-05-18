package com.itesm.panoptimize.controller;

public interface Mappable<Entity, DTO> {
    DTO convertToDTO(Entity entity);
    Entity convertToEntity(DTO dto);
}
