package com.sevenb.recipes_manager.service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${CLOUDINARY_URL}") String cloudinaryUrl) {
        this.cloudinary = new Cloudinary(cloudinaryUrl);
    }

    public String upload(MultipartFile file) throws IOException {
        Map<?,?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) result.get("secure_url");
    }

    // Nuevo método para subir desde byte[]
    public String upload(byte[] bytes) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap());
        return (String) result.get("secure_url");
    }
}
