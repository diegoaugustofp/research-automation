package br.com.diegoaugustofp.research_automation.domain.document;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "telegram_messages")
@Getter
@Setter
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

}
