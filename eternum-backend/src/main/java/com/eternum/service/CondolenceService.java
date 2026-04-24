package com.eternum.service;

import com.eternum.dto.CondolenceDTO;
import com.eternum.entity.Condolence;
import com.eternum.entity.Memorial;
import com.eternum.entity.User;
import com.eternum.repository.CondolenceRepository;
import com.eternum.repository.MemorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CondolenceService {

    private final CondolenceRepository condolenceRepository;
    private final MemorialRepository memorialRepository;

    @Transactional
    public Condolence createCondolence(Memorial memorial, User user, CondolenceDTO.CreateRequest request) {
        Condolence condolence = new Condolence();
        condolence.setMemorial(memorial);
        if (user != null) {
            condolence.setAuthorUser(user);
        }
        condolence.setAuthorName(request.getAuthorName());
        condolence.setAuthorEmail(request.getAuthorEmail() != null ? request.getAuthorEmail() : "");
        condolence.setMessage(request.getMessage());

        return condolenceRepository.save(condolence);
    }

    @Transactional(readOnly = true)
    public List<Condolence> findByMemorial(Integer memorialId) {
        return condolenceRepository.findByFkMemorialOrderByCreatedDateDesc(memorialId);
    }

    @Transactional(readOnly = true)
    public Page<Condolence> findApprovedByMemorial(Integer memorialId, Pageable pageable) {
        return condolenceRepository.findByFkMemorialAndIsApprovedTrueOrderByCreatedDateDesc(memorialId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Condolence> findById(Integer id) {
        return condolenceRepository.findById(id);
    }

    @Transactional
    public Condolence approveCondolence(Integer condolenceId) {
        Condolence condolence = condolenceRepository.findById(condolenceId)
                .orElseThrow(() -> new RuntimeException("Condolencia no encontrada"));
        condolence.setIsApproved(true);
        return condolenceRepository.save(condolence);
    }

    @Transactional
    public void deleteCondolence(Integer condolenceId) {
        condolenceRepository.deleteById(condolenceId);
    }

    public CondolenceDTO.Response toResponse(Condolence condolence) {
        return CondolenceDTO.Response.builder()
                .pkCondolence(condolence.getPkCondolence())
                .fkMemorial(condolence.getFkMemorial())
                .fkAuthorUser(condolence.getFkAuthorUser())
                .authorName(condolence.getAuthorName())
                .authorEmail(condolence.getAuthorEmail())
                .message(condolence.getMessage())
                .isApproved(condolence.getIsApproved())
                .createdDate(condolence.getCreatedDate())
                .build();
    }

    public List<CondolenceDTO.Response> toResponseList(List<Condolence> condolences) {
        return condolences.stream().map(this::toResponse).collect(Collectors.toList());
    }

}
