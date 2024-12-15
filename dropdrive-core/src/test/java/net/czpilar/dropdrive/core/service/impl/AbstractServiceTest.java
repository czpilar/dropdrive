package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.v2.DbxClientV2;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class AbstractServiceTest {

    private AbstractService service;

    @Mock
    private IDropDriveCredential dropDriveCredential;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private DbxClientV2 dbxClient;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        service = new AbstractService() {
        };
        service.setApplicationContext(applicationContext);

        when(applicationContext.getBean(DbxClientV2.class)).thenReturn(dbxClient);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testSetAndGetDropDriveCredential() {
        assertNull(service.getDropDriveCredential());

        service.setDropDriveCredential(dropDriveCredential);

        assertEquals(dropDriveCredential, service.getDropDriveCredential());
    }

    @Test
    public void testGetDbxClient() {
        DbxClientV2 result = service.getDbxClient();

        assertNotNull(result);
        assertEquals(dbxClient, result);

        verify(applicationContext).getBean(DbxClientV2.class);

        verifyNoMoreInteractions(applicationContext);
    }
}
