package com.fineapple.domain.user.handler;

public class BoardPageHandler {
    int totalCnt;
    int page;
    int pageSize;
    int totalPage;
    int beginPage;
    int endPage;
    boolean showPrev;
    boolean showNext;
    int naviSize = 10;

    // 생성자 1: 기본 pageSize 10
    public BoardPageHandler(int totalCnt, int page) {
        this(totalCnt, page, 10);
    }

    // 생성자 2: 직접 pageSize 지정
    public BoardPageHandler(int totalCnt, int page, int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize는 0보다 커야 합니다.");
        }

        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;

        // 실수로 나누어 소수점 올림 처리
        totalPage = (int)Math.ceil((double)totalCnt / pageSize);

        // beginPage 계산 (1, 11, 21, ...)
        beginPage = (page - 1) / naviSize * naviSize + 1;

        // endPage는 전체 페이지를 넘지 않도록 제한
        endPage = Math.min(beginPage + naviSize - 1, totalPage);

        showPrev = beginPage != 1;
        showNext = endPage != totalPage;
    }

    void print() {
        System.out.println("page = " + page);
        System.out.println(showPrev ? "[prev]" : "");
        for (int i = beginPage; i <= endPage; i++) {
            System.out.print(i + " ");
        }
        System.out.println(showNext ? "[next]" : "");
    }

    @Override
    public String toString() {
        return "BoardPageHandler{" +
                "totalCnt=" + totalCnt +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", beginPage=" + beginPage +
                ", endPage=" + endPage +
                ", showPrev=" + showPrev +
                ", showNext=" + showNext +
                '}';
    }
}
