package com.sideproject.spj001.util;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

@Component
public class EntityCheckRunner {

	@Bean
    public CommandLineRunner checkEntities(EntityManager entityManager) {
        return args -> {
            Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
            System.out.println("📌 掃描到的 @Entity 列表：");
            for (EntityType<?> entity : entities) {
                System.out.println("✅ " + entity.getName());
            }
        };
    }
}
