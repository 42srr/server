package ggs.srr.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class FtUser {

    @Id @GeneratedValue
    private Long id;

    private double level;
    private Long resourceOwnerId;
    private String email;
    private String intraId;
    private String image;
    private int correction_point;
    private int wallet;

    @OneToMany(mappedBy = "ftUser")
    private List<ProjectUser> projects = new ArrayList<>();

    public FtUser() {
    }

    public FtUser(double level, Long resourceOwnerId, String email, String intraId, String image, int correction_point, int wallet) {
        this.level = level;
        this.resourceOwnerId = resourceOwnerId;
        this.email = email;
        this.intraId = intraId;
        this.image = image;
        this.correction_point = correction_point;
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return String.format("FtUser{level=%f, intraId='%s'}", level, intraId);
    }
}
