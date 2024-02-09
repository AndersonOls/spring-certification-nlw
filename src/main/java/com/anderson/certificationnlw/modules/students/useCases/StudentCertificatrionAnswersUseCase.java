package com.anderson.certificationnlw.modules.students.useCases;

import com.anderson.certificationnlw.modules.questions.entities.QuestionEntity;
import com.anderson.certificationnlw.modules.questions.repositories.QuestionRepository;
import com.anderson.certificationnlw.modules.students.dto.StudentCertificatrionAnswersDTO;
import com.anderson.certificationnlw.modules.students.dto.VerifyHasCertificationDTO;
import com.anderson.certificationnlw.modules.students.entities.AnswerCertificationsEntity;
import com.anderson.certificationnlw.modules.students.entities.CertificationStudentEntity;
import com.anderson.certificationnlw.modules.students.entities.StudentEntity;
import com.anderson.certificationnlw.modules.students.repositories.CertificationStudentRepository;
import com.anderson.certificationnlw.modules.students.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCertificatrionAnswersUseCase {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CertificationStudentRepository certificationStudentRepository;

    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificatrionAnswersDTO dto) throws Exception {

       var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

       if(hasCertification){
           throw new Exception("Você já tirou sua certificação!");
       }

        List<QuestionEntity> questionEntity = questionRepository.findByTechnology(dto.getTechnology());
        List<AnswerCertificationsEntity> answerCertifications = new ArrayList<>();

        AtomicInteger correctAnswers = new AtomicInteger(0);

        dto.getQuestionAnswers()
                .stream().forEach(questionAnswer -> {
                    var question = questionEntity.stream()
                            .filter(q -> q.getId().equals(questionAnswer.getQuestionID())).findFirst().get();

                    var findCorrectAlternative = question.getAlternatives().stream()
                            .filter(alternative -> alternative.isCorrect()).findFirst().get();

                    if (findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())) {
                        questionAnswer.setCorrect(true);
                        correctAnswers.incrementAndGet();
                    } else {
                        questionAnswer.setCorrect(false);
                    }

                    var answersCerificationsEntity =  AnswerCertificationsEntity.builder()
                            .answerID(questionAnswer.getAlternativeID())
                            .quetionID(questionAnswer.getQuestionID())
                            .isCorrect(questionAnswer.isCorrect()).build();

                    answerCertifications.add(answersCerificationsEntity);
                });

        // Verificar se o estudadnte existe
        var student = studentRepository.findByEmail(dto.getEmail());
        UUID studentID;
        if (student.isEmpty()) {
            var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();
            studentCreated = studentRepository.save(studentCreated);
            studentID = studentCreated.getId();
        } else {
            studentID = student.get().getId();
        }


        CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder().
                technology(dto.getTechnology())
                .studentID(studentID)
                .grade(correctAnswers.get())
                .build();

        var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

        answerCertifications.stream().forEach( answerCertification ->{
            answerCertification.setCertificationID(certificationStudentEntity.getId());
            answerCertification.setCertificationStudentEntity(certificationStudentEntity);
        });

        certificationStudentEntity.setAnswerCertificationsEntities(answerCertifications);

        return certificationStudentCreated;
    }

}
