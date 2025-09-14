package gr.aueb.cf.tourapp.rest;

import gr.aueb.cf.tourapp.core.exceptions.*;
import gr.aueb.cf.tourapp.dto.BookingInsertDTO;
import gr.aueb.cf.tourapp.dto.BookingReadOnlyDTO;
import gr.aueb.cf.tourapp.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookingRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingRestController.class);
    private final BookingService bookingService;

    @Operation(
            summary = "Save a booking",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking inserted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookingReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/customers/bookings/save")
    public ResponseEntity<BookingReadOnlyDTO> saveBooking(
            @Valid @RequestBody BookingInsertDTO dto,
            BindingResult bindingResult
            ) throws AppObjectAlreadyExistsException, AppObjectNotAuthorizedException, AppObjectInvalidArgumentException, ValidationException {

        if (bindingResult.hasErrors()) {
            LOGGER.warn("The booking could not be inserted", bindingResult);
            throw new ValidationException(bindingResult);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        LOGGER.info("username: {}", username);

        try {
            BookingReadOnlyDTO bookingReadOnlyDTO = bookingService.saveBooking(dto, username);
            LOGGER.info("Booking saved: {}", bookingReadOnlyDTO.getBookingDate());
            return new ResponseEntity<>(bookingReadOnlyDTO, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error while saving booking", e);
            throw e;
        }

    }

    @Operation(
            summary = "Update the booking after guide found",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookingReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PutMapping("/guides/bookings/save/{id}")
    public ResponseEntity<BookingReadOnlyDTO> updateBookingGuide(
            @PathVariable Long id
    ) throws AppObjectNotFoundException, AppObjectNotAuthorizedException, AppObjectAlreadyExistsException, AppServerException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            BookingReadOnlyDTO bookingReadOnlyDTO = bookingService.updateBookingGuide(id, username);
            return new ResponseEntity<>(bookingReadOnlyDTO, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn("The booking could not be updated", e);
            throw e;
        }
    }

    @Operation(
            summary = "Get all bookings",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookingReadOnlyDTO.class)
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
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingReadOnlyDTO>> getAllBookings() {
        List<BookingReadOnlyDTO> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all bookings of a customer",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookingReadOnlyDTO.class)
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
    @GetMapping("/customers/bookings")
    public ResponseEntity<List<BookingReadOnlyDTO>> getBookingsOfCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            List<BookingReadOnlyDTO> bookings = bookingService.findBookingByBookingDateAndCustomerUsername(username);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn("We could not find any current booking for the specific customer", e);
            throw e;
        }
    }

    @Operation(
            summary = "Get available bookings without a guide",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookingReadOnlyDTO.class)
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
    @GetMapping("/guides/available-bookings")
    public ResponseEntity<List<BookingReadOnlyDTO>> getBookingsWithoutGuide() {

        try {
            List<BookingReadOnlyDTO> bookings = bookingService.findBookingByBookingDateAndGuideIsNull();
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn("We could not find any current booking for the specific customer", e);
            throw e;
        }
    }

    @Operation(
            summary = "Get bookings of a guide",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookingReadOnlyDTO.class)
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
    @GetMapping("/guides/bookings")
    public ResponseEntity<List<BookingReadOnlyDTO>> getBookingsofAGuide() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            List<BookingReadOnlyDTO> bookings = bookingService.findBookingByGuideUsername(username);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn("We could not find any guided tour for the specific guide", e);
            throw e;
        }
    }

    @Operation(
            summary = "Get all bookings paginated",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookingReadOnlyDTO.class)
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
    @GetMapping("/bookings/paginated")
    public ResponseEntity<Page<BookingReadOnlyDTO>> getPaginatedBookings(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<BookingReadOnlyDTO> bookingsPage = bookingService.getPaginatedBookings(page, size);
        return new ResponseEntity<>(bookingsPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a booking",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Booking deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookingReadOnlyDTO.class)
                            )
                    )
            }
    )
    @DeleteMapping("/customers/bookings/delete/{id}")
    public ResponseEntity<BookingReadOnlyDTO> deleteBookingById(
            @PathVariable Long id
    ) throws AppObjectNotFoundException, AppObjectNotAuthorizedException, AppObjectAlreadyExistsException, AppServerException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            BookingReadOnlyDTO bookingToBeDeleted = bookingService.findBookingById(id);
            bookingService.deleteBookingById(id, username);
            return new ResponseEntity<>(bookingToBeDeleted, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn("The booking could not be deleted", e);
            throw e;
        }
    }
}
