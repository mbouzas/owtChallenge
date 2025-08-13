package dev.manubouzas.owtchallenge.boat;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BoatController handles CRUD operations for boats.
 * It is secured to allow only users with the 'USER' role to access its endpoints.
 *
 * @author Manuel Bouzas
 */
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/boats")
public class BoatController {
    // Repository for managing Boat entities
    private final BoatRepository boatRepository;

    /**
     * Constructor to inject the BoatRepository dependency.
     *
     * @param boatRepository the repository used to access Boat data
     */
    public BoatController(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    /**
     * Retrieve all boats.
     *
     * @return a list of all Boat entities
     */
    @GetMapping
    public List<Boat> getAllBoats() {
        return boatRepository.findAll();
    }

    /**
     * Retrieve a boat by its ID.
     *
     * @param id the ID of the boat to retrieve
     * @return ResponseEntity containing the found Boat, or 404 Not Found if no boat exists with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Boat> getBoatById(@PathVariable Long id) {
        return boatRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new boat.
     *
     * @param boat the Boat object to create
     * @return ResponseEntity containing the created Boat with HTTP status 201 Created
     */
    @PostMapping
    public ResponseEntity<Boat> createBoat(@Valid @RequestBody Boat boat) {
        Boat savedBoat = boatRepository.save(boat);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBoat);
    }

    /**
     * Update an existing boat by its ID.
     *
     * @param id the ID of the boat to update
     * @param boatDetails the Boat object containing updated data
     * @return ResponseEntity containing the updated Boat, or 404 Not Found if no boat exists with the given ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<Boat> updateBoat(@PathVariable Long id, @Valid @RequestBody Boat boatDetails) {
        return boatRepository.findById(id)
                .map(boat -> {
                    boat.updateFrom(boatDetails);
                    return ResponseEntity.ok(boatRepository.save(boat));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a boat by its ID.
     *
     * @param id the ID of the boat to delete
     * @return ResponseEntity with HTTP status 204 No Content if deleted, or 404 Not Found if no boat exists with the given ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoat(@PathVariable Long id) {
        if (boatRepository.existsById(id)) {
            boatRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
