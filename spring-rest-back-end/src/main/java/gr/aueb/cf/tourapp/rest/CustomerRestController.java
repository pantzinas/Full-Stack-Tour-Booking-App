package gr.aueb.cf.tourapp.rest;

import gr.aueb.cf.tourapp.core.exceptions.*;
import gr.aueb.cf.tourapp.core.filters.CustomerFilters;
import gr.aueb.cf.tourapp.core.filters.Paginated;
import gr.aueb.cf.tourapp.dto.CustomerInsertDTO;
import gr.aueb.cf.tourapp.dto.CustomerReadOnlyDTO;
import gr.aueb.cf.tourapp.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CustomerRestController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerRestController.class);
    private final CustomerService customerService;

    @Operation(
            summary = "Save a customer",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer inserted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/customers/save")
    public ResponseEntity<CustomerReadOnlyDTO> saveCustomer(
            @Valid @RequestBody CustomerInsertDTO insertDTO, BindingResult bindingResult)
            throws AppObjectInvalidArgumentException, ValidationException, AppObjectAlreadyExistsException, AppServerException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        CustomerReadOnlyDTO customerReadOnlyDTO = customerService.saveCustomer(insertDTO);
        return new ResponseEntity<>(customerReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all customers ",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customers Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
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
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerReadOnlyDTO>> getAllCustomers() throws AppObjectNotFoundException, AppServerException {

        List<CustomerReadOnlyDTO> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all customers paginated",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customers Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
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
    @GetMapping("/customers/paginated")
    public ResponseEntity<Page<CustomerReadOnlyDTO>> getPaginatedCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size
    ) throws AppObjectNotFoundException, AppServerException {

        Page<CustomerReadOnlyDTO> paginatedCustomers = customerService.getPaginatedCustomers(page, size);
        return new ResponseEntity<>(paginatedCustomers, HttpStatus.OK);
    }

    @Operation(
            summary = "Get customers filtered",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
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
    @PostMapping("/customers/filtered")
    public ResponseEntity<List<CustomerReadOnlyDTO>> getFilteredCustomers(
            @Nullable @RequestBody CustomerFilters filters,
            Principal principal
            ) throws AppObjectNotFoundException, AppObjectNotAuthorizedException {

        List<CustomerReadOnlyDTO> customers = customerService.getCustomersFiltered(filters);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Operation(
            summary = "Get filtered and paginated customers",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerReadOnlyDTO.class)
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
    @PostMapping("/customers/filtered/paginated")
    public ResponseEntity<Paginated<CustomerReadOnlyDTO>> getPaginatedFilteredCustomers(
            @Nullable @RequestBody CustomerFilters filters,
            Principal principal
    ) throws AppObjectNotFoundException, AppObjectNotAuthorizedException {

        Paginated<CustomerReadOnlyDTO> paginatedCustomers = customerService.getCustomersFilteredPaginated(filters);
        return new ResponseEntity<>(paginatedCustomers, HttpStatus.OK);
    }

}
