package com.example.cultureconnect.databaseaccess;

import static org.junit.jupiter.api.Assertions.*;

class DAOTest {

        @org.junit.jupiter.api.Test
        void testConnection() {
            DAO dao = new DAO();
            assertNotNull(dao.con);
        }
}