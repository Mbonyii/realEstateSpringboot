package com.example.realestate.controller;

import com.example.realestate.dto.ImageDto;
import com.example.realestate.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ImageDto>> getPropertyImages(@PathVariable Long propertyId) {
        List<ImageDto> images = imageService.findByPropertyId(propertyId);
        return ResponseEntity.ok(images);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ImageDto> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("propertyId") Long propertyId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "isPrimary", defaultValue = "false") boolean isPrimary) throws IOException {
        
        ImageDto uploadedImage = imageService.saveImage(file, propertyId, description, isPrimary);
        return new ResponseEntity<>(uploadedImage, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ImageDto> updateImage(
            @PathVariable Long id,
            @Valid @RequestBody ImageDto imageDto) {
        
        imageDto.setId(id);
        ImageDto updatedImage = imageService.update(imageDto);
        return ResponseEntity.ok(updatedImage);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/primary")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ImageDto> setPrimaryImage(@PathVariable Long id) {
        ImageDto primaryImage = imageService.setPrimaryImage(id);
        return ResponseEntity.ok(primaryImage);
    }
}