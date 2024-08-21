package team.zxorg.zxnoter.info.mc;

public class McMap {
    public McMeta meta;
    public McTime[] time;
    public McNote[] note;


    public static class McNote {
        public int[] beat;
        public Integer x;
        public int w;
        public McNote[] seg;
    }

    public static class McTime {
        public int[] beat;
        public float bpm;
        public float delay;
    }

    public static class McMeta {
        public int id;
        public String creator;
        public String background;
        public String cover;
        public String version;
        public int preview;
        public int mode;
        public McSong song;

        public static class McSong {
            public int id;
            public String title;
            public String artist;
            public String file;
            public float bpm;
        }
    }


}
