package team.zxorg.zxnoter.resource;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import team.zxorg.zxnoter.resource.pack.EditorResourcePack;
import team.zxorg.zxnoter.resource.pack.IconResourcePack;
import team.zxorg.zxnoter.resource.pack.StyleResourcePack;
import team.zxorg.zxnoter.resource.type.IconResource;
import team.zxorg.zxnoter.resource.type.LanguageResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ResourcePack {
    public String name;
    public String id;
    public String authors;
    public String describe = "no describe.";
    public Path packPath;
    ArrayList<IconResource> iconResources = new ArrayList<>();
    ArrayList<EditorResourcePack> editorResourcePacks = new ArrayList<>();
    ArrayList<StyleResourcePack> styleResourcePacks = new ArrayList<>();
    ArrayList<LanguageResource> languageResources = new ArrayList<>();

    @Override
    public String toString() {
        return "资源包{" +
                "名称:'" + name + '\'' +
                ", id:'" + id + '\'' +
                ", 作者:'" + authors + '\'' +
                ", 描述:'" + describe + '\'' +
                '}';
    }

    public ResourcePack(Path resourcePackPath) {
        packPath = resourcePackPath;
        try {
            JSONObject packInfo = JSON.parseObject(Files.newInputStream(resourcePackPath.resolve("resource-pack.json")));
            name = packInfo.getString("name");
            id = packInfo.getString("id");
            if (name == null || id == null) {
                throw new RuntimeException("Invalid name or id");
            }

            authors = (authors != null) ? authors : "";
            describe = (describe != null) ? describe : "";
        } catch (IOException e) {
            System.err.println("资源包读取异常");
            throw new RuntimeException(e);
        }
    }
}
