/*
 * Copyright (C) 2013 The Holodeck B2B Team, Sander Fieten
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.holodeckb2b.ebms3.persistent.message;

import java.util.Collection;
import javax.persistence.EntityManager;
import org.holodeckb2b.ebms3.constants.ProcessingStates;
import org.holodeckb2b.ebms3.persistent.processing.ProcessingState;
import org.holodeckb2b.ebms3.util.JPAUtil;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Sander Fieten <sander at holodeckb2b.org>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageUnitQueriesTest {
    
    private static final String T_MSG_ID_1 = "63768761876278234678624@msg-id.org";
    private static final String T_MSG_ID_2 = "987654321098ddsa6543210@msg-id.org";
    private static final String T_MSG_ID_3 = "64554576187627846a122qa@msg-id.org";
    private static final String T_MSG_ID_4 = "98765445545409876543210@msg-id.org";
    private static final String T_MSG_ID_5 = "fffw2345545409876543210@msg-id.org";   
    
    private static final String T_PROCSTATE_1 = "firststate";
    private static final String T_PROCSTATE_2 = "secondstate";
    
    private static final String T_PMODE1 = "PMODE_1";
    private static final String T_PMODE2 = "PMODE_2";
    private static final String T_PMODE3 = "PMODE_3";
    
    
    EntityManager   em;
    
    public MessageUnitQueriesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        EntityManager em = JPAUtil.getEntityManager();

        
        em.getTransaction().begin();
        
        UserMessage mu1 = new UserMessage();
        mu1.setMessageId(T_MSG_ID_1);
        ProcessingState s1 = new ProcessingState(T_PROCSTATE_1);
        em.persist(s1);
        mu1.setProcessingState(s1);
        mu1.setPMode(T_PMODE1);
        em.persist(mu1);

        UserMessage mu2 = new UserMessage();
        mu2.setMessageId(T_MSG_ID_2);
        ProcessingState s2 = new ProcessingState(T_PROCSTATE_1);
        em.persist(s2);
        mu2.setProcessingState(s2);
        mu2.setPMode(T_PMODE2);
        em.persist(mu2);

        PullRequest mu3 = new PullRequest();
        mu3.setMessageId(T_MSG_ID_3);
        ProcessingState s3 = new ProcessingState(T_PROCSTATE_2);
        em.persist(s3);
        mu3.setProcessingState(s3);
        mu3.setPMode(T_PMODE2);
        em.persist(mu3);

        Receipt mu4 = new Receipt();
        mu3.setMessageId(T_MSG_ID_4);
        ProcessingState s4 = new ProcessingState(T_PROCSTATE_2);
        em.persist(s4);
        mu4.setProcessingState(s4);
        mu4.setPMode(T_PMODE3);
        em.persist(mu4);       
        
        UserMessage mu5 = new UserMessage();
        mu5.setMessageId(T_MSG_ID_5);
        ProcessingState s5 = new ProcessingState(ProcessingStates.DELIVERED);
        em.persist(s5);
        mu5.setProcessingState(s5);
        mu5.setDirection(MessageUnit.Direction.IN);
        mu5.setPMode(T_PMODE3);
        em.persist(mu5);

        UserMessage mu6 = new UserMessage();
        mu6.setMessageId(T_MSG_ID_5);
        ProcessingState s6 = new ProcessingState(ProcessingStates.AWAITING_RECEIPT);
        em.persist(s6);
        mu6.setProcessingState(s6);
        mu6.setDirection(MessageUnit.Direction.OUT);
        mu6.setPMode(T_PMODE2);
        em.persist(mu6);

        UserMessage mu7 = new UserMessage();
        mu7.setMessageId(T_MSG_ID_5);
        ProcessingState s7 = new ProcessingState(ProcessingStates.DELIVERED);
        em.persist(s7);
        mu7.setProcessingState(s7);
        mu7.setDirection(MessageUnit.Direction.IN);
        mu7.setPMode(T_PMODE3);
        em.persist(mu7);

        
        em.getTransaction().commit();
        em.close();
    }
    
    @AfterClass
    public static void cleanup() {
        EntityManager em = JPAUtil.getEntityManager();
        
        em.getTransaction().begin();
        Collection<MessageUnit> tps = em.createQuery("from MessageUnit", MessageUnit.class).getResultList();
        
        for(MessageUnit mu : tps)
            em.remove(mu);
        
        em.getTransaction().commit();
    }
    
    @Before
    public void setUp() {
        em = JPAUtil.getEntityManager();
    }
    
    @After
    public void tearDown() {
        em.close();
    }

 
    /**
     * Test of find query
     */
    @Test
    public void test03_findWithMessageIdQuery() {
        em.getTransaction().begin();
        
        Collection<MessageUnit> result = em.createNamedQuery("MessageUnit.findWithMessageIdInDirection",
                                            MessageUnit.class)
                                            .setParameter("msgId", T_MSG_ID_5)
                                            .setParameter("direction", MessageUnit.Direction.IN)
                                            .getResultList();
        assertEquals(2, result.size());

        em.getTransaction().commit();      
    }
    
}
