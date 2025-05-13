package com.sismics.docs.core.dao.jpa;

import com.sismics.docs.BaseTransactionalTest;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.util.TransactionUtil;
import com.sismics.docs.core.util.authentication.InternalAuthenticationHandler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Tests the persistence layer.
 * 
 * @author jtremeaux
 */
public class TestJpa extends BaseTransactionalTest {
    
    @Test
    public void testJpa() throws Exception {
        // Create a user
        UserDao userDao = new UserDao();
        User user = createUser("testJpa");

        TransactionUtil.commit();

        // Search a user by ID
        user = userDao.getById(user.getId());
        Assert.assertNotNull(user);
        Assert.assertEquals("toto@docs.com", user.getEmail());

        // Authenticate using database
        Assert.assertNotNull(new InternalAuthenticationHandler().authenticate("testJpa", "12345678"));

        // Delete user
        userDao.delete("testJpa", user.getId());
        TransactionUtil.commit();
    }

    @Test
    public void testUserEntityAccessors() {
        User user = new User();
        Date now = new Date();

        user.setId("u001")
            .setRoleId("admin")
            .setUsername("alice")
            .setPassword("secret")
            .setEmail("alice@example.com")
            .setPrivateKey("privateKey")
            .setTotpKey("totp")
            .setStorageQuota(1000L)
            .setStorageCurrent(500L)
            .setCreateDate(now)
            .setDeleteDate(now)
            .setDisableDate(now)
            .setOnboarding(true);

        Assert.assertEquals("u001", user.getId());
        Assert.assertEquals("admin", user.getRoleId());
        Assert.assertEquals("alice", user.getUsername());
        Assert.assertEquals("secret", user.getPassword());
        Assert.assertEquals("alice@example.com", user.getEmail());
        Assert.assertEquals("privateKey", user.getPrivateKey());
        Assert.assertEquals("totp", user.getTotpKey());
        Assert.assertEquals(Long.valueOf(1000), user.getStorageQuota());
        Assert.assertEquals(Long.valueOf(500), user.getStorageCurrent());
        Assert.assertEquals(now, user.getCreateDate());
        Assert.assertEquals(now, user.getDeleteDate());
        Assert.assertEquals(now, user.getDisableDate());
        Assert.assertTrue(user.isOnboarding());

        Assert.assertTrue(user.toString().contains("alice"));
        Assert.assertEquals("alice", user.toMessage());
    }
}
