package fr.gilles.dleapi.payloader.response;


public class ExceptionMessage {
    public static final String BAD_CREDENTIAL = "Bad Credential";
    public static final String NOT_FOUND = "Not Found";
    public static final String ACCOUNT_NOT_FOUND = "Account "+NOT_FOUND;
    public static final String PRODUCT_NOT_FOUND = "Product "+NOT_FOUND;
    public static final String USER_NOT_FOUND = "User NOT FOUND";
    public static final String PRODUCT_ALREADY_LIKED ="Product Already Liked";
    public static final String NO_MATCHES_PASSWORD = "Password Not Match";
    public static final  String ALREADY_REGISTER_EMAIL ="Email Already Register";
    public static final String INTERNAL_ERROR = "Something went wrong";
    public static final String UNAUTHORIZED = "Unauthorized action";
    public static  final String INVALID_FORMAT ="Invalid File Format";
    private ExceptionMessage(){}
}
