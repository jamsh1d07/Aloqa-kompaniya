package com.example.aloqa_kompaniya.service;

import com.example.aloqa_kompaniya.config.MailSender;
import com.example.aloqa_kompaniya.dto.ApiResponse;
import com.example.aloqa_kompaniya.dto.RegisterDto;
import com.example.aloqa_kompaniya.entity.Employee;
import com.example.aloqa_kompaniya.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
final EmployeeRepository employeeRepository;
    final PasswordEncoder passwordEncoder;
    final MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Object register(RegisterDto dto) throws MessagingException {
        boolean byUsername = employeeRepository.existsByUsername(dto.getUsername());
        if (byUsername) {
            return new ApiResponse("This username is already exist", false);
        }
        boolean byPhone = employeeRepository.existsByPhone(dto.getPhone());
        if (byPhone) {
            return new ApiResponse("This phone number is already exist", false);
        }
        boolean byEmail = employeeRepository.existsByEmail(dto.getEmail());
        if (byEmail) {
            return new ApiResponse("This email is already exist", false);
        }
        Employee employee = new Employee();
        if (dto.getEmail().contains("@")) {
            employee.setEmail(dto.getEmail());
        } else {
            employee.setPhone(dto.getPhone());
        }
        employee.setUsername(dto.getUsername());
        employee.setFullName(dto.getFullName());
        employee.setPassword(dto.getPassword());
        employee.setActive(true);

        //4 xonali
        String code = UUID.randomUUID().toString().substring(0, 5).concat(UUID.randomUUID().toString().substring(0, 5));

        SimpleMailMessage message = new SimpleMailMessage();
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.addHeader("content-type", "html/text");
        message.setFrom("pdp@gmail.com");
        message.setTo(dto.getEmail());
        message.setSubject("Tasdiqlash kodi");
        message.setText(code);
        message.setSentDate(new Date());
        mailSender.getEmail().send(message);
        employeeRepository.save(employee);
        return new ApiResponse("Mailga xabar ketti Tasdiqlang!", true);
    }

    public ApiResponse verify(String email, String emailCode) {
        Optional<Employee> optionalUser = employeeRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            Employee employee = optionalUser.get();
            if (employee.getEmailCode().equals(emailCode)){
                // enabledni true qilamiz
                employee.setActive(true);
                employeeRepository.save(employee);
                return new ApiResponse("Successfully verified",true,employee);
            }
        }
        return new ApiResponse("Something went wrong",false);
    }
}
