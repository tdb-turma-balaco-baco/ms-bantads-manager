package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.inbound.CreateManager;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "managers")
@NoArgsConstructor
@AllArgsConstructor
public class Manager implements Serializable {
    @Serial private static final long serialVersionUID = 5397338376573272269L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "managers_id_seq")
    @SequenceGenerator(name = "managers_id_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column private boolean isActive = true;

    @CreatedBy
    @Column private String createdBy;

    @LastModifiedBy
    @Column private String updatedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Manager(@NonNull CreateManager createManager) {
        this.setFirstName(createManager.firstName());
        this.setLastName(createManager.lastName());
        this.setCpf(createManager.cpf());
        this.setEmail(createManager.email());
        this.setPhone(createManager.phone());
        this.setCreatedBy(createManager.createdBy());

        LocalDateTime now = LocalDateTime.now();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }
}
