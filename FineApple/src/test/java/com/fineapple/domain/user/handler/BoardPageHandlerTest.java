package com.fineapple.domain.user.handler;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardPageHandlerTest {
    @Test
    public void test() {
        BoardPageHandler ph = new BoardPageHandler(250, 11);
        ph.print();
        System.out.println("ph = "+ph);
        assertTrue(ph.beginPage==1);
        assertTrue(ph.endPage==10);
    }

}