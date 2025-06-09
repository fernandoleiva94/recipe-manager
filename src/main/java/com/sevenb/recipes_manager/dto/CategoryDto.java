package com.sevenb.recipes_manager.dto;


import jakarta.persistence.Transient;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
        private Long id;
        private String description;
        @Transient
        private Long userId;


        public CategoryDto(Long id, String description) {
                this.id = id;
                this.description = description;
        }
}


