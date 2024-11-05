package com.github.viniciusmartins.qrcode.entity;

import com.github.viniciusmartins.qrcode.entity.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "qrcode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "txid", nullable = false)
    private String txid;
    @Column(name = "qrcode_value", nullable = false)
    private BigDecimal value;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusEnum status;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Column(name = "due_date")
    private LocalDate dueDate;

}
