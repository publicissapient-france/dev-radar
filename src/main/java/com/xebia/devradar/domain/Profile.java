
package com.xebia.devradar.domain;

import com.xebia.devradar.utils.GravatarUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Access(AccessType.FIELD)
@NamedQueries({
@NamedQuery(name = Profile.ORDER_BY_NAME, query = "from Profile p order by p.nickname")
})
public class Profile extends AbstractEntity {

    public static final String ORDER_BY_NAME = "Profile.orderByName";

    @NotBlank
    @Size(min = 1, max = 30)
    private String nickname;

    @NotBlank
    @Email
    @Size(min = 1, max = 80)
    private String email;

    @NotBlank
    @URL
    private String gravatarUrl;

    @Size(min = 0, max = 30)
    private String aliasSCM;

    public Profile() {
    }

    public Profile(String nickname, String email) {
        this(nickname, email, nickname);
    }

    public Profile(String nickname, String email, String aliasSCM) {
        this(nickname,
            email,
            GravatarUtils.constructGravatarUrlFromEmail(email, false, true),
            aliasSCM);
    }

    private Profile(String nickname, String email, String gravatarUrl, String aliasSCM) {
        this.nickname = nickname;
        this.email = email;
        this.gravatarUrl = gravatarUrl;
        this.aliasSCM = aliasSCM;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.gravatarUrl = GravatarUtils.constructGravatarUrlFromEmail(email, false, true);
    }

    public String getGravatarUrl() {
        return gravatarUrl;
    }

    public String getAliasSCM() {
        return aliasSCM;
    }

    public void setAliasSCM(String aliasSCM) {
        this.aliasSCM = aliasSCM;
    }
}
