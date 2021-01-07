package HCY.Ex1.repository;

import HCY.Ex1.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() throws Exception {
        //given
        System.out.println(memoRepository.getClass().getName());
        //when

        //then

    }

    @Test
    public void testInsertDummies() throws Exception {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("sample..." + i).build();
            memoRepository.save(memo);
        });

    }

    @Test
    public void testSelect() throws Exception {

        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }

    }

    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(100L).memoText("Change Memo...").build();
        memoRepository.save(memo);
    }

    @Test
    public void testDelete(){
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() throws Exception {

        // 1페이지, 10개.
        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("=============================");

        System.out.println("Total pages = " + result.getTotalPages());
        System.out.println("Total count = " + result.getTotalElements());
        System.out.println("Page number = " + result.getNumber()); // 현재 페이지 번호.
        System.out.println("Page size = " + result.getSize()); // 페이지당 데이터 개수
        System.out.println("Has next page? = " + result.hasNext()); // 다음 페이지가 있나?
        System.out.println("first page? = " + result.isFirst()); // 시작 페이지 (0) 인가?

        System.out.println("=============================");

        for (Memo memo : result.getContent()){
            System.out.println(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });

        Sort sort2 = Sort.by("memoText").ascending();
        Sort sort3 = sort1.and(sort2);
        Pageable pageable2 = PageRequest.of(0, 10, sort3);
        Page<Memo> result2 = memoRepository.findAll(pageable2);
        result2.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethods() throws Exception {

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }

    }


    @Test
    public void testQueryMethodWithPageable() throws Exception {

        Sort sort = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(3, 10, sort); // 3번 페이지 10개 역순으로 정렬!
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        for (Memo memo : result) {
            System.out.println(memo);
        }

    }

    @Test
    @Transactional
    @Commit
    public void testDeleteQueryMethods() throws Exception {
        memoRepository.deleteMemoByMnoLessThan(10L);
    }



}