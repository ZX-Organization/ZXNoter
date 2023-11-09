package team.zxorg.zxnoter.ui.main.stage.body.area.editor.threed;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import team.zxorg.zxnoter.resource.ZXResources;
import team.zxorg.zxnoter.ui.main.stage.body.EditorArea;
import team.zxorg.zxnoter.ui.main.stage.body.area.editor.base.BaseEditor;

import java.io.File;
import java.nio.file.Path;

public class ThreeDimensionalMapEditor extends BaseEditor {
    MouseEvent mouseEvent;
    Translate cameraTranslate = new Translate(0, 0, -15);
    Translate beforeCameraTranslate = new Translate(0, 0, -15);
    Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);
    Rotate beforeCameraRotateX = new Rotate(0, Rotate.X_AXIS);
    Rotate beforeCameraRotateY = new Rotate(0, Rotate.Y_AXIS);

    public ThreeDimensionalMapEditor(EditorArea editorArea) {
        super(Path.of("zxn://three"),editorArea);


        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFieldOfView(30);
        camera.getTransforms().addAll(
                cameraRotateX,
                cameraRotateY,
                cameraTranslate);


        {
            Box box = new Box(5, 5, 5);
            box.getTransforms().addAll(
                    new Rotate(0, Rotate.X_AXIS),
                    new Rotate(0, Rotate.Y_AXIS),
                    new Translate(0, 0, 4));

            PhongMaterial material = new PhongMaterial(Color.RED);
            box.setMaterial(material);


            // 创建背景图形
            ImageView imageView = new ImageView(ZXResources.LOGO);
            imageView.setFitHeight(5);
            imageView.setFitWidth(5);
            imageView.getTransforms().addAll(
                    new Rotate(0, Rotate.X_AXIS),
                    new Rotate(0, Rotate.Y_AXIS),
                    new Translate(-2.5, -2.5, 4));
        }

        Group group = new Group();

        SubScene subScene = new SubScene(group, 400, 400);
        subScene.setCamera(camera);
        subScene.setFill(Color.BLACK);

        subScene.setOnMousePressed(event -> {
            mouseEvent = event;
            beforeCameraTranslate = cameraTranslate.clone();
            beforeCameraRotateX = cameraRotateX.clone();
            beforeCameraRotateY = cameraRotateY.clone();
        });
        subScene.setOnMouseDragged(event -> {
            if (event.isShiftDown()) {
                cameraRotateX.setAngle(beforeCameraRotateX.getAngle() + (event.getY() - mouseEvent.getY()) / 10f);
                cameraRotateY.setAngle(beforeCameraRotateY.getAngle() - (event.getX() - mouseEvent.getX()) / 10f);
            } else {
                cameraTranslate.setX(beforeCameraTranslate.getX() - (event.getX() - mouseEvent.getX()) / 40f);
                cameraTranslate.setY(beforeCameraTranslate.getY() - (event.getY() - mouseEvent.getY()) / 40f);
            }
        });
        subScene.setOnScroll(event -> {
            cameraTranslate.setZ(cameraTranslate.getZ() * (event.getDeltaY() > 0 ? 0.8 : 1.2));
            System.out.println(cameraTranslate.getZ());
        });
        //group.getChildren().add(subScene);

        // 创建TriangleMesh对象并导入OBJ文件

        ObjModelImporter objModelImporter = new ObjModelImporter();
        objModelImporter.read(new File("docs/reference/3d/dcz.obj"));
        for (MeshView meshView : objModelImporter.getImport()) {
            PhongMaterial material = new PhongMaterial(Color.RED);
            meshView.setMaterial(material);
            meshView.getTransforms().addAll(
                    new Rotate(0, Rotate.X_AXIS),
                    new Rotate(0, Rotate.Y_AXIS),
                    new Translate(0, 0, 0));
            group.getChildren().add(meshView);
        }



        /*{


            TriangleMesh triangleMesh = new TriangleMesh();
            float[] vertices = {
                    0f, 0f, 0f,
                    50f, 50f, 50f,
                    0f, 50f, 0f
            };   // 11
            float[] texture = {
                    1.00f, 1.00f,        // 0
                    1.00f, 1.00f,        // 1
                    1.00f, 1.00f,
                   };        // 11
            int[] faces = {
                    0, 0, 1, 1, 2, 2};     // 19


            triangleMesh.getPoints().addAll(vertices);
            triangleMesh.getTexCoords().addAll(texture);
            triangleMesh.getFaces().addAll(faces);

            MeshView meshView = new MeshView(triangleMesh);
            PhongMaterial material = new PhongMaterial(Color.RED);
            meshView.setMaterial(material);
            meshView.getTransforms().addAll(
                    new Rotate(0, Rotate.X_AXIS),
                    new Rotate(0, Rotate.Y_AXIS),
                    new Translate(0, 0, 80));

            group.getChildren().add(meshView);
        }*/

        //setContent(subScene);
    }

    @Override
    protected void closed() {

    }

    @Override
    protected void saveFile() {

    }

}
