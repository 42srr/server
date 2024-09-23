package ggs.srr.domain;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Entity
@Slf4j
public class TimeTable {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_room_id")
    private StudyRoom studyRoom;

    @OneToMany(mappedBy = "timeTable")
    private final LinkedList<Slot> timeTable = new LinkedList<>();

    //db에서 불러오는 로직을 만들고
    public TimeTable() {
        //hash map initialize
        for(int i = 0; i < 48; i++)
            timeTable.add(new Slot(null, i));
        log.info("time table init\n");
    }

    //중복검사 로직 들어가고
    //starttime, endtime 이 정말 시간이 아니라 칸의 개수
//    private boolean CheckTimeTableDuplicated(int startTime, int endTime, SrTeam srTeam) {
//        for (int i = startTime; i <= endTime; i++) {
//            if (timeTable.get(i) != null)
//                return false;
//        }
//        return true;
//    }

    //todo
    //팀 리더 중복 확인 로직

    //맵에 시간 넣어주는 로직
    public boolean MakeReservation(int startTime, int endTime, SrTeam srTeam) {
        if (CheckTimeTableDuplicated(startTime, endTime, srTeam))
            return false;
        for(int i = startTime; i <= endTime; i++)
            timeTable.add(new Slot(null, i));
        return true;
    }


}
