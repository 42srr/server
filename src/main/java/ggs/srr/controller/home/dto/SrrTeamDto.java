package ggs.srr.controller.home.dto;

import lombok.Data;

@Data
public class SrrTeamDto {
    private String teams;

    public SrrTeamDto(String teams) {
        this.teams = teams;
    }
}
