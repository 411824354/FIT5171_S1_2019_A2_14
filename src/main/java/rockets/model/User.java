package rockets.model;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;

public class User extends Entity {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

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
        notBlank(lastName,"the last name cannot be null or empty");
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        notBlank(email, "email cannot be null or empty");
        email = email.trim().replace(" ","");
        if (!(emailFormat(email) && email.length() < 40)){

            throw new IllegalArgumentException("incorrect email format e.g:asdf@gmail.com");

        }
        else {

            this.email = email;
        }
    }

    public static boolean emailFormat(String email) {
        boolean tag = true;
        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            tag = false;
        }
        return tag;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        notBlank(password, "password cannot be null or empty");
        if (passwordFormat(password)){
            this.password = password;
        }
        else {
            throw new IllegalArgumentException("incorrect password format");
        }
    }

    public boolean passwordFormat(String str){
        boolean isDigit = false;
        boolean isLetter = false;
        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == ' '){
                return false;
            }
            if (Character.isDigit(str.charAt(i)))
            {
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i)))
            {
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{6,12}$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;

    }

    // match the given password against user's password and return the result
    public boolean isPasswordMatch(String password) {
        return this.password.equals(password.trim());
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
