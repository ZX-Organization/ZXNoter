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
  // 着色器程序
  QOpenGLShaderProgram *shader_program;
  // 顶点数组对象,顶点缓冲对象,帧缓冲对象,Uniform缓冲对象
  uint32_t VAO, VBO, FBO, UBO;
  // 初始化gl环境
  void initializeGL() override;
  // 渲染内容
  void paintGL() override;
  // 调整大小
  void resizeGL(int w, int h) override;

  // 鼠标按钮事件重写
  void mousePressEvent(QMouseEvent *event) override;
  void mouseReleaseEvent(QMouseEvent *event) override;
  // 鼠标移动事件重写
  void mouseMoveEvent(QMouseEvent *event) override;
  // 鼠标滚轮事件重写
  void wheelEvent(QWheelEvent *event) override;
};

#endif  // ZXNOTER_GLCANVAS_H
