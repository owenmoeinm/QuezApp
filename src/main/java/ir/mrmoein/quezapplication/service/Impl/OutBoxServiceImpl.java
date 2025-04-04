package ir.mrmoein.quezapplication.service.Impl;

import ir.mrmoein.quezapplication.controller.admin.AdminController;
import ir.mrmoein.quezapplication.model.entity.OutboxEvent;
import ir.mrmoein.quezapplication.repository.elastic.SearchStudent;
import ir.mrmoein.quezapplication.repository.elastic.SearchTeacher;
import ir.mrmoein.quezapplication.repository.jpa.OutBoxRepository;
import ir.mrmoein.quezapplication.service.OutBoxService;
import ir.mrmoein.quezapplication.util.DTOService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutBoxServiceImpl implements OutBoxService {

    private final OutBoxRepository repository;
    private final SearchTeacher searchTeacher;
    private final SearchStudent searchStudent;
    private final DTOService dtoService;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public OutBoxServiceImpl(OutBoxRepository repository, SearchTeacher searchTeacher, SearchStudent searchStudent, DTOService dtoService) {
        this.repository = repository;
        this.searchTeacher = searchTeacher;
        this.searchStudent = searchStudent;
        this.dtoService = dtoService;
    }

    @Override
    @Scheduled(fixedRate = 5000)
    @Transactional(rollbackOn = Exception.class)
    public void processOutboxEvents() {
        try {
            List<OutboxEvent> all = repository.findAll();
            List<OutboxEvent> teachers = all.stream().filter((e) -> "ROLE_TEACHER".equals(e.getRole())).toList();
            List<OutboxEvent> students = all.stream().filter((e) -> "ROLE_STUDENT".equals(e.getRole())).toList();
            for (OutboxEvent teacher : teachers) {
                searchTeacher.save(dtoService.getTeacherDoc(teacher));
            }
            for (OutboxEvent student : students) {
                searchStudent.save(dtoService.getStudentDoc(student));
            }
            repository.deleteAll();
            logger.info("insert data in elasticSearch. ");
        } catch (Exception e) {
            System.err.println("Failed save elastic: " + e.getMessage());
        }

    }
}
