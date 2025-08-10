package dev.manubouzas.owtchallenge.boat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Represents a boat in the system.
 * Tracks its creation and last update times automatically.
 *
 * @author Manuel Bouzas
 */

@Entity // Marks this class as a JPA entity mapped to a database table
@Table // Optional: specifies the table name (defaults to "boat" if omitted)
@Data
public class Boat {

    @Id // Unique Id as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the primary key
    private Long id;

    @Column(nullable = false) // Cannot be null in DB
    private String name;

    private String description;

    @Positive// Ensures capacity is positive
    private int capacity;

    private int size;

    @Enumerated(EnumType.STRING)
    @NotNull (message = "Type cannot be null")// Ensures type is not null
    private BoatType type;

    @Column(nullable = false, updatable = false) // Cannot be null and never updated after creation
    private OffsetDateTime createdAt;

    @Column(nullable = false) // Cannot be null, can be updated
    private OffsetDateTime updatedAt;

    // Lifecycle callbacks
    @PrePersist
    public void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
    public void updateFrom(Boat source) {
        this.name = source.getName();
        this.description = source.getDescription();
        this.capacity = source.getCapacity();
        this.size = source.getSize();
        this.type = source.getType();
    }

}
