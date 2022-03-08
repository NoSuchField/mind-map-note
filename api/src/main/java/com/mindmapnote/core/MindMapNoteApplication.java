package com.mindmapnote.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan({"com.mindmapnote.core.mapper"})
public class MindMapNoteApplication {
    public static void main(String[] args) {
        SpringApplication.run(MindMapNoteApplication.class, args);
    }
}
