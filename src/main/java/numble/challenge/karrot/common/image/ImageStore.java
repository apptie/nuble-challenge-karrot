package numble.challenge.karrot.common.image;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageStore {

    @Value("${file.img.dir}")
    private String imgDir;

    @Value("${file.profile.dir}")
    private String profileDir;

    public String getFullPath(String filename) {
        StringBuilder sb = new StringBuilder();
        return sb.append(imgDir).append(filename).toString();
    }

    public String getProfileFullPath(String filename) {
        StringBuilder sb = new StringBuilder();
        return sb.append(profileDir).append(filename).toString();
    }

    public String storeProfile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        int width = image.getWidth();
        int height = image.getHeight();
        int standard = Math.min(width, height);

        Thumbnails.of(image).sourceRegion(Positions.CENTER, standard, standard)
                        .size(standard, standard)
                .toFile(getProfileFullPath(storeFileName));


        return storeFileName;
    }

    public String storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return storeFileName;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
