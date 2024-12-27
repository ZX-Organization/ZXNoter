#ifndef ZXNOTER_GLCANVAS_H
#define ZXNOTER_GLCANVAS_H

// opengl组件相关头文件
#include <qopenglfunctions_4_1_core.h>
#include <qopenglshaderprogram.h>
#include <qopenglwidget.h>

class GLCanvas : public QOpenGLWidget, QOpenGLFunctions_4_1_Core {
    Q_OBJECT
   public:
    // 构造&析构
    GLCanvas(QWidget *parent = nullptr);
    virtual ~GLCanvas();

   protected:
    // 顶点数组对象,顶点缓冲对象,帧缓冲对象,Uniform缓冲对象
    uint32_t VAO, VBO, FBO, UBO;
    // 初始化gl环境
    void initializeGL() override;
    // 渲染内容
    void paintGL() override;
    // 调整大小
    void resizeGL(int w, int h) override;
};

#endif  // ZXNOTER_GLCANVAS_H
