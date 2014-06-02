package com.gameaurora.nova.modules.cloudmessages;

public class CloudMessageSender {

    private String name;
    private String prettyName;

    public CloudMessageSender(String name, String prettyName) {
        this.name = name;
        this.prettyName = prettyName;
    }

    public String getName() {
        return name;
    }

    public String getPrettyName() {
        return prettyName;
    }

}
