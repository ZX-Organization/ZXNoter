package team.zxorg.mapeditcore.io.writer;

import team.zxorg.mapeditcore.map.ZXMap;
import team.zxorg.mapeditcore.map.mapdata.ZXMapData;
import team.zxorg.mapeditcore.map.mapdata.datas.ImdMapData;
import team.zxorg.mapeditcore.map.mapdata.datas.OsuMapData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class OsuWriter extends MapWriter {

    @Override
    public void writeFile(File file) throws IOException {
        OsuMapData osuMapData = null;
        ZXMapData zxMapData = null;
        if (map.metaData instanceof ImdMapData data) {
            zxMapData = data.toZxMeta();
        } else if (map.metaData instanceof ZXMapData data)
            if (data instanceof OsuMapData osuData)
                osuMapData = osuData;
            else zxMapData = data;
        else osuMapData = new OsuMapData();
        if (zxMapData !=null){
            osuMapData = new OsuMapData(zxMapData);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        writer.write("osu file format v14");
        writer.newLine();

        writer.newLine();
        writer.write("[General]");
        writer.newLine();
        Set<String> geKeys = osuMapData.getGeneralInfo().keySet();
        for (String key : geKeys) {
            writer.write(key + ":" + osuMapData.getGeneralInfo().get(key));
            writer.newLine();
        }

        writer.newLine();
        writer.write("[Editor]");
        writer.newLine();
        Set<String> EdKeys = osuMapData.getEditorInfo().keySet();
        for (String key : EdKeys) {
            writer.write(key + ":" + osuMapData.getEditorInfo().get(key));
            writer.newLine();
        }

        writer.newLine();
        writer.write("[Metadata]");
        writer.newLine();
        writer.write("Title:" + osuMapData.getTitle());
        writer.newLine();
        writer.write("TitleUnicode:" + osuMapData.getTitleUnicode());
        writer.newLine();
        writer.write("Artist:" + osuMapData.getArtist());
        writer.newLine();
        writer.write("ArtistUnicode:" + osuMapData.getArtistUnicode());
        writer.newLine();
        writer.write("Creator:" + osuMapData.getCreator());
        writer.newLine();
        writer.write("Version:" + osuMapData.getMapVersion());
        writer.newLine();
        writer.write("Source:" + osuMapData.getSource());
        writer.newLine();
        writer.write("Tags:" + osuMapData.getTags());
        writer.newLine();
        writer.write("BeatmapId:" + osuMapData.getBeatMapId());
        writer.newLine();
        writer.write("BeatmapSetId:" + osuMapData.getBeatMapSetId());
        writer.newLine();

        writer.newLine();
        writer.write("[Difficulty]");
        writer.newLine();
        Set<String> DiKeys = osuMapData.getDifficultInfo().keySet();
        for (String key : DiKeys) {
            writer.write(key + ":" + osuMapData.getDifficultInfo().get(key));
            writer.newLine();
        }

        writer.newLine();
        writer.write("[Event]");
        writer.newLine();
        Set<String> EvKeys = osuMapData.getEventInfo().keySet();
        int index=0;
        for (String key : EvKeys) {
            writer.write("//" + OsuMapData.eventDefaultInfo[index++]);
            writer.newLine();
            String value = osuMapData.getEventInfo().get(key);
            if (value == null || "".equals(value)) continue;
            else writer.write(value);
            writer.newLine();
        }



        writer.flush();
        writer.close();
    }

    public OsuWriter(ZXMap map) {
        super(map);
    }

    @Override
    public String getSuffix() {
        return ".osu";
    }
}
