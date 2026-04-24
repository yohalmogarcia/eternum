package com.eternum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Integer pkNotification;
    private Integer fkUser;
    private String notificationType;
    private String title;
    private String message;
    private String relatedEntityType;
    private Integer relatedEntityId;
    private Boolean isRead;
    private LocalDateTime readDate;
    private LocalDateTime createdDate;

}
