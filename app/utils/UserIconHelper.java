package utils;

import play.libs.Files;
import play.mvc.Http;
import utils.errorHandler.FileNameIsToLong;

import java.util.UUID;

public class UserIconHelper {
    public static Http.MultipartFormData.FilePart<Files.TemporaryFile> getIconFromRequest(Http.Request request, String filedKey) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<Files.TemporaryFile> picture = body.getFile(filedKey);
        return  picture;
    }

    public static void validateIconFileName(String iconName) throws FileNameIsToLong {
        if (iconName.length() > 50) {
            throw new FileNameIsToLong(iconName);
        }
    }

    public static String generateUserIconFileName(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }
}
