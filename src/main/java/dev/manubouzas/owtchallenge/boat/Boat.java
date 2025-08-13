package dev.manubouzas.owtchallenge.boat;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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

    /** Unique identifier for the boat (primary key). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the primary key
    private Long id;

    /** Name of the boat. Cannot be null. */
    @Column(nullable = false) // Cannot be null in DB
    private String name;

    /** Optional description of the boat. */
    private String description;

    /** Maximum capacity of the boat. Must be positive. */
    @Positive// Ensures capacity is positive
    private int capacity;

    /** Size of the boat (e.g., length in meters). */
    private int size;

    /** Type of the boat (e.g., SAILBOAT, MOTORBOAT). Cannot be null. */
    @Enumerated(EnumType.STRING)
    @NotNull (message = "Type cannot be null")// Ensures type is not null
    private BoatType type;

    /** Timestamp of when the boat was created. Set automatically on persist. */
    @Column(nullable = false, updatable = false)// Cannot be null and never updated after creation
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private OffsetDateTime createdAt;

    /** Timestamp of when the boat was last updated. Set automatically on update. */
    @Column(nullable = false)// Cannot be null, can be updated
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private OffsetDateTime updatedAt;

    /**
     * Sets creation and update timestamps before persisting.
     */
    @PrePersist
    public void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Updates the last updated timestamp before updating the entity.
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    /**
     * Updates this boat's fields from another boat instance.
     *
     * @param source the Boat object containing updated data
     */
    public void updateFrom(Boat source) {
        this.name = source.getName();
        this.description = source.getDescription();
        this.capacity = source.getCapacity();
        this.size = source.getSize();
        this.type = source.getType();
    }

}
