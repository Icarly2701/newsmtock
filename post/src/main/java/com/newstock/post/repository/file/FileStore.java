package com.newstock.post.repository.file;

import com.newstock.post.domain.post.PostContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

    @Value("${aws.bucket-name}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.access-key}")
    private String accessKey;
    @Value("${aws.secret-key}")
    private String secretKey;
    @Value("${file.dir}")
    private String fileDir;

    public String[] getFullPath(String fileName){
        LocalDate currentDate = LocalDate.now();

        String fileDirectoryUnique = String.format("%d\\%02d%02d", currentDate.getYear(),
                currentDate.getMonthValue(),
                currentDate.getDayOfMonth());

        String fileDirectoryUniqueSlash = String.format("%d/%02d%02d", currentDate.getYear(),
                currentDate.getMonthValue(),
                currentDate.getDayOfMonth());

        String fileDirectory = fileDir + fileDirectoryUnique;

        File folder = new File(fileDirectory);
        if(!folder.exists()){
            try{
                folder.mkdirs();
            }catch(Exception e){
                e.getStackTrace();
            }
        }
        return new String[]{"post_image/" + fileDirectoryUniqueSlash + "/" + fileName, fileDirectory + "\\" + fileName};
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){
            if(!multipartFile.isEmpty()){
                try {
                    storeFileResult.add(storeFileS3(multipartFile));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException
    {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String[] fullPath = getFullPath(storeFileName);
        multipartFile.transferTo(new File(fullPath[1]));
        return new UploadFile(originalFilename, storeFileName, fullPath[0]);
    }

    public UploadFile storeFileS3(MultipartFile multipartFile) throws IOException
    {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String[] fullPath = getFullPath(storeFileName);

        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .build();

        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fullPath[0])
                .build(), RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        String filePath = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fullPath[0];

        return new UploadFile(originalFilename, storeFileName, filePath);
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
