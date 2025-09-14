package gr.aueb.cf.tourapp.rest;

import gr.aueb.cf.tourapp.core.exceptions.*;
import gr.aueb.cf.tourapp.dto.TourInsertDTO;
import gr.aueb.cf.tourapp.dto.TourReadOnlyDTO;
import gr.aueb.cf.tourapp.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TourRestController {

    public static final Logger LOGGER = LoggerFactory.getLogger(TourRestController.class);
    public final TourService tourService;

    @Operation(
            summary = "Save a tour",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tour inserted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TourReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/tours/save")
    public ResponseEntity<TourReadOnlyDTO> saveTour(
            @Valid @RequestBody TourInsertDTO insertDTO,
            BindingResult bindingResult
            ) throws AppObjectAlreadyExistsException, AppObjectInvalidArgumentException, ValidationException, AppServerException {

        if (bindingResult.hasErrors()) {
            LOGGER.warn("The tour could not be inserted");
            throw new ValidationException(bindingResult);
        }

        TourReadOnlyDTO tourReadOnlyDTO = tourService.saveTour(insertDTO);

        return new ResponseEntity<>(tourReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all tours",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tours Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TourReadOnlyDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access Denied",
                            content = @Content
                    )
            }
    )
    @GetMapping("/tours/all")
    public ResponseEntity<List<TourReadOnlyDTO>> getAllTours() {
        List<TourReadOnlyDTO> tours = tourService.getAllTours();
        return new ResponseEntity<>(tours, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a tour",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tour deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TourReadOnlyDTO.class)
                            )
                    )
            }
    )
    @DeleteMapping("/tours/delete/{id}")
    public ResponseEntity<TourReadOnlyDTO> deleteTourById(
            @PathVariable Long id
    ) throws AppObjectNotFoundException, AppObjectNotAuthorizedException, AppObjectAlreadyExistsException, AppServerException {

        try {
            tourService.deleteByTourId(id);
            return new ResponseEntity<>(tourService.findTourById(id), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn("The tour could not be deleted", e);
            throw e;
        }
    }
}
