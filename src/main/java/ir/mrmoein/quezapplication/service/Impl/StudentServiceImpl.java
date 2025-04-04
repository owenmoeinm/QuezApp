package ir.mrmoein.quezapplication.service.Impl;

import ir.mrmoein.quezapplication.exception.NotFoundRequestException;
import ir.mrmoein.quezapplication.model.entity.Student;
import ir.mrmoein.quezapplication.repository.elastic.SearchStudent;
import ir.mrmoein.quezapplication.repository.jpa.StudentRepository;
import ir.mrmoein.quezapplication.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final SearchStudent searchRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, SearchStudent searchRepository) {
        this.studentRepository = studentRepository;
        this.searchRepository = searchRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean remove(String nationalCode) {
        try {
            Student student = studentRepository.findByNationalCode(nationalCode)
                    .orElseThrow(() -> new NotFoundRequestException("Student Not Found !!!"));
            studentRepository.deleteStudentFromJoinTable(student.getId());
            studentRepository.deleteByNationalCode(student.getNationalCode());
            searchRepository.deleteByNationalCode(student.getNationalCode());
            return true;
        } catch (Exception e) {
            throw new NotFoundRequestException(" delete failed!!! " + e.getMessage());
        }
    }
}
