package com.kunminx.puremusic.domain.usecase;

import com.kunminx.architecture.domain.usecase.UseCase;
import com.kunminx.puremusic.data.config.Configs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Create by KunMinX at 20/03/16
 */
public class DownloadUseCase extends UseCase<DownloadUseCase.RequestValues, DownloadUseCase.ResponseValue> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        try {
            URL url = new URL(requestValues.url);
            InputStream is = url.openStream();
            File file = new File(Configs.COVER_PATH, requestValues.path);
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            is.close();
            os.close();

            getUseCaseCallback().onSuccess(new ResponseValue(file));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private String url;
        private String path;

        public RequestValues(String url, String path) {
            this.url = url;
            this.path = path;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private File mFile;

        public ResponseValue(File file) {
            mFile = file;
        }

        public File getFile() {
            return mFile;
        }

        public void setFile(File file) {
            mFile = file;
        }
    }
}
