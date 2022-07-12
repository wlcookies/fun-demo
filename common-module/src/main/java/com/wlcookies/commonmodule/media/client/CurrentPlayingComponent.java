package com.wlcookies.commonmodule.media.client;

public class CurrentPlayingComponent {
    private String serviceName;
    private boolean isPlaying;

    public CurrentPlayingComponent() {
    }

    public CurrentPlayingComponent(String serviceName, boolean isPlaying) {
        this.serviceName = serviceName;
        this.isPlaying = isPlaying;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
