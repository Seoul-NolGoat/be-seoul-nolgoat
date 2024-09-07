package wad.seoul_nolgoat.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String filename = generateFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, filename).toString();
    }

    public void deleteFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            String key = path.substring(1);
            String decodedKey = URLDecoder.decode(key, StandardCharsets.UTF_8); // 한글 또는 특수문자가 있는 경우를 위해

            amazonS3.deleteObject(bucket, decodedKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3: " + fileUrl, e);
        }
    }

    private String generateFileName(String originalFilename) {
        return UUID.randomUUID() + "-" + originalFilename.replace(" ", "_");
    }
}
