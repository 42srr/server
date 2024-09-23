package ggs.srr.domain;

import jakarta.persistence.*;

import java.sql.Time;
import java.util.ArrayList;

@Entity
public class StudyRoom {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String place;


    @OneToMany(mappedBy = "studyRoom")
    private ArrayList<TimeTable> timeTables = new ArrayList<>();

    public StudyRoom() {
        for(int i = 0; i <= 31; i++) {
            timeTables.add(new TimeTable());
        }
    }

//    public LinkedList<Slot> getTodayReservationStatus(int place, int day) {
//
//    }

    //todo
    //예약을 받는 로직 with 중복검사
    public  boolean makeReservationStudyRoom (int startTime, int endTime, SrTeam srTeam, int day) {
        return timeTable[day].MakeReservation(startTime, endTime, srTeam);
    }
}
