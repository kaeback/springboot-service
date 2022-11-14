package com.example.springbootservice.web;

import com.example.springbootservice.config.auth.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = HelloController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class HelloControllerTest {
    @Autowired
    /*
        MocMvc
         - 웹 API를 테스트할 때 사용
         - 스프링 MVC 테스트의 시작점
         - 이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트를 할 수 있다.
    */
    private MockMvc mvc;

    @WithMockUser(roles = "USER")
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))          // MocMvc를 통해 /hello 주소로 HTTP GET 요청을 한다.
                .andExpect(status().isOk())           // HTTP Header의 Status를 검증한다.
                .andExpect(content().string(hello));  // mvc.perform()의 결과를 검증한다.
    }

    @WithMockUser(roles = "USER")
    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        /*
            param
             - API 테스트할 때 사용될 요청 파라미터를 설정한다.
             - 단, 값은 String만 허용된다.
             - 숫자, 날짜 등의 데이터는 문자열로 변경해야 한다.

            jsonPath
             - JSON 응답값을 필드별로 검증할 수 있는 메소드
             - $를 기준으로 필드명을 명시
        */
        mvc.perform(
                        get("/hello/dto")
                                .param("name", name)
                                .param("amount", String.valueOf(amount))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}