package com.app.fileprocess.validator;

import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

/**
 * UserFileValidator
 * 
 * <P>Holds validation for user files
 * 
 * @author arunitillekeratne
 * @version 1.0
 *
 */
@Component
public class UserFileValidator {

	/**
	 * Check validity of a filename
	 * 
	 * @param o
	 * @return
	 */
    public boolean validate(Object o) {
        String filename = (String) o;
        if (StringUtils.isEmptyOrWhitespace(filename) ||
        		!filename.endsWith(".opi")) {
        	return false;
        } else {
        	return true;
        }
    }

}
