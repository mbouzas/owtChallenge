package dev.manubouzas.owtchallenge.boat;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Boat entities.
 * Extends JpaRepository to provide CRUD operations.
 *
 * @author Manuel Bouzas
 */
public interface BoatRepository extends JpaRepository<Boat, Long> {
    // No additional methods needed; JpaRepository provides all necessary CRUD operations
}
