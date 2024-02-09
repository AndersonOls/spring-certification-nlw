package com.anderson.certificationnlw.modules.students.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCertificatrionAnswersDTO {
    private String email;
    private String technology;
    private List<QuestionAnswerDTO> questionAnswers;
}
