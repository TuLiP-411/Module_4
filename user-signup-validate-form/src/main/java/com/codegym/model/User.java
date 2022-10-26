package com.codegym.model;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class User implements Validator {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int age;

    public User() {
    }

    public User(String firstName, String lastName, String phoneNumber, int age, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        String firstName = user.getFirstName();
        ValidationUtils.rejectIfEmpty(errors, "firstName", "name.empty");
        if (firstName.length() > 45 || firstName.length() < 5) {
            errors.rejectValue("firstName", "name.length");
        }

        String lastName = user.getLastName();
        ValidationUtils.rejectIfEmpty(errors, "lastName", "name.empty");
        if (lastName.length() > 45 || lastName.length() < 5) {
            errors.rejectValue("lastName", "name.length");
        }

        String number = user.getPhoneNumber();
        ValidationUtils.rejectIfEmpty(errors, "phoneNumber", "number.empty");
        if (number.length() > 11 || number.length() < 10) {
            errors.rejectValue("phoneNumber", "number.length");
        }
        if (!number.startsWith("0")) {
            errors.rejectValue("phoneNumber", "number.startsWith");
        }
        if (!number.matches("(^$|[0-9]*$)")) {
            errors.rejectValue("phoneNumber", "number.matches");
        }

        int age = user.getAge();
        if (age <= 18) {
            errors.rejectValue("age", "age.not.legit");
        }

        String email = user.getEmail();
        if (!number.matches("(^[a-zA-Z0-9]@[a-z]\\.[a-z]$)")) {
            errors.rejectValue("email", "email.matches");
        }
    }

}

