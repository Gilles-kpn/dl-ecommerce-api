package fr.gilles.dleapi.services.files;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fr.gilles.dleapi.payloader.response.ExceptionMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FileService {

    private final Cloudinary cloudinary;
    private final String[] imageExtensions = new String[]{"jpg","jpeg","png"};
    private final String[] videoExtensions = new String[]{"mp4", "mkv"};

    public Map<String, Object> uploadImage(String folder, MultipartFile file) throws IOException {
        if(isImage(file)){
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", folder));
        }
        throw new IOException(ExceptionMessage.INVALID_FORMAT);
    }



    public boolean isImage(@NotNull MultipartFile file) {
        String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(
                Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".") + 1);
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
