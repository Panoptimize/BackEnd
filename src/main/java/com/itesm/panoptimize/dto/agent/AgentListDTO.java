package com.itesm.panoptimize.dto.agent;
import java.util.List;

public class AgentListDTO {

        private String id;
        private String name;
        private String status;
        private String workspace;
        private String instanceID;
        private int score;

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }







    public AgentListDTO(String id, String name, String status, String workspace, int score, String instanceID){
        this.id = id;
        this.name = name;
        this.status = status;
        this.workspace = workspace;
        this.score = score;
        this.instanceID = instanceID;
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }




        public String getWorkspace() {
            return workspace;
        }

        public void setWorkspace(String workspace) {
            this.workspace = workspace;
        }


    }
