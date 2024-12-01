package ggs.srr.api.controller.user.dto;

import ggs.srr.domain.user.FtUser;
import lombok.Data;

@Data
public class UserDto {

    private String intraId;
    private String email;
    private String url;
    private int wallet;
    private int collectionPoint;
    private double level;
    private String image;

    public UserDto(FtUser ftUser) {
        this.intraId = ftUser.getIntraId();
        this.wallet = ftUser.getWallet();
        this.collectionPoint = ftUser.getCollectionPoint();
        this.level = ftUser.getLevel();
        this.image = ftUser.getImage();
    }
}
