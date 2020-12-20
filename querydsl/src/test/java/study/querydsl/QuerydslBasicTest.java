package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em);
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");

        em.persist(team1);
        em.persist(team2);

        Member member1 = new Member("member1", 10, team1);
        Member member2 = new Member("member2", 20, team2);
        Member member3 = new Member("member3", 30, team1);
        Member member4 = new Member("member4", 40, team2);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        // member1을 찾아라.
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        //QMember m = new QMember("m");
        //QMember m = QMember.member;

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        List<Member> findMembers = queryFactory
                .selectFrom(member)
                .where(member.age.in(10, 20))
                .fetch();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMembers.size()).isEqualTo(2);
    }


    @Test
    public void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch() {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();
//                .limit(1).fetchOne();

        /**
         * 페이징을 위해, 컨텐츠 쿼리랑 카운트 쿼리가 따로 나감
         * 복잡한 곳에서는 성능최적화를 위해 가급적 따로 쿼리를 짜자.
         */
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        long total = results.getTotal();
        List<Member> content = results.getResults();


        long t = queryFactory
                .selectFrom(member)
                .fetchCount();

    }

    /**
     * 1. 나이 내림차순 (desc)
     * 2. 이름 올림차순 (asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }


    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void paging2() {
        QueryResults<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        // 진짜 전체 개수 새는 카운트 쿼리가 한번 나가서 토탈에 들어감.
        assertThat(result.getTotal()).isEqualTo(4);
        assertThat(result.getLimit()).isEqualTo(2);
        assertThat(result.getOffset()).isEqualTo(1);
        assertThat(result.getResults().size()).isEqualTo(2);
    }

    @Test
    public void aggregation() {
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.max(),
                        member.age.avg(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        // 단일 타입이 아니라 여러가지 타입이 들어 올 수 있어서 Tuple로 조회됨.
        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구해라.
     *
     * @throws Exception
     */
    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                .select(
                        team.name,
                        member.age.avg()
                )
                .from(member)
                .join(member.team, team)
                // team의 이름으로 grouping
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("team1");
        assertThat(teamA.get(member.age.avg())).isEqualTo(20);
        assertThat(teamB.get(team.name)).isEqualTo("team2");
        assertThat(teamB.get(member.age.avg())).isEqualTo(30);
    }


    /**
     * 팀 A에 소속된 모든 회원.
     */
    @Test
    public void join() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(member.team.name.eq("team1"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member3");

    }

    /**
     * 세타 조인. 연관관계가 없는 것 끼리 조인
     * 회원의 이름이 팀 이름과 같은 회원 조회.
     */
    @Test
    public void theta_join() {
        em.persist(new Member("team1"));
        em.persist(new Member("team2"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        // 모든 회원, 모든 팀을 다 조인해버리고, (디비가 최적화는 함)
        // 막 조인을 다해버림.. ㅋㅋ

        assertThat(result)
                .extracting("username")
                .containsExactly("team1", "team2");

    }

    /**
     * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회.
     * JPQL : select m, t from Member m left join m.team t on t.name = "teamA"
     *
     * 기본적으로 on 절은 필터링을 통해서 join하는 대상 자체를 줄여버림.
     * where는 일단 join하고 거기서 필터링하는 느낌.
     * @throws Exception
     */
    @Test
    public void join_on_filtering() throws Exception {

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team, team).on(team.name.eq("team1"))
                .fetch();

        // 이거랑 join on 절이랑 똑같음. 익숙한 where 절 사용도 ㄱㅊ.
        // left join은 어쩔수없이 on 절 사용
//        queryFactory
//                .select(member, team)
//                .from(member)
//                .join(member.team, team)
//                .where(team.name.eq("team1"))
//                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }


    /**
     * 연관관계가 없는 엔티티 외부 조인.
     * 회원의 이름이 팀 이름과 같은 회원 조회.
     *
     * 하이버네이트 5.1부터 on을 사용해서 서로 관계가 없는 필드로 외부 조인하는 기능이 추가되었다!     */
    @Test
    public void join_on_no_relation() {
        em.persist(new Member("team1"));
        em.persist(new Member("team2"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                /**
                 * 원래는 leftJoin(member.team, team) 일케 되었음
                 * 이렇게 되면 join하는 대상이 id로 매칭이 되는데
                 * 빼고 그냥 leftJoin(team) 으로 하면 뒤에 on절에 해당하는 것만
                 * filtering되서 조인이 된다!
                 * 위와의 차이점 중요! (사실상 막 조인임, 서로다른컬럼의 같은 값 ㅋㅋ)
                 */
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        // 모든 회원, 모든 팀을 다 조인해버리고, (디비가 최적화는 함)
        // 막 조인을 다해버림.. ㅋㅋ

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용.").isFalse();

    }

    @Test
    public void fetchJoinYes() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용.").isTrue();

    }

    /** 나이가 가장 적은 사람 조회 **/
    /** SubQuery는 JPAExpressions를 사용하며, 같은 member라도 서로 alience가 겹치면 안되기 때문에
     * QMember 객체를 따로 생성한다. **/
    @Test
    public void subQuery() {

        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory.select(member)
                .from(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);

    }

    /** 나이가 평균 이상 사람 조회 **/
    /** SubQuery는 JPAExpressions를 사용하며, 같은 member라도 서로 alience가 겹치면 안되기 때문에
     * QMember 객체를 따로 생성한다. **/
    @Test
    public void subQueryGoe() {

        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory.select(member)
                .from(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30,40);

    }


    /** 나이가 평균 이상 사람 조회 **/
    /** SubQuery는 JPAExpressions를 사용하며, 같은 member라도 서로 alience가 겹치면 안되기 때문에
     * QMember 객체를 따로 생성한다. **/
    @Test
    public void subQueryIn() {

        QMember memberSub = new QMember("memberSub");

        /** 효율적이지 않은 코드. **/
        List<Member> result = queryFactory.select(member)
                .from(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                        .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30,40);

    }

    @Test
    public void selectSubQuery(){

        QMember memberSub = new QMember("memberSub");
        List<Tuple> list = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : list) {
            System.out.println("tuple = " + tuple);
        }
    }


    @Test
    public void basicCase() {
        List<String> fetch = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타")
                )
                .from(member)
                .fetch();

        for (String s : fetch) {
            System.out.println(s);
        }
    }

    @Test
    public void complexCase() {
        List<String> list = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타")
                ).from(member)
                .fetch();

        for (String s : list) {
            System.out.println(s);
        }
    }

    /** 결과가 [member1, A] 이렇게 나오게 상수 표현.**/
    @Test
    public void constant() {
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println(tuple);
        }
    }

    /** 문자열 더하기. **/
    @Test
    public void concat() {

        // {username}_{age}
        List<String> fetch = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue())) // int를 string으로.. enum도 변환가능.
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for (String s : fetch) {
            System.out.println(s);
        }
    }


    @Test
    public void simpleProjection() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println(s);
        }
    }


    @Test
    public void tupleProjection() {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

            System.out.println(username);
            System.out.println(age);
        }
    }


    @Test
    public void findDtoByJPQL() {

        List<MemberDto> result = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();

        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }


    /** Dto를 가져올 때 setter property 를 활용한 방법. **/
    @Test
    public void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class, member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }

    /** Dto를 가져올 때 field를 활용하는 방법. field에 쫙 꼽아버림. **/
    @Test
    public void findDtoByField() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }


    /** Dto를 가져올 때 생성자를 활용하는 방법. **/
    @Test
    public void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }

    /** Dto를 가져올 때 field를 활용하는 방법. field에 쫙 꼽아버림. **/
    /** Dto 에 이름이 username이 아니라 name일 경우.**/
    @Test
    public void findUserDto() {
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name")
                        ,member.age))
                .from(member)
                .fetch();

        for (UserDto memberDto : result) {
            System.out.println(memberDto);
        }
    }

    /** subquery 로 "age" alias **/
    @Test
    public void findUserDtoSubQuery() {
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(JPAExpressions
                        .select(memberSub.age.max())
                        .from(memberSub), "age")
                ))
                .from(member)
                .fetch();

        for (UserDto memberDto : result) {
            System.out.println(memberDto);
        }
    }


    @Test
    public void findDtoByQueryProjection() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println(memberDto);
        }
    }

    /** member1이고 나이가 10인 사람을 동적으로 찾기. **/
    @Test
    public void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    /** parameter가 null이냐 아니냐에 따라서 쿼리가 동적으로 바뀌어야 함.**/
    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();
        if(usernameCond != null){
            builder.and(member.username.eq(usernameCond));
        }

        if(ageCond != null){
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    public void dynamicQuery_whereParam() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {

        return queryFactory
                .selectFrom(member)
                /** null 이 return 되면 무시됨. **/
                //.where(usernameEq(usernameCond), ageEq(ageCond))
                .where(allEq(usernameCond, ageCond))
                .fetch();

    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    /** 조립도 가능. null처리는 해줘야함!. 이코드엔 빠짐. **/
    private BooleanExpression allEq(String usernameCod, Integer ageCond) {
        return usernameEq(usernameCod).and(ageEq(ageCond));
    }

    @Test
    public void bulkUpdate() {

        //DB
        //member1 = 10 -> 비회원
        //member2 = 20 -> 비회원
        //member3 = 30 -> member3
        //member4 = 4- -> member4

        //영속성 context
        //member1 = 10 -> member1
        //member2 = 20 -> member2
        //member3 = 30 -> member3
        //member4 = 4- -> member4

        //모든 벌크 연산은 영속성 컨텍스트를 무시하고 바로 DB의 값을 바꿔버림.

        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        //DB에서 가지고 오면, 이미 영속성 컨텍스트에 member1에 대한 pk값이 id로 영속상태의 객체가
        //존재하기 때문에 디비에서 가져온 걸 버림. 영속성 컨텍스트가 우선권.
        //그래서 DB에는 멤버1,2는 비회원이지만, 영속성컨텍스트에서는 맴버1,2이므로
        //그대로 맴버 1,2로 가져와짐.

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member member1 : result) {
            System.out.println(member1);
        }

        // 그래서 무조건 em.flush(), em.clear() 일케 해버리셈.
        em.flush();
        em.clear();

        List<Member> result2 = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member member1 : result2) {
            System.out.println(member1);
        }
    }

    @Test
    public void bulkAdd() {

        // 모든 age에 +1
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();

        long count2 = queryFactory
                .update(member)
                .set(member.age, member.age.multiply(2))
                .execute();
    }

    @Test
    public void bulkDelete() {
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }

    @Test
    public void sqlFunction() {

        // member라는 단어를 M으로 바꿔서 조회.
        List<String> result = queryFactory
                .select(Expressions.stringTemplate("function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println(s);
        }

    }


    @Test
    public void sqlFunction2() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                //.where(member.username.eq(
                        //Expressions.stringTemplate("function('lower', {0})", member.username)))
                .where(member.username.eq(member.username.lower()))
                .fetch();

        for (String s : result) {
            System.out.println(s);
        }
    }
}
