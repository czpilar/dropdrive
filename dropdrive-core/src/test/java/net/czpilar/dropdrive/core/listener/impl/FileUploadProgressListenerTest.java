package net.czpilar.dropdrive.core.listener.impl;

import net.czpilar.dropdrive.core.listener.IFileUploadProgressListener;
import org.junit.Test;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class FileUploadProgressListenerTest {

    @Test
    public void testProgressChangedInitiation() {
        FileUploadProgressListener listener = new FileUploadProgressListener("filename", 150);

        listener.progressChanged(IFileUploadProgressListener.State.INITIATION, 0);
    }

    @Test
    public void testProgressChangedComplete() {
        FileUploadProgressListener listener = new FileUploadProgressListener("filename", 150);

        listener.progressChanged(IFileUploadProgressListener.State.COMPLETE, 0);
    }

    @Test
    public void testProgressChangedInProgress() {
        FileUploadProgressListener listener = new FileUploadProgressListener("filename", 150);

        listener.progressChanged(IFileUploadProgressListener.State.IN_PROGRESS, 0);
    }

    @Test
    public void testProgressChangedInProgressWithZeroLength() {
        FileUploadProgressListener listener = new FileUploadProgressListener("filename", 0);

        listener.progressChanged(IFileUploadProgressListener.State.IN_PROGRESS, 100);
    }
}