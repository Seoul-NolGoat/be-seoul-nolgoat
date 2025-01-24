package wad.seoul_nolgoat.service.s3;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wad.seoul_nolgoat.exception.ApplicationException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

import static wad.seoul_nolgoat.exception.ErrorCode.FILE_READ_FAILED;
import static wad.seoul_nolgoat.exception.ErrorCode.INVALID_FILE_URL_FORMAT;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = generateFileName(Objects.requireNonNull(originalFilename));

        String imageUrl;
        try {
            S3Resource s3Resource = s3Template.upload(
                    bucket,
                    fileName,
                    multipartFile.getInputStream(),
                    ObjectMetadata.builder()
                            .contentType(multipartFile.getContentType())
                            .build()
            );
            imageUrl = s3Resource.getURL().toString();
        } catch (IOException e) {
            throw new ApplicationException(FILE_READ_FAILED, e);
        }

        return imageUrl;
    }

    public void deleteFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            String key = path.substring(1);
            String decodedKey = URLDecoder.decode(key, StandardCharsets.UTF_8); // 한글 또는 특수문자가 있는 경우를 위해

            s3Template.deleteObject(bucket, decodedKey);
        } catch (MalformedURLException e) {
            throw new ApplicationException(INVALID_FILE_URL_FORMAT, e);
        }
    }

    private String generateFileName(String originalFilename) {
        return UUID.randomUUID() + "-" + originalFilename.replace(" ", "_");
    }
}
