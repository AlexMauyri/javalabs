package ru.ssau.tk.DoubleA.javalabs.persistence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.ssau.tk.DoubleA.javalabs.bootloader.MathApplication;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.User;
import ru.ssau.tk.DoubleA.javalabs.security.user.Role;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MathApplication.class)
@AutoConfigureMockMvc
public class UserAndAuthControllerTest {
    @Autowired
    private MockMvc mvc;

    private String token;
    private final String login = "{\"username\":\"Vovan\",\"password\":\"1984\"}";
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() throws Exception {
        MvcResult result = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(login)).andExpect(status().isOk()).andReturn();
        token = "Bearer " + result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
    }

    @Test
    void registerAndLoginTest() throws Exception {

        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Alex\",\"password\":\"Mayuri\"}")).andExpect(status().isOk());

        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Alex\",\"password\":\"Mayuri\"}")).andExpect(status().isBadRequest());

        mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Alex\",\"password\":\"Bredik\"}")).andExpect(status().isForbidden()).andReturn();

        MvcResult mvcResult = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Alex\",\"password\":\"Mayuri\"}")).andExpect(status().isOk()).andReturn();

        String anotherToken = mvcResult.getResponse().getContentAsString();

        mvc.perform(get("/users/whoami")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();


        mvcResult = mvc.perform(get("/users/username=Alex")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn();

        String str = mvcResult.getResponse().getContentAsString();
        User user = mapper.readValue(str, User.class);

        Assertions.assertEquals("Alex", user.getUsername());

        mvc.perform(delete("/users/" + user.getUser_id()).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk());
    }

    @Test
    void getTest() throws Exception {
        MvcResult result = mvc.perform(get("/users/whoami")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        User user = mapper.readValue(result.getResponse().getContentAsString(), User.class);
        Assertions.assertEquals(1, user.getUser_id());
        Assertions.assertEquals("Vovan", user.getUsername());
        Assertions.assertEquals(Role.ADMIN, user.getRole());

        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Alex\",\"password\":\"Mayuri\"}")).andExpect(status().isOk());

        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Gabriel\",\"password\":\"V2\"}")).andExpect(status().isOk());

        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Leon\",\"password\":\"1181\"}")).andExpect(status().isOk());

        result = mvc.perform(get("/users")
                        .header(HttpHeaders.AUTHORIZATION, token))
                        .andExpect(status().isOk())
                        .andReturn();

        User[] users = mapper.readValue(result.getResponse().getContentAsString(), User[].class);
        Assertions.assertEquals("Alex", users[1].getUsername());
        Assertions.assertEquals("Gabriel", users[2].getUsername());
        Assertions.assertEquals("Leon", users[3].getUsername());

        mvc.perform(get("/users/id=-1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andReturn();

        result = mvc.perform(get("/users/id=1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        user = mapper.readValue(result.getResponse().getContentAsString(), User.class);

        Assertions.assertEquals("Vovan", user.getUsername());

        mvc.perform(get("/users/username=noname")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound())
                .andReturn();

        for (int i = 1; i < users.length; i++) {
            mvc.perform(delete("/users/" + users[i].getUser_id()).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk());
        }
    }

    @Test
    void deleteAndUpdateUser() throws Exception {
        mvc.perform(delete("/users/" + -1).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isNotFound());
        mvc.perform(put("/users/" + -1).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isNotFound());

        mvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Alex\",\"password\":\"Mayuri\"}")).andExpect(status().isOk());

        MvcResult mvcResult = mvc.perform(get("/users/username=Alex")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn();

        User user = mapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);

        Assertions.assertEquals(Role.USER, user.getRole());

        mvcResult = mvc.perform(put("/users/" + user.getUser_id()).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk()).andReturn();

        user = mapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);

        Assertions.assertEquals(Role.ADMIN, user.getRole());

        mvc.perform(delete("/users/" + user.getUser_id()).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk());
    }
}
