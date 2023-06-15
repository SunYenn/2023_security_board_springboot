package com.example.back.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    /**
     * Spring 애플리케이션에서 사용할 ModelMapper를 구성하기 위한 설정 파일
     * @Bean으로 선언하여 ModelMapper를 빈으로 등록, ModelMapper의 설정 구성
     */
    @Bean
    public ModelMapper modelMapper() {

        // Java 객체 간의 매핑을 자동화하기 위해 사용되는 라이브러리
        // DTO와 Entity 사이의 데이터 전달을 단순화하고 편리하게 만들어주는 역할
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }
}

