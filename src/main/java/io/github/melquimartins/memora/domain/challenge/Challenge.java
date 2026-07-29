package io.github.melquimartins.memora.domain.challenge;

import io.github.melquimartins.memora.domain.alternative.Alternative;
import io.github.melquimartins.memora.domain.module.Module;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @PrePersist
    protected void onCreate() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "multiplier")
    private int multiplier = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @OneToMany(
            mappedBy = "challenge",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Alternative> alternatives = new ArrayList<>();

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    @Column(name = "available_again_at")
    private LocalDateTime availableAgainAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Challenge() {
    }

    public Challenge(String title) {
        this.title = title;
    }
}
