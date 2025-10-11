package com.snugplace.demo.Service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface ImageService {

    Map upload(MultipartFile image) throws Exception;
    Map delete(String imageId) throws Exception;
}
