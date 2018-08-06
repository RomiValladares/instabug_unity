package com.duelit.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.joda.time.DateTime;

import java.io.File;
import java.io.Serializable;

public class User implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @DynamicSerialize(
            id = "facebookId",
            asUnderscore = "fb_user_id",
            asUpperCamelCase = "fbUserId"
    )
    private String facebookId;
    //database id
    private int id;
    private String email;
    private String password;
    @DynamicSerialize(
            id = "firstName",
            asUnderscore = "first_name",
            asUpperCamelCase = "firstName"
    )
    private String firstName;
    @DynamicSerialize(
            id = "lastName",
            asUnderscore = "last_name",
            asUpperCamelCase = "lastName"
    )
    private String lastName;
    private String role;
    private String userStatus;
    private int age;
    private String country;
    private String pictureURL;
    private String state;
    private byte[] pictureContent;
    private File pictureFile;
    private DateTime birthDate;

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public User(String email, String password, String firstName, String lastName, int age) {
        this(email, password, firstName, lastName);
        this.age = age;
    }

    public User(String facebookId, String picture, String email, String firstName, String lastName) {
        this.email = email;
        this.facebookId = facebookId;
        this.pictureURL = picture;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(User sessionUser) {
        this(sessionUser.getEmail(), sessionUser.getPassword(), sessionUser.getFirstName(), sessionUser.getLastName());
        this.age = sessionUser.getAge();
        this.country = sessionUser.getCountry();
        this.id = sessionUser.getId();
        this.pictureURL = sessionUser.getPictureURL();
        this.role = sessionUser.getRole();
        this.state = sessionUser.getState();
        this.userStatus = sessionUser.getUserStatus();
    }

    public User(String id, String pictureURL, String email, String firstName, String lastName, int age, String country) {
        this(id, pictureURL, email, firstName, lastName);
        this.age = age;
        this.country = country;
    }

    protected User(Parcel in) {
        facebookId = in.readString();
        id = in.readInt();
        email = in.readString();
        password = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        role = in.readString();
        userStatus = in.readString();
        age = in.readInt();
        country = in.readString();
        pictureURL = in.readString();
        state = in.readString();

        Serializable serializable = in.readSerializable();
        if (serializable != null)
            birthDate = (DateTime) in.readSerializable();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        String concatenation = firstName != null ? firstName : "";
        concatenation += " ";
        concatenation += lastName != null ? lastName : "";
        return concatenation;
    }

    public String getCountryAndState() {
        if (!TextUtils.isEmpty(state)) {
            return state + ", " + country;
        }
        return country;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", country='" + country + '\'' +
                ", pictureURL='" + pictureURL + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getAge() != user.getAge()) return false;
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(user.getFirstName()) : user.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(user.getLastName()) : user.getLastName() != null)
            return false;
        if (getCountry() != null ? !getCountry().equals(user.getCountry()) : user.getCountry() != null)
            return false;
        return !(getState() != null ? !getState().equals(user.getState()) : user.getState() != null);

    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + getAge();
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        return result;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(facebookId);
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(role);
        dest.writeString(userStatus);
        dest.writeInt(age);
        dest.writeString(country);
        dest.writeString(pictureURL);
        dest.writeString(state);
        dest.writeSerializable(birthDate);
    }

    public DateTime getBirthDate() {
        return birthDate;
    }

}