#include "glcanvas.h"

#include <gl/GL.h>
#include <qopenglext.h>
#include <qopenglfunctions.h>
#include <qopenglwidget.h>
#include <qwidget.h>

#include <fstream>
#include <iostream>
#include <sstream>
#include <string>

GLCanvas::GLCanvas(QWidget* parent) : QOpenGLWidget(parent) {
  // 初始化opengl显示组件
}

// 默认析构函数
GLCanvas::~GLCanvas() {
  // 绑定上下文
  makeCurrent();
  glDeleteVertexArrays(1, &VAO);
  glDeleteBuffers(1, &VBO);
  delete shader_program;
  // 解除上下文
  doneCurrent();
}

void GLCanvas::initializeGL() {
  // 初始化gl
  // 初始化opengl显卡函数接口
  initializeOpenGLFunctions();
  const GLubyte* version = glGetString(GL_VERSION);
  std::cout << "GL Version" << version << std::endl;

  // 读取着色器源代码
  std::ifstream vsis, fsis;
  std::stringstream vsstrstream, fsstrstream;
  vsis.open("../src/qt/gl/assetes/shader/vertexshader.glsl.vert");
  fsis.open("../src/qt/gl/assetes/shader/fragmentshader.glsl.frag");
  vsstrstream << vsis.rdbuf();
  fsstrstream << fsis.rdbuf();
  std::string vssourcesstr, fssourcestr;
  vssourcesstr = vsstrstream.str();
  fssourcestr = fsstrstream.str();
  const char* vssourcecode = vssourcesstr.c_str();
  const char* fssourcecode = fssourcestr.c_str();

  // 创建并编译着色器
  shader_program = new QOpenGLShaderProgram(this);
  if (!shader_program->addShaderFromSourceCode(QOpenGLShader::Vertex,
                                               vssourcecode)) {
    qFatal("Vertex shader compilation failed: %s",
           qPrintable(shader_program->log()));
  }
  if (!shader_program->addShaderFromSourceCode(QOpenGLShader::Fragment,
                                               fssourcecode)) {
    qFatal("Fragment shader compilation failed: %s",
           qPrintable(shader_program->log()));
  }

  // 链接着色器程序
  if (!shader_program->link()) {
    qFatal("Shader program linking failed: %s",
           qPrintable(shader_program->log()));
  }

  // 生成顶点数组对象
  glGenVertexArrays(1, &VAO);
  glBindVertexArray(VAO);

  // 生成顶点缓冲对象
  glGenBuffers(1, &VBO);
  // 绑定顶点缓冲对象
  glBindBuffer(GL_ARRAY_BUFFER, VBO);

  // 顶点数据
  float vertices[6] = {-0.5f, -0.5f, 0.5f, -0.5f, 0.0f, 0.5f};
  // 上传为非动态顶点数据
  glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);
  // 描述location0 顶点缓冲0~2float为float类型数据(用vec2接收)
  glEnableVertexAttribArray(0);
  glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), nullptr);
}

void GLCanvas::paintGL() {
  // 渲染内容
  // 背景色
  glClearColor(0.23f, 0.23f, 0.23f, 1.0f);
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  // 使用着色器程序
  shader_program->bind();

  // 绑定顶点数组对象
  glBindVertexArray(VAO);
  // 绘制三角形(0~3顶点)
  glDrawArrays(GL_TRIANGLES, 0, 3);

  // 解绑
  glBindVertexArray(0);
  shader_program->release();
}

void GLCanvas::resizeGL(int w, int h) {
  // 调整gl可视区域
  glViewport(0, 0, w, h);
}

// 鼠标按钮事件重写
void GLCanvas::mousePressEvent(QMouseEvent* event) {}
void GLCanvas::mouseReleaseEvent(QMouseEvent* event) {}
// 鼠标移动事件重写
void GLCanvas::mouseMoveEvent(QMouseEvent* event) {}
// 鼠标滚轮事件重写
void GLCanvas::wheelEvent(QWheelEvent* event) {}
