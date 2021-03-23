package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.entities.TableEntity;
import ru.smn.poker.repository.TableRepository;

@Service
@RequiredArgsConstructor
public class TableService {
    private final TableRepository tableRepository;

    @Transactional
    public void save(TableEntity tableEntity){
        tableRepository.save(tableEntity);
    }
}
