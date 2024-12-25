#include "glcanvas.h"
#include <gl/gl.h>
#include <qopenglwidget.h>
#include <qwidget.h>

GLCanvas::GLCanvas(QWidget *parent) : QOpenGLWidget(parent) {
  // 初始化opengl显示组件
}

// 默认析构函数
GLCanvas::~GLCanvas() = default;

void GLCanvas::initializeGL() {
  // 初始化gl
  // 初始化opengl显卡函数接口
  initializeOpenGLFunctions();
}

void GLCanvas::paintGL() {
  // 渲染内容
  // 背景色
  glClearColor(0.23f, 0.23f, 0.23f, 1.0f);
  glClear(GL_COLOR_BUFFER_BIT);
}

void GLCanvas::resizeGL(int w, int h) {
  // 调整gl可视区域
  glViewport(0, 0, w, h);
}