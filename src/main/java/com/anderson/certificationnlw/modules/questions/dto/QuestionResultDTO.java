package com.anderson.certificationnlw.modules.questions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResultDTO {

    private UUID id;
    private String technology;
    private String description;

    private List<AlternativesResultDTO> alternatives;

}
