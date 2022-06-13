package com.app.fileprocess.validator;

import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;


@Component
public class UserFileValidator {

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
