package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.repository.HandRepository;
import ru.smn.poker.repository.TableRepository;
import ru.smn.poker.service.HandService;

@Service
@RequiredArgsConstructor
public class SimpleHandService implements HandService {
    private final HandRepository handRepository;
    private final TableRepository tableRepository;

    @Transactional
    public Long getNewHandId(Long tableId){
        return handRepository.save(HandEntity.builder()
                .table(tableRepository.findById(tableId).orElseThrow())
                .build()).getId();
    }
}
