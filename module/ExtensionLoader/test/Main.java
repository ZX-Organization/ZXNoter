import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import team.zxorg.info.ExtensionInfo;

public class Main {
    public static void main(String[] args) {
        ExtensionInfo extensionInfo = new ExtensionInfo();
        //extensionInfo.authors = new ArrayList<>();
        extensionInfo.id = "nmsl";
        extensionInfo.version = "123.321.123";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json =gson.toJson(extensionInfo);

        ExtensionInfo newExtensionInfo = gson.fromJson(json, ExtensionInfo.class);
        System.out.println(newExtensionInfo.authors);
    }
}
