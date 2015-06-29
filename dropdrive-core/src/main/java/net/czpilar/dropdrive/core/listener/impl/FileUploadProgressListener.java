package net.czpilar.dropdrive.core.listener.impl;

import java.text.NumberFormat;

import net.czpilar.dropdrive.core.listener.IFileUploadProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener used for printing progress of uploading file.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class FileUploadProgressListener implements IFileUploadProgressListener {

    private static final Logger LOG = LoggerFactory.getLogger(FileUploadProgressListener.class);

    private final String filename;
    private final long length;

    public FileUploadProgressListener(String filename, long length) {
        this.filename = filename;
        this.length = length;
    }

    @Override
    public void progressChanged(State state, long uploaded) {
        switch (state) {
            case INITIATION:
                LOG.info("Initiation uploading file {}", filename);
                break;
            case IN_PROGRESS:
                LOG.info("Uploaded {} of file {}", NumberFormat.getPercentInstance().format(getProgress(uploaded)), filename);
                break;
            case COMPLETE:
                LOG.info("Finished uploading file {}", filename);
                break;
        }
    }

    private double getProgress(long uploaded) {
        return length == 0 ? 0 : ((double) uploaded / length);
    }
}
