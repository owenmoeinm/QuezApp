package ir.mrmoein.quezapplication.service;

import ir.mrmoein.quezapplication.model.dto.*;

import java.util.List;

public interface ExamService {

    List<ExamDTO> getExams();

    ExamDTO createExam(RequestExamDTO exam);

    boolean removeExam(String id);

    List<ExamQuestionDTO> getQuestionsExam(String id);

    ExamQuestionDTO createQuestion(RequestExamQuestionDTO dto);

    List<QuestionSearchDTO> searchQuestion(String query);

    ExamQuestionDTO selectedQuestion(String id , String exam);

    ExamQuestionDTO updateScore(String id , String score , String exam);

    void removeQuestion(String id , String exam);

}
