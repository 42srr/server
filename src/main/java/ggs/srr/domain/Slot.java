package ggs.srr.domain;


import jakarta.persistence.*;

@Entity
public class Slot {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "time_table_id")
    private TimeTable timeTable;

    @OneToOne
    @JoinColumn(name = "team_id")
    private SrTeam team;
    private int idx;


    public Slot(SrTeam team, int idx) {
        this.team = team;
        this.idx = idx;
    }
}
