package com.financetrackerapp.service;

import com.financetrackerapp.entity.EmailOtp;
import com.financetrackerapp.repository.EmailOtpRepository;
import com.financetrackerapp.security.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private EmailOtpRepository emailOtpRepository;

    @Autowired
    private EmailService emailService;

    public void generateAndSendOtp(String email) {

        String otp = OtpUtil.generateOtp();

        EmailOtp emailOtp = emailOtpRepository
                .findTopByEmailOrderByExpiryTimeDesc(email)
                .orElse(null);

        if (emailOtp != null) {

            // 🔁 RESEND COOLDOWN (60 sec)
            if (emailOtp.getLastSentTime() != null &&
                    emailOtp.getLastSentTime().plusSeconds(60).isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Please wait before requesting new OTP");
            }

        } else {
            emailOtp = new EmailOtp();
            emailOtp.setEmail(email);
        }

        // 🔄 overwrite old OTP (important)
        emailOtp.setOtp(otp);
        emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        emailOtp.setVerified(false);
        emailOtp.setLastSentTime(LocalDateTime.now());

        emailOtpRepository.save(emailOtp);

        emailService.sendOtpEmail(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {

        EmailOtp storedOtp = emailOtpRepository
                .findTopByEmailOrderByExpiryTimeDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        // ❌ Expired
        if (storedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        // ❌ Already verified (optional safety)
        if (storedOtp.isVerified()) {
            throw new RuntimeException("OTP already verified");
        }

        // ❌ Invalid
        if (!storedOtp.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // ✅ Success
        storedOtp.setVerified(true);
        emailOtpRepository.save(storedOtp);

        return true;
    }

}
