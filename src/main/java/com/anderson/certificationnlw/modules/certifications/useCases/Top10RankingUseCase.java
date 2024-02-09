package com.anderson.certificationnlw.modules.certifications.useCases;

import com.anderson.certificationnlw.modules.students.entities.CertificationStudentEntity;
import com.anderson.certificationnlw.modules.students.repositories.CertificationStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Top10RankingUseCase {

    @Autowired
    CertificationStudentRepository certificationStudentRepository;
    public List<CertificationStudentEntity> execute(){
       return this.certificationStudentRepository.findTop10ByOrderByGradeDesc();
    }
}
