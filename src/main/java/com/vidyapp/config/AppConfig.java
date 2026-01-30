package com.vidyapp.config;

import com.vidyapp.dtos.GradeDTO;
import com.vidyapp.models.Grade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.modelmapper.ModelMapper;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configure mapping from Grade Entity -> GradeDTO
        modelMapper.typeMap(Grade.class, GradeDTO.class).addMappings(mapper -> {
            // Map Grade.getBatch().getBatchId() -> GradeDTO.setBatchId()
            mapper.map(src -> src.getBatch().getBatchId(), GradeDTO::setBatchId);
        });

        return modelMapper;
    }
}