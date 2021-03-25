package ru.smn.poker.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smn.poker.entities.HandEntity;
import ru.smn.poker.repository.HandRepository;

@Service
@RequiredArgsConstructor
public class HandService {
    private final HandRepository handRepository;

    public Long getNewHandId(){
        return handRepository.save(new HandEntity()).getId();
    }
}
