package com.example.springbootjsfexample.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class TypeDto {

    private Integer id;
    private String libelle;

}
