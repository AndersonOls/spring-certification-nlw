package com.anderson.certificationnlw.modules.students.controllers;

import com.anderson.certificationnlw.modules.students.dto.StudentCertificatrionAnswersDTO;
import com.anderson.certificationnlw.modules.students.dto.VerifyHasCertificationDTO;
import com.anderson.certificationnlw.modules.students.entities.CertificationStudentEntity;
import com.anderson.certificationnlw.modules.students.useCases.StudentCertificatrionAnswersUseCase;
import com.anderson.certificationnlw.modules.students.useCases.VerifyIfHasCertificationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    @Autowired
    private StudentCertificatrionAnswersUseCase studentCertificatrionAnswersUseCase;


    @PostMapping("/verifyIfHasCertification")
    public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO){
        var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
        if(result){
            return "Usuario já fez a prova";
        }
        return "Usuario pode fazer a prova";

    }

    @PostMapping("/certification/answer")
    public ResponseEntity<Object> certificationAnswer(
            @RequestBody StudentCertificatrionAnswersDTO studentCertificatrionAnswersDTO) {
        try {
            var result = studentCertificatrionAnswersUseCase.execute(studentCertificatrionAnswersDTO);
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
