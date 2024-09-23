package ggs.srr.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class SrTeam {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private FtUser leader;

    @OneToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    private List<String> teamMembers;
    private int teamNum;

    public SrTeam() {
    }

    //todo
    //팀 리더 혹은 팀 멤머 안에서 중복 있는지
    public SrTeam(FtUser leader, List<String> teamMembers, int teamNum) {
        this.leader = leader;
        this.teamMembers = teamMembers;
        this.teamNum = teamNum;
    }

    public Long changeTeamLeader(FtUser leader) {
        this.leader = leader;
        return leader.getId();
    }

    //todo
    //팀 리더 혹은 팀 멤머 안에서 중복 있는지
    public void changeMembers(List<String> groupMembers) {
        this.teamMembers = teamMembers;
    }

    @Override
    public String toString() {
        return String.format("SrTeam{leader='%s', groups='%s'}", leader, teamMembers);
    }
}

