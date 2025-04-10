package ir.mrmoein.quezapplication.service;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public interface OutBoxService {

    void processOutboxEvents();

    void checkExpireExam();

}
