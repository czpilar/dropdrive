package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.v2.DbxClientV2;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
class AbstractServiceTest {

    private AbstractService service;

    @Mock
    private IDropDriveCredential dropDriveCredential;

    @Mock
    private DbxClientV2 dbxClient;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        service = new AbstractService() {
        };
        service.setDbxClient(dbxClient);
    }

    @AfterEach
    void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSetAndGetDropDriveCredential() {
        assertNull(service.getDropDriveCredential());

        service.setDropDriveCredential(dropDriveCredential);

        assertEquals(dropDriveCredential, service.getDropDriveCredential());
    }

    @Test
    void testGetDbxClient() {
        DbxClientV2 result = service.getDbxClient();

        assertNotNull(result);
        assertEquals(dbxClient, result);
    }
}
