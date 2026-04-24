package com.eternum.service;

import com.eternum.dto.VirtualCandleDTO;
import com.eternum.entity.Memorial;
import com.eternum.entity.User;
import com.eternum.entity.VirtualCandle;
import com.eternum.repository.MemorialRepository;
import com.eternum.repository.VirtualCandleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VirtualCandleService {

    private final VirtualCandleRepository candleRepository;
    private final MemorialRepository memorialRepository;

    @Transactional
    public VirtualCandle lightCandle(Memorial memorial, User user, String message) {
        VirtualCandle candle = new VirtualCandle();
        candle.setMemorial(memorial);
        if (user != null) {
            candle.setUser(user);
        }
        candle.setCandleMessage(message != null ? message : "");

        VirtualCandle saved = candleRepository.save(candle);

        // Incrementar contador en el memorial
        memorialRepository.incrementCandleCount(memorial.getPkMemorial());

        return saved;
    }

    @Transactional(readOnly = true)
    public List<VirtualCandle> findByMemorial(Integer memorialId) {
        return candleRepository.findByFkMemorialOrderByLitDateDesc(memorialId);
    }

    @Transactional(readOnly = true)
    public Long countByMemorial(Integer memorialId) {
        return candleRepository.countByMemorialId(memorialId);
    }

    public VirtualCandleDTO.Response toResponse(VirtualCandle candle) {
        return VirtualCandleDTO.Response.builder()
                .pkVirtualCandle(candle.getPkVirtualCandle())
                .fkMemorial(candle.getFkMemorial())
                .fkUser(candle.getFkUser())
                .candleMessage(candle.getCandleMessage())
                .litDate(candle.getLitDate())
                .createdDate(candle.getCreatedDate())
                .build();
    }

    public List<VirtualCandleDTO.Response> toResponseList(List<VirtualCandle> candles) {
        return candles.stream().map(this::toResponse).collect(Collectors.toList());
    }

}
