package fr.gilles.auth.services.files;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class FileService {

    private final Cloudinary cloudinary;
    private final String[] imageExtensions = new String[]{"jpg","jpeg","png"};
    private final String[] videoExtensions = new String[]{"mp4", "mkv"};

    public Map uploadImage(String folder, MultipartFile file) throws IOException {
        if(isImage(file)){
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", folder));
        }
        return null;
    }



    public boolean isImage(@NotNull MultipartFile file) {
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        return containsExtension(extension, imageExtensions);
    }




    public boolean containsExtension(@NotNull String extension, String[] extensions) {
        for (String ext : extensions) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        return false;
    }


}