package com.example.realestate.service;

import com.example.realestate.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    
    List<ImageDto> findByPropertyId(Long propertyId);
    
    ImageDto saveImage(MultipartFile file, Long propertyId, String description, boolean isPrimary) throws IOException;
    
    ImageDto update(ImageDto imageDto);
    
    void delete(Long id);
    
    ImageDto setPrimaryImage(Long id);
}