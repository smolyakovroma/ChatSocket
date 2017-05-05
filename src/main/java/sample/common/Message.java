package sample.common;

import java.io.Serializable;

public interface Message extends Serializable {

    int CLOSE_TYPE = 0;
    int CONNECT_TYPE = 1;
    int CONTENT_TYPE = 2;

    String getName();
    String getContent();
    int getType();

}
