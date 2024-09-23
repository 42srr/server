package ggs.srr.controller.home;

import ggs.srr.controller.home.dto.QuotesDto;
import ggs.srr.controller.home.dto.SrrTeamDto;
import ggs.srr.domain.FtUser;
import ggs.srr.domain.Quotes;
import ggs.srr.domain.Slot;
import ggs.srr.domain.SrTeam;
import ggs.srr.service.srteam.SrTeamService;
import ggs.srr.service.user.FtUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class SrrController {

    private final FtUserService userService;
    private final SrTeamService srTeamService;
    @PostMapping("/srr/team")
    public Long addSrrTeam(SrrTeamDto srrTeamDto) {
        String[] teamGroups = srrTeamDto.toString().split(",", 2);
        String[] teamMembers = teamGroups[1].split(",");
        List<String> teamMemberList = new ArrayList<>(Arrays.asList(teamGroups[1]));
        FtUser leader = userService.findByIntraId(teamGroups[0]);

        SrTeam srTeam = new SrTeam(leader, teamMemberList, 1 + teamMembers.length);
        return srTeamService.addTeams(srTeam);
    }

//    @GetMapping("/srr/todayReservation")
//    public LinkedList<Slot> getTodayReservation() {
//
//    }
}