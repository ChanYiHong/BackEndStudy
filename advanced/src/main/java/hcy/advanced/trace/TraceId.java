package hcy.advanced.trace;

import java.util.UUID;

public class TraceId {

    private String id;  // 트렌잭션 아이디
    private int level;  // 로그 레벨 깊이

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    // UUID 의 앞 8자리만 활용.
    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createNextId() {
        // 같은 트렌젝션 로그의 다른 과정
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
