package org.helpdesk.posts.model.document;

import javax.validation.constraints.NotEmpty;

public class UserInfo {
    private String emailAddress;    // for unregistered users who make a request with a ticket
    @NotEmpty(message = "User name must be stated.")
    private String userName;
    private String userFirstNameLastName;

    public UserInfo(String emailAddress, @NotEmpty(message = "User name must be stated.") String userName, String userFirstNameLastName) {
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.userFirstNameLastName = userFirstNameLastName;
    }

    public UserInfo() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstNameLastName() {
        return userFirstNameLastName;
    }

    public void setUserFirstNameLastName(String userFirstNameLastName) {
        this.userFirstNameLastName = userFirstNameLastName;
    }
}
