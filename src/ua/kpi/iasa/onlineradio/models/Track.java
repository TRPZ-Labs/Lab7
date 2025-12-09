package ua.kpi.iasa.onlineradio.models;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class Track {
    private int id;
    private String title;
    private String artist;
    private Duration duration;
    private String filePath;
    private List<User> likedBy;

    public Track(int id, String title, String artist, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.likedBy = new ArrayList<>();
        // Тривалість можна було б отримувати з метаданих файлу
        this.duration = Duration.ofMinutes(3).plusSeconds(30);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }
    
    public String getFilePath() {
        return filePath;
    }

    public int getLikeCount() {
        return likedBy.size();
    }

    public void addLike(User user) {
        if (!likedBy.contains(user)) {
            likedBy.add(user);
        }
    }
    
    @Override
    public String toString() {
        return "Track{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", artist='" + artist + '\'' +
               ", likes=" + getLikeCount() +
               '}';
    }
}