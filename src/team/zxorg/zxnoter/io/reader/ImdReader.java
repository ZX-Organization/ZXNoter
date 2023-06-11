package team.zxorg.zxnoter.io.reader;

import team.zxorg.zxnoter.map.LocalizedMapInfo;
import team.zxorg.zxnoter.map.ZXMap;
import team.zxorg.zxnoter.note.Timing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ImdReader implements LocalizedMapInfo.UnLocalizing {
    public static ZXMap readFile(Path path) throws IOException {

        InputStream inputStream= Files.newInputStream(path);
        byte[] data = inputStream.readAllBytes();
        ByteBuffer bf=ByteBuffer.wrap(data);
        inputStream.close();
        bf.order(ByteOrder.LITTLE_ENDIAN);

        LocalizedMapInfo localizedMapInfo = new LocalizedMapInfo();


        //图长度
        bf.getInt();

        //图时间点数
        int timingAmount = bf.getInt();
        ArrayList<Timing> timingPoints = new ArrayList<>(timingAmount);

        //读取首时间点bpm作为基准bpm
        //从第13字节读取double
        bf.position(12);
        double baseBpm = bf.getDouble();

        //跳回首timingPoint处
        bf.position(8);
        for (int i = 0; i < timingAmount; i++) {
            timingPoints.add(
                new Timing(
                        bf.getInt(), bf.getDouble() / baseBpm
                )
            );
        }

        //03 03
        bf.getShort();

        //表格行数
        bf.getInt();


        return new ZXMap();
    }

    public static void main(String[] args) {
        try {
            readFile(Path.of("docs/reference/Corruption/Corruption_4k_ez.imd"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String unLocalize(String name) {
        return null;
    }
}
