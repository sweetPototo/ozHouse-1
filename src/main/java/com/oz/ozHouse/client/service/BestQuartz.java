package com.oz.ozHouse.client.service;

import java.time.LocalTime;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("bestService")
public class BestQuartz {
   @Autowired
   private SqlSession sqlSession;
   
    public void testJobMethod() {
       LocalTime now = LocalTime.now();
       System.out.println("---- BEST 업데이트 ----");
       // 상품 전체를 normal spec 으로 업데이트 한다. 
       int res = sqlSession.update("updateProductNormal");
       
       if (res > 0) {
          System.out.println("normal 업데이트 성공 : " + now);
          // 일주일 동안 주문이 5회 이상이었던 product를 Best product 로 업데이트 한다
          int res2 = sqlSession.update("updateBestProduct");
          if (res2 > 0) {
             System.out.println("Best update 성공 : " + now);
          }
       }
       
    }
}