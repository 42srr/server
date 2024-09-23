package ggs.srr.repository.srteam;

import ggs.srr.domain.SrTeam;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
public class SrTeamRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(SrTeam team){
        em.persist(team);
        return team.getId();
    }
}
