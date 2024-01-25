package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.inbound.CreateManager;
import jakarta.persistence.*;
import lombok.Data;
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
public class Manager implements Serializable {
    @Serial
    private static final long serialVersionUID = 5397338376573272269L;

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

    @Column
    private boolean isActive = true;

    @CreatedBy
    @Column
    private String createdBy;

    @LastModifiedBy
    @Column
    private String updatedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Manager create(@NonNull CreateManager createManager) {
        var entity = new Manager();
        entity.setFirstName(createManager.firstName());
        entity.setLastName(createManager.lastName());
        entity.setCpf(createManager.cpf());
        entity.setEmail(createManager.email());
        entity.setPhone(createManager.phone());
        entity.setCreatedBy(createManager.createdBy());

        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }
}
