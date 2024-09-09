package ikovsky;

public class SongResponse {
    private String id;
    private String songName;
    private String midiString;

    public SongResponse(
            String id,
            String songName,
            String midiString
    ) {
        this.id = id;
        this.songName = songName;
        this.midiString = midiString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getMidiString() {
        return midiString;
    }

    public void setMidiString(String midiString) {
        this.midiString = midiString;
    }
}
