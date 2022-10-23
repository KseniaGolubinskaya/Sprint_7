package ru.yandex.practikum.dto;

public class CancelOrderRequest {
    private Integer track;

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public CancelOrderRequest(Integer track) {
        this.track = track;
    }
}
