package ggs.srr.service.srteam;

import ggs.srr.domain.SrTeam;
import ggs.srr.repository.srteam.SrTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SrTeamService {
    private final SrTeamRepository srTeamRepository;

    public Long addTeams(SrTeam srTeam) {
        return srTeamRepository.save(srTeam);
    }
}
