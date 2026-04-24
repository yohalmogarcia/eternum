package com.eternum.service;

import com.eternum.dto.MemorialDTO;
import com.eternum.entity.Memorial;
import com.eternum.entity.MemorialPhoto;
import com.eternum.entity.User;
import com.eternum.repository.MemorialPhotoRepository;
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
public class MemorialService {

    private final MemorialRepository memorialRepository;
    private final MemorialPhotoRepository photoRepository;

    @Transactional
    public Memorial createMemorial(User user, MemorialDTO.CreateRequest request) {
        Memorial memorial = new Memorial();
        memorial.setUser(user);
        memorial.setDeceasedName(request.getDeceasedName());
        memorial.setDeceasedLastName(request.getDeceasedLastName());
        memorial.setBirthDate(request.getBirthDate());
        memorial.setDeathDate(request.getDeathDate());
        memorial.setBiography(request.getBiography() != null ? request.getBiography() : "");
        memorial.setEpitaph(request.getEpitaph() != null ? request.getEpitaph() : "");
        memorial.setProfilePhotoUrl(request.getProfilePhotoUrl() != null ? request.getProfilePhotoUrl() : "");

        return memorialRepository.save(memorial);
    }

    @Transactional(readOnly = true)
    public Optional<Memorial> findById(Integer id) {
        return memorialRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Memorial> findByIdWithUser(Integer id) {
        return memorialRepository.findByIdWithUser(id);
    }

    @Transactional(readOnly = true)
    public Page<Memorial> findActiveMemorials(Pageable pageable) {
        return memorialRepository.findByIsActiveTrueOrderByCreatedDateDesc(pageable);
    }

    @Transactional(readOnly = true)
    public List<Memorial> findFeaturedMemorials() {
        return memorialRepository.findFeaturedMemorials();
    }

    @Transactional(readOnly = true)
    public List<Memorial> findByUser(Integer userId) {
        return memorialRepository.findByFkUser(userId);
    }

    @Transactional(readOnly = true)
    public List<Memorial> searchMemorials(String query) {
        return memorialRepository.searchMemorials(query);
    }

    @Transactional
    public Memorial updateMemorial(Integer memorialId, MemorialDTO.UpdateRequest request) {
        Memorial memorial = memorialRepository.findById(memorialId)
                .orElseThrow(() -> new RuntimeException("Memorial no encontrado"));

        if (request.getDeceasedName() != null) {
            memorial.setDeceasedName(request.getDeceasedName());
        }
        if (request.getDeceasedLastName() != null) {
            memorial.setDeceasedLastName(request.getDeceasedLastName());
        }
        if (request.getBirthDate() != null) {
            memorial.setBirthDate(request.getBirthDate());
        }
        if (request.getDeathDate() != null) {
            memorial.setDeathDate(request.getDeathDate());
        }
        if (request.getBiography() != null) {
            memorial.setBiography(request.getBiography());
        }
        if (request.getEpitaph() != null) {
            memorial.setEpitaph(request.getEpitaph());
        }
        if (request.getProfilePhotoUrl() != null) {
            memorial.setProfilePhotoUrl(request.getProfilePhotoUrl());
        }

        return memorialRepository.save(memorial);
    }

    @Transactional
    public void deleteMemorial(Integer memorialId) {
        Memorial memorial = memorialRepository.findById(memorialId)
                .orElseThrow(() -> new RuntimeException("Memorial no encontrado"));
        memorial.setIsActive(false);
        memorialRepository.save(memorial);
    }

    @Transactional
    public MemorialPhoto addPhoto(Integer memorialId, String photoUrl, String caption, boolean isMain) {
        Memorial memorial = memorialRepository.findById(memorialId)
                .orElseThrow(() -> new RuntimeException("Memorial no encontrado"));

        if (isMain) {
            photoRepository.clearMainPhotoFlag(memorialId);
        }

        MemorialPhoto photo = new MemorialPhoto();
        photo.setMemorial(memorial);
        photo.setPhotoUrl(photoUrl);
        photo.setCaption(caption != null ? caption : "");
        photo.setIsMainPhoto(isMain);

        long count = photoRepository.count();
        photo.setDisplayOrder((int) count + 1);

        return photoRepository.save(photo);
    }

    public MemorialDTO.Response toResponse(Memorial memorial) {
        List<MemorialPhoto> photos = photoRepository.findByFkMemorialOrderByDisplayOrderAsc(memorial.getPkMemorial());

        return MemorialDTO.Response.builder()
                .pkMemorial(memorial.getPkMemorial())
                .fkUser(memorial.getFkUser())
                .userEmail(memorial.getUser() != null ? memorial.getUser().getEmail() : null)
                .deceasedName(memorial.getDeceasedName())
                .deceasedLastName(memorial.getDeceasedLastName())
                .birthDate(memorial.getBirthDate())
                .deathDate(memorial.getDeathDate())
                .biography(memorial.getBiography())
                .epitaph(memorial.getEpitaph())
                .profilePhotoUrl(memorial.getProfilePhotoUrl())
                .isFeatured(memorial.getIsFeatured())
                .candleCount(memorial.getCandleCount())
                .isActive(memorial.getIsActive())
                .createdDate(memorial.getCreatedDate())
                .photos(photos.stream().map(this::toPhotoResponse).collect(Collectors.toList()))
                .build();
    }

    public MemorialDTO.ListResponse toListResponse(Memorial memorial) {
        MemorialPhoto mainPhoto = photoRepository.findMainPhotoByMemorialId(memorial.getPkMemorial());

        return MemorialDTO.ListResponse.builder()
                .pkMemorial(memorial.getPkMemorial())
                .deceasedName(memorial.getDeceasedName())
                .deceasedLastName(memorial.getDeceasedLastName())
                .birthDate(memorial.getBirthDate())
                .deathDate(memorial.getDeathDate())
                .epitaph(memorial.getEpitaph())
                .mainPhotoUrl(mainPhoto != null ? mainPhoto.getPhotoUrl() : memorial.getProfilePhotoUrl())
                .candleCount(memorial.getCandleCount())
                .createdDate(memorial.getCreatedDate())
                .build();
    }

    private MemorialDTO.PhotoResponse toPhotoResponse(MemorialPhoto photo) {
        return MemorialDTO.PhotoResponse.builder()
                .pkMemorialPhoto(photo.getPkMemorialPhoto())
                .photoUrl(photo.getPhotoUrl())
                .caption(photo.getCaption())
                .displayOrder(photo.getDisplayOrder())
                .isMainPhoto(photo.getIsMainPhoto())
                .build();
    }

}
