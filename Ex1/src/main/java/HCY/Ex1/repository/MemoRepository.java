package HCY.Ex1.repository;

import HCY.Ex1.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    // mno 값이 from 에서 to 사이의 객체들을 구하고, mno의 역순으로 정렬하기 위한 쿼리 메서드. (메서드 이름 자체가 query!)
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // pageable 파라미터를 넘겨서 페이징 뿐 아니라 정렬도 이름이 안길고 간편하게!
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // 비추천하는 방법.. 쿼리가 row마다 하나씩 나감.
    void deleteMemoByMnoLessThan(Long num);

    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();
}
