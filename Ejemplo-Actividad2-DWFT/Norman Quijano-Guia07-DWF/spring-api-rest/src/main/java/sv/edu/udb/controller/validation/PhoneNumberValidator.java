package sv.edu.udb.controller.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * PhoneNumber validation class.
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber,
        String> {
    private String regexPattern;
    private Pattern pattern;
    private Matcher matcher;
    @Override
    public void initialize(final PhoneNumber constraintAnnotation) {
        regexPattern = constraintAnnotation.pattern();
    }
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return isValidPhoneNumber(phoneNumber);
    }
    /**
     * Check if the phoneNumber is valid.
     *
     * @param phoneNumber of the user
     * @return true is the phone number is valid otherwise false
     */
    private boolean isValidPhoneNumber(final String phoneNumber) {
        pattern = Pattern.compile(regexPattern);
        matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
