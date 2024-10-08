package ggs.srr.domain.projectuser;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.user.FtUser;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class ProjectUser {

    @Id @GeneratedValue
    private long id;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private FtUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public void initUser (FtUser user) {
        this.user = user;
        user.getProjectUsers().add(this);
    }

    public void initProject(Project project) {
        this.project = project;
        project.getProjectUsers().add(this);
    }
}
