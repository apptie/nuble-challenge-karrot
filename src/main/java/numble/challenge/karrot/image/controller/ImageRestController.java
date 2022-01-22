package numble.challenge.karrot.image.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.common.image.ImageStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageRestController {

    private final ImageStore imageStore;

    @GetMapping("/{filename}/{type}")
    public Resource processImage(@PathVariable String filename, @PathVariable(required = false) String type) throws MalformedURLException {
        if (filename == null || filename.equals("") || filename.equals("null")) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (type.equals("image")) {
            sb.append("file:").append(imageStore.getFullPath(filename));
        }
        else if (type.equals("profile")) {
            sb.append("file:").append(imageStore.getProfileFullPath(filename));
        }
        return new UrlResource(sb.toString());
    }

    @PostMapping("/upload")
    public String uploadImage(@ModelAttribute MultipartFile image) throws IOException {
        return imageStore.storeFile(image);
    }

    @PostMapping("/upload/profile")
    public String uploadProfile(@ModelAttribute MultipartFile profile) throws IOException {
        return imageStore.storeProfile(profile);
    }
}
