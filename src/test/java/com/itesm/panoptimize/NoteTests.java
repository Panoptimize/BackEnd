package com.itesm.panoptimize;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteTests {
    @Autowired
    private MockMvc mockMvc;
    private String firebaseToken;

    FirebaseTestSetup firebaseTestSetup = new FirebaseTestSetup();

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = firebaseTestSetup.getFirebaseToken();
    }

    @Test
    public void testGetAgentNotesList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/note/agent/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.pageable").exists())
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.size").isNumber())
                .andExpect(jsonPath("$.number").isNumber())
                .andExpect(jsonPath("$.first").isBoolean())
                .andExpect(jsonPath("$.last").isBoolean())
                .andExpect(jsonPath("$.numberOfElements").isNumber())
                .andExpect(jsonPath("$.empty").isBoolean())

                .andExpect(jsonPath("$.content[0].id", notNullValue()))
                .andExpect(jsonPath("$.content[0].name", notNullValue()))
                .andExpect(jsonPath("$.content[0].description", notNullValue()))
                .andExpect(jsonPath("$.content[0].priority", notNullValue()))
                .andExpect(jsonPath("$.content[0].solved", notNullValue()))
                .andExpect(jsonPath("$.content[0].createdAt", notNullValue()))
                .andExpect(jsonPath("$.content[0].updatedAt", notNullValue()));
    }

    @Test
    public void testGetAgentNotesListNoNotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/note/agent/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.numberOfElements").value(0))
                .andExpect(jsonPath("$.empty").value(true));
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateNote() throws Exception {
        String createRequestBody = """
        {
          "createNote": {
            "name": "Test Create",
            "description": "The note will be created.",
            "priority": "HIGH",
            "solved": false
          },
          "createAgentPerformance": {
            "avgAfterContactWorkTime": 10,
            "avgHandleTime": 5,
            "avgAbandonTime": 15,
            "avgHoldTime": 20,
            "id": 1
          }
        }
        """;
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/note/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + firebaseToken)
                    .content(createRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Test Create"))
                .andExpect(jsonPath("$.description").value("The note will be created."))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.solved").value(false))
                .andExpect(jsonPath("$.createdAt").value(notNullValue()))
                .andExpect(jsonPath("$.updatedAt").value(notNullValue()));
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateNoteWithNoAgentPerformance() throws Exception {
        String createRequestBody = """
        {
          "createNote": {
            "name": "Test Create",
            "description": "The note will be created.",
            "priority": "HIGH",
            "solved": false
          }
        }
        """;
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/note/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(createRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("IllegalArgumentException"))
                .andExpect(jsonPath("$.message").value("AgentPerformance data is missing"));
    }

    @Test
    @Transactional
    @Rollback
    public void testEditNote() throws Exception {
        String createRequestBody = """
        {
          "createNote": {
            "name": "Test Create for Edit",
            "description": "The note will be created and then edited.",
            "priority": "HIGH",
            "solved": false
          },
          "createAgentPerformance": {
            "avgAfterContactWorkTime": 10,
            "avgHandleTime": 5,
            "avgAbandonTime": 15,
            "avgHoldTime": 20,
            "id": 1
          }
        }
        """;

        String createdNote = mockMvc.perform(MockMvcRequestBuilders
                        .post("/note/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(createRequestBody))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int createdNoteId = JsonPath.parse(createdNote).read("$.id");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/note/" + createdNoteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content("{\"name\":\"Edit Test\", \"description\":\"Test description\", \"priority\":\"LOW\", \"solved\":false}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(createdNoteId))
                .andExpect(jsonPath("$.name").value("Edit Test"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.solved").value(false))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void testEditNoteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                    .put("/note/99999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + firebaseToken)
                    .content("{\"name\":\"Edit Test\", \"description\":\"Test description\", \"priority\":\"LOW\", \"solved\":false}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteNote() throws Exception {
        String createRequestBody = """
        {
          "createNote": {
            "name": "Test Create Delete",
            "description": "The note will be created and then deleted.",
            "priority": "HIGH",
            "solved": false
          },
          "createAgentPerformance": {
            "avgAfterContactWorkTime": 10,
            "avgHandleTime": 5,
            "avgAbandonTime": 15,
            "avgHoldTime": 20,
            "id": 1
          }
        }
        """;

        String createdNote = mockMvc.perform(MockMvcRequestBuilders
                        .post("/note/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(createRequestBody))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int createdNoteId = JsonPath.parse(createdNote).read("$.id");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/note/" + createdNoteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/note/" + createdNoteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteNoteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/note/99999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testGetAgentPerformanceByNoteId() throws Exception {

        String createRequestBody = """
        {
          "createNote": {
            "name": "Test Create AP",
            "description": "The note will be created and the Agent Performance will be extracted.",
            "priority": "HIGH",
            "solved": false
          },
          "createAgentPerformance": {
            "avgAfterContactWorkTime": 10,
            "avgHandleTime": 5,
            "avgAbandonTime": 15,
            "avgHoldTime": 20,
            "id": 1
          }
        }
        """;

        String createdNote = mockMvc.perform(MockMvcRequestBuilders
                        .post("/note/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken)
                        .content(createRequestBody))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int createdNoteId = JsonPath.parse(createdNote).read("$.id");

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/agent-performance/note/" + createdNoteId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.avgAfterContactWorkTime").value(10))
                .andExpect(jsonPath("$.avgHandleTime").value(5))
                .andExpect(jsonPath("$.avgAbandonTime").value(15))
                .andExpect(jsonPath("$.avgHoldTime").value(20));
    }

    @Test
    public void testGetAgentPerformanceByNonexistentId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/agent-performance/note/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + firebaseToken))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
