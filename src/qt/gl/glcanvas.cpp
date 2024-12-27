#include "glcanvas.h"

#include <fstream>
#include <gl/GL.h>
#include <gl/gl.h>
#include <qopenglext.h>
#include <qopenglwidget.h>
#include <qwidget.h>
#include <sstream>
#include <string>

GLCanvas::GLCanvas(QWidget *parent) : QOpenGLWidget(parent) {
    // 初始化opengl显示组件
}

// 默认析构函数
GLCanvas::~GLCanvas() = default;

void GLCanvas::initializeGL() {
    // 初始化gl
    // 初始化opengl显卡函数接口
    initializeOpenGLFunctions();
    // 创建着色器
    auto vshader = glCreateShader(GL_VERTEX_SHADER);
    auto fshader = glCreateShader(GL_FRAGMENT_SHADER);
    // 读取着色器源代码
    std::ifstream vsis,fsis;
    std::stringstream vsstrstream,fsstrstream;
    vsis.open("../src/qt/gl/assetes/shader/vertexshader.glsl.vert");
    fsis.open("../src/qt/gl/assetes/shader/fragmentshader.glsl.vert");
    vsstrstream << vsis.rdbuf();
    fsstrstream << fsis.rdbuf();
    std::string vssourcesstr,fssourcestr;
    vssourcesstr = vsstrstream.str();
    fssourcestr = fsstrstream.str();
    const char* vssourcecode = vssourcesstr.c_str();
    const char* fssourcecode = fssourcestr.c_str();
    // 导入源代码
    glShaderSource(vshader, 1, &vssourcecode, nullptr);
    glShaderSource(fshader, 1, &fssourcecode, nullptr);
    // 编译源代码
    glCompileShader(vshader);
    glCompileShader(fshader);

    // 生成顶点数组对象
#ifdef __APPLE__
    glGenVertexArraysAPPLE(1, &VAO);
    glBindVertexArrayAPPLE(VAO);
#else
    glGenVertexArrays(1, &VAO);
    glBindVertexArray(VAO);
#endif  //__APPLE__

    // 生成顶点缓冲对象
    glGenBuffers(1, &VBO);
    // 顶点数据
    float vertices[6] = {-0.5f, -0.5f, 0.5f, -0.5f, 0.0f, 0.5f};
    // 绑定顶点缓冲对象
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    // 上传为非动态顶点数据
    glBufferData(GL_ARRAY_BUFFER, (int)(6 * sizeof(float)), vertices,
                 GL_STATIC_DRAW);
    // 描述location0 顶点缓冲0~2float为float类型数据(用vec2接收)
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), nullptr);

    
}

void GLCanvas::paintGL() {
    // 渲染内容
    // 背景色
    glClearColor(0.23f, 0.23f, 0.23f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    // 绘制三角形(0~3顶点)
    glDrawArrays(GL_TRIANGLES, 0, 3);
}

void GLCanvas::resizeGL(int w, int h) {
    // 调整gl可视区域
    glViewport(0, 0, w, h);
}
