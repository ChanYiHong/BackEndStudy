package study.datajpa.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // JPA는 기본적으로 Default 생성자가 있어야 함. protected 까지 열어놔야 함.
    // Proxy 기술을 쓸 때, hibernate가 객체를 만들어야 하기 때문에 열어놔야 함. private 금지.
    // lombok으로도 가능.
    protected Member(){}

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null){
            changeTeam(team);
        }
    }

    // == 연관 관계 메서드 == //
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
