package ua.kpi.iasa.onlineradio.facade;

import ua.kpi.iasa.onlineradio.data.*;
import ua.kpi.iasa.onlineradio.models.*;
import ua.kpi.iasa.onlineradio.repositories.*;

import java.util.Optional;

public class RadioSystemFacade {
    // Підсистеми, які ми приховуємо від UI
    private final IUserRepository userRepo;
    private final ITrackRepository trackRepo;
    private final IPlaylistRepository playlistRepo;
    private final IPlaybackEventRepository historyRepo;

    private final MusicLibrary library;
    private final Streamer streamer;
    private User currentUser;

    public RadioSystemFacade() {
        // Ініціалізація всіх підсистем (приховуємо складність створення)
        this.trackRepo = new TrackRepository();
        this.userRepo = new UserRepository();
        this.playlistRepo = new PlaylistRepository();
        this.historyRepo = new PlaybackEventRepository();

        this.library = new MusicLibrary(trackRepo);
        this.streamer = new Streamer();

        setupInitialData();
    }

    // --- Методи для Автентифікації ---
    public boolean login(String username, String password) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPasswordHash().equals(password)) {
            this.currentUser = userOpt.get();
            System.out.println("Facade: Користувач " + currentUser.getUsername() + " увійшов у систему.");
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // --- Методи для Керування Відтворенням ---
    public void setPlaylist(int playlistId) {
        playlistRepo.findById(playlistId).ifPresent(streamer::setActivePlaylist);
    }

    public void changePlaybackMode(IterationMode mode) {
        // Отримуємо поточний плейлист зі стрімера (або через репозиторій)
        // Для прикладу припустимо, що ми знаємо ID активного плейлиста або додали геттер в Streamer
        // Тут ми просто оновимо перший плейлист для демонстрації
        playlistRepo.findById(1).ifPresent(p -> {
            p.setMode(mode);
            // Перезавантажуємо плейлист у стрімер, щоб застосувати новий ітератор
            streamer.setActivePlaylist(p);
        });
    }

    public void play() {
        streamer.play();
        logHistory();
    }

    public void nextTrack() {
        streamer.nextTrack();
        logHistory();
    }

    public void likeCurrentTrack() {
        Track current = streamer.getCurrentTrack();
        if (current != null && currentUser != null) {
            current.addLike(currentUser);
            // Тут можна було б викликати trackRepo.update(current), якби це була реальна БД
            System.out.println("Facade: Лайк додано для '" + current.getTitle() + "'");
        }
    }

    public Track getCurrentTrack() {
        return streamer.getCurrentTrack();
    }

    // --- Внутрішні методи ---

    private void logHistory() {
        Track current = streamer.getCurrentTrack();
        if (current != null && currentUser != null) {
            PlaybackEvent event = new PlaybackEvent(currentUser.getId(), current.getId());
            historyRepo.save(event);
            // Тут UI навіть не знає, що ми щось пишемо в базу!
        }
    }

    private void setupInitialData() {
        userRepo.save(new Administrator(0, "admin", "admin"));
        userRepo.save(new User(0, "listener", "1234"));

        Track t1 = new Track(0, "Bohemian Rhapsody", "Queen", "music/queen.mp3");
        Track t2 = new Track(0, "Smells Like Teen Spirit", "Nirvana", "music/nirvana.mp4");
        Track t3 = new Track(0, "Shape of You", "Ed Sheeran", "music/ed.mp3");
        Track t4 = new Track(0, "Believer", "Imagine Dragons", "music/believer.mp3");

        trackRepo.save(t1);
        trackRepo.save(t2);
        trackRepo.save(t3);
        trackRepo.save(t4);

        Playlist p1 = new Playlist(0, "Best Rock");
        p1.addTrack(t1);
        p1.addTrack(t2);
        p1.addTrack(t3);
        p1.addTrack(t4);
        playlistRepo.save(p1);
    }
}