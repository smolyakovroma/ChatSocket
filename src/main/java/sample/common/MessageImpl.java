package sample.common;

public class MessageImpl implements Message {

    private final String name;
    private final String content;
    private final int type;

    public MessageImpl(String name, String content, int type) {
        this.name = name;
        this.content = content;
        this.type = type;
    }



    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + ": "+content;

    }
}
