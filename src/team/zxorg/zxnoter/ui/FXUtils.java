package team.zxorg.zxnoter.ui;

import de.javagl.obj.FloatTuple;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjFace;
import javafx.scene.shape.TriangleMesh;

import java.nio.FloatBuffer;

public class FXUtils {
    public static TriangleMesh getTriangleMesh(Obj obj) {
        // 创建JavaFX的TriangleMesh对象
        TriangleMesh triangleMesh = new TriangleMesh();

        for (int i = 0; i < obj.getNumVertices(); i++) {
            FloatTuple face = obj.getVertex(i);
            for (int j = 0; j < face.getDimensions(); j++) {
                triangleMesh.getPoints().addAll(face.get(j));
            }
        }

        for (int i = 0; i < obj.getNumFaces(); i++) {
            ObjFace face = obj.getFace(i);

            for (int j = 0; j < face.getNumVertices(); j++) {
                triangleMesh.getFaces().addAll(face.getVertexIndex(j));
            }
        }

        for (int i = 0; i < obj.getNumTexCoords(); i++) {
            FloatTuple face = obj.getTexCoord(i);
            for (int j = 0; j < face.getDimensions(); j++) {
                triangleMesh.getTexCoords().addAll(face.get(j));
            }
        }
        return triangleMesh;
    }
}
