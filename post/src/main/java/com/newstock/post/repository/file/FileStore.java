package com.newstock.post.repository.file;

import com.newstock.post.domain.post.PostContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

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
        return new String[]{"/post_image/" + fileDirectoryUniqueSlash + "/" + fileName, fileDirectory + "\\" + fileName};
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){
            if(!multipartFile.isEmpty()){
                try {
                    storeFileResult.add(storeFile(multipartFile));
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
