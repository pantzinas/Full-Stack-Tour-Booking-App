package gr.aueb.cf.tourapp.rest;

import gr.aueb.cf.tourapp.core.exceptions.*;
import gr.aueb.cf.tourapp.core.filters.GuideFilters;
import gr.aueb.cf.tourapp.core.filters.Paginated;
import gr.aueb.cf.tourapp.dto.GuideInsertDTO;
import gr.aueb.cf.tourapp.dto.GuideReadOnlyDTO;
import gr.aueb.cf.tourapp.service.GuideService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class GuideRestController {

    private final static Logger LOGGER = LoggerFactory.getLogger(GuideRestController.class);
    private final GuideService guideService;

    @Operation(
            summary = "Save a guide",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Guide inserted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GuideReadOnlyDTO.class)
                            )
                    )
            }
    )
    @PostMapping("/guides/save")
    public ResponseEntity<GuideReadOnlyDTO> saveGuide(
            @Valid @RequestBody GuideInsertDTO guideInsertDTO,
            BindingResult bindingResult
            ) throws AppObjectInvalidArgumentException, ValidationException, AppObjectAlreadyExistsException, AppServerException {

        if (bindingResult.hasErrors()) {
            LOGGER.warn("The guide could not be inserted");
            throw new ValidationException(bindingResult);
        }

        GuideReadOnlyDTO guideReadOnlyDTO = guideService.saveGuide(guideInsertDTO);

        return new ResponseEntity<>(guideReadOnlyDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all guides",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Guide Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GuideReadOnlyDTO.class)
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
    @GetMapping("/guides")
    public ResponseEntity<List<GuideReadOnlyDTO>> getAllGuides() {
        List<GuideReadOnlyDTO> guides = guideService.getAllGuides();
        return new ResponseEntity<>(guides, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all guides paginated",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Guide Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GuideReadOnlyDTO.class)
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
    @GetMapping("/guides/paginated")
    public ResponseEntity<Page<GuideReadOnlyDTO>> getPaginatedGuides(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        Page<GuideReadOnlyDTO> guidesPage = guideService.getPaginatedGuides(page, size);
        return new ResponseEntity<>(guidesPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Get guides filtered",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Guides Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GuideReadOnlyDTO.class)
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
    @PostMapping("/guides/filtered")
    public ResponseEntity<List<GuideReadOnlyDTO>> getFilteredGuides(@Nullable @RequestBody GuideFilters filters,
                                                                    Principal principal)
        throws AppObjectNotFoundException, AppObjectNotAuthorizedException {

        try {
            if (filters == null) filters = GuideFilters.builder().build();
            return new ResponseEntity<>(guideService.getGuidesFiltered(filters), HttpStatus.OK);
        } catch(Exception e) {
            LOGGER.warn("Guides could not be retrieved");
            throw e;
        }

    }

    @Operation(
            summary = "Get paginated and filtered guides",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Guides Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GuideReadOnlyDTO.class)
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
    @PostMapping("/guides/filtered/paginated")
    public ResponseEntity<Paginated<GuideReadOnlyDTO>> getPaginatedFilteredGuides(@Nullable @RequestBody GuideFilters filters,
                                                                                  Principal principal)
        throws AppObjectNotFoundException ,AppObjectNotAuthorizedException {

        try {
            if (filters == null) filters = GuideFilters.builder().build();
            return new ResponseEntity<>(guideService.getGuidesFilteredPaginated(filters), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warn("Guides could not be retrieved");
            throw e;
        }
    }

}
