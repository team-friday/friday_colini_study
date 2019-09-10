package com.colini.study.api.test.intergration;

import com.colini.study.api.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExceptionTests {


    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void entityNotFoundTests() throws Exception {
        //when
        //given, when
        MvcResult result =  mockMvc.perform(get("/channel/100"))
                                    .andExpect(status().isNotFound())
                                    .andDo(print())
                                    .andReturn();

        assertThat(result.getResponse().getStatus(), is(404));


    }
}
