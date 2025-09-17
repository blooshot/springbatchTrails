package com.krishnadev.InventoryManagement.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beaner {

    @Bean
    public BaseObjectTranslator baseObjectTranslator(){
        return new BaseObjectTranslator(new ModelMapper());
    }
}
