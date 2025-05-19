package com.example.realestate.service.impl;

import com.example.realestate.dto.ImageDto;
import com.example.realestate.exception.FileStorageException;
import com.example.realestate.exception.ResourceNotFoundException;
import com.example.realestate.model.Image;
import com.example.realestate.model.Property;
import com.example.realestate.repository.ImageRepository;
import com.example.realestate.repository.PropertyRepository;
import com.example.realestate.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final PropertyRepository propertyRepository;

    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public List<ImageDto> findByPropertyId(Long propertyId) {
        List<Image> images = imageRepository.findByPropertyId(propertyId);
        return images.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ImageDto saveImage(MultipartFile file, Long propertyId, String description, boolean isPrimary) throws IOException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", propertyId));

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path targetLocation = uploadPath.resolve(fileName);

        try {
            // Copy file to the target location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName, ex);
        }

        // If this is set as primary, reset other primary images
        if (isPrimary) {
            List<Image> primaryImages = imageRepository.findByPropertyIdAndIsPrimaryTrue(propertyId);
            primaryImages.forEach(img -> {
                img.setPrimary(false);
                imageRepository.save(img);
            });
        }

        // Create image record in database
        Image image = new Image();
        image.setUrl(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString());
        image.setDescription(description);
        image.setPrimary(isPrimary);
        image.setProperty(property);

        Image savedImage = imageRepository.save(image);
        return convertToDto(savedImage);
    }

    @Override
    @Transactional
    public ImageDto update(ImageDto imageDto) {
        Image image = imageRepository.findById(imageDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageDto.getId()));

        image.setDescription(imageDto.getDescription());
        
        if (imageDto.isPrimary() && !image.isPrimary()) {
            // Reset other primary images for this property
            List<Image> primaryImages = imageRepository.findByPropertyIdAndIsPrimaryTrue(image.getProperty().getId());
            primaryImages.forEach(img -> {
                img.setPrimary(false);
                imageRepository.save(img);
            });
            image.setPrimary(true);
        }

        Image updatedImage = imageRepository.save(image);
        return convertToDto(updatedImage);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));
        
        // Extract the file name from the URL
        String fileUrl = image.getUrl();
        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        
        // Delete the file from storage
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file " + fileName, ex);
        }
        
        imageRepository.delete(image);
    }

    @Override
    @Transactional
    public ImageDto setPrimaryImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id));
        
        // Reset other primary images for this property
        List<Image> primaryImages = imageRepository.findByPropertyIdAndIsPrimaryTrue(image.getProperty().getId());
        primaryImages.forEach(img -> {
            if (!img.getId().equals(id)) {
                img.setPrimary(false);
                imageRepository.save(img);
            }
        });
        
        image.setPrimary(true);
        Image updatedImage = imageRepository.save(image);
        return convertToDto(updatedImage);
    }

    private ImageDto convertToDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setDescription(image.getDescription());
        dto.setPrimary(image.isPrimary());
        dto.setPropertyId(image.getProperty().getId());
        return dto;
    }
}