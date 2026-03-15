package net.czpilar.dropdrive.core.listener.impl;

import net.czpilar.dropdrive.core.listener.IFileUploadProgressListener;
import org.junit.jupiter.api.Test;

/**
 * @author David Pilar (david@czpilar.net)
 */
class FileUploadProgressListenerTest {

    @Test
    void testProgressChangedInitiation() {
        FileUploadProgressListener listener = new FileUploadProgressListener("filename", 150);

        listener.progressChanged(IFileUploadProgressListener.State.INITIATION, 0);
    }

    @Test
    void testProgressChangedComplete() {
        FileUploadProgressListener listener = new FileUploadProgressListener("filename", 150);

        listener.progressChanged(IFileUploadProgressListener.State.COMPLETE, 0);
    }

    @Test
    void testProgressChangedInProgress() {
        FileUploadProgressListener listener = new FileUploadProgressListener("filename", 150);

        listener.progressChanged(IFileUploadProgressListener.State.IN_PROGRESS, 0);
    }

    @Test
    void testProgressChangedInProgressWithZeroLength() {
        FileUploadProgressListener listener = new FileUploadProgressListener("filename", 0);

        listener.progressChanged(IFileUploadProgressListener.State.IN_PROGRESS, 100);
    }
}