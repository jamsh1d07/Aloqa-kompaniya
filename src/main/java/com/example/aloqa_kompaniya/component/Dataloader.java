package com.example.aloqa_kompaniya.component;

import com.example.aloqa_kompaniya.entity.Employee;
import com.example.aloqa_kompaniya.entity.Role;
import com.example.aloqa_kompaniya.repository.EmployeeRepository;
import com.example.aloqa_kompaniya.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Dataloader implements CommandLineRunner {

    final RoleRepository roleRepository;
    final EmployeeRepository employeeRepository;
    final PasswordEncoder passwordEncoder;
    @Value("${spring.sql.init.mode}")
    private String mode;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    public void run(String... args) throws Exception {




        if (ddl.equals("create") && mode.equals("always")) {
            Role director =roleRepository.save(new Role(1,"DIRECTOR"));
            Role manager=roleRepository.save(new Role(2,"MANAGER"));
            Role user=roleRepository.save(new Role(3,"USER"));
            employeeRepository.save(new Employee("Jamshid",passwordEncoder.encode("0000"),"jamshidjondev@gmail.com",director,true));

        }
    }
}
