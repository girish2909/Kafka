package org.pih.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "failed_messages")
@Data
public class FailedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use TEXT type for large JSON payloads in PostgreSQL
    @Column(columnDefinition = "TEXT")
    private String payload;

    private String originalTopic;
    
    @Column(columnDefinition = "TEXT")
    private String exceptionReason;
    
    private LocalDateTime createdAt;
    
    private String status;
}