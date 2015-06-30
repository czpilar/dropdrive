package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxClient;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DbxClient.class })
public class AbstractServiceTest {

    private AbstractService service;

    @Mock
    private IDropDriveCredential dropDriveCredential;

    @Mock
    private Credential credential;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private DbxClient dbxClient;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        service = new AbstractService() {
        };
        service.setApplicationContext(applicationContext);

        when(applicationContext.getBean(DbxClient.class)).thenReturn(dbxClient);
    }

    @Test
    public void testSetAndGetDropDriveCredential() {
        assertNull(service.getDropDriveCredential());

        service.setDropDriveCredential(dropDriveCredential);

        assertEquals(dropDriveCredential, service.getDropDriveCredential());
    }

    @Test
    public void testGetDbxClient() {
        DbxClient result = service.getDbxClient();

        assertNotNull(result);
        assertEquals(dbxClient, result);

        verify(applicationContext).getBean(DbxClient.class);

        verifyNoMoreInteractions(applicationContext);
    }
}