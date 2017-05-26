package ua.sgkhmja.wboard.service.mapper;

import ua.sgkhmja.wboard.domain.*;
import ua.sgkhmja.wboard.service.dto.WriterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Writer and its DTO WriterDTO.
 */
@Mapper(componentModel = "spring", uses = {ReaderMapper.class, })
public interface WriterMapper extends EntityMapper <WriterDTO, Writer> {
    @Mapping(source = "reader.id", target = "readerId")
    WriterDTO toDto(Writer writer); 
    @Mapping(source = "readerId", target = "reader")
    Writer toEntity(WriterDTO writerDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Writer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Writer writer = new Writer();
        writer.setId(id);
        return writer;
    }
}
