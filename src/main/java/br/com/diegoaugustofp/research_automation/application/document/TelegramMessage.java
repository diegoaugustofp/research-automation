package br.com.diegoaugustofp.research_automation.application.document;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "telegram_messages")
@Data
@NoArgsConstructor
public class TelegramMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private Long messageId;

    private String senderName;

    @Column(nullable = false)
    private LocalDateTime eventTimestamp;

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

}
