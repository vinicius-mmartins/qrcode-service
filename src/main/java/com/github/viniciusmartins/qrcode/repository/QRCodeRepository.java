package com.github.viniciusmartins.qrcode.repository;

import com.github.viniciusmartins.qrcode.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, UUID> {

    Optional<QRCode> findByTxid(String txid);

}
