package ua.kpi.iasa.onlineradio.models;

import ua.kpi.iasa.onlineradio.models.iterators.ITrackIterator;
import ua.kpi.iasa.onlineradio.models.iterators.InfinitePlaylistIterator;
import ua.kpi.iasa.onlineradio.models.iterators.ShuffleIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private List<Track> tracks;
    private IterationMode mode;

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
        this.tracks = new ArrayList<>();
        this.mode = IterationMode.INFINITE;
    }

    // ФАБРИЧНИЙ МЕТОД
    public ITrackIterator createIterator() {
        switch (this.mode) {
            case SHUFFLE:
                System.out.println("Playlist: Створення Shuffle ітератора.");
                return new ShuffleIterator(this.tracks);
            case ONE_TIME:
                System.out.println("Playlist: Створення One-time ітератора (для простоти насправді Infinite).");
                return new InfinitePlaylistIterator(this.tracks);
            case INFINITE:
            default:
                System.out.println("Playlist: Створення Infinite ітератора.");
                return new InfinitePlaylistIterator(this.tracks);
        }
    }

    public IterationMode getMode() {
        return mode;
    }

    public void setMode(IterationMode mode) {
        this.mode = mode;
        System.out.println("Режим плейлиста '" + name + "' змінено на: " + mode);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void addTrack(Track track) {
        if (track != null && !tracks.contains(track)) {
            tracks.add(track);
        }
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mode=" + mode +
                ", trackCount=" + tracks.size() +
                '}';
    }
}